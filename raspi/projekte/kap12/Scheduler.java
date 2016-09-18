package raspi.projekte.kap12;

import raspi.logger.DataLogger;
import raspi.hardware.spi.MCP3008;
import raspi.schedule.*;
import java.text.ParseException;
import java.io.IOException;  
import java.util.Date;

/**
 * Write a description of class Scheduler here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Scheduler extends Thread
{
    final DataLogger loggerTag;
    final DataLogger loggerJahr;
    private String messenTag;
    private String speichernLoggerJahr;
    public static final String TAG = "Tag";
    public static final String MAX = "   Max";
    public static final String MIN = "   Min";
    private double min = 3.3;
    private double max = 0.0;
    private double fuellmenge = 0;
    private Sensor sensor;

    /**
     * Konstruktor der Klasse Scheduler
     */
    public Scheduler(DataLogger loggerTag, DataLogger loggerJahr){
        this.loggerTag = loggerTag;
        this.loggerJahr = loggerJahr;
        sensor = new Sensor();
    }

    @Override
    public void run(){
        try{
            // Die Messung, die alle 30 min ausgeführt wird, beginnt sofort.
            messenTag = ScheduleUtil.startInMinutes("0", "10");
            // Speichern jede Nacht 00:00 Uhr bis 00:01 Uhr 
            speichernLoggerJahr = ScheduleUtil.startFromToAllDays("000000", "000100");
            //speichernLoggerJahr = ScheduleUtil.startFromToAllDays("084800", "084900");
            while(!Thread.interrupted()){
                try{
                    if(ScheduleService.scheduleCheckForLongTimeUse(messenTag)){ 
                        fuellmenge = sensor.getFuellmenge();
                        if(fuellmenge < min){
                            min = fuellmenge;
                        }    
                        if(fuellmenge > max){
                            max = fuellmenge;
                        }    
                        loggerTag.add(fuellmenge, TAG);
                        //System.out.printf("%2$tY.%2$tm.%2$td %2$tH:%2$tM:%2$tS:%2$tL %1$s%n", fuellmenge, new Date());
                        messenTag = ScheduleUtil.startInMinutes("2", "10");
                    }
                    if(ScheduleService.scheduleCheckForDalyUse(speichernLoggerJahr)){
                        loggerJahr.add(max,MAX);
                        loggerJahr.add(min,MIN);
                        loggerJahr.storeFlush();
                        // 2 Minuten warten, damit in der Nacht nur einmal gespeichert wird.
                        Thread.sleep(120000);
                        min = 10000.0;
                        max = 0.0;
                    }
                    Thread.sleep(10000); //10 Sekunden
                }catch(InterruptedException ex){
                    loggerJahr.storeFlush();
                    break;
                } 
            }
        }catch(ParseException ex){
            loggerJahr.storeFlush();
        }  
    }

    /*public static void main(String[] args) throws IOException{
        final DataLogger loggerTag;
        final DataLogger loggerJahr;
        loggerTag = new DataLogger(null,"LoggerZisterneTag.txt", 50);
        loggerJahr = new DataLogger(null,"LoggerZisterneJahr.txt", 370);

        final Scheduler scheduler = new Scheduler(loggerTag, loggerJahr);
        scheduler.start();
    }*/
}

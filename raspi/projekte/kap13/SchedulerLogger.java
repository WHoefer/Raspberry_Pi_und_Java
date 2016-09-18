package raspi.projekte.kap13;

import raspi.schedule.Scheduler;
import raspi.logger.DataLogger;
/**
 * Die Klasse SchedulerLogger loggt die Temperatur. Es werden aber keine Messwerte abgespeichert.
 * 
 * @author Wolfgang Höfer
 * @version 1.0
 */
public class SchedulerLogger  extends Scheduler
{
    private DataLogger dataLogger;
    private Sensor sensor;
    private String info;
    
    /**
     * Konstruktor für die Klasse SchedulerLogger
     */
    public SchedulerLogger(long loop,  Sensor sensor, DataLogger dataLogger, String info)
    {
       super(loop);
       this.dataLogger = dataLogger;
       this.sensor = sensor;
       this.info = info;
    }

    @Override
    public synchronized void executeActive(){
        dataLogger.add(sensor.getTemperatur(), info);
    }
    
    @Override
    public synchronized void executePassive(){
        dataLogger.add(sensor.getTemperatur(), info);
    }

}

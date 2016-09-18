package raspi.projekte.kap13;

import raspi.schedule.Scheduler;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
/**
 * Die Klasse SchedulerTemperatur bildet eine Zweipunktregelung nach, die 
 * die Temperatur im Tag- und Nachtbetrieb mit einer Hysterese von 1°C 
 * regelt.
 * 
 * @author Wolfgang Höfer
 * @version 1.0
 */
public class SchedulerTemperatur extends Scheduler
{

    private double tempTag;
    private double tempNacht;
    private double tempDiv;
    private Sensor sensor;
    private GpioPinDigitalOutput relais;
    
    
    /**
     * Konstruktor der Klasse SchedulerTemperatur
     */
    public SchedulerTemperatur(long loop, Sensor sensor, GpioPinDigitalOutput relais)
    {
       super(loop);
       this.sensor = sensor;
       tempTag = 20.0d;
       tempNacht = 16.0d;
       tempDiv = 0.5d;
       this.relais = relais;
    }

    /**
     * setTagNacht setzt die Temperaturen für den Tag- und den Nachtbetrieb.
     * Die Parameter nehmen die Temperaturen in °C entgegen.
     *
     * @param tempTag Temperatur in °C im Tagbetrieb
     * @param tempNacht Temperatur in °C im Nachtbetrieb
     */
    public synchronized void setTagNacht(double tempTag, double tempNacht){
        this.tempTag = tempTag;
        this.tempNacht = tempNacht;
    }
    
    public synchronized double getTag(){
        return tempTag;
    }

    public synchronized double getNacht(){
        return tempNacht;
    }
    
    
    @Override
    public synchronized void executeActive(){
        sensor.readTemperatur();
        if(sensor.getTemperatur() > (tempTag+tempDiv)){
            relais.low();
        }
        if(sensor.getTemperatur() < (tempTag-tempDiv)){
            relais.high();
        }    
    }
    
    @Override
    public synchronized void executePassive(){
        sensor.readTemperatur();
        if(sensor.getTemperatur() > (tempNacht+tempDiv)){
            relais.low();
        }
        if(sensor.getTemperatur() < (tempNacht-tempDiv)){
            relais.high();
        }    
    }

    @Override
    public synchronized void executeDisabled(){
        sensor.readTemperatur();
        relais.low();
    }

}

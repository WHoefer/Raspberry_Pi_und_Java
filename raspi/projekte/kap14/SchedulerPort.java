package raspi.projekte.kap14;

import raspi.schedule.Scheduler;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
/**
 * Die Klasse SchedulerPort übernimmt die Zeitsteuerung für 
 * einen GPIO-Port.
 * 
 * @author Wolfgang Höfer
 * @version 1.0
 */
public class SchedulerPort  extends Scheduler
{
    private GpioPinDigitalOutput port;

    /**
     * Konstruktor der Klasse SchedulerRelais
     */
    public SchedulerPort(long loop,GpioPinDigitalOutput port)
    {
        super(loop);
        this.port = port;
    }

    @Override
    public synchronized void executeActive(){
        port.high();
    }

    @Override
    public synchronized void executePassive(){
        port.low();
    }

    @Override
    public synchronized void executeDisabled(){
        port.low();
    }

}

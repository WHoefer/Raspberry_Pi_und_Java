package raspi.projekte.kap12;

import raspi.hardware.spi.MCP3002;
import raspi.hardware.GP2Distance20To150;
import raspi.Tank;


/**
 * Die Klasse Sensor liefert die Füllmenge einer Zisterne, die die Form
 * eines liegenden Zylinders hat.
 * 
 * @author Wolfgang Höfer
 * @version 1.0
 */
public class Sensor
{
    MCP3002 adc;
    GP2Distance20To150 distanz;
    /**
     * FROM_GROUND_TO_SENSOR
     * Der Senso wurde 1,5m über dem Zisternenboden montiert.
     */
    private static final double FROM_GROUND_TO_SENSOR = 1.5d;
    /**
     * RADIUS
     * Der Radius der Zisterne beträgt 0,65m.
     */
    private static final double RADIUS = 0.65d;
    /**
     * LAENGE
     * Die Länge der Zisterne beträgt 2,1m.
     */
    private static final double LAENGE = 2.1d;

    
    private double alt = 0.0d;
    /**
     * Konstruktor der Klasse Sensor
     */
    public Sensor()
    {
        adc = new MCP3002(MCP3002.CS0, MCP3002.CLOCK1M);
        distanz = new GP2Distance20To150(); 
    }

    /**
     * getFuellmenge liefert die Füllmenge der Zsiterne in Liter.
     * 
     *
     * @return Füllmenge
     */
    public double getFuellmenge(){
        double u = adc.readChannelInVolt(0);
        double l = distanz.getValue(u);
        if(l == 0.0d){
            l = alt;
        }
        alt = l;
        l = l/100d;
        double f = Tank.getVolHorCyl(l, RADIUS, LAENGE, FROM_GROUND_TO_SENSOR);
        return f;
    }

    
}

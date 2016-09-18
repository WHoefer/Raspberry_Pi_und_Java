package raspi.projekte.kap13;

import raspi.hardware.spi.MCP3002;

/**
 * Die Klasse Sensor liefert die aktueelle Temperatur in °C.
 * 
 * @author Wolfgang Höfer
 * @version 1.0
 */
public class Sensor
{
    private MCP3002 adc;
    private double temperatur;

    /**
     * Konstruktor der Klasse Sensor
     */
    public Sensor()
    {
        adc = new MCP3002(MCP3002.CS0, MCP3002.CLOCK1M);
        readTemperatur();
    }

    /**
     * getTemperatur liefert die aktuelle Temperatur in °C.
     *
     * @return Temperatur in °C
     */
    public synchronized double getTemperatur(){
        return temperatur;
    }
    
    /**
     * readTemperatur fragt den ADC ab und berechnet die 
     * Temperatur in °C.
     *     */
    public synchronized  void readTemperatur(){
        double u = adc.readChannelInVolt(0);
        temperatur = (3.396-u)*11.747;
    }

    public static void main(String[] args)throws Exception{

        Sensor sensor = new Sensor();
        while(true){
            Thread.sleep(1000);
            System.out.println(sensor.getTemperatur());

        }
    }
}

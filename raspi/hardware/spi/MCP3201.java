package raspi.hardware.spi;

import com.pi4j.wiringpi.Spi;
/**
 * Treiberklasse für MCP3201. Nicht getestet.
 * 
 * @author Wolfgang Höfer
 * @version 0.0
 */
public class MCP3201
{
    public static final int CS0 = 0;
    public static final int CS1 = 1;
    public static final int CLOCK1M = 1000000;
    private int cs = 0; 

    /**
     * MCP3201 Constructor
     *
     * @param cs Chip Select kann 0 oder 1 sein
     * @param clock Busfrequenz in Hz
     */
    public MCP3201(int cs, int clock)
    {
        this.cs = cs;
        if (Spi.wiringPiSPISetup(cs, clock) <= -1) {
            System.out.println("Fehler: SPI-Bus konnte nicht initialisiert werden!");
        }
    }
    /**
     * Method readChannel<br>
     * 
     * Liest den gewandelten Wert.
     *
     * @return Gewandelte Wert
     */
    public int readChannel(){
        byte[] command = new byte[2];
        int value;
        
        command[0] = (byte) 0b0000_0000; 
        command[1] = (byte) 0b0000_0000; 
        Spi.wiringPiSPIDataRW(0, command, 2); 
        int intMSB = (command[1] & 0x1f) << 7;
        int intLSB = (command[2] & 0xfe) >> 1;
        value =  (intMSB + intLSB) & 0xfff;
        return value;


    }

    public double readChannelInVolt(){
        double u = readChannel();
        return (3.3d * u) / 4069.0d;

    }

}

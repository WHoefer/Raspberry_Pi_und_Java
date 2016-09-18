package raspi.hardware.spi;

import com.pi4j.wiringpi.Spi;
/**
 * Treiberklasse für MCP3008. Nicht getestet.
 * 
 * @author Wolfgang Höfer
 * @version 0.0
 */
public class MCP3008
{
    public static final int CS0 = 0;
    public static final int CS1 = 1;
    public static final int CLOCK1M = 1000000;
    private int cs = 0; 

    /**
     * MCP3008 Constructor
     *
     * @param cs Chip Select kann 0 oder 1 sein
     * @param clock Busfrequenz in Hz
     */
    public MCP3008(int cs, int clock)
    {
        this.cs = cs;
        if (Spi.wiringPiSPISetup(cs, clock) <= -1) {
            System.out.println("Fehler: SPI-Bus konnte nicht initialisiert werden!");
        }
    }
    /**
     * Method readChannel<br>
     * 
     * Liest den gewandelten Wert von Kanal 0 bis 7.
     *
     * @param channel 0 bis 7 für Kanal 0 bis 7
     * @return Gewandelte Wert
     */
    public int readChannel(int channel){
        byte[] command = new byte[3];
        int value;
        
        command[0] = (byte) 0b0000_0001; //Startbit
        command[1] = (byte)(8 + channel << 4);
        command[2] = (byte) 0b0000_0000; //Wird nur für den Rückgabewerte benötigt
        Spi.wiringPiSPIDataRW(0, command, 3); 
        int intMSB = (command[1] & 0x03) << 8;
        int intLSB = command[2] & 0xff;
        value =  (intMSB + intLSB) & 0x3ff;
        return value;


    }

    public double readChannelInVolt(int channel){
        double u = readChannel(channel);
        return (3.3d * u) / 1023.0d;

    }

}

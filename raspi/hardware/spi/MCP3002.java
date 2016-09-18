package raspi.hardware.spi;


import com.pi4j.wiringpi.Spi;

/**
 * Klasse zum Ansteueren des ADC MCP3002 über den SPI-Bus.
 * 
 * @author Wolfgang Höfer
 * @version 1.0
 */
public class MCP3002
{
    public static final int CS0 = 0;
    public static final int CS1 = 1;
    public static final int CLOCK1M = 1000000;
    private int cs = 0; 
    
    /**
     * MCP3002 Constructor
     *
     * @param cs Chip Select kann 0 oder 1 sein
     * @param clock Busfrequenz in Hz
     */
    public MCP3002(int cs, int clock)
    {
        this.cs = cs;
        if (Spi.wiringPiSPISetup(cs, clock) <= -1) {
            System.out.println("Fehler: SPI-Bus konnte nicht initialisiert werden!");
        }
    }
    /**
     * Method readChannel<br>
     * 
     * Liest den gewandelten Wert von Kanal 0 oder 1.
     *
     * @param channel 0 für Kanal 0 und 1 für Kanal 1
     * @return Gewandelter Wert von Kanal 0 oder 1
     */
    public int readChannel(int channel){
        byte[] command = new byte[2];
        int value;
        if(channel == 0){
            command[0] = (byte) 0b1101_0000;
        }else{    
            command[0] = (byte) 0b1111_0000;
        }    
        command[1] = 0b00; 
        Spi.wiringPiSPIDataRW(cs, command, 2); 
        int intMSB = (command[0] << 7) & 0x380;
        int intLSB = (command[1] >> 1) & 0x7f;
        value =  (intMSB + intLSB) & 0x3ff;
        return value;

    }

    public double readChannelInVolt(int channel){
        double u = readChannel(channel);
        return (3.3d * u) / 1023.0d;

    }

}

package raspi.hardware.lcd;


/**
 * PinInterface ist ein Interface für die Klasse
 * DisplayHD44780
 * 
 * @author Wolfgang Höfer
 * @version 1.0
 */
public interface PinInterface
{
    
    public void setDataLSB(int value);
    public void setDataMSB(int value);
    public void setData(int value);
    public void enableCommand();
    public void setRS(boolean value);
    public void setEnable(boolean value);
    public void shutdown();
    public boolean is8BitMode();
    public boolean is5x10Mode();
    public boolean isMultilineMode();
}

package raspi.projekte.kap18;

import com.pi4j.io.gpio.*;
import raspi.hardware.lcd.DisplayHD44780;
import raspi.hardware.lcd.PinDirect;
import raspi.hardware.lcd.PinInterface;

public class Test8Bit
{
    GpioController gpio = GpioFactory.getInstance();
    public static void main(String[] args)
    {
        Pin[] dataPins = {RaspiPin.GPIO_00, RaspiPin.GPIO_01, RaspiPin.GPIO_02, RaspiPin.GPIO_03,
                RaspiPin.GPIO_04, RaspiPin.GPIO_05, RaspiPin.GPIO_06, RaspiPin.GPIO_07};
        PinInterface pinDirect = new PinDirect(true, false, true, RaspiPin.GPIO_10, RaspiPin.GPIO_11, dataPins );

        DisplayHD44780 display  = new DisplayHD44780(pinDirect);
        TestDisplay.test(display);
    }
}

package raspi.projekte.kap06;

import raspi.process.Shell;
import raspi.listener.ShutdownListener;
import raspi.listener.VNCStartListener;
import raspi.listener.VNCStopListener;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.GpioPin;
import java.util.Calendar;

/**
 * Wenn GPIO 6 länger als 3 sec Low-Pegel erhält, wird der Raspberry Pi ausgeschaltet.
 * Eine Pegeländerung auf GPIO 5 startet den VNC-Server.
 * Eine Pegeländerung auf GPIO 4 stoppt den VNC-Server.
 * 
 * @author Wolfgang Höfer
 * @version 1.0
 */
public class RaspiSteuern
{

    public static void main(String[] args) throws InterruptedException{
        final GpioController gpio = GpioFactory.getInstance();
        final GpioPinDigitalInput button1 = gpio.provisionDigitalInputPin(RaspiPin.GPIO_06,PinPullResistance.PULL_UP);
        final GpioPinDigitalInput button2 = gpio.provisionDigitalInputPin(RaspiPin.GPIO_05,PinPullResistance.PULL_UP);
        final GpioPinDigitalInput button3 = gpio.provisionDigitalInputPin(RaspiPin.GPIO_04,PinPullResistance.PULL_UP);

        button1.addListener(new ShutdownListener(null));        
        button2.addListener(new VNCStartListener("1280","800"));        
        button3.addListener(new VNCStopListener());        

        while (true) {
            Thread.sleep(500);
        }
    }
}

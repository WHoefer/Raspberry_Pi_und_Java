package raspi.listener;

import raspi.process.Shell;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.GpioPin;

/**
 * VNC-Server starten
 * 
 * @author Wolfgang Höfer
 * @version Version 1.0
 */
public class VNCStartListener  implements GpioPinListenerDigital 
{
    String w;
    String h;

    public VNCStartListener(String w, String h){
        this.w = w;
        this.h = h;
    }    

    @Override
    public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {

        GpioPin pin = event.getPin();
        PinState state = event.getState();
        if(state.isHigh()){   
            Shell.executeAndWaitFor("vncserver :1 -geometry " + w +"x" + h +" -depth 16 -dpi 96");
            Shell.execute("chmod a+xwr /home/pi/.Xauthority");
        }
    }

}

package raspi.listener;

import raspi.process.Shell;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.GpioPin;

/**
 * VNC-Server stoppen
 * 
 * @author Wolfgang Höfer
 * @version 1.0
 */
public class VNCStopListener implements GpioPinListenerDigital 
{

    public VNCStopListener(){
    }    

    @Override
    public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {

        GpioPin pin = event.getPin();
        PinState state = event.getState();
        if(state.isHigh()){   
            Shell.execute("vncserver -kill :1");
        }
    }

}
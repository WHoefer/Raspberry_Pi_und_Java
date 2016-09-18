package raspi.listener;

import raspi.process.Shell;
import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.*;
/*import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.GpioPin;*/
import java.util.Calendar;

/**
 * Raspberry Pi ausschalten.
 * 
 * @author Wolfgang Höfer
 * @version 1.0
 */
public class ShutdownListener implements GpioPinListenerDigital {

    boolean check = true;
    long  start = 0;
    long  stopp = 0;
    long  div = 0;

    Object obj = null;

    public ShutdownListener(Object obj){
        this.obj = obj;
    }    

    @Override
    public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {

        GpioPin pin = event.getPin();
        String name = pin.getName();
        PinState state = event.getState();

        if(state.isLow()){
            start = Calendar.getInstance().getTimeInMillis();
        }else if(state.isHigh()){  
            stopp = Calendar.getInstance().getTimeInMillis(); 
            div = (stopp - start) / 1000l;
            if(div > 3){
                beforeShutdown(obj);
                Shell.execute("shutdown -h now");
            }
        }  
    }

    
    public void setObject(Object obj){
        this.obj = obj;
    }
    
    public void beforeShutdown(Object obj){
    }    
}  


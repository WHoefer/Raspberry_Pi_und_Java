package raspi.projekte.kap06;

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.*;

/**
 * Test für digitale Sensoren
 * 
 * @author Wolfgang Höfer 
 * @version 1.0
 */
public class SchmittTrigger
{

    public static class SensorListener implements GpioPinListenerDigital {
        @Override
        public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
            GpioPin pin = event.getPin();
            String name = pin.getName();
            PinState state = event.getState();

            if(state.isLow() && name.equals("LDR")){
                System.out.println("Licht ein!");
            }else if(state.isHigh() && name.equals("LDR")){ 
                System.out.println("Licht aus!");
            }else if(state.isLow() && name.equals("LS")){
                System.out.println("Lichtschranke zu!");
            }else if(state.isHigh() && name.equals("LS")){ 
                System.out.println("Lichtschranke offen!");
            } 
        }
    }    

    public static void main(String[] args) throws InterruptedException{
        final GpioController gpio = GpioFactory.getInstance();
        final GpioPinDigitalInput ldr = gpio.provisionDigitalInputPin(RaspiPin.GPIO_01,"LDR", PinPullResistance.OFF);
        final GpioPinDigitalInput lichtschranke = gpio.provisionDigitalInputPin(RaspiPin.GPIO_02,"LS",PinPullResistance.OFF);

        SensorListener sensorListener = new SensorListener();
        ldr.addListener(sensorListener);
        lichtschranke.addListener(sensorListener);

        while(true){
            Thread.sleep(10000);
        }    

    }

}

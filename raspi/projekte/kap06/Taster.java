package raspi.projekte.kap06;

import raspi.schedule.ScheduleUtil;
import raspi.schedule.ScheduleService;
import java.text.ParseException;
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

/**
 * Low-Pegel auf GPIO 6 aktiviert GPIO 1 für 2 Minuten.
 * Low-Pegel auf GPIO 5 aktiviert GPIO 2 für 1 Minute mit einer Pulsdauer von 2 Sekunden.
 * Low-Pegel auf GPIO 4 beendet das Propgramm.
 * 
 * @author Wolfgang Höfer
 * @version Version 1.0
 */
public class Taster
{

    static String commandRelais1 = ScheduleUtil.allwaysOff();
    static String commandRelais2 = ScheduleUtil.allwaysOff();
    static boolean loop = true;

    public static class ButtonListener implements GpioPinListenerDigital {
        @Override
        public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {

            GpioPin pin = event.getPin();
            String name = pin.getName();
            PinState state = event.getState();

            if(state.isLow() && name.equals("GPIO 6")){
                commandRelais1 = ScheduleUtil.activeForMinutes("2");
            }else if(state.isLow() && name.equals("GPIO 5")){
                commandRelais2 = ScheduleUtil.startPulseInMinutes(0, 1, 2);
            }else if(state.isLow() && name.equals("GPIO 4")){
                loop = false;
            }    
        }
    }    

    public static void main(String[] args) throws ParseException, InterruptedException{
        /* Ausgänge konfiguriren */
        final GpioController gpio = GpioFactory.getInstance();
        final GpioPinDigitalOutput relais1 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01);
        final GpioPinDigitalOutput relais2 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02);
        //final GpioPinDigitalOutput relais3 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03);
        relais1.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
        relais2.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
        //relais3.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
        /* Eingänge konfigurieren */
        final GpioPinDigitalInput button1 = gpio.provisionDigitalInputPin(RaspiPin.GPIO_04,PinPullResistance.PULL_UP);
        final GpioPinDigitalInput button2 = gpio.provisionDigitalInputPin(RaspiPin.GPIO_05,PinPullResistance.PULL_UP);
        final GpioPinDigitalInput button3 = gpio.provisionDigitalInputPin(RaspiPin.GPIO_06,PinPullResistance.PULL_UP);
        /* Listener erzeugen */
        ButtonListener gpl = new ButtonListener();
        /* Listener für alle drei Eingänge registrieren */
        button1.addListener(gpl);        
        button2.addListener(gpl);        
        button3.addListener(gpl);        

        while (loop) {
            if(ScheduleService.scheduleCheckForLongTimeUse(commandRelais1)){
                relais1.high();
            }else{
                relais1.low();
            }    
            if(ScheduleService.scheduleCheckForLongTimeUse(commandRelais2)){
                relais2.high();
            }else{
                relais2.low();
            }    
            Thread.sleep(500);
        }
        gpio.shutdown();
        System.out.println("Exit");
    }
}

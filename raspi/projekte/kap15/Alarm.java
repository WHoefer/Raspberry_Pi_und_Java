package raspi.projekte.kap15;

import raspi.schedule.*;
import raspi.mail.MailService;
import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.*;
import java.text.ParseException;
import java.util.Properties;

/**
 * Write a description of class Alarm here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Alarm
{
    static String commandBeforStart = null;
    static String commandMailCheck = null;
    static String beforeStart = "";
    static String mailCheck = "";
    static Properties props;

    public static final int ALARMSLEEP = 0;
    public static final int ALARMSTART = 1;
    public static final int ALARMWAIT = 2;
    public static final int ALARMACTIVE = 3;
    public static  int loop = ALARMSLEEP;
    public static boolean alarm = false;
    public static Boolean busy = true; 

    public static class BewegungListener implements GpioPinListenerDigital {
        @Override
        public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
            GpioPin pin = event.getPin();
            PinState state = event.getState();
            if(state.isHigh() && loop == ALARMACTIVE){
                loop = ALARMSLEEP;
                commandMailCheck = ScheduleUtil.activeForMinutes(mailCheck);
                AlarmService.sendMessage(props);
            }    
        }
    }    

    public static class AlarmStartListener implements GpioPinListenerDigital {
        @Override
        public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
            GpioPin pin = event.getPin();
            PinState state = event.getState();
            if(state.isHigh()){
                loop = ALARMSTART;
            }    
        }
    }    

    public static class AlarmStopListener implements GpioPinListenerDigital {
        @Override
        public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
            GpioPin pin = event.getPin();
            PinState state = event.getState();
            if(state.isHigh()){
                loop = ALARMSLEEP;
            }    
        }
    }    

    public static void main(String[] args) throws InterruptedException, ParseException{

        AlarmProperties pService = new AlarmProperties("Alarm.properties");
        props = pService.getProperties();
        beforeStart = props.getProperty("beforeStart").trim();
        mailCheck = props.getProperty("mailcheck").trim();

        final GpioController gpio = GpioFactory.getInstance();
        final GpioPinDigitalInput alarmStart      = gpio.provisionDigitalInputPin(RaspiPin.GPIO_01, PinPullResistance.PULL_UP);
        final GpioPinDigitalInput alarmStop       = gpio.provisionDigitalInputPin(RaspiPin.GPIO_02, PinPullResistance.PULL_UP);
        final GpioPinDigitalInput shutdown        = gpio.provisionDigitalInputPin(RaspiPin.GPIO_03,PinPullResistance.PULL_UP);
        final GpioPinDigitalInput bewegungsmelder = gpio.provisionDigitalInputPin(RaspiPin.GPIO_04,PinPullResistance.PULL_UP);
        final GpioPinDigitalOutput led             = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_05);
        led.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF); 

        Shutdown shutdownListener = new Shutdown(busy);
        shutdown.addListener(shutdownListener); 
        bewegungsmelder.addListener(new BewegungListener());
        alarmStart.addListener(new AlarmStartListener());
        alarmStop.addListener(new AlarmStopListener());

        loop = ALARMSLEEP;
        commandMailCheck = ScheduleUtil.activeForMinutes(mailCheck);
        while (busy) {
            switch (loop){
                case ALARMSLEEP:{
                    if(!ScheduleService.scheduleCheckForLongTimeUse(commandMailCheck)){
                        if(AlarmService.isAlarmOnOff(props, loop )){
                            loop = ALARMSTART;
                        }
                        commandMailCheck = ScheduleUtil.activeForMinutes(mailCheck);
                    }
                    break;
                }
                case ALARMSTART:{
                    commandBeforStart = ScheduleUtil.activeForMinutes(beforeStart);
                    led.toggle();
                    loop = ALARMWAIT;
                    break;
                }
                case ALARMWAIT:{
                    led.toggle();
                    if(!ScheduleService.scheduleCheckForLongTimeUse(commandBeforStart)){
                        loop = ALARMACTIVE;
                        led.low();
                    }
                    break;
                }
                case ALARMACTIVE: {
                    if(!ScheduleService.scheduleCheckForLongTimeUse(commandMailCheck)){
                        if(AlarmService.isAlarmOnOff(props, loop )){
                            loop = ALARMSLEEP;
                        }
                        commandMailCheck = ScheduleUtil.activeForMinutes(mailCheck);
                    }
                    break;
                }
            }
            Thread.sleep(1000);
        }
    }
}

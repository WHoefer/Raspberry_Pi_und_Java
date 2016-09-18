package raspi.projekte.kap06;

import raspi.schedule.*;
import raspi.audio.SourceDataPlayer;
import raspi.audio.PlayerException;
import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.*;
import java.text.ParseException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Wolfgang Höfer 
 * @version 1.0
 */
public class Ultraschall
{

    public double messen(GpioPinDigitalInput echo, GpioPinDigitalOutput trigger)throws InterruptedException{
        double start = 0d;
        double stopp = 0d;
        double div = 0d;
        double distanz = 0d;
        double luft = 343.0f;
        while(echo.isHigh());
        trigger.high();
        Thread.sleep(20);
        trigger.low();
        while(echo.isLow());
        start = System.nanoTime();
        while(echo.isHigh());
        stopp = System.nanoTime();
        div = stopp - start;
        distanz = div * luft * 0.5E-7;
        return distanz;
    }

    public static void main(String[] args) throws InterruptedException{
        final GpioController gpio = GpioFactory.getInstance();
        final GpioPinDigitalOutput trigger = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01);
        trigger.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
        final GpioPinDigitalInput echo = gpio.provisionDigitalInputPin(RaspiPin.GPIO_02,PinPullResistance.OFF);
        Ultraschall ultraschall = new Ultraschall();
        Thread.sleep(2000);
        while(true){
            Thread.sleep(2000);
            System.out.printf("%1$3.0f %n", ultraschall.messen(echo, trigger));
        }

    }
}

package raspi.projekte.kap05;

import raspi.schedule.ScheduleUtil;
import raspi.schedule.ScheduleService;
import java.text.ParseException;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.GpioPin;

/**
 * Akkus laden
 * 
 * @author Wolfgang Höfer
 * @version 1.0
 */
public class Akkusladen
{
    public static void main(String[] args) throws IOException, ParseException, InterruptedException{

        final GpioController gpio = GpioFactory.getInstance();

        final GpioPinDigitalOutput relais1 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01);
        relais1.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);

        String command = "";
        BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Geben Sie die Minuten ein, die das Akku");
        System.out.println("geladen werden soll: ");
        String dauerMinuten = console.readLine();
        command = ScheduleUtil.activeForMinutes(dauerMinuten);
        System.out.println("Akku wird geladen...");
        relais1.high();             
        while (ScheduleService.scheduleCheckForLongTimeUse(command)) {
            Thread.sleep(5000);
        }
        relais1.low();
        System.out.println("Die Ladezeit ist beendet.");
    }
}

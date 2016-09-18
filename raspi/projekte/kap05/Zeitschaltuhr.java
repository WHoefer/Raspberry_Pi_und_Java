package raspi.projekte.kap05;

import raspi.schedule.ScheduleUtil;
import raspi.schedule.ScheduleService;
import java.text.ParseException;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;


import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.GpioPin;

/**
 * Zeitschaltuhr
 * 
 * @author Wolfgang Höfer
 * @version 1.0
 */
public class Zeitschaltuhr
{
    public static void main(String[] args) throws IOException, ParseException, InterruptedException{

        final GpioController gpio = GpioFactory.getInstance();

        final GpioPinDigitalOutput relais = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01);
        relais.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);

        System.out.println("Zeitschaltuhr");
        String command = "";
        String loop = "";
        BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Geben Sie bitte ein, in wie viel Minuten das Relais");
        System.out.println("eingeschaltet werden soll: ");
        String startMinuten = console.readLine();
        System.out.println("Geben Sie bitte ein, wie viele Minuten das");
        System.out.println("Relais eingeschaltet bleiben soll: ");
        String dauerMinuten = console.readLine();
        System.out.println("In welchem Sekundentakt soll das Relais");
        System.out.println("ein- und ausgeschaltet werden: ");
        String takt = console.readLine();
        command = ScheduleUtil.startPulseInMinutes(startMinuten, dauerMinuten,takt);
        String dauerGesamt =  ScheduleUtil.addMinutes(startMinuten, dauerMinuten);
        loop = ScheduleUtil.activeForMinutes(dauerGesamt);
        System.out.println("Zeitschaltuhr ist aktiv!");
        System.out.println("Drücken Sie die Return-Taste, um das Programm vorzeitig zu beenden!");
        InputStream is = null;
        int c = 0;
        while (ScheduleService.scheduleCheckForLongTimeUse(loop) && c == 0) {
            if(ScheduleService.scheduleCheckForLongTimeUse(command)){
                relais.high();             
            }else{
                relais.low();
            }
            Thread.sleep(500);
            c = System.in.available();
        }
        relais.low();
        System.out.println("Ende");
    }
}

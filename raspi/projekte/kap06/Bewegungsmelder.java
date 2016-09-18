package raspi.projekte.kap06;


import raspi.schedule.*;
import raspi.audio.SourceDataPlayer;
import raspi.audio.PlayerException;
import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.*;
import java.text.ParseException;

/**
 * Komfortsteuerung für Badezimmer oder andere Wohnräume.<br>
 * 
 * Die Klasse Bewegungsmelder erfasst über einen Bewegungsmelder Bewegungen und schaltet dann
 * für eine vorgegebene Zeit eine Relais (z.B. für Licht) ein und spielt eine Audiodatei ab.
 * Wenn keine Bewegung mehr festgestellt wird, bleibt der Zustand für die eingestellte Zeit 
 * erhalten (lichtMusikEin in Minuten). Danach wird das Relais abgeschaltet und das Audio
 * ausgeblendet. Nach drei Sekunden wird dann das zweite Relais aktiviert (Lüfter im Badezimmer)
 * und bleibt die vorgegebene Zeit (luefterEin in Minuten) eingeschaltet. Wenn keine Bewegung
 * erfasst wird, verweilt das Programm ohne weitere Aktionen.
 * 
 * @author Wolfgang Höfer
 * @version 1.0
 */
public class Bewegungsmelder
{

    static String commandLicht = null;
    static String commandLuefter = null;
    static String commandMusik = null;

    static final int PAUSE = 0;
    static final int LICHTAUDIOEIN = 1;
    static final int LICHTAUDIOAUS = 2;
    static final int LUEFTEREIN = 3;
    static final int LUEFTERAUS = 4;
    static  int l = PAUSE;

    static String lichtMusikEin = "1";
    static String luefterEin = "1";
    static String audiodatei = "/home/pi/JavaProgramme/Projekte/Raspberry_Pi_und_Java/audio/Wellen.wav";

    public static class BewegungListener implements GpioPinListenerDigital {
        @Override
        public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {

            GpioPin pin = event.getPin();
            String name = pin.getName();
            PinState state = event.getState();

            if(state.isHigh()){
                l = LICHTAUDIOEIN;
                commandLicht = ScheduleUtil.activeForMinutes(lichtMusikEin);
                commandMusik = ScheduleUtil.activeForMinutes(lichtMusikEin);
            }    
        }
    }    

    public static void main(String[] args) throws InterruptedException, ParseException, PlayerException{

        if(args.length == 3){
            lichtMusikEin = args[0];
            luefterEin = args[1];
            audiodatei = args[2];
        }else if(args.length == 1){
            audiodatei = args[0];
        }

        final GpioController gpio = GpioFactory.getInstance();
        final GpioPinDigitalOutput licht = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01);
        final GpioPinDigitalOutput luefter = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02);
        licht.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
        luefter.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
        final GpioPinDigitalInput taster = gpio.provisionDigitalInputPin(RaspiPin.GPIO_06,PinPullResistance.PULL_UP);
        final GpioPinDigitalInput bewegungsmelder = gpio.provisionDigitalInputPin(RaspiPin.GPIO_05,PinPullResistance.PULL_UP);
         
        Shutdown shutdownListener = new Shutdown(null);
        taster.addListener(shutdownListener); 
        bewegungsmelder.addListener(new BewegungListener());

        SourceDataPlayer player = null;

        l = PAUSE;
        luefter.low();
        licht.low();
        commandLicht = ScheduleUtil.activeForMinutes(lichtMusikEin);
        commandLuefter = ScheduleUtil.activeForMinutes(luefterEin);
        commandMusik = ScheduleUtil.activeForMinutes(lichtMusikEin);

        while (true) {
            switch (l){
                case PAUSE: 
                break;
                case LICHTAUDIOEIN:{
                    if(ScheduleService.scheduleCheckForLongTimeUse(commandLicht)){
                        licht.high();
                        luefter.low();
                        if(player == null || player.isReady()){
                            player = new SourceDataPlayer(audiodatei);
                            shutdownListener.setObject(player);
                            player.setGain(-30f);
                            player.startPlayer();
                            Thread.sleep(1000);
                            player.setGain(-20f);
                            Thread.sleep(1000);
                            player.setGain(-10f);
                            Thread.sleep(1000);
                            player.setGain(-0f);
                            Thread.sleep(1000);
                        }
                    }else{l = LICHTAUDIOAUS;}
                    break;
                }
                case LICHTAUDIOAUS: {
                    licht.low();
                    luefter.low();
                    if(player != null){
                        player.setGain(-10f);
                        Thread.sleep(1000);
                        player.setGain(-20f);
                        Thread.sleep(1000);
                        player.setGain(-30f);
                        Thread.sleep(1000);
                        player.stopPlayer();
                        player.closePlayer();
                        player = null;
                    }
                    Thread.sleep(3000);
                    commandLuefter = ScheduleUtil.activeForMinutes(luefterEin);
                    l = LUEFTEREIN;
                    break;
                }
                case LUEFTEREIN:{ 
                    if(ScheduleService.scheduleCheckForLongTimeUse(commandLuefter)){
                        luefter.high();
                    }else{ l = LUEFTERAUS;}
                    break;
                }
                case LUEFTERAUS:{ 
                    luefter.low();
                    l = PAUSE;
                    break;
                }

            }
            Thread.sleep(1000);
        }
    }
}

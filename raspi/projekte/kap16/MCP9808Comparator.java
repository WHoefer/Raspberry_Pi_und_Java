package raspi.projekte.kap16;

import com.pi4j.io.i2c.*;
import raspi.hardware.i2c.MCP9808;
import java.io.IOException;
import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.*;

/**
 * Test für den MCP9808
 * 
 * @author Wolfgang Höfer
 * @version 1.0
 */
public class MCP9808Comparator
{

    public static final int ADDRESS = 0x1A; // Adresse
    public static final String  LINE = "------------------"; 
    public static final String  TESTLINE = "%1$s Test: %2$s %3$s %n"; 
    public static final String  TEMPOUT  = "%1$f°C%n"; 
    public static boolean alertInterrupt;
    public static I2CBus bus1;
    public static MCP9808 testI2C;

    static boolean ok = false;

    public static class TempListener implements GpioPinListenerDigital {
        @Override
        public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
            GpioPin pin = event.getPin();
            PinState state = event.getState();
            if(state.isHigh()){
                System.out.printf("Eingangspsannung = 3,3V Temperatur = %1$f°C%n",testI2C.getAmbientTemp());
                ok = true;
            }else{
                System.out.printf("Eingangspsannung = 0V Temperatur = %1$f°C%n",testI2C.getAmbientTemp());
            }

        }
    }        

    public static void main(String[] args) throws IOException, InterruptedException{
        bus1 = I2CFactory.getInstance(I2CBus.BUS_1);
        testI2C = new MCP9808(bus1.getDevice(ADDRESS));
        testI2C.reset();
        testI2C.setResultion(MCP9808.RES05);
        Thread.sleep(300);
        testI2C.configComparatorMode(true, true);
        Thread.sleep(300);
        double t1 =  testI2C.getAmbientTemp();
        testI2C.setCritTemp(t1+3.0d);
        testI2C.setHysteresis(MCP9808.HYST15);

        System.out.printf(TESTLINE, LINE, "Modus Comparator", LINE);
        System.out.printf("Kritische Temperatur %1$f °C%n",testI2C.getCritTemp());
        final GpioController gpio = GpioFactory.getInstance();
        final GpioPinDigitalInput alert = gpio.provisionDigitalInputPin(RaspiPin.GPIO_04,PinPullResistance.PULL_UP);
        alert.addListener(new TempListener());
        System.out.println("Hysterese = 1,5°C");
        System.out.print("Temperatur = ");
        System.out.printf(TEMPOUT, t1);
        System.out.println("Sensor mit Finger erwärmen!");
        while(!ok){
            Thread.sleep(1000);
        }
        ok = false;
        System.out.println("Sensor abkühlen lassen (10sec)!");
        Thread.sleep(10000);
        System.out.println("Sensor mit Finger erwärmen!");
        while(!ok){
            Thread.sleep(1000);
        }
        ok = false;
        System.out.println("Sensor abkühlen lassen (10sec)!");
        Thread.sleep(10000);
        gpio.shutdown();
        System.out.printf(TESTLINE, LINE, "Beendet", LINE);
    }
}

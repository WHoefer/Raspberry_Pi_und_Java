package raspi.projekte.kap16;

import  com.pi4j.io.i2c.*;
import  raspi.hardware.i2c.MCP9808;
import  java.io.IOException;
import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.*;

/**
 * Test für den MCP9808
 * 
 * @author Wolfgang Höfer 
 * @version 1.0
 */
public class MCP9808Test
{

    public static final int ADDRESS = 0x1A; // Adresse
    public static final String  LINE = "------------------"; 
    public static final String  TESTLINE = "%1$s Test: %2$s %3$s %n"; 
    public static final String  TEMPOUT  = "%1$f°C%n"; 
    public static I2CBus bus1;
    public static MCP9808 testI2C;
    public static int delay = 300;

    public static void main(String[] args) throws IOException, InterruptedException{
        bus1 = I2CFactory.getInstance(I2CBus.BUS_1);
        testI2C = new MCP9808(bus1.getDevice(ADDRESS));
        testI2C.reset();

        System.out.printf(TESTLINE, LINE, "Umgebungstemperatur", LINE);
        System.out.printf(TEMPOUT, testI2C.getAmbientTemp());

        System.out.printf(TESTLINE, LINE, "Auflösung", LINE);
        testI2C.setResultion(MCP9808.RES05);
        Thread.sleep(delay);  // Wandlung in 30ms
        System.out.printf(TEMPOUT, testI2C.getAmbientTemp());
        testI2C.setResultion(MCP9808.RES025);
        Thread.sleep(delay);  // Wandlung in 65ms
        System.out.printf(TEMPOUT, testI2C.getAmbientTemp());
        testI2C.setResultion(MCP9808.RES0125);
        Thread.sleep(delay); // Wandlung in 130ms
        System.out.printf(TEMPOUT, testI2C.getAmbientTemp());
        testI2C.setResultion(MCP9808.RES00625);
        Thread.sleep(delay); // Wandlung in 250ms
        System.out.printf(TEMPOUT, testI2C.getAmbientTemp());

        System.out.printf(TESTLINE, LINE, "Unterer Temperaturgrenzwert", LINE);
        double temp = testI2C.getAmbientTemp();
        testI2C.setLowerTemp(temp + 4.0d);
        Thread.sleep(delay);
        boolean boundaryCheck = testI2C.isAmbientLessLowerBoundary();
        Thread.sleep(delay);
        System.out.printf("Unterer Wert unterschritten true = %1$b%n",boundaryCheck);
        testI2C.setLowerTemp(temp - 4.0d);
        Thread.sleep(delay);
        boundaryCheck = testI2C.isAmbientLessLowerBoundary();
        Thread.sleep(delay);
        System.out.printf("Unterer Wert unterschritten false = %1$b%n",boundaryCheck);
        
        System.out.printf(TESTLINE, LINE, "Oberer Temperaturgrenzwert", LINE);
        temp = testI2C.getAmbientTemp();
        testI2C.setUpperTemp(temp - 4.0d);
        Thread.sleep(delay);
        boundaryCheck = testI2C.isAmbientGreaterUpperBoundary();
        Thread.sleep(delay);
        System.out.printf("Oberer Wert übererschritten true = %1$b%n",boundaryCheck);
        testI2C.setUpperTemp(temp + 4.0d);
        Thread.sleep(delay);
        boundaryCheck = testI2C.isAmbientGreaterUpperBoundary();
        Thread.sleep(delay);
        System.out.printf("Oberer Wert übererschritten false = %1$b%n",boundaryCheck);
        
        System.out.printf(TESTLINE, LINE, "Kritische Temperatur", LINE);
        temp = testI2C.getAmbientTemp();
        testI2C.setCritTemp(temp - 4.0d);
        Thread.sleep(delay);
        boundaryCheck = testI2C.isAmbientGreaterEqualCrit();
        Thread.sleep(delay);
        System.out.printf("Kritische Temperatur übererschritten true = %1$b%n",boundaryCheck);
        testI2C.setCritTemp(temp + 4.0d);
        Thread.sleep(delay);
        boundaryCheck = testI2C.isAmbientGreaterEqualCrit();
        Thread.sleep(delay);
        System.out.printf("Kritische Temperatur übererschritten false = %1$b%n",boundaryCheck);
        
        System.out.printf(TESTLINE, LINE, "Grenzwerte schreiben und lesen", LINE);
        testI2C.setCritTemp(31.0d);
        testI2C.setUpperTemp(25.5d);
        testI2C.setLowerTemp(20.0d);
        System.out.printf("Kritische Temperatur 31.0°C =  %1$f °C%n",testI2C.getCritTemp());
        System.out.printf("Obere Grenztemperatur 25.5°C = %1$f °C%n",testI2C.getUpperTemp());
        System.out.printf("Untere Grenztemperatur 20.0°C = %1$f °C%n",testI2C.getLowerTemp());
        
        System.out.printf(TESTLINE, LINE, "Hysterese für Grenztemperaturen", LINE);
        testI2C.setHysteresis(MCP9808.HYST30);
        double t1 =  testI2C.getAmbientTemp();
        testI2C.setCritTemp(t1+4.0d);
        System.out.printf("Umgebungstemperatur = %1$f °C%n",t1);
        System.out.printf("Oberer Wert für kritische Temperatur  = %1$f °C%n",t1+4.0d);
        System.out.printf("Unterer Wert für kritische Temperatur  = %1$f °C%n",t1+4.0d-3.0d);
        System.out.printf("Eingestellte Hysterese = %1$f °C%n",3.0d);
        System.out.println("Sensor mit Finger erwärmen!");
        while(!testI2C.isAmbientGreaterEqualCrit()){
            Thread.sleep(250);
        }
        t1 = testI2C.getAmbientTemp();
        System.out.println("Die kritische Temperatur von " + t1 + "°C wurde erreicht!");
        while(testI2C.isAmbientGreaterEqualCrit()){
            Thread.sleep(250);
        }
        double t2 =  testI2C.getAmbientTemp();
        System.out.println("Die kritische Temperatur von " + t2 + "°C wurde unterschritten!");
        System.out.printf("Hysterese = %1$f °C%n",t1-t2);
        System.out.printf(TESTLINE, LINE, "Beendet", LINE);

    }
}

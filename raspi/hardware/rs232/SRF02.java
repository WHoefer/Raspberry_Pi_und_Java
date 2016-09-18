package raspi.hardware.rs232;

import com.pi4j.io.serial.Serial;
import com.pi4j.io.serial.SerialDataEvent;
import com.pi4j.io.serial.SerialDataListener;
import com.pi4j.io.serial.SerialFactory;
import com.pi4j.io.serial.SerialPortException;

/**
 * Treiberklasse für den Ultraschallsensor SRF02 im
 * RS232 Modus.
 * 
 * @author Wolfgang Höfer 
 * @version 1.0
 */
public class SRF02
{
    private final Serial serial;
    public static final int START_MEASURE_INCHES     = 0x50; // Start Messung in Zoll
    public static final int START_MEASURE_CM         = 0x51; // Start Messung in cm
    public static final int START_MEASURE_MSECOND    = 0x52; // Start Messung in µs
    public static final int GET_DISTANCE             = 0x5E; // Messwert holen

    public static final int START_AUTO_MEASURE_INCHES  = 0x53; // Start Automessung in Zoll 
    public static final int START_AUTO_MEASURE_CM      = 0x54; // Start Automessung in Centimeter
    public static final int START_AUTO_MEASURE_MSECOND = 0x55; // Start Automessung in µs

    public static final int IMPULSE                  = 0x5C; // Sendet 8 mal 40kHz Impulstöne

    public static final int RECEIVE_MEASURE_INCHES     = 0x56; // Start Messung in Zoll ohne Impuls
    public static final int RECEIVE_MEASURE_CM         = 0x57; // Start Messung in cm ohne Impuls
    public static final int RECEIVE_MEASURE_MSECOND    = 0x58; // Start Messung in µs ohne Impuls

    public static final int RECEIVE_AUTO_MEASURE_INCHES  = 0x59; // Start Automessung in Zoll  ohne Impuls
    public static final int RECEIVE_AUTO_MEASURE_CM      = 0x5A; // Start Automessung in cm ohne Impuls
    public static final int RECEIVE_AUTO_MEASURE_MSECOND = 0x5B; // Start Automessung in µs ohne Impuls

    private static final int CHANGEID1 = 0xA0;          // Erster Befehl zum Ändern der Adresse
    private static final int CHANGEID2 = 0xAA;          // Zweiter Befehl zum Ändern der Adresse
    private static final int CHANGEID3 = 0xA5;          // Zweiter Befehl zum Ändern der Adresse

    private static final int FIRMWARE     = 0x5D; // Firmware-Version abfragen
    private static final int MIN_DISTANCE = 0x5F; // Kleinste zu messende Distanz erfragen
    private static final int AUTO_CAL     = 0x60; // Kalibrierung

    private int distance;
    private String buffer;
    
    /**
     * Konstruktor der Klasse SRF02
     */
    public SRF02()
    {
        serial = SerialFactory.createInstance();
        try {
            serial.open(Serial.DEFAULT_COM_PORT, 9600);
        }
        catch(IllegalStateException ex){
            ex.printStackTrace();                    
        }
        serial.addListener(new SerialDataListener() {
                @Override
                public void dataReceived(SerialDataEvent event) {
                    setBuffer(event.getData());
                    distance = getInt(getBuffer());
                }            
            });

    }

    /**
     * getInt wandelt einen ganzzahligen Wert, der in einer Zeichenkette
     * übergeben wird, in einen int-Wert um. Das höherwertige Zeichen liegt 
     * in der Zeichenkette an erster Stelle.
     * Wenn die Zeichenkette länger als zwei Zeichen lang isdt, wird 0 zurückgegeben.
     *
     * @param value Zeichenkette, die aus zwei zeichen besteht.
     * @return int-Wert
     */
    public int getInt(String value){
        char[] ca = value.toCharArray();
        if(ca.length == 2){
            int msb = ca[0];
            int lsb = ca[1];
            msb = (msb << 8) & 0xFF00;
            return msb + lsb;
        }
        return 0;
    }    

    /**
     * setBuffer setzt den Inhalt des RS232 Puffers. Die ist nicht der
     * UART Puffer, sondern ein zwischen Puffer, der gesendete und empfangene
     * Zeichen enthält.
     *
     * @param value Pufferwert
     */
    private synchronized void setBuffer(String value){
        buffer = value;
    }    

    /**
     * getBuffer gibt den Inhalt des RS232 Puffers zurück. Dies ist nicht der
     * UART Puffer, sondern ein zwischen Puffer, der gesendete und empfangene
     * Zeichen enthält.
     *
     * @return Pufferinhalt
     */
    private synchronized String getBuffer(){
        return buffer;
    }    

    /**
     *  wait hält den aktuellen Thread für die Zeit
     *  time in Millisekunden an. Der Rückgabewert ist 1. 
     *  Im Fehlerfall wird -1 zurückgegeben.
     *
     * @param time Zeit in Millisekunden
     * @return Fehlerstatus 1/-1
     */
    public int wait(int time){
        try{
            Thread.sleep(time);
        }catch(InterruptedException ex){
            return -1;
        }
        return 1;
    }

    /**
     * getDistanceManually misst die Entfernung im manuellen Modus.
     * Mit address wird die Adresse des Sensors überegeben und mit
     * manuallyCommand wird die zu messende Einheit angegben. Die folgenden
     * Parameter sind für manuallyCommand erlaubt.<br>
     * SRF02.START_MEASURE_INCHES.....Messung in Zoll<br>
     * SRF02.START_MEASURE_CM.........Messung in cm<br>
     * SRF02.START_MEASURE_MSECOND....Messung in µs<br>
     * Im Fehlerfall wird -1 zurückgegeben.
     *
     * @param address Sensoradresse
     * @param manuallyCommand  Gibt die gewünschte Einheit an
     * @return Gemessene Distanz in der vorgegebenen Einheit
     */
    public int getDistanceManually(int address, int manuallyCommand){
        distance = -1;
        int val = -1;
        int count = 0;
        serial.write((byte) address);
        wait(1);
        serial.write((byte) manuallyCommand);
        wait(70);
        serial.write((byte) address);
        wait(1);
        serial.write((byte) GET_DISTANCE);
        while((val == -1) && count < 20){
            val = distance;
            wait(100);
            count++;
        } 
        distance = -1;
        return val;
    }

    /**
     * getDistanceAuto misst die Entfernung im automatischen Modus.
     * Mit address wird die Adresse des Sensors überegeben und mit
     * autoCommand wird die zu messende Einheit angegben. Die folgenden
     * Parameter sind für autoCommand erlaubt.<br>
     * SRF02.START_AUTO_MEASURE_INCHES.....Messung in Zoll<br>
     * SRF02.START_AUTO_MEASURE_CM.........Messung in cm<br>
     * SRF02.START_AUTO_MEASURE_MSECOND....Messung in µs<br>
     * Im Fehlerfall wird -1 zurückgegeben.
     *
     * @param address Sensoradresse
     * @param autoCommand Gibt die gewünschte Einheit an
     * @return Gemessene Distanz in der vorgegebenen Einheit
     */
    public int getDistanceAuto(int address, int autoCommand){
        distance = -1;
        int val = -1;
        int count = 0;
        serial.write((byte) address);
        wait(1);
        serial.write((byte) autoCommand);
        while((val == -1) && count < 20){
            val = distance;
            wait(100);
            count++;
        } 
        distance = -1;
        return val;
    }

    /**
     * getDistanceAutoCM gibt die Distanz in cm Zurück. 
     * Es wird im Auto-Modus gemessen.
     *
     * @param address Sensoradresse
     * @return Distanz in cm.
     */
    public int getDistanceAutoCM(int address){
        return getDistanceAuto(address, START_AUTO_MEASURE_CM);
    }

    /**
     * getDistanceAutoInches gibt die Distanz in Zoll Zurück. 
     * Es wird im Auto-Modus gemessen.
     *
     * @param address Sensoradresse
     * @return Distanz in Zoll.
     */
    public int getDistanceAutoInches(int address){
        return getDistanceAuto(address, START_AUTO_MEASURE_INCHES);
    }

    /**
     * getDistanceAutoMSecond gibt die Distanz in µs Zurück. 
     * Es wird im Auto-Modus gemessen.
     *
     * @param address Sensoradresse
     * @return Distanz in µs.
     */
    public int getDistanceAutoMSecond(int address){
        return getDistanceAuto(address, START_AUTO_MEASURE_MSECOND);
    }

    /**
     * getDistanceCM gibt die Distanz in cm Zurück. 
     * Es wird im Manuellen-Modus gemessen.
     *
     * @param address Sensoradresse
     * @return Distanz in cm.
     */
    public int getDistanceCM(int address){
        return getDistanceManually(address, START_MEASURE_CM);
    }

    /**
     * getDistanceInches gibt die Distanz in Zoll Zurück. 
     * Es wird im Manuellen-Modus gemessen.
     *
     * @param address Sensoradresse
     * @return Distanz in Zoll.
     */
    public int getDistanceInches(int address){
        return getDistanceManually(address, START_MEASURE_INCHES);
    }

    /**
     * getDistanceMSecond gibt die Distanz in µs Zurück. 
     * Es wird im Manuellen-Modus gemessen.
     *
     * @param address Sensoradresse
     * @return Distanz in µs.
     */
    public int getDistanceMSecond(int address){
        return getDistanceManually(address, START_MEASURE_MSECOND);
    }


    /**
     * getFirmware gibt die Firmware-Version
     * des Sensors zurück. Im Fehlerfall wird ein
     * leere Zeichenkette zurückgegeben.
     *
     * @param address Sensoradresse
     * @return Firmware-Version
     */
    public String getFirmware(int address){
        setBuffer("");
        String val = "";
        int count = 0;
        serial.write((byte) address);
        wait(1);
        serial.write((byte) FIRMWARE);
        while((val == "") && count < 10){
            wait(1000);
            val =  getBuffer();
            count++;
        } 
        setBuffer("");
        if(val != null && !val.isEmpty()){
            byte[] firmware = val.getBytes();
            if(firmware.length > 0){
                return String.format("%1$s", firmware[0]);
            }
        }
        return "";
    }

    /**
     * flush sendet alle Zeichen, die sich noch im Puffer
     * befinden und leert die Puffer des UART.
     */
    public void flush(){
        serial.flush();
    }    

    /**
     * close schließt die Schnittstelle.
     */
    public void close(){
        serial.close();
    }    
    
    /**
     * shutdown beendet alle seriellen Daten-Monitor-Threads.
     * Diese Methode sollte immer vor dem Beenden einer  Anwendung
     * aufgerufen werden.
     *
     */
    public void shutdown(){
        serial.shutdown();
    }    

    
    /**
     * getMinDistance gibt die kleinste zu messende
     * Distanz zurück. Die Einhait des Rückgabewertes 
     * richtet sich nach der aktuell eingestellten Einheit.
     *
     * @param address Sensoradresse
     * @return Kleinste zu messende Distanz
     */
    public int getMinDistance(int address){
        distance = -1;
        int val = -1;
        int count = 0;
        serial.write((byte) address);
        wait(1);
        serial.write((byte) MIN_DISTANCE);
        while((val == -1) && count < 10){
            val = distance;
            wait(1000);
            count++;
        } 
        distance = -1;
        return val;
    }

    /**
     * setAddress ändert die Adresse des Sensors. Mit 
     * addressOld wird der Sensor angesprochen und mit
     * addressNew wird die neue Adresse gesetzt.
     *
     * @param addressOld Aktuelle Adresse
     * @param addressNew Neue Adresse
     */
    public void setAddress(int addressOld, int addressNew){
        serial.write((byte) addressOld);
        wait(1);
        serial.write((byte) CHANGEID1);
        wait(1);
        serial.write((byte) addressOld);
        wait(1);
        serial.write((byte) CHANGEID2);
        wait(1);
        serial.write((byte) addressOld);
        wait(1);
        serial.write((byte) CHANGEID3);
        wait(1);
        serial.write((byte) addressOld);
        wait(1);
        serial.write((byte) addressNew);
    }


}

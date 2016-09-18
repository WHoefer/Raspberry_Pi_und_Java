package raspi.hardware.i2c;

import  com.pi4j.io.i2c.*;
import  java.io.IOException;

/**
 * I2C-Treiber für den Temperatursensor MCP9808.
 * 
 * @author Wolfgang Höfer
 * @version 1.0
 */
public class MCP9808  extends I2C
{

    public static final int CONFIG = 0x01; // RW Konfigurationsregister
    public static final int TUPPER = 0x02; // RW obere Grenze Alarmtemperatur
    public static final int TLOWER = 0x03; // RW untere Grenze Alarmtemperatur
    public static final int TCRIT  = 0x04; // RW kritische Temperatur
    public static final int TEMPER = 0x05; // R  Umgebungstemperatur
    public static final int RESOL  = 0x08; // RW Aufösung RES05 - RES00625

    public static final int RES05    = 0x00; // Auflösung 0,5°C
    public static final int RES025   = 0x01; // Auflösung 0,25°C
    public static final int RES0125  = 0x10; // Auflösung 0,125°C
    public static final int RES00625 = 0x11; // Auflösung 0,0625°C

    public static final int HYST00 = 0x0; // Hysterese = 0°C
    public static final int HYST15 = 0x1; // Hysterese = 1,5°C
    public static final int HYST30 = 0x2; // Hysterese = 3,0°C
    public static final int HYST60 = 0x3; // Hysterese = 6,0°C

    public static final int  AlertOutputModeBit     = 0b0000_0001; //byte[1]
    public static final int  AlertOutputPolarityBit = 0b0000_0010; //byte[1]
    public static final int  AlertOutputSelectBit   = 0b0000_0100; //byte[1]
    public static final int  AlertOutputControlBit  = 0b0000_1000; //byte[1]
    public static final int  AlertOutputStatusBit   = 0b0001_0000; //byte[1]
    public static final int  InterruptClearBit      = 0b0010_0000; //byte[1]
    public static final int  WindowLockBit          = 0b0100_0000; //byte[1]
    public static final int  TCritLockBit           = 0b1000_0000; //byte[1]

    public static final int  ShutdownModeBit        = 0b0000_0001; //byte[0]


    public MCP9808(final I2CDevice dev)
    {
        super(dev);
    }

    /**
     * Konvertiert eine Temperaturangabe aus einem 2 Byte langen Array in einen double Wert. 
     * Das Format im Array muss MCP9808-Kompatibel sein.<br>
     * 16 15 14 13  12  11  10  09  08  07  06  05  04   03   02   01<br>
     * -  -  -  SGN 2^7 2^6 2^5 2^4 2^3 2^2 2^1 2^0 2^-1 2^-2 2^-3 2^-4<br>
     * byte[0]......                byte[1].......<br>
     * 
     * @param reg Temperaturangabe im MCO9808-Format
     * @return Temperatur als double-Wert
     */
    public double convertTemp(byte[] reg){
        double nachkomma    = (reg[1] & (byte)0x0F)/16d;
        double vorkommaLow  = (double)((reg[1] & 0xF0) >> 4);
        double vorkommaHigh = (double)((reg[0] & 0x0F) << 4);
        double temp = vorkommaHigh + vorkommaLow + nachkomma;
        double sign = 1.0;
        if((reg[0] & 0b10000) == 0b10000){
            sign = -1.0;
        }  
        return temp * sign;
    }

    /**
     * Konvertiert einen double-Wert, der eine Temperaturangabe repräsentiert, in ein 
     * 2 Byte langes Array. Das Format entspricht dem im MCP9808 benötigten Temperaturformat.<br>
     * 16 15 14 13  12  11  10  09  08  07  06  05  04   03   02   01<br>
     * -  -  -  SGN 2^7 2^6 2^5 2^4 2^3 2^2 2^1 2^0 2^-1 2^-2 2^-3 2^-4<br>
     * byte[0]......                byte[1].......<br>
     *
     * @param value Temperaturangabe als double-Wert
     * @return Temperaturangabe im MCO9808-Format
     */
    public static byte[] convertTempToReg(double value){
        byte[] reg = {(byte)0,(byte)0};
        int sign = 0b0001_0000;
        boolean pos = true;
        if(value < -40.0d){
            value = -40.0d;
        }
        if(value > 125.0d){
            value = 125.0d;
        } 
        if(value < 0.0d){
            value = -value;
            pos = false;
        }    
        int h = (int)value;
        reg[0] = (byte)((h >> 4) & 0x0F);
        reg[1] = (byte)((h << 4) & 0xF0);
        int nachkomma = (int)((value % h) * 16d) ;
        reg[1] =  (byte)((reg[1] | nachkomma) & 0xFF);
        if(!pos){
            reg[0] =  (byte)(reg[0] | sign);
        }  
        return reg;
    }

    /**
     * getAmbientTemp fragt die Umgebungstemperatur ab.
     *
     * @return Temperatur in °C
     */
    public double getAmbientTemp(){
        byte[] temp = {0,0};
        readArray(TEMPER, temp, 2);
        return convertTemp(temp);
    }

    /**
     * isAmbientGreaterEqualCrit erfragt, ob die Umgebungstemperatur 
     * größer/gleich der kritischen Temperatur ist.
     * Gibt true zurück wenn die Umgebungstemperatur 
     * größer/gleich der kritischen Temperatur ist.
     *
     * @return boolean
     * 
     */
    public boolean isAmbientGreaterEqualCrit(){
        byte[] temp = {0,0};
        readArray(TEMPER, temp, 2);
        return (temp[0] & 0b1000_0000) == 0b1000_0000;
    }

    /**
     * getCritTemp erfragt den Vorgabewert der kritischen Temperatur ab.
     *
     * @return Temperatur in °C
     */
    public double getCritTemp(){
        byte[] temp = {0,0};
        readArray(TCRIT, temp, 2);
        return convertTemp(temp);
    }

    /**
     * setCritTemp setzt den Vorgabewert für die kritische Temperatur.
     * Gibt 1 zurück, wenn erfolgreich geschrieben werden konnte. Im Fehlerfall
     * wird -1 zurückgegeben.
     *
     * @param dTemp Temperatur in °C
     * @return Schreibstatus 1/-1
     */
    public int setCritTemp(double dTemp){
        byte[] temp = {0,0};
        temp = convertTempToReg(dTemp);
        return writeArray(TCRIT, temp,2);
    }

    /**
     * getUpperTemp erfragt den Vorgabewert der oberen Temperaturgrenze ab.
     *
     * @return Temperatur in °C
     */
    public double getUpperTemp(){
        byte[] temp = {0,0};
        readArray(TUPPER, temp, 2);
        return convertTemp(temp);
    }

    /**
     * setUpperTemp setzt den Vorgabewert für die obere Temperaturgrenze.
     * Gibt 1 zurück, wenn erfolgreich geschrieben werden konnte. Im Fehlerfall
     * wird -1 zurückgegeben.
     *
     * @param dTemp Temperatur in °C
     * @return Schreibstatus 1/-1
     */
    public int setUpperTemp(double dTemp){
        byte[] temp = {0,0};
        temp = convertTempToReg(dTemp);
        return writeArray(TUPPER, temp,2);
    }

    /**
     * isAmbientGreaterUpperBoundary erfragt, ob die Umgebungstemperatur
     * größer als die obere Temperaturgrenze ist.
     *
     * @return boolean
     */
    public boolean isAmbientGreaterUpperBoundary(){
        byte[] temp = {0,0};
        readArray(TEMPER, temp, 2);
        return (temp[0] & 0b0100_0000) == 0b0100_0000;
    }

    /**
     * getLowerTemp erfragt den Vorgabewert die untere Temperaturgrenze ab.
     *
     * @return Temperatur in °C
     */
    public double getLowerTemp(){
        byte[] temp = {0,0};
        readArray(TLOWER, temp, 2);
        return convertTemp(temp);
    }

    /**
     * setLowerTemp setzt den Vorgabewert für die untere Temperaturgrenze.
     * Gibt 1 zurück, wenn erfolgreich geschrieben werden konnte. Im Fehlerfall
     * wird -1 zurückgegeben.
     *
     * @param dTemp Temperatur in °C
     * @return Schreibstatus 1/-1
     */
    public int setLowerTemp(double dTemp){
        byte[] temp = {0,0};
        temp = convertTempToReg(dTemp);
        return writeArray(TLOWER, temp,2);
    }

    /**
     * isAmbientLessLowerBoundary erfragt, ob die Umgebungstemperatur
     * kleiner als die untere Temperaturgrenze ist.
     *
     * @return boolean
     */
    public boolean isAmbientLessLowerBoundary(){
        byte[] temp = {0,0};
        readArray(TEMPER, temp, 2);
        return (temp[0] & 0b0010_0000) == 0b0010_0000;
    }


    /**
     * configComparatorMode aktiviert den Alert-Ausgang im Komparator-Modus.
     * Mit activeHigh = true/false wird der Alert-Ausgang High-/Low-Aktiv gesetzt.
     * Mit alertOnlyCrit = true reagiert der Alert-Ausgang nur auf das Überschreiten
     * der kritischen Temperatur.
     * Gibt 1 zurück, wenn erfolgreich geschrieben werden konnte. Im Fehlerfall
     * wird -1 zurückgegeben.
     *
     * @param activeHigh Alert-Ausgang Activ-High/Activ-Low 
     * @param alertOnlyCrit Alert nur bei Überschreitung der kritischen Temperatur
     * @return Schreibstatus
     */
    public int configComparatorMode(boolean activeHigh, boolean alertOnlyCrit){
        byte[] config = {0,0};
        readArray(CONFIG, config, 2);
        int conf = config[1];//Bit 1...8
        conf = setBit(conf, AlertOutputControlBit, 1); //Alert Output enabled
        conf = setBit(conf, AlertOutputModeBit, 0);    //Mode = Comparator
        if(activeHigh){
            conf = setBit(conf, AlertOutputPolarityBit, 1); //Alert Output = activ-high
        }else{
            conf = setBit(conf, AlertOutputPolarityBit, 0); //Alert Output = activ-low   
        }
        if(alertOnlyCrit){
            conf = setBit(conf, AlertOutputSelectBit, 1); //Alarmmeldung nur wenn Umgebungstemperatur  > kritische Temparatur 
        }else{
            conf = setBit(conf, AlertOutputSelectBit, 0); //Alarmmeldung bei TUpper, TLower and TCrit   
        }
        config[1] = (byte)conf;
        return writeArray(CONFIG, config, 2);

    }

    /**
     * alertOutputDisable deaktiviert den Alert-Ausgang
     * Gibt 1 zurück, wenn erfolgreich geschrieben werden konnte. Im Fehlerfall
     * wird -1 zurückgegeben.
     *
     * @return Schreibstatus
     */
    public int alertOutputDisable(){
        byte[] config = {0,0};
        readArray(CONFIG, config, 2);
        int conf = config[1];//Bit 1...8
        conf = setBit(conf, AlertOutputControlBit, 0); //Alert Output disabled
        config[1] = (byte)conf;
        return writeArray(CONFIG, config, 2);
    }

    /**
     * isAlertOutputStatus erfragt ob der Alertstatus auf true gesetzt wurde.
     *
     * @return Status
     */
    public boolean isAlertOutputStatus(){
        byte[] config = {0,0};
        readArray(CONFIG, config, 2);
        int conf = config[1];
        if(isHigh(conf, AlertOutputStatusBit)){
            return true;
        }
        return false;
    }

    /**
     * setHysteresis setzt die Hysterese für alle Alarmtemperaturen. Die folgenden
     * Konstanten können verwendet werden:<br>
     * MCP9808.HYST00 = 0°C<br>
     * MCP9808.HYST15 = 1,5°C<br>
     * MCP9808.HYST30 = 3,0°C<br>
     * MCP9808.HYST60 = 6,0°C<br>
     * Gibt 1 zurück, wenn erfolgreich geschrieben werden konnte. Im Fehlerfall
     * wird -1 zurückgegeben.
     *
     * @param hyst Wert für Hysterese
     * @return Schreibstatus 1/-1
     */
    public int setHysteresis(int hyst){
        byte[] config = {0,0};
        readArray(CONFIG, config, 2);
        int conf = config[0];
        switch(hyst){
            case HYST00:{  //Hysterese = 0°C
                conf = setBit(conf, 0b0000_0010, 0); 
                conf = setBit(conf, 0b0000_0100, 0); 
                break;
            }
            case HYST15:{  //Hysterese = 1,5°C
                conf = setBit(conf, 0b0000_0010, 1); 
                conf = setBit(conf, 0b0000_0100, 0); 
                break;
            }
            case HYST30:{  //Hysterese = 3,0°C
                conf = setBit(conf, 0b0000_0010, 0); 
                conf = setBit(conf, 0b0000_0100, 1); 
                break;
            }
            case HYST60:{  //Hysterese = 6,0°C
                conf = setBit(conf, 0b0000_0010, 1); 
                conf = setBit(conf, 0b0000_0100, 1); 
                break;
            }
            default:{
                return -1;
            }

        }
        config[0] = (byte)conf;
        return writeArray(CONFIG, config, 2);
    }

    /**
     * setResultion legt die Auflösung der gemessenen Umgebungstemperatur fest.
     * Mit den folgenden Konstanten werden die Verschiedenen Auflösungen eingestellt.<br>
     * MCP9808.RES05     -> 0,5°C<br>
     * MCP9808.RES025    -> 0,25°C<br>
     * MCP9808.RES01255  -> 0,125°C<br>
     * MCP9808.RES006255 -> 0,0625°C<br>
     * Gibt 1 zurück, wenn erfolgreich geschrieben werden konnte. Im Fehlerfall
     * wird -1 zurückgegeben.
     *
     * @param res Wert zum Einstellen der Hysterese
     * @return Schreibstatus 1/-1
     */
    public int setResultion(int res){
        int conf = read(RESOL);
        switch(res){
            case RES05:{  //Auflösung = 0,5°C
                conf = setBit(conf, 0b0000_0001, 0); 
                conf = setBit(conf, 0b0000_0010, 0); 
                break;
            }
            case RES025:{  //Auflösung = 0,25°C
                conf = setBit(conf, 0b0000_0001, 1); 
                conf = setBit(conf, 0b0000_0010, 0); 
                break;
            }
            case RES0125:{  //Auflösung = 0,125°C
                conf = setBit(conf, 0b0000_0001, 0); 
                conf = setBit(conf, 0b0000_0010, 1); 
                break;
            }
            case RES00625:{  //Auflösung = 0,0625°C
                conf = setBit(conf, 0b0000_0001, 1); 
                conf = setBit(conf, 0b0000_0010, 1); 
                break;
            }
            default:{
                return -1;
            }

        }
        return write(RESOL, (byte)conf);
    }

    /**
     * setShutdownMode setzt den MCP9808 in den Shutdown Modus.
     * Gibt 1 zurück, wenn erfolgreich geschrieben werden konnte. Im Fehlerfall
     * wird -1 zurückgegeben.
     *
     * @return Schreibstatus 1/-1
     */
    public int setShutdownMode(){
        byte[] config = {0,0};
        readArray(CONFIG, config, 2);
        int conf = config[0];
        config[0] = (byte)setBit(conf, ShutdownModeBit, 1); 
        return writeArray(CONFIG, config, 2);
    }

    /**
     * setActiveMode setzt den MCP9808 wird in den aktiven Modus.
     * Gibt 1 zurück, wenn erfolgreich geschrieben werden konnte. Im Fehlerfall
     * wird -1 zurückgegeben.
     *
     * @return Schreibstatus 1/-1
     */
    public int setActiveMode(){
        byte[] config = {0,0};
        readArray(CONFIG, config, 2);
        int conf = config[0];
        config[0] = (byte)setBit(conf, ShutdownModeBit, 0); 
        return writeArray(CONFIG, config, 2);
    }


    /**
     * reset setzt das Konfigurationsregister auf Defaulteinstellung.
     * Gibt 1 zurück, wenn erfolgreich geschrieben werden konnte. Im Fehlerfall
     * wird -1 zurückgegeben.
     *
     * @return Schreibstatus 1/-1
     */
    public int reset(){
        byte[] config = {0,0};
        return writeArray(CONFIG, config, 2);
    }

}

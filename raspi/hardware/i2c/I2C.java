package raspi.hardware.i2c;

import  com.pi4j.io.i2c.*;
import  java.io.IOException;
/**
 * I2C ist eine Klasse, die grundlegende Schreib-, Lese- und Bitoperationen unterstüzt.
 * Sie ist als Elternklasse für I2C-Geräte gedacht.
 * 
 * @author Wolfgang Höfer
 * @version 1.0
 */
public class I2C
{
    /**
     * I2CDevice
     */
     public final I2CDevice dev;
    /**
     * Constructor for objects of class I2C
     */
    public I2C(final I2CDevice dev)
    {
        this.dev = dev;
    }

    /**
     * setBit setzt an die durch bitpos bestimmte Stelle in reg das Bit auf
     * 1 oder 0. Für bitpos muss zum Beispiel für Bit 5 der Wert 0b0001_0000
     * bzw. 16 übergeben werden. Die Methode greift nicht auf ein I2C-Gerät
     * zu.
     *
     * @param reg Register, in dem das Bit geändert werden soll.
     * @param bitpos Bitposition
     * @param level Setzt Bit auf 0 oder 1
     * @return Gibt das geänderte Register zurück
     */
    public int setBit(int reg, int bitpos, int level){
        if(level == 0){
            reg =  reg & (~bitpos);
        }else if(level == 1){
            reg =  reg | bitpos;
        }
        return reg;
    }

    /**
     * getBit holt aus dem Register reg den Bitwert an der Stelle bitpos.
     * Für bitpos muss zum Beispiel für Bit 5 der Wert 0b0001_0000 bzw 16
     * übergeben werden.Die Methode greift nicht auf ein I2C-Gerät
     * zu.
     * 
     * @param reg Register
     * @param bitpos Bitposition
     * @return Bitwert 0/1
     */
    public int getBit(int reg, int bitpos){
        if( (reg | bitpos) == reg){
            return 1;
        }else{
            return 0;
        }    
    }

    /**
     * isBit liefert für ein gesetztes Bit aus dem Register reg den Wert true sonst false. 
     * Der Bitwert aus dem Register reg wird an der Stelle bitpos ausgelesen.Für bitpos 
     * muss zum Beispiel für Bit 5 der Wert 0b0001_0000 bzw 16 übergeben werden. Die Methode 
     * greift nicht auf ein I2C-Gerät
     * zu.
     *
     * @param reg Register
     * @param bitpos Bitposition
     * @return true für ein gesetztes Bit sonst false
     */
    public boolean isBit(int reg, int bitpos){
        int val = getBit(reg, bitpos);
        if(val == 1){
            return true;
        }else{
            return false;
        }    
    }

    
    /**
     * read liest das Register reg eines I2C-Gerätes und gibt den Inhalt zurück.
     * Die Methode kann bis zu 3 Byte lange Register auslesen. Ab dem 4.
     * Byte ist der Rückgabewert für den Fehlerfall (-1) nicht mehr 
     * eindeutig.
     * 
     * @param reg Register
     * @return Im Fehlerfall wird -1 zurückgegeben
     */
    public int read(int reg){
        try{
            return dev.read(reg);
        }catch(IOException ex){
            System.out.println("Lesefehler von I2C-Gerät aus Register: " + reg);
            System.out.println(ex.toString());
            return -1;
        }
    }    

    /**
     * readArray liest aus dem Register reg  eines I2C-Gerätes die Anzahl Bytes, 
     * die durch size angegeben wird und speichert diese in das Byte-Array array. 
     * Im Fehlerfall wird das erste Byte im Array mit dem Wert 0 belegt.
     *
     * @param reg Register
     * @param array Byte-Array
     * @param size Anzahl Byte, die gelesen werden sollen
     * @return Byte-Array
     */
    public byte[] readArray(int reg, byte[] array, int size ){
        
        try{
            int count = dev.read(reg, array, 0, size);
            if(count == size){
                return array;
            }else{
                System.out.println("Lesefehler! Es wurden nicht so viele Bytes zurückgegeben, wie angeforder wurden!");
                System.out.println( count + " <> " + size);
                return array;
            }
        }catch(IOException ex){
            System.out.println("Lesefehler von I2C-Gerät aus Register: " + reg);
            System.out.println(ex.toString());
            array[0] = 0;
            return array;
        }
    }    

    
    /**
     * writeArray schreibt den Inhalt aus dem Byte-Array array in das Register
     * reg eines I2C-Gerätes. Die Anzahl der zu schreibenden Bytes steht in size. 
     * Im Fehlerfall wird -1 zurückgegeben, sonst 1.
     *
     * @param reg Register
     * @param array Byte-Array, das in das Regsietr geschrieben wird.
     * @param size Anzahl zu schriebender Bytes.
     * @return Schreibstatus 1/-1
     */
    public int writeArray(int reg, byte[] array, int size ){
        try{
            dev.write(reg, array, 0, size);
            return 1;
        }catch(IOException ex){
            System.out.println("Schreibfehler nach I2C-Gerät nach Register: " + reg);
            System.out.println(ex.toString());
            return -1;
        }
    }    

    
    
    /**
     * readPin liest das Register reg des I2C-Gerätes und gibt das Bit bitpos zurück.
     * Für bitpos muss zum Beispiel für Bit 5 der Wert 0b0001_0000 bzw 16
     * übergeben werden. Im Fehlerfall wird -1 zurückgegeben.
     * Die Methode kann bis zu 3 Byte lange Register auslesen. Ab dem 4.
     * Byte ist der Rückgabewert für den Fehlerfall (-1) nicht mehr 
     * eindeutig.
     * 
     * @param reg Register
     * @param bitpos Bitposition
     * @return Bitwert an der vorgegebenen Bitposition
     */
    public int readPin(int reg, int bitpos){
        int regVal;
        regVal = read(reg);
        if(regVal != -1){
            return getBit(regVal, bitpos);
        }else{    
            return -1;
        }   
    }    

    
    /**
     * isHigh liest das Register reg des I2C-Gerätes und prüft das Bit an der Stelle
     * bitpos. Gibt true zurück, wenn das Bit gesetzt ist, sonst false.
     * Für bitpos muss zum Beispiel für Bit 5 der Wert 0b0001_0000 bzw 16
     * übergeben werden.
     *
     * @param reg Register
     * @param bitpos Bitposition
     * @return true für gesetzt, sonst false
     */
    public boolean isHigh(int reg, int bitpos){
        int val = readPin(reg, bitpos);
        if(val == 1){
            return true;
        }else if(val == 0){
            return false;
        }
        return false; 
    }    

    /**
     * isLow liest das Register reg des I2C-Gerätes und prüft das Bit an der Stelle
     * bitpos. Gibt true zurück, wenn das Bit nicht gesetzt ist, sonst false.
     * Für bitpos muss zum Beispiel für Bit 5 der Wert 0b0001_0000 bzw 16
     * übergeben werden.
     *
     * @param reg Register
     * @param bitpos Bitposition
     * @return true für nicht gesetzt, sonst false
     */
    public boolean isLow(int reg, int bitpos){
        if(isHigh(reg, bitpos)){
            return false;
        }    
        return true;
    }    

    /**
     * write schreibt in das Register reg des I2C-Gerätes den Wert val.
     * Im Fehlerfall wird -1 zurückgegeben, sonst 1.
     *
     * @param reg Register
     * @param val Wert
     * @return Schreibstatus 1/-1
     */
    public int write(int reg, byte val){
        try{
            dev.write(reg, val);
            return 1;
        }catch(IOException ex){
            System.out.println("Schreibfehler nach I2C-Gerät nach Register: " + reg + " und Wert: " + val);
            System.out.println(ex.toString());
            return -1;
        }
    }    

    /**
     * configPin liest von dem I2C-Gerät das Register reg und ändert
     * an der Stelle bitpos den Bitwert auf level und schreibt die
     * Daten wieder in das Register des I2C-Gerätes.
     * Im Fehlerfall wird -1 zurückgegeben, sonst 1.
     * Für bitpos muss zum Beispiel für Bit 5 der Wert 0b0001_0000 bzw 16
     * übergeben werden.
     *
     * @param reg Register
     * @param level Bitwert
     * @param bitpos Bitposition
     * @return Schreibstatus 1/-1
     */
    public int configPin(int reg, int level, int bitpos){
        int regVal = read(reg);
        if(regVal != -1){
            regVal = setBit(regVal, bitpos, level);
            return write(reg, (byte)regVal);
        }else{
            return -1;
        }   
    }    

    /**
     * configPin liest von dem I2C-Gerät das Register regRead und ändert
     * an der Stelle bitpos den Bitwert auf level und schreibt die
     * Daten in das Register regWrite des I2C-Gerätes.
     * Im Fehlerfall wird -1 zurückgegeben, sonst 1.
     * Für bitpos muss zum Beispiel für Bit 5 der Wert 0b0001_0000 bzw 16
     * übergeben werden.
     *
     * @param regRead Register, das ausgeslen wird
     * @param regWrite Register, in das die Änderungen geschrieben werden
     * @param Bitwert
     * @param bitpos Bitposition
     * @return Schreibstatus 1/-1
     */
    public int configPin(int regRead, int regWrite, int level, int bitpos){
        int regVal = read(regRead);
        if(regVal != -1){
            regVal = setBit(regVal, bitpos, level);
            return write(regWrite, (byte)regVal);
        }else{
            return -1;
        }   
    }    

    /**
     * configPinToggle liest von einem I2C-Gerät aus dem Register regRead, 
     * invertiert den Bitwert an der Stelle bitpos und schreibt die Daten 
     * in das Regsiter regWrite des I2C-Gerätes.
     * Im Fehlerfall wird -1 zurückgegeben, sonst 1.
     * Für bitpos muss zum Beispiel für Bit 5 der Wert 0b0001_0000 bzw 16
     * übergeben werden.
     *
     * @param regRead Register, das ausgeslen wird
     * @param regWrite Register, in das die Änderungen geschrieben werden
     * @param bitpos Bitposition
     * @return Schreibstatus 1/-1
     */
    public int configPinToggle(int regRead, int regWrite, int bitpos){
        int regVal = read(regRead);
        if(regVal != -1){
            if(getBit(regVal, bitpos)== 1){
                regVal = setBit(regVal, bitpos, 0);
            }else{
                regVal = setBit(regVal, bitpos, 1);
            }
            return write(regWrite, (byte)regVal);
        }else{
            return -1;
        }   
    }    

}

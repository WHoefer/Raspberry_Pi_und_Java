package raspi.logger;

import java.util.Calendar;

/**
 * SingleValue<br>
 * Containerklasse
 * 
 * @author Wolfgang Höfer
 * @version 1.0
 */
public class SingleValue
{
    /**
     * Konstante für den Datentyp Byte
     */
    public static final int TYPE_BYTE = 0; 
    /**
     * Konstante für den Datentyp Short
     */
    public static final int TYPE_SHORT = 1;
    /**
     * Konstante für den Datentyp Integer
     */
    public static final int TYPE_INTEGER = 2;
    /**
     * Konstante für den Datentyp Long
     */
    public static final int TYPE_LONG = 3;
    /**
     * Konstante für den Datentyp Double
     */
    public static final int TYPE_DOUBLE = 4;
    /**
     * Konstante für den Datentyp Float
     */
    public static final int TYPE_FLOAT = 5;
    /**
     * Konstante für den Datentyp byte
     */
    public static final int STYPE_BYTE = 6;
    /**
     * Konstante für den Datentyp short
     */
    public static final int STYPE_SHORT = 7;
    /**
     * Konstante für den Datentyp int
     */
    public static final int STYPE_INTEGER = 8;
    /**
     * Konstante für den Datentyp long
     */
    public static final int STYPE_LONG = 9;
    /**
     * Konstante für den Datentyp double
     */
    public static final int STYPE_DOUBLE = 10;
    /**
     * Konstante für den Datentyp float
     */
    public static final int STYPE_FLOAT = 11;
    /**
     * Konstante für den Datentyp String
     */
    public static final int TYPE_STRING = 12;
    /**
     * Konstante für den Datentyp char
     */
    public static final int STYPE_CHAR = 13;
    /**
     * Konstante für den Datentyp Char
     */
    public static final int TYPE_CHAR = 14;
    /**
     * Konstante für den Datentyp boolean
     */
    public static final int STYPE_BOOLEAN = 15;
    /**
     * Konstante für den Datentyp Boolean
     */
    public static final int TYPE_BOOLEAN = 16;
    
    private Calendar cal;
    private String singleValue;
    private int valueType;
    private String info;

    
    /**
     * Konstruktor für Objekte der Klasse SingleValue
     */
    public SingleValue(Calendar cal, String singleValue, int valueType, String info)
    {
        this.cal = cal;
        this.singleValue = singleValue;
        this.valueType = valueType;
        this.info = info;
    }

    public Calendar getCal(){
        return this.cal;
    }
    
    public String getSingleValue(){
        return this.singleValue;
    }
    
    public int getValueType(){
        return this.valueType;
    }
    
    public String getInfo(){
        return this.info;
    }
}

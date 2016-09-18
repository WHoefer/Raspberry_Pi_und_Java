package raspi.gui;


import javax.swing.*;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;

/**
 * Die Validator-Klasse kann Zeitangaben und Zeitintervalle, die in JTextField-Objekten stehen,
 * validieren.
 * 
 * @author Wolfgang Höfer 
 * @version 1.0
 */
public class Validator
{

    /**
     * Validiert die Zeitangabe aus dem JTextField und entfernt WhiteSpaces.
     * Der Inhalt des Textfeldes muss für eine positive Rückmeldung das 
     * folgende Format haben: HH:mm
     *
     * @param field JTextField mit Zeitangabe.
     * @return true wenn Validierung erfolgreich, sonst false.
     */
    public static boolean validateShortTime(JTextField field){
        String regEx = "[012]{1}[0123456789]{1}:[012345]{1}[0123456789]{1}";
        return validateRegEx(regEx, field);
    }

    /**
     * Validiert die Zeitangabe aus dem JTextField und entfernt WhiteSpaces.
     * Der Inhalt des Textfeldes muss für eine positive Rückmeldung das 
     * folgende Format haben: HH:mm:ss
     *
     * @param field JTextField mit Zeitangabe.
     * @return true wenn Validierung erfolgreich, sonst false.
     */
    public static boolean validateTime(JTextField field){
        String regEx = "[012]{1}[0123456789]{1}:[012345]{1}[0123456789]{1}:[012345]{1}[0123456789]{1}";
        return validateRegEx(regEx, field);
    }

    /**
     * Validiert ein Zeitintervall und entfernt WhiteSpaces.
     * Der Inhalt der Textfelder muss für eine positive Rückmeldung das 
     * folgende Format haben: HH:mm 
     * Außerdem muss die erst Zeitangabe vor der zweiten Zeitangabe liegen.
     *
     * @param from JTextField mit Zeitangabe.
     * @param to JTextField mit Zeitangabe.
     * @return true wenn Validierung erfolgreich, sonst false.
     */
    public static boolean validateShortTimeInterval(JTextField from, JTextField to){
        return validateRegExInterval(from, to, new SimpleDateFormat("HH:mm"));
    }

    /**
     * Validiert ein Zeitintervall und entfernt WhiteSpaces.
     * Der Inhalt der Textfelder muss für eine positive Rückmeldung das 
     * folgende Format haben: HH:mm:ss 
     * Außerdem muss die erst Zeitangabe vor der zweiten Zeitangabe liegen.
     *
     * @param from JTextField mit Zeitangabe.
     * @param to JTextField mit Zeitangabe.
     * @return true wenn Validierung erfolgreich, sonst false.
     */
    public static boolean validateTimeInterval(JTextField from, JTextField to){
        return validateRegExInterval(from, to, new SimpleDateFormat("HH:mm:ss"));
    }

    /**
     * Validiert ein Zeitintervall und entfernt WhiteSpaces.
     * Das Format der Textfelder wird durch den Parameter sdf vorgegeben. 
     * Außerdem muss die erst Zeitangabe vor der zweiten Zeitangabe liegen.
     *
     * @param from JTextField mit Zeitangabe.
     * @param to JTextField mit Zeitangabe.
     * @param sdf SimpleDateFormat zur Validierung.
     * @return true wenn Validierung erfolgreich, sonst false.
     */
    public static boolean validateRegExInterval(JTextField from, JTextField to, SimpleDateFormat sdf){
        Date dFrom;
        Date dTo;
        if(validateShortTime(from) && validateShortTime(to)){
            try{
                dFrom = sdf.parse(from.getText());
                dTo   = sdf.parse(to.getText());
            }catch(ParseException ex){
                return false;
            }   
            if(dFrom.getTime() < dTo.getTime()){
                return true;
            }
        }
        return false;
    }

    /**
     * Method validateRegEx
     *
     * @param regEx A parameter
     * @param field A parameter
     * @return The return value
     */
    public static boolean validateRegEx(String regEx, JTextField field){
        boolean valid = true;
        if(field != null){
            String value = field.getText();
            value = value.trim();
            field.setText(value);
            if(value.matches(regEx)){
                return true;
            }    
        }
        return false;
    }        
}

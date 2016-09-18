package raspi.projekte.kap14;

import raspi.schedule.ScheduleUtil;
import javax.swing.*;
import java.text.ParseException;
import java.awt.*;

/**
 * Write a description of class GuiUtil here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class GuiUtil
{
    private String von;
    private String bis;
    private String tag;
    private String commandString;
    private String view;
	private JTextField tBis;
	private JTextField tVon;
	private JComboBox cbTag;

    public static final String[] wochentage = {"Montag","Dienstag","Mittwoch","Donnerstag","Freitag","Samstag","Sonntag"};
    /**
     * Das Ausgabeformat
     * 0 1 2 3 4 5 6 7 8 9 0 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 
     * W O C H E N T A G              S  S  :  M  M     -  S  S  :  M  M
     */
    public static final String ausgabeFormat = "%1$-14.14s%2$-6.6s- %3$-6.6s";
    
    public GuiUtil(JTextField tVon, JTextField tBis, JComboBox cbTag){
        this.tVon = tVon;
        this.tBis = tBis;
        this.cbTag = cbTag;
    }
    
    
    public void setVon(String von){
        this.von = von;
    }

    public String getVon(){
        return von;
    }

    public void setBis(String bis){
        this.bis = bis;
    }

    public void setTag(String tag){
        this.tag = tag;
    }
    
    public String getBis(){
        return bis;
    }

    /**
     * getCommandString<br>
     * 
     * Erzeugt aus dem folgenden Anzeigeformat einen commandString.
     * 0 1 2 3 4 5 6 7 8 9 0 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27
     * W O C H E N T A G              S  S  :  M  M     -     S  S  :  M  M
     *
     * @param listElement Anzeigeformat
     * @return commandString
     */
    public static String getCommandString(String listElement){
        String retVal = "";
        String days = "";
        if(listElement.startsWith(wochentage[0])){
            days = "1000000";
        }else if(listElement.startsWith(wochentage[1])){    
            days = "0100000";
        }else if(listElement.startsWith(wochentage[2])){    
            days = "0010000";
        }else if(listElement.startsWith(wochentage[3])){    
            days = "0001000";
        }else if(listElement.startsWith(wochentage[4])){    
            days = "0000100";
        }else if(listElement.startsWith(wochentage[5])){    
            days = "0000010";
        }else if(listElement.startsWith(wochentage[6])){    
            days = "0000001";
        }    
            
        try{
            StringBuilder startTime =  new StringBuilder();
            startTime.append(listElement.substring(14,16));
            startTime.append(listElement.substring(17,19));
            startTime.append("00");
            StringBuilder stopTime =  new StringBuilder();
            stopTime.append(listElement.substring(22,24));
            stopTime.append(listElement.substring(25,27));
            stopTime.append("00");
            retVal = ScheduleUtil.startFromTo(startTime.toString(), stopTime.toString(), days);
        }catch(ParseException pex){
            return "";
        }
        return retVal;
    }    
    /**
     * Method getListElement<br>
     * 
     * Erzeugt eine formatierte Ausgabe.
     *
     * @return Ausgabe
     */
    public String getListElement(){
        String tag =  (String)cbTag.getSelectedItem();
        String von = tVon.getText();
        String bis = tBis.getText();
        return String.format(ausgabeFormat, tag, von, bis );
    }

    public static String getListElement(String commandString){
        String tag = "";
        if(ScheduleUtil.isMonday(commandString)){
            tag = wochentage[0];
        }else if(ScheduleUtil.isTuesday(commandString)){
            tag = wochentage[1];
        }else if(ScheduleUtil.isWednesday(commandString)){
            tag = wochentage[2];
        }else if(ScheduleUtil.isThursday(commandString)){
            tag = wochentage[3];
        }else if(ScheduleUtil.isFriday(commandString)){
            tag = wochentage[4];
        }else if(ScheduleUtil.isSaturday(commandString)){
            tag = wochentage[5];
        }else if(ScheduleUtil.isSunday(commandString)){
            tag = wochentage[6];
        }
        String von = ScheduleUtil.getFromTime(commandString);
        String bis = ScheduleUtil.getToTime(commandString);
        return String.format(ausgabeFormat, tag, von, bis );
    }
    
    public static void init(JComponent jc, int x, int y, int w, int h, Font font, Color fc, Color bc){
        jc.setBounds(x, y, w, h);
        jc.setEnabled(true);
        jc.setVisible(true);
        jc.setFont(font);
        jc.setForeground(fc);
        jc.setBackground(bc);
    }
    
}

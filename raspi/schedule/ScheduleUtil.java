package raspi.schedule;

import java.util.Calendar;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;

/**
 * ScheduleUtil ist eine Hilfsklasse, die Schedule-Commands
 * erzeugen kann, so wie die Methode scheduleCheck der Klasse
 * ScheduleService diese benötigt.
 * 
 * @author Wolfgang Höfer
 * @version 1.0
 */
public class ScheduleUtil
{

    /**
     * Method addMinutes<br>
     * 
     * Die beiden Integer-Werte werden int konvertiert und addiert.
     * Der Rückgabewert entspricht der Summe als String.
     *
     * @param duration1 A parameter
     * @param duration2 A parameter
     * @return The return value
     */
    public static String addMinutes(String duration1, String duration2) throws NumberFormatException{
        int d1 = Integer.parseInt(duration1);
        int d2 = Integer.parseInt(duration2);
        int d = d1+d2;
        return Integer.toString(d);
    }

    /**
     * Method activeForMinutes<br>
     * 
     * Erzeugt ein Schedule-Command, das eine Aktivität einer 
     * definierten Dauer erzeugt. Die Dauer wird durch den
     * Übergabeparameter duration in Minuten festgelegt.
     *
     * @param duration Aktivität in Minuten
     * @return Schedule-Command
     */
    public static String activeForMinutes(String duration) throws NumberFormatException{
        return startPulseInMinutes(
            Integer.parseInt("0"), 
            Integer.parseInt(duration), 
            Integer.parseInt("00000"));
    }

    /**
     * Method startInMinutes<br>
     *
     * Erzeugt ein Schedule-Command, das eine Aktivität einer 
     * definierten Dauer erzeugt und nach einer vorgegeben Zeit
     * aktiviert wird. Die Dauer wird durch den
     * Übergabeparameter duration in Minuten festgelegt. Der
     * Startzeitpunkt wird durch den Parameter startMinutes übergeben.
     * 
     * @param startMinutes Gibt den Start der Aktivität in Minuten an. 
     * @param duration Gibt die Dauer der Aktivität in Minuten an.
     * @return Schedule-Command
     */
    public static String startInMinutes(String startMinutes, String duration) throws NumberFormatException{
        return startPulseInMinutes(
            Integer.parseInt(startMinutes), 
            Integer.parseInt(duration), 
            Integer.parseInt("00000"));
    }

    /**
     * Method startPulseInMinutes<br>
     *
     * Erzeugt ein Schedule-Command, das eine Aktivität einer 
     * definierten Dauer erzeugt und nach einer vorgegeben Zeit
     * aktiviert wird. Die Aktivität pusiert mit der durch den
     * Parameter pulse angegebenen Zeit in Sekunden. Die Dauer wird durch den
     * Übergabeparameter duration in Minuten festgelegt. Der
     * Startzeitpunkt wird durch den Parameter startMinutes übergeben.
     * 
     * @param startMinutes Gibt den Start der Aktivität in Minuten an. 
     * @param duration Gibt die Dauer der Aktivität in Minuten an.
     * @param pulse Gibt die Pulsdauer in Sekunden an, mit der die Aktivität 
     * ein- und ausgeschaltet wird.
     * @return Schedule-Command
     */
    public static String startPulseInMinutes(String startMinutes, String duration, String pulse) throws NumberFormatException{
        return startPulseInMinutes(
            Integer.parseInt(startMinutes), 
            Integer.parseInt(duration), 
            Integer.parseInt(pulse));
    }

    /**
     * Method startPulseInMinutes<br>
     *
     * Erzeugt ein Schedule-Command, das eine Aktivität einer 
     * definierten Dauer erzeugt und nach einer vorgegeben Zeit
     * aktiviert wird. Die Aktivität pulsiert mit der durch den
     * Parameter pulse angegebenen Zeit in Sekunden. Die Dauer wird durch den
     * Übergabeparameter duration in Minuten festgelegt. Der
     * Startzeitpunkt wird durch den Parameter startMinutes übergeben.
     * 
     * @param startMinutes Gibt den Start der Aktivität in Minuten an. 
     * @param duration Gibt die Dauer der Aktivität in Minuten an.
     * @param pulse Gibt die Pulsdauer in Sekunden an, mit der die Aktivität 
     * ein- und ausgeschaltet wird.
     * @return Schedule-Command
     */
    public static String startPulseInMinutes(int startMinutes, int duration, int pulse){
        StringBuilder command = new StringBuilder();  

        Calendar startDate  = Calendar.getInstance();
        startDate.add(Calendar.MINUTE, startMinutes);
        Calendar stopDate  = Calendar.getInstance();
        stopDate.add(Calendar.MINUTE, startMinutes + duration);

        if(pulse > 99999){
            pulse = 99999;
        }    

        command.append(ScheduleService.dateFormat.format(startDate.getTime()));
        command.append(ScheduleService.dateFormat.format(stopDate.getTime()));
        command.append(ScheduleService.ALLDAYSOFWEEK);
        command.append(ScheduleService.timeFormat.format(startDate.getTime()));
        command.append(ScheduleService.timeFormat.format(stopDate.getTime()));
        command.append(String.format("%1$05d", pulse));

        return command.toString();
    }

    /**
     * Method startPulseInMinutes<br>
     *
     * Erzeugt ein Schedule-Command, das eine Aktivität einer 
     * definierten Dauer erzeugt und nach einer vorgegeben Zeit
     * aktiviert wird. Die Aktivität pulsiert mit der durch den
     * Parameter pulse angegebenen Zeit in Sekunden. Die Dauer wird durch den
     * Übergabeparameter duration in Sekunden festgelegt. Der
     * Startzeitpunkt wird durch den Parameter startSeconds übergeben.
     * 
     * @param startMinutes Gibt den Start der Aktivität in Sekunden an. 
     * @param duration Gibt die Dauer der Aktivität in Sekunden an.
     * @param pulse Gibt die Pulsdauer in Sekunden an, mit der die Aktivität 
     * ein- und ausgeschaltet wird.
     * @return Schedule-Command
     */
    public static String startPulseInSecundes(int startSeconds, int duration, int pulse){
        StringBuilder command = new StringBuilder();  

        Calendar startDate  = Calendar.getInstance();
        startDate.add(Calendar.SECOND, startSeconds);
        Calendar stopDate  = Calendar.getInstance();
        stopDate.add(Calendar.SECOND, startSeconds + duration);

        if(pulse > 99999){
            pulse = 99999;
        }    

        command.append(ScheduleService.dateFormat.format(startDate.getTime()));
        command.append(ScheduleService.dateFormat.format(stopDate.getTime()));
        command.append(ScheduleService.ALLDAYSOFWEEK);
        command.append(ScheduleService.timeFormat.format(startDate.getTime()));
        command.append(ScheduleService.timeFormat.format(stopDate.getTime()));
        command.append(String.format("%1$05d", pulse));

        return command.toString();
    }

    /**
     * Method startFromTo<br>
     *
     * Erzeugt ein Schedule-Command, für das die übergebenen Tage von der startTime bis zur stopTime aktiv ist
     * und nicht pulsiert. Die übergebenen Zeiten müssen das Format HHmmss haben. Die Tage werden im Format 
     * wwwwwww übergeben. Das erste w steht hier für den Montag und kann 1 für aktiv oder 0 für inaktiv gesetzt
     * werden. Die weiteren Felder sind die Wochentage bis einschließlich Sonntag. 1111111 setzte alle Wochentage 
     * auf aktiv und 0000000 setzt alle Wochentage inaktiv.
     *
     * @param startTime Startzeit HHmmss
     * @param stopTime Stopzeit HHmmss
     * @param days Tage in der Woche wwwwwww
     * @return Schedule-Command
     */
    public static String startFromTo(String startTime, String stopTime, String days) throws ParseException{
        if(!days.matches("[01]{7}")){
            throw new ParseException("days muss 7 Zeichen lang sein und darf nur die Zeichen 0 und 1 enthalten",1);
        }
        StringBuilder command = new StringBuilder();  
        command.append("01011970");
        command.append("01013000");
        command.append(days);
        command.append(startTime);
        command.append(stopTime);
        command.append("00000");
        return command.toString();
    }

    /**
     * Method startFromToAllDays<br>
     *
     * Erzeugt ein Schedule-Command, das für alle Tage in der Woche von der startTime bis zur stopTime aktiv ist
     * und nicht pulsiert. Die übergebenen Zeiten müssen das Format HHmmss haben. 
     *
     * @param startTime Startzeit HHmmss
     * @param stopTime Stopzeit HHmmss
     * @return Schedule-Command
     */
    public static String startFromToAllDays(String startTime, String stopTime)throws ParseException{
        return startFromTo(startTime, stopTime, ScheduleService.ALLDAYSOFWEEK);
    }

    /**
     * Method allwaysOn
     *
     * @return String Schedule-Command
     */
    public static String allwaysOn(){
        StringBuilder command = new StringBuilder();  
        command.append("01011970");
        command.append("01013000");
        command.append(ScheduleService.ALLDAYSOFWEEK);
        command.append("000000");
        command.append("235959");
        command.append("00000");
        return command.toString();

    }    

    /**
     * Method allwaysOff
     *
     * @return String Schedule-Command
     */
    public static String allwaysOff(){
        StringBuilder command = new StringBuilder();  
        command.append("01011970");
        command.append("01011970");
        command.append("0000000");
        command.append("000000");
        command.append("000000");
        command.append("00000");
        return command.toString();

    } 

    public static String getFromTimeWithSeconds(String command){
        StringBuilder retVal = new StringBuilder(); 
        retVal.append(command.substring(23, 25));
        retVal.append(":");
        retVal.append(command.substring(25, 27));
        retVal.append(":");
        retVal.append(command.substring(27, 29));
        return retVal.toString();
    }

    public static String getFromTime(String command){
        return getFromTimeWithSeconds(command).substring(0, 5);
    }

    public static String getToTimeWithSeconds(String command){
        StringBuilder retVal = new StringBuilder(); 
        retVal.append(command.substring(29, 31));
        retVal.append(":");
        retVal.append(command.substring(31, 33));
        retVal.append(":");
        retVal.append(command.substring(33, 35));
        return retVal.toString();
    }

    public static String getToTime(String command){
        return getToTimeWithSeconds(command).substring(0, 5);
    }

    public static boolean isMonday(String command){
        if(command.substring(16,23).equals("1000000")){
            return true;
        }
        return false;
    }

    public static boolean isTuesday(String command){
        if(command.substring(16,23).equals("0100000")){
            return true;
        }
        return false;
    }

    public static boolean isWednesday(String command){
        if(command.substring(16,23).equals("0010000")){
            return true;
        }
        return false;
    }

    public static boolean isThursday(String command){
        if(command.substring(16,23).equals("0001000")){
            return true;
        }
        return false;
    }

    public static boolean isFriday(String command){
        if(command.substring(16,23).equals("0000100")){
            return true;
        }
        return false;
    }

    public static boolean isSaturday(String command){
        if(command.substring(16,23).equals("0000010")){
            return true;
        }
        return false;
    }

    public static boolean isSunday(String command){
        if(command.substring(16,23).equals("0000001")){
            return true;
        }
        return false;
    }

}

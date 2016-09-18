package raspi.schedule;

import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;

/**
 * Write a description of class ScheduleService here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class ScheduleService
{
    public static DateFormat dateFormat = new SimpleDateFormat("ddMMyyyy"); 
    public static DateFormat timeFormat = new SimpleDateFormat("HHmmss"); 
    public static DateFormat dateTimeFormat = new SimpleDateFormat("ddMMyyyyHHmmss"); 
    public static DateFormat testFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss:SS"); 
    public static final String ALLDAYSOFWEEK = "1111111";

    /**
     * scheduleCheckForDalyUse prüft, ob sich die im übergebenen Paramater command enthaltenen
     * Zeitbereiche mit der aktuellen Zeit decken.
     * <br>
     * Der übergebene command String muss das folgende Format haben:<br><br>
     * 
     * Zeichen 0-7  Startdatum im Format ddMMyyyy<br>
     * Zeichen 8-15 Endedatum  im Format ddMMyyyy<br>
     * Zeichen 16-22 Wochentage, die mit Montag beginnen und den Wert 0(inaktiv) oder 1(aktiv) haben kann<br>
     * Zeichen 23-28 Startzeit im Format HHmmss<br>
     * Zeichen 29-34 Endezeit im Format HHmmss<br>
     * Zeichen 35-39 halbe Pulsdauer im Format sssss (fünfstellige Sekundenangabe)<br><br>
     * 
     * Startdatum Endedatum Mo Di Mi Do Fr Sa So Startzeit         Endezeit          Puls<br>
     * ddMMyyyy   ddMMyyyy  w  w  w  w  w  w  w  H  H  m  m  s  s  H  H  m  m  s  s  S  S  S  S  S<br>
     * 01234567   89012345  16 17 18 19 20 21 22 23 24 25 26 27 28 29 30 31 32 33 34 35 36 37 38 39<br><br>
     *               
     * scheduleCheckForDalyUse gibt true zurück, wenn die folgenden Bedingungen erfüllt sind:<br>
     * 1. das aktuelle Datum zwischen dem Start- und Stoppdatum liegt<br>
     * 2. der aktuelle Wochentag aktiv geschaltet ist <br>
     * 3. die aktuelle Zeit zwischen der Start- und Endezeit liegt<br>
     * 4. die halbe Pulsdauer von der Tagesstartzeit an gerade aktiv ist<br><br>
     * 
     * Wenn für die halbe pulsdauer 00000 angegeb wird, ist der Puls ausgeschaltet und immer aktiv.<br>
     * Alle Angaben sind Tagesbezogen und zwischen dem Start- und Stoppdatum aktiv.<br>
     * <br>
     * @param  command ddMMyyyyddMMyyyywwwwwwwHHmmssHHmmssSSSSS
     * @return  boolean Gibt true zurück, wenn alle vier Bedingungen erfüllt sind
     */
    public synchronized static boolean scheduleCheckForDalyUse(String command) throws ParseException
    {   if(isInDateInterval(command) && 
        isInTimeInterval(command) && 
        isInDayOfWeekInterval(command) &&
        isPulseDalyActiv(command)){
            return true;
        }  
        return false;
    }

    /**
     * scheduleCheckForLongTimeUse prüft, ob sich die im übergebenen Paramater command enthaltenen
     * Zeitbereiche mit der aktuellen Zeit decken.
     * <br>
     * Der übergebene command String muss das folgende Format haben:<br><br>
     * 
     * Zeichen 0-7  Startdatum im Format ddMMyyyy<br>
     * Zeichen 8-15 Endedatum  im Format ddMMyyyy<br>
     * Zeichen 16-22 Wochentage, die mit Montag beginnen und den Wert 0(inaktiv) oder 1(aktiv) haben kann<br>
     * Zeichen 23-28 Startzeit im Format HHmmss<br>
     * Zeichen 29-34 Endezeit im Format HHmmss<br>
     * Zeichen 35-39 halbe Pulsdauer im Format sssss (fünfstellige Sekundenangabe)<br><br>
     * 
     * Startdatum Endedatum Mo Di Mi Do Fr Sa So Startzeit         Endezeit          Puls
     * ddMMyyyy   ddMMyyyy  w  w  w  w  w  w  w  H  H  m  m  s  s  H  H  m  m  s  s  S  S  S  S  S<br>
     * 01234567   89012345  16 17 18 19 20 21 22 23 24 25 26 27 28 29 30 31 32 33 34 35 36 37 38 39<br><br>
     *               
     * scheduleCheckForLongTimeUse gibt true zurück, wenn die folgenden Bedingungen erfüllt sind:<br>
     * 1. das aktuelle Datum/Zeit zwischen dem Start- und Stopp-datum/zeit liegt<br>
     * 2. der aktuelle Wochentag aktiv geschaltet ist<br>
     * 3. die halbe Pulsdauer von dem Startdatum und Startzeit an gerade aktiv ist<br><br>
     * 
     * Wenn für die halbe pulsdauer 00000 angegeb wird, ist der Puls ausgeschaltet und immer aktiv.<br><br>
     * 
     * @param  command ddMMyyyyddMMyyyywwwwwwwHHmmssHHmmssSSSSS
     * @return  boolean Gibt true zurück, wenn alle vier Bedingungen erfüllt sind
     */
    public synchronized static boolean scheduleCheckForLongTimeUse(String command) throws ParseException
    {   if(isInDateTimeInterval(command) && 
        isInDayOfWeekInterval(command) &&
        isPulseLongActiv(command)){
            return true;
        }  
        return false;
    }

    
    
    
    /**
     * isInDateTimeInterval wertet aus dem übergebenen Parameter command 
     * den Startzeitpunkt und Endezeitpunkt mit Datum und Uhrzeit aus. Wenn
     * der aktuelle Zeitpunkt inklusiv Datum in diesen Bereich fällt, wird true 
     * zurück gegeben. Sonst immer false.
     *
     * @param command 
     * @return boolean 
     */
    public synchronized static boolean isInDateTimeInterval(String command) throws ParseException 
    {
        Calendar startCal = Calendar.getInstance();
        Calendar endCal = Calendar.getInstance();
        Calendar currentDate  = Calendar.getInstance();

        startCal.setTime(dateTimeFormat.parse(command.substring(0,8)+command.substring(23, 29)));
        endCal.setTime(dateTimeFormat.parse(command.substring(8,16)+command.substring(29, 35)));

        truncMilli(startCal);
        truncMilli(endCal);
        truncMilli(currentDate);

        long start = startCal.getTimeInMillis();
        long end = endCal.getTimeInMillis();
        long current = currentDate.getTimeInMillis();

        if(current >= start && current <= end){
            return true;
        }    
        return false;
    }

    /**
     * isInDateInterval wertet aus dem übergebenen Parameter command 
     * das Startdatum und Endedatum aus. Wenn das aktuelle Datum in 
     * diesen Bereich fällt, wird true zurück gegeben. Sonst immer false.
     *
     * @param command 
     * @return boolean 
     */
    public synchronized static boolean isInDateInterval(String command) throws ParseException 
    {
        Calendar startCal = Calendar.getInstance();
        Calendar endCal = Calendar.getInstance();
        Calendar currentDate  = Calendar.getInstance();

        startCal.setTime(dateFormat.parse(command.substring(0,8)));
        endCal.setTime(dateFormat.parse(command.substring(8,16)));

        truncTime(startCal);
        truncTime(endCal);
        truncTime(currentDate);

        long start = startCal.getTimeInMillis();
        long end = endCal.getTimeInMillis();
        long current = currentDate.getTimeInMillis();

        if(current >= start && current <= end){
            return true;
        }    
        return false;
    }

    /**
     * isInTimeInterval wertet aus dem übergebenen Parameter command 
     * den Start- und Endezeitpunkt aus. Wenn die aktuelle Zeit in 
     * diesen Bereich fällt, wird true zurück gegeben. Sonst immer false.
     *
     * @param command 
     * @return boolean 
     */
    public synchronized static boolean isInTimeInterval(String command) throws ParseException 
    {
        Calendar startCal = Calendar.getInstance();
        Calendar endCal = Calendar.getInstance();
        Calendar currentDate  = Calendar.getInstance();

        startCal.setTime(timeFormat.parse(command.substring(23,29)));
        endCal.setTime(timeFormat.parse(command.substring(29,35)));

        truncDate(startCal);
        truncDate(endCal);
        truncDate(currentDate);

        long start = startCal.getTimeInMillis();
        long end = endCal.getTimeInMillis();
        long current = currentDate.getTimeInMillis();

        if(current >= start && current <= end){
            return true;
        }    
        return false;
    }

    /**
     * isInDayOfWeekInterval wertet aus dem Übergabeparameter command die
     * Wochentage aus. Es wird true zurück gegeben, wenn der aktuelle Wochentag
     * im Übergabeparameter aktiviert ist. Sonst immer false.
     *
     * @param command 
     * @return boolean 
     */
    public synchronized static boolean isInDayOfWeekInterval(String command) throws ParseException 
    {
        Calendar currentDate  = Calendar.getInstance();

        int currentDayOfWeek = currentDate.get(Calendar.DAY_OF_WEEK);
        int offset = 0;
        switch(currentDayOfWeek){
            case Calendar.MONDAY:    offset = 0; break;
            case Calendar.TUESDAY:   offset = 1; break;
            case Calendar.WEDNESDAY: offset = 2; break;
            case Calendar.THURSDAY: offset = 3; break;
            case Calendar.FRIDAY: offset = 4; break;
            case Calendar.SATURDAY: offset = 5; break;
            case Calendar.SUNDAY: offset = 6; break;
        }
        //Position 16 in command ist der Montag
        String dayStr = command.substring(16+offset,17+offset);
        if(dayStr.equals("1")){
            return true;
        }
        return false;
    }

    /**
     * isPulseDalyActiv wertet aus dem Übergabeparameter command die halbe
     * Pulsangabe aus und die Startzeit aus. Es wird true zurück gegeben, wenn 
     * vom Tagesstartzeitpunkt an die halbe Pulsdauer in einem geraden vielfachen 
     * zur aktuellen Zeit steht. Sonst immer false.
     *
     * @param command 
     * @return boolean 
     */
    public synchronized static boolean isPulseDalyActiv(String command) throws ParseException 
    {
        Calendar startCal = Calendar.getInstance();
        Calendar currentDate  = Calendar.getInstance();

        //dateTimeFormat.parse(command.substring(0,8)+command.substring(23, 29)
        startCal.setTime(timeFormat.parse(command.substring(23,29)));
        truncDate(startCal);
        truncDate(currentDate);

        long start = startCal.getTimeInMillis();  
        long current = currentDate.getTimeInMillis();
        String pulseStr = command.substring(35);
        long pulseWidth = Integer.parseInt(pulseStr) * 1000L;
        if(pulseWidth == 0){
            return true;
        }    
        long div = current - start;
        long times = div / pulseWidth;
        if((times % 2) == 0 ){
            return true;
        }
        return false;
    }

    /**
     * isPulseDalyActiv wertet aus dem Übergabeparameter command die halbe
     * Pulsangabe, das Startdatum und die Startzeit aus. Es wird true zurück gegeben, 
     * wenn vom dem Startdatum und dem Startzeitpunkt an die halbe Pulsdauer 
     * in einem geraden vielfachen zur aktuellen Zeit steht. Sonst immer false.
     *
     * @param command 
     * @return boolean 
     */
    public synchronized static boolean isPulseLongActiv(String command) throws ParseException 
    {
        Calendar startCal = Calendar.getInstance();
        Calendar currentDate  = Calendar.getInstance();

        startCal.setTime(dateTimeFormat.parse(command.substring(0,8)+command.substring(23, 29)));
        truncMilli(startCal);
        truncMilli(currentDate);

        long start = startCal.getTimeInMillis();  
        long current = currentDate.getTimeInMillis();
        String pulseStr = command.substring(35);
        long pulseWidth = Integer.parseInt(pulseStr) * 1000L;
        if(pulseWidth == 0){
            return true;
        }    
        long div = current - start;
        long times = div / pulseWidth;
        if((times % 2) == 0 ){
            return true;
        }
        return false;
    }

    /**
     * truncMilli setzt die Millisekunden in dem übergebene Calendar-Objekt
     * auf 0.
     *
     * @param cal Calendar
     */
    private synchronized static void truncMilli(Calendar cal){
        cal.set(Calendar.MILLISECOND, 0);
    }    

    /**
     * truncTime setzt die Zeitangaben Stunde, Minute, Sekunde und Millisekunde 
     * in dem übergebene Calendar-Objekt auf 0.
     *
     * @param cal Calendar
     */
    private synchronized static void truncTime(Calendar cal){
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
    }    

    /**
     * truncDate setzt die Zeitangaben Jahr, Monat, Tag und Millisekunde 
     * in dem übergebene Calendar-Objekt auf 0.
     *
     * @param cal Calendar
     */
    private synchronized static void truncDate(Calendar cal){
        cal.set(Calendar.YEAR, 1970);
        cal.set(Calendar.MONTH, Calendar.JANUARY);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.MILLISECOND, 0);
    }    

}

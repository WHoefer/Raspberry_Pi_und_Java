package raspi.projekte.kap05;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.io.IOException;
import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.Locale;

/**
 * Das Programm ermittelt, wie viele Arbeitstage zwischen Weihnachten 
 * und Silvester liegen.
 * @author  Wolfgang Höfer
 * @version 1.0.0
 */
public class ArbeitstageWeihnachtenSilvester
{


    static TimeZone zeitzone = TimeZone.getTimeZone("Europe/Berlin");
    static Locale sprachraum = Locale.GERMANY;
    static Calendar calendar = null;

    private static int wochentag(int count, int day, int jahr){
        calendar.set(jahr, Calendar.DECEMBER, day);
        int wochentag = calendar.get(Calendar.DAY_OF_WEEK);
        int montag = Calendar.MONDAY;
        int freitag = Calendar.FRIDAY;
        if(wochentag >= montag && wochentag <= freitag)
        {
            count++;
        }    
        return count;
    }    

    public static void main(String[] args) throws IOException{
        System.out.println("Wie viele Arbeitstage liegen zwischen Weihnachten und Silvester?");

        calendar = new GregorianCalendar(zeitzone, sprachraum);
        int jahr = calendar.get(Calendar.YEAR);
        int count = 0;
        for(int i = 0;i < 21; i++){
            count =  wochentag(count, 27, jahr);
            count =  wochentag(count, 28, jahr);
            count =  wochentag(count, 29, jahr);
            count =  wochentag(count, 30, jahr);
            System.out.println(jahr + " gibt es " + count + " Arbeitstage zwischen Weihnachten und Silvester.");
            calendar.add(Calendar.YEAR,1);
            jahr = calendar.get(Calendar.YEAR);
            count = 0;
        }

    }

}

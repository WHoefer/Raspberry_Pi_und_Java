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
 * Das Programm gibt Ihnen von Ihrer Geburt an bis zum 80. Geburtstag 
 * Ihr Alter, das Jahr und den Wochentag, an dem Sie Geburtstag haben, aus.
 * 
 * @author  Wolfgang Höfer
 * @version 1.0.0
 */
public class Geburtstage
{

    public static void main(String[] args) throws IOException{
        System.out.println("An welchem Wochentag haben Sie Geburtstag!");
        System.out.println("Geben Sie bitte Ihr Geburtsdatum ein!");
        System.out.println("Zum Beispiel: 30.05.2003");

        DateFormat datumsFormat = new SimpleDateFormat("dd.MM.yyyy");
        DateFormat datumAusgabe = new SimpleDateFormat("yyyy EEEE");
        String lineRead = "";
        Date geburtstag = null;
        boolean check = true;
        BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
        while (check) {
            try{
                lineRead = console.readLine();
                geburtstag = datumsFormat.parse(lineRead);
                check = false;
            }catch(ParseException ex){
                System.out.println("Falsches Format!");
                System.out.println(
                  "Bitte folgendes Format verwenden: 30.05.2003");
                check = true;
            }
        }
        TimeZone zeitzone = TimeZone.getTimeZone("Europe/Berlin");
        Locale sprachraum = Locale.GERMANY;
        Calendar calendar = new GregorianCalendar(zeitzone, sprachraum);
        calendar.setTime(geburtstag);
        for(int i = 0;i < 81; i++){
             System.out.println("Alter: " + i + "  "+ datumAusgabe.format(calendar.getTime()));
             calendar.add(Calendar.YEAR,1);
        }

    }

}

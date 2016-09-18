package raspi.process;


import java.io.InputStreamReader;
import java.io.BufferedReader;

/**
 * Kommandos als externe Prozesse ausführen.
 * 
 * @author Wolfgang Höfer
 * @version 1.0
 */
public class Shell
{

 
    
    /**
     * Method executeAndWaitFor<br>
     * 
     * Das übergebene Kommando wird als externer Prozess ausgeführt und 
     * gewartet bis dieser beendet ist. Es wird true zurückgegeben, wenn 
     * der Prozess erfolgreich ausgeführt werden konnte. Im 
     * Fehlerfall wird false zurückgegeben;
     *
     * @param command Kommando, das als externer Prozess ausgeführt werden soll.
     * @return true oder false
     */
    public static boolean executeAndWaitFor(String command){
        try{
            Process p = Runtime.getRuntime().exec(command);
            p.waitFor();
        }catch(Exception e) {
            e.printStackTrace();
            return false;            
        }
        return true;    
    }

    /**
     * Method execute<br>
     * 
     * Das übergebene Kommando wird als externer Prozess ausgeführt. Es wird true 
     * zurückgegeben, wenn der Prozess erfolgreich ausgeführt werden konnte. Im 
     * Fehlerfall wird false zurückgegeben;
     *
     * @param command Kommando, das als externer Prozess ausgeführt werden soll.
     * @return true oder false
     */
    public static boolean execute(String command){
        try{
            Process p = Runtime.getRuntime().exec(command);
        }catch(Exception e) {
            e.printStackTrace();
            return false;            
        }
        return true;    
    }

    
    /**
     * Method executeWithResponse<br>
     *
     * Das übergebene Kommando wird als externer Prozess ausgeführt. Es wird die 
     * Systemantwort als Zeichenkette zurückgegeben. Im Fehlerfall wird null 
     * zurückgegeben;
     *
     * @param command Kommando, das als externer Prozess ausgeführt werden soll.
     * @return Die Systemantwort oder null im Fehlerfall.
     */
    public static String executeWithResponse(String command){
        StringBuffer sb = new StringBuffer(); 
        try{
            Process p = Runtime.getRuntime().exec(command);
            p.waitFor();
            InputStreamReader ir = new InputStreamReader(p.getInputStream());
            BufferedReader r = new BufferedReader(ir);
            
            String line = "";
            while((line = r.readLine()) != null){
                sb.append(line + "\n");
            }
        }catch(Exception e) {
            e.printStackTrace();
            return null;
        }    
        return sb.toString();
    }

}

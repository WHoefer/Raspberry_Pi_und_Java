package raspi.files;

import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.StandardOpenOption;
import java.nio.file.OpenOption;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Objekt zum Beschreiben einer Datei. Bei jedem Beschreiben der Datei, wird
 * automatisch ein Zeilenumbruch eingefügt. Für den Übergabeparameter OpenOption
 * gelten die folgenden Regeln:<br><br>
 * 
 * StandardOpenOption.CREATE  Neue Datei wird erzeugt. Wenn die Datei bereits besteht, wird
 * der Inhalt vor dem beschreiben gelöscht.<br>
 * StandardOpenOption.WRITE   Datei muss existieren, der alte Inhalt wird aber vor dem 
 * Beschreiben verworfen.<br>
 * StandardOpenOption.APPEND  Datei muss existieren, der neue Inhalt wird angefügt.<br>
 * 
 * @author Wolfgang Höfer 
 * @version 1.0
 */
public class ReadLines
{

    FileSystem fileSystem = null;
    Path path = null;
    BufferedReader reader = null;

    /**
     * WriteLines Constructor<br>
     * 
     * Objekt zum Beschreiben einer Datei. Bei jedem Beschreiben der Datei, wird
     * automatisch ein Zeilenumbruch eingefügt. Für den Übergabeparameter OpenOption
     * gelten die folgenden Regeln:<br><br>
     * 
     * StandardOpenOption.CREATE  Neue Datei wird erzeugt. Wenn die Datei bereits besteht, wird
     * der Inhalt vor dem beschreiben gelöscht.<br>
     * StandardOpenOption.WRITE   Datei muss existieren, der alte Inhalt wird aber vor dem 
     * Beschreiben verworfen.<br>
     * StandardOpenOption.APPEND  Datei muss existieren, der neue Inhalt wird angefügt.<br>
     *
     * @param strPath Pfad
     * @param strFile Dateiname
     * @param options OpenOption
     */
    public ReadLines(String strPath, String strFile) throws IOException{
        fileSystem = FileSystems.getDefault();
        path = fileSystem.getPath(strPath, strFile);
        reader = Files.newBufferedReader(path, StandardCharsets.UTF_8);
    }


    /**
     * Method readLn<br>
     * 
     * Liest zeilenweise aus einer Datei und speichert alle eingelsenen Zeilen
     * in eine ArrayList und gibt diese zurück. Im Fehlerfall wird null zurück
     * gegeben.
     *
     * @return Dateiinhalt als Liste.
     */
    public List<String> readLn(){
        String line = null;
        List list = new ArrayList();
        try {
            while((line = reader.readLine()) != null){
              list.add(line);
            }
        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);
            return null;
        }
        return list;
    }    

    /**
     * Method close<br>
     * 
     * Eingabe-Stream wird geschlossen. Bei erfolgreicher Ausführung wird true zurückgegeben. 
     * Im Fehlerfall wird false zurückgegeben.
     * 
     * 
     * @return boolean true/false
     */
    public boolean close(){
        if(reader != null){
            try{
                reader.close();
            }catch(IOException ex){
                System.err.format("IOException: %s%n", ex);
                return false;
            }  
        }
        return true;
    }

    public static void main(String[] args){
        try{
            /* Neue Datei erzeugen. Alter Inhalt wird gelöscht */
            WriteLines wl = new WriteLines("/home/pi/", "test.txt", StandardOpenOption.CREATE);
            wl.writeLn("Create 11");
            wl.writeLn("Create 22");
            wl.writeLn("Create 33");
            wl.close();
            /* Ein bestehendes File öffnen. Der alte Inhalt wird überschrieben */ 
            wl = new WriteLines("/home/pi/", "test.txt", StandardOpenOption.WRITE);
            wl.writeLn("                  Write 1");
            wl.writeLn("                  Write 2");
            wl.close();
            /* Ein bestehendes File öffnen. Inhalte werden angehangen */ 
            wl = new WriteLines("/home/pi/", "test.txt", StandardOpenOption.APPEND);
            wl.writeLn("                  Append 1");
            wl.writeLn("                  Append 2");
            wl.writeLn("                  Append 3");
            wl.close();
            
            
            ReadLines rl = new ReadLines("/home/pi/", "test.txt");
            List list = rl.readLn();
            Iterator it = list.iterator();
            while(it.hasNext()){
                String line = (String)it.next();
                System.out.println(line);
            }
            System.out.println(System.getProperty("user.dir"));
            System.out.println(System.getProperty("user.home"));
            System.out.println(System.getProperty("user.name"));
        }catch(IOException ex ){
            System.err.format("IOException: %s%n", ex);
        }

    }

}


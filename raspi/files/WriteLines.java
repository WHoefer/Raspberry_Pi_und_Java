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
public class WriteLines
{

    FileSystem fileSystem = null;
    Path path = null;
    BufferedWriter writer = null;

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
    public WriteLines(String strPath, String strFile, OpenOption options ) throws IOException{
        fileSystem = FileSystems.getDefault();
        path = fileSystem.getPath(strPath, strFile);
        writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8, options);
    }

    /**
     * Method writeLn<br>
     * 
     * Die übergebene Zeichenkette wird in die Datei geschrieben und ein Newline angefügt.
     * Bei erfolgreicher Ausführung wird true zurückgegeben. Im Fehlerfall wird false 
     * zurückgegeben.
     *
     * @param line Zeichenkette, die in die Datei geschrieben werden soll.
     * @return boolean true/false
     */
    public boolean writeLn(String line){
        try {
            writer.write(line);
            writer.newLine();
            writer.flush();
        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);
            return false;
        }
        return true;
    }    

    /**
     * Method close<br>
     * 
     * Ausgabe-Stream wird geschlossen. Bei erfolgreicher Ausführung wird true zurückgegeben. 
     * Im Fehlerfall wird false zurückgegeben.
     * 
     * 
     * @return boolean true/false
     */
    public boolean close(){
        if(writer != null){
            try{
                writer.close();
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
        }catch(IOException ex ){
            System.err.format("IOException: %s%n", ex);
        }

    }

}

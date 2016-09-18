package raspi.files;


import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Writer;
import java.io.Reader;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

/**
 * Die Main-Klasse Schreiben.
 *
 * @author  (Ihr Name)
 * @version (eine Versionsnummer oder ein Datum)
 */
public class Schreiben
{
    /**
     * Konstruktor fuer die Test-Klasse Schreiben
     */
    public Schreiben()
    {
    }

    public static void main(String[] args)
    {

        String test = "test";
        Charset cset = null;
        /**
         * Verzeichnisse auflisten
         */
        File file = new File("c:\\temp\\");
        File[] files =  file.listFiles();

        System.out.println(file.getParent());
        for (int i=0; i < files.length; i++){
            if(files[i].isDirectory()){
                System.out.println("DIR: " + files[i].getName());
            }
            if(files[i].isFile()){
                System.out.println("FILE: " + files[i].getName());
            }
            if(files[i].isHidden()){
                System.out.println("HIDDEN: " + files[i].getName());
            }
        }

        
        /*
        file.exists();//Datei oder Verzeichnis
        file.canRead();
        file.canWrite();
        file.createNewFile();
        file.delete(); //Datei oder Verzeichnis
        file.getFreeSpace() //Freier Speicher in Byte in der aktuellen Partition
         */

        /**
         * Datei schreiben
         */
        /*   Writer fw = null;
        try{  
        fw = new FileWriter( "c:\\temp\\test.txt" );  

        fw.write( "Zwei Jäger treffen sich..." );  
        fw.append( System.getProperty("line.separator") ); // e.g. "\n"
        fw.append( "Drei Jäger treffen sich..." );  

        }catch ( IOException e ) { 
        System.err.println( "Konnte Datei nicht erstellen" );}
        finally {  
        if ( fw != null )    
        try{ 
        fw.close(); 
        }catch ( IOException e ) { 
        e.printStackTrace(); 
        }
        }

         */
        FileSystem fileSystem = FileSystems.getDefault();
        Path path = fileSystem.getPath("c:\\temp\\", "test.txt");
        BufferedWriter writer = null;
        try {
            writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8); 
            writer.write("Hier steht der Text");
            writer.newLine();
            writer.append("Und noch mehr Text");
        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);
        }finally{
            if(writer != null){
                try{
                    writer.close();
                }catch(IOException ex){
                    System.err.format("IOException: %s%n", ex);
                }  
            }
        }

         BufferedReader reader = null;
        
        try {
            //Charset charset = Charset.forName("US-ASCII");
            //Path path = FileSystems.getDefault().getPath("c:\\temp\\", "test.txt");
            reader = Files.newBufferedReader(path, StandardCharsets.UTF_8);

            String line = null;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            reader.close();
        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);
        }finally{
            if(reader != null){
                try{
                    reader.close();
                }catch(IOException ex){
                    System.err.format("IOException: %s%n", ex);
                }  
            }
        }
    }
}

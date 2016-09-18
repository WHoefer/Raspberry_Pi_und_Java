package raspi.files;

import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.NoSuchFileException;
import java.util.Properties;
import java.io.BufferedReader;
import java.io.OutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.StandardOpenOption;
import java.io.IOException;

/**
 * Die Klasse PropertyServiceTest ist eine Service-Klasse, die beim Anlegen und 
 * Auslesen von Eigenschaftdateien unterstüzt.
 * 
 * @author Wolfgang Höfer
 * @version 1.0
 */
public class PropertyServiceTest
{
    Properties properties = null;
    FileSystem fileSystem = null;
    Path path = null;
    OutputStream writer = null;
    InputStream reader = null;
    String propComment = null;

    /**
     * PropertieService Constructor<br>
     * 
     * Zugriff auf eine Eigenschaftdatei, die im Verzeichnis strPath liegt und 
     * den Namen strFile hat. Wenn die Datei nicht exisiert wird sie angelegt und
     * der Kommentar propComment eingefügt.
     *
     * @param strPath Pfad
     * @param strFile Dateiname
     * @param propComment Kommentar
     */
    public PropertyServiceTest(String strPath, String strFile, String propComment) throws IOException
    {
        this.propComment = propComment;
        properties = new Properties();
        fileSystem = FileSystems.getDefault();
        path = fileSystem.getPath(strPath, strFile);

        try{
            writer = Files.newOutputStream(path, StandardOpenOption.WRITE);
        }catch(NoSuchFileException ex){
            writer = Files.newOutputStream(path, StandardOpenOption.CREATE);
        }
        reader = Files.newInputStream(path, StandardOpenOption.READ);
    }

    /**
     * PropertyService Constructor<br>
     * 
     * Zugriff auf eine Eigenschaftdatei, die im user.dir liegt und 
     * den Namen strFile hat. Wenn die Datei nicht exisiert wird sie angelegt und
     * der Kommentar propComment eingefügt.
     *
     * @param strFile Dateiname
     * @param propComment Kommentar
     */
    public PropertyServiceTest(String strFile, String propComment) throws IOException
    {
        this(System.getProperty("user.dir"), strFile, propComment); 
    }

    /**
     * PropertyService Constructor<br>
     * 
     * Zugriff auf eine Eigenschaftdatei, die im Verzeichnis strPath liegt und 
     * den Namen strFile hat. Wenn die Datei nicht exisiert, wird sie angelegt und
     * der Kommentar propComment eingefügt. Beim Anlegen werden alle 
     * Eigenschaften def in der Datei abgelegt.
     * 
     *
     * @param def Standardwerte
     * @param strPath Pfad
     * @param strFile Dateiname
     * @param propComment Kommentar
     */
    public PropertyServiceTest(Properties def, String strPath, String strFile, String propComment) throws IOException
    {
        this.propComment = propComment;
        properties = new Properties(def);
        fileSystem = FileSystems.getDefault();
        path = fileSystem.getPath(strPath, strFile);

        try{
            writer = Files.newOutputStream(path, StandardOpenOption.WRITE);
        }catch(NoSuchFileException ex){
            writer = Files.newOutputStream(path, StandardOpenOption.CREATE);
            def.store(writer,propComment);
        }
        reader = Files.newInputStream(path, StandardOpenOption.READ);
    }

    /**
     * PropertyService Constructor<br>
     * 
     * Zugriff auf eine Eigenschaftdatei, die im Verzeichnis user.dir liegt und 
     * den Namen strFile hat. Wenn die Datei nicht exisiert wird sie angelegt und
     * der Kommentar propComment eingefügt. Beim Anlegen werden alle 
     * Eigenschaften def in der Datei abgelegt.
     *
     * @param def Standardwerte
     * @param strFile Dateiname
     * @param propComment Kommentar
     */
    public PropertyServiceTest(Properties def, String strFile, String propComment) throws IOException
    {
        this(def, System.getProperty("user.dir"), strFile, propComment); 
    }

    /**
     * Method store<br>
     * 
     * Eigenschaften werden gespeichert. Gibt true zurück, wenn erfolgrich gespeichert
     * wurde. Bei einem Fehler wird false zurückgegeben.
     *
     * @return boolean
     */
    public boolean store () {
        try{
            properties.store(writer, propComment);
        }catch(IOException ex){
            System.err.format("IOException: %s%n", ex);
            return false;
        }
        return true;
    }

    /**
     * Method load<br>
     * 
     * Eigenschaften werden geladen. Gibt true zurück, wenn erfolgrich ausgelesen
     * wurde. Bei einem Fehler wird false zurückgegeben.
     *
     * @return boolean
     */
    public boolean load () {
        try{
            properties.load(reader);
        }catch(IOException ex){
            System.err.format("IOException: %s%n", ex);
            return false;
        }
        return true;
    }

    /**
     * Method getProperty<br>
     * 
     * Eigenschaft mit dem Schlüssel key wird gelesen.
     *
     * @param Schlüssel
     * @return Property
     */
    public String getProperty(String key){
        return properties.getProperty(key);
    }

    /**
     * Method setProperty<br>
     * 
     * Eigenschaft key mit dem Wert value wird gesetzt.
     *
     * @param Schlüssel
     * @param Wert
     */
    public void setProperty(String key, String value){
        properties.setProperty(key, value);
    }

    public static void main(String[] args){
        try{
            Properties prop = new Properties();
            prop.setProperty("key3", "wert3");
            prop.setProperty("key4", "wert4");

            PropertyServiceTest propertyService = new PropertyServiceTest(prop, "/home/pi/", "test.props", "Kommentar");
            propertyService.load();
            propertyService.setProperty("Pfad","/home/pi");
            System.out.println(propertyService.getProperty("key1"));
            System.out.println(propertyService.getProperty("key2"));
            System.out.println(propertyService.getProperty("key3"));
            System.out.println(propertyService.getProperty("key4"));
            propertyService.store();
            PropertyServiceTest propertyService1 = new PropertyServiceTest(prop, "test.props", "Kommentar");
            propertyService1.setProperty("name", "Willi");
            propertyService1.store();
        }catch(IOException ex){
            System.err.format("IOException: %s%n", ex);

        }
    }

}

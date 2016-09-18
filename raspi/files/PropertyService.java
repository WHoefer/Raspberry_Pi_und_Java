package raspi.files;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Die Klasse PropertyService ist eine Service-Klasse, die beim Anlegen und 
 * Auslesen von Eigenschaftdateien unterstüzt.
 * 
 * @author Wolfgang Höfer
 * @version 1.0
 */
public class PropertyService
{
    private Properties props;
    private String error;
    /**
     * Constructor for objects of class PropertieService
     */
    public PropertyService(String name)
    {
        props = new Properties();
        try{
            FileInputStream in = new FileInputStream(name);
            props.load(in);
            in.close();
        }catch(FileNotFoundException fnfex){
            try{
                FileOutputStream out = new FileOutputStream(name);
                setDefaultProperties(props);
                props.store(out, "Defaultproperties");
                out.close();
            }catch(IOException ex){
                error = "Fehler: %nEs konnte keine Propertiedatei gefunden%n und auch keine erzeugt werden.";
                System.out.printf(error);
            }    
        }catch(IOException ioex){
            error = "Fehler: %nPropertiedatei konnte nicht gelesen werden.";
            System.out.printf(error);
        }

    }

    /**
     * Setzt die default Properties und sollte durch 
     * eine Kindklasse überschrieben werden. Die Methode 
     * wird vom Konstruktor aufgerufen, der props übergibt.
     *
     * @param props A parameter
     */
    public void setDefaultProperties(Properties props){
        props.setProperty("titel", "Titel der Anwnedung");
    }
    
    
    public Properties getProperties(){
        return props;
    }
}

package raspi.projekte.kap14;

import raspi.files.PropertyService;
import java.util.Properties;
/**
 * Write a description of class ZeitschaltUhrProperties here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class ZeitschaltUhrProperties extends PropertyService
{

    /**
     * Constructor for objects of class ZeitschaltUhrProperties
     */
    public ZeitschaltUhrProperties(String name)
    {
      super(name);
    }
    
    @Override
     public void setDefaultProperties(Properties props){
        props.setProperty(HttpHandlerZeitschaltuhr.GPIO01, "Steckdose 1");
        props.setProperty(HttpHandlerZeitschaltuhr.GPIO02, "Steckdose 2");
        props.setProperty(HttpHandlerZeitschaltuhr.GPIO03, "Steckdose 3");
        props.setProperty(HttpHandlerZeitschaltuhr.GPIO04, "Steckdose 4");
        props.setProperty("IP", "localhost");
        props.setProperty("Port", "8080");
        props.setProperty("Root", "daten");
    }

}

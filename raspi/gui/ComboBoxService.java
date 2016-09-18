package raspi.gui;

import java.util.Map;
import java.util.HashMap;
import java.util.Collection;
import java.util.Set;
import java.util.Iterator;

/**
 * Write a description of class GuiService1 here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class ComboBoxService
{

    Map<String, String> itemMap;

    /**
     * Constructor for objects of class GuiService1
     */
    public ComboBoxService()
    {
        itemMap = new HashMap<String, String>();
    }

    public void addItem(String key, String item){
        itemMap.put(key, item);
    }

    public Object[] getItems(){
        Collection<String> col = itemMap.values();
        return col.toArray();
    }

    public String getKey(String item){
        Set<String> kies = itemMap.keySet();
        for(Iterator<String> it = kies.iterator(); it.hasNext();){
            String key = it.next();
            String itemValue = itemMap.get(key);
            if(itemValue.equals(item)){
                return key;
            }    
        }
        return "";
    }
}

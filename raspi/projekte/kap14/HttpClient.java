package raspi.projekte.kap14;


import java.util.Map; 
import java.util.HashMap;
import java.util.List; 
import raspi.webservice.RestUtil;
import raspi.webservice.Client;

/**
 * HttpClient baut die Verbindung zu dem Server auf.
 * 
 * @author Wolfgang Höfer
 * @version 1.0
 */
public class HttpClient extends Client
{

    private Map<String, String> headerMap;
    private List<String> listString;

    public HttpClient(String urlStr){
        super(urlStr);
    }

    public List<String> getListStrings(){
        return listString;
    }

    public Map<String, String> getHeaderMap(){
        return headerMap;
    }
    
    public void executeGetSchedule(String port){
        String htmlString = requestGet(port).toString();
        headerMap = RestUtil.getHeaderMap(htmlString);
        listString = RestUtil.getList(htmlString);
    }
    
    public void executePostSchedule(String port, List<String> datalist){
        Map<String, String> headerMap1 = new HashMap<String, String>();
        StringBuilder htmlString = new StringBuilder();
        headerMap1.put(HttpHandlerZeitschaltuhr.BEFEHL, HttpHandlerZeitschaltuhr.POSTSCHEDULE);
        headerMap1.put(HttpHandlerZeitschaltuhr.GPIOPORT, port);
        RestUtil.createTablesFromList(headerMap1, datalist, htmlString);
        String htmlStringResponse = requestPost(htmlString.toString()).toString();
        headerMap = RestUtil.getHeaderMap(htmlStringResponse);
        listString = RestUtil.getList(htmlStringResponse);
    }

}

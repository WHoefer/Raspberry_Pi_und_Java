package raspi.projekte.kap13;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map; 
import java.util.Set; 
import java.util.Map.Entry; 
import java.util.HashMap;
import java.util.List; 
import java.util.ArrayList;
import java.util.Iterator; 
import raspi.webservice.RestUtil;
import raspi.webservice.Client;
import raspi.logger.DiagramDatePoint;

/**
 * Write a description of class RestClient here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class HttpClient extends Client
{

    private List<DiagramDatePoint> listDatePoint;
    private Map<String, String> headerMap;
    private List<String> listString;

    public HttpClient(String urlStr){
        super(urlStr);
    }

    public void executeGetLogTag(){
        StringBuilder htmlString = requestGet(HttpHandlerHeizung.GETLOGTAG);
        listDatePoint = RestUtil.getDiagramDatePointList(htmlString.toString());
    }

    public void executeGetSchedule(){
        String htmlString = requestGet(HttpHandlerHeizung.GETSCHEDULE).toString();
        headerMap = RestUtil.getHeaderMap(htmlString);
        listString = RestUtil.getList(htmlString);
    }

    public List<DiagramDatePoint> getDiagramDatePoints(){
        return listDatePoint;
    }

    public List<String> getListStrings(){
        return listString;
    }

    public Map<String, String> getHeaderMap(){
        return headerMap;
    }

    public void executePostSchedule(Double tempTag, Double tempNacht, List<String> datalist){
        Map<String, String> headerMap1 = new HashMap();
        StringBuilder htmlString = new StringBuilder();
        headerMap1.put(HttpHandlerHeizung.BEFEHL, HttpHandlerHeizung.POSTSCHEDULE);
        headerMap1.put(HttpHandlerHeizung.TEMPTAG, tempTag.toString());
        headerMap1.put(HttpHandlerHeizung.TEMPNACHT, tempNacht.toString());
        headerMap1.put(HttpHandlerHeizung.GPIOPORT, HttpHandlerHeizung.GPIO01);
        RestUtil.createTablesFromList(headerMap1, datalist, htmlString);
        String htmlStringResponse = requestPost(htmlString.toString()).toString();
        headerMap = RestUtil.getHeaderMap(htmlStringResponse);
        listString = RestUtil.getList(htmlStringResponse);
    }

    public static void main(String[] args) {
        HttpClient restClientGet = new HttpClient("http://192.168.0.121:8080/daten");
        /**
         * Test: executeGetLogTag
         */
        System.out.println("Test: executeGetLogTag");
        restClientGet.executeGetLogTag();
        List list =  restClientGet.getDiagramDatePoints();
        Iterator it = list.iterator();
        while(it.hasNext()){
            DiagramDatePoint ddp = (DiagramDatePoint)it.next();
            System.out.printf("Log %1$s  %2$s %n", ddp.getX(), ddp.getY());
        }       
        System.out.println("************************************************************");
        /**
         * Test: executeGetSchedule
         */
        System.out.println("Test: executeGetSchedule");
        restClientGet.executeGetSchedule();
        List<String> commandList  =  restClientGet.getListStrings();
        Map headerMap = restClientGet.getHeaderMap();
        System.out.printf("Befehl: %1$s %n", headerMap.get(HttpHandlerHeizung.BEFEHL)); 
        System.out.printf("Temperatur Tag: %1$s %n", headerMap.get(HttpHandlerHeizung.TEMPTAG)); 
        System.out.printf("Temperatur Nacht: %1$s %n", headerMap.get(HttpHandlerHeizung.TEMPNACHT)); 
        System.out.printf("GPIO Port: %1$s %n", headerMap.get(HttpHandlerHeizung.GPIOPORT)); 
        it = commandList.iterator();
        while(it.hasNext()){
            String command = (String)it.next();
            System.out.printf("Schedule %1$s %n", command);
        }  
        System.out.println("************************************************************");
        /**
         * Test executePostSchedule
         */
        System.out.println("Test: executePostSchedule");
        List<String> newCommands = new ArrayList();
        newCommands.add("0101197001013000111111100000023595900000");
        newCommands.add("0101197001013000100000000000023595900000");
        newCommands.add("0101197001013000010000000000023595900000");
        newCommands.add("0101197001013000001000000000023595900000");
        newCommands.add("0101197001013000000100000000023595900000");
        restClientGet.executePostSchedule(36.0, 30.0, newCommands);
        headerMap = restClientGet.getHeaderMap();
        System.out.printf("Befehl: %1$s %n", headerMap.get(HttpHandlerHeizung.BEFEHL)); 
        System.out.printf("Temperatur Tag: %1$s %n", headerMap.get(HttpHandlerHeizung.TEMPTAG)); 
        System.out.printf("Temperatur Nacht: %1$s %n", headerMap.get(HttpHandlerHeizung.TEMPNACHT)); 
        System.out.printf("GPIO Port: %1$s %n", headerMap.get(HttpHandlerHeizung.GPIOPORT)); 
        it = commandList.iterator();
        while(it.hasNext()){
            String command = (String)it.next();
            System.out.printf("Schedule %1$s %n", command);
        }       
        System.out.println("************************************************************");
        /**
         * Test: executeGetSchedule
         */
        System.out.println("Test: executeGetSchedule");
        restClientGet.executeGetSchedule();
        commandList  =  restClientGet.getListStrings();
        headerMap = restClientGet.getHeaderMap();
        System.out.printf("Befehl: %1$s %n", headerMap.get(HttpHandlerHeizung.BEFEHL)); 
        System.out.printf("Temperatur Tag: %1$s %n", headerMap.get(HttpHandlerHeizung.TEMPTAG)); 
        System.out.printf("Temperatur Nacht: %1$s %n", headerMap.get(HttpHandlerHeizung.TEMPNACHT)); 
        System.out.printf("GPIO Port: %1$s %n", headerMap.get(HttpHandlerHeizung.GPIOPORT)); 
        it = commandList.iterator();
        while(it.hasNext()){
            String command = (String)it.next();
            System.out.printf("Schedule %1$s %n", command);
        }
        System.out.println("ENDE ENDE ENDE ENDE ENDE ENDE ENDE ENDE ENDE ENDE ENDE ");

    }
}

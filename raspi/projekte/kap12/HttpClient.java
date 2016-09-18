package raspi.projekte.kap12;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map; 
import java.util.List; 
import java.util.Iterator; 
import raspi.webservice.RestUtil;
import raspi.logger.DiagramDatePoint;

/**
 * Write a description of class RestClient here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class HttpClient
{

    private String urlStr; 
    private List<DiagramDatePoint> listDatePoint;

    public HttpClient(String urlStr){
        this.urlStr = urlStr;
    }

    public List<DiagramDatePoint> getTag(){
        executeCommand("Tag");
        return getListDiagramDatePoint();
    }

    public List<DiagramDatePoint> getJahrMax(){
        executeCommand("JahrMax");
        return getListDiagramDatePoint();
    }

    public List<DiagramDatePoint> getJahrMin(){
        executeCommand("JahrMin");
        return getListDiagramDatePoint();
    }

    private void executeCommand(String befehl){
        StringBuilder htmlString = request(befehl);
        listDatePoint = RestUtil.getDiagramDatePointList(htmlString.toString());
    }

    private List<DiagramDatePoint> getListDiagramDatePoint(){
        return listDatePoint;
    }

    private StringBuilder request(String befehl){
        StringBuilder responseStr = new StringBuilder();
        try {

            URL url = new URL(urlStr+"?Befehl="+befehl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "text/html");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("HTTP Fehler: Error - "
                    + conn.getResponseCode());
            }else{

                BufferedReader br = 
                new BufferedReader(new InputStreamReader(conn.getInputStream()));

                String output;
                while ((output = br.readLine()) != null) {
                    responseStr.append(output);
                }

                conn.disconnect();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }  
        return responseStr;

    }

    public static void main(String[] args) {
        HttpClient restClientGet = new HttpClient("http://192.168.0.121:8080/daten");

        List list = restClientGet.getTag();
        Iterator it = list.iterator();
        while(it.hasNext()){
            DiagramDatePoint ddp = (DiagramDatePoint)it.next();
            System.out.printf("Tag %1$s  %2$s %n", ddp.getX(), ddp.getY());
        }       

        list = restClientGet.getJahrMax();
        it = list.iterator();
        while(it.hasNext()){
            DiagramDatePoint ddp = (DiagramDatePoint)it.next();
            System.out.printf("Max %1$s  %2$s %n", ddp.getX(), ddp.getY());
        }       
        list = restClientGet.getJahrMin();
        it = list.iterator();
        while(it.hasNext()){
            DiagramDatePoint ddp = (DiagramDatePoint)it.next();
            System.out.printf("Min %1$s  %2$s %n", ddp.getX(), ddp.getY());
        }       

    }
}

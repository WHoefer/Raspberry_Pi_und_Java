package raspi.webservice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map; 
import java.util.List; 
import java.util.Iterator; 

/**
 * HTTP Client
 * 
 * @author Wolfgang Höfer 
 * @version 1.0
 */
public class Client
{
    public String urlStr; 

    /**
     * Constructor for objects of class Client
     */
    public Client(String urlStr)
    {
        this.urlStr = urlStr;
    }

    public StringBuilder requestGet(String befehl){
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

    public StringBuilder requestPost(String htmlString){
        StringBuilder responseStr = new StringBuilder();
        try {
            byte[] response = htmlString.getBytes("UTF-8");
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);           
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Accept", "text/html");
            //conn.setRequestProperty("Content-Type", "text/html");
            //conn.setRequestProperty("charset", "utf-8");
            //conn.setRequestProperty("Content-Length", Integer.toString(response.length));
            OutputStream out = conn.getOutputStream();
            out.write(response);
            out.flush();

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

}

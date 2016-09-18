package raspi.webservice;

import com.sun.net.httpserver.HttpExchange;
import java.net.HttpURLConnection;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
/**
 * Http Handler
 * 
 * @author Wolfgang Höfer 
 * @version 1.0
 */
public class Handler extends Http
{

    public void response(final StringBuilder responseStr, final HttpExchange httpExchange ){
        try{
            httpExchange.getResponseHeaders().add("Content-type", "text/html");
            String respStr = responseStr.toString();
            byte[] response = respStr.getBytes("UTF-8");
            httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, response.length);
            final OutputStream os = httpExchange.getResponseBody();
            os.write(response);
            os.close(); 
        }catch(IOException ex){
            System.out.println(ex.toString());
        }
    }

    public StringBuilder getBody(HttpExchange httpExchange){
        StringBuilder requestString = new StringBuilder();
        try{
            InputStream in = httpExchange.getRequestBody();
           
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String output;
            while ((output = br.readLine()) != null) {
                requestString.append(output);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } 
        return requestString;
    }

    public static boolean isGet(HttpExchange httpExchange){
        if(httpExchange.getRequestMethod().equals(GET)){
            return true;
        }
        return false;
    }

    public static boolean isPost(HttpExchange httpExchange){
        if(httpExchange.getRequestMethod().equals(POST)){
            return true;
        }
        return false;
    }

    public static boolean isPut(HttpExchange httpExchange){
        if(httpExchange.getRequestMethod().equals(PUT)){
            return true;
        }
        return false;
    }

    public static boolean isDelete(HttpExchange httpExchange){
        if(httpExchange.getRequestMethod().equals(DELETE)){
            return true;
        }
        return false;
    }

}

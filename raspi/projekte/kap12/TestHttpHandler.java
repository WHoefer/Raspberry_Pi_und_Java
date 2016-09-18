package raspi.projekte.kap12;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;

public class TestHttpHandler implements HttpHandler 
{

    public static final String HTML = "<HTML><HEADER></HEADER><BODY>Hallo, <br>hier antwortet der Raspberry Pi!</BODY></HTML>";

    public TestHttpHandler()throws Exception
    {
    }

    public void handle(HttpExchange httpExchange) throws IOException
    {
        try{
            httpExchange.getResponseHeaders().add("Content-type", "text/html");
            byte[] response = HTML.getBytes();
            httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, response.length);
            OutputStream os = httpExchange.getResponseBody();
            os.write(response);
            os.close(); 
        }catch(IOException ex){
            System.out.println(ex.toString());
        }
    }
}

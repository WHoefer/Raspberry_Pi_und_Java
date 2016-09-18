package raspi.projekte.kap12;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.BufferedReader;
import raspi.logger.DataLogger;
import raspi.logger.DiagramDatePoint;
import raspi.webservice.RestUtil;
import raspi.webservice.ParameterUtil;

/**
 * Write a description of class HttpHandlerZisterne here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class HttpHandlerZisterne  implements HttpHandler 
{

    final DataLogger loggerTag;
    final DataLogger loggerJahr;
    final Scheduler scheduler;
    public static final String BEFEHL = "Befehl";
    public static final String JAHRMAX = "JahrMax";
    public static final String JAHRMIN = "JahrMin";
    public static final String TAG = "Tag";

    public HttpHandlerZisterne()throws Exception
    {
        this.loggerTag = new DataLogger(null,"LoggerZisterneTag.txt", 730);
        this.loggerJahr = new DataLogger(null,"LoggerZisterneJahr.txt", 370);
        loggerJahr.enableFileLog();
        scheduler = new Scheduler(loggerTag, loggerJahr);
        scheduler.start();
    }

    /**
    * Only method prescribed by the HttpHandler interface.
    *  
    * @param httpExchange Encapsulation of HTTP request and response.
    * @throws java.io.IOException
    */
    @SuppressWarnings("unchecked")
    public void handle(HttpExchange httpExchange) throws IOException
    {
        final String requestMethod = httpExchange.getRequestMethod();
        final StringBuilder responseStr = new StringBuilder();
        Map headerMap = new HashMap();
        Calendar calStart = new GregorianCalendar();
        calStart.add(Calendar.DAY_OF_YEAR, -365);
        Calendar calStoppp = new GregorianCalendar();

        if(requestMethod.equalsIgnoreCase("GET")){
            final Map paraMap = ParameterUtil.getParameters(httpExchange);
            if(paraMap != null && !paraMap.isEmpty()){
                String befehl = (String)paraMap.get(BEFEHL);
                if(befehl.equals(TAG)){
                    headerMap.put(BEFEHL,TAG);
                    RestUtil.createTablesFromDiagramDatePoint(headerMap, 
                        loggerTag.getDiagramDatePointListFromBuffer(86400, Scheduler.TAG),
                        responseStr);
                }else if(befehl.equals(JAHRMAX)){
                    headerMap.put(BEFEHL,JAHRMAX);
                    RestUtil.createTablesFromDiagramDatePoint(headerMap, 
                        loggerJahr.getDiagramDatePointListFromFile(calStart, calStoppp, Scheduler.MAX),
                        responseStr);
                }else if(befehl.equals(JAHRMIN)){
                    headerMap.put(BEFEHL,JAHRMIN);
                    RestUtil.createTablesFromDiagramDatePoint(headerMap, 
                        loggerJahr.getDiagramDatePointListFromFile(calStart, calStoppp, Scheduler.MIN),
                        responseStr);
                }else{
                    getCurrent(headerMap, responseStr);
                }
            }else{
                getCurrent(headerMap, responseStr);
            }
            response(responseStr, httpExchange );
        }
    }

    private void getCurrent(final Map headerMap, final StringBuilder responseStr){
        List<DiagramDatePoint> listDatePoint;
        String out = "";
        listDatePoint = loggerTag.getDiagramDatePointListFromBuffer(86400, Scheduler.TAG);
        if(listDatePoint.size() > 0){
            DiagramDatePoint diagramDatePoint = listDatePoint.get(listDatePoint.size()-1);
            out = String.format(Locale.GERMANY, 
                "Verfügbare Wassermenge<br> in der Zisterne %1$.0f Liter.", 
                diagramDatePoint.getY());
        }
        headerMap.put(out,"");
        RestUtil.createTablesFromDiagramDatePoint(headerMap, null, responseStr);
        //System.out.println(responseStr.toString());
    }

    private void response(final StringBuilder responseStr, final HttpExchange httpExchange ){
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

}

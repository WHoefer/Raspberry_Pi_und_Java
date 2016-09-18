package raspi.projekte.kap13;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;
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
import raspi.webservice.Handler;
import raspi.webservice.DataStore;
import raspi.schedule.ScheduleUtil;

import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.PinPullResistance;

/**
 * Write a description of class HttpHandlerZisterne here.
 * 
 * @author Wolfgang Höfer
 * @version 1.0
 */
public class HttpHandlerHeizung extends Handler implements HttpHandler 
{

    final DataLogger loggerTag;
    final DataStore dataStore;
    final SchedulerLogger schedulerLogger;
    final SchedulerTemperatur schedulerTemperatur;
    final GpioController gpio = GpioFactory.getInstance();
    final GpioPinDigitalOutput relais = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01);
    final Sensor sensor;

    public static final String BEFEHL = "Befehl";
    public static final String GETSCHEDULE = "GetSchedule";
    public static final String GETLOGTAG = "GetLogTag";
    public static final String POSTSCHEDULE = "PostSchedule";
    public static final String TEMPTAG = "TemperaturTag";
    public static final String TEMPNACHT = "TemperaturNacht";
    public static final String GPIOPORT = "GPIO_Port";
    public static final String GPIO01 = "GPIO_01";
    public static final String ERROR = "Error";
    public static final String TEMPERATUR = "Temp";

    private Double tagTemp;
    private Double nachtTemp;
    private Map headerMap = new HashMap();
    private Map dataMap = new HashMap();

    public HttpHandlerHeizung()throws Exception
    {
        dataStore = new  DataStore(null, "DataStoreHeizung.html");
        headerMap =  dataStore.readHeader();
        String tagStr    = (String)headerMap.get(TEMPTAG);
        String nachtStr  = (String)headerMap.get(TEMPNACHT);
        if(tagStr == null || tagStr.isEmpty()){
            tagStr = "20.0";
        }    
        if(nachtStr == null || nachtStr.isEmpty()){
            nachtStr = "16.0";
        }    
        tagTemp   = new Double(tagStr);
        nachtTemp = new Double(nachtStr);

        loggerTag = new DataLogger(null,"LoggerHeizungTag.txt", 730);
        relais.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF); 
        sensor = new Sensor();

        schedulerLogger = new SchedulerLogger(120000,  sensor, loggerTag, TEMPERATUR);
        List listLogger = new ArrayList();
        listLogger.add(ScheduleUtil.allwaysOn());
        schedulerLogger.setCommandStringList(listLogger);
        schedulerLogger.start();

        schedulerTemperatur = new SchedulerTemperatur(60000,  sensor, relais);
        List listTemp = dataStore.readList();
        if (listTemp == null || listTemp.isEmpty()){
            listTemp = new ArrayList();
            listTemp.add(ScheduleUtil.allwaysOn());
        }    
        schedulerTemperatur.start();
        schedulerTemperatur.setTagNacht(tagTemp, nachtTemp);
        schedulerTemperatur.setCommandStringList(listTemp);

    }

    /**
    *  
    * @param httpExchange Encapsulation of HTTP request and response.
    * @throws java.io.IOException
    */
    @SuppressWarnings("unchecked")
    public void handle(HttpExchange httpExchange) throws IOException
    {
        final StringBuilder responseStr = new StringBuilder();
        String befehlStr;
        String portStr;
        String tagStr;
        String nachtStr;
        boolean ok = false;
        List<String> list = null;

        if(isGet(httpExchange)){
            final Map paraMap = ParameterUtil.getParameters(httpExchange);
            if(paraMap != null && !paraMap.isEmpty()){
                befehlStr = (String)paraMap.get(BEFEHL);
                if(befehlStr.equals(GETLOGTAG)){
                    headerMap.put(BEFEHL,GETLOGTAG);
                    RestUtil.createTablesFromDiagramDatePoint(headerMap, 
                        loggerTag.getDiagramDatePointListFromBuffer(86400, TEMPERATUR),
                        responseStr);
                }else if(befehlStr.equals(GETSCHEDULE)){
                    createScheduleResponse(responseStr);
                }
            }
        }else if(isPost(httpExchange)){
            ok = false;
            try{
                final StringBuilder strBuilder = getBody(httpExchange);
                headerMap = RestUtil.getHeaderMap(strBuilder.toString());
                if(headerMap != null){
                    befehlStr = (String)headerMap.get(BEFEHL);
                    portStr   = (String)headerMap.get(GPIOPORT);
                    tagStr    = (String)headerMap.get(TEMPTAG);
                    nachtStr  = (String)headerMap.get(TEMPNACHT);
                    if(befehlStr != null && befehlStr.equals(POSTSCHEDULE) &&
                    portStr != null && portStr.equals(GPIO01) &&
                    tagStr != null && nachtStr != null){
                        tagTemp   = new Double(tagStr);
                        nachtTemp = new Double(nachtStr);
                        list = RestUtil.getList(strBuilder.toString());
                        if(list != null && !list.isEmpty()){
                            schedulerTemperatur.setTagNacht(tagTemp, nachtTemp);
                            schedulerTemperatur.setCommandStringList(list);
                            createScheduleResponse(responseStr);
                            ok = true;
                        }else{
                            createErrorResponse("Keine Zeitsteuerung gefunden!",responseStr); 
                        }
                    }else{
                        createErrorResponse("Es wurden nicht alle notwendigen Felder gesetzt.!",responseStr); 
                    }
                }else{
                    createErrorResponse("Keine Header-Tabelle gefunden!",responseStr);
                }
                RestUtil.createTablesFromMap(headerMap, null, responseStr);
            }catch(NullPointerException ex){
                createErrorResponse("Allgemeiner Fehler im Datenformat! ",responseStr);
            }
            catch(NumberFormatException ex){
                createErrorResponse("Temperatur im falschen Format!",responseStr);
            }
        }
        response(responseStr, httpExchange );
        if(ok){
            dataStore.storeHeaderMapAndList(headerMap, list);
        }
    }

    private void createErrorResponse(String info, final StringBuilder responseStr){
        headerMap = new HashMap();
        headerMap.put(ERROR, info);
        RestUtil.createTablesFromMap(headerMap, null, responseStr);
    }

    private void createScheduleResponse(final StringBuilder responseStr){
        headerMap = new HashMap();
        List<String> dataList = schedulerTemperatur.getCommandStringList();
        headerMap.put(BEFEHL,GETSCHEDULE);
        headerMap.put(TEMPTAG, Double.toString(schedulerTemperatur.getTag()));
        headerMap.put(TEMPNACHT, Double.toString(schedulerTemperatur.getNacht()));
        headerMap.put(GPIOPORT, "GPIO_01");
        RestUtil.createTablesFromList(headerMap, dataList, responseStr);
    }
}

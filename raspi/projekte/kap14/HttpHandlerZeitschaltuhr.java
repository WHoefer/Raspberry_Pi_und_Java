package raspi.projekte.kap14;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Calendar;
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
public class HttpHandlerZeitschaltuhr extends Handler implements HttpHandler 
{

    final DataStore dataStore1;
    final DataStore dataStore2;
    final DataStore dataStore3;
    final DataStore dataStore4;
    final SchedulerPort schedulerPort1;
    final SchedulerPort schedulerPort2;
    final SchedulerPort schedulerPort3;
    final SchedulerPort schedulerPort4;
    final GpioController gpio = GpioFactory.getInstance();
    final GpioPinDigitalOutput relais1 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01);
    final GpioPinDigitalOutput relais2 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02);
    final GpioPinDigitalOutput relais3 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03);
    final GpioPinDigitalOutput relais4 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04);

    public static final String BEFEHL = "Befehl";
    public static final String POSTSCHEDULE = "PostSchedule";
    public static final String GPIOPORT = "GPIO_Port";
    public static final String GPIO01 = "GPIO_01";
    public static final String GPIO02 = "GPIO_02";
    public static final String GPIO03 = "GPIO_03";
    public static final String GPIO04 = "GPIO_04";
    public static final String ERROR = "Error";

    private Map headerMap = new HashMap();
    private Map dataMap = new HashMap();

    public HttpHandlerZeitschaltuhr()throws Exception
    {
        relais1.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF); 
        relais2.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF); 
        relais3.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF); 
        relais4.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF); 

        dataStore1 = new  DataStore(null, "DataStoreZeitschaltuhr1.html");
        dataStore2 = new  DataStore(null, "DataStoreZeitschaltuhr2.html");
        dataStore3 = new  DataStore(null, "DataStoreZeitschaltuhr3.html");
        dataStore4 = new  DataStore(null, "DataStoreZeitschaltuhr4.html");

        schedulerPort1 = new SchedulerPort(10000, relais1);
        schedulerPort2 = new SchedulerPort(10000, relais2);
        schedulerPort3 = new SchedulerPort(10000, relais3);
        schedulerPort4 = new SchedulerPort(10000, relais4);

        initScheduler(schedulerPort1, dataStore1, relais1);
        initScheduler(schedulerPort2, dataStore2, relais2);
        initScheduler(schedulerPort3, dataStore3, relais3);
        initScheduler(schedulerPort4, dataStore4, relais4);
    }

    private void initScheduler(SchedulerPort schedulerPort, DataStore dataStore, GpioPinDigitalOutput relais ){
        List list = dataStore.readList();
        if (list == null || list.isEmpty()){
            list = new ArrayList();
            schedulerPort.setEnabled(false);
        }    
        schedulerPort.start();
        schedulerPort.setCommandStringList(list);
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

        befehlStr = "";
        portStr = "";
        if(isGet(httpExchange)){
            final Map paraMap = ParameterUtil.getParameters(httpExchange);
            if(paraMap != null && !paraMap.isEmpty()){
                befehlStr = (String)paraMap.get(BEFEHL);
                if(befehlStr.equals(GPIO01)){
                    createScheduleResponse(responseStr,schedulerPort1 ,GPIO01);
                }else if(befehlStr.equals(GPIO02)){
                    createScheduleResponse(responseStr,schedulerPort2 ,GPIO02);
                }else if(befehlStr.equals(GPIO03)){
                    createScheduleResponse(responseStr,schedulerPort3 ,GPIO03);
                }else if(befehlStr.equals(GPIO04)){
                    createScheduleResponse(responseStr,schedulerPort4 ,GPIO04);
                }else{
                    createErrorResponse("Unbekannter Port!",responseStr);
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
                    if(befehlStr != null && befehlStr.equals(POSTSCHEDULE) && portStr != null){
                        list = RestUtil.getList(strBuilder.toString());
                        if(list != null && !list.isEmpty()){
                            if(portStr.equals(GPIO01)){
                                schedulerPort1.setCommandStringList(list);
                                schedulerPort1.setEnabled(true);
                                createScheduleResponse(responseStr,schedulerPort1 ,GPIO01);
                                ok = true;
                            }else if(portStr.equals(GPIO02)){
                                schedulerPort2.setCommandStringList(list);
                                schedulerPort2.setEnabled(true);
                                createScheduleResponse(responseStr,schedulerPort2 ,GPIO02);
                                ok = true;
                            }else if(portStr.equals(GPIO03)){
                                schedulerPort3.setCommandStringList(list);
                                schedulerPort3.setEnabled(true);
                                createScheduleResponse(responseStr,schedulerPort3 ,GPIO03);
                                ok = true;
                            }else if(portStr.equals(GPIO04)){
                                schedulerPort4.setCommandStringList(list);
                                schedulerPort4.setEnabled(true);
                                createScheduleResponse(responseStr,schedulerPort4 ,GPIO04);
                                ok = true;
                            }else{
                                createErrorResponse("Unbekannter Port.",responseStr);
                            }
                        }else{
                            if(portStr.equals(GPIO01)){
                                schedulerPort1.setEnabled(false);
                                ok = true;
                            }else if(portStr.equals(GPIO02)){
                                schedulerPort2.setEnabled(false);
                                ok = true;
                            }else if(portStr.equals(GPIO03)){
                                schedulerPort3.setEnabled(false);
                                ok = true;
                            }else if(portStr.equals(GPIO04)){
                                schedulerPort4.setEnabled(false);
                                ok = true;
                            }else{
                                createErrorResponse("Unbekannter Port.",responseStr);
                            }
                            createErrorResponse("Keine Zeitsteuerung gefunden. Port abgeschaltet.",responseStr); 
                        }
                    }else{
                        createErrorResponse("Es wurden nicht alle notwendigen Felder gesetzt.",responseStr); 
                    }
                }else{
                    createErrorResponse("Keine Header-Tabelle gefunden.",responseStr);
                }
                //RestUtil.createTablesFromMap(headerMap, null, responseStr);
            }catch(NullPointerException ex){
                createErrorResponse("Allgemeiner Fehler im Datenformat. ",responseStr);
            }
            //catch(NumberFormatException ex){
              //  createErrorResponse("Temperatur im falschen Format.",responseStr);
            //}
        }
        response(responseStr, httpExchange );
        if(ok){
            if(portStr.equals(GPIO01)){
                dataStore1.storeHeaderMapAndList(null, list);
            }else if(portStr.equals(GPIO02)){
                dataStore2.storeHeaderMapAndList(null, list);
            }else if(portStr.equals(GPIO03)){
                dataStore3.storeHeaderMapAndList(null, list);
            }else if(portStr.equals(GPIO04)){
                dataStore4.storeHeaderMapAndList(null, list);
            }
        }
    }

    private void createErrorResponse(String info, final StringBuilder responseStr){
        headerMap = new HashMap();
        headerMap.put(ERROR, info);
        RestUtil.createTablesFromMap(headerMap, null, responseStr);
    }

    private void createScheduleResponse(final StringBuilder responseStr, SchedulerPort schedulerPort, String gpio){
        headerMap = new HashMap();
        headerMap.put(BEFEHL,gpio);
        List<String> dataList = schedulerPort.getCommandStringList();
        RestUtil.createTablesFromList(headerMap, dataList, responseStr);
    }
}

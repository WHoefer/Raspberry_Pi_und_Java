package raspi.webservice;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.Locale;
import java.util.Date;
import java.util.GregorianCalendar;
import java.nio.file.Files;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.nio.charset.StandardCharsets;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.text.ParseException;
import raspi.logger.SingleValue;
import raspi.logger.DiagramDatePoint;
import raspi.webservice.RestUtil;

/**
 * DataLogger<br>
 * Die Klasse DataLogger stellt eine Buffer bereit in dem verschiedene Datentypen gespeichert 
 * werden können. Ist die Standardgröße des Buffers ereicht, so werden ältere Werte in die Logdatei 
 * speichert. Dieser Vorgang wird durch die Methode store() angestoßen.
 * 
 * @author Wolfgang Höfer
 * @version 1.0
 */
public class DataStore
{

    private List<SingleValue> dataList =null;
    private Calendar cal = null;
    private TimeZone timeZone = null;
    private Locale locale = null;

    private String pathStr = null;
    private String fileStr = null;
    private FileSystem fileSystem = null;
    private Path path = null;
    private BufferedWriter writer = null;
    private BufferedReader reader = null;
    private boolean enable;

    /*private static final String lineFormat = 
    "%1$tY.%1$tm.%1$td %1$tH:%1$tM:%1$tS:%1$tL %2$02d %3$6.6s %4$s%n";
    private static final String dateFormatStr =
    "yyyy.MM.dd HH:mm:ss:SSS";
    private static final int lengthDateFormat = 23;
    private DateFormat dateFormat = null;    
     */
    /**
     * DataLogger Constructor<br>
     *
     * @param pathStr Speicherpfad der Logdatei
     * @param fileStr Name der Logdatei
     * @param maxListLength Standardlänge des Buffers
     * @param timeZone Zeitzone
     * @param locale Locale
     */
    public DataStore(String pathStr, String fileStr, TimeZone timeZone, Locale locale) throws IOException
    {
        validateCreate(pathStr, fileStr);
        dataList = new ArrayList<SingleValue>();
        this.timeZone = timeZone;
        this.locale = locale;
    }

    /**
     * DataLogger Constructor<br>
     * Die übergeben Parameter setzen Pfad und Dateiname der Logdatei und die Standardlänge des Buffers.
     * TimeZone = Europe/Berlin
     * Local = Germany
     *
     * @param pathStr A parameter
     * @param fileStr A parameter
     * @param maxListLength A parameter
     */
    public DataStore(String pathStr, String fileStr) throws IOException
    {
        validateCreate(pathStr, fileStr);
        dataList = new ArrayList<SingleValue>();
        timeZone = TimeZone.getTimeZone("Europe/Berlin");
        locale = Locale.GERMANY;
    }

    /**
     * DataLogger Constructor<br>
     * Setzt folgende Werte:
     * TimeZone = Europe/Berlin
     * Local = Germany
     * Logdatei = DefaultLogger.txt
     * Pfad = USER-DIR
     * Standardlänge des Buffers = 200
     *
     */
    public DataStore() throws IOException
    {
        validateCreate(null, null);
        dataList = new ArrayList<SingleValue>();
        timeZone = TimeZone.getTimeZone("Europe/Berlin");
        locale = Locale.GERMANY;
    }

    /**
     * Method validateCreate<br>
     * Die in den Konstruktoren übergebenen Werte für den Pfad, den Dateiname und die Standardlänge des
     * Buffers werden validiert. Wird für die Pfadangabe pathStr kein Wert übergeben, so wird die
     * Logdatei im USER-DIR angelegt. Wird für den Dateinamen fileStr kein Wert angegeben, so wird die 
     * Logdatei DefaultLogger.txt genannt. Und wenn der Parameter maxListLength einen Wert kleiner oder 
     * gleich 0 hat, wird dieser Wert auf 200 gesetzt.
     *
     * @param pathStr Pfad der Logdatei
     * @param fileStr Name der Logdatei
     * @param maxListLength Standardlänge des Buffers
     */
    private void validateCreate(String pathStr, String fileStr) throws IOException{
        if(pathStr == null || pathStr.isEmpty()){
            pathStr = System.getProperty("user.dir");
        }
        if(fileStr == null || fileStr.isEmpty()){
            fileStr = "DefaultStore.html";
        }
        String ls = System.getProperty("file.separator");
        if(!pathStr.endsWith(ls)){
            pathStr = pathStr + ls;
        }
        this.pathStr = pathStr;
        this.fileStr = fileStr;
        fileSystem = FileSystems.getDefault();
        path = fileSystem.getPath(pathStr, fileStr);
        File file = new File(pathStr + fileStr);
        if(!file.exists()){
            file.createNewFile();
        }
    }

    public synchronized int storeHeaderMapAndSingleValueList(Map<String, String> headerMap, List<SingleValue> dataList){
        StringBuilder htmlString = new StringBuilder(); 
        RestUtil.createTablesFromSingleValue(headerMap, dataList, htmlString);
        return store(htmlString);
    }

    public synchronized int storeHeaderMapAndList(Map<String, String> headerMap, List<String> dataList){
        StringBuilder htmlString = new StringBuilder(); 
        RestUtil.createTablesFromList(headerMap, dataList, htmlString);
        return store(htmlString);
    }

    public synchronized int storeHeaderMapAndDatePointList(Map<String, String> headerMap, List<DiagramDatePoint> dataList){
        StringBuilder htmlString = new StringBuilder(); 
        RestUtil.createTablesFromDiagramDatePoint(headerMap, dataList, htmlString);
        return store(htmlString);
    }

    public synchronized int storeHeaderMapAndMap(Map<String, String> headerMap, Map<String, String> dataMap){
        StringBuilder htmlString = new StringBuilder(); 
        RestUtil.createTablesFromMap(headerMap, dataMap, htmlString);
        return store(htmlString);
    }

    public synchronized Map<String, String> readHeader(){
        StringBuilder htmlString = readFromFile();
        return RestUtil.getHeaderMap(htmlString.toString());
    }

    public synchronized List<String> readList(){
        StringBuilder htmlString = readFromFile();
        return RestUtil.getList(htmlString.toString());
    }

    public synchronized List<SingleValue> readSingleValueList(){
        StringBuilder htmlString = readFromFile();
        return RestUtil.getSingleValueList(htmlString.toString());
    }

    public synchronized List<DiagramDatePoint> readDiagramDatePointList(){
        StringBuilder htmlString = readFromFile();
        return RestUtil.getDiagramDatePointList(htmlString.toString());
    }

    public synchronized Map<String, String> readDataMap(){
        StringBuilder htmlString = readFromFile();
        return null; //Fehlt noch  RestUtil.
    }

    public synchronized int store(StringBuilder stringBuilder){
        if(clearLogfile()==1){
            return writeToFile(stringBuilder);
        }
        return -1;
    }

    /**
     * Method clearLogfile<br>
     * Die Datei wird geleert. Die Methode gibt 1 zurück, wenn die Aktion
     * erfolgreich war. Bei einem Fehler wird -1 zurückgegeben.
     * 
     * @return int
     */
    private synchronized int clearLogfile(){
        int retVal = 1;
        try{
            File file = new File(pathStr + fileStr);
            if(file.exists()){
                file.delete();
            }
            file.createNewFile();
        }catch(IOException ex){
            return -1;
        }
        return retVal;
    }

    private synchronized int writeToFile(StringBuilder stringBuilder){
        int retVal = 0;
        int size = dataList.size();
        try{
            writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8, StandardOpenOption.APPEND);
            writer.append(stringBuilder.toString());
        } catch( IOException ex){
            retVal = -1;
        }finally{
            if(writer != null){
                try{
                    writer.close();
                }catch( IOException ex){
                    retVal = -1;
                }
            }
        }
        return retVal;
    }

    private synchronized StringBuilder readFromFile(){
        StringBuilder lineBuffer;
        try{
            reader = Files.newBufferedReader(path, StandardCharsets.UTF_8);
            lineBuffer = new StringBuilder();
            String line = null;

            while ((line = reader.readLine()) != null) {
                lineBuffer.append(line);
            }

        }catch(IOException ex){
            lineBuffer = new StringBuilder();
        }finally{
            if(reader != null){
                try{
                    reader.close();
                }catch(IOException ex){
                    lineBuffer = new StringBuilder();
                }
            }
        }
        return lineBuffer;
    }
}

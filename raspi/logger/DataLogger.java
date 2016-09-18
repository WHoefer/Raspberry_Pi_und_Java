package raspi.logger;

import java.util.List;
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

/**
 * DataLogger<br>
 * Die Klasse DataLogger stellt eine Buffer bereit in dem verschiedene Datentypen gespeichert 
 * werden können. Ist die Standardgröße des Buffers ereicht, so werden ältere Werte in die Logdatei 
 * speichert. Dieser Vorgang wird durch die Methode store() angestoßen.
 * 
 * @author Wolfgang Höfer
 * @version 1.0
 */
public class DataLogger
{

    private List<SingleValue> dataList =null;
    private Calendar cal = null;
    private TimeZone timeZone = null;
    private Locale locale = null;
    private long maxListLength = 0L;

    private String pathStr = null;
    private String fileStr = null;
    private FileSystem fileSystem = null;
    private Path path = null;
    private BufferedWriter writer = null;
    private BufferedReader reader = null;
    private boolean enable;

    private static final String lineFormat = 
        "%1$tY.%1$tm.%1$td %1$tH:%1$tM:%1$tS:%1$tL %2$02d %3$6.6s %4$s%n";
    private static final String dateFormatStr =
        "yyyy.MM.dd HH:mm:ss:SSS";
    private static final int lengthDateFormat = 23;
    private DateFormat dateFormat = null;    

    /**
     * DataLogger Constructor<br>
     *
     * @param pathStr Speicherpfad der Logdatei
     * @param fileStr Name der Logdatei
     * @param maxListLength Standardlänge des Buffers
     * @param timeZone Zeitzone
     * @param locale Locale
     */
    public DataLogger(String pathStr, String fileStr, long maxListLength, TimeZone timeZone, Locale locale) throws IOException
    {
        validateCreate(pathStr, fileStr, maxListLength);
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
    public DataLogger(String pathStr, String fileStr, long maxListLength) throws IOException
    {
        validateCreate(pathStr, fileStr, maxListLength);
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
    public DataLogger() throws IOException
    {
        validateCreate(null, null, 0L);
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
    private void validateCreate(String pathStr, String fileStr, long maxListLength) throws IOException{
        if(pathStr == null || pathStr.isEmpty()){
            pathStr = System.getProperty("user.dir");
        }
        if(fileStr == null || fileStr.isEmpty()){
            fileStr = "DefaultLogger.txt";
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
        if(maxListLength <= 0L){
            maxListLength = 200L;
        }
        this.maxListLength = maxListLength;
        enable = false;
        dateFormat = new SimpleDateFormat(dateFormatStr);
    }

    /**
     * Method clearLogfile<br>
     * Die Logdatei wird geleert. Die Methode gibt 1 zurück, wenn die Aktion
     * erfolgreich war. Bei einem Fehler wird -1 zurückgegeben.
     * 
     * @return int
     */
    public synchronized int clearLogfile(){
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

    /**
     * Method store<br>
     * Die ältesten Werte werden aus dem Buffer entfernt und in dieLogdatei geschrieben. Dabei werden
     * so viele Werte gespeichert, bis der Buffer auf die Standardlänge reduziert ist.
     *
     * @return int Gibt die Anzahl der gesicherten Werte zurück.
     */
    public synchronized int store(){
        int retVal = 0;
        int size = dataList.size();
        if(enable){
            if(size > maxListLength){
                try{
                    writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8, StandardOpenOption.APPEND);
                    for(int i = 0; i < size - maxListLength; i++){
                        SingleValue singleValue = dataList.remove(0);
                        String line = getLine(singleValue);
                        writer.append(line);
                        retVal++;
                    }
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
            }
        }
        return retVal;
    }

    /**
     * Method storeFlush<br>
     * Alle Werte aus dem Buffer werden in die Logdatei geschrieben und der Buffer geleert.
     * Wenn das Schreiben in die Logdatei gesperrt wurde, beilbt der Aufruf ohne Wirkung.
     * Ist während der Verarbeitung ein Fehler aufgetreten, gibt die Methode -1 zurück.
     * 
     * @return Gibt die Anzahl der gesicherten Werte zurück.
     */
    public synchronized int storeFlush(){
        int retVal = 0;
        int size = dataList.size();
        if(enable){
            try{
                writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8, StandardOpenOption.APPEND);
                for(int i = 0; i < size; i++){
                    SingleValue singleValue = dataList.remove(0);
                    String line = getLine(singleValue);
                    writer.append(line);
                    retVal++;
                }
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
        }
        return retVal;
    }

    /**
     * Method getListFromFile<br>
     * Aus der Logdatei werden die letzten Werte, die die Bezeichnung pinfo haben, gesucht
     * und als Liste von SingleValue-Objekten zurückgegeben. Der Parameter seconds gibt die 
     * Anzahl an Sekunden an, die in die Verganagenheit gesucht wird.
     *
     * @param seconds Rückwärtiger Zeitbereich in Sekunden
     * @param pinfo Bezeichnung für die zu suchenden Werte.
     * @return Liste von SingleValue-Objekten.
     */
    public synchronized List<SingleValue> getListFromFile(int seconds, String pinfo){
        Calendar start = new GregorianCalendar(timeZone, locale);
        start.add(Calendar.SECOND, -seconds);
        Calendar stopp = new GregorianCalendar(timeZone, locale);
        stopp.add(Calendar.YEAR, 1);
        return getListFromFile(start, stopp, pinfo);
    }

    /**
     * Method getListFromFile<br>
     * Für den vorgegebenen Zeitbereich und die Bezeichnung wird eine Liste von SingelValue-Objekten
     * in der Logsatei gesucht und zurückgegeben.
     *
     * @param start Startzeitpunkt der Suche.
     * @param stopp Stoppzeitpunkt der Suche
     * @param pinfo Es werden nur Werte it der Bezeichnung pinfo gesucht.
     * @return Liste von SingleValue-Objekten.
     */
    public synchronized List<SingleValue> getListFromFile(Calendar start, Calendar stopp, String pinfo){
        List<SingleValue> list = new ArrayList<SingleValue>();
        Calendar cal = new GregorianCalendar(timeZone, locale);
        try{
            reader = Files.newBufferedReader(path, StandardCharsets.UTF_8);
            String line = null;

            while ((line = reader.readLine()) != null) {
                Date date = dateFormat.parse(line.substring(0, lengthDateFormat));
                cal.setTime(date);
                if(start.before(cal) && stopp.after(cal)){
                    String info = line.substring(27,33);
                    if(info.equals(pinfo)){
                        String temp;
                        if(line.substring(24,26).startsWith("0")){
                            temp = line.substring(25,26);
                        }else{
                            temp = line.substring(24,26);
                        }
                        int type = Integer.parseInt(temp);

                        String value = line.substring(34);
                        SingleValue singleValue = new SingleValue(cal, value ,type, info );
                        list.add(singleValue);
                    }
                }
            }
        }catch(IOException ex){
            list.clear();
        }catch(ParseException ex){
            list.clear();
        }finally{
            if(reader != null){
                try{
                    reader.close();
                }catch(IOException ex){
                    list.clear();
                }
            }
        }
        return list;
    }

    /**
     * Method DiagramDatePoint<br>
     * Für den vorgegebenen Zeitbereich und die Bezeichnung wird eine Liste von SingelValue-Objekten
     * in der Logsatei gesucht und zurückgegeben.
     *
     * @param start Startzeitpunkt der Suche.
     * @param stopp Stoppzeitpunkt der Suche
     * @param pinfo Es werden nur Werte it der Bezeichnung pinfo gesucht.
     * @return Liste von DiagramDatePoint-Objekten.
     */
    public synchronized List<DiagramDatePoint> getDiagramDatePointListFromFile(Calendar start, Calendar stopp, String pinfo){
        List<DiagramDatePoint> list = new ArrayList<DiagramDatePoint>();
        Calendar cal = new GregorianCalendar(timeZone, locale);
        try{
            reader = Files.newBufferedReader(path, StandardCharsets.UTF_8);
            String line = null;

            while ((line = reader.readLine()) != null) {
                Date date = dateFormat.parse(line.substring(0, lengthDateFormat));
                cal.setTimeInMillis(date.getTime());
                if(start.before(cal) && stopp.after(cal)){
                    String info = line.substring(27,33);
                    if(info.equals(pinfo)){
                        String temp;
                        if(line.substring(24,26).startsWith("0")){
                            temp = line.substring(25,26);
                        }else{
                            temp = line.substring(24,26);
                        }
                        int type = Integer.parseInt(temp);
                        String value = line.substring(34);
                        double y = new Double(value);
                        long x = cal.getTimeInMillis();
                        list.add(new DiagramDatePoint(x,y));
                    }
                }
            }
        }catch(IOException ex){
            list.clear();
        }catch(ParseException ex){
            list.clear();
        }finally{
            if(reader != null){
                try{
                    reader.close();
                }catch(IOException ex){
                    list.clear();
                }
            }
        }
        return list;
    }

    /**
     * Method getLine<br>
     * Formatierte Zeichenkette wird aus einem SingelValue-Objekt erzeugt und
     * zurückgegeben.
     *
     * @param singleValue SingleValue-Objekt
     * @return Formatierte Zeichenkette
     */
    private String getLine(SingleValue singleValue){
        return String.format(locale, lineFormat, 
            singleValue.getCal(), singleValue.getValueType(), 
            singleValue.getInfo(), singleValue.getSingleValue());
    }

    /**
     * Method getListFromFile<br>
     * Aus dem Buffer werden die letzten Werte, die die Bezeichnung pinfo haben, gesucht
     * und als Liste von SingleValue-Objekten zurückgegeben. Der Parameter seconds gibt die 
     * Anzahl an Sekunden an, die in die Verganagenheit gesucht wird.
     *
     * @param seconds Rückwärtiger Zeitbereich in Sekunden
     * @param pinfo Bezeichnung für die zu suchenden Werte.
     * @return Liste von SingleValue-Objekten.
     */
    public synchronized List<SingleValue> getListFromBuffer(int seconds, String pinfo){
        Calendar start = new GregorianCalendar(timeZone, locale);
        start.add(Calendar.SECOND, -seconds);
        Calendar stopp = new GregorianCalendar(timeZone, locale);
        stopp.add(Calendar.YEAR, 1);
        return getListFromBuffer(start, stopp, pinfo);
    }

    /**
     * Method getDiagramDatePointListFromBuffer<br>
     * Aus dem Buffer werden die letzten Werte, die die Bezeichnung pinfo haben, gesucht
     * und als Liste von DiagramDatePoint-Objekten zurückgegeben. Der Parameter seconds gibt die 
     * Anzahl an Sekunden an, die in die Verganagenheit gesucht wird.
     *
     * @param seconds Rückwärtiger Zeitbereich in Sekunden
     * @param pinfo Bezeichnung für die zu suchenden Werte.
     * @return Liste von DiagramDatePoint-Objekten.
     */
    public synchronized List<DiagramDatePoint> getDiagramDatePointListFromBuffer(int seconds, String pinfo){
        Calendar start = new GregorianCalendar(timeZone, locale);
        start.add(Calendar.SECOND, -seconds);
        Calendar stopp = new GregorianCalendar(timeZone, locale);
        stopp.add(Calendar.YEAR, 1);
        return getDiagramDatePointListFromBuffer(start, stopp, pinfo);
    }

    /**
     * Method getListFromBuffer<br>
     * Für den vorgegebenen Zeitbereich und die Bezeichnung wird eine Liste von SingelValue-Objekten
     * in dem Buffer gesucht und zurückgegeben.
     *
     * @param start Startzeitpunkt der Suche.
     * @param stopp Stoppzeitpunkt der Suche
     * @param pinfo Es werden nur Werte it der Bezeichnung pinfo gesucht.
     * @return Liste von SingleValue-Objekten.
     */
    public synchronized List<SingleValue> getListFromBuffer(Calendar start, Calendar stopp, String pinfo){
        List<SingleValue> list = new ArrayList<SingleValue>();
        Calendar cal = new GregorianCalendar(timeZone, locale);
        Iterator it = dataList.iterator();
        while(it.hasNext()){
            SingleValue singleValue = (SingleValue)it.next();
            if(start.before(singleValue.getCal()) && stopp.after(singleValue.getCal())){
                if(singleValue.getInfo() != null && singleValue.getInfo().equals(pinfo)){
                    list.add(singleValue);
                }
            }
        }
        return list;
    }

    /**
     * Method getDiagramDatePointListFromBuffer<br>
     * Für den vorgegebenen Zeitbereich und die Bezeichnung wird eine Liste von 
     * DiagramDatePoint-Objekten in dem Buffer gesucht und zurückgegeben.
     *
     * @param start Startzeitpunkt der Suche.
     * @param stopp Stoppzeitpunkt der Suche
     * @param pinfo Es werden nur Werte it der Bezeichnung pinfo gesucht.
     * @return Liste von DiagramDatePoint-Objekten.
     */
    public synchronized List<DiagramDatePoint> getDiagramDatePointListFromBuffer(Calendar start, Calendar stopp, String pinfo){
        List<DiagramDatePoint> list = new ArrayList<DiagramDatePoint>();
        Calendar cal = new GregorianCalendar(timeZone, locale);
        Iterator it = dataList.iterator();
        while(it.hasNext()){
            SingleValue singleValue = (SingleValue)it.next();
            if(start.before(singleValue.getCal()) && stopp.after(singleValue.getCal())){
                if(singleValue.getInfo() != null && singleValue.getInfo().equals(pinfo)){
                    double y = new Double(singleValue.getSingleValue());
                    long x = singleValue.getCal().getTimeInMillis();
                    list.add(new DiagramDatePoint(x,y));
                }
            }
        }
        return list;
    }

    /**
     * Method getDataList<br>
     * Der Buffer wird zurückgegeben.
     *
     * @return Eine Liste mit SingelValue-Objekten wird zurückgegeben.
     */
    public synchronized List<SingleValue> getDataList(){
        return dataList;
    }

    /**
     * Method add<br>
     * Schreibt einen neuen Wert in den Buffer. Wenn vorher disableFileLog aufgerufen
     * wurde, wächst der Buffer nicht über die eingestellte maximale Anzahl an Werten an.
     *
     * @param singleValue Neuer Wert für den Buffer
     * @param valueType Typ des übergebenen Wertes
     * @param info A Bezeichnung für den übergebenen Wert.
     */
    private synchronized void add(String singleValue, int valueType, String info){
        Calendar cal = new GregorianCalendar(timeZone, locale);
        dataList.add(new SingleValue(cal, singleValue, valueType, info));
        if(!enable){
            int size = dataList.size();
            if(size >= maxListLength){
                SingleValue temp = dataList.remove(0);
            }
        }    
    }

    /**
     * Method enableFileLog<br>
     * Gibt das Schreiben in die Logdatei frei.
     *
     */
    public void enableFileLog(){
        enable = true;
    }    

    /**
     * Method disableFileLog<br>
     * Es wird nach dem Aufruf nicht mehr in die Logdatei geschrieben.
     * Alle Werte bleiben im Buffer. Wird die maximale Größe des Buffers erreicht, wird
     * bei jedem neu eingeschriebenen Wert der ältetste Wert aus dem Buffer entfernt.
     *
     */
    public void disableFileLog(){
        enable = false;
    }    

    /**
     * Method getInfoList<br>
     * Liefert die folgenden Werte als Liste von SingelValue-Objekten
     * FreeMem: Freier Speicher in der aktuellen Partition in Byte.
     * FExist:  Gibt true zurück, wenn die Logdatei existiert, sonst false.
     * Path:    Pfad der Logdatei.
     * Name:    Name der Logdatei.
     * BuffSize:Grüße des Buffers
     * 
     * @return Eine Liste mit SingelValue-Objekten wird zurückgegeben.
     */
    public synchronized List<SingleValue> getInfoList(){
        List<SingleValue> list = new ArrayList<SingleValue>();
        Calendar cal = new GregorianCalendar(timeZone, locale);
        File file = new File(pathStr + fileStr);
        String freeMem = Long.toString(file.getFreeSpace());
        SingleValue singleValue = new SingleValue(cal, freeMem,SingleValue.TYPE_STRING, "FreeMem");
        list.add(singleValue);
        String totalMem = Long.toString(file.getTotalSpace());
        singleValue = new SingleValue(cal, totalMem,SingleValue.TYPE_STRING, "TotMem");
        list.add(singleValue);
        String usableMem = Long.toString(file.getUsableSpace());
        singleValue = new SingleValue(cal, usableMem,SingleValue.TYPE_STRING, "UsebMem");
        list.add(singleValue);
        String fileExist = Boolean.toString(file.exists());
        singleValue = new SingleValue(cal, fileExist,SingleValue.TYPE_STRING, "FExist");
        list.add(singleValue);
        singleValue = new SingleValue(cal, pathStr,SingleValue.TYPE_STRING, "Path");
        list.add(singleValue);
        singleValue = new SingleValue(cal, fileStr,SingleValue.TYPE_STRING, "Name");
        list.add(singleValue);
        String buffSize = Integer.toString(dataList.size());
        singleValue = new SingleValue(cal, buffSize,SingleValue.TYPE_STRING, "BuffSize");
        list.add(singleValue);
        String fileLength = Long.toString(file.length());
        singleValue = new SingleValue(cal, fileLength,SingleValue.TYPE_STRING, "FileLen");
        list.add(singleValue);
        return list;
    }

    public void add(byte value, String info){
        add(Byte.toString(value), SingleValue.STYPE_BYTE, info);
    }

    public void add(Byte value, String info){
        add(value.toString(), SingleValue.TYPE_BYTE, info);
    }

    public void add(short value, String info){
        add(Short.toString(value), SingleValue.STYPE_SHORT, info);
    }

    public void add(Short value, String info){
        add(value.toString(), SingleValue.TYPE_SHORT, info);
    }

    public void add(int value, String info){
        add(Integer.toString(value), SingleValue.STYPE_INTEGER, info);
    }

    public void add(Integer value, String info){
        add(value.toString(), SingleValue.TYPE_INTEGER, info);
    }

    public void add(long value, String info){
        add(Long.toString(value), SingleValue.STYPE_LONG, info);
    }

    public void add(Long value, String info){
        add(value.toString(), SingleValue.TYPE_LONG, info);
    }

    public void add(double value, String info){
        add(Double.toString(value), SingleValue.STYPE_DOUBLE, info);
    }

    public void add(Double value, String info){
        add(value.toString(), SingleValue.TYPE_DOUBLE, info);
    }

    public void add(float value, String info){
        add(Float.toString(value), SingleValue.STYPE_FLOAT, info);
    }

    public void add(Float value, String info){
        add(value.toString(), SingleValue.TYPE_FLOAT, info);
    }

    public void add(char value, String info){
        add(Character.toString(value), SingleValue.STYPE_CHAR, info);
    }

    public void add(Character value, String info){
        add(value.toString(), SingleValue.TYPE_CHAR, info);
    }

    public void add(boolean value, String info){
        add(Boolean.toString(value), SingleValue.STYPE_BOOLEAN, info);
    }

    public void add(Boolean value, String info){
        add(value.toString(), SingleValue.TYPE_BOOLEAN, info);
    }

    public long getMaxListLength(){
        return maxListLength;
    }
}

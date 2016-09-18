package raspi.webservice;

import raspi.logger.SingleValue;
import raspi.logger.DiagramDatePoint;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;
import java.lang.StringBuilder;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.Calendar;
import java.util.GregorianCalendar;
/**
 * Klasse zur Unterstüzung von einfachen Rest-Webservices.
 * 
 * @author Wolfgang Höfer
 * @version 1.0
 */
public class RestUtil
{

    public static final String REGEX_VALID = "[+\\w\\s/<>\\.\\-;!:,öäüÖÄÜß\\%\\?\\*]*?";
    public static final String REGEX_VALID_TEXT = "[+\\w\\s/\\.\\-;!:,öäüÖÄÜß\\%\\?\\*]*?";
    public static final String REGEX_TABLE_DATA = "<table name=\"DATA\"" + REGEX_VALID + "</table>";
    public static final String REGEX_TABLE_HEADER = "<table name=\"HEADER\"" + REGEX_VALID + "</table>";
    public static final String REGEX_ROW   = "<tr>"+ REGEX_VALID +"</tr>";
    public static final String REGEX_COL   = "<td>"+ REGEX_VALID_TEXT +"</td>";

    public static final String HEADER_TABLE   = "HEADER";
    public static final String DATA_TABLE   = "DATA";

    public RestUtil()
    {
    }

    @SuppressWarnings("unchecked")
    public static void createStartHTMLPage(StringBuilder strBuilder) {
        strBuilder.append("<?xml version='1.0' encoding='UTF-8' ?>\n");
        strBuilder.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n");
        CreateHTMLTags.createStartTag("html", null, strBuilder);
        CreateHTMLTags.createStartTag("head", null, strBuilder);
        CreateHTMLTags.createStartTag("title", null, strBuilder);
        strBuilder.append("REST-Webservice");
        CreateHTMLTags.createStoppTag("title", strBuilder);
        Map metaMap = new HashMap();
        metaMap.put("http-equiv", "Content-Type");
        metaMap.put("content", "text/html; charset=UTF-8");
        CreateHTMLTags.createCloseTag("meta", metaMap, strBuilder);
        CreateHTMLTags.createStoppTag("head", strBuilder);
        CreateHTMLTags.createStartTag("body", null, strBuilder);
    }

    @SuppressWarnings("unchecked")
    public static void createStoppHTMLPage(StringBuilder strBuilder) {
        CreateHTMLTags.createStoppTag("body", strBuilder);
        CreateHTMLTags.createStoppTag("html", strBuilder);
    }

    @SuppressWarnings("unchecked")
    private static Map getNameHeader(){
        Map nameMap = new HashMap();
        nameMap.put("name", HEADER_TABLE);
        return nameMap;
    }

    @SuppressWarnings("unchecked")
    private static Map getNameData(){
        Map nameMap = new HashMap();
        nameMap.put("name", DATA_TABLE);
        return nameMap;
    }

    @SuppressWarnings("unchecked")
    public static void createKVTable(Map<String, String> map, StringBuilder strBuilder, Map nameMap){
        if (map != null) {
            CreateHTMLTags.createStartTag("table", nameMap, strBuilder);
            Set<Entry<String, String>> entrySet = map.entrySet();
            for (Iterator<Entry<String, String>> it = entrySet.iterator(); it.hasNext();) {
                CreateHTMLTags.createStartTag("tr", null, strBuilder);
                Entry<String, String> entry = it.next();
                CreateHTMLTags.createStartTag("td", null, strBuilder);
                strBuilder.append(entry.getKey());
                CreateHTMLTags.createStoppTag("td", strBuilder);
                CreateHTMLTags.createStartTag("td", null, strBuilder);
                strBuilder.append(entry.getValue());
                CreateHTMLTags.createStoppTag("td", strBuilder);
                CreateHTMLTags.createStoppTag("tr", strBuilder);
            }
            CreateHTMLTags.createStoppTag("table", strBuilder);
        }
    }

    @SuppressWarnings("unchecked")
    public static void createTable(List<String> list, StringBuilder strBuilder){
        if (list != null) {
            CreateHTMLTags.createStartTag("table", getNameData(), strBuilder);
            for (Iterator it = list.iterator(); it.hasNext();) {
                CreateHTMLTags.createStartTag("tr", null, strBuilder);
                CreateHTMLTags.createStartTag("td", null, strBuilder);
                strBuilder.append(it.next());
                CreateHTMLTags.createStoppTag("td", strBuilder);
                CreateHTMLTags.createStoppTag("tr", strBuilder);
            }
            CreateHTMLTags.createStoppTag("table", strBuilder);
        }
    }

    @SuppressWarnings("unchecked")
    public static void createSingleValueTable(List<SingleValue> list, StringBuilder strBuilder){
        if (list != null) {
            CreateHTMLTags.createStartTag("table", getNameData(), strBuilder);
            for (Iterator it = list.iterator(); it.hasNext();) {
                SingleValue singleValue = (SingleValue)it.next();
                CreateHTMLTags.createStartTag("tr", null, strBuilder);
                CreateHTMLTags.createStartTag("td", null, strBuilder);
                strBuilder.append(Long.toString(singleValue.getCal().getTimeInMillis()));
                CreateHTMLTags.createStoppTag("td", strBuilder);
                CreateHTMLTags.createStartTag("td", null, strBuilder);
                strBuilder.append(singleValue.getInfo());
                CreateHTMLTags.createStoppTag("td", strBuilder);
                CreateHTMLTags.createStartTag("td", null, strBuilder);
                strBuilder.append(Integer.toString(singleValue.getValueType()));
                CreateHTMLTags.createStoppTag("td", strBuilder);
                CreateHTMLTags.createStartTag("td", null, strBuilder);
                strBuilder.append(singleValue.getSingleValue());
                CreateHTMLTags.createStoppTag("td", strBuilder);
                CreateHTMLTags.createStoppTag("tr", strBuilder);
            }
            CreateHTMLTags.createStoppTag("table", strBuilder);
        }
    }

    @SuppressWarnings("unchecked")
    public static void createDiagramDatePointTable(List<DiagramDatePoint> list, StringBuilder strBuilder){
        if (list != null) {
            CreateHTMLTags.createStartTag("table", getNameData(), strBuilder);
            for (Iterator it = list.iterator(); it.hasNext();) {
                DiagramDatePoint point = (DiagramDatePoint)it.next();
                CreateHTMLTags.createStartTag("tr", null, strBuilder);
                CreateHTMLTags.createStartTag("td", null, strBuilder);
                strBuilder.append(Long.toString(point.getX()));
                CreateHTMLTags.createStoppTag("td", strBuilder);
                CreateHTMLTags.createStartTag("td", null, strBuilder);
                strBuilder.append(Double.toString(point.getY()));
                CreateHTMLTags.createStoppTag("td", strBuilder);
                CreateHTMLTags.createStoppTag("tr", strBuilder);
            }
            CreateHTMLTags.createStoppTag("table", strBuilder);
        }
    }

    /**
     * Method getMap
     *
     * @param htmlString A parameter
     * @param regexTable A parameter
     * @return The return value
     */
    @SuppressWarnings("unchecked")
    public static Map<String, String> getMap(String htmlString, String regexTable){
        Map<String, String> map = new HashMap<String, String>();
        if(htmlString != null){
            htmlString = htmlString.replaceAll("[\\n\\r\\t]", ""); 
            Pattern tablePattern = Pattern.compile(regexTable);
            Matcher tableMatcher = tablePattern.matcher(htmlString);
            Pattern rowPattern = Pattern.compile(REGEX_ROW);
            Matcher rowMatcher = null;
            Pattern colPattern = Pattern.compile(REGEX_COL);
            Matcher colMatcher = null;
            String key = "";
            String value = "";
            if (tableMatcher != null) {
                while (tableMatcher.find()) {
                    rowMatcher = rowPattern.matcher(tableMatcher.group());
                    if (rowMatcher != null) {
                        while (rowMatcher.find()) {
                            colMatcher = colPattern.matcher(rowMatcher.group());
                            if(colMatcher != null){
                                while (colMatcher.find()) {
                                    key = colMatcher.group();
                                    key = key.replaceAll("<td>|</td>","");
                                    if(colMatcher.find()){
                                        value = colMatcher.group();
                                        value = value.replaceAll("<td>|</td>","");
                                        map.put(key, value);
                                    }else{ map.put(key, "null");}
                                }
                            }
                        }
                    }
                }
            }
        }
        return map;
    }

    /**
     * Method getList<br>
     * 
     * Parst den HTML-String nach einer einspaltige Tabelle mit dem Namen DATA und liefert 
     * den Inhalt in eine Liste mit String-Objekten zurück.
     *
     * @param htmlString HTML-String
     * @return Liste mit String-Objekten
     */
    @SuppressWarnings("unchecked")
    public static List<String> getList(String htmlString){
        List<String> list = new ArrayList<String>();
        if(htmlString != null){
            htmlString = htmlString.replaceAll("[\\n\\r\\t]", ""); 
            Pattern tablePattern = Pattern.compile(REGEX_TABLE_DATA);
            Matcher tableMatcher = tablePattern.matcher(htmlString);
            Pattern rowPattern = Pattern.compile(REGEX_ROW);
            Matcher rowMatcher = null;
            Pattern colPattern = Pattern.compile(REGEX_COL);
            Matcher colMatcher = null;
            String value = "";
            if (tableMatcher != null) {
                while (tableMatcher.find()) {
                    rowMatcher = rowPattern.matcher(tableMatcher.group());
                    if (rowMatcher != null) {
                        while (rowMatcher.find()) {
                            colMatcher = colPattern.matcher(rowMatcher.group());
                            if(colMatcher != null){
                                while (colMatcher.find()) {
                                    value = colMatcher.group();
                                    value = value.replaceAll("<td>|</td>","");
                                    list.add(value);
                                }
                            }
                        }
                    }
                }
            }
        }
        return list;
    }

    /**
     * Method getSingleValueList<br>
     * 
     * Parst den HTML-String nach einer vierspaltige Tabelle mit dem Namen DATA und liefert eine 
     * Liste mit SingleValue-Objekten zurück.
     *
     * @param htmlString HTML-String
     * @return Liste mit SingleValue-Objekten
     */
    @SuppressWarnings("unchecked")
    public static List<SingleValue> getSingleValueList(String htmlString){
        List<SingleValue> list = new ArrayList();
        if(htmlString != null){
            htmlString = htmlString.replaceAll("[\\n\\r\\t]", ""); 
            Pattern tablePattern = Pattern.compile(REGEX_TABLE_DATA);
            Matcher tableMatcher = tablePattern.matcher(htmlString);
            Pattern rowPattern = Pattern.compile(REGEX_ROW);
            Matcher rowMatcher = null;
            Pattern colPattern = Pattern.compile(REGEX_COL);
            Matcher colMatcher = null;
            String cal = "";
            String typ = "";
            String info = "";
            String value = "";
            if (tableMatcher != null) {
                while (tableMatcher.find()) {
                    rowMatcher = rowPattern.matcher(tableMatcher.group());
                    if (rowMatcher != null) {
                        while (rowMatcher.find()) {
                            colMatcher = colPattern.matcher(rowMatcher.group());
                            if(colMatcher != null){
                                while (colMatcher.find()) {
                                    cal = colMatcher.group();
                                    cal = cal.replaceAll("<td>|</td>","");
                                    if(colMatcher.find()){
                                        info = colMatcher.group();
                                        info = info.replaceAll("<td>|</td>","");
                                    }
                                    if(colMatcher.find()){
                                        typ = colMatcher.group();
                                        typ = typ.replaceAll("<td>|</td>","");
                                    }
                                    if(colMatcher.find()){
                                        value = colMatcher.group();
                                        value = value.replaceAll("<td>|</td>","");
                                    }
                                    Calendar cal1 = GregorianCalendar.getInstance();
                                    cal1.setTimeInMillis(Long.parseLong(cal));
                                    list.add(new SingleValue(cal1,info,Integer.parseInt(typ),value));
                                }
                            }
                        }
                    }
                }
            }
        }
        return list;
    }

    /**
     * Method getDiagramDatePointList<br>
     * 
     * Parst den HTML-String und liefert eine Liste mit DiagramDatePoint-Objekten zurück.
     *
     * @param htmlString A parameter
     * @return The return value
     */
    @SuppressWarnings("unchecked")
    public static List<DiagramDatePoint> getDiagramDatePointList(String htmlString){
        List<DiagramDatePoint> list = new ArrayList();
        if(htmlString != null){
            htmlString = htmlString.replaceAll("[\\n\\r\\t]", ""); 
            Pattern tablePattern = Pattern.compile(REGEX_TABLE_DATA);
            Matcher tableMatcher = tablePattern.matcher(htmlString);
            Pattern rowPattern = Pattern.compile(REGEX_ROW);
            Matcher rowMatcher = null;
            Pattern colPattern = Pattern.compile(REGEX_COL);
            Matcher colMatcher = null;
            String x = "";
            String y = "";
            if (tableMatcher != null) {
                while (tableMatcher.find()) {
                    rowMatcher = rowPattern.matcher(tableMatcher.group());
                    if (rowMatcher != null) {
                        while (rowMatcher.find()) {
                            colMatcher = colPattern.matcher(rowMatcher.group());
                            if(colMatcher != null){
                                while (colMatcher.find()) {
                                    x = colMatcher.group();
                                    x = x.replaceAll("<td>|</td>","");
                                    if(colMatcher.find()){
                                        y = colMatcher.group();
                                        y = y.replaceAll("<td>|</td>","");
                                    }
                                    list.add(new DiagramDatePoint(Long.parseLong(x),Double.parseDouble(y)));
                                }
                            }
                        }
                    }
                }
            }
        }
        return list;
    }

    
    /**
     * Methode getHeaderMap<br>
     * getHeaderMap parst eine HTML-Seite nach einer Tabelle mit zwei Spalten
     * und dem Namen HEADER. Der Inhalt der gefundenen Tabelle wird in eine
     * Map geschrieben.
     *
     * @param htmlString HTML-Seite
     * @return Map mit den geparsten Werten.
     */
    public static Map<String, String> getHeaderMap(String htmlString){
         return getMap(htmlString, REGEX_TABLE_HEADER);
    }
    
    /**
     * Methode getDataMap<br>
     * getDataMap parst eine HTML-Seite nach einer Tabelle mit zwei Spalten
     * und dem Namen DATA. Der Inhalt der gefundenen Tabelle wird in eine
     * Map geschrieben.
     *
     * @param htmlString HTML-Seite
     * @return Map mit den geparsten Werten.
     */
    public static Map getDataMap(String htmlString){
         return getMap(htmlString, REGEX_TABLE_DATA);
    }
    
    /**
     * Method createTablesFromMap<br>
     * 
     * Erzeugt eine HTML-Seite mit zwei zweispaltigen HTML-Tabellen. Die erste Tabelle wird aus dem Übergabeparameter
     * headerMap erzeugt. Die zweite Tabelle wird aus dem Übergabeparameter dataMap erszeugt.
     *
     * @param headerMap Eine Map Mit Schlüssel-/Wertepaar, die in die HTML-Tabelle mit dem Namen HEADER gerendert werden.
     * @param dataMap Eine Map Mit Schlüssel-/Wertepaar, die in die HTML-Tabelle mit dem Namen DATA gerendert werden.
     * @param htmlString A parameter
     */
    @SuppressWarnings("unchecked")
    public static void createTablesFromMap(Map<String, String> headerMap, Map<String, String> dataMap, StringBuilder htmlString){
        createStartHTMLPage(htmlString);
        createKVTable(headerMap, htmlString, getNameHeader());
        createKVTable(dataMap, htmlString, getNameData());
        createStoppHTMLPage(htmlString);
    }

    /**
     * Method createTablesFromList<br>
     * 
     * Erzeugt eine HTML-Seite mit zwei zweispaltigen HTML-Tabellen. Die erste Tabelle wird aus dem Übergabeparameter
     * headerMap erzeugt. Die zweite Tabelle wird aus dem Übergabeparameter dataList erszeugt.
     *
     * @param headerMap Eine Map Mit Schlüssel-/Wertepaar, die in die HTML-Tabelle mit dem Namen HEADER gerendert werden.
     * @param dataList Eine Liste mit String-Objekten, die in die HTML-Tabelle mit dem Namen DATA gerendert werden.
     * @param htmlString StringBuilder, in dem die gerenderte HTML-Antwort abgelegt wird.
     */
    @SuppressWarnings("unchecked")
    public static void createTablesFromList(Map<String, String> headerMap, List<String> dataList, StringBuilder htmlString){
        createStartHTMLPage(htmlString);
        createKVTable(headerMap, htmlString, getNameHeader());
        createTable(dataList, htmlString);
        createStoppHTMLPage(htmlString);
    }

    /**
     * Method createTablesFromSingleValue<br>
     * 
     * Erzeugt eine HTML-Seite mit zwei zweispaltigen HTML-Tabellen. Die erste Tabelle wird aus dem Übergabeparameter
     * headerMap erzeugt. Die zweite Tabelle wird aus dem Übergabeparameter dataList erszeugt.
     *
     * @param headerMap Eine Map Mit Schlüssel-/Wertepaar, die in die HTML-Tabelle mit dem Namen HEADER gerendert werden.
     * @param dataList Eine Liste mit SingleValue-Objekten, die in die HTML-Tabelle mit dem Namen DATA gerendert werden.
     * @param htmlString StringBuilder, in dem die gerenderte HTML-Antwort abgelegt wird.
     *
     */
    @SuppressWarnings("unchecked")
    public static void createTablesFromSingleValue(Map<String, String> headerMap, List<SingleValue> dataList, StringBuilder htmlString){
        createStartHTMLPage(htmlString);
        createKVTable(headerMap, htmlString, getNameHeader());
        createSingleValueTable(dataList, htmlString);
        createStoppHTMLPage(htmlString);
    }

    /**
     * Method createTablesFromDiagramDatePoint<br>
     * 
     * Erzeugt eine HTML-Seite mit zwei zweispaltigen HTML-Tabellen. Die erste Tabelle wird aus dem Übergabeparameter
     * headerMap erzeugt. Die zweite Tabelle wird aus dem Übergabeparameter dataList erszeugt.
     *
     * @param headerMap Eine Map Mit Schlüssel-/Wertepaar, die in die HTML-Tabelle mit dem Namen HEADER gerendert werden.
     * @param dataList Eine Liste mit DiagramDatePoint-Objekten, die in die HTML-Tabelle mit dem Namen DATA gerendert werden.
     * @param htmlString StringBuilder, in dem die gerenderte HTML-Antwort abgelegt wird.
     *
     */
    @SuppressWarnings("unchecked")
    public static void createTablesFromDiagramDatePoint(Map<String, String> headerMap, List<DiagramDatePoint> dataList, StringBuilder htmlString){
        createStartHTMLPage(htmlString);
        createKVTable(headerMap, htmlString, getNameHeader());
        createDiagramDatePointTable(dataList, htmlString);
        createStoppHTMLPage(htmlString);
    }

    @SuppressWarnings("unchecked")
    public static void main(String[] args){
        Map headerMap = new HashMap();
        headerMap.put("Action", "GET");
        headerMap.put("DATE", "17.08.2015");

        List<String> listin = new ArrayList();
        listin.add("Wert 1");
        listin.add("Wert 2");
        listin.add("Wert 3");
        listin.add("Wert 4");
        listin.add("Wert 5");
        listin.add("Wert 6");
        listin.add("Wert 7");
        StringBuilder stb = new StringBuilder();
        createTablesFromList(headerMap,listin, stb);
        List list = getList(stb.toString());
        Map map = getMap(stb.toString(), REGEX_TABLE_HEADER);
        Set es = map.entrySet();
        Iterator it = es.iterator();
        while(it.hasNext()){
            Entry entry = (Entry)it.next();
            System.out.println(entry.getKey()+" = " + entry.getValue());
        }
        Iterator itl = list.iterator();
        while(itl.hasNext()){
            String entry = (String)itl.next();
            System.out.println(entry);
        }
        //System.out.println(stb);

        Map<String, String> listm = new HashMap();
        listm.put("k1", "v1");
        listm.put("k2", "v2");
        listm.put("k3", "v3");
        StringBuilder stbm = new StringBuilder();
        createTablesFromMap(headerMap, listm, stbm);
        String wert = stbm.toString();
        //System.out.println(wert);
        Map mapH = getMap(wert, REGEX_TABLE_HEADER);
        Map mapD = getMap(wert, REGEX_TABLE_DATA);
        es = mapH.entrySet();
        it = es.iterator();
        while(it.hasNext()){
            Entry entry = (Entry)it.next();
            System.out.println(entry.getKey()+" = " + entry.getValue());
        }
        es = mapD.entrySet();
        it = es.iterator();
        while(it.hasNext()){
            Entry entry = (Entry)it.next();
            System.out.println(entry.getKey()+" = " + entry.getValue());
        }

        List svList = new ArrayList();
        Calendar calTest = new GregorianCalendar();
        svList.add(new SingleValue(calTest, "INFO", 1, "23,34683563"));
        svList.add(new SingleValue(calTest, "INFO", 1, "21,33"));
        svList.add(new SingleValue(calTest, "INFO", 1, "18,0E+03"));
        svList.add(new SingleValue(calTest, "INFO", 1, "23,63"));
        stbm = new StringBuilder();
        createTablesFromSingleValue(headerMap, svList, stbm);
        map = getMap(stbm.toString(), REGEX_TABLE_HEADER);
        es = map.entrySet();
        it = es.iterator();
        while(it.hasNext()){
            Entry entry = (Entry)it.next();
            System.out.println(entry.getKey()+" = " + entry.getValue());
        }
        List resList = getSingleValueList(stbm.toString());
        itl = resList.iterator();
        while(itl.hasNext()){
            SingleValue sv = (SingleValue)itl.next();
            System.out.printf("%1$tH:%1$tM:%1$tS %2$s %3$d %4$s %n", sv.getCal(), sv.getInfo(), sv.getValueType(), sv.getSingleValue());
        }
    }
}

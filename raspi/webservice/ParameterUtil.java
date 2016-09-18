package raspi.webservice;

import com.sun.net.httpserver.HttpExchange;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * ParameterUtil ist ein Werkzeug,um Parameter aus einer URL zu extrahieren.
 * 
 * @author Wolfgang Höfer
 * @version 1.0
 */
public class ParameterUtil {
    
    
    /**
     * getParameters liefert eine Map mit Parametern zurück.
     *
     * @param httpExchange A parameter
     * @return The return value
     */
    public static Map getParameters(HttpExchange httpExchange) throws IOException {

        Map<String, Object> parameters = new HashMap<String, Object>();
        if ("post".equalsIgnoreCase(httpExchange.getRequestMethod())) {
            parsePostParameters(httpExchange, parameters);
        } else {
            parseGetParameters(httpExchange, parameters);
        }
        return parameters;
    }

    /**
     * creatOutParameters schreibt die gefundenen Parameter auf die Konsole.
     *
     * @param parameters A parameter
     */
    public static void creatOutParameters(Map<String, Object> parameters) {
        System.out.printf("%n %nParameter auswerten! %n");
        System.out.printf(" %1$-15s   %2$-15s %n", "Parameter", "Wert");
        Set<String> keySet = parameters.keySet();
        Iterator<String> it = keySet.iterator();
        for (Iterator<String> it1 = keySet.iterator(); it1.hasNext();) {
            String key = it1.next();
            Object value = parameters.get(key);
            System.out.printf(" %1$-15s   %2$-15s %n", key, value);
        }
    }

    /**
     * viewParameters schreibt die gefundenen Parameter auf die Konsole.
     *
     * @param httpExchange A parameter
     */
    @SuppressWarnings("unchecked")
    public static void viewParameters(HttpExchange httpExchange) throws IOException{
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters = getParameters(httpExchange);
        creatOutParameters(parameters);
    }

    /**
     * viewParameters schreibt die gefundenen Parameter auf die Konsole.
     *
     *
     * @param parameters A parameter
     */
    @SuppressWarnings("unchecked")
    public static void viewParameters(Map<String, Object> parameters) throws IOException{
        creatOutParameters(parameters);
    }

    /**
     * parseGetParameters liest die Parameter aus der URI.
     *
     * @param exchange A parameter
     * @param parameters A parameter
     */
    @SuppressWarnings("unchecked")
    private static void parseGetParameters(HttpExchange exchange, Map<String, Object> parameters)
    throws UnsupportedEncodingException {
        URI requestedUri = exchange.getRequestURI();
        String query = requestedUri.getRawQuery();
        parseQuery(query, parameters);
    }

    /**
     * parsePostParameters liest die Parameter aus dem HTTP-Body
     *
     * @param exchange A parameter
     * @param parameters A parameter
     */
    @SuppressWarnings("unchecked")
    private static void parsePostParameters(HttpExchange exchange, Map<String, Object> parameters)
    throws IOException, UnsupportedEncodingException {
        if ("post".equalsIgnoreCase(exchange.getRequestMethod())) {
            InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), "utf-8");
            BufferedReader br = new BufferedReader(isr);
            String query = br.readLine();
            parseQuery(query, parameters);
        }
    }

    /**
     * parseQuery liest die Parameter aus einer Query.
     *
     * @param query A parameter
     * @param parameters A parameter
     */
    @SuppressWarnings("unchecked")
    private static void parseQuery(String query, Map<String, Object> parameters)
    throws UnsupportedEncodingException {

        if (query != null) {
            String pairs[] = query.split("[&]");

            for (String pair : pairs) {
                String param[] = pair.split("[=]");

                String key = null;
                String value = null;
                if (param.length > 0) {
                    key = URLDecoder.decode(param[0], System.getProperty("file.encoding"));
                }

                if (param.length > 1) {
                    value = URLDecoder.decode(param[1], System.getProperty("file.encoding"));
                }

                if (parameters.containsKey(key)) {
                    Object obj = parameters.get(key);
                    if (obj instanceof List<?>) {
                        List<String> values = (List<String>) obj;
                        values.add(value);
                    } else if (obj instanceof String) {
                        List<String> values = new ArrayList<String>();
                        values.add((String) obj);
                        values.add(value);
                        parameters.put(key, values);
                    }
                } else {
                    parameters.put(key, value);
                }
            }
        }
    }
}

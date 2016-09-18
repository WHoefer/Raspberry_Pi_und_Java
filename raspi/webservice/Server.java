package raspi.webservice;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;    
import java.io.IOException;  
import java.net.InetAddress;
import java.net.InetSocketAddress;  
import java.net.UnknownHostException;
import raspi.logger.DataLogger;

/**
 * HttpServer
 * 
 * @author Wolfgang Höfer 
 * @version 1.0
 */
public class Server
{
    public static final int DEFAULT_PORT = 8080;  
    public static final String DEFAULT_ROOT = "/daten";
    
    final HttpServer server;
    final String hostName;
    final String ctxRoot;
    final int port;

    /**
     * Constructor for objects of class HttpServer
     */
    public Server(final String[] args, HttpHandler handler) throws Exception
    {
        hostName = InetAddress.getLocalHost().getHostAddress();
        ctxRoot = contextRoot(args);
        port = serverPort(args);
        server = HttpServer.create(new InetSocketAddress(port), 0);
        if(handler != null){
            server.createContext(ctxRoot, handler);  
            server.start();  
            System.out.printf("%n HTTP Server ist gestartet und unter folgender URL zu erreichen.%n");
            System.out.println("http://" + hostName + ":" + port + ctxRoot);
        }else{
            System.out.printf("%n HTTP Server wurde nicht gestartet, da kein HttpHandler übergeben wurde.%n");
        }
    }

    private static String contextRoot(final String[] args)
    {
        String defRoot = DEFAULT_ROOT;
        final int laenge = args.length;
        if (laenge > 0)
        {
            final String ctxRoot = args[0];
            defRoot = (ctxRoot.startsWith("/")) ? ctxRoot : "/" + ctxRoot;
        }
        return defRoot;
    }

    private static int serverPort(final String[] args)
    {
        int port = DEFAULT_PORT;
        final int laenge = args.length;
        if (laenge > 1)
        {
            port = Integer.parseInt(args[1]);
        }
        return port;
    }

}


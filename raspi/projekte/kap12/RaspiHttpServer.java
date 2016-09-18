package raspi.projekte.kap12;

import com.sun.net.httpserver.HttpServer;  
import java.io.IOException;  
import java.net.InetAddress;
import java.net.InetSocketAddress;  
import java.net.UnknownHostException;
import raspi.logger.DataLogger;

/**
 * Write a description of class RaspiHttpServer here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class RaspiHttpServer
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
    public RaspiHttpServer(final String[] args) throws Exception
    {
      hostName = InetAddress.getLocalHost().getHostAddress();
      ctxRoot = contextRoot(args);
      port = serverPort(args);
      server = HttpServer.create(new InetSocketAddress(port), 0);
      server.createContext(ctxRoot, new HttpHandlerZisterne());  
      System.out.printf("%n HTTP Server ist gestartet und unter folgender URL zu erreichen.%n");
      System.out.println("http://" + hostName + ":" + port + ctxRoot);
      server.start();  
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
   
   public static void main(String[] args) throws Exception {
     new RaspiHttpServer(args);
   }
   
}

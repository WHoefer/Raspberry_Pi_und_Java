package raspi.projekte.kap13;

import raspi.webservice.Server;
import com.sun.net.httpserver.HttpHandler;

/**
 * Die Klasse RaspiHttpServer erzeugt eine Instanz der Klasse Server
 * und übergibt dem Konstruktor eine Instanz der Klasse HttpHandlerHeizung
 * und die Kommandozeilenparameter.
 * 
 * @author Wolfgang Höfer
 * @version 1.0
 */
public class RaspiHttpServer
{
    public static void main(String[] args)throws Exception
    {
        new Server(args, new HttpHandlerHeizung());
    }
}

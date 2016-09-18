package raspi.projekte.kap14;

import raspi.webservice.Server;
/**
 * Die Klasse RaspiHttpServer erzeugt eine Instanz der Klasse Server
 * und übergibt dem Konstruktor eine Instanz der Klasse HttpHandlerZeitschaltuhr
 * und die Kommandozeilenparameter.
 * 
 * @author Wolfgang Höfer
 * @version 1.0
 */
public class RaspiHttpServer
{
    public static void main(String[] args)throws Exception
    {
        new Server(args, new HttpHandlerZeitschaltuhr());
    }
}

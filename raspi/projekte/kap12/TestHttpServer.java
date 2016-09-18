package raspi.projekte.kap12;

import com.sun.net.httpserver.HttpServer;  
import java.io.IOException;  
import java.net.InetAddress;
import java.net.InetSocketAddress;  
import java.net.UnknownHostException;

public class TestHttpServer
{

    final HttpServer server;

    public TestHttpServer() throws Exception
    {
        server = HttpServer.create(new InetSocketAddress(80), 0);
        server.createContext("/test", new TestHttpHandler());  
        System.out.printf("%n HTTP Server ist gestartet.%n");
        server.start();  
    }

    public static void main(String[] args) throws Exception {
        new TestHttpServer();
    }

}

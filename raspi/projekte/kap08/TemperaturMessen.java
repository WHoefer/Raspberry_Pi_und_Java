package raspi.projekte.kap08;

import raspi.hardware.spi.MCP3002;

/**
 * Write a description of class TemperarurMessen here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class TemperaturMessen
{
    public static void main(String[] args) throws InterruptedException {
        MCP3002 mcp = new MCP3002(MCP3002.CS0, MCP3002.CLOCK1M);
        while(true){
            Thread.sleep(1000);
            double u = mcp.readChannelInVolt(0);
            double t = -12.121d * (u - 3.239d);
            System.out.printf("Temperatur: %1$4.2f °C %n",t);
        }    

    }
}

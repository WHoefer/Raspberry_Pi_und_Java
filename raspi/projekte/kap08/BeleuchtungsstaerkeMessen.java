package raspi.projekte.kap08;

import raspi.hardware.spi.MCP3002;

/**
 * Write a description of class TemperarurMessen here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class BeleuchtungsstaerkeMessen
{
    public static void main(String[] args) throws InterruptedException {
        MCP3002 mcp = new MCP3002(MCP3002.CS0, MCP3002.CLOCK1M);
        while(true){
            Thread.sleep(1000);
            double u = mcp.readChannelInVolt(1);
            double e = Math.pow((5.0237d*(3.3/u - 1)), 1.4286);
            System.out.printf("Beleuchtungsstärke: %1$4.2f lx %n",e);
        }    

    }
}

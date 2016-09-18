package raspi.projekte.kap08;

import raspi.hardware.spi.MCP3002;


/**
 * Write a description of class PotentiometerAuslesen here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class PotentiometerAuslesen
{
    public static void main(String[] args) throws InterruptedException {
        MCP3002 mcp = new MCP3002(MCP3002.CS0, MCP3002.CLOCK1M);
        int int0 = 0;
        int int1 = 0;
        while(true){
            Thread.sleep(1000);
            int0 = mcp.readChannel(0);
            int1 = mcp.readChannel(1);
            System.out.printf("%1$s   %2$s %n",int0, int1);
        }    

    }
}

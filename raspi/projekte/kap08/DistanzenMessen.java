package raspi.projekte.kap08;

import raspi.hardware.spi.MCP3002;
import raspi.hardware.GP2Distance20To150;

/**
 * Testprogramm für den Distanzsensor GP2YA02YK0F
 * 
 * @author Wolfgang Höfer
 * @version 1.0
 */
public class DistanzenMessen
{
    public static void main(String[] args) throws InterruptedException {
        MCP3002 mcp = new MCP3002(MCP3002.CS0, MCP3002.CLOCK1M);
        GP2Distance20To150 ds = new GP2Distance20To150(); 
        double distanz;
        while(true){
            Thread.sleep(1000);
            double u = mcp.readChannelInVolt(0);
            if(ds.isValidValue(u)){
              distanz = ds.getValue(u); 
              System.out.printf("Distanz: %1$4.2f cm %n",distanz);
            }    
        }    
    }
}

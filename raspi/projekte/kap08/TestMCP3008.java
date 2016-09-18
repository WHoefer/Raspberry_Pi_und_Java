package raspi.projekte.kap08;
import raspi.hardware.spi.MCP3008;

/**
 * Write a description of class TestMCP3008 here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class TestMCP3008
{

    public static void main(String[] args) throws InterruptedException {
        MCP3008 mcp = new MCP3008(MCP3008.CS0, MCP3008.CLOCK1M);
        while(true){
            Thread.sleep(1000);
            double c0 = mcp.readChannelInVolt(0);
            double c1 = mcp.readChannelInVolt(1);
            double c2 = mcp.readChannelInVolt(2);
            double c3 = mcp.readChannelInVolt(3);
            double c4 = mcp.readChannelInVolt(4);
            double c5 = mcp.readChannelInVolt(5);
            double c6 = mcp.readChannelInVolt(6);
            double c7 = mcp.readChannelInVolt(7);
            double t = -12.121d * (c5 - 3.239d);
            double e = Math.pow((5.0237d*(3.3/c1 - 1)), 1.4286);
            System.out.printf("Temperatur: %1$4.2f °C  Beleuchtungsstärke: %2$4.2f lx Spannung: %3$4.2f %n",t, e, c0);
        }    

    }
}

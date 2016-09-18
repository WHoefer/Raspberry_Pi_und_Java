package raspi.projekte.kap09;

import raspi.hardware.spi.MCP3008;
import raspi.schedule.*;
import raspi.logger.DataLogger;
import java.text.ParseException;
import java.io.IOException;

public class DatenSpeicher
{
    public static void main(String[] args) throws ParseException, InterruptedException, IOException{
        MCP3008 mcp = new MCP3008(MCP3008.CS0, MCP3008.CLOCK1M);
        DataLogger log = new DataLogger();
        log.enableFileLog();
        String messen = ScheduleUtil.startPulseInSecundes(0, 4, 0);
        String speichern = ScheduleUtil.startPulseInSecundes(0, 4, 500);
        int c = 0;
        double spannungPort5 = 0;
        double spannungPort1 = 0;
        double temperatur = 0;
        double beleuchtungsstaerke = 0;
        while (c == 0) {
            if(ScheduleService.scheduleCheckForLongTimeUse(messen)){ 
                spannungPort5 = mcp.readChannelInVolt(5);
                spannungPort1 = mcp.readChannelInVolt(1);
                temperatur = -12.170d * (spannungPort5 - 3.239d);
                beleuchtungsstaerke = Math.pow((5.0237d*(3.3/spannungPort1 - 1)), 1.4286);
                log.add(temperatur, "TEMPERAT");
                log.add(beleuchtungsstaerke, "BELEUCHT");
                messen = ScheduleUtil.startPulseInSecundes(30, 500, 0);
            }
            if(ScheduleService.scheduleCheckForLongTimeUse(speichern)){
                log.store();
                speichern = ScheduleUtil.startPulseInSecundes(3600, 500, 0);
            }
            Thread.sleep(500);
            c = System.in.available();
        }
        log.storeFlush();
    }
}

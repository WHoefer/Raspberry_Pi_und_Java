package raspi.projekte.kap18;

import  com.pi4j.io.i2c.*;
import  java.io.IOException;
import raspi.hardware.i2c.MCP23017; 
import raspi.hardware.lcd.DisplayHD44780;
import raspi.hardware.lcd.PinMCP23017;
import raspi.hardware.lcd.PinInterface;

public class Test8BitMCP23017
{
    public static final int ADDRESS = 0x20;
    public static void main(String[] args) throws IOException, InterruptedException
    {
        I2CBus bus1 = I2CFactory.getInstance(I2CBus.BUS_1);
        MCP23017 mcp23017 = new MCP23017(bus1.getDevice(ADDRESS));

        int[] dataPins = {MCP23017.PIN1, MCP23017.PIN2, MCP23017.PIN3, MCP23017.PIN4,
                MCP23017.PIN5, MCP23017.PIN6, MCP23017.PIN7, MCP23017.PIN8};
        PinInterface pinMCP23017 = new PinMCP23017(true, false, true, MCP23017.PIN1, MCP23017.PORTB, MCP23017.PIN2, MCP23017.PORTB,dataPins, MCP23017.PORTA, mcp23017 );

        DisplayHD44780 display  = new DisplayHD44780(pinMCP23017);
        TestDisplay.test(display);
    }
}

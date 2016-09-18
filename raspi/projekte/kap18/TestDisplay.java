package raspi.projekte.kap18;

import raspi.hardware.lcd.DisplayHD44780;
/**
 * TestDisplay gibt den Testablauf vor.
 * 
 * @author Wolfgang Höfer
 * @version 1.0
 */
public class TestDisplay
{
    public static void test(DisplayHD44780 disp){

        disp.displayOn();
        disp.displayClear();
        disp.cursorBlock();
        disp.writeString("Cursor");
        disp.setCursorPos(0, 1);
        disp.writeString("Block");
        disp.waitms(2000);
        
        disp.displayClear();
        disp.cursorLine();
        disp.cursorOn();
        disp.writeString("Cursor");
        disp.setCursorPos(0, 1);
        disp.writeString("Line");
        disp.waitms(2000);
        
        disp.displayClear();
        disp.cursorOff();
        disp.writeString("Cursor");
        disp.setCursorPos(0, 1);
        disp.writeString("aus");
        disp.waitms(2000);
        
        disp.displayClear();
        disp.cursorOn();
        disp.writeString("Cursor");
        disp.setCursorPos(0, 1);
        disp.writeString("an");
        disp.waitms(2000);
        
        disp.cursorOff();
        disp.displayHome();
        disp.writeString("1. Zeile mit einem sehr langen Text!");
        disp.setCursorPos(0, 1);
        disp.writeString("2. Zeile mit einem sehr langen Text!");
        
        disp.setShiftLeft();
        for(int i = 0; i < 40; i++){
            disp.shiftDisplay();
            disp.waitms(100);
        }   
        disp.setShiftRight();
        for(int i = 0; i < 40; i++){
            disp.shiftDisplay();
            disp.waitms(100);
        }
        


    }
}

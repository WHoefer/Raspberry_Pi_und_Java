package raspi.hardware.lcd;


/**
 * Treiberklasse für LCD-Displays mit dem Controller
 * HD 44780 oder ST7066.
 * 
 * @author Wolfgang HÖfer 
 * @version 1.0
 */
public class DisplayHD44780
{

    public static final int MODE8BIT        = 0b0011_0000;
    public static final int MODE4BIT        = 0b0010_0000;
    public static final int INIT4BIT1       = 0b0011;
    public static final int INIT4BIT2       = 0b0010;
    public static final int MODEMULT        = 0b0000_1000; //Multiline Display
    public static final int MODEF510        = 0b0000_0100; //Font 5x10

    public static final int DISPLAY_CLEAR   = 0b0000_0001;
    public static final int DISPLAY_HOME    = 0b0000_0010;

    public static final int ENTRYMODE       = 0b0000_0100;
    public static final int AUTOINCREMENT   = 0b0000_0010;  
    public static final int MOVEDISPLAY     = 0b0000_0001;  

    public static final int DISPLAY         = 0b0000_1000;
    public static final int DISPLAY_ON      = 0b0000_0100;
    public static final int CURSOR_BLOCK    = 0b0000_0001;
    public static final int CURSOR_ON       = 0b0000_0010;

    public static final int SHIFT           = 0b0001_0000;
    public static final int SHIFT_DISPLAY   = 0b0000_1000;
    public static final int SHIFT_RIGHT     = 0b0000_0100;

    public static final int DDRAM_ADDRESS   = 0x80;
    private boolean eightBits = true; 
    private PinInterface pinInterface;
    private int displaycontrol; 
    private int shiftcontrol; 
    private int entrycontrol; 

    /**
     * DisplayHD44780 Konstruktor
     *
     * @param pinInterface Wird zum Ansteuern der angeschlossenen Hardware benutzt.
     */
    public DisplayHD44780(PinInterface pinInterface){
        this.pinInterface = pinInterface;
        this.pinInterface.setRS(false);
        this.pinInterface.setEnable(false);
        displaycontrol = DISPLAY | DISPLAY_ON;
        shiftcontrol = SHIFT;
        entrycontrol = ENTRYMODE | AUTOINCREMENT;
        int mode = MODE8BIT;
        if(pinInterface.is8BitMode()){
            sendCmd(MODE8BIT);
            waitms(8);
            sendCmd(MODE8BIT);
            waitms(1);
            sendCmd(MODE8BIT);
            waitms(1);
            mode = MODE8BIT;
        }else{
            pinInterface.setDataMSB(INIT4BIT1);
            waitms(8);
            pinInterface.setDataMSB(INIT4BIT1);
            waitms(1);
            pinInterface.setDataMSB(INIT4BIT1);
            waitms(1);
            pinInterface.setDataMSB(INIT4BIT2);
            waitms(1);
            mode = MODE4BIT;
        }
        if(pinInterface.is5x10Mode()){
            mode = mode | MODEF510;
        }    
        if(pinInterface.isMultilineMode()){
            mode = mode | MODEMULT;
        }    
        sendCmd(mode); 
        waitms(1);
        displayClear();        
        sendCmd(entrycontrol); 
        waitms(1);
        sendCmd(displaycontrol); 
        waitms(1);

    }

    /**
     * waitns hält den Thread für die Zeit die übergebene
     * Anzahl an Nanosekunden an.  
     *
     * @param nanos A parameter
     */
    public void waitns(int nanos){
        try{
            Thread.sleep(0, nanos);
        }catch(InterruptedException ex){
        }
    }

    /**
     * waitms hält den Thread für die Zeit die übergebene
     * Anzahl an Millisekunden an.  
     *
     * @param millis A parameter
     */
    public void waitms(int millis){
        try{
            Thread.sleep(millis);
        }catch(InterruptedException ex){
        }
    }

    /**
     * send übergibt an das Display ein 4 oder 8 Bit breites
     * Datenwort.
     *
     * @param data A parameter
     */
    private void send(int data){
        if(pinInterface.is8BitMode()){
            pinInterface.setData(data);
            waitms(1);
            pinInterface.enableCommand();
        }else{
            pinInterface.setDataMSB(data);
            waitms(1);
            pinInterface.enableCommand();
            waitms(1);
            pinInterface.setDataLSB(data);
            waitms(1);
            pinInterface.enableCommand();
            waitms(1);
        }
    }

    /**
     * sendCmd schickt einen Befehl zum Display.
     *
     * @param command A parameter
     */
    private void sendCmd(int command){
        pinInterface.setRS(false);
        waitms(2);
        send(command);
    }

    /**
     * displayHome setzt den Cursor auf Startadresse
     * 0x00. Auch wenn eine Shift-Operation durchgeführt wurde.
     *
     */
    public void displayHome(){
        sendCmd(DISPLAY_HOME);
        waitms(2);
    }

    /**
     * displayClear löscht das Display und setzt den 
     * Cursor auf Startadresse 0x00.
     *
     */
    public void displayClear(){
        sendCmd(DISPLAY_CLEAR);
        waitms(4);
    }

    /**
     * cursorBlock zeigt den Cursor als Block an,
     * der blinkt.
     *
     */
    public void cursorBlock(){
        displaycontrol = displaycontrol | CURSOR_BLOCK;
        sendCmd(displaycontrol);
        waitms(2);
    }

    /**
     * cursorLine stellt den Cursor als Linie dar,
     * die nicht blinkt.
     *
     */
    public void cursorLine(){
        displaycontrol = displaycontrol & ~CURSOR_BLOCK;
        sendCmd(displaycontrol);
        waitms(2);
    }

    /**
     * cursorOn macht den Cursor sichtbar.
     *
     */
    public void cursorOn(){
        displaycontrol = displaycontrol | CURSOR_ON;
        sendCmd(displaycontrol);
        waitms(2);
    }

    /**
     * cursorOff macht den Cursor unsichtbar.
     *
     */
    public void cursorOff(){
        displaycontrol = displaycontrol & ~CURSOR_ON;
        sendCmd(displaycontrol);
        waitms(2);
    }

    /**
     * displayOn schaltet das Display ein.
     *
     */
    public void displayOn(){
        displaycontrol = displaycontrol | DISPLAY_ON;
        sendCmd(displaycontrol);
        waitms(2);
    }

    /**
     * displayOff schaltet das Display aus. Die
     * Daten beilben erhalten.
     *
     */
    public void displayOff(){
        displaycontrol = displaycontrol & ~DISPLAY_ON;
        sendCmd(displaycontrol);
        waitms(2);
    }

    /**
     * shiftDisplay bewegt das Display in die voreingestellte
     * Richtung.
     *
     */
    public void shiftDisplay(){
        shiftcontrol = shiftcontrol | SHIFT_DISPLAY;
        sendCmd(shiftcontrol);
        waitms(1);
    }

    /**
     * shiftCorsor bewegt den Cursor in die voreingestellt
     * Richtung.
     *
     */
    public void shiftCorsor(){
        shiftcontrol = shiftcontrol & ~SHIFT_DISPLAY;
        sendCmd(shiftcontrol);
        waitms(1);
    }

    /**
     * setShiftRight setzt die Bewegungsrichtung für
     * Cursor und Display auf rechts.
     *
     */
    public void setShiftRight(){
        shiftcontrol = shiftcontrol | SHIFT_RIGHT;
    }

    /**
     * setShiftLeft setzt die Bewegungsrichtung für
     * Cursor und Display auf rechtslinks
     *
     */
    public void setShiftLeft(){
        shiftcontrol = shiftcontrol & ~SHIFT_RIGHT;
    }

    /**
     * setAutoIncrement setzt das automatische Erhöhen
     * der RAM-Adresse nach jedem Schreibvorgang.
     *
     */
    public void setAutoIncrement(){
        entrycontrol = entrycontrol | AUTOINCREMENT;
        sendCmd(entrycontrol);
        waitms(1);
    }

    /**
     * setAutoDecrement setzt das automatische Verringern
     * der RAM-Adresse nach jedem Schreibvorgang.
     *
     */
    public void setAutoDecrement(){
        entrycontrol = entrycontrol & ~AUTOINCREMENT;
        sendCmd(entrycontrol);
        waitms(1);
    }

    /**
     * setAutomoveDisplay setzt das automatische Verschieben
     * des Displays nach der Schreiboperation. Der Cursor bleibt
     * auf der gleichen Stelle.
     *
     */
    public void setAutomoveDisplay(){
        entrycontrol = entrycontrol | MOVEDISPLAY;
        sendCmd(entrycontrol);
        waitms(1);
    }

    /**
     * setAutomoveCursor setzt das automatische Verschieben
     * des Cursors nach jeder Schreiboperation.
     *
     */
    public void setAutomoveCursor(){
        entrycontrol = entrycontrol & ~MOVEDISPLAY;
        sendCmd(entrycontrol);
        waitms(1);
    }


    /**
     * setCursorPos setzt den Cursor auf die Angegebene Zeile (row)
     * und Spalte (col). Die Zähluing beginnt immer bei 0.
     *
     * @param col A parameter
     * @param row A parameter
     */
    public void setCursorPos(int col, int row){
        int[] ddramRowAdresses = {0x00,0x40,0x14,0x54};
        int startRow = DDRAM_ADDRESS | ddramRowAdresses[row];
        sendCmd(startRow + col);
    }

    /**
     * setCursorPos setzt den Cursor auf die angegeben RAM-Adresse
     *
     * @param start A parameter
     */
    public void setCursorPos(int start){
        sendCmd(start | DDRAM_ADDRESS);
    }

    /**
     * writeChar schreibt ein einzelnes Zeichen ins Display.
     *
     * @param c A parameter
     */
    public void writeChar(char c){
        pinInterface.setRS(true);
        waitms(1);
        send((int)c);
    }

    /**
     * writeString schreibt eine Zeichenkette ins Display.
     *
     * @param str A parameter
     */
    public void writeString(String str){
        char[] chars = str.toCharArray();
        for(int i = 0; i < chars.length;i++){
            writeChar(chars[i]);
        }
    }

    /**
     * shutdown beendet alle Zugriffe auf das Display.
     *
     */
    public void shutdown(){
        pinInterface.shutdown();
    }


}

package raspi.hardware.lcd;

import raspi.hardware.i2c.MCP23017;
/**
 * Write a description of class PinMCP23017 here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class PinMCP23017 implements PinInterface
{

    private int[] pins8 = {0,0,0,0,0,0,0,0};
    private int[] pins4 = {0,0,0,0};
    private int rs;
    private int enable;
    private int portData;
    private int portRS;
    private int portEnable;    
    private boolean mode8Bit;
    private boolean mode5x10;
    private boolean modeMultiline;
    private MCP23017 mcp23017;

    /**
     * PinMCP23017  Konstruktor der Klasse.<br>
     * 
     * Der Konstruktor initialisiert das Display. Die in dem Array dataPins übergeben Pins des MCP23017
     * müssen alle auf dem gleichen Port liegen. Also entweder alle auf PORTA oder PORTB.
     *
     * @param mode8Bit Mit true wird der 8Bit und mit false der 4Bit-Modus eingestellt.
     * @param mode5x10 true für Displays mit einer Zeichendarstellung von 5x10 und false für 5x7.
     * @param modeMultiline true für mehrzeilige Displays und false für einzeilige.
     * @param rsPin gibt den Pin des MCP23017 an, der mit dem RS-Eingang des Displays verbunden ist.
     * @param portRS  gibt den Port des MCP23017 an, auf dem der RS-Pin liegt.
     * @param enablePin gibt den Pin des MCP23017 an, der mit dem Enable-Eingang des Displays verbunden ist.
     * @param portEnable gibt den Port des MCP23017 an, auf dem der Enable-Pin liegt.
     * @param dataPins Ein Array, das die Pins des MCP23017 angibt, die mit den Dateneingängen D0...D3(D7) 
     * des Displays verbunden sind.
     * @param portData Port, auf den die Dateneingänge des Displays liegen.
     * @param mcp23017 Instanz der Klasse MCP23017.
     */
    public PinMCP23017(boolean mode8Bit,  boolean mode5x10, boolean modeMultiline, int rsPin, int portRS, int enablePin, int portEnable, int[] dataPins, int portData, MCP23017 mcp23017)
    {
        this.mcp23017 = mcp23017;
        this.mode8Bit = mode8Bit;
        this.mode5x10 = mode5x10;
        this.modeMultiline = modeMultiline;
        if(mode8Bit){
            for(int i = 0; i < 8; i++){
                pins8[i] =  dataPins[i];
                mcp23017.setOutPort(portData, dataPins[i]);    
            }
        }else{
            for(int i = 0; i < 4; i++){
                pins4[i]  =  dataPins[i];
                mcp23017.setOutPort(portData, dataPins[i]);    
            }
        }
        this.portData = portData;
        this.rs = rsPin;
        this.portRS = portRS; 
        mcp23017.setOutPort(portRS, rsPin);  
        this.enable = enablePin;
        this.portEnable = portEnable;
        mcp23017.setOutPort(portEnable, enablePin);  
    }

    /**
     * setDataMSB setzt die MCP23017-Ausgänge, die mit den Daten Pins 
     * D0 bis D3 des Displays verbunden sind. Aus dem Übergabeparameter
     * werden die oberen vier Bits zum Setzen der Ausgänge ausgewertet.
     * Diese Methode wird im 4Bit-Modus aufgerufen.
     *
     * @param value Ein 8 Bit langes Datenwort.
     */
    @Override
    public void setDataMSB(int value){
        int msb = (value >> 4) & 0x0F;
        int v = msb;
        for(int i = 0; i< 4 ; i++){
            mcp23017.setPin(portData, (msb & 0x01), pins4[i]);
            msb = msb >> 1;
        }
        //print4(v);
    }

    
    
    /**
     * setDataLSB setzt die MCP23017-Ausgänge, die mit den Daten Pins 
     * D0 bis D3 des Displays verbunden sind. Aus dem Übergabeparameter
     * werden die unteren vier Bits zum Setzen der Ausgänge ausgewertet.
     * Diese Methode wird im 4Bit-Modus aufgerufen.
     *
     * @param value Ein 8 Bit langes Datenwort.
     */
    @Override
    public void setDataLSB(int value){
        int lsb = value & 0x0F;
        int v = lsb;
        for(int i = 0; i< 4 ; i++){
            mcp23017.setPin(portData, (lsb & 0x01), pins4[i]);
            lsb = lsb >> 1;
        }
        //print4(v);
    }

    private String pin(int pin){
        if( mcp23017.isHigh(portData, pin)){ 
            return "1";
        }
        return "0";
    }    

    public void print4(int value){
        System.out.printf("RS = %1$s %2$s = %3$s %4$s %5$s %6$s %n" , 
            pin(rs),
            Integer.toBinaryString(value),
            pin(pins4[3]), pin(pins4[2]), pin(pins4[1]), pin(pins4[0]));
    }

    public void print(int value){
        System.out.printf("RS = %1$s %2$s = %3$s %4$s %5$s %6$s %7$s %8$s %9$s %10$s%n" , 
            pin(rs),
            Integer.toBinaryString(value),
            pin(pins8[7]), pin(pins8[6]), pin(pins8[5]), pin(pins8[4]), 
            pin(pins8[3]), pin(pins8[2]), pin(pins8[1]), pin(pins8[0]));
    }

    /**
     * setData setzt die MCP23017-Ausgänge, die mit den Daten Pins 
     * D0 bis D7 des Displays verbunden sind. Diese Methode wird 
     * im 8Bit-Modus aufgerufen.
     *
     * @param value Ein 8 Bit langes Datenwort.
     */
    @Override
    public void setData(int value){
        int v = value;
        for(int i = 0; i< 8 ; i++){
            mcp23017.setPin(portData, (value & 0x01), pins8[i]);
            value = value >> 1;
        }
        //print(v);
    }

    /**
     * enableCommand gibt einen Impuls von 50µs auf den
     * MCP23017-Pin, der mit dem Enable-Eingang des Displays 
     * verbunden ist.
     * Ablauf:
     * 1. Enable = true
     * 2. 50µs Pause
     * 3. Enable = false (Mit der fallenden Flanke werden die Daten übernommen)
     * 4. 50µ Pause
     *
     */
    @Override
    public void enableCommand(){
        try{
            mcp23017.setPin(portEnable, 1, enable);
            Thread.sleep(0, 50000);
            mcp23017.setPin(portEnable, 0, enable);
            Thread.sleep(0, 50000);
        }catch(InterruptedException ex){
        }
    }

    /**
     * setRS setzt den MCP23017-Pin, der mit dem RS-Eingang des Displays
     * verbunden ist auf den übergebenen Wert.
     *
     * @param value true setzt den Ausgang auf High-Pegel und false auf Low-Pegel.
     */
    @Override
    public void setRS(boolean value){
        if(value){
            mcp23017.setPin(portRS, 1, rs);
        }else{
            mcp23017.setPin(portRS, 0, rs);
        }
    }

    /**
     * setEnable setzt den MCP23017-Pin, der mit dem Enable-Eingang des Displays
     * verbunden ist auf den übergebenen Wert.
     *
     * @param value true setzt den Ausgang auf High-Pegel und false auf Low-Pegel.
     */
    @Override
    public void setEnable(boolean value){
        if(value){
            mcp23017.setPin(portEnable, 1, enable);
        }else{
            mcp23017.setPin(portEnable, 0, enable);
        }
    }

    /**
     * is8BitMode gibt true zurück, wenn der 8Bit-Modus eingestellt ist.
     * Im 4Bit-Modus wird  false zurückgegeben.
     *
     * @return true/false
     */
    @Override
    public boolean is8BitMode(){
        return mode8Bit;
    }    

    /**
     * is5x10Mode gibt true zurück, wenn 5x10 eingestellt ist.
     * Bei 5x7 Displays wird false zurückgegeben.
     *
     * @return true/false
     */
    @Override
    public boolean is5x10Mode(){
        return mode5x10;
    }

    /**
     * isMultilineMode gibt true zurück, wenn ein mehrzeiliges 
     * Display angeschlossen ist, sonst false.
     *
     * @return true/false
     */
    @Override
    public boolean isMultilineMode(){
        return modeMultiline;
    }    

    /**
     * shutdown führt in dieser Implementierung keine Aktion aus..
     *
     */
    @Override
    public void shutdown(){
    }

}

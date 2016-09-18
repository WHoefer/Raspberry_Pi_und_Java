package raspi.hardware.lcd;

import com.pi4j.io.gpio.*;
/**
 * PinDirect setzt die Hardware-Anbindung zum Display um.
 * 
 * @author Wolfgang Höfer
 * @version 1.0
 */
public class PinDirect implements PinInterface
{
    final GpioController gpio;
    final GpioPinDigitalOutput[] gpios8 = {null, null,null,null,null,null,null,null};
    final GpioPinDigitalOutput[] gpios4 = {null, null,null,null};
    final GpioPinDigitalOutput rs;
    final GpioPinDigitalOutput enable;

    private boolean mode8Bit;
    private boolean mode5x10;
    private boolean modeMultiline;

    /**
     * PinDirect Konstruktor der Klasse.<br>
     * 
     *
     * @param mode8Bit Mit true wird der 8Bit und mit false der 4Bit-Modus eingestellt.
     * @param mode5x10 true für Displays mit einer Zeichendarstellung von 5x10 und false für 5x7.
     * @param modeMultiline true für mehrzeilige Displays und false für einzeilige.
     * @param rsPin GPIO Pin, der mit dem RS-Pin des Displays verbunden ist.
     * @param enablePin GPIO Pin, der mit dem Enable-Pin des Displays verbunden ist.
     * @param dataPins Array mit GPIO Pins, die mit den Datenpins D0...D3(D7) verbunden sind.
     */
    public PinDirect(boolean mode8Bit,  boolean mode5x10, boolean modeMultiline, Pin rsPin, Pin enablePin, Pin[] dataPins){
        gpio = GpioFactory.getInstance();
        this.mode8Bit = mode8Bit;
        this.mode5x10 = mode5x10;
        this.modeMultiline = modeMultiline;
        if(mode8Bit){
            for(int i = 0; i < 8; i++){
                gpios8[i] =  gpio.provisionDigitalOutputPin(dataPins[i], PinState.LOW);
                gpios8[i].setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
            }
        }else{
            for(int i = 0; i < 4; i++){
                gpios4[i] =  gpio.provisionDigitalOutputPin(dataPins[i],PinState.LOW);
                gpios4[i].setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
            }
        }
        this.rs = gpio.provisionDigitalOutputPin(rsPin, PinState.LOW);
        this.rs.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
        this.enable = gpio.provisionDigitalOutputPin(enablePin, PinState.LOW);
        this.enable.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
    }

    /**
     * setDataMSB setzt die GPIO-Ausgänge, die mit den Daten Pins 
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
            gpios4[i].setState((msb & 0x01) == 0x01);
            msb = msb >> 1;
        }
        //print4(v);
    }

    /**
     * setDataLSB setzt die GPIO-Ausgänge, die mit den Daten Pins 
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
            gpios4[i].setState((lsb & 0x01) == 0x01);
            lsb = lsb >> 1;
        }
        //print4(v);
    }

    private String pin(GpioPinDigitalOutput pin){
        if( pin.isHigh()){ 
            return "1";
        }
        return "0";
    }    

    public void print4(int value){
        System.out.printf("RS = %1$s %2$s = %3$s %4$s %5$s %6$s %n" , 
            pin(rs),
            Integer.toBinaryString(value),
            pin(gpios4[3]), pin(gpios4[2]), pin(gpios4[1]), pin(gpios4[0]));
    }

    public void print(int value){
        System.out.printf("RS = %1$s %2$s = %3$s %4$s %5$s %6$s %7$s %8$s %9$s %10$s%n" , 
            pin(rs),
            Integer.toBinaryString(value),
            pin(gpios8[7]), pin(gpios8[6]), pin(gpios8[5]), pin(gpios8[4]), 
            pin(gpios8[3]), pin(gpios8[2]), pin(gpios8[1]), pin(gpios8[0]));
    }

    /**
     * setData setzt die GPIO-Ausgänge, die mit den Daten Pins 
     * D0 bis D7 des Displays verbunden sind. Diese Methode wird 
     * im 8Bit-Modus aufgerufen.
     *
     * @param value Ein 8 Bit langes Datenwort.
     */
    @Override
    public void setData(int value){
        int v = value;
        for(int i = 0; i< 8 ; i++){
            if((value & 0x01) == 0x01){
                gpios8[i].setState(true);
            } else{
                gpios8[i].setState(false);
            }
            value = value >> 1;
        }
        //print(v);
    }

    /**
     * enableCommand gibt einen Impuls von 50µs auf den
     * GPIO-Pin, der mit dem Enable-Eingang des Displays 
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
            enable.setState(true);
            Thread.sleep(0, 50000);
            enable.setState(false);
            Thread.sleep(0, 50000);
        }catch(InterruptedException ex){
        }
    }

    /**
     * setRS setzt den GPIO-Pin, der mit dem RS-Eingang des Displays
     * verbunden ist auf den übergebenen Wert.
     *
     * @param value true setzt den Ausgang auf High-Pegel und false auf Low-Pegel.
     */
    @Override
    public void setRS(boolean value){
        rs.setState(value);
    }

    /**
     * setEnable setzt den GPIO-Pin, der mit dem Enable-Eingang des Displays
     * verbunden ist auf den übergebenen Wert.
     *
     * @param value true setzt den Ausgang auf High-Pegel und false auf Low-Pegel.
     */
    @Override
    public void setEnable(boolean value){
        enable.setState(value);
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
     * shutdown setzt alle Ausgänge in den definierten Ruhezustand.
     *
     */
    @Override
    public void shutdown(){
        gpio.shutdown();
    }

}

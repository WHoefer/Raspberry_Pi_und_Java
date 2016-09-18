package raspi.hardware.i2c;

import  com.pi4j.io.i2c.*;
import  java.io.IOException;
/**
 * Write a description of class MCP23017 here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class MCP23017 extends I2C
{
    public static final int IODIRA   = 0x00; //RW-1 Port A: Richtung 1 = Einagng 0 = Ausgang
    public static final int IODIRB   = 0x10; //RW-1 Port B: Richtung 1 = Einagng 0 = Ausgang
    public static final int IPOLA    = 0x01; //RW-0 Port A: Polarität 1 = invertiert
    public static final int IPOLB    = 0x11; //RW-0 Port B: Polarität 1 = invertiert
    public static final int GPINTENA = 0x02; //RW-0 Port A: Interruptfreigabe 1 = Int-On-Change
    public static final int GPINTENB = 0x12; //RW-0 Port B: Interruptfreigabe 1 = Int-On-Change
    public static final int DEFVALA  = 0x03; //RW-0 Port A: Standardwert für Pin. Wenn Interrupt freigegben wird Int bei Pegeländerung gegenüber DEFVAL ausgelöst
    public static final int DEFVALB  = 0x13; //RW-0 Port B: Standardwert für Pin. Wenn Interrupt freigegben wird Int bei Pegeländerung gegenüber DEFVAL ausgelöst
    public static final int INTCONA  = 0x04; //RW-0 Port A: 1 = vergleich mit DEFVAL 0 = Int-On-Change
    public static final int INTCONB  = 0x14; //RW-0 Port B: 1 = vergleich mit DEFVAL 0 = Int-On-Change
    public static final int IOCON    = 0x05; //RW-0 BANK MIRROR SEQOP DISSLW HAEN ODR INTPOL --- 
    public static final int GPPUA    = 0x06; //RW-0 Port A: 1 = 100K PullUpp-Wiederstand
    public static final int GPPUB    = 0x16; //RW-0 Port B: 1 = 100K PullUpp-Wiederstand
    public static final int INTFA    = 0x07; //R -0 Zeigt den Pin an, der den Interrupt ausgelöst hat.
    public static final int INTFB    = 0x17; //R -0 Zeigt den Pin an, der den Interrupt ausgelöst hat.
    public static final int INTCAPA  = 0x08; //R -x Speichert die Portzustände während des Interrupts.
    public static final int INTCAPB  = 0x18; //R -x Speichert die Portzustände während des Interrupts.
    public static final int GPIOA    = 0x09; //RW-0 Eingänge
    public static final int GPIOB    = 0x19; //RW-0 Eingänge
    public static final int OLATA    = 0x0A; //RW-0 Ausgänge
    public static final int OLATB    = 0x1A; //RW-0 Ausgänge

    public static final int PIN1     = 0b00000001;
    public static final int PIN2     = 0b00000010;
    public static final int PIN3     = 0b00000100;
    public static final int PIN4     = 0b00001000;
    public static final int PIN5     = 0b00010000;
    public static final int PIN6     = 0b00100000;
    public static final int PIN7     = 0b01000000;
    public static final int PIN8     = 0b10000000;
    public static final int PORTA    = 1;
    public static final int PORTB    = 2;
    public static final int PULLUPON  = 1;
    public static final int PULLUPOFF = 0;
    public static final int POLARITYINVERT = 1;
    public static final int POLARITYNORMAL = 0;

    
    public MCP23017(final I2CDevice dev) throws IOException
    {
        super(dev);
        //IOCON.BANK = 1
        dev.write(0x05, (byte)0b10000000);
        dev.write(0x0A, (byte)0b10000000);
        //OLATA = 0
        dev.write(0x0A, (byte)0b00000000);
        dev.write(IODIRA, (byte)0b11111111);
        dev.write(IODIRB, (byte)0b11111111);

    }

    public int setOutPort(int port, int pin){
        if(port == PORTA){
            if(configPin(IODIRA, 0, pin) == 1 &&
            configPin(IPOLA, 0, pin) == 1){
                return 1;
            }else{
                return -1;
            }    
        }else if(port == PORTB){
            if(configPin(IODIRB, 0, pin) == 1 &&
            configPin(IPOLB, 0, pin) == 1){
                return 1;
            }else{
                return -1;
            }
        }
        return -1;
    }

    public int setInPort(int port, int pin, int pullup){
        if(port == PORTA){
            if(configPin(IODIRA, 1, pin) == 1 &&
            configPin(GPPUA, pullup, pin) == 1){
                return 1;
            }else{
                return -1;
            }
        }else if(port == PORTB){
            if(configPin(IODIRB, 1, pin)  == 1 &&
            configPin(GPPUB, pullup, pin) == 1){
                return 1;
            }else{
                return -1;
            }
        }
        return -1;
    }

    public int setInPortOnChangeInt(int port, int pin, int pullup){
        if(port == PORTA){
            if(configPin(IODIRA, 1, pin) == 1 &&
            configPin(GPPUA, pullup, pin)== 1 &&
            configPin(GPINTENA, 1, pin)== 1 &&
            configPin(INTCONA, 0, pin)== 1){
                return 1;
            }else{
                return -1;
            }
        }else if(port == PORTB){
            if(configPin(IODIRB, 1, pin)== 1 &&
            configPin(GPPUB, pullup, pin)== 1 &&
            configPin(GPINTENB, 1, pin)== 1 &&   
            configPin(INTCONB, 0, pin)== 1){
                return 1;
            }else{
                return -1;
            }
        }
        return -1;
    }

    public int setInPortDefValInt(int port, int pin, int pullup, int defval){
        if(port == PORTA){
            if(configPin(IODIRA, 1, pin) == 1 &&
            configPin(GPPUA, pullup, pin)== 1 &&
            configPin(GPINTENA, 1, pin)== 1 &&
            configPin(INTCONA, 1, pin)== 1 &&
            configPin(DEFVALA,defval, pin)== 1 ){
                return 1;
            }else{
                return -1;
            }
        }else if(port == PORTB){
            if(configPin(IODIRB, 1, pin)== 1 &&
            configPin(GPPUB, pullup, pin)== 1 &&
            configPin(GPINTENB, 1, pin)== 1 &&   
            configPin(INTCONB, 1, pin)== 1 &&
            configPin(DEFVALB,defval, pin)== 1 ){
                return 1;
            }else{
                return -1;
            }
        }
        return -1;
    }

    public int setInputPolarity(int port, int pin, int polarity){
        if(port == PORTA){
            return configPin(IPOLA, polarity, pin); 
        }else if(port == PORTB){
            return configPin(IPOLB, polarity, pin);
        }
        return -1;
    }

    public int getInPort(int port, int pin){
        if(port == PORTA){
            return readPin(GPIOA, pin);
        }else if(port == PORTB){
            return readPin(GPIOB, pin);
        }
        return -1;
    }

    public boolean isPinHigh(int port, int pin){
        if(port == PORTA){
            return isHigh(GPIOA, pin);
        }else if(port == PORTB){
            return isHigh(GPIOB, pin);
        }
        return false;
    }

    public boolean isPinLow(int port, int pin){
        if(port == PORTA){
            return isLow(GPIOA, pin);
        }else if(port == PORTB){
            return isLow(GPIOB, pin);
        }
        return false;
    }

    public int setPinHigh(int port, int pin){
        if(port == PORTA){
            return configPin(GPIOA,OLATA, 1, pin);
        }else if(port == PORTB){
            return configPin(GPIOB,OLATB, 1, pin);
        }
        return -1;
    }

    public int setPinLow(int port, int pin){
        if(port == PORTA){
            return configPin(GPIOA, OLATA, 0, pin);
        }else if(port == PORTB){
            return configPin(GPIOB, OLATB, 0, pin);
        }
        return -1;
    }

    public int setPin(int port, int value, int pin){
        if(port == PORTA){
            return configPin(GPIOA, OLATA, value, pin);
        }else if(port == PORTB){
            return configPin(GPIOB, OLATB, value, pin);
        }
        return -1;
    }

    
    public int togglePin(int port, int pin){
        if(port == PORTA){
            return configPinToggle(GPIOA, OLATA, pin);
        }else if(port == PORTB){
            return configPinToggle(GPIOB, OLATB, pin);
        }
        return -1;
    }

    public int getInterruptFlags(int port){
        if(port == PORTA){
            return read(INTFA);
        }else if(port == PORTB){
            return read(INTFB);
        }
        return -1;
    }

    public int getCaptureFlags(int port){
        if(port == PORTA){
            return read(INTCAPA);
        }else if(port == PORTB){
            return read(INTCAPB);
        }
        return -1;
    }    

    /**
     * setMirrorIntAB
     * mit val  = 1 werden beide Interruptausgänge für Port A und Port B gespiegelt.
     * mit val  = 0 bezieht sich INTA auf Port A und INTB auf Port B
     * @param mirr A parameter
     * @return The return value
     */
    public int setMirrorForIntAB(int val){
        if(val == 1){
            return configPin(IOCON, 1, 7);
        }else if(val == 0){
            return configPin(IOCON, 0, 7);
        }
        return -1;
    }    

    /**
     * Method setOpenDrainForIntAB
     * mit val = 1 werden die Interruptausgänge INTA/INTB  als Open Drain Ausgänge geschaltet.
     * mit val = 0 werden die Interruptausgänge INTA/INTB  als aktive Ausgänge mit Treiber geschaltet.
     * @param val A parameter
     * @return The return value
     */
    public int setOpenDrainForIntAB(int val){
        if(val == 1){
            return configPin(IOCON, 1, 3);
        }else if(val == 0){
            return configPin(IOCON, 0, 3);
        }
        return -1;
    }    

    /**
     * Method setIntpolForIntAB
     * mit val = 1 werden die Interruptausgänge INTA/INTB auf Active-high geschaltet.
     * mit val = 0 werden die Interruptausgänge INTA/INTB auf Active-low geschaltet.
     *
     * @param val A parameter
     * @return The return value
     */
    public int setIntpolForIntAB(int val){
        if(val == 1){
            return configPin(IOCON, 1, 3);
        }else if(val == 0){
            return configPin(IOCON, 0, 3);
        }
        return -1;
    }    

    public void print(){
        System.out.printf("IODIRA   %1$8.8s Richtung:  1 = Einagng / 0 = Ausgang %n",Integer.toBinaryString(read(IODIRA)));
        System.out.printf("IODIRB   %1$8.8s Richtung:  1 = Einagng / 0 = Ausgang %n",Integer.toBinaryString(read(IODIRB)));
        System.out.printf("IPOLA    %1$8.8s Polarität: 1 = invertiert / 0 = normal %n",Integer.toBinaryString(read(IPOLA)));
        System.out.printf("IPOLB    %1$8.8s Polarität: 1 = invertiert / 0 = normal %n",Integer.toBinaryString(read(IPOLB)));
        System.out.printf("GPINTENA %1$8.8s Int-On-Change: 1 = ein / 0 = aus %n",Integer.toBinaryString(read(GPINTENA)));
        System.out.printf("GPINTENB %1$8.8s Int-On-Change: 1 = ein / 0 = aus %n",Integer.toBinaryString(read(GPINTENB)));
        System.out.printf("DEFVALA  %1$8.8s Standardwert:  1/0. Interrupt wird bei Pegeländerung gegenüber DEFVAL ausgelöst %n",Integer.toBinaryString(read(DEFVALA)));
        System.out.printf("DEFVALB  %1$8.8s Standardwert:  1/0. Interrupt wird bei Pegeländerung gegenüber DEFVAL ausgelöst %n",Integer.toBinaryString(read(DEFVALB)));
        System.out.printf("INTCONA  %1$8.8s Int Typ: 1 = vergleich mit DEFVAL 0 = Int-On-Change %n",Integer.toBinaryString(read(INTCONA)));
        System.out.printf("INTCONB  %1$8.8s Int Typ: 1 = vergleich mit DEFVAL 0 = Int-On-Change %n",Integer.toBinaryString(read(INTCONB)));
        System.out.printf("IOCON    %1$8.8s BANK MIRROR SEQOP DISSLW HAEN ODR INTPOL ---  %n",Integer.toBinaryString(read(IOCON)));
        System.out.printf("GPPUA    %1$8.8s PullUpp-Wiederstand: 1 = 100K / 0 = abgeschaltet %n",Integer.toBinaryString(read(GPPUA)));
        System.out.printf("GPPUB    %1$8.8s PullUpp-Wiederstand: 1 = 100K / 0 = abgeschaltet %n",Integer.toBinaryString(read(GPPUB)));
        System.out.printf("INTFA    %1$8.8s Interrupt ausgelöst: 1 =  ausgelöst / 0 = kein Interrupt %n",Integer.toBinaryString(read(INTFA)));
        System.out.printf("INTFB    %1$8.8s Interrupt ausgelöst: 1 =  ausgelöst / 0 = kein Interrupt %n",Integer.toBinaryString(read(INTFB)));
        System.out.printf("INTCAPA  %1$8.8s Interruptspeicher der Ports: 1 = ausgelöst / 0 = kein Interrupt %n",Integer.toBinaryString(read(INTCAPA)));
        System.out.printf("INTCAPB  %1$8.8s Interruptspeicher der Ports: 1 = ausgelöst / 0 = kein Interrupt %n",Integer.toBinaryString(read(INTCAPB)));
        System.out.printf("GPIOA    %1$8.8s Eingänge %n",Integer.toBinaryString(read(GPIOA)));
        System.out.printf("GPIOB    %1$8.8s Eingänge %n",Integer.toBinaryString(read(GPIOB)));
        System.out.printf("OLATA    %1$8.8s Ausgänge: 1 = high / 0 = low  %n",Integer.toBinaryString(read(OLATA)));
        System.out.printf("OLATB    %1$8.8s Ausgänge: 1 = high / 0 = low %n",Integer.toBinaryString(read(OLATB)));
    }

}

package raspi.projekte.kap16;

import  com.pi4j.io.i2c.*;
import  java.io.IOException;
import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.*;
import raspi.hardware.i2c.MCP23017;
/**
 * Write a description of class TestI2C here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class MCP23017Test  
{

    public static final int ADDRESS = 0x20;

    public MCP23017Test() {
    }    



    public static class InterruptListener implements GpioPinListenerDigital {
        MCP23017 testI2C;

        public InterruptListener(final MCP23017 testI2C){
            this.testI2C = testI2C;
        }

        @Override
        public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event){
            int flagsC = testI2C.getCaptureFlags(MCP23017.PORTB);
            if(!testI2C.isBit(flagsC, MCP23017.PIN1)){
                System.out.println("Interrupt auf PortB  Pin 1 Capture:" + Integer.toBinaryString(flagsC));
            } 
            if(!testI2C.isBit(flagsC, MCP23017.PIN2)){
                System.out.println("Interrupt auf PortB  Pin 2 Capture:" + Integer.toBinaryString(flagsC));
            } 
            if(!testI2C.isBit(flagsC, MCP23017.PIN3)){
                System.out.println("Interrupt auf PortB  Pin 3 Capture:" + Integer.toBinaryString(flagsC));
            } 
        }
    }    

    public static void main(String[] args) throws IOException, InterruptedException{
        I2CBus bus1 = I2CFactory.getInstance(I2CBus.BUS_1);
        MCP23017 testI2C = new MCP23017(bus1.getDevice(ADDRESS));

        testI2C.setOutPort(MCP23017.PORTA, MCP23017.PIN1);
        testI2C.setOutPort(MCP23017.PORTA, MCP23017.PIN2);
        testI2C.setOutPort(MCP23017.PORTA, MCP23017.PIN3);
        testI2C.setOutPort(MCP23017.PORTA, MCP23017.PIN4);
        testI2C.setOutPort(MCP23017.PORTA, MCP23017.PIN5);
        testI2C.setOutPort(MCP23017.PORTA, MCP23017.PIN6);
        testI2C.setOutPort(MCP23017.PORTA, MCP23017.PIN7);
        testI2C.setOutPort(MCP23017.PORTA, MCP23017.PIN8);
        testI2C.setInPortOnChangeInt(MCP23017.PORTB, MCP23017.PIN1, MCP23017.PULLUPON);
        testI2C.setInPortOnChangeInt(MCP23017.PORTB, MCP23017.PIN2, MCP23017.PULLUPON);
        testI2C.setInPortOnChangeInt(MCP23017.PORTB, MCP23017.PIN3, MCP23017.PULLUPON);
        testI2C.setInPort(MCP23017.PORTB, MCP23017.PIN4, MCP23017.PULLUPON);
        testI2C.setInPort(MCP23017.PORTB, MCP23017.PIN5, MCP23017.PULLUPON);
        testI2C.setInPort(MCP23017.PORTB, MCP23017.PIN6, MCP23017.PULLUPON);
        testI2C.setInPort(MCP23017.PORTB, MCP23017.PIN7, MCP23017.PULLUPON);
        testI2C.setInPort(MCP23017.PORTB, MCP23017.PIN8, MCP23017.PULLUPON);
        testI2C.print();

        final GpioController gpio = GpioFactory.getInstance();
        final GpioPinDigitalInput interruptPort  = gpio.provisionDigitalInputPin(RaspiPin.GPIO_01,"Interrupt_GPIO",PinPullResistance.PULL_UP);

        InterruptListener interruptListener = new InterruptListener(testI2C);
        interruptPort.addListener(interruptListener);

        testI2C.setPinLow(MCP23017.PORTA, MCP23017.PIN1);
        testI2C.setPinLow(MCP23017.PORTA, MCP23017.PIN2);
        testI2C.setPinLow(MCP23017.PORTA, MCP23017.PIN3);
        testI2C.setPinLow(MCP23017.PORTA, MCP23017.PIN4);
        testI2C.setPinLow(MCP23017.PORTA, MCP23017.PIN5);
        testI2C.setPinLow(MCP23017.PORTA, MCP23017.PIN6);
        testI2C.setPinLow(MCP23017.PORTA, MCP23017.PIN7);
        testI2C.setPinLow(MCP23017.PORTA, MCP23017.PIN8);
        for (int count = 0; count < 40; count++) {
            testI2C.togglePin(MCP23017.PORTA, MCP23017.PIN1);
            Thread.sleep(50);
            testI2C.togglePin(MCP23017.PORTA, MCP23017.PIN2);
            Thread.sleep(50);
            testI2C.togglePin(MCP23017.PORTA, MCP23017.PIN3);
            Thread.sleep(50);
            testI2C.togglePin(MCP23017.PORTA, MCP23017.PIN4);
            Thread.sleep(50);
            testI2C.togglePin(MCP23017.PORTA, MCP23017.PIN5);
            Thread.sleep(50);
            testI2C.togglePin(MCP23017.PORTA, MCP23017.PIN6);
            Thread.sleep(50);
            testI2C.togglePin(MCP23017.PORTA, MCP23017.PIN7);
            Thread.sleep(50);
            testI2C.togglePin(MCP23017.PORTA, MCP23017.PIN8);
            Thread.sleep(50);
        }
    }
}


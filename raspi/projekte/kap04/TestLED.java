package raspi.projekte.kap04;


import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.GpioPin;

public class TestLED {

    public static void main(String[] args) throws InterruptedException {

        System.out.println("Alle LEDs auf dem Gertboard blinken nacheinander 1sec lang.");

        // create gpio controller
        final GpioController gpio = GpioFactory.getInstance();

        // provision gpio pin #01 & #03 as an output pins and blink
        final GpioPinDigitalOutput led1 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01);
        led1.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
        final GpioPinDigitalOutput led2 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02);
        led2.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
        final GpioPinDigitalOutput led3 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03);
        led3.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
        final GpioPinDigitalOutput led4 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04);
        led4.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
        final GpioPinDigitalOutput led5 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_05);
        led5.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
        final GpioPinDigitalOutput led6 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_06);
        led6.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
        final GpioPinDigitalOutput led7 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_07);
        led7.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
        final GpioPinDigitalOutput led8 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_08);
        led8.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
        final GpioPinDigitalOutput led9 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_09);
        led9.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
        final GpioPinDigitalOutput led10 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_10);
        led10.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
        final GpioPinDigitalOutput led11 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_11);
        led11.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
        final GpioPinDigitalOutput led12 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_12);
        led12.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);

        // Jede LED blinkt 1000ms im 100ms Takt 
        led1.blink(100, 1000);
        Thread.sleep(1000);
        led2.blink(100, 1000);
        Thread.sleep(1000);
        led3.blink(100, 1000);
        Thread.sleep(1000);
        led4.blink(100, 1000);
        Thread.sleep(1000);
        led5.blink(100, 1000);
        Thread.sleep(1000);
        led6.blink(100, 1000);
        Thread.sleep(1000);
        led7.blink(100, 1000);
        Thread.sleep(1000);
        led8.blink(100, 1000);
        Thread.sleep(1000);
        led9.blink(100, 1000);
        Thread.sleep(1000);
        led10.blink(100, 1000);
        Thread.sleep(1000);
        led11.blink(100, 1000);
        Thread.sleep(1000);
        led12.blink(100, 1000);
        Thread.sleep(1000);
        // shtudown stoppt alle GPIO-Threads
        gpio.shutdown();  
        System.out.println("Shutdown: Alle benutzten Ausgänge des Raspberry auf LOW-Pegel.");
    }
}


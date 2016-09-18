package raspi.projekte.kap17;


import raspi.hardware.rs232.SRF02;

public class Entfernungsmessung
{
    private static long distance1 = 0;
    private static long distance2 = 0;
    private static long distance3 = 0;
    private static long wait = 10;
    
    public static void main(String[] args) throws InterruptedException{
        SRF02 srf02 = new SRF02();
        System.out.printf("Firmware-Version %1$s %n", srf02.getFirmware(0x00));
        Thread.sleep(wait);
        distance1 = srf02.getDistanceAuto(0x00, SRF02.START_AUTO_MEASURE_CM);
        Thread.sleep(wait);
        System.out.println("Messung im Modus: AUTO");
        for(int i =  0; i < 5; i++){
            distance1 = srf02.getDistanceAuto(0x00, SRF02.START_AUTO_MEASURE_CM);
            Thread.sleep(wait);
            distance2 = srf02.getDistanceAuto(0x00, SRF02.START_AUTO_MEASURE_INCHES);
            Thread.sleep(wait);
            distance3 = srf02.getDistanceAuto(0x00, SRF02.START_AUTO_MEASURE_MSECOND);
            System.out.printf("%4$d) %1$d cm %2$d Zoll %3$d µs %n", distance1, distance2, distance3, i);
            Thread.sleep(wait);
        }
        System.out.println("Messung im Modus: Manuell");
        for(int i =  0; i < 5; i++){
            distance1 = srf02.getDistanceManually(0x00, SRF02.START_MEASURE_CM);
            Thread.sleep(wait);
            distance2 = srf02.getDistanceManually(0x00, SRF02.START_MEASURE_INCHES);
            Thread.sleep(wait);
            distance3 = srf02.getDistanceManually(0x00, SRF02.START_MEASURE_MSECOND);
            System.out.printf("%4$d) %1$d cm %2$d Zoll %3$d µs %n", distance1, distance2, distance3, i);
            Thread.sleep(wait);
        }
        srf02.close();
        srf02.shutdown();
    }
}

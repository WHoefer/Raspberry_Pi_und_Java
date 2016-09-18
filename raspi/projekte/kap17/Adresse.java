package raspi.projekte.kap17;


import raspi.hardware.rs232.SRF02;

public class Adresse
{
    private static long distance1 = 0;
    private static long wait = 10;
    
    public static void main(String[] args) throws InterruptedException{
        SRF02 srf02 = new SRF02();
        System.out.printf("Firmware-Version %1$s %n", srf02.getFirmware(0x00));
        Thread.sleep(wait);
        System.out.println("Sensoradresse auf 0x01 ändern!");
        srf02.setAddress(0x00, 0x01);
        Thread.sleep(wait);
        System.out.println("Sensor wird mit Adresse 0x01 angesprochen!");
        for(int i =  0; i < 5; i++){
            distance1 = srf02.getDistanceAuto(0x01, SRF02.START_AUTO_MEASURE_CM);
            Thread.sleep(wait);
            System.out.printf("%2$d) %1$d cm %n", distance1, i);
            Thread.sleep(wait);
        }
        Thread.sleep(wait);
        System.out.println("Sensoradresse auf 0x00 ändern!");
        srf02.setAddress(0x01, 0x00);
        Thread.sleep(wait);
        System.out.println("Sensor wird mit Adresse 0x00 angesprochen!");
        for(int i =  0; i < 5; i++){
            distance1 = srf02.getDistanceAuto(0x00, SRF02.START_AUTO_MEASURE_CM);
            Thread.sleep(wait);
            System.out.printf("%2$d) %1$d cm %n", distance1, i);
            Thread.sleep(wait);
        }
        srf02.close();
        srf02.shutdown();
    }
}

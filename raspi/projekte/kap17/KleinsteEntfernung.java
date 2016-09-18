package raspi.projekte.kap17;

import raspi.hardware.rs232.SRF02;

public class KleinsteEntfernung
{
    private static long wait = 10;

    public static void main(String[] args) throws InterruptedException{
        SRF02 srf02 = new SRF02();
        System.out.printf("Firmware-Version %1$s %n", srf02.getFirmware(0x00));
        Thread.sleep(wait);
        srf02.getDistanceAutoCM(0x00);
        Thread.sleep(wait);
        for(int i =  0; i < 3; i++){
            Thread.sleep(wait);
            System.out.printf("%2$d) Kleinste zu messende Distanz %1$s cm %n", srf02.getMinDistance(0x00),i);
        }
        Thread.sleep(wait);
        srf02.getDistanceAutoInches(0x00);
        Thread.sleep(wait);
        for(int i =  0; i < 3; i++){
            Thread.sleep(300);
            System.out.printf("%2$d) Kleinste zu messende Distanz %1$s Zoll %n", srf02.getMinDistance(0x00),i);
        }
        Thread.sleep(wait);
        srf02.getDistanceAutoMSecond(0x00);
        Thread.sleep(wait);
        for(int i =  0; i < 3; i++){
            Thread.sleep(wait);
            System.out.printf("%2$d) Kleinste zu messende Distanz %1$s µs %n", srf02.getMinDistance(0x00),i);
        }
        srf02.close();
        srf02.shutdown();
    }
}

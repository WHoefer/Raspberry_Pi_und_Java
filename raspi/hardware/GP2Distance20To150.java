package raspi.hardware;

/**
 * Distanzsensor GP2YA02YK0F der Firma SHARP
 * Der Messebereich liegt zwischen 20 cm und 150 cm.
 * Die gültigen Spannungswerte liegen zwischen Längen zu den entsprechenden Spannungswerten berechnet. Werte, die zwischen
 * zwei Stützwerten liegen, werden linear interpoliert.
 * 
 * @author Wolfgang Höfer
 * @version 1.0
 */
public class GP2Distance20To150 extends SensorTables
{
    public GP2Distance20To150(){
        interpolationValues = new double[15][2];
        //Stützwertetabelle
        interpolationValues[0][1] = 2.74d; // Spannung
        interpolationValues[0][0] = 15d;   // Länge
        interpolationValues[1][1] = 2.53d;
        interpolationValues[1][0] = 20d;
        interpolationValues[2][1] = 1.99d;
        interpolationValues[2][0] = 30d;
        interpolationValues[3][1] = 1.54d;
        interpolationValues[3][0] = 40d;
        interpolationValues[4][1] = 1.24d;
        interpolationValues[4][0] = 50d;
        interpolationValues[5][1] = 1.04d;
        interpolationValues[5][0] = 60d;
        interpolationValues[6][1] = 0.905d;
        interpolationValues[6][0] = 70d;
        interpolationValues[7][1] = 0.811d;
        interpolationValues[7][0] = 80d;
        interpolationValues[8][1] = 0.716d;
        interpolationValues[8][0] = 90d;
        interpolationValues[9][1] = 0.663d;
        interpolationValues[9][0] = 100d;
        interpolationValues[10][1] = 0.6d;
        interpolationValues[10][0] = 110d;
        interpolationValues[11][1] = 0.558d;
        interpolationValues[11][0] = 120d;
        interpolationValues[12][1] = 0.505d;
        interpolationValues[12][0] = 130d;
        interpolationValues[13][1] = 0.463d;
        interpolationValues[13][0] = 140d;
        interpolationValues[14][1] = 0.442d;
        interpolationValues[14][0] = 150d;
    }


    public static void main(String[] args){
        /* for(int i = 0; i < interpolationValues.length; i++){
        System.out.printf("Length = %1$f   Voltage = %2$f  %n", interpolationValues[i][0],interpolationValues[i][1]);
        }*/
        GP2Distance20To150 sensor = new GP2Distance20To150();
        System.out.println (sensor.getValue(3d));
        System.out.println (sensor.getValue(2.7399d));
        System.out.println (sensor.getValue(2.73d));
        System.out.println (sensor.getValue(1d));
        System.out.println (sensor.getValue(1.04d));
        System.out.println (sensor.getValue(0.559d));
        System.out.println (sensor.getValue(0.558d));
        System.out.println (sensor.getValue(0.557d));
        System.out.println (sensor.getValue(0.442d));
        System.out.println (sensor.getValue(0.441d));
        GP2Distance10To80 s1 = new GP2Distance10To80();
        System.out.println (s1.getValue(3.2d));
        System.out.println (s1.getValue(3.15d));
        System.out.println (s1.getValue(3d));
        System.out.println (s1.getValue(2d));
        System.out.println (s1.getValue(1d));
        System.out.println (s1.getValue(0.5d));
        System.out.println (s1.getValue(0.4d));
        System.out.println (s1.getValue(0.39d));
    }
}

package raspi.hardware;


/**
 * Stützwertetabelle.
 * 
 * @author Wolfgang Höfer
 * @version 1.0
 */
public class GP2Distance10To80 extends SensorTables
{
   public GP2Distance10To80(){
        interpolationValues = new double[9][2];
        //Stützwertetabelle
        interpolationValues[0][1] = 3.15d; // Spannung
        interpolationValues[0][0] = 5d;   // Länge
        interpolationValues[1][1] = 2.31d;
        interpolationValues[1][0] = 10d;
        interpolationValues[2][1] = 1.33d;
        interpolationValues[2][0] = 20d;
        interpolationValues[3][1] = 0.92d;
        interpolationValues[3][0] = 30d;
        interpolationValues[4][1] = 0.72d;
        interpolationValues[4][0] = 40d;
        interpolationValues[5][1] = 0.58d;
        interpolationValues[5][0] = 50d;
        interpolationValues[6][1] = 0.5d;
        interpolationValues[6][0] = 60d;
        interpolationValues[7][1] = 0.43d;
        interpolationValues[7][0] = 70d;
        interpolationValues[8][1] = 0.4d;
        interpolationValues[8][0] = 80d;
    }
}

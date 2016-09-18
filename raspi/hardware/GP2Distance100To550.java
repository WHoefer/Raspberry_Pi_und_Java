package raspi.hardware;

/**
 * Stützwertetabelle.
 * 
 * @author Wolfgang Höfer
 * @version 1.0
 */
public class GP2Distance100To550 extends SensorTables
{
    // instance variables - replace the example below with your own
    public GP2Distance100To550(){
        interpolationValues = new double[11][2];
        //Stützwertetabelle
        interpolationValues[0][1] = 3.09d; // Spannung
        interpolationValues[0][0] = 50d;   // Länge
        interpolationValues[1][1] = 2.51d;
        interpolationValues[1][0] = 100d;
        interpolationValues[2][1] = 2.06d;
        interpolationValues[2][0] = 150d;
        interpolationValues[3][1] = 1.81d;
        interpolationValues[3][0] = 200d;
        interpolationValues[4][1] = 1.65d;
        interpolationValues[4][0] = 250d;
        interpolationValues[5][1] = 1.56d;
        interpolationValues[5][0] = 300d;
        interpolationValues[6][1] = 1.50d;
        interpolationValues[6][0] = 350d;
        interpolationValues[7][1] = 1.45d;
        interpolationValues[7][0] = 400d;
        interpolationValues[8][1] = 1.41d;
        interpolationValues[8][0] = 450d;
        interpolationValues[9][1] = 1.40d;
        interpolationValues[9][0] = 500d;
        interpolationValues[10][1] = 1.37d;
        interpolationValues[10][0] = 550d;
    }
}

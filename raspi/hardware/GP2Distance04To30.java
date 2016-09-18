package raspi.hardware;


/**
 * Stützwertetabelle.
 * 
 * @author Wolfgang Höfer
 * @version 1.0
 */
public class GP2Distance04To30 extends SensorTables
{
   public GP2Distance04To30(){
        interpolationValues = new double[17][2];
        //Stützwertetabelle
        interpolationValues[0][1] = 3.04d; // Spannung
        interpolationValues[0][0] = 3d;   // Länge
        interpolationValues[1][1] = 2.72d;
        interpolationValues[1][0] = 4d;
        interpolationValues[2][1] = 2.34d;
        interpolationValues[2][0] = 5d;
        interpolationValues[3][1] = 2.02d;
        interpolationValues[3][0] = 6d;
        interpolationValues[4][1] = 1.76d;
        interpolationValues[4][0] = 7d;
        interpolationValues[5][1] = 1.56d;
        interpolationValues[5][0] = 8d;
        interpolationValues[6][1] = 1.4d;
        interpolationValues[6][0] = 9d;
        interpolationValues[7][1] = 1.27d;
        interpolationValues[7][0] = 10d;
        interpolationValues[8][1] = 1.07d;
        interpolationValues[8][0] = 12d;
        interpolationValues[9][1] = 0.926d;
        interpolationValues[9][0] = 14d;
        interpolationValues[10][1] = 0.811d;
        interpolationValues[10][0] = 16d;
        interpolationValues[11][1] = 0.732d;
        interpolationValues[11][0] = 18d;
        interpolationValues[12][1] = 0.661d;
        interpolationValues[12][0] = 20d;
        interpolationValues[13][1] = 0.529d;
        interpolationValues[13][0] = 25d;
        interpolationValues[14][1] = 0.511d;
        interpolationValues[14][0] = 30d;
        interpolationValues[15][1] = 0.37d;
        interpolationValues[15][0] = 35d;
        interpolationValues[16][1] = 0.317d;
        interpolationValues[16][0] = 40d;
    }}

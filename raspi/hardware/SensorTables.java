package raspi.hardware;

/**
 * SensorTables<br>
 * 
 * Unterstützt die Verarbeitung von Stützwertetabellen und führt 
 * eine lineare Interpolation zwischen zwei Stützwerten durch.
 * 
 * @author Wolfgang Höfer
 * @version 1.0
 */
public class SensorTables
{
    public SensorTables(){
    }

    public double[][] interpolationValues; // =  {{ 0d,  0d}};

    /**
     * Method getInterpolation<br>
     * Lineare interpolation zwischen zwei Stützwerten.
     *
     * @param i Index aus der Stützwertetabelle
     * @param node Stützstelle
     * @return Interpolierter Wert.
     */
    private double getInterpolation(int i, double node){
        if(i == 0){
            return 0d;
        }
        double d1 = interpolationValues[i-1][0];
        double d2 = interpolationValues[i][0];
        double u1 = interpolationValues[i-1][1];
        double u2 = interpolationValues[i][1];
        double m = (d2-d1)/(u2 -u1);
        return node * m + d1 - (u1 * m); 
    }

    /**
     * Method getValue<br>
     * Gibt zum übergebenen Wert die Zuordnung aus der Interpolatioonstabelle zurück.
     * Werte zwischen zwei Stüzwerten, werden linear interpoliert.
     * Die übergebenen Werte müssen >= dem letzen Stützwert aus interpolationValues 
     * sein und < erstem Stützwert aus interpolationValues sein. Liegt der 
     * übergebene Wert nicht in diesem Intervall, so wird immer der Wert
     * 0 zurückgegeben.
     *
     * @param pnode Stützstelle
     * @return Länge in cm
     */
    public double getValue(double pnode){
        double interpolationValue = 0d;
        for(int i = 0; i < interpolationValues.length; i++){
            double node = interpolationValues[i][1];
            if(pnode >= node){
                return getInterpolation(i, pnode);
            }
        }
        return interpolationValue;
    }

    /**
     * Method getInfo<br>
     *
     * Gibt einen Informationstext über das gültige Werteintervall aus.
     */
    public void getInfo(){
       System.out.printf("Gültiger Wertebereich < %1$f und >= %2$f. %n" , 
       interpolationValues[0][1],  interpolationValues[interpolationValues.length -1][1]);
    }

    /**
     * Method isValidValue<br>
     * Prüft, ob der übergebene Wert im gültigen Intervall liegt.
     * @param node  Zu prüfender Wert.
     * @return True für einen gültigen Wert und False für einen ungültigen Wert.
     */
    public boolean isValidValue(double node){
       if(node < interpolationValues[0][1] && node >= interpolationValues[interpolationValues.length -1][1]){
           return true;
       }
       return false;
    }
    
}

package raspi;


/**
 * Die Klasse Tank stellt Methoden zur Verfügung,  um Füllmengen von zylindrischen und quaderförmiegen
*  Tanks zu berechnen. Alle übergebenen Parameter werden in Meter angegeben. 
 * 
 * @autor Wolfgang Höfer
 * @version 1.0
 */
public class Tank
{

    /**
     * Method getVolVertCyl<br>
     * Berechnet die Füllmenge in Litern für einen vertikal stehenden Tank in Zylinderform. Der Parameter
     * formGrountToSensor gibt den Abstand des Sensors bis zum Boden des Tanks in Metern an. r ist der 
     * Radius des Tanks in Meter und mit distanceToWaterSurfaceArea wird der vom Sensor gemessene Abstand 
     * bis zur Wasseroberfläche in Meter übergeben.
     *
     * @param distanceToWaterSurfaceArea Gemessener Abstand zwischen Sensor und Wasseroberfläche (Einheit: Meter)
     * @param r Radius des Tanks (Einheit: Meter)
     * @param formGrountToSensor Abstand vom Boden des Tanks bis zum Sensor (Einheit: Meter)
     * @return Füllmenge in Liter
     */
    public static double getVolVertCyl(double distanceToWaterSurfaceArea, double r, double formGrountToSensor){
        if(formGrountToSensor > distanceToWaterSurfaceArea){
            return r*r*Math.PI*(formGrountToSensor - distanceToWaterSurfaceArea)*1000.0d;
        }
        return  r*r*Math.PI*formGrountToSensor*1000.0d;
    }

    /**
     * Method getVolHorCyl<br>
     * Berechnet die Füllmenge in Litern für einen horizontal liegenden Tank in Zylinderform. Der Parameter
     * formGrountToSensor gibt den Abstand des Sensors bis zum Boden des Tanks in Metern an. r ist der 
     * Radius und l die Lämge des Tanks in Meter und mit distanceToWaterSurfaceArea wird der vom Sensor gemessene Abstand 
     * bis zur Wasseroberfläche in Meter übergeben.
     * 
     * @param distanceToWaterSurfaceArea Gemessener Abstand zwischen Sensor und Wasseroberfläche (Einheit: Meter)
     * @param r Radius des Tanks (Einheit: Meter)
     * @param l Länge des Tanks (Einheit: Meter)
     * @param formGrountToSensor Abstand vom Boden des Tanks bis zum Sensor (Einheit: Meter)
     * @return Füllmenge in Liter
     */
    public static double getVolHorCyl(double distanceToWaterSurfaceArea, double r, double l, double formGrountToSensor){
        double h = formGrountToSensor - distanceToWaterSurfaceArea;
        if(h > 2*r){
            h = 2*r;
        }
        return (r*r*Math.acos(1-h/r)-(r-h)*Math.sqrt(2*r*h-h*h))*l*1000.0d;
    }

    /**
     * Method getVolCUBOID<br>
     * Berechnet die Füllmenge in Litern für einen quaderförmigen Tank. Der Parameter
     * formGrountToSensor gibt den Abstand des Sensors bis zum Boden des Tanks in Metern an. 
     * a und b sind Länge und Breite des Tanks in Meter und mit distanceToWaterSurfaceArea wird 
     * der vom Sensor gemessene Abstand bis zur Wasseroberfläche in Meter übergeben.
     *
     * @param distanceToWaterSurfaceArea  Gemessener Abstand zwischen Sensor und Wasseroberfläche (Einheit: Meter)
     * @param a Länge des Tanks (Einheit: Meter)
     * @param b Breite des Tanks (Einheit: Meter)
     * @param formGrountToSensor  Abstand vom Boden des Tanks bis zum Sensor (Einheit: Meter)
     * @return Füllmenge in Liter
     */
    public static double getVolCUBOID(double distanceToWaterSurfaceArea, double a, double b,double formGrountToSensor){
        if(formGrountToSensor > distanceToWaterSurfaceArea){
            return (a*b*(formGrountToSensor - distanceToWaterSurfaceArea))*1000.0d;
        }
        return a*b*formGrountToSensor*1000.0d;
    }

    public static void main(String[] args){
        System.out.println(getVolCUBOID(1.0d, 1.0d, 1.0d, 2.0d));
        System.out.println(getVolVertCyl(1.0d, 0.5d, 2.0d));
        System.out.println(getVolHorCyl(1.0d, 0.5d, 1.0d, 2.0d));
    }
}

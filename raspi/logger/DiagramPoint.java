package raspi.logger;
/**
 * DiagramPoint<br>
 * Containerklasse
 * 
 * @author Wolfgang Höfer
 * @version 1.0
 */
public class DiagramPoint
{
    private double y, x;
    
    /**
     * DiagramPoint Constructor
     *
     * @param x A parameter
     * @param y A parameter
     */
    public DiagramPoint(double x, double y)
    {this.x = x; this.y = y;}
    public double getX(){  return x; }
    public double getY(){  return y; }
}

package raspi.logger;
/**
 * DiagramDatePoint<br>
 * Containerklasse
 * 
 * @author Wolfgang Höfer
 * @version 1.0
 */
public class DiagramDatePoint
{
    private double y;
    private long x;
    
    /**
     * DiagramDatePoint Constructor
     *
     * @param x A parameter
     * @param y A parameter
     */
    public DiagramDatePoint(long x, double y)
    { this.x = x; this.y = y; }
    public long getX(){ return x; }
    public double getY(){  return y; }
}

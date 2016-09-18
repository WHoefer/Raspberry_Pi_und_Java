package raspi.logger;

import java.util.List;
import java.util.ArrayList;
import java.awt.Rectangle;
import java.awt.Point;

/**
 * Write a description of class ComputeDiagramm here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class ComputeDiagram
{
    private double yM;
    private double yD;
    private double xM;
    private double xD;
    private double minXVal;
    private double maxXVal;
    private long minXValD;
    private long maxXValD;
    private double minYVal; 
    private double maxYVal;
    private double yMinMainValue; 
    private double yMainGrid;
    private double ySubGrid;
    private double xMinMainValue;
    private double xMainGrid; 
    private double xSubGrid;
    private long xMinMainValueD;
    private long xMainGridD; 
    private long xSubGridD;
    private boolean linearX;
    private boolean linearY;
    private Point xAxisStartPoint = null;
    private Point xAxisStopPoint = null;
    private Point yAxisStartPoint = null;
    private Point yAxisStopPoint = null;
    private List<Integer> ymainGridList = null;
    private List  ymainValueList = null;
    private List<Integer> ysubGridList = null;
    private List<Integer> xmainGridList = null;
    private List  xmainValueList = null;
    private List<Integer> xsubGridList = null;

    private static final int DATEMODE = 0;
    private static final int DEFAULTMODE = 1;
    private static final int LOGARITHMODE = 2;
    private int mode;

    /**
     * Constructor for objects of class ComputeDiagramm
     */
    public ComputeDiagram(int x, int y , int width, int height, 
    double minXVal, double maxXVal, double minYVal, double maxYVal, 
    double yMinMainValue, double yMainGrid, double ySubGrid, boolean linearY,
    double xMinMainValue, double xMainGrid, double xSubGrid, boolean linearX)
    {
        mode = DEFAULTMODE;
        createPoints(x, y , width, height);
        if(linearY){
            yM = -(yAxisStopPoint.getY() - yAxisStartPoint.getY())/(maxYVal - minYVal);
            yD = yAxisStopPoint.getY() - yM * minYVal;
        }else{
            yM = -(yAxisStopPoint.getY() - yAxisStartPoint.getY())/(Math.log10(maxYVal) - Math.log10(minYVal));   
            yD = yAxisStopPoint.getY() - yM * Math.log10(minYVal);
        } 
        if(linearX){
            xM = (xAxisStopPoint.getX() - xAxisStartPoint.getX())/(maxXVal - minXVal);   
            xD = xAxisStopPoint.getX() - xM * maxXVal;
        }else{
            xM = (xAxisStopPoint.getX() - xAxisStartPoint.getX())/(Math.log10(maxXVal) - Math.log10(minXVal));   
            xD = xAxisStopPoint.getX() - xM * Math.log10(maxXVal);
        }
        this.minXVal = minXVal;
        this.maxXVal = maxXVal;
        this.minYVal = minYVal;
        this.maxYVal = maxYVal;
        this.linearX = linearX;
        this.linearY = linearY;
        this.yMinMainValue = yMinMainValue;
        this.yMainGrid = yMainGrid;
        this.ySubGrid = ySubGrid ;
        this.xMinMainValue = xMinMainValue ;
        this.xMainGrid = xMainGrid;
        this.xSubGrid = xSubGrid;
        createYMainGrid();
        createYSubGrid();
        createXMainGrid();
        createXSubGrid();
    }

    public ComputeDiagram(int x, int y , int width, int height, 
    long minXVal, long maxXVal, double minYVal, double maxYVal, 
    double yMinMainValue, double yMainGrid, double ySubGrid, boolean linearY,
    long xMinMainValue, long xMainGrid, long xSubGrid)
    {
        mode = DATEMODE;
        createPoints(x, y , width, height);
        if(linearY){
            yM = -(yAxisStopPoint.getY() - yAxisStartPoint.getY())/(maxYVal - minYVal);   
            yD = yAxisStopPoint.getY() - yM * minYVal;
        }else{
            yM = -(yAxisStopPoint.getY() - yAxisStartPoint.getY())/(Math.log10(maxYVal) - Math.log10(minYVal));   
            yD = yAxisStopPoint.getY() - yM * Math.log10(minYVal);
        }
        xM = (xAxisStopPoint.getX() - xAxisStartPoint.getX())/(maxXVal - minXVal);   
        xD = xAxisStopPoint.getX() - xM * maxXVal;
        this.minXValD = minXVal;
        this.maxXValD = maxXVal;
        this.minYVal = minYVal;
        this.maxYVal = maxYVal;
        this.linearX = true;
        this.linearY = linearY;
        this.yMinMainValue = yMinMainValue;
        this.yMainGrid = yMainGrid;
        this.ySubGrid = ySubGrid ;
        this.xMinMainValueD = xMinMainValue ;
        this.xMainGridD = xMainGrid;
        this.xSubGridD = xSubGrid;
        createYMainGrid();
        createYSubGrid();
        createXMainGridDate();
        createXSubGridDate();
    }

    private void createPoints(int x, int y , int width, int height){
        xAxisStartPoint = new Point(x, y+height);
        xAxisStopPoint =  new Point(x+width , y+height);
        yAxisStartPoint = new Point(x, y);
        yAxisStopPoint =  new Point(x, y+height);
    }        

    public int getYPosForValue(double value){
        if(linearY){
            return (int)(yM * value + yD); 
        }
        return (int)(yM * Math.log10(value) + yD); 
    }

    public int getXPosForValue(double value){
        if(linearX){
            return (int)(xM * value + xD); 
        }
        return (int)(xM * Math.log10(value) + xD); 
    }

    public int getXPosForValue(long value){
        return (int)(xM * value + xD); 
    }

    private void createYMainGrid(){
        ymainGridList = new ArrayList<Integer>();
        ymainValueList = new ArrayList();
        if(linearY){
            for(double v = yMinMainValue; v <= maxYVal ; v = v + yMainGrid){
                ymainGridList.add(new Integer(getYPosForValue(v)));
                ymainValueList.add(v);
            }
        }else{
            for(double v = minYVal; v <= maxYVal ; v = v * 10.0d){
                ymainGridList.add(new Integer(getYPosForValue(v)));
                ymainValueList.add(v);
            }
        }
    }

    private void createYSubGrid(){
        ysubGridList = new ArrayList<Integer>();
        if(linearY){
            for(double v = yMinMainValue; v >= minYVal ; v = v - ySubGrid){
                ysubGridList.add(new Integer(getYPosForValue(v)));
            }
            for(double v = yMinMainValue; v <= maxYVal ; v = v + ySubGrid){
                ysubGridList.add(new Integer(getYPosForValue(v)));
            }
        }else{
            for(double v = minYVal; v < maxYVal ; v = v * 10.0d){
                for(double s = 1; s <= 9 ; s++){ 
                    ysubGridList.add(new Integer(getYPosForValue(v*s)));
                }
            }
        }
    }

    private void createXMainGrid(){
        xmainGridList = new ArrayList<Integer>();
        xmainValueList = new ArrayList();
        if(linearX){
            for(double v = xMinMainValue; v <= maxXVal ; v = v + xMainGrid){
                xmainGridList.add(new Integer(getXPosForValue(v)));
                xmainValueList.add(v);
            }
        }else{
            for(double v = minXVal; v <= maxXVal ; v = v * 10.0d){
                xmainGridList.add(new Integer(getXPosForValue(v)));
                xmainValueList.add(v);
            }
        }
    }

    private void createXSubGrid(){
        xsubGridList = new ArrayList<Integer>();
        if(linearX){
            for(double v = xMinMainValue; v >= minXVal ; v = v - xSubGrid){
                xsubGridList.add(new Integer(getXPosForValue(v)));
            }
            for(double v = xMinMainValue; v <= maxXVal ; v = v + xSubGrid){
                xsubGridList.add(new Integer(getXPosForValue(v)));
            }
        }else{
            for(double v = minXVal; v < maxXVal ; v = v * 10.0d){
                for(double s = 1; s <= 9 ; s++){ 
                    xsubGridList.add(new Integer(getXPosForValue(v*s)));
                }
            }
        }
    }

    private void createXMainGridDate(){
        xmainGridList = new ArrayList<Integer>();
        xmainValueList = new ArrayList();
        for(long v = xMinMainValueD; v >= minXValD ; v = v - xMainGridD){
            xmainGridList.add(new Integer(getXPosForValue(v)));
            xmainValueList.add(v);
        }
        for(long v = xMinMainValueD; v <= maxXValD ; v = v + xMainGridD){
            xmainGridList.add(new Integer(getXPosForValue(v)));
            xmainValueList.add(v);
        }
    }

    private void createXSubGridDate(){
        xsubGridList = new ArrayList<Integer>();
        for(long v = xMinMainValueD; v >= minXValD ; v = v - xSubGridD){
            xsubGridList.add(new Integer(getXPosForValue(v)));
        }
        for(long v = xMinMainValueD; v <= maxXValD ; v = v + xSubGridD){
            xsubGridList.add(new Integer(getXPosForValue(v)));
        }
    }

    public List getMainGridYValues(){
        return ymainValueList;
    }    

    public List getMainGridYPos(){
        return ymainGridList;
    }    

    public List getSubGridYPos(){
        return ysubGridList;
    }    

    public List getMainGridXValues(){
        return xmainValueList;
    }    

    public List getMainGridXPos(){
        return xmainGridList;
    }    

    public List getSubGridXPos(){
        return xsubGridList;
    }    

    public Point getYAxisStartPoint(){
        return yAxisStartPoint;
    }

    public Point getYAxisStopPoint(){
        return yAxisStopPoint;
    }

    public Point getXAxisStartPoint(){
        return xAxisStartPoint;
    }

    public Point getXAxisStopPoint(){
        return xAxisStopPoint;
    }

}

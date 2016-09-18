package raspi.logger;

//import javax.swing.SwingUtilities;
//import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics; 
import java.awt.Font; 
import java.awt.Point;
import java.awt.FontMetrics; 
/*import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseMotionAdapter; 
*/
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.Map.Entry;
import java.util.Iterator;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.Locale;

/**
 * Write a description of class JDiagramm here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class JDiagram extends JPanel {

    private String xoutFormat;
    private String youtFormat;
    private TimeZone timeZone = null;
    private Locale locale = null;

    private int x;
    private int y;
    private int width;
    private int height;

    private static final int DATEMODEFORXAXIS = 0;
    private static final int DEFAULTMODEFORXAXIS = 1;
    private static final int LOGARITHMODEFORXAXIS = 2;
    private static final int DEFAULTMODEFORYAXIS = 3;
    private static final int LOGARITHMODEFORYAXIS = 4;
    private int modeXAxis;
    private int modeYAxis;

    private long minXValDate;
    private long maxXValDate;
    private long xMinMainValueDate;
    private long xMainGridDate;
    private long xSubGridDate;

    private double minXVal;
    private double maxXVal;
    private double minYVal;
    private double maxYVal; 
    private double yMinMainValue;
    private double yMainGrid;
    private double ySubGrid;
    private boolean linearY;
    private double xMinMainValue;
    private double xMainGrid;
    private double xSubGrid; 
    private boolean linearX;
    private boolean drawSubGridY;
    private boolean drawSubGridX;
    private boolean drawMainGridY;
    private boolean drawMainGridX;
    private Color xMainGridColor;
    private Color yMainGridColor;
    private Color xSubGridColor;
    private Color ySubGridColor;
    private Color color;

    private List<DiagramPoint> pointList;
    private List<DiagramDatePoint> datePointList;
    private Map dataMap;
    private Map dataColorMap;

    public static final long SECOND = 1000 ;
    public static final long MINUTE = SECOND * 60;
    public static final long HOUR = MINUTE * 60;
    public static final long DAY = HOUR * 24;

    private static final String COLOR = "1";
    private static final String LIST = "2";

    @SuppressWarnings("unchecked")
    public JDiagram() {
        xoutFormat = "%1$s";
        youtFormat = "%1$s";
        linearX = true;
        linearY = true;
        drawSubGridY = true;
        drawSubGridX = true;
        drawMainGridY = true;
        drawMainGridX = true;
        minXVal = -100.0d;
        maxXVal = 100.0d;
        xMinMainValue = -100.0d;
        xMainGrid = 50.0d;
        xSubGrid = 10.0d;
        minYVal = -100.0d;
        maxYVal = 100.0d;
        yMinMainValue = -100.0d;
        yMainGrid = 50.0d;
        ySubGrid = 10.0d;
        modeXAxis = DEFAULTMODEFORXAXIS;
        modeYAxis = DEFAULTMODEFORYAXIS;
        timeZone = TimeZone.getTimeZone("Europe/Berlin");
        locale = Locale.GERMANY;
        setBorder(BorderFactory.createLineBorder(Color.black));
        xMainGridColor = Color.black;
        yMainGridColor = Color.black;
        xSubGridColor = Color.gray;
        ySubGridColor = Color.gray;
        x = 40;
        y = 40;
        width = 400;
        height = 200;
        pointList = new ArrayList();
        datePointList = new ArrayList();
        dataMap = new HashMap();
        dataColorMap = new HashMap();
    }

    public Dimension getPreferredSize() {
        return new Dimension(400,200);
    }

    public void add(String name, Color colorData, List list){
        Map map = new HashMap();
        map.put(LIST, list);
        map.put(COLOR, colorData);
        dataMap.put(name, map);
    }

    public void remove(String name){
        dataMap.remove(name);
    }

    private void computeDimension(Graphics g){
        FontMetrics  metrics = g.getFontMetrics();
        String outVal = String.format(locale, youtFormat, maxYVal);
        x = metrics.stringWidth(outVal);
        outVal = String.format(locale, youtFormat, minYVal);
        if(metrics.stringWidth(outVal) > x){
            x = metrics.stringWidth(outVal);
        }
        x = x + 10; 
        y = 10 + metrics.getHeight();;
        height = getHeight()-y-metrics.getHeight()-10;
        if(modeXAxis == DEFAULTMODEFORXAXIS || modeXAxis == LOGARITHMODEFORXAXIS){
            outVal = String.format(locale, xoutFormat, maxXVal);
        }else{
            outVal = String.format(locale, xoutFormat, maxXValDate);
        }
        width = getWidth()-x-10-metrics.stringWidth(outVal)/2;
    }

    public static void drawStringYPos(Graphics g, String text, int xRight, int yRight){
        FontMetrics  metrics = g.getFontMetrics();
        int hgt = metrics.getHeight();
        int strwidth = metrics.stringWidth(text);
        g.drawString(text,xRight-strwidth-2,yRight+hgt/3);
    }

    public static void drawStringXPos(Graphics g, String text, int xTop, int yTop){
        FontMetrics  metrics = g.getFontMetrics();
        int hgt = metrics.getHeight();
        int strwidth = metrics.stringWidth(text);
        g.drawString(text,xTop-strwidth/2, yTop+2+hgt);
    }

    private void drawData(Graphics g, ComputeDiagram computeDiagram){
        int xp =0;
        int yp = 0;
        DiagramPoint dp;
        DiagramDatePoint ddp;
        Set keySet = dataMap.keySet();
        Iterator it = keySet.iterator();
        while(it.hasNext()){
            Object key = it.next();
            dataColorMap = (Map)dataMap.get(key);
            List list = (List)dataColorMap.get(LIST);
            Color colorPoints = (Color)dataColorMap.get(COLOR);
            color = g.getColor();
            g.setColor(colorPoints);
            Iterator itData = list.iterator();
            while(itData.hasNext()){
                Object obj = itData.next();
                if(obj instanceof DiagramPoint){
                    dp = (DiagramPoint)obj;
                    xp = computeDiagram.getXPosForValue(dp.getX());
                    yp = computeDiagram.getYPosForValue(dp.getY());
                    if(dp.getX() <= maxXVal && dp.getX() >= minXVal && dp.getY() <= maxYVal && dp.getY() >= minYVal){
                        g.fillRect(xp, yp, 2, 2);
                    }
                }
                if(obj instanceof DiagramDatePoint){
                    ddp = (DiagramDatePoint)obj;
                    xp = computeDiagram.getXPosForValue(ddp.getX());
                    yp = computeDiagram.getYPosForValue(ddp.getY());
                    Calendar cal = new GregorianCalendar();
                    cal.setTimeInMillis(xp);
                    if(ddp.getX() <= maxXValDate && ddp.getX() >= minXValDate && ddp.getY() <= maxYVal && ddp.getY() >= minYVal){
                        g.fillRect(xp, yp, 2, 2);
                    }
                }
            }
            g.setColor(color);
        }
    }

    private void drawYAxis(Graphics g, ComputeDiagram computeDiagram){
        List posList = computeDiagram.getMainGridYPos();
        List valueList = computeDiagram.getMainGridYValues();
        Point yAxisStartPoint = computeDiagram.getYAxisStartPoint();
        Point yAxisStopPoint = computeDiagram.getYAxisStopPoint();
        int x = (int)yAxisStartPoint.getX();
        Iterator it = valueList.iterator();
        int i = 0;
        g.drawLine((int)yAxisStartPoint.getX(),(int)yAxisStartPoint.getY(),
            (int)yAxisStopPoint.getX() , (int)yAxisStopPoint.getY());
        while(it.hasNext()){
            Double val = new Double((double)it.next());
            int y = ((Integer)posList.get(i)).intValue();
            String outVal = String.format(locale, youtFormat, val);
            drawStringYPos(g, outVal, x, y);
            i++;
        }
    }

    private void drawYMainGrid(Graphics g, ComputeDiagram computeDiagram){
        List posList = computeDiagram.getMainGridYPos();
        Point yAxisStartPoint = computeDiagram.getYAxisStartPoint();
        Point xAxisStopPoint = computeDiagram.getXAxisStopPoint();
        int xStart = (int)yAxisStartPoint.getX();
        int xStop = (int)xAxisStopPoint.getX();
        Iterator it = posList.iterator();
        while(it.hasNext()){
            Integer y = (Integer)it.next();
            g.drawLine(xStart,y,xStop,y);
        }
    }

    private void drawYSubGrid(Graphics g, ComputeDiagram computeDiagram){
        List posList = computeDiagram.getSubGridYPos();
        Point yAxisStartPoint = computeDiagram.getYAxisStartPoint();
        Point xAxisStopPoint = computeDiagram.getXAxisStopPoint();
        int xStart = (int)yAxisStartPoint.getX();
        int xStop = (int)xAxisStopPoint.getX();
        Iterator it = posList.iterator();
        while(it.hasNext()){
            Integer y = (Integer)it.next();
            g.drawLine(xStart,y,xStop,y);
        }
    }

    private void drawXAxis(Graphics g, ComputeDiagram computeDiagram){
        List posList = computeDiagram.getMainGridXPos();
        List valueList = computeDiagram.getMainGridXValues();
        Point xAxisStartPoint = computeDiagram.getXAxisStartPoint();
        Point xAxisStopPoint = computeDiagram.getXAxisStopPoint();
        int y = (int)xAxisStartPoint.getY();
        Iterator it = valueList.iterator();
        int i = 0;
        int x = 0;
        String outVal = "";
        g.drawLine((int)xAxisStartPoint.getX(),(int)xAxisStartPoint.getY(),
            (int)xAxisStopPoint.getX() ,(int)xAxisStopPoint.getY());
        while(it.hasNext()){
            if(modeXAxis == DEFAULTMODEFORXAXIS || modeXAxis == LOGARITHMODEFORXAXIS){
                Double val = new Double((double)it.next());
                x = ((Integer)posList.get(i)).intValue();
                outVal = String.format(locale, xoutFormat, val);
            }else{
                Long val = new Long((long)it.next());
                x = ((Integer)posList.get(i)).intValue();
                outVal = String.format(locale, xoutFormat, val);
            }                
            drawStringXPos(g, outVal, x, y);
            i++;
        }
    }

    private void drawXMainGrid(Graphics g, ComputeDiagram computeDiagram){
        List posList = computeDiagram.getMainGridXPos();
        Point yAxisStartPoint = computeDiagram.getYAxisStartPoint();
        Point yAxisStopPoint = computeDiagram.getYAxisStopPoint();
        int yStart = (int)yAxisStartPoint.getY();
        int yStop = (int)yAxisStopPoint.getY();
        Iterator it = posList.iterator();
        while(it.hasNext()){
            Integer x = (Integer)it.next();
            g.drawLine(x,yStart,x,yStop);
        }
    }

    private void drawXSubGrid(Graphics g, ComputeDiagram computeDiagram){
        List posList = computeDiagram.getSubGridXPos();
        Point yAxisStartPoint = computeDiagram.getYAxisStartPoint();
        Point yAxisStopPoint = computeDiagram.getYAxisStopPoint();
        int yStart = (int)yAxisStartPoint.getY();
        int yStop = (int)yAxisStopPoint.getY();
        Iterator it = posList.iterator();
        while(it.hasNext()){
            Integer x = (Integer)it.next();
            g.drawLine(x,yStart,x,yStop);
        }
    }

    public void setXMainGridColor(Color gridColor){
        this.xMainGridColor = gridColor;
    }

    public void setYMainGridColor(Color gridColor){
        this.yMainGridColor = gridColor;
    }

    public void setXSubGridColor(Color gridColor){
        this.xSubGridColor = gridColor;
    }

    public void setYSubGridColor(Color gridColor){
        this.ySubGridColor = gridColor;
    }

    public void setXAxisLinaer(double minXVal, double maxXVal, double xMinMainValue, double xMainGrid, double xSubGrid, String xoutFormat){
        modeXAxis = DEFAULTMODEFORXAXIS;
        linearX = true;
        if(xoutFormat == null || xoutFormat.isEmpty()){
            this.xoutFormat = "%1$s";
        }else{
            this.xoutFormat = xoutFormat;
        }
        this.xMainGrid = xMainGrid;
        this.xSubGrid = xSubGrid;
        this.minXVal = minXVal;
        this.maxXVal = maxXVal;
        this.xMinMainValue = xMinMainValue;
        this.xMainGrid = xMainGrid;
        this.xSubGrid = xSubGrid;
    }

    public void setXAxisLogarithmic(double minXVal, double maxXVal, String xoutFormat){
        modeXAxis = LOGARITHMODEFORXAXIS;
        linearX = false;
        if(xoutFormat == null || xoutFormat.isEmpty()){
            this.xoutFormat = "%1$s";
        }else{
            this.xoutFormat = xoutFormat;
        }
        this.xMainGrid = xMainGrid;
        this.xSubGrid = xSubGrid;
        this.minXVal = minXVal;
        this.maxXVal = maxXVal;
    }

    public void setXAxisForDate(Calendar startcal, Calendar stopcal, Calendar startgridcal, long xMainGrid, long xSubGrid, String xoutFormat){
        modeXAxis = DATEMODEFORXAXIS;
        linearX = true;
        if(xoutFormat == null || xoutFormat.isEmpty()){
            this.xoutFormat = "%1$s";
        }else{
            this.xoutFormat = xoutFormat;
        }
        this.minXValDate = startcal.getTimeInMillis();
        this.maxXValDate = stopcal.getTimeInMillis();
        this.xMinMainValueDate = startgridcal.getTimeInMillis();
        this.xMainGridDate = xMainGrid;
        this.xSubGridDate = xSubGrid;
    }

    public void setXAxisForDateOneDay(Calendar stopcal){
        modeXAxis = DATEMODEFORXAXIS;
        linearX = true;
        Calendar startcal = new GregorianCalendar(timeZone, locale);
        Calendar startGridCal = new GregorianCalendar(timeZone, locale);
        startcal.setTimeInMillis(stopcal.getTimeInMillis());
        startcal.add(Calendar.HOUR, -24);
        startGridCal.setTimeInMillis(stopcal.getTimeInMillis());
        startGridCal.add(Calendar.HOUR, -23);
        startGridCal.set(Calendar.MINUTE, 0);
        startGridCal.set(Calendar.SECOND, 0);
        startGridCal.set(Calendar.MILLISECOND, 0);
        setXAxisForDate(startcal, stopcal, startGridCal, this.HOUR, this.MINUTE * 30, "%1$tH");
    }

    public void setXAxisForDateOneWeek(Calendar stopcal){
        modeXAxis = DATEMODEFORXAXIS;
        linearX = true;
        Calendar startcal = new GregorianCalendar(timeZone, locale);
        Calendar startGridCal = new GregorianCalendar(timeZone, locale);
        startcal.setTimeInMillis(stopcal.getTimeInMillis());
        startcal.add(Calendar.DAY_OF_MONTH, -7);
        startGridCal.setTimeInMillis(startcal.getTimeInMillis());
        startGridCal.set(Calendar.HOUR, 0);
        startGridCal.set(Calendar.MINUTE, 0);
        startGridCal.set(Calendar.SECOND, 0);
        startGridCal.set(Calendar.MILLISECOND, 0);
        setXAxisForDate(startcal, stopcal, startGridCal, this.DAY, this.HOUR * 12, "%1$ta");
    }

    public void setYAxisLinaer(double minYVal, double maxYVal, double yMinMainValue, double yMainGrid, double ySubGrid, String youtFormat){
        modeYAxis = DEFAULTMODEFORYAXIS;
        linearY = true;
        if(youtFormat == null || youtFormat.isEmpty()){
            this.youtFormat = "%1$s";
        }else{
            this.youtFormat = youtFormat;
        }
        this.yMainGrid = yMainGrid;
        this.ySubGrid = ySubGrid;
        this.minYVal = minYVal;
        this.maxYVal = maxYVal;
        this.yMinMainValue = yMinMainValue;
        this.yMainGrid = yMainGrid;
        this.ySubGrid = ySubGrid;
    }

    public void setYAxisLogarithmic(double minYVal, double maxYVal, String youtFormat){
        modeYAxis = LOGARITHMODEFORYAXIS;
        linearY = false;
        if(youtFormat == null || youtFormat.isEmpty()){
            this.youtFormat = "%1$s";
        }else{
            this.youtFormat = youtFormat;
        }
        this.yMainGrid = yMainGrid;
        this.ySubGrid = ySubGrid;
        this.minYVal = minYVal;
        this.maxYVal = maxYVal;
    }

    public void viewSubgridX(boolean visable){
        drawSubGridX = visable;
    }

    public void viewSubgridY(boolean visable){
        drawSubGridY = visable;
    }

    public void viewMaingridX(boolean visable){
        drawMainGridX = visable;
    }

    public void viewMaingridY(boolean visable){
        drawMainGridY = visable;
    }
    
    @SuppressWarnings("unchecked")
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); 
        this.computeDimension(g);
        
        ComputeDiagram computeDiagram = null;
        if(modeXAxis == DATEMODEFORXAXIS) {
            computeDiagram = new ComputeDiagram(
                x, y, width, height,
                minXValDate, maxXValDate, minYVal, maxYVal,
                yMinMainValue, yMainGrid, ySubGrid, linearY, 
                xMinMainValueDate, xMainGridDate, xSubGridDate);
        } else{
            computeDiagram = new ComputeDiagram(
                x, y, width, height,
                minXVal, maxXVal, minYVal, maxYVal,
                yMinMainValue, yMainGrid, ySubGrid, linearY, 
                xMinMainValue, xMainGrid, xSubGrid, linearX);
        }    
        color = g.getColor();
        if(drawSubGridY){
            g.setColor(ySubGridColor);
            drawYSubGrid(g, computeDiagram);
        }
        if(drawSubGridX){
            g.setColor(xSubGridColor);
            drawXSubGrid(g, computeDiagram);
        }
        if(drawMainGridX){
            g.setColor(xMainGridColor);
            drawXMainGrid(g, computeDiagram);
        }
        if(drawMainGridY){
            g.setColor(yMainGridColor);
            drawYMainGrid(g, computeDiagram);
        }
        g.setColor(color);
        drawYAxis(g, computeDiagram);
        drawXAxis(g, computeDiagram);
        drawData(g, computeDiagram);
    }  
}

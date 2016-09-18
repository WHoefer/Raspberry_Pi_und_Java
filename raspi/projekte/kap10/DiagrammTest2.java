package raspi.projekte.kap10;


import java.awt.*;
import javax.swing.*;
import java.util.GregorianCalendar;
import java.util.Calendar;
import java.util.List;
import java.util.ArrayList;
import java.io.IOException;
import raspi.logger.JDiagram;
import raspi.logger.DataLogger;

/**
 * Write a description of class DiagrammTest1 here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class DiagrammTest2 extends JFrame {

    private JDiagram diagram1;
    private JDiagram diagram2;
    private DataLogger dataLogger;

    @SuppressWarnings("unchecked")
    public DiagrammTest2(){

        setTitle("DiagrammTest2");
        setSize(424, 436);

        JPanel contentPane = new JPanel(null);
        contentPane.setPreferredSize(new Dimension(424,436));
        contentPane.setBackground(new Color(192,192,192));
        
        Calendar calStart = new GregorianCalendar();
        calStart.set(2015, Calendar.APRIL, 6, 10, 0,0);
        Calendar calStopp = new GregorianCalendar();
        calStopp.set(2015, Calendar.APRIL, 7, 15, 0, 0);
        List loggerListTemp = new ArrayList();
        List loggerListBeleucht = new ArrayList();
        try{
            //dataLogger = new DataLogger("C:\\temp", "DefaultLogger.txt", 300);
            dataLogger = new DataLogger();
            loggerListTemp =dataLogger.getDiagramDatePointListFromFile(calStart, calStopp, "TEMPER");
            loggerListBeleucht =dataLogger.getDiagramDatePointListFromFile(calStart, calStopp, "BELEUC");
        }catch(IOException ex){
        }

        
        diagram1 = new JDiagram();
        diagram1.setBorder(BorderFactory.createEtchedBorder(1));
        diagram1.setBounds(12,12,400,200);
        diagram1.setBackground(new Color(214,217,223));
        diagram1.setForeground(new Color(0,0,0));
        diagram1.setEnabled(true);
        diagram1.setFont(new Font("sansserif",0,8));
        diagram1.setVisible(true);
        diagram1.setXAxisForDateOneDay(calStopp);
        diagram1.setYAxisLogarithmic(0.001d, 100000d, null);
        diagram1.viewSubgridX(false);
        diagram1.viewSubgridY(false);
        diagram1.add("Beleuchtung", Color.black, loggerListBeleucht);

        diagram2 = new JDiagram();
        diagram2.setBorder(BorderFactory.createEtchedBorder(1));
        diagram2.setBounds(12,224,400,200);
        diagram2.setBackground(new Color(255,255,255));
        diagram2.setForeground(new Color(255,0,0));
        diagram2.setEnabled(true);
        diagram2.setFont(new Font("sansserif",0,8));
        diagram2.setVisible(true);
        diagram2.setXAxisForDateOneDay(calStopp);
        diagram2.setYAxisLinaer(15d, 35d, 20d, 10d, 2.5d, "%1$s °C");
        diagram2.viewSubgridX(false);
        diagram2.viewSubgridY(true);
        diagram2.add("Temperatur", Color.black, loggerListTemp);

        contentPane.add(diagram1);
        contentPane.add(diagram2);

        getContentPane().add(contentPane);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        pack();
        setVisible(true);
    }

    public static void main(String[] args){
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    new DiagrammTest2();
                }
            });
    }

}
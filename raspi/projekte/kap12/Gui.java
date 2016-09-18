package raspi.projekte.kap12;

import javax.swing.UIManager.LookAndFeelInfo;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.border.Border;
import javax.swing.*;
import raspi.logger.JDiagram;
import raspi.logger.DiagramDatePoint;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class Gui extends JFrame {
    private JLabel lTagesverlauf;
    private JLabel lTitel;
    private JLabel lJahresverlauf;
    private JDiagram diagram1;
    private JDiagram diagram2;
    private JButton bExit;
    private List listTag;
    private List listMax;
    private List listMin;
    private HttpClient httpClient;
    private Calendar calStopp;
    private Calendar calStart;
    private Calendar calStartGrid;
    private int yPos;
    private int wDiag;

    public Gui(){

        httpClient = new HttpClient("http://localhost:8080/daten");
        listTag = httpClient.getTag();
        listMax = httpClient.getJahrMax();
        listMin = httpClient.getJahrMin();
        double menge = 0.0d;
        if(listTag != null && listTag.size() > 0){
            menge = ((DiagramDatePoint)listTag.get(listTag.size()-1)).getY();
        }

        calStopp = new GregorianCalendar();
        calStart = new GregorianCalendar();
        calStartGrid = new GregorianCalendar();
        calStart.setTimeInMillis(calStopp.getTimeInMillis());
        calStartGrid.setTimeInMillis(calStopp.getTimeInMillis());
        calStart.add(Calendar.DAY_OF_YEAR, -365);
        calStartGrid.add(Calendar.DAY_OF_YEAR, -300);
        calStartGrid.set(Calendar.DAY_OF_MONTH, 2);

        this.setTitle("Zisterne");
        this.setSize(520,800);
        this.setLocation(0, 0);
        Color bgCol = new Color(214,217,223);
        Color bgColDia = new Color(255,255,255);
        Color fgCol = new Color(0,0,0);
        Font fontDiag = new Font(Font.DIALOG,Font.PLAIN,10);
        Font fontLabel = new Font(Font.DIALOG,Font.PLAIN,12);
        Font fontTitel = new Font(Font.DIALOG,Font.BOLD,16);

        JPanel contentPane = new JPanel(null);
        contentPane.setPreferredSize(new Dimension(1024,520));
        contentPane.setBackground(bgCol);
        yPos = 10;
        wDiag = 970;

        lTitel = new JLabel();
        lTitel.setBounds(20,yPos,550,40);
        lTitel.setBackground(bgCol);
        lTitel.setForeground(fgCol);
        lTitel.setFont(fontTitel);
        lTitel.setText(String.format(Locale.GERMANY,"Verfügbare Wassermenge in der Zisterne %1$.0f Liter.", menge));
        lTitel.setVisible(true);

        bExit = new JButton();
        bExit.setBounds(580,yPos,90,40);
        bExit.setBackground(bgCol);
        bExit.setForeground(fgCol);
        bExit.setEnabled(true);
        bExit.setFont(fontLabel);
        bExit.setText("Exit");
        bExit.setVisible(true);
        bExit.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    System.exit(0);
                }
            });

        lTagesverlauf = new JLabel();
        lTagesverlauf.setBounds(20,yPos=yPos+40,200,20);
        lTagesverlauf.setBackground(bgCol);
        lTagesverlauf.setForeground(fgCol);
        lTagesverlauf.setFont(fontLabel);
        lTagesverlauf.setText("Tagesverlauf");
        lTagesverlauf.setVisible(true);

        diagram1 = new JDiagram();
        diagram1.setBorder(BorderFactory.createEmptyBorder());
        diagram1.setBounds(20,yPos=yPos+20,wDiag,200);
        diagram1.setBackground(bgColDia);
        diagram1.setForeground(fgCol);
        diagram1.setFont(fontDiag);
        diagram1.setVisible(true);
        diagram1.setXAxisForDateOneDay(calStopp);
        diagram1.setYAxisLinaer(0.0d, 2650.0d, 500.0d, 500.0d, 250.0d, "%1$.0f l");
        diagram1.viewSubgridX(false);
        diagram1.viewSubgridY(false);
        diagram1.add("Tag", Color.black, listTag);

        lJahresverlauf = new JLabel();
        lJahresverlauf.setBounds(20,yPos=yPos+210,350,20);
        lJahresverlauf.setBackground(bgCol);
        lJahresverlauf.setForeground(fgCol);
        lJahresverlauf.setFont(fontLabel);
        lJahresverlauf.setText("Jahresverlauf");
        lJahresverlauf.setVisible(true);

        diagram2 = new JDiagram();
        diagram2.setBorder(BorderFactory.createEmptyBorder());
        diagram2.setBounds(20,yPos=yPos+20,wDiag,200);
        diagram2.setBackground(bgColDia);
        diagram2.setForeground(fgCol);
        diagram2.setFont(fontDiag);
        diagram2.setVisible(true);
        diagram2.setXAxisForDate(calStart, calStopp, calStartGrid, JDiagram.DAY*31, JDiagram.DAY*31, "%1$te. %1$tb");
        diagram2.setYAxisLinaer(0.0d, 2650.0d, 500.0d, 500.0d, 250.0d, "%1$.0f l");
        diagram2.viewSubgridX(false);
        diagram2.viewSubgridY(false);
        diagram2.add("Min", Color.black, listMin);
        diagram2.add("Max", Color.black, listMax);

        contentPane.add(lTagesverlauf);
        contentPane.add(lTitel);
        contentPane.add(lJahresverlauf);
        contentPane.add(diagram1);
        contentPane.add(diagram2);
        contentPane.add(bExit);

        this.add(contentPane);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
    }

    public static void main(String[] args){
        System.setProperty("swing.defaultlaf", "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    new Gui();
                }
            });
    }

}
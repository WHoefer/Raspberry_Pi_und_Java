package raspi.projekte.kap10;


import java.awt.*;
import javax.swing.*;
import java.util.List;
import java.util.ArrayList;
import raspi.logger.JDiagram;
import raspi.logger.DiagramPoint;

/**
 * Testprogramm für die Klasse JDiagram
 * 
 * @author Wolfgang Höfer
 * @version 1.0
 */
public class DiagrammTest1 extends JFrame {

    private JDiagram diagram1, diagram2, diagram3;
    private List testList1, testList2, testList3;

    @SuppressWarnings("unchecked")
    public DiagrammTest1(){

        setTitle("DiagrammTest1");
        setSize(424, 648);
        JPanel contentPane = new JPanel(null);
        contentPane.setPreferredSize(new Dimension(424,648));
        contentPane.setBackground(new Color(192,192,192));

        testList1 = new ArrayList();
        testList2 = new ArrayList();
        testList3 = new ArrayList();
        for(float i = 0; i < 1000; i = i + 2){
            testList1.add(new DiagramPoint(i, i/10));
            testList2.add(new DiagramPoint(i, i/20));
            testList3.add(new DiagramPoint(i, i/40));
        }

        diagram1 = getDiagram(12, Color.white,12);
        diagram1.setXAxisLinaer(0d, 1000d, 0d, 200d, 100d, null);
        diagram1.setYAxisLinaer(0d, 100d, 0d, 20d, 10d, "%1$s °C");
        diagram1.viewSubgridX(true);
        diagram1.viewSubgridY(false);

        diagram2 = getDiagram(224, Color.gray,14);
        diagram2.setXAxisLinaer(0d, 1000d, 200d, 200d, 100d, null);
        diagram2.setYAxisLogarithmic(0.1d, 100d, null);
        diagram2.viewSubgridX(false);
        diagram2.viewSubgridY(false);

        diagram3 = getDiagram(436, Color.white,12);
        diagram3.setXAxisLogarithmic(10d, 1000d, null);
        diagram3.setYAxisLogarithmic(0.1d, 100d, null);
        diagram3.viewSubgridX(true);
        diagram3.viewSubgridY(true);

        contentPane.add(diagram1);
        contentPane.add(diagram2);
        contentPane.add(diagram3);
        getContentPane().add(contentPane);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        pack();
        setVisible(true);
    }

    private JDiagram getDiagram(int y, Color color, int fontHeight){
       JDiagram jDiagram = new JDiagram();
       jDiagram.setBorder(BorderFactory.createEtchedBorder(1));
       jDiagram.setBounds(12,y,400,200);
       jDiagram.setBackground(color);
       jDiagram.setForeground(new Color(0,0,0));
       jDiagram.setEnabled(true);
       jDiagram.setFont(new Font("sansserif",1,fontHeight));
       jDiagram.setVisible(true);
       jDiagram.add("test1",Color.red, testList1);
       jDiagram.add("test2", Color.black,testList2);
       jDiagram.add("test3", Color.green,testList3);

       return jDiagram;
    }
    
    
    public static void main(String[] args){
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    new DiagrammTest1();
                }
            });
    }

}
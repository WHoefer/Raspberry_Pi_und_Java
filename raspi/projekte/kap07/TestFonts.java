package raspi.projekte.kap07;

import java.awt.Dimension;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.GraphicsEnvironment;
import java.util.Locale;
public class TestFonts extends JFrame {

    private void setzeLabel(JLabel label, String font, int style, int size, String text, int y){
        label.setBounds(10,y,120,35);
        label.setBackground(new Color(214,217,223));
        label.setForeground(new Color(0,0,0));
        label.setEnabled(true);
        label.setFont(new Font("sansserif",style,size));
        label.setText(text);
        label.setVisible(true);
    }    

    //Constructor 
    @SuppressWarnings("unchecked")
    public TestFonts(){

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String[] fonts =  ge.getAvailableFontFamilyNames();

        setTitle("Test Fonts");
        setSize(300,100);
        JPanel contentPane = new JPanel(null);
        contentPane.setPreferredSize(new Dimension(300,600));
        contentPane.setBackground(new Color(192,192,192));
        //Label wird initialisiert
        JLabel label1 = new JLabel();
        setzeLabel(label1,Font.SANS_SERIF, Font.PLAIN, 12,Font.SANS_SERIF+ ",PLAIN,12", 35);
        JLabel label2 = new JLabel();
        setzeLabel(label2,Font.DIALOG, Font.BOLD, 12,Font.DIALOG+",BOLD,12", 70);
        JLabel label3 = new JLabel();
        setzeLabel(label3,Font.DIALOG_INPUT, Font.BOLD, 12, Font.DIALOG_INPUT+",Bold,12", 105);

        contentPane.add(label1);
        contentPane.add(label2);
        contentPane.add(label3);
        getContentPane().add(contentPane);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        pack(); 
        setVisible(true);
    }

    public static void main(String[] args){
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    new TestFonts();
                }
            });
    }

}
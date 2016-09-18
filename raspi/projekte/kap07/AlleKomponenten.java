package raspi.projekte.kap07;

/**
 *Text genereted by Simple GUI Extension for BlueJ
 */
import javax.swing.UIManager.LookAndFeelInfo;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import javax.swing.border.Border;
import javax.swing.*;

public class AlleKomponenten extends JFrame {
    private JMenuBar menuBar;
    private JButton button1;
    private JCheckBox checkbox1;
    private JComboBox combobox1;
    private DrawingPane drawingPane;
    private JLabel label1;
    private JLabel label2;
    private JLabel label3;
    private JLabel label4;
    private JLabel label5;
    private JLabel label6;
    private JLabel label7;
    private JLabel label8;
    private JLabel label9;
    private JLabel label10;
    private JList list1;
    private JPasswordField passwordfield1;
    private JRadioButton radiobutton1;
    private JTextArea textarea1;
    private JTextField textfield1;
    private JPanel panel1;
    private JToggleButton tbutton1;

    //Constructor 
    @SuppressWarnings("unchecked")
    public AlleKomponenten(){

        setTitle("Alle Komponenten");
        setSize(590,410);

        //pane with null layout
        JPanel contentPane = new JPanel(null);
        contentPane.setPreferredSize(new Dimension(590,410));
        contentPane.setBackground(new Color(192,192,192));

        label1 = new JLabel();
        label1.setBounds(10,10,90,35);
        label1.setBackground(new Color(214,217,223));
        label1.setForeground(new Color(0,0,0));
        label1.setEnabled(true);
        label1.setFont(new Font("sansserif",0,12));
        label1.setText("JButton");
        label1.setVisible(true);

        button1 = new JButton();
        button1.setBounds(100,10,90,35);
        button1.setBackground(new Color(214,217,223));
        button1.setForeground(new Color(0,0,0));
        button1.setEnabled(true);
        button1.setFont(new Font("sansserif",0,12));
        button1.setText("Button1");
        button1.setVisible(true);

        label7 = new JLabel();
        label7.setBounds(10,50,90,35);
        label7.setBackground(new Color(214,217,223));
        label7.setForeground(new Color(0,0,0));
        label7.setEnabled(true);
        label7.setFont(new Font("sansserif",0,12));
        label7.setText("JTextFeld");
        label7.setVisible(true);

        textfield1 = new JTextField();
        textfield1.setBounds(100,50,140,35);
        textfield1.setBackground(new Color(255,255,255));
        textfield1.setForeground(new Color(0,0,0));
        textfield1.setEnabled(true);
        textfield1.setFont(new Font("sansserif",0,12));
        textfield1.setText("...");
        textfield1.setVisible(true);

        label6 = new JLabel();
        label6.setBounds(10,90,90,35);
        label6.setBackground(new Color(214,217,223));
        label6.setForeground(new Color(0,0,0));
        label6.setEnabled(true);
        label6.setFont(new Font("sansserif",0,12));
        label6.setText("JPasswordField");
        label6.setVisible(true);

        passwordfield1 = new JPasswordField();
        passwordfield1.setBounds(100,90,140,35);
        passwordfield1.setBackground(new Color(214,217,223));
        passwordfield1.setForeground(new Color(0,0,0));
        passwordfield1.setEnabled(true);
        passwordfield1.setFont(new Font("sansserif",0,12));
        passwordfield1.setText("abc");
        passwordfield1.setVisible(true);

        label4 = new JLabel();
        label4.setBounds(10,130,90,35);
        label4.setBackground(new Color(214,217,223));
        label4.setForeground(new Color(0,0,0));
        label4.setEnabled(true);
        label4.setFont(new Font("sansserif",0,12));
        label4.setText("JTextArea");
        label4.setVisible(true);

        textarea1 = new JTextArea();
        textarea1.setBounds(100,130,140,105);
        textarea1.setBackground(new Color(255,255,255));
        textarea1.setForeground(new Color(0,0,0));
        textarea1.setEnabled(true);
        textarea1.setFont(new Font("sansserif",0,12));
        textarea1.setText("...");
        textarea1.setBorder(BorderFactory.createBevelBorder(1));
        textarea1.setVisible(true);

        label3 = new JLabel();
        label3.setBounds(10,250,90,35);
        label3.setBackground(new Color(214,217,223));
        label3.setForeground(new Color(0,0,0));
        label3.setEnabled(true);
        label3.setFont(new Font("sansserif",0,12));
        label3.setText("JToggleButton");
        label3.setVisible(true);

        /*        drawingPane = new DrawingPane();
        drawingPane.setBounds(100,250,140,105);
        drawingPane.setBackground(new Color(214,217,223));
        drawingPane.setForeground(new Color(0,0,0));
        drawingPane.setEnabled(true);
        drawingPane.setFont(new Font("sansserif",0,12));
        drawingPane.setBorder(BorderFactory.createBevelBorder(1));
        drawingPane.setVisible(true);*/

        JToggleButton tbutton1 = new JToggleButton();
        tbutton1.setBounds(100,250,140,35);
        tbutton1.setBackground(new Color(214,217,223));
        tbutton1.setForeground(new Color(0,0,0));
        tbutton1.setEnabled(true);
        tbutton1.setFont(new Font("sansserif",0,12));
        tbutton1.setText("ToggleButton");
        //tbutton1.setBorder(BorderFactory.createBevelBorder(1));
        tbutton1.setVisible(true);

        label8 = new JLabel();
        label8.setBounds(300,10,90,35);
        label8.setBackground(new Color(214,217,223));
        label8.setForeground(new Color(0,0,0));
        label8.setEnabled(true);
        label8.setFont(new Font("sansserif",0,12));
        label8.setText("JCheckBox");
        label8.setVisible(true);

        checkbox1 = new JCheckBox();
        checkbox1.setBounds(390,10,90,35);
        checkbox1.setBackground(new Color(214,217,223));
        checkbox1.setForeground(new Color(0,0,0));
        checkbox1.setEnabled(true);
        checkbox1.setFont(new Font("sansserif",0,12));
        checkbox1.setText("...");
        checkbox1.setVisible(true);

        label9 = new JLabel();
        label9.setBounds(300,50,90,35);
        label9.setBackground(new Color(214,217,223));
        label9.setForeground(new Color(0,0,0));
        label9.setEnabled(true);
        label9.setFont(new Font("sansserif",0,12));
        label9.setText("JRadioButton");
        label9.setVisible(true);

        radiobutton1 = new JRadioButton();
        radiobutton1.setBounds(390,50,90,35);
        radiobutton1.setBackground(new Color(214,217,223));
        radiobutton1.setForeground(new Color(0,0,0));
        radiobutton1.setEnabled(true);
        radiobutton1.setFont(new Font("sansserif",0,12));
        radiobutton1.setText("...");
        radiobutton1.setVisible(true);

        label2 = new JLabel();
        label2.setBounds(300,90,90,35);
        label2.setBackground(new Color(214,217,223));
        label2.setForeground(new Color(0,0,0));
        label2.setEnabled(true);
        label2.setFont(new Font("sansserif",0,12));
        label2.setText("JComboBox");
        label2.setVisible(true);

        combobox1 = new JComboBox();
        combobox1.setBounds(390,90,90,35);
        combobox1.setBackground(new Color(214,217,223));
        combobox1.setForeground(new Color(0,0,0));
        combobox1.setEnabled(true);
        combobox1.setFont(new Font("sansserif",0,12));
        combobox1.setVisible(true);

        label5 = new JLabel();
        label5.setBounds(300,130,90,35);
        label5.setBackground(new Color(214,217,223));
        label5.setForeground(new Color(0,0,0));
        label5.setEnabled(true);
        label5.setFont(new Font("sansserif",0,12));
        label5.setText("JList");
        label5.setVisible(true);

        list1 = new JList();
        list1.setBounds(390,130,140,105);
        list1.setBackground(new Color(255,255,255));
        list1.setForeground(new Color(0,0,0));
        list1.setEnabled(true);
        list1.setFont(new Font("sansserif",0,12));
        list1.setVisible(true);

        label10 = new JLabel();
        label10.setBounds(300,250,90,35);
        label10.setBackground(new Color(214,217,223));
        label10.setForeground(new Color(0,0,0));
        label10.setEnabled(true);
        label10.setFont(new Font("sansserif",0,12));
        label10.setText("JPanel");
        label10.setVisible(true);

        panel1 = new JPanel(null);
        panel1.setBorder(BorderFactory.createEtchedBorder(1));
        panel1.setBounds(390,250,140,105);
        panel1.setBackground(new Color(214,217,223));
        panel1.setForeground(new Color(0,0,0));
        panel1.setEnabled(true);
        panel1.setFont(new Font("sansserif",0,12));
        panel1.setVisible(true);

        
        
        //adding components to contentPane panel
        contentPane.add(button1);
        contentPane.add(checkbox1);
        contentPane.add(combobox1);
        contentPane.add(tbutton1);
        contentPane.add(label1);
        contentPane.add(label2);
        contentPane.add(label3);
        contentPane.add(label4);
        contentPane.add(label5);
        contentPane.add(label6);
        contentPane.add(label7);
        contentPane.add(list1);
        contentPane.add(label8);
        contentPane.add(label9);
        contentPane.add(passwordfield1);
        contentPane.add(radiobutton1);
        contentPane.add(textarea1);
        contentPane.add(textfield1);
        contentPane.add(panel1);
        contentPane.add(label10);

        //adding panel to JFrame and seting of window position and close operation
        getContentPane().add(contentPane);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        pack();
        setVisible(true);
    }


    public static void main(String[] args){
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    new AlleKomponenten();
                }
            });
    }

}
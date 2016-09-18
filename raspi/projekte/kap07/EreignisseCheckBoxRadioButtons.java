package raspi.projekte.kap07;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class EreignisseCheckBoxRadioButtons extends JFrame {
    private JMenuBar menuBar;
    private JCheckBox checkbox1;
    private JCheckBox checkbox2;
    private JCheckBox checkbox3;
    private JRadioButton radiobutton1;
    private JRadioButton radiobutton2;
    private JRadioButton radiobutton3;
    private JTextArea textarea1;

    //Constructor 
    @SuppressWarnings("unchecked")
    public EreignisseCheckBoxRadioButtons(){

        setTitle("Auf Ereignisse in CheckBoxen und RadioButtons reagieren.");
        setSize(500,350);
        //menu generate method

        //pane with null layout
        JPanel contentPane = new JPanel(null);
        contentPane.setPreferredSize(new Dimension(500,350));
        contentPane.setBackground(new Color(192,192,192));

        checkbox1 = new JCheckBox();
        checkbox1.setBounds(15,12,90,35);
        checkbox1.setBackground(new Color(214,217,223));
        checkbox1.setForeground(new Color(0,0,0));
        checkbox1.setEnabled(true);
        checkbox1.setFont(new Font("sansserif",0,12));
        checkbox1.setText("Auto");
        checkbox1.setVisible(true);
        checkbox1.setActionCommand("check");
        checkbox1.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    action(evt);
                }
            });

        checkbox2 = new JCheckBox();
        checkbox2.setBounds(15,42,90,35);
        checkbox2.setBackground(new Color(214,217,223));
        checkbox2.setForeground(new Color(0,0,0));
        checkbox2.setEnabled(true);
        checkbox2.setFont(new Font("sansserif",0,12));
        checkbox2.setText("Haus");
        checkbox2.setVisible(true);
        checkbox2.setActionCommand("check");
        checkbox2.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    action(evt);
                }
            });

        checkbox3 = new JCheckBox();
        checkbox3.setBounds(15,72,90,35);
        checkbox3.setBackground(new Color(214,217,223));
        checkbox3.setForeground(new Color(0,0,0));
        checkbox3.setEnabled(true);
        checkbox3.setFont(new Font("sansserif",0,12));
        checkbox3.setText("Fahrrad");
        checkbox3.setVisible(true);
        checkbox3.setActionCommand("check");
        checkbox3.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    action(evt);
                }
            });

        radiobutton1 = new JRadioButton();
        radiobutton1.setBounds(15,119,90,35);
        radiobutton1.setBackground(new Color(214,217,223));
        radiobutton1.setForeground(new Color(0,0,0));
        radiobutton1.setEnabled(true);
        radiobutton1.setFont(new Font("sansserif",0,12));
        radiobutton1.setText("10");
        radiobutton1.setVisible(true);
        radiobutton1.setActionCommand("radio");
        radiobutton1.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    action(evt);
                }
            });

        radiobutton2 = new JRadioButton();
        radiobutton2.setBounds(121,119,90,35);
        radiobutton2.setBackground(new Color(214,217,223));
        radiobutton2.setForeground(new Color(0,0,0));
        radiobutton2.setEnabled(true);
        radiobutton2.setFont(new Font("sansserif",0,12));
        radiobutton2.setText("50");
        radiobutton2.setVisible(true);
        radiobutton2.setActionCommand("radio");
        radiobutton2.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    action(evt);
                }
            });

        radiobutton3 = new JRadioButton();
        radiobutton3.setBounds(220,120,90,35);
        radiobutton3.setBackground(new Color(214,217,223));
        radiobutton3.setForeground(new Color(0,0,0));
        radiobutton3.setEnabled(true);
        radiobutton3.setFont(new Font("sansserif",0,12));
        radiobutton3.setText("100");
        radiobutton3.setVisible(true);
        radiobutton3.setActionCommand("radio");
        radiobutton3.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    action(evt);
                }
            });

        ButtonGroup group = new ButtonGroup();
        group.add(radiobutton1);
        group.add(radiobutton2);
        group.add(radiobutton3);

        textarea1 = new JTextArea();
        textarea1.setBounds(13,163,454,162);
        textarea1.setBackground(new Color(255,255,255));
        textarea1.setForeground(new Color(0,0,0));
        textarea1.setEnabled(true);
        textarea1.setFont(new Font("sansserif",0,12));
        textarea1.setText("");
        textarea1.setBorder(BorderFactory.createBevelBorder(1));
        textarea1.setVisible(true);

        //adding components to contentPane panel
        contentPane.add(checkbox1);
        contentPane.add(checkbox2);
        contentPane.add(checkbox3);
        contentPane.add(radiobutton1);
        contentPane.add(radiobutton2);
        contentPane.add(radiobutton3);
        contentPane.add(textarea1);

        //adding panel to JFrame and seting of window position and close operation
        getContentPane().add(contentPane);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        pack();
        setVisible(true);

    }

    private void action (ActionEvent evt) {

        if(evt.getActionCommand().equals("check")){
            JCheckBox jCheckBox =  (JCheckBox) evt.getSource();
            String name = jCheckBox.getText();
            boolean sel = jCheckBox.isSelected();
            textarea1.append(name+"  "+sel+"\n");
        }else{
            JRadioButton jRadioButton =  (JRadioButton) evt.getSource();
            String name = jRadioButton.getText();
            textarea1.append(name+"\n");

        }
    }

    public static void main(String[] args){
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    new EreignisseCheckBoxRadioButtons();
                }
            });
    }

}
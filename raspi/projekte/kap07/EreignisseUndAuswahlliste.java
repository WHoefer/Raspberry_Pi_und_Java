package raspi.projekte.kap07;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class EreignisseUndAuswahlliste extends JFrame {
    private JMenuBar menuBar;
    private JComboBox<String[]> combobox1;
    private JTextArea textarea1;

    //Constructor 
    @SuppressWarnings("unchecked")
    public EreignisseUndAuswahlliste(){

        setTitle("Auf Ereignisse in einer Auswahlliste reagieren.");
        setSize(230,190);
        JPanel contentPane = new JPanel(null);
        contentPane.setPreferredSize(new Dimension(230,190));
        contentPane.setBackground(new Color(192,192,192));

        String[] wochentage = { "Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag" , "Samstag", "Sonntag" };
        combobox1 = new JComboBox(wochentage);
        combobox1.setBounds(19,17,150,35);
        combobox1.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    ActionTextUebernehemen(evt);
                }
            });

        textarea1 = new JTextArea();
        textarea1.setBounds(23,55,150,100);

        contentPane.add(combobox1);
        contentPane.add(textarea1);
        getContentPane().add(contentPane);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        pack();
        setVisible(true);
    }

   
    private void ActionTextUebernehemen (ActionEvent evt) {
        textarea1.append(combobox1.getSelectedItem()+"\n");
    }

    public static void main(String[] args){
        System.setProperty("swing.defaultlaf", "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    new EreignisseUndAuswahlliste();
                }
            });
    }

}
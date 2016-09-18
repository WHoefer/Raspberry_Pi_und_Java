package raspi.projekte.kap07;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;


public class EreignisseUndListe extends JFrame {

    private JMenuBar menuBar;
    private JList list1;
    private JTextField textfield1;
    private ListSelectionModel listSelectionModel;

    //Constructor 
    @SuppressWarnings("unchecked")
    public EreignisseUndListe(){

        setTitle("Auf Ereignisse in einer Liste Reagieren");
        setSize(413,216);

        //pane with null layout
        JPanel contentPane = new JPanel(null);
        contentPane.setPreferredSize(new Dimension(413,216));
        contentPane.setBackground(new Color(192,192,192));

        textfield1 = new JTextField();
        textfield1.setBounds(48,128,315,38);
        textfield1.setBackground(new Color(255,255,255));
        textfield1.setForeground(new Color(0,0,0));
        textfield1.setEnabled(true);
        textfield1.setFont(new Font("sansserif",0,12));
        textfield1.setText("");
        textfield1.setVisible(true);

        //GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        //String[] fonts =  ge.getAvailableFontFamilyNames();

        String[] wochentage = { "Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag" , "Samstag", "Sonntag" };
        list1 = new JList(wochentage);
        //list1.setBounds(48,14,314,100);
        list1.setBackground(new Color(255,255,255));
        list1.setForeground(new Color(0,0,0));
        list1.setEnabled(true);
        list1.setFont(new Font("sansserif",0,12));
        list1.setVisible(true);
        
       

        ListSelectionModel lsm = list1.getSelectionModel();
        lsm.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list1.addListSelectionListener(new ListActionListener(textfield1, wochentage ));

        //adding components to contentPane panel
        JScrollPane scrollPane = new JScrollPane(list1);
        scrollPane.setBounds(48,14,314,100);

        contentPane.add(scrollPane);
        contentPane.add(textfield1);

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
                    new EreignisseUndListe();
                }
            });
    }

}
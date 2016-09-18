package raspi.projekte.kap07;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class EreignisseUndText extends JFrame {
    private JMenuBar menuBar;
    private JButton button1;
    private JTextArea textarea1;
    private JTextField textfield1;

    //Constructor 
    public EreignisseUndText(){

        setTitle("Ereignisse und Text");
        setSize(323,246);
        //Panel erzeugen
        JPanel contentPane = new JPanel(null);
        contentPane.setPreferredSize(new Dimension(323,246));
        contentPane.setBackground(new Color(192,192,192));
        //Farben und Schrift definieren
        Color bg =  new Color(255,255,255);
        Color bgbu =  new Color(100,100,100);
        Color fg =  new Color(0,0,0);
        Font font = new Font(Font.SANS_SERIF,Font.PLAIN,12);
        //Button erzeugen
        button1 = new JButton();
        button1.setBounds(8,16,90,35);
        button1.setBackground(bgbu);
        button1.setForeground(fg);
        button1.setEnabled(true);
        button1.setFont(font);
        button1.setText("Übernehmen");
        button1.setVisible(true);
        //Dem Button wird ein Listener übergeben, der auf Ereignisse horcht.
        button1.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    actionTextUebernehmen(evt);
                }
            });
        //Mehrzeiliges Textfeld erzeugen
        textarea1 = new JTextArea();
        textarea1.setBounds(8,58,285,164);
        /*textarea1.setBackground(bg);
        textarea1.setForeground(fg);
        textarea1.setEnabled(true);
        textarea1.setFont(font);
        textarea1.setText("");
        textarea1.setBorder(BorderFactory.createBevelBorder(1));
        textarea1.setVisible(true);*/
        //Textfeld erzeugen
        textfield1 = new JTextField();
        textfield1.setBounds(105,17,189,35);
        /*textfield1.setBackground(bg);
        textfield1.setForeground(fg);
        textfield1.setEnabled(true);
        textfield1.setFont(font);
        textfield1.setText("");
        textfield1.setVisible(true);*/
        //Alle Elemente dem Panel hinzufügen
        contentPane.add(button1);
        contentPane.add(textarea1);
        contentPane.add(textfield1);

        getContentPane().add(contentPane);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        pack();
        setVisible(true);
    }
    //Wenn der Button aktiviert wurde, wird der Text aus textfield1
    //zu textarea1 hinzugefügt.
    private void actionTextUebernehmen (ActionEvent evt) {
        textarea1.append(textfield1.getText()+"\n");
    }

    public static void main(String[] args){
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    new EreignisseUndText();
                }
            });
    }
}
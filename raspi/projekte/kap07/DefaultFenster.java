package raspi.projekte.kap07;

import java.awt.Dimension;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;


public class DefaultFenster extends JFrame {

	//Constructor 
	@SuppressWarnings("unchecked")
	public DefaultFenster(){

	    //Titel und Größe des Fensters werden gesetzt
		setTitle("Ich bin ein JFrame");
		// Muss gesetzt werden, da sonst die Methode setLocationRelativeTo das Fenster nicht zentriert.
		setSize(300,100);
		//Panle mit Null-Layout wird erzeugt und die bevorzugte größe
		//des und die Hintergrundfarbe des Panels wird eingestell.
		JPanel contentPane = new JPanel(null);
		contentPane.setPreferredSize(new Dimension(300,100));
		contentPane.setBackground(new Color(192,192,192));
		//Label wird initialisiert
		JLabel label1 = new JLabel();
        label1.setBounds(10,35,120,35);
        label1.setBackground(new Color(214,217,223));
        label1.setForeground(new Color(0,0,0));
        label1.setEnabled(true);
        label1.setFont(new Font("sansserif",0,12));
        label1.setText("Ich bin ein JLabel");
        label1.setVisible(true);
		//Textfeld wird initialisiert
		JTextField textfield1  = new JTextField();
        textfield1.setBounds(130,35,140,35);
        textfield1.setBackground(new Color(255,255,255));
        textfield1.setForeground(new Color(0,0,0));
        textfield1.setEnabled(true);
        textfield1.setFont(new Font("sansserif",0,12));
        textfield1.setText("Ich bin ein JTextField");
        textfield1.setVisible(true);
		//Componenten werden hinzugefügt
		contentPane.add(textfield1);
		contentPane.add(label1);
		//Panel wird JFrame hinzugefügt
		getContentPane().add(contentPane);
		//Wenn JFrame geschlossen wird, endet das Programm
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//Da null übergeben wird, wird das Fenster zentriert dargestellt.
		//Es könnten hier auch Komponenten angeben werden, an denen 
		//das Fenster ausgerichtet wird.
		setLocationRelativeTo(null);
		//Das Fenster wird angezeigt
		pack(); //bevorzugte Größe wird eingestellt und alle Unterkomponenten angepasst falls verhanden
		setVisible(true);
	}



	 public static void main(String[] args){
	    // Die GUI läuft in einem eigenen Thread 
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new DefaultFenster();
			}
		});
	}

}
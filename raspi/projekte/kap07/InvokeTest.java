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


public class InvokeTest extends JFrame {

	private JMenuBar menuBar;
	private JButton button1;
	private JTextField textfield1;

	//Constructor 
	public InvokeTest(){

		setTitle("InvokeLater Test");
		setSize(500,400);
		//menu generate method
		generateMenu();
		setJMenuBar(menuBar);

		//pane with null layout
		JPanel contentPane = new JPanel(null);
		contentPane.setPreferredSize(new Dimension(500,400));
		contentPane.setBackground(new Color(192,192,192));


		button1 = new JButton();
		button1.setBounds(41,40,90,35);
		button1.setBackground(new Color(214,217,223));
		button1.setForeground(new Color(0,0,0));
		button1.setEnabled(true);
		button1.setFont(new Font("sansserif",0,12));
		button1.setText("Ereignis");
		button1.setVisible(true);
		//Set action for button click
		//Call defined method
		button1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				actionButton(evt);
			}
		});


		textfield1 = new JTextField();
		textfield1.setBounds(44,84,300,35);
		textfield1.setBackground(new Color(255,255,255));
		textfield1.setForeground(new Color(0,0,0));
		textfield1.setEnabled(true);
		textfield1.setFont(new Font("sansserif",0,12));
		textfield1.setText("Hallo");
		textfield1.setVisible(true);

		//adding components to contentPane panel
		contentPane.add(button1);
		contentPane.add(textfield1);

		//adding panel to JFrame and seting of window position and close operation
		getContentPane().add(contentPane);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		pack();
		setVisible(true);
	}

	//Method actionPerformed for button1
	private void actionButton (ActionEvent evt) {
	 	 textfield1.setText("Textfeld wird direkt beschrieben.");
		 SwingUtilities.invokeLater(
		 new Runnable() {
          public void run() {
            try {
              Thread.sleep(5000); 
            } catch (InterruptedException ex) { }
            textfield1.setText("Und nach 5 Sekunden überschrieben.");
          }
        });
    }
       
	

	//method for generate menu
	public void generateMenu(){
		menuBar = new JMenuBar();

		JMenu file = new JMenu("File");
		JMenu tools = new JMenu("Tools");
		JMenu help = new JMenu("Help");

		JMenuItem open = new JMenuItem("Open   ");
		JMenuItem save = new JMenuItem("Save   ");
		JMenuItem exit = new JMenuItem("Exit   ");
		JMenuItem preferences = new JMenuItem("Preferences   ");
		JMenuItem about = new JMenuItem("About   ");


		file.add(open);
		file.add(save);
		file.addSeparator();
		file.add(exit);
		tools.add(preferences);
		help.add(about);

		menuBar.add(file);
		menuBar.add(tools);
		menuBar.add(help);
	}



	 public static void main(String[] args){
		System.setProperty("swing.defaultlaf", "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new InvokeTest();
			}
		});
	}

}
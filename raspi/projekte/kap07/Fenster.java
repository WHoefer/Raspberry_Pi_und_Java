package raspi.projekte.kap07;

import javax.swing.JFrame;


public class Fenster extends JFrame {


	public Fenster(){
		setSize(300,100);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}



	 public static void main(String[] args){
	     new Fenster();
	}

}
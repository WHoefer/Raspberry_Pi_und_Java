package raspi.projekte.kap07;

import javax.swing.event.*;
import javax.swing.*;

public class ListActionListener  implements ListSelectionListener
{
    
    private  JTextField textfield;
    private  String[] wochentage;
    
    public ListActionListener( JTextField textfield, String[] wochentage )
    {
        this.textfield = textfield;
        this.wochentage = wochentage;
    }

    public void valueChanged(ListSelectionEvent e) { 
        //ListSelectionModel lsm = (ListSelectionModel)e.getSource();
            JList lsm = (JList)e.getSource();
    textfield.setText(wochentage[lsm.getMinSelectionIndex()]);
    }
    
    
    
}

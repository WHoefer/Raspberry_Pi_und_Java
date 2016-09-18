package raspi.projekte.kap13;

import java.awt.*;
import javax.swing.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;
import raspi.schedule.ScheduleUtil;


public class JTempPanel extends JPanel {
    private JLabel jLDay;
    private JLabel jLBis;
    private JLabel jLVon;
    private JTextField jTFBis;
    private JTextField jTFVon;
    public static DateFormat timeFormat = new SimpleDateFormat("HH:mm");

    public JTempPanel(LayoutManager layout, String titel){
        super(layout);
        setSize(500,400);

        Font fontLabel = new Font(Font.DIALOG,Font.PLAIN,12);
        Color fgCol = new Color(0,0,0);
        Color bgCol = new Color(214,217,223);

        jLDay = new JLabel();
        jLDay.setBounds(0,10,90,35);
        jLDay.setBackground(bgCol);
        jLDay.setForeground(fgCol);
        jLDay.setEnabled(true);
        jLDay.setFont(fontLabel);
        jLDay.setText(titel);
        jLDay.setVisible(true);

        jLVon = new JLabel();
        jLVon.setBounds(100,10,40,35);
        jLVon.setBackground(bgCol);
        jLVon.setForeground(fgCol);
        jLVon.setEnabled(true);
        jLVon.setFont(fontLabel);
        jLVon.setText("Von:");
        jLVon.setVisible(true);

        jTFVon = new JTextField();
        jTFVon.setBounds(135,10,60,35);
        jTFVon.setBackground(bgCol);
        jTFVon.setForeground(fgCol);
        jTFVon.setEnabled(true);
        jTFVon.setFont(fontLabel);
        jTFVon.setText("10:00");
        jTFVon.setVisible(true);

        jLBis = new JLabel();
        jLBis.setBounds(200,10,40,35);
        jLBis.setBackground(bgCol);
        jLBis.setForeground(fgCol);
        jLBis.setEnabled(true);
        jLBis.setFont(fontLabel);
        jLBis.setText("Bis:");
        jLBis.setVisible(true);

        jTFBis = new JTextField();
        jTFBis.setBounds(235,10,60,35);
        jTFBis.setBackground(bgCol);
        jTFBis.setForeground(fgCol);
        jTFBis.setEnabled(true);
        jTFBis.setFont(fontLabel);
        jTFBis.setText("23:30");
        jTFBis.setVisible(true);

        this.add(jLDay);
        this.add(jLBis);
        this.add(jLVon);
        this.add(jTFBis);
        this.add(jTFVon);
    }

    public void setTimeInterval(String command){
        jTFVon.setText(ScheduleUtil.getFromTime(command));
        jTFBis.setText(ScheduleUtil.getToTime(command));
    }

    public String getCommandString(String days){
        String retVal = "";
        try{
            StringBuilder startTime =  new StringBuilder();
            startTime.append(jTFVon.getText().substring(0,2));
            startTime.append(jTFVon.getText().substring(3,5));
            startTime.append("00");
            StringBuilder stopTime =  new StringBuilder();
            stopTime.append(jTFBis.getText().substring(0,2));
            stopTime.append(jTFBis.getText().substring(3,5));
            stopTime.append("00");
            retVal = ScheduleUtil.startFromTo(startTime.toString(), stopTime.toString(), days);
        }catch(ParseException pex){
            return "";
        }
        return retVal;
    }    

    public String getPanelText(){
        return String.format("Falsches Format oder Intervall: %n%1$s: Von: %2$s Bis: %3$s", jLDay.getText(), jTFVon.getText(), jTFBis.getText());
    }

    public boolean isValid(){
        boolean retVal = true;
        Date dVon = new Date();
        Date dBis = new Date();
        try{
            if(jTFVon != null){
                String wert =   jTFVon.getText();
                if(wert != null && !wert.isEmpty()){
                    dVon = timeFormat.parse(wert);
                }
            }
            if(jTFBis != null){
                String wert =   jTFBis.getText();
                if(wert != null && !wert.isEmpty()){
                    dBis = timeFormat.parse(wert);
                }
            }
        }catch(ParseException pex){
            retVal = false;
        }
        if(!dVon.before(dBis)){
            return false;
        }
        return retVal;
    }
}
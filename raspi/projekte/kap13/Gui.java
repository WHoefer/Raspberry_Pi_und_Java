package raspi.projekte.kap13;

import javax.swing.UIManager.LookAndFeelInfo;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.border.Border;
import javax.swing.*;
import raspi.logger.JDiagram;
import raspi.logger.DiagramDatePoint;
import raspi.schedule.ScheduleUtil;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.Locale;

public class Gui extends JFrame {
    private JLabel lTagesverlauf;
    private JLabel lTitel;
    private JLabel lTempTag;
    private JLabel lTempNacht;
    private JTextField jTFTag;
    private JTextField jTFNacht;
    private JDiagram diagram1;
    private JTempPanel tempPanelMo;
    private JTempPanel tempPanelDi;
    private JTempPanel tempPanelMi;
    private JTempPanel tempPanelDo;
    private JTempPanel tempPanelFr;
    private JTempPanel tempPanelSa;
    private JTempPanel tempPanelSo;
    private JButton bExit;
    private JButton bSend;
    private List listTag;
    private HttpClient httpClient;
    private Calendar calStopp;
    private int yPos;
    private int yPosTempTag;
    private int yPosTempNacht;
    private int wDiag;
    private int wTempPanel;

    public Gui(){

        httpClient = new HttpClient("http://localhost:8080/daten");
        httpClient.executeGetLogTag();
        List<DiagramDatePoint> listTag =  httpClient.getDiagramDatePoints();
        httpClient.executeGetSchedule();
        List<String> listSchedule = httpClient.getListStrings();
        Map<String, String> headerMap = httpClient.getHeaderMap();

        double temperatur = 0.0d;
        if(listTag != null && listTag.size() > 0){
            temperatur = ((DiagramDatePoint)listTag.get(listTag.size()-1)).getY();
        }

        calStopp = new GregorianCalendar();

        this.setTitle("Heizungssteuerung");
        this.setSize(520,800);
        this.setLocation(0, 0);
        Color bgCol = new Color(214,217,223);
        Color bgColDia = new Color(255,255,255);
        Color fgCol = new Color(0,0,0);
        Font fontDiag = new Font(Font.DIALOG,Font.PLAIN,10);
        Font fontLabel = new Font(Font.DIALOG,Font.PLAIN,12);
        Font fontTitel = new Font(Font.DIALOG,Font.BOLD,16);

        JPanel contentPane = new JPanel(null);
        contentPane.setPreferredSize(new Dimension(1024,520));
        contentPane.setBackground(bgCol);
        yPos = 10;
        wDiag = 970;
        wTempPanel = 300;

        lTitel = new JLabel();
        lTitel.setBounds(20,yPos,550,40);
        lTitel.setBackground(bgCol);
        lTitel.setForeground(fgCol);
        lTitel.setFont(fontTitel);
        lTitel.setText(String.format(Locale.GERMANY,"Die aktuelle Temperatur beträgt %1$.0f °C.", temperatur));
        lTitel.setVisible(true);

        bExit = new JButton();
        bExit.setBounds(580,yPos,90,40);
        bExit.setBackground(bgCol);
        bExit.setForeground(fgCol);
        bExit.setEnabled(true);
        bExit.setFont(fontLabel);
        bExit.setText("Exit");
        bExit.setVisible(true);
        bExit.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    System.exit(0);
                }
            });

        lTagesverlauf = new JLabel();
        lTagesverlauf.setBounds(20,yPos=yPos+40,200,20);
        lTagesverlauf.setBackground(bgCol);
        lTagesverlauf.setForeground(fgCol);
        lTagesverlauf.setFont(fontLabel);
        lTagesverlauf.setText("Tagesverlauf");
        lTagesverlauf.setVisible(true);

        diagram1 = new JDiagram();
        diagram1.setBorder(BorderFactory.createEmptyBorder());
        diagram1.setBounds(20,yPos=yPos+20,wDiag,200);
        diagram1.setBackground(bgColDia);
        diagram1.setForeground(fgCol);
        diagram1.setFont(fontDiag);
        diagram1.setVisible(true);
        diagram1.setXAxisForDateOneDay(calStopp);
        diagram1.setYAxisLinaer(0.0d, 40.0d, 5.0d, 10.0d, 5.0d, "%1$.0f °C");
        diagram1.viewSubgridX(false);
        diagram1.viewSubgridY(false);
        diagram1.add("Tag", Color.black, listTag);

        tempPanelMo = new JTempPanel(null,"Montag");
        tempPanelMo.setBounds(20,yPos= yPos + 240,wTempPanel,35);

        tempPanelFr = new JTempPanel(null, "Freitag");
        tempPanelFr.setBounds(400,yPos,wTempPanel,35);
        yPosTempTag = yPos;

        tempPanelDi = new JTempPanel(null, "Dienstag");
        tempPanelDi.setBounds(20,yPos= yPos + 40,wTempPanel,35);

        tempPanelSa = new JTempPanel(null, "Samstag");
        tempPanelSa.setBounds(400,yPos, wTempPanel,35);
        yPosTempNacht = yPos;

        tempPanelMi = new JTempPanel(null,"Mittwoch");
        tempPanelMi.setBounds(20,yPos= yPos + 40,wTempPanel,35);

        tempPanelSo = new JTempPanel(null, "Sonntag");
        tempPanelSo.setBounds(400,yPos,wTempPanel,35);

        tempPanelDo = new JTempPanel(null, "Donnerstag");
        tempPanelDo.setBounds(20,yPos= yPos + 40,wTempPanel,35);

        bSend = new JButton();
        bSend.setBounds(395,yPos = yPos + 5,100,40);
        bSend.setBackground(bgCol);
        bSend.setForeground(fgCol);
        bSend.setEnabled(true);
        bSend.setFont(fontLabel);
        bSend.setText("Speichern");
        bSend.setVisible(true);
        bSend.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    validateAndSend(evt);
                }
            });

        lTempTag = new JLabel();
        lTempTag.setBounds(740,yPosTempTag,150,35);
        lTempTag.setBackground(bgCol);
        lTempTag.setForeground(fgCol);
        lTempTag.setEnabled(true);
        lTempTag.setFont(fontLabel);
        lTempTag.setText("Temp. Tagbetrieb:");
        lTempTag.setVisible(true);

        jTFTag = new JTextField();
        jTFTag.setBounds(900,yPosTempTag,60,35);
        jTFTag.setBackground(bgCol);
        jTFTag.setForeground(fgCol);
        jTFTag.setEnabled(true);
        jTFTag.setFont(fontLabel);
        jTFTag.setText("20,0");
        jTFTag.setVisible(true);

        lTempNacht = new JLabel();
        lTempNacht.setBounds(740,yPosTempNacht,150,35);
        lTempNacht.setBackground(bgCol);
        lTempNacht.setForeground(fgCol);
        lTempNacht.setEnabled(true);
        lTempNacht.setFont(fontLabel);
        lTempNacht.setText("Temp. Nachtbetrieb:");
        lTempNacht.setVisible(true);

        jTFNacht = new JTextField();
        jTFNacht.setBounds(900,yPosTempNacht,60,35);
        jTFNacht.setBackground(bgCol);
        jTFNacht.setForeground(fgCol);
        jTFNacht.setEnabled(true);
        jTFNacht.setFont(fontLabel);
        jTFNacht.setText("17,0");
        jTFNacht.setVisible(true);

        contentPane.add(lTagesverlauf);
        contentPane.add(lTitel);
        contentPane.add(diagram1);
        contentPane.add(bExit);
        contentPane.add(tempPanelMo);
        contentPane.add(tempPanelDi);
        contentPane.add(tempPanelMi);
        contentPane.add(tempPanelDo);
        contentPane.add(tempPanelFr);
        contentPane.add(tempPanelSa);
        contentPane.add(tempPanelSo);
        contentPane.add(bSend);
        contentPane.add(jTFTag);
        contentPane.add(jTFNacht);
        contentPane.add(lTempTag);
        contentPane.add(lTempNacht);

        this.add(contentPane);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);

        setValues( headerMap, listSchedule);
    }

    private void validateAndSend(ActionEvent evt){
        List<String> list = new ArrayList();
        boolean ok = true;
        if(validate(tempPanelMo)){
            list.add(tempPanelMo.getCommandString("1000000"));
        }else{ ok = false; } 
        if(validate(tempPanelDi)){
            list.add(tempPanelDi.getCommandString("0100000"));
        }else{ ok = false; } 
        if(validate(tempPanelMi)){
            list.add(tempPanelMi.getCommandString("0010000"));
        }else{ ok = false; } 
        if(validate(tempPanelDo)){
            list.add(tempPanelDo.getCommandString("0001000"));
        }else{ ok = false; } 
        if(validate(tempPanelFr)){
            list.add(tempPanelFr.getCommandString("0000100"));
        }else{ ok = false; } 
        if(validate(tempPanelSa)){
            list.add(tempPanelSa.getCommandString("0000010"));
        }else{ ok = false; } 
        if(validate(tempPanelSo)){
            list.add(tempPanelSo.getCommandString("0000001"));
        }else{ ok = false; }

        if(!validateTemp(jTFTag)){ ok = false; }
        if(!validateTemp(jTFNacht)){ ok = false; }

        if(ok){
            Double tag = new Double(jTFTag.getText().replace(",", "."));
            Double nacht = new Double(jTFNacht.getText().replace(",", "."));
            httpClient.executePostSchedule(tag, nacht, list);
            Map<String, String> headerMap = httpClient.getHeaderMap();
            List<String> retList = httpClient.getListStrings();
            if(headerMap.get(HttpHandlerHeizung.ERROR) == null){
                setValues( headerMap, retList);
                JOptionPane.showMessageDialog(this,
                    "Daten wurden erfolgreich übertragen.", "",
                    JOptionPane.PLAIN_MESSAGE);
            }
        }
    }

    private void setValues( Map<String, String> headerMap, List<String> retList){
        Double tag = new Double(headerMap.get(HttpHandlerHeizung.TEMPTAG));
        jTFTag.setText(tag.toString());
        Double nacht = new Double(headerMap.get(HttpHandlerHeizung.TEMPNACHT));
        jTFNacht.setText(nacht.toString());
        Iterator<String> it = retList.iterator();
        while(it.hasNext()){
            String command = it.next();
            if(ScheduleUtil.isMonday(command)){
                tempPanelMo.setTimeInterval(command);
            }
            if(ScheduleUtil.isTuesday(command)){
                tempPanelDi.setTimeInterval(command);
            }
            if(ScheduleUtil.isWednesday(command)){
                tempPanelMi.setTimeInterval(command);
            }
            if(ScheduleUtil.isThursday(command)){
                tempPanelDo.setTimeInterval(command);
            }
            if(ScheduleUtil.isFriday(command)){
                tempPanelFr.setTimeInterval(command);
            }
            if(ScheduleUtil.isSaturday(command)){
                tempPanelSa.setTimeInterval(command);
            }
            if(ScheduleUtil.isSunday(command)){
                tempPanelSo.setTimeInterval(command);
            }
        }
    }

    private boolean validateTemp(JTextField textField){
        boolean retVal = true;
        try{
            Double temp = new Double(textField.getText().replace(",", "."));
        }catch(NumberFormatException nex){
            JOptionPane.showMessageDialog(this,
                "Formatfehler: " + textField.getText(), "Fehler Temperaturangabe!",
                JOptionPane.PLAIN_MESSAGE);
            retVal = false;
        }
        return retVal;
    }

    private boolean validate(JTempPanel tempPanel){
        if(!tempPanel.isValid()){    
            JOptionPane.showMessageDialog(this,
                tempPanel.getPanelText(), "Fehler!",
                JOptionPane.PLAIN_MESSAGE);
            return false;
        }
        return true;
    }

    public static void main(String[] args){
        System.setProperty("swing.defaultlaf", "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    new Gui();
                }
            });
    }

}
package raspi.projekte.kap14;

import javax.swing.UIManager.LookAndFeelInfo;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import raspi.schedule.ScheduleUtil;
import raspi.gui.ComboBoxService;
import raspi.gui.Validator;
import raspi.files.PropertyService;
import java.util.Properties;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

public class Gui extends JFrame {
    private JLabel lTitel;
    private JLabel lAuswahPort;
    private JLabel lTag;
    private JLabel lVon;
    private JLabel lBis;
    private JTextField tBis;
    private JTextField tVon;
    private JButton bExit;
    private JButton bSend;
    private JButton bAnleg;
    private JButton bEntf;
    private JComboBox cbPort;
    private JComboBox cbTag;
    private JList lCommand;
    private JScrollPane scrollPane;
    private DefaultListModel model;

    private List listTag;
    private HttpClient httpClient;
    private GuiUtil guiUtil;
    private ComboBoxService cbService;
    private String gpio;
    private String gpio01;
    private String gpio02;
    private String gpio03;
    private String gpio04;
    private String ip;
    private String port;
    private String root;
    private List<String> listSchedule;
    private Map<String, String> headerMap;

    @SuppressWarnings("unchecked")
    public Gui(){
        ZeitschaltUhrProperties pService = new ZeitschaltUhrProperties("Zeitschaltuhr.properties");
        Properties props = pService.getProperties();
        gpio01 = props.getProperty(HttpHandlerZeitschaltuhr.GPIO01);
        gpio02 = props.getProperty(HttpHandlerZeitschaltuhr.GPIO02);
        gpio03 = props.getProperty(HttpHandlerZeitschaltuhr.GPIO03);
        gpio04 = props.getProperty(HttpHandlerZeitschaltuhr.GPIO04);
        ip = props.getProperty("IP");
        port = props.getProperty("Port");
        root = props.getProperty("Root");

        cbService = new ComboBoxService();
        cbService.addItem(HttpHandlerZeitschaltuhr.GPIO01,gpio01);
        cbService.addItem(HttpHandlerZeitschaltuhr.GPIO02,gpio02);
        cbService.addItem(HttpHandlerZeitschaltuhr.GPIO03,gpio03);
        cbService.addItem(HttpHandlerZeitschaltuhr.GPIO04,gpio04);

        gpio = HttpHandlerZeitschaltuhr.GPIO01;
        httpClient = new HttpClient("http://" + ip + ":" + port + "/" + root);
        requestServer(gpio);

        this.setTitle("Client: Zeitschaltuhr");
        this.setSize(800,500);
        this.setLocation(0, 0);
        Color bgCol = new Color(214,217,223);
        Color bgColList = new Color(255,255,255);
        Color fgCol = new Color(0,0,0);
        Font fontDiag = new Font(Font.DIALOG,Font.PLAIN,10);
        Font fontLabel = new Font(Font.DIALOG,Font.PLAIN,12);
        Font fontTitel = new Font(Font.DIALOG,Font.BOLD,16);
        Font fontList = new Font(Font.MONOSPACED,Font.BOLD,12);

        JPanel contentPane = new JPanel(null);
        contentPane.setPreferredSize(new Dimension(700,450));
        contentPane.setBackground(bgCol);

        lTitel = new JLabel();
        GuiUtil.init(lTitel,20,10,160,40, fontTitel, fgCol, bgCol);
        lTitel.setText("Zeitschaltuhr");

        lTag = new JLabel();
        GuiUtil.init(lTag,20,100,90,35, fontLabel, fgCol, bgCol);
        lTag.setText("Wochentag");

        lVon = new JLabel();
        GuiUtil.init(lVon,320,150,40,35, fontLabel, fgCol, bgCol);
        lVon.setText("Von:");

        tVon = new JTextField();
        GuiUtil.init(tVon,350,150,90,35, fontLabel, fgCol, bgCol);
        tVon.setText("");

        lBis = new JLabel();
        GuiUtil.init(lBis,450,150,40,35, fontLabel, fgCol, bgCol);
        lBis.setText("Bis:");

        tBis = new JTextField();
        GuiUtil.init(tBis,475,150,90,35, fontLabel, fgCol, bgCol);
        tBis.setText("");

        lAuswahPort = new JLabel();
        GuiUtil.init(lAuswahPort,20,52,90,35, fontLabel, fgCol, bgCol);
        lAuswahPort.setText("Steckdose");

        cbTag = new JComboBox(GuiUtil.wochentage);
        GuiUtil.init(cbTag,110,100,200,35, fontLabel, fgCol, bgCol);

        cbPort = new JComboBox(cbService.getItems());
        cbPort.setSelectedItem(gpio01);
        GuiUtil.init(cbPort,110,50,200,35, fontLabel, fgCol, bgCol);
        cbPort.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    String item = (String)cbPort.getSelectedItem();
                    gpio = cbService.getKey(item);
                    requestServer(gpio);
                    setValues();
                }
            });

        guiUtil = new GuiUtil(tVon, tBis, cbTag);      

        lCommand = new JList();
        model = new DefaultListModel();
        GuiUtil.init(lCommand,0,0,290,200, fontList, fgCol, bgColList);
        lCommand.setModel(model);
        scrollPane = new JScrollPane(lCommand);  
        scrollPane.setBounds(20,150,290,212);

        bExit = new JButton();
        GuiUtil.init(bExit,160,380,120,35, fontLabel, fgCol, bgCol);
        bExit.setText("Exit");
        bExit.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    System.exit(0);
                }
            });

        bAnleg = new JButton();
        GuiUtil.init(bAnleg ,320,200,120,35, fontLabel, fgCol, bgCol);
        bAnleg.setText("Anlegen");
        bAnleg.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    if(isValidFormat()){
                        model.addElement(guiUtil.getListElement());
                    }
                }
            });

        bEntf = new JButton();
        GuiUtil.init(bEntf ,450,200,115,35, fontLabel, fgCol, bgCol);
        bEntf.setText("Entfernen");
        bEntf.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    int index = lCommand.getSelectedIndex();
                    if(index >= 0){
                        model.remove(index);
                    }
                }
            });

        bSend = new JButton();
        GuiUtil.init(bSend ,20,380,120,35, fontLabel, fgCol, bgCol);
        bSend.setText("Speichern");
        bSend.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    send(evt);
                }
            });

        contentPane.add(lTitel);
        contentPane.add(bExit);
        contentPane.add(lAuswahPort);
        contentPane.add(cbPort);
        contentPane.add(lTag);
        contentPane.add(cbTag);
        contentPane.add(lVon);
        contentPane.add(tVon);
        contentPane.add(lBis);
        contentPane.add(tBis);
        contentPane.add(bAnleg);
        contentPane.add(scrollPane);
        contentPane.add(bEntf);
        contentPane.add(bSend);

        this.add(contentPane);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);

        setValues();
    }

    @SuppressWarnings("unchecked")
    private void send(ActionEvent evt){
        List<String> list = new ArrayList();
        Map<String, String> headerMap;
        Object[] commands = model.toArray();
        for(int i = 0; i < commands.length; i++){
            String listElement = (String)commands[i];
            String commandString = GuiUtil.getCommandString(listElement);
            list.add(commandString);
        }
        String selectedItem =  (String)cbPort.getSelectedItem();
        httpClient.executePostSchedule(cbService.getKey(selectedItem), list);
        headerMap = httpClient.getHeaderMap();
        listSchedule = httpClient.getListStrings();
        if(headerMap.get(HttpHandlerZeitschaltuhr.ERROR) == null){            
            if(listSchedule != null && !listSchedule.isEmpty()){
                setValues();
                JOptionPane.showMessageDialog(this,
                    "Daten wurden erfolgreich übertragen.", "",
                    JOptionPane.PLAIN_MESSAGE);
            }
        }else{
            String infoServer = headerMap.get(HttpHandlerZeitschaltuhr.ERROR);
            JOptionPane.showMessageDialog(this,
                infoServer, "Info Server!",
                JOptionPane.PLAIN_MESSAGE);
        }             

    }

    private void requestServer(String gpio){
        httpClient.executeGetSchedule(gpio);
        listSchedule = httpClient.getListStrings();
        headerMap = httpClient.getHeaderMap();
    }   

    @SuppressWarnings("unchecked")
    private void setValues(){
        model.clear();
        Iterator<String> it = listSchedule.iterator();
        while(it.hasNext()){
            String commandString = it.next();
            model.addElement(GuiUtil.getListElement(commandString));
        }

    }

    public boolean isValidFormat(){
        boolean retVal = true;
        if(!Validator.validateShortTimeInterval(tVon, tBis)){
            retVal = false;
            JOptionPane.showMessageDialog(this,
                "Format- oder Zeitfehler in der Von-/Bis-Zeit!", "Fehler!",
                JOptionPane.PLAIN_MESSAGE);
        }
        return retVal;
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
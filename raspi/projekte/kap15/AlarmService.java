package raspi.projekte.kap15;

import raspi.mail.MailService;
import raspi.mail.EMail;
import java.util.Properties;
import java.util.List;
import java.util.Iterator;
import java.util.Date;

public class AlarmService
{

    public static void sendMessageAlarmOn(Properties props){
        MailService.sendMimeMessageWithTLS(
            props.getProperty("mail.smtp.port"), 
            props.getProperty("mail.smtp.host"), 
            props.getProperty("mail.smtp.username"), 
            props.getProperty("mail.smtp.password"), 
            props.getProperty("mail.smtp.from"), 
            props.getProperty("mail.smtp.to"),  
            props.getProperty("mail.smtp.subjectOn"),  
            String.format("%1$s%n%2$td.%2$tm.%2$tY %2$tH:%2$tM:%2$tS", "", new Date()), 
            (new Boolean(props.getProperty("mail.smtp.debug"))).booleanValue());
    }

    public static void sendMessageAlarmOff(Properties props){
        MailService.sendMimeMessageWithTLS(
            props.getProperty("mail.smtp.port"), 
            props.getProperty("mail.smtp.host"), 
            props.getProperty("mail.smtp.username"), 
            props.getProperty("mail.smtp.password"), 
            props.getProperty("mail.smtp.from"), 
            props.getProperty("mail.smtp.to"),  
            props.getProperty("mail.smtp.subjectOff"),  
            String.format("%1$s%n%2$td.%2$tm.%2$tY %2$tH:%2$tM:%2$tS", "", new Date()), 
            (new Boolean(props.getProperty("mail.smtp.debug"))).booleanValue());
    }
    
    public static void sendMessage(Properties props){
        MailService.sendMimeMessageWithTLS(
            props.getProperty("mail.smtp.port"), 
            props.getProperty("mail.smtp.host"), 
            props.getProperty("mail.smtp.username"), 
            props.getProperty("mail.smtp.password"), 
            props.getProperty("mail.smtp.from"), 
            props.getProperty("mail.smtp.to"),  
            props.getProperty("mail.smtp.subject"),  
            String.format("%1$s%n%2$td.%2$tm.%2$tY %2$tH:%2$tM:%2$tS", props.getProperty("mail.smtp.text"), new Date()), 
            (new Boolean(props.getProperty("mail.smtp.debug"))).booleanValue());
    }

    public static List<EMail> getMessages(Properties props){
        return  MailService.getEMailsPop3TLS(
            props.getProperty("mail.pop3.port"), 
            props.getProperty("mail.pop3.host"), 
            props.getProperty("mail.pop3.username"), 
            props.getProperty("mail.pop3.password"), 
            props.getProperty("mail.pop3.filter"), 
            (new Boolean(props.getProperty("mail.pop3.delete"))).booleanValue(),
            (new Boolean(props.getProperty("mail.pop3.debug"))).booleanValue());
    }   

    public static boolean isAlarmOnOff(Properties props, int loop ){
        boolean change = false;
        Date date = null;
        String text = "";
        List<EMail> list = getMessages(props);
        if(list != null && !list.isEmpty()){
            for(Iterator<EMail> it = list.iterator(); it.hasNext();){
                EMail email = it.next();
                if(date == null){
                    text = email.getText();
                    date = email.getReceivedDate();
                }
                if(date.before(email.getReceivedDate())){
                    date = email.getReceivedDate();
                    text = email.getText();
                }
            }
            if(loop == Alarm.ALARMSLEEP && text.matches("[\r\f\t\n.]*" + props.getProperty("mail.message.on") + "[\r\f\t\n.]*")){
                change = true;
                sendMessageAlarmOn(props);
            }
            if(loop == Alarm.ALARMACTIVE && text.matches("[\r\f\t\n.]*" + props.getProperty("mail.message.off") + "[\r\f\t\n.]*")){
                change = true;
                sendMessageAlarmOff(props);
            }

        }
        return change;
    }
}
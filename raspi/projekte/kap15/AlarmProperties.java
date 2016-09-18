package raspi.projekte.kap15;

import raspi.files.PropertyService;
import java.util.Properties;

public class AlarmProperties extends PropertyService
{
     public AlarmProperties(String name)
    {
      super(name);
    }
    
    @Override
     public void setDefaultProperties(Properties props){
        props.setProperty("mail.smtp.port","587");
        props.setProperty("mail.smtp.host","smtp.web.de");
        props.setProperty("mail.smtp.username","xxxxx");
        props.setProperty("mail.smtp.password","xxxxx");
        props.setProperty("mail.smtp.from","xxxxx@web.de");
        props.setProperty("mail.smtp.to","xxxxxx@xxx.xx");
        props.setProperty("mail.smtp.subject","!!!Alarm!!!");
        props.setProperty("mail.smtp.subjectOn","!!!Alarm eingeschaltet!!!");
        props.setProperty("mail.smtp.subjectOff","!!!Alarm ausgeschaltet!!!");
        props.setProperty("mail.smtp.text","Es wurde ein Alarm ausgelöst.");
        props.setProperty("mail.smtp.debug","false");
        props.setProperty("mail.pop3.port","995");
        props.setProperty("mail.pop3.host","pop.web.de");
        props.setProperty("mail.pop3.username","xxxxxxx");
        props.setProperty("mail.pop3.password","xxxxxxx");
        props.setProperty("mail.pop3.filter","Alarm");
        props.setProperty("mail.pop3.delete","true");
        props.setProperty("mail.pop3.debug","false");  
        props.setProperty("mail.message.on","On");
        props.setProperty("mail.message.off","Off");
        props.setProperty("mailcheck","20");
        props.setProperty("beforeStart","1");
    }
}

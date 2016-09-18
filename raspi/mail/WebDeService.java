package raspi.mail;

import java.util.List;
import java.util.Iterator;
/**
 * WebDeService ist eine Service-Klasse für web.de
 * 
 * @author W. Höfer
 * @version 1.0
 */
public class WebDeService
{
    public static void sendWebDeMimeMessageWithTLS(final String username, final String password, String from, String to , String subject, String text, boolean debug){
        MailService.sendMimeMessageWithTLS("587", "smtp.web.de", username, password, from, to , subject, text, debug);
    }

    public static void sendWebDeMultipartMessageWithTLS(final String username, final String password, String from, String to , String subject, String text, String[] files, boolean debug){
        MailService.sendMultipartMessageWithTLS("587", "smtp.web.de", username, password, from, to , subject, text, files, debug);
    }

    public static List<EMail> getEMailsPop3TLS(final String username, final String password, String filter, boolean delete, boolean debug){
        return  MailService.getEMailsPop3TLS("995", "pop.web.de", username,  password, filter, delete, debug);
    }   
    
}

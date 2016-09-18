package raspi.mail;

import javax.mail.Message;
import javax.mail.MessagingException;
import java.util.List;
/**
 * GmailService ist eine Servcie-Klasse gmail.com
 * 
 * @author W.Höfer
 * @version 1.0
 */
public class GmailService
{
    public static void sendGMailMimeMessageWithTLS(final String username, final String password, String from, String to , String subject, String text, boolean debug){
        MailService.sendMimeMessageWithTLS("587", "smtp.gmail.com", username, password, from, to , subject, text, debug);
    }

    public static void sendGMailMultipartMessageWithTLS(final String username, final String password, String from, String to , String subject, String text, String[] files, boolean debug){
        MailService.sendMultipartMessageWithTLS("587", "smtp.gmail.com", username, password, from, to , subject, text, files, debug);
    }

    public static List<EMail> getEMailsPop3TLS(final String username, final String password, String filter, boolean delete, boolean debug){
        return  MailService.getEMailsPop3TLS("995", "pop.gmail.com", username,  password, filter, delete, debug);
    }   

}

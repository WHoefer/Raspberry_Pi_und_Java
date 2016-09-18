package raspi.mail;

import javax.mail.*;
import javax.activation.*;
import javax.mail.internet.*;
import java.util.Properties;
import java.util.Date;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;
import java.io.IOException;

/**
 * MailService ist ein Service um E-Mails über einen SMTP-Server zu senden und
 * über einen POP3-Server zu empfangen.
 * 
 * @author W. Höfer
 * @version 1.0
 */
public class MailService
{
    /**
     * getTLSSession erzeugt eine TLS-Session. 
     *
     * @param port Port
     * @param host Host des E-Mail Providers
     * @param username Benutzername des E-Mail-Kontos
     * @param password Kennwort des E-Mail-Kontos
     * @param debug Debug ein- oder ausschalten
     * @return The return value
     */
    public static Session getTLSSession(String port, String host, final String username, final String password, boolean debug){

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
        session.setDebug(debug);
        return session;
    }

    /**
     * getMimeMessage erzeugt eine MimeMessage. 
     *
     * @param session E-Mail Session
     * @param from E-MAil-Adresse des Senders
     * @param to E-Mail-Adresse des Empfängers
     * @param subject Betreff
     * @param text E-Mail-Text
     * @return The return value
     */
    public static Message getMimeMessage(Session session, String from, String to , String subject, String text){
        Message message = null;
        try {
            message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO,	InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(text);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        return message;
    }

    /**
     * getMultipartMessage erzeugt eine MultiPart Session.
     *
     * @param session E-Mail Session
     * @param from E-MAil-Adresse des Senders
     * @param to E-Mail-Adresse des Empfängers
     * @param subject Betreff
     * @param text E-Mail-Text
     * @param files Array mit den Pfaden der Dateianhängen
     * @return The return value
     */
    public static Message getMultipartMessage(Session session, String from, String to , String subject, String text, String[] files){
        Message message = null;
        BodyPart messageBodyPart = null;
        Multipart multipart = null;
        try {
            message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO,	InternetAddress.parse(to));
            message.setSubject(subject);

            messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(text);
            multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);

            for(int i = 0; i < files.length; i++){
                messageBodyPart = new MimeBodyPart();
                String filename = files[i];
                DataSource source = new FileDataSource(filename);
                messageBodyPart.setDataHandler(new DataHandler(source));
                messageBodyPart.setFileName(filename);
                multipart.addBodyPart(messageBodyPart);
            }

            message.setContent(multipart);            
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        return message;
    }

    /**
     * sendMimeMessageWithTLS sendet eine E-Mail ohne Dateianhang.
     *
     * @param port Portnummer des SMTP-Servers
     * @param host SMTP-Server
     * @param username Benutzername
     * @param password Kennwort
     * @param from Absender
     * @param to Empfänger
     * @param subject Betreff
     * @param text E-Mail-Text
     * @param debug Setzt den Debug-Mode
     */
    public static void sendMimeMessageWithTLS(String port, String host, final String username, final String password, String from, String to , String subject, String text, boolean debug){
        try{
            Session session = getTLSSession(port, host, username, password, debug);
            Message message = getMimeMessage(session, from, to , subject, text);
            Transport.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * sendMultipartMessageWithTLS sendet eine E-Mail mit Dateianhang.
     *
     * @param port Portnummer des SMTP-Servers
     * @param host SMTP-Server
     * @param username Benutzername
     * @param password Kennwort
     * @param from Absender
     * @param to Empfänger
     * @param subject Betreff
     * @param text E-Mail-Text
     * @param files Array mit Dateinamen und absolutem Pfad
     * @param debug Setzt den Debug-Mode
     */
    public static void sendMultipartMessageWithTLS(String port, String host, final String username, final String password, String from, String to , String subject, String text, String[] files, boolean debug){
        try{
            Session session = getTLSSession(port, host, username, password, debug);
            Message message = getMultipartMessage(session, from, to , subject, text, files);
            Transport.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * getMailsFromMessages gibt eien Liste mit EMail-Objekten zurück. Die E-Mails werden aus dem 
     * Message-Array gelsen. Mit dem Parameter filter gibt man an, dass nur die E-Mails in die Liste 
     * aufganommen werden, deren Betreff mit filter übereinstimmt beziehungsweise der Inhalt von filter
     * im Betreff enthalten sein muss.
     * Mit dem Parameter delete kann angegeben werden, dass die E-Mails, die durch den Filter gelesen wurden,
     * zum löschen auf dem SMTP-Server markiert werden.
     *
     * @param messages Array mit Message-Objekten aus dem dei E-Mails gelesen werden.
     * @param filter Filter für den Betreff der E-Mails.
     * @param delte A paramete
     * @return The return value
     */
    public static List<EMail> getMailsFromMessages(Message[] messages, String filter, boolean delte){
        List<EMail> mails = new ArrayList<EMail>();
        try{
            for (int i = 0; i < messages.length; i++) {
                Message message = messages[i];
                Address[] fromAddress = message.getFrom();
                String subject = message.getSubject();
                if(subject.matches(filter)){
                    String from = "";
                    if(fromAddress.length > 0){
                        from = fromAddress[0].toString();
                    }
                    String contentType = message.getContentType();
                    String text = "";
                    if(contentType.startsWith("multipart")){
                        Multipart content = (Multipart)message.getContent();
                        BodyPart body = content.getBodyPart(0);
                        text = body.getContent().toString();
                    }else{
                        text = message.getContent().toString();
                    }    
                    String description = message.getDescription();
                    int messageNumber = message.getMessageNumber();
                    Date receivedDate = message.getSentDate();
                    mails.add(new  EMail(subject, from, text, contentType, description, messageNumber, receivedDate ));
                    if(delte){
                        message.setFlag(Flags.Flag.DELETED, true);
                    }
                }
            }
        }catch (MessagingException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }
        return mails;
    }

    /**
     * getEMailsPop3TLS holt E-Mails von einem POP3-Server ab. Mit dem Parameter filter werden nur die EMails geholt, bei denen
     * die Betreffzeile zum Filter passt. filter ist ein regulärer Ausdruck.
     *
     * @param port Port des POP3-Servers
     * @param host POP3-Server
     * @param username Benutzername
     * @param password Kennwort
     * @param filter Filter für die zu holenden E-Mails
     * @param delete Setzt das Löschen-Flag für die geholte E-Mail
     * @param debug Mit treu wird der Debug-Modus eingeschaltet.
     * @return Liste mit EMail-Objekten
     */
    public static List<EMail> getEMailsPop3TLS(String port, String host, final String username, final String password, String filter, boolean delete, boolean debug){
        List<EMail> mails = new ArrayList<EMail>();
        Message[] messages = {};
        Properties props = new Properties();
        props.put("mail.store.protocol", "pop3");
        props.put("mail.pop3s.starttls.enable", "true");
        props.put("mail.pop3s.host", host);
        props.put("mail.pop3s.port", port);
        try{
            Session session = Session.getDefaultInstance(props);
            session.setDebug(debug);
            Store store = session.getStore("pop3s");
            store.connect(host, username, password);
            Folder folder = store.getFolder("INBOX");
            if(delete){
                folder.open(Folder.READ_WRITE);
            }else{    
                folder.open(Folder.READ_ONLY);
            }
            messages = folder.getMessages();
            mails = getMailsFromMessages(messages, filter, delete);
            if(delete){
                folder.close(true);
            }else{
                folder.close(false);
            }
            store.close();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return mails;
    }
    
}

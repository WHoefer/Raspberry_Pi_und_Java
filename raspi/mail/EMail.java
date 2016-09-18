package raspi.mail;

import java.util.Date;

/**
 * EMail ist eine Container-Kasse für E-Mails.
 * 
 * @author W. Höfer
 * @version 1.0
 */
public class EMail
{
    private String subject;
    private String from;
    private String text;
    private String contentType;
    private String description;
    private int messageNumber;
    private Date receivedDate;

    /**
     * Constructor for objects of class EMail
     */
    public EMail(String subject, String from, String text, String contentType,
    String description, int messageNumber, Date receivedDate)

    {
        this.subject = subject;
        this.from = from;
        this.text = text;
        this.contentType = contentType;
        this.description = description;
        this.messageNumber = messageNumber;
        this.receivedDate = receivedDate;
    }

    public String getSubject(){
        return subject;
    }    
    public String getFrom(){
        return from;
    }    
    public String getText(){
        return text;
    }    
    public String getCcontentType(){
        return contentType;
    }    
    public String getDescription(){
        return description;
    }    
    public int getMessageNumber(){
        return messageNumber;
    }    
    public Date getReceivedDate(){
        return receivedDate;
    }    
}

package helper;

import com.google.common.base.Joiner;
import com.sun.xml.internal.org.jvnet.mimepull.MIMEMessage;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class EmailUtil {

    private static final String FROM_EMAIL="aamitranjan007@gmail.com";
    private static final String SMTP_HOST="smtp.gmail.com";
    private static final String EMAIL_TYPE="text/html;charset=utf-8";
    private static final String SMTP_PORT="587";




    public static void sendEmail(String to,String subject,String body,String attachment){
        Map<String,String> mailConfig=new HashMap<>();
        mailConfig.put("from",FROM_EMAIL);
        mailConfig.put("to",to);
        mailConfig.put("host",SMTP_HOST);
        mailConfig.put("subject",subject);
        sendMail(mailConfig,new String[]{body},attachment.split(";"));
    }

    private static void sendMail(Map<String,String> config,String[] msgs,String[] attachments){
        MimeMessage message=initMessage(config);
        composeMail(message,msgs,attachments);
        sendMail(message);
    }

    private static void sendMail(MimeMessage message){
        try{
            Transport.send(message);
        }catch (MessagingException e){
            e.printStackTrace();
        }
    }

    private static MimeMessage composeMail(MimeMessage message,String[] msgs,String[] attachments){
        Multipart attach;
        try{
            attach=addAttachments(attachments,msgs);
            message.setContent(attach);
        }catch(Exception e){
            e.printStackTrace();
        }
        return message;
    }

    private static Multipart addAttachments(String[] fileNames,String[] msgs){
        Multipart multipart=new MimeMultipart("mixed");
        addBodyMessages(multipart,msgs);
        for(int i=0;fileNames!=null && i<fileNames.length;i++){
            String fileName=fileNames[i];
            addAttachment(multipart,fileName);
        }
        return multipart;
    }
    private static void addAttachment(Multipart multipart,String fileName){
        DataSource source=new FileDataSource(fileName);
        BodyPart messageBodyPart=new MimeBodyPart();
        try{
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(new File(fileName).getName());
            multipart.addBodyPart(messageBodyPart);
        }catch (MessagingException e){
            e.printStackTrace();
        }

    }
    private static void addBodyMessages(Multipart multipart,String[] msgs){
        BodyPart messageBodyPart=new MimeBodyPart();
        try{
           String bodyMessage=Joiner.on("\n").join(msgs);
           messageBodyPart.setContent(bodyMessage,EMAIL_TYPE);
           multipart.addBodyPart(messageBodyPart);
        }catch (MessagingException e){
            e.printStackTrace();
        }

    }

    private static MimeMessage initMessage(Map<String,String> config){
      MimeMessage message=initMessage(config.get("host"),config.get("from"),
              config.get("to"),config.get("subject"));
      return message;
    }

    private static MimeMessage initMessage(String host,String from,String to,String subject){
        Session session=getSession(host);
        MimeMessage message=new MimeMessage(session);
        try{
            message.setFrom(new InternetAddress(from));
            message.addRecipients(Message.RecipientType.TO,to);
            message.setSubject(subject);
        }catch (AddressException e){
            e.printStackTrace();
        }catch (MessagingException e){
            e.printStackTrace();
        }
        return message;
    }

    private static Session getSession(String host){
        Properties properties=System.getProperties();
        properties.put("mail.smtp.auth","true");// if not gmail-then "false"
        properties.put("mail.smtp.starttls.enable","true"); // if not gmail-then "false"
        properties.setProperty("mail.smtp.host",host);
        properties.setProperty("mail.transport.protocol","smtp");
        properties.put("mail.smtp.port",SMTP_PORT);
        //Session session=Session.getDefaultInstance(properties); // -- if not gmail
        //For Gmail
        /*https://www.google.com/settings/security/lesssecureapps
        3. Turn "Allow less secure apps: OFF" to "Allow less secure apps: ON"*/
        Session session=Session.getDefaultInstance(properties,new javax.mail.Authenticator(){
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(
                        "aamitranjan007@gmail.com", "1A##8is004#");// Specify the Username and the PassWord
            }
        });
        return session;
    }

    public static String getReportFooter(){
        return "<html><body><h3>Please <a href=overview-features.html>Click Here</a> for detailed report</h3>"
                +"</body></html>";
    }
}

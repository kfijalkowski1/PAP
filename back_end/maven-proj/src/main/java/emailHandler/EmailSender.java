package emailHandler;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.lang.*;



public class EmailSender {

    private static final Logger logger = LogManager.getLogger(EmailSender.class);
    public static Session prepareSession() {
        String host = "smtp.gmail.com";

        Properties properties = System.getProperties();

        properties.put("mail.smtp.ssl.protocols", "TLSv1.2");
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.socketFactory.port", "587");
        properties.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.starttls.enable", true);
        properties.put("mail.smtp.auth", true);
        properties.put("mail.smtp.ssl.trust", "*");

        Session session = Session.getInstance(properties,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("pap22zim.z27@gmail.com", "wkwbnxmbteissgdc");
                    }
                });

//        session.setDebug(true);
        return session;
    }

    private static void sendEmail(String login, String text, String subject) {
        String from = "pap22zim.z27@gmail.com";

        String email = getEmail.run(login);

        if (email != null && !email.equals("") && !email.equals("null")) {
            try {
                MimeMessage message = new MimeMessage(prepareSession());
                message.setFrom(new InternetAddress(from));
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
                message.setSubject(subject);
                String msg = text;
                message.setText(msg);

                System.out.println("sending...");
                // Send message

                new Thread(new Runnable(){
                    public void run(){
                        try {
                            Transport.send(message);
                        } catch (MessagingException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }).start();


                System.out.println("Sent message successfully....");
            } catch (MessagingException mex) {
                mex.printStackTrace();
            }
        }
    }

    public static void registrationConfirm(String login) {
        logger.info("Sending registration confirm");
        String subject = "Registration confirmation";
        String msg = "Hi " + login + ",\n" +
                "You have successfully register on our website.\n" +
                "Automatically generated, do not reply.";
        sendEmail(login, msg, subject);

    }

    public static void sendReportLog(String login, String userMessage) {

        String from = "pap22zim.z27@gmail.com";

        try {
            MimeMessage message = new MimeMessage(prepareSession());
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(from));
            message.setSubject("User report: " + login);

            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(userMessage);

            MimeBodyPart attachmentPart = new MimeBodyPart();
            attachmentPart.attachFile(new File("logs/current.log"));

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            multipart.addBodyPart(attachmentPart);

            message.setContent(multipart);
            Transport.send(message);

            System.out.println("sending...");
            // Send message
            Transport.send(message);
            System.out.println("Sent message successfully....");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void exchangeConfirm(String login) {

        logger.info("Sending exchange confirm");
        String subject = "Exchange confirmation";
        String msg = "Hi " + login + ",\n" +
                "You have successfully exchanged groups.\n" +
                "Automatically generated, do not reply.";
        sendEmail(login, msg, subject);

    }

    public static void passwordChangeConfirm(String login) {

        logger.info("Sending exchange confirm");
        String subject = "Password changed";
        String msg = "Hi " + login + ",\n" +
                "You have successfully changed your password.\n" +
                "Automatically generated, do not reply.";
        sendEmail(login, msg, subject);
    }
}


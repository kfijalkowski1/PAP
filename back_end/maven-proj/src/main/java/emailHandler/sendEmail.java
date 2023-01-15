package emailHandler;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class sendEmail {

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

        session.setDebug(true);
        return session;
    }

    public static void registrationConfirm(String login, String email) {
        String from = "pap22zim.z27@gmail.com";

        try {
            MimeMessage message = new MimeMessage(prepareSession());
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            message.setSubject("Registration confirmation");
            String msg = "Hi " + login + ",\n" +
                         "You have successfully register on our website.\n" +
                         "Automatically generated, do not reply.";
            message.setText(msg);

            System.out.println("sending...");
            // Send message
            Transport.send(message);
            System.out.println("Sent message successfully....");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
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

    public static void exchangeConfirm(String login, String email) {
        String from = "pap22zim.z27@gmail.com";

        try {
            MimeMessage message = new MimeMessage(prepareSession());
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            message.setSubject("Exchange confirmation");
            String msg = "Hi " + login + ",\n" +
                    "You have successfully exchanged groups.\n" +
                    "Automatically generated, do not reply.";
            message.setText(msg);

            System.out.println("sending...");
            // Send message
            Transport.send(message);
            System.out.println("Sent message successfully....");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }

    public static void passwordChangeConfirm(String email, String login) {
        String from = "pap22zim.z27@gmail.com";

        try {
            MimeMessage message = new MimeMessage(prepareSession());
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            message.setSubject("Password changed");
            String msg = "Hi " + login + ",\n" +
                    "You have already changed your password.\n" +
                    "Automatically generated, do not reply.";
            message.setText(msg);

            System.out.println("sending...");
            // Send message
            Transport.send(message);
            System.out.println("Sent message successfully....");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }
}


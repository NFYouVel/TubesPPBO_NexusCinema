package tubes.utils;

import java.util.Properties;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public class EmailSender {

    public static void sendEmail(String to, String otpCode) {

        final String senderEmail = "marvelnathanael072006@gmail.com";
        final String senderPassword = "wcwu ovur irxe dina";
        final String senderName = "Nexus Cinema Project";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        try {
            Message msg = new MimeMessage(session);

            // set from name
            msg.setFrom(new InternetAddress(senderEmail, senderName));

            msg.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            msg.setSubject("Thank you for your purchase! Here is your ticket code. Enjoy the movie!");
            msg.setText("Here is your code: " + otpCode);

            Transport.send(msg);

            System.out.println("Email sent successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Testing
    // public static void main(String[] args) {
    //     // sendEmail(
    //     //         "someone@gmail.com",
    //     //         "Your Ticket Code",
    //     //         "Here is your code: 123456"
    //     // );
    // }
}

package it.chalmers.commit.notification;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;

public class EmailNotifier implements NotificationObserver {

    private final String sender;
    private final String password;
    private final String recipient;

    public EmailNotifier(String sender, String password, String recipient) {
        this.sender = sender;
        this.password = password;
        this.recipient = recipient;
    }


    @Override
    public void notify(String subjectString, String messageString) {

        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(properties,
            new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(sender, password);
                }
            });

        try {
            Message email = new MimeMessage(session);

            email.setFrom(new InternetAddress(sender));
            email.setRecipients(
                Message.RecipientType.TO,
                InternetAddress.parse(recipient)
            );

            email.setSubject(subjectString);
            email.setText(messageString);

            Transport.send(email);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.williapv.grades.gradesscraper;

import java.util.Date;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author paul
 */
public class Emailer {
    
    private static final String MAIL_USERNAME_KEY = "mail.smtp.username";
    private static final String MAIL_PASSWORD_KEY = "mail.smtp.password";
    private static final String MAIL_HOST_KEY = "mail.smtp.host";
    
    private Properties properties;
    
    public Emailer(Properties props)
    {
        this.properties = props;
    }
    
    public void sendEmail(String subject, String message)
    {
        // SUBSTITUTE YOUR EMAIL ADDRESSES HERE!!!
        String to = properties.getProperty(MAIL_USERNAME_KEY);
        String from = to;
        // SUBSTITUTE YOUR ISP'S MAIL SERVER HERE!!!
        
        
        Authenticator authenticator = new Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {
                String username = properties.getProperty(MAIL_USERNAME_KEY);
                String password = properties.getProperty(MAIL_PASSWORD_KEY);
                PasswordAuthentication authentication = new PasswordAuthentication(username, password);
                return authentication;
            }
        };

        Session session = Session.getInstance(properties, authenticator);

        try {
            // Instantiatee a message
            Message msg = new MimeMessage(session);
            //Set message attributes
            msg.setFrom(new InternetAddress(from));
            InternetAddress[] address = {new InternetAddress(to)};
            msg.setRecipients(Message.RecipientType.TO, address);
            msg.setSubject(subject);
            msg.setSentDate(new Date());
            // Set message content
            msg.setContent(message,"text/html");
            //Send the message
            Transport.send(msg);
        } catch (MessagingException mex) {
            // Prints all nested (chained) exceptions as well
            mex.printStackTrace();
        }
        
    }
    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.williapv.grades.gradescraper;

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
 * Utility class for emailing
 * @author paul
 */
public class Emailer {
    
    private static final String MAIL_USERNAME_KEY = "mail.smtp.username";
    private static final String MAIL_PASSWORD_KEY = "mail.smtp.password";
    private static final String MAIL_TO = "scraper.mail.to";
    private static final String MAIL_FROM = "scraper.mail.from";
    
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
            msg.setFrom(new InternetAddress(properties.getProperty(MAIL_FROM)));
            String mailTo = properties.getProperty(MAIL_TO);
            if(properties.getProperty(MAIL_TO) == null)
            {
                System.err.println("Error in email addresses " + mailTo);
            }
            String[] mailToRecipients = mailTo.split(",");
            InternetAddress[] addresses = new InternetAddress[mailToRecipients.length];
            for(int idx = 0; idx < mailToRecipients.length; idx++)
            {
                addresses[idx] = new InternetAddress(mailToRecipients[idx]);
            }
            msg.setRecipients(Message.RecipientType.TO, addresses);
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

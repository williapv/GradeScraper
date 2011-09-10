package com.williapv.grades.gradescraper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Properties;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

/**
 * Scrapes grades from https://stihome.stpsb.org
 * and emails them to you
 *
 */
public class ScrapeGrades {

    /**
     * Run the app.  No args needed
     * @param args 
     */
    public static void main(String[] args) {
        //get user home directory
        String userHome = System.getProperty("user.home");

        //list out gradescraper directory for .properties files
        File homeDir = new File(userHome + File.separator + ".gradescraper");
        if(!homeDir.exists())
        {
            System.err.println("[Error] You must create a directory in your "
                    + "home directory called .gradescraper.  Properties files go "
                    + "here that set the applicaiton up for the specific school.");
            System.exit(-1);
        }
        File[] propertiesFiles = homeDir.listFiles(new PropertiesFileFilter());

        for (int idx = 0; idx < propertiesFiles.length; idx++) {
            Properties props = new Properties();
            try {
                props.load(new FileInputStream(propertiesFiles[idx]));
                getAndEmailGrades(props);
            } catch (IOException ex) {
                Logger.getLogger(ScrapeGrades.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Get the grades from the application and email
     * them to you.  Pass in a properties file.  The file is 
     * originally picked up from ~/.gradescraper/*.properties
     * @param props 
     */
    private static void getAndEmailGrades(Properties props) {
        HttpClient client = new HttpClient();
        getPage("https://stihome.stpsb.org/login.asp?stname=Louisiana&disname=ST&code=52&selschool=" + props.getProperty("scraper.school.code") + "&submit1=Submit", client);
        NameValuePair[] loginData = {
            new NameValuePair("intPin", props.getProperty("scraper.student.pin")),
            new NameValuePair("idNum", props.getProperty("scraper.student.id")),
            new NameValuePair("Radio", "Student")
        };
        postPage(loginData, "https://stihome.stpsb.org/transitPass1.asp", client);
        getPage("https://stihome.stpsb.org/student/stuWelcome.asp", client);
        String grades = getPage("https://stihome.stpsb.org/student/stuClassAveWithletter.asp", client);
        Emailer email = new Emailer(props);
        email.sendEmail(props.getProperty("scraper.mail.subject"), grades);

    }

    /**
     * Login to the site
     * @param data
     * @param url
     * @param client
     * @return 
     */
    private static String postPage(NameValuePair[] data, String url, HttpClient client) {
        String returnString = null;
        // Create a method instance.
        PostMethod method = new PostMethod(url);
        //method.setFollowRedirects(true);
        method.setRequestBody(data);
        // Provide custom retry handler is necessary
        method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
                new DefaultHttpMethodRetryHandler(3, false));

        try {
            // Execute the method.
            int statusCode = client.executeMethod(method);
            if (statusCode != HttpStatus.SC_OK) {
                System.err.println("Method failed: " + method.getStatusLine());
            }
            // Read the response body.
            byte[] responseBody = method.getResponseBody();
            returnString = new String(responseBody);
        } catch (HttpException e) {
            System.err.println("Fatal protocol violation: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Fatal transport error: " + e.getMessage());
        } finally {
            // Release the connection.
            method.releaseConnection();
        }
        return returnString;
    }

    /**
     * Get a generic page
     * @param url
     * @param client
     * @return 
     */
    private static String getPage(String url, HttpClient client) {
        // Create a method instance.
        GetMethod method = new GetMethod(url);
        String returnString = null;
        // Provide custom retry handler is necessary
        method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
                new DefaultHttpMethodRetryHandler(3, false));

         try {
            // Execute the method.
            int statusCode = client.executeMethod(method);

            if (statusCode != HttpStatus.SC_OK) {
                System.err.println("Method failed: " + method.getStatusLine());
            }

            // Read the response body.
            byte[] responseBody = method.getResponseBody();

            // Deal with the response.
            // Use caution: ensure correct character encoding and is not binary data
            returnString = new String(responseBody);

        } catch (HttpException e) {
            System.err.println("Fatal protocol violation: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Fatal transport error: " + e.getMessage());
        } finally {
            // Release the connection.
            method.releaseConnection();
        }
        return returnString;

    }
}

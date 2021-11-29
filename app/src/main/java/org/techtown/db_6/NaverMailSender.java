package org.techtown.db_6;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;

public class NaverMailSender extends javax.mail.Authenticator{


    private Authenticator auth;
    private MimeMessage msg;
    private Session session;

    public NaverMailSender() {

        Properties props = new Properties();

        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.naver.com");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "587");

        this.auth = new MyAuthentication();
        this.session = Session.getDefaultInstance(props, auth);
        this.msg = new MimeMessage(session);

    }

    public synchronized void sendMail(String title, String content, String recipients) throws Exception {


          try {
            msg.setSentDate(new Date());
            InternetAddress from = new InternetAddress();

            from = new InternetAddress("ColorfulCardApp<podojom@naver.com>");
            msg.setFrom(from);

            InternetAddress to = new InternetAddress(recipients);
            msg.setRecipient(Message.RecipientType.TO, to);

            msg.setSubject(title, "UTF-8");
            msg.setText(content, "UTF-8");
            msg.setHeader("content-Type", "text/html");

            javax.mail.Transport.send(msg);
            Log.d("tag","전송함");

        } catch (AddressException addr_e){
            addr_e.printStackTrace();
        } catch (MessagingException msg_e){
            msg_e.printStackTrace();
        }

    }

    class MyAuthentication extends Authenticator {

        PasswordAuthentication account;

        public MyAuthentication(){
            String id = "podojom";
            String pw = "yeon21912091";
            account = new PasswordAuthentication(id, pw);
        }

        public PasswordAuthentication getPasswordAuthentication(){
            return account;
        }
    }
}

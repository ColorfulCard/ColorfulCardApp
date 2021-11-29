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

public class GmailSender extends javax.mail.Authenticator{

 //   private String mailhost = "smtp.gmail.com";
    private String mailhost = "smtp.naver.com";

    private String user;
    private String password;
    private Session session;
    private String emailCode;

    public GmailSender() {
    //    this.user = "yeon0038@gmail.com";
   //     this.password = "yeon21912091";

          this.user = "podojom";
      this.password = "yeon21912091";
        emailCode = createEmailCode();
        Properties props = new Properties();

        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.naver.com");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "587");

    /*    props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.host", mailhost);
        props.put("mail.smtp.auth", "true");
     //   props.put("mail.smtp.port", "465");
           props.put("mail.smtp.port", "587");
      //  props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.port", "587");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.quitwait", "false");
        //구글에서 지원하는 smtp 정보를 받아와 MimeMessage 객체에 전달해준다.*/

        Authenticator auth = new MyAuthentication();
      //  session = Session.getDefaultInstance(props, this);
        Session session = Session.getDefaultInstance(props, auth);
        MimeMessage msg = new MimeMessage(session);


        try {
            msg.setSentDate(new Date());
            InternetAddress from = new InternetAddress();

            from = new InternetAddress("sender<podojom@naver.com>");
            msg.setFrom(from);

            InternetAddress to = new InternetAddress("dpswpfgkxm30@naver.com");
            msg.setRecipient(Message.RecipientType.TO, to);

            msg.setSubject("title", "UTF-8");
            msg.setText("content", "UTF-8");
            msg.setHeader("content-Type", "text/html");

            javax.mail.Transport.send(msg);

            Log.d("tag","전송함");
        } catch (AddressException addr_e){
            addr_e.printStackTrace();
        } catch (MessagingException msg_e){
            msg_e.printStackTrace();
        }
    }


    public String getEmailCode() { return emailCode; } //생성된 이메일 인증코드 반환
    private String createEmailCode() {
        //이메일 인증코드 생성
        String[] str = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
        String newCode = new String();

        for (int x = 0; x < 8; x++) {
            int random = (int) (Math.random() * str.length);
            newCode += str[random];
        }
        return newCode;
    }
    protected PasswordAuthentication getPasswordAuthentication() {
        //해당 메서드에서 사용자의 계정(id & password)을 받아 인증받으며 인증 실패시 기본값으로 반환됨.
        return new PasswordAuthentication(user, password);
    }

    public synchronized void sendMail(String subject, String body, String recipients) throws Exception {

        MimeMessage message = new MimeMessage(session);

        DataHandler handler = new DataHandler(new ByteArrayDataSource(body.getBytes(), "text/plain"));
        //본문 내용을 byte단위로 쪼개어 전달

        message.setSender(new InternetAddress(user));//본인 이메일 설정
        Log.d("tag","발신인 설정");
        message.setSubject(subject);//해당 이메일의 본문 설정
        Log.d("tag","본문 설정");
        message.setDataHandler(handler);
        if (recipients.indexOf(',') > 0)
            message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(recipients));
        else message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipients));
        Transport.send(message);
        Log.d("tag","메세지발송됨");
    }

    public class ByteArrayDataSource implements DataSource {
        private byte[] data;
        private String type;

        public ByteArrayDataSource(byte[] data, String type) {
            super();
            this.data = data;
            this.type = type;

        }
        public void setType(String type) {
            this.type = type;
        }
        public String getContentType() {
            if (type == null)
                return "application/octet-stream";
            else return type;
        }
        public InputStream getInputStream() throws IOException {
            return new ByteArrayInputStream(data);
        }
        public String getName() {
            return "ByteArrayDataSource";
        }
        public OutputStream getOutputStream() throws IOException {
            throw new IOException("Not Supported");
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

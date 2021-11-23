package org.techtown.db_6;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;



public class MainActivity extends AppCompatActivity {


    private MainHandler handler;
    static final int MSG_SUCCESS =1;
    static final int MSG_FAIL =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty);
        handler = new MainHandler();
        MailSendingThread thread = new MailSendingThread();
        thread.start();

    }
    class MailSendingThread extends Thread{
        @Override
        public void run()
        {
            Message message=handler.obtainMessage(); //메인스레드 핸들러의 메시지 객체 가져오기
            try{
                GmailSender gMailSender = new GmailSender("yeon0038@gmail.com", "yeon21912091");
                Log.d("tag","sender Make");
                gMailSender.sendMail("제목입니다", "내용입니다", "podojom@naver.com");

                message.what= MSG_SUCCESS;
                handler.sendMessage(message);

            }catch (SendFailedException e) {

                message.what= MSG_FAIL;
                handler.sendMessage(message);

            } catch (MessagingException e) { Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해주십시오", Toast.LENGTH_SHORT).show();
            } catch (Exception e) { e.printStackTrace(); }

        }

    }

    class MainHandler extends Handler {
        @Override
        public void handleMessage(Message message){
            switch(message.what)
            {

                case MSG_SUCCESS:
                    Toast.makeText(getApplicationContext(), "이메일을 성공적으로 보냈습니다.", Toast.LENGTH_SHORT).show();
                    break;
                case MSG_FAIL:
                    Toast.makeText(getApplicationContext(), "이메일 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

}






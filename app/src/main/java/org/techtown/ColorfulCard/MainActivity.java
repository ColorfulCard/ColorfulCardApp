package org.techtown.ColorfulCard;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import javax.mail.MessagingException;
import javax.mail.SendFailedException;


public class MainActivity extends AppCompatActivity {


    private MainHandler handler;

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
                NaverMailSender gMailSender = new NaverMailSender();
                Log.d("tag","sender Make");
                gMailSender.sendMail("제목입니다", "내용입니다", "podojom@naver.com");

                message.what= StateSet.MailMsg.MSG_SUCCESS;
                handler.sendMessage(message);

            }catch (SendFailedException e) {

                message.what= StateSet.MailMsg.MSG_FAIL;
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

                case StateSet.MailMsg.MSG_SUCCESS:
                    Toast.makeText(getApplicationContext(), "이메일을 성공적으로 보냈습니다.", Toast.LENGTH_SHORT).show();
                    break;
                case StateSet.MailMsg.MSG_FAIL:
                    Toast.makeText(getApplicationContext(), "이메일 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

}






package org.techtown.db_6;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;



public class LoadingActivity extends AppCompatActivity {

    private Intent intent;
    private UserCard user; //사용자 클래스
    private BalanceCheckThread thread;
    private MainHandler handler;
    private TextView loading;


    @Override
    protected  void onCreate(Bundle savedInstanceState) {

        intent = getIntent();
        user = (UserCard) intent.getSerializableExtra("user");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        loading = findViewById(R.id.loading3);
        loading.setVisibility(View.VISIBLE);

        handler = new MainHandler();
        thread=new BalanceCheckThread();
        thread.start();



    }


    class BalanceCheckThread extends Thread{

        @Override
        public void run()
        {
            Message message=handler.obtainMessage(); //메인스레드 핸들러의 메시지 객체 가져오기

            try {

                for(Card card : user.getCards()) {

                    String cardNum = card.getCardNum();
                    BalanceCheck checker = new BalanceCheck(cardNum.substring(0, 4), cardNum.substring(4, 8), cardNum.substring(8, 12), cardNum.substring(12));

                    if (checker.tryBalanceCheck(1).equals("success")) {
                        String[] balances = checker.getAllBalanceAttributes();
                        user.setCardBalances(balances); //스레드 안에서 유저정보 업데이트함

                    } else {
                        //  message.what = MSG_FAIL;
                        //  handler.sendMessage(message);
                    }
                }
            } catch (IOException e) {
                message.what = StateSet.LoadingMsg.MSG_FAIL; //메시지 아이디 설정
                handler.sendMessage(message); //메인스레드 핸들러로 메시지 보내기
                e.printStackTrace();
            }

            message.what = StateSet.LoadingMsg.MSG_SUCCESS_BALCHECK; //메시지 아이디 설정. 반복문 다 돌면서 fail 한 번도 안났으니 성공함
            handler.sendMessage(message); //메인스레드 핸들러로 메시지 보내기
        }


    }

    class MainHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            switch (message.what) {
                case StateSet.LoadingMsg.MSG_SUCCESS_BALCHECK:
                    Intent intent = new Intent(LoadingActivity.this, UserCardListActivity.class);
                    intent.putExtra("user", user);
                    startActivity(intent);
                    overridePendingTransition(R.anim.none,R.anim.none);
                    finish();
                    break;
                case StateSet.LoadingMsg.MSG_FAIL:
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoadingActivity.this);
                    AlertDialog dialog = builder.setMessage("네트워크가 원활하지 않습니다. 네트워크 상태를 확인하십시오").setPositiveButton("확인", null).create();
                    dialog.show();
                    break;
            }
        }
    }

}

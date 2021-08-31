package org.techtown.db_6;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeActivity extends AppCompatActivity {

    final int MSG_SUCCESS = 1;
    final int MSG_FAIL=0;

    Intent intent;
    User user; //사용자 클래스
    MainHandler handler; //별것도아닌게 그지같은 스레드자식들
    //어째서 푸시가안되나 다시 해볼려고 주석달아본다
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        intent = getIntent();
        user = (User) intent.getSerializableExtra("user");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        TextView nameView=(TextView)findViewById(R.id.nameView);
        Button button3 = (Button) findViewById(R.id.button3);
        nameView.setText(user.getName()+"님");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://sw-env.eba-weppawy7.ap-northeast-2.elasticbeanstalk.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        RetrofitService service1 = retrofit.create(RetrofitService.class);
        Call<List<UserCard>> call = service1.getUserCardList(user.getId());
        call.enqueue(new Callback<List<UserCard>>() {

            @Override
            public void onResponse(Call<List<UserCard>> call, Response<List<UserCard>> response) {

                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "카드정보 불러오기 성공", Toast.LENGTH_SHORT).show();
                    List<UserCard> result = response.body();
                    for(int i = 0; i<result.size(); i++)
                    {
                        UserCard card = result.get(i);
                        user.addCard(card);
                    }

                } else {
                    Log.d("tag", "실패");
                }

                for(UserCard card: user.getCards())
                {
                    Log.d("tag","사용자카드"+card+"\n");
                }
            }

            @Override
            public void onFailure(Call<List<UserCard>> call, Throwable t) {
                Log.d("tag", "실패2" + t.getMessage());
            }

        });


        handler = new MainHandler();

        //"등록된 카드" 버튼을 눌렀을 때
        button3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                BalanceCheckThread thread= new BalanceCheckThread();  //카드정보 크롤링해오는 스레드 실행
                thread.start();
            }

        });


    }

    class BalanceCheckThread extends Thread{

        @Override
        public void run()
        {
            Message message=handler.obtainMessage(); //메인스레드 핸들러의 메시지 객체 가져오기
            //문자열16자리 4자리씩 끊는거 해야함
            for(UserCard card : user.getCards()){

                String cardNum = card.getCardNum();
                BalanceCheck checker = new BalanceCheck (cardNum.substring(0,4) , cardNum.substring(4,8), cardNum.substring(8,12), cardNum.substring(12));

                // BalanceCheck checker = new BalanceCheck("8010","0430","0078","6344");

                try {
                    if(checker.tryBalanceCheck().equals("success"))
                    {
                        String[] balances =checker.getAllBalanceAttributes();
                        String totalBalance = checker.getTotalBalance();

                        user.setCardBalances(balances,totalBalance); //스레드 안에서 유저정보 업데이트함

                        for( String amount : balances ) {
                            Log.d("tag", amount+" ");
                        }
                        Log.d("tag",totalBalance);

                    }
                    else
                    {
                        message.what = MSG_FAIL;
                        handler.sendMessage(message);
                    }
                } catch (IOException e) {
                    message.what = MSG_FAIL; //메시지 아이디 설정
                    handler.sendMessage(message); //메인스레드 핸들러로 메시지 보내기
                    e.printStackTrace();
                }

            }

            message.what = MSG_SUCCESS; //메시지 아이디 설정. 반복문 다 돌면서 fail 한 번도 안났으니 성공함
            handler.sendMessage(message); //메인스레드 핸들러로 메시지 보내기
        }

    }
    //핸들러 안에서 전달받은 메시지에 따라 화면전환 or 다이얼로그 띄우기 처리함
    class MainHandler extends Handler{
        @Override
        public void handleMessage(Message message){
            switch(message.what)
            {
                case MSG_SUCCESS:
                    Intent intent = new Intent(HomeActivity.this,UserCardList2.class);
                    intent.putExtra("user",user);
                    startActivity(intent);
                    finish();
                    break;
                case MSG_FAIL:
                    AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                    AlertDialog dialog = builder.setMessage("네트워크가 원활하지 않습니다. 네트워크 상태를 확인하십시오").setPositiveButton("확인", null).create();
                    dialog.show();
                    break;

            }
        }
    }


    @Override
    public void onBackPressed(){ //뒤로가기 버튼 누르면 종료
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("애플리케이션을 종료하시겠습니까?");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // finish();
                finishAffinity();
                System.runFinalization();
                System.exit(0);
            }
        });
        builder.setNegativeButton("취소",null);
        builder.show();
    }

}
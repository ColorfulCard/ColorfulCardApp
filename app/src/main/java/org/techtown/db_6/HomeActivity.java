package org.techtown.db_6;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    private BackKeyHandler backKeyHandler= new BackKeyHandler(this);

   private Intent intent;
   private UserCard user; //사용자 클래스
   private MainHandler handler;
   private List<ArrayList<MemberStore>> memberStores = new ArrayList<ArrayList<MemberStore>>(3);
   private TextView WelcomeNameView;
   private Button RegiCardBt;
   private Button SearchStoreBt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        intent = getIntent();
        user = (UserCard) intent.getSerializableExtra("user");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        TextView WelcomeNameView= findViewById(R.id.nameView);
        Button RegiCardBt = findViewById(R.id.button3); //등록된 카드 버튼
        Button SearchStoreBt = findViewById(R.id.button4); //가맹점 찾기 버튼

        WelcomeNameView.setText(user.getName()+"님, 환영합니다");

        Server server = new Server();
        RetrofitService service1 = server.getRetrofitService();
        Call<List<Card>> call = service1.getUserCardList(user.getId());
        call.enqueue(new Callback<List<Card>>() {

            @Override
            public void onResponse(Call<List<Card>> call, Response<List<Card>> response) {

                if (response.isSuccessful()) {

                    if(response.body().isEmpty()) //등록된 카드 없는 상태
                        Log.d("tag",response.body().toString());
                    else{
                     //   Toast.makeText(getApplicationContext(), "등록 카드정보 불러오기 성공", Toast.LENGTH_SHORT).show();
                        List<Card> result = response.body();
                        user.setCard(result);

                        int i=0;
                        for(Card card: user.getCards())
                        {
                            Log.d("tag","사용자카드"+i+"번째"+card+"\n");
                            i++;
                        }
                    }

                } else {
                    Log.d("tag", "실패");
                }


            }

            @Override
            public void onFailure(Call<List<Card>> call, Throwable t) {
                Log.d("tag", "실패2" + t.getMessage());
            }

        });


        handler = new MainHandler();

        //"등록된 카드" 버튼을 눌렀을 때
        RegiCardBt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(), "로딩중", Toast.LENGTH_SHORT).show();
                if(user.getCards().isEmpty()) {
                    Intent intent = new Intent(HomeActivity.this, UserCardListActivity.class);
                    intent.putExtra("user", user);
                    startActivity(intent);
                 //   finish();
                }
                else {
                    //BalanceCheckThread thread = new BalanceCheckThread();  //카드정보 크롤링해오는 스레드 실행
                    //thread.start();

                    Intent intent = new Intent(HomeActivity.this, LoadingActivity.class);
                    intent.putExtra("user", user);
                    startActivity(intent);

                }
            }

        });

        //가맹점 찾기 버튼을 누를 때
        SearchStoreBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetMemberStoreThread thread = new GetMemberStoreThread();
                thread.start();
            }
        });

    }


 /*   class BalanceCheckThread extends Thread{

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
                message.what = MSG_FAIL; //메시지 아이디 설정
                handler.sendMessage(message); //메인스레드 핸들러로 메시지 보내기
                e.printStackTrace();
            }

            message.what = MSG_SUCCESS_BALCHECK; //메시지 아이디 설정. 반복문 다 돌면서 fail 한 번도 안났으니 성공함
            handler.sendMessage(message); //메인스레드 핸들러로 메시지 보내기
        }




    }*/




    class GetMemberStoreThread extends Thread{
        @Override
        public void run() {
            final String[] type = {"급식", "부식", "교육"};

            Message message = handler.obtainMessage(); //메인스레드 핸들러의 메시지 객체 가져오기


            Server server = new Server();
            RetrofitService service = server.getRetrofitService();
            for (int i = 0; i < 3; i++) {

                Call<List<MemberStore>> call = service.getStorebyType(type[i]);
                Log.d("tag", type[i]+"가맹점GET시도");

                int finalI = i;
                call.enqueue(new Callback<List<MemberStore>>() {

                    @Override
                    public void onResponse(Call<List<MemberStore>> call, Response<List<MemberStore>> response) {

                        if (response.isSuccessful()) {

                            Log.d("tag", type[finalI]+"가맹점GET성공");
                            //System.out.println(response.body().toString());
                            memberStores.add((ArrayList<MemberStore>) response.body());
                        } else {
                            Log.d("tag", type[finalI]+"가맹점GET실패");
                        }

                    }

                    @Override
                    public void onFailure(Call<List<MemberStore>> call, Throwable t) {
                        Log.d("tag", type[finalI]+"가맹점GET실패2"+t);
                    }
                });

            }

            while(true){  //정보 다 받아올때까지 기다리기 뇌피셜 야매코드인데 걱정된다.
                if(memberStores.size()>2)
                {
                    message.what = StateSet.HomeMsg.MSG_SUCCESS_GETSTORE; //메시지 아이디 설정
                    handler.sendMessage(message); //메인스레드 핸들러로 메시지 보내기
                    break;
                }
                else
                    ;
            }
        }
        
        
    }
    //핸들러 안에서 전달받은 메시지에 따라 화면전환 or 다이얼로그 띄우기 처리함
    class MainHandler extends Handler{
        @Override
        public void handleMessage(Message message){
            switch(message.what)
            {
               /* case MSG_SUCCESS_BALCHECK:
                    Intent intent = new Intent(HomeActivity.this, UserCardListActivity.class);
                    intent.putExtra("user",user);
                    startActivity(intent);
                    break;*/
                case StateSet.HomeMsg.MSG_SUCCESS_GETSTORE:
                    Intent intent2 = new Intent(HomeActivity.this,MapActivity.class);

                    for(int i=0;i<3;i++){  //빨리들어오는 순대로 리스트에 추가되기 때문에,,, 순서에 따른 타입 체크카 필요함.
                       String type = memberStores.get(i).get(0).getStore_type();
                       if(type.equals("교육")){
                           intent2.putParcelableArrayListExtra("eduMemberStores", memberStores.get(i));
                       }
                       else if(type.equals("부식")){
                           intent2.putParcelableArrayListExtra("sideMealMemberStores", memberStores.get(i));
                       }
                       else{
                           intent2.putParcelableArrayListExtra("mealMemberStores", memberStores.get(i));
                       }
                    }
                    startActivity(intent2);
                    break;
                case StateSet.HomeMsg.MSG_FAIL:
                    AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                    AlertDialog dialog = builder.setMessage("네트워크가 원활하지 않습니다. 네트워크 상태를 확인하십시오").setPositiveButton("확인", null).create();
                    dialog.show();
                    break;

            }
        }
    }


    @Override
    public void onBackPressed() { //뒤로가기 버튼 누르면 종료
        backKeyHandler.onBackPressed();
    }

}
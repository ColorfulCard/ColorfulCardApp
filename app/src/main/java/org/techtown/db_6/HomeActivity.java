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

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    private BackKeyHandler backKeyHandler= new BackKeyHandler(this);
    private Intent intent;
    public static UserCard user; //사용자 클래스
    private MainHandler handler;
    private List<MemberStore> allStores= new ArrayList<>();
    private List<Integer> userFavorStoreID = new ArrayList<>();
    private ArrayList<MemberStore> mealMemberStore = new ArrayList<>();
    private ArrayList<MemberStore> sideMealMemberStore = new ArrayList<>();
    private ArrayList<MemberStore> eduMemberStore = new ArrayList<>();
    private ArrayList<MemberStore> favorMemberStore = new ArrayList<>();

    private TextView WelcomeNameView;
    private Button regiCardBt;      //카드등록 버튼
    private Button findStoreBt;  //가맹점 조회버튼
    private Button BoardBt;   //소통게시판버튼


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        intent = getIntent();
        user = (UserCard) intent.getSerializableExtra("user");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        WelcomeNameView = findViewById(R.id.nameView);
        regiCardBt = findViewById(R.id.button3); //등록된 카드 버튼
        findStoreBt = findViewById(R.id.button4); //가맹점 찾기 버튼
        BoardBt = findViewById(R.id.button5); //소통게시판 버튼

        WelcomeNameView.setText(user.getName() + "님, 환영합니다");

        Server server = new Server();
        RetrofitService service1 = server.getRetrofitService();
        Call<List<Card>> call = service1.getUserCardList(user.getId());
        call.enqueue(new Callback<List<Card>>() {

            @Override
            public void onResponse(Call<List<Card>> call, Response<List<Card>> response) {

                if (response.isSuccessful()) {

                    if (response.body().isEmpty()) //등록된 카드 없는 상태
                        Log.d("tag", response.body().toString());
                    else {
                        List<Card> result = response.body();
                        user.setCard(result);
                    }

                }
            }

            @Override
            public void onFailure(Call<List<Card>> call, Throwable t) {
                Log.d("tag", "실패2" + t.getMessage());
            }

        });


        handler = new MainHandler();

        //"등록된 카드" 버튼을 눌렀을 때
        regiCardBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (user.getCards().isEmpty()) {
                    Intent intent = new Intent(HomeActivity.this, UserCardListActivity.class);
                    intent.putExtra("user", user);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(HomeActivity.this, LoadingActivity.class);
                    intent.putExtra("user", user);
                    startActivity(intent);
                }
            }

        });

        //가맹점 찾기 버튼을 누를 때
        findStoreBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GetFavoriteStoreThread thread = new GetFavoriteStoreThread();
                thread.start();     //즐겨찾기 목록 다 get하고 나면 가맹점 목록 다 들고온다.

            }
        });


        BoardBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(HomeActivity.this, BoardActivity.class);
                startActivity(intent);

            }
        });

    }

    class GetFavoriteStoreThread extends Thread{

        @Override
        public void run() {

            Message message = handler.obtainMessage();

            Server server = new Server();
            RetrofitService service1 = server.getRetrofitService();
            Call<List<Integer>> call= service1.getFavoriteStore(user.getId());

            call.enqueue(new Callback<List<Integer>>() {
                @Override
                public void onResponse(Call<List<Integer>> call, Response<List<Integer>> response) {
                    if (response.isSuccessful()) {

                        userFavorStoreID = response.body();
                        message.what = StateSet.HomeMsg.MSG_SUCCESS_GETFAVORSTORE;
                        handler.sendMessage(message);
                    }
                }
                @Override
                public void onFailure(Call<List<Integer>> call, Throwable t) {
                    message.what = StateSet.HomeMsg.MSG_FAIL;
                }
            });


        }
    }
    class GetMemberStoreThread extends Thread{
        @Override
        public void run() {

            Message message = handler.obtainMessage(); //메인스레드 핸들러의 메시지 객체 가져오기

            Server server = new Server();

            RetrofitService service1= server.getRetrofitService();
            Call<List<MemberStore>> call = service1.getAllStore();
            call.enqueue(new Callback<List<MemberStore>>() {
                @Override
                public void onResponse(Call<List<MemberStore>> call, Response<List<MemberStore>> response) {

                    if(response.isSuccessful()) {
                        Log.d("tag", "가맹점GET성공");

                        allStores = response.body();

                        message.what = StateSet.HomeMsg.MSG_SUCCESS_GETSTORE; //메시지 아이디 설정
                        handler.sendMessage(message); //메인스레드 핸들러로 메시지 보내기

                    }else{
                        Log.d("tag", "가맹점GET실패");
                        message.what = StateSet.HomeMsg.MSG_FAIL;
                    }
                }

                @Override
                public void onFailure(Call<List<MemberStore>> call, Throwable t) {
                    Log.d("tag", "가맹점GET실패2");
                    message.what = StateSet.HomeMsg.MSG_FAIL;

                }
            });

        }
    }

    //핸들러 안에서 전달받은 메시지에 따라 화면전환 or 다이얼로그 띄우기 처리함
    class MainHandler extends Handler{
        @Override
        public void handleMessage(Message message){
            switch(message.what)
            {
                case StateSet.HomeMsg.MSG_SUCCESS_GETFAVORSTORE:
                    GetMemberStoreThread thread = new GetMemberStoreThread();
                    thread.start();
                    break;

                case StateSet.HomeMsg.MSG_SUCCESS_GETSTORE:

                    for (MemberStore store : allStores) {

                        if(!userFavorStoreID.isEmpty()){
                            //사용자가 등록한 즐겨찾기 가맹점이 있는 경우
                            int sid=  store.getSid();

                            for( int userFavorSID: userFavorStoreID) {
                                if(sid==userFavorSID){
                                    favorMemberStore.add(store);
                                  //  Log.d("tag",sid+"");
                                    break;
                                }
                            }
                        }

                        //즐겨찾기 된 가맹점들은 급식,부식,교육 스토어리스트에 포함됨.
                        String stype = store.getStype();
                        if (stype.equals("급식")) {
                            mealMemberStore.add(store);
                        } else if (stype.equals("교육")) {
                            eduMemberStore.add(store);
                        } else {
                            sideMealMemberStore.add(store);
                        }
                    }

                    Intent intent2 = new Intent(HomeActivity.this,MapActivity.class);
                    intent2.putParcelableArrayListExtra("eduMemberStores", eduMemberStore);
                    intent2.putParcelableArrayListExtra("sideMealMemberStores", sideMealMemberStore);
                    intent2.putParcelableArrayListExtra("mealMemberStores", mealMemberStore);
                    intent2.putParcelableArrayListExtra("favorMemberStores",favorMemberStore);
                    intent2.putExtra("userID", user.getId());
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
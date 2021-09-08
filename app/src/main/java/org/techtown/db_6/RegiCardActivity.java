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
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegiCardActivity extends AppCompatActivity {

    Intent intent;
    User user;

    final int MSG_SUCCESS_VAILCHECK = 1;
    final int MSG_FAIL=0;
    MainHandler handler;

    private EditText et_cardName, cardNo1, cardNo2, cardNo3, cardNo4;
    private RadioButton chMeal,chBusic,chEdu;
    private Button button, vaildCheckBT;
    private TextView vaildCheckResult;
    private AlertDialog dialog;
    private boolean validate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        intent = getIntent();
        user = (User)intent.getSerializableExtra("user");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_card);

        et_cardName = findViewById(R.id.et_cardName);
        cardNo1 =  findViewById(R.id.cardNo1);
        cardNo2 = findViewById(R.id.cardNo2);
        cardNo3 = findViewById(R.id.cardNo3);
        cardNo4 = findViewById(R.id.cardNo4);

        chMeal = findViewById(R.id.chMeal);
        chBusic = findViewById(R.id.chBusic);
        chEdu = findViewById(R.id.chEdu);

        button = findViewById(R.id.button);
        vaildCheckBT = findViewById(R.id.vaildCheckBT);
        vaildCheckResult= findViewById(R.id.vaildCheckResult);

        handler = new MainHandler();

        //카드인증 버튼클릭
        vaildCheckBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validate) {
                    return; //검증 완료
                }

                if(cardNo1.getText().toString().equals("") || cardNo2.getText().toString().equals("") || cardNo3.getText().toString().equals("") || cardNo4.getText().toString().equals(""))
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegiCardActivity.this);
                    dialog = builder.setMessage("카드번호를 모두 입력하세요.").setPositiveButton("확인", null).create();
                    dialog.show();
                    return;
                }

                BalanceCheckThread thread= new BalanceCheckThread();
                thread.start();

            }
        });

        //등록하기 버튼 클릭
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String cardName = et_cardName.getText().toString();
                StringBuilder cardNo = new StringBuilder();
                cardNo.append(cardNo1.getText().toString());
                cardNo.append(cardNo2.getText().toString());
                cardNo.append(cardNo3.getText().toString());
                cardNo.append(cardNo4.getText().toString());

                String cardType="";
                //급식:0      부식:1        교육:2
                if(chMeal.isChecked()) {
                    cardType = "0";
                }
                else if(chBusic.isChecked()){
                    cardType = "1";
                }
                else if(chEdu.isChecked()) {
                    cardType = "2";
                }


                if(cardName.equals("") || cardType.equals(""))
                {
                    Toast.makeText(getApplicationContext(), "모두 기입하였는지 확인하세요", Toast.LENGTH_SHORT).show();
                }
                else if(validate == false){
                    if(vaildCheckResult.getText().toString().equals("컬러풀카드 인증에 실패하였습니다"))
                        Toast.makeText(getApplicationContext(), "인증된 카드만 등록가능 합니다", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(getApplicationContext(), "카드인증을 진행해주세요", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("http://sw-env.eba-weppawy7.ap-northeast-2.elasticbeanstalk.com/")
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();


                    RetrofitService service1 = retrofit.create(RetrofitService.class);
                    Call<UserCard> call = service1.postUserCard(cardNo.toString(),user.getId(),cardName,cardType);
                    call.enqueue(new Callback<UserCard>() {

                        @Override
                        public void onResponse(Call<UserCard> call, Response<UserCard> response) {
                            if (response.isSuccessful()) {
                                Intent intent = new Intent(RegiCardActivity.this, HomeActivity.class); //일단은 로그인 성공하면 해당 id가 가진 카드리스트 보여주는 화면으로 이동
                                user.clearCardBalances();
                                intent.putExtra("user",user);
                                startActivity(intent);
                                finish();

                            } else {
                                Log.d("tag", "get 한 후 반환해올때 getByNum한 카드가 하나가 아니여서 발생하는 오류 동작에는 문제 없음");
                                Intent intent = new Intent(RegiCardActivity.this, HomeActivity.class);
                                user.clearCardBalances();
                                intent.putExtra("user",user);
                                startActivity(intent);
                                finish();
                            }

                        }

                        @Override
                        public void onFailure(Call<UserCard> call, Throwable t) {
                            Log.d("tag", "실패2" + t.getMessage());
                        }
                    });
                }
            }
        });

    }

    class BalanceCheckThread extends Thread{ //조회가 되면 인증된 카드로 취급함

        @Override
        public void run()
        {
            Message message=handler.obtainMessage();

            try {
                BalanceCheck checker = new BalanceCheck(cardNo1.getText().toString(),cardNo2.getText().toString(),cardNo3.getText().toString(),cardNo4.getText().toString());

                if (checker.tryBalanceCheck(2).equals("success")) {
                    message.what = MSG_SUCCESS_VAILCHECK;
                    handler.sendMessage(message); //메인스레드 핸들러로 메시지 보내기
                } else { //실패할 경우
                    message.what = MSG_FAIL;
                    handler.sendMessage(message);
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    class MainHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            switch (message.what)
            {
                case MSG_SUCCESS_VAILCHECK:
                    vaildCheckResult.setText("컬러풀카드 인증되었습니다");
                    cardNo1.setEnabled(false); //카드번호값 고정
                    cardNo2.setEnabled(false);
                    cardNo3.setEnabled(false);
                    cardNo4.setEnabled(false);
                    validate = true; //검증 완료
                    break;
                case MSG_FAIL:
                    vaildCheckResult.setText("컬러풀카드 인증에 실패하였습니다");
                    vaildCheckResult.setTextColor(0xAAef484a);
                    break;
            }
        }
    }
}


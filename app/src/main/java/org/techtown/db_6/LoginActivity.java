package org.techtown.db_6;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {
    private EditText edit_id,edit_pwd;
    private Button button3,joinbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edit_id = findViewById(R.id.edit_id);
        edit_pwd = findViewById(R.id.edit_pwd);
        button3 = (Button) findViewById(R.id.button3);
        joinbutton = (Button) findViewById(R.id.joinbutton1);

        joinbutton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), AgreeActivity.class);
                startActivity(i);
            }
        });


        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String id = edit_id.getText().toString();
                String pwd = edit_pwd.getText().toString();
                if(id.equals("")) {
                    Toast.makeText(getApplicationContext(), "아이디를 입력해주세요", Toast.LENGTH_SHORT).show();
                }
                else if(pwd.equals("")){
                    Toast.makeText(getApplicationContext(), "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("http://sw-env.eba-weppawy7.ap-northeast-2.elasticbeanstalk.com/")
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();


                    RetrofitService service1 = retrofit.create(RetrofitService.class);
                    Call<UserProfile> call = service1.getUserProfile(id);
                    call.enqueue(new Callback<UserProfile>() {

                        @Override
                        public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                            if (response.isSuccessful()) {
                                UserProfile result = response.body();
                                if (result.getPwd().equals(pwd) == false)
                                    Toast.makeText(getApplicationContext(), "아이디 또는 비밀번호가 잘못 입력되었습니다", Toast.LENGTH_SHORT).show();
                                else {

                                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class); //일단은 로그인 성공하면 해당 id가 가진 카드리스트 보여주는 화면으로 이동
                                    User user = new User(result.getId(),result.getName()); //서버에서 물어다온 user id 로 생성함
                                    intent.putExtra("user",user);
                                    startActivity(intent);
                                    finish();
                                }

                            } else {
                                Log.d("tag", "실패");
                            }


                        }

                        @Override
                        public void onFailure(Call<UserProfile> call, Throwable t) {
                            Log.d("tag", "실패2" + t.getMessage());
                            if(t.getMessage().equals("End of input at line 1 column 1 path $")) {
                                Toast.makeText(getApplicationContext(), "입력한 정보에 해당하는 계정을 찾을 수 없습니다", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
            });
        }


    @Override
    public void onBackPressed(){ //뒤로가기 버튼 누르면 종료
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("애플리케이션을 종료하시겠습니까?");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                finishAffinity();
                System.runFinalization();
                System.exit(0);
            }
        });
        builder.setNegativeButton("취소",null);
        builder.show();
    }
    }
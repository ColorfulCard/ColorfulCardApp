package org.techtown.db_6;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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

public class Login extends AppCompatActivity {
    private EditText edit_id,edit_pwd;
    private Button button3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edit_id = findViewById(R.id.edit_id);
        edit_pwd = findViewById(R.id.edit_pwd);

        button3 = (Button) findViewById(R.id.button3);

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("tag","button\n");
                String id = edit_id.getText().toString();
                String pwd = edit_pwd.getText().toString();
                if(id.equals("") || pwd.equals("")) {
                    Toast.makeText(getApplicationContext(), " 아이디와 비밀번호를 모두 기입하세요 .", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("http://sw-env.eba-weppawy7.ap-northeast-2.elasticbeanstalk.com/")
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();


                    RetrofitService service1 = retrofit.create(RetrofitService.class);
                    Log.d("tag", "1\n");

                    Call<UserProfile> call = service1.getUserProfile(id);

                    Log.d("tag", "2\n");
                    call.enqueue(new Callback<UserProfile>() {

                        @Override
                        public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                            if (response.isSuccessful()) {
                                UserProfile result = response.body();
                                if (result.getPwd().equals(pwd) == false)
                                    Toast.makeText(getApplicationContext(), "비밀번호가 틀립니다", Toast.LENGTH_SHORT).show();
                                else {
                                    Toast.makeText(getApplicationContext(), "로그인에 성공하셨습니다", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(Login.this, UserCardList.class); //일단은 로그인 성공하면 해당 id가 가진 카드리스트 보여주는 화면으로 이동
                                    User user = new User(result.getId(),result.getName()); //서버에서 물어다온 user id 로 생성함
                                    intent.putExtra("user",user);
                                    startActivity(intent);
                                }

                            } else {
                                Log.d("tag", "실패");
                            }


                        }

                        @Override
                        public void onFailure(Call<UserProfile> call, Throwable t) {
                            Log.d("tag", "실패2" + t.getMessage());
                            if(t.getMessage().equals("End of input at line 1 column 1 path $")) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                                AlertDialog dialog = builder.setMessage("가입된 ID가 아닙니다.").setPositiveButton("확인", null).create();
                                dialog.show();
                                Toast.makeText(getApplicationContext(), "가입된 ID가 아닙니다", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
            });
        }

    }
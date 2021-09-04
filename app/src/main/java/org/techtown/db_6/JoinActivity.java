package org.techtown.db_6;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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


public class JoinActivity extends AppCompatActivity {

    private EditText edit_id ,edit_pwd,edit_pwd2,edit_name;
    private Button button3 ,button7 ;
    private AlertDialog dialog;
    private boolean validate = false , checkPW = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        edit_id = findViewById(R.id.edit_id);
        edit_pwd = findViewById(R.id.edit_pwd);
        edit_pwd2 = findViewById(R.id.edit_pwd2);
        edit_name = findViewById(R.id.edit_name);
        button3 = (Button) findViewById(R.id.button3);
        button7 = (Button) findViewById(R.id.button7);

        button7.setOnClickListener(new View.OnClickListener() {  //ID중복검사 버튼

            @Override
            public void onClick(View view) {
                String id = edit_id.getText().toString();

                if (validate) {
                    return; //검증 완료
                }

                if (id.equals("")) { //아이디 입력안했을 때
                    AlertDialog.Builder builder = new AlertDialog.Builder(JoinActivity.this);
                    dialog = builder.setMessage("아이디를 입력하세요.").setPositiveButton("확인", null).create();
                    dialog.show();
                    return;
                }

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
                            AlertDialog.Builder builder = new AlertDialog.Builder(JoinActivity.this);
                            dialog = builder.setMessage("이미 존재하는 아이디입니다").setPositiveButton("확인", null).create();
                            dialog.show();
                            System.out.println("중복ID:" + result.getId());


                        } else {
                            Log.d("tag", "response 실패");
                        }
                    }

                    @Override
                    public void onFailure(Call<UserProfile> call, Throwable t) {
                        Log.d("tag", "테이블에 존재하지 않는 ID라 등록가능" + t.getMessage());
                        if(t.getMessage().equals("End of input at line 1 column 1 path $"))
                        {
                            AlertDialog.Builder builder = new AlertDialog.Builder(JoinActivity.this);
                            dialog = builder.setMessage("사용할 수 있는 아이디입니다.").setPositiveButton("확인", null).create();
                            dialog.show();
                            edit_id.setEnabled(false); //아이디값 고정
                            validate = true; //검증 완료
                        }

                    }

                });
            }
        });
        
        button3.setOnClickListener(new View.OnClickListener() {  //회원가입버튼
            @Override
            public void onClick(View v) {

                String id = edit_id.getText().toString();
                String pwd = edit_pwd.getText().toString();
                String pwd2 = edit_pwd2.getText().toString();
                String name = edit_name.getText().toString();

                if(id.equals("") || pwd.equals("") || pwd2.equals("") || name.equals("")) {
                    Toast.makeText(getApplicationContext(), "모두 기입하였는지 한번 더 확인해주세요.", Toast.LENGTH_SHORT).show();
                }
                else if(validate == false){
                    Toast.makeText(getApplicationContext(), "ID중복확인을 진행해주세요.", Toast.LENGTH_SHORT).show();
                }
                else if (pwd.equals(pwd2) == false) {

                    Toast.makeText(getApplicationContext(), "비밀번호가 다릅니다", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("http://sw-env.eba-weppawy7.ap-northeast-2.elasticbeanstalk.com/")
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    RetrofitService service1 = retrofit.create(RetrofitService.class);
                    //인터페이스 객체구현
                    Log.d("tag", "1\n");
                    Call<UserProfile> call = service1.postUserProfile(id, pwd, name);
                    //사용할 메소드 선언
                    Log.d("tag", "2\n");
                    call.enqueue(new Callback<UserProfile>() {

                        @Override
                        public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {

                            if (response.isSuccessful()) {
                                //메인스레드 작업가능
                                Log.d("tag", "회원등록성공\n");
                                Toast.makeText(getApplicationContext(), "회원등록에 성공하였습니다", Toast.LENGTH_SHORT).show();
                                //넘기는거 해주기

                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), "회원등록에 실패하였습니다", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<UserProfile> call, Throwable t) {
                            Log.d("tag", "네트워크 연결문제로 회원등록 실패" + t.getMessage());
                            Toast.makeText(getApplicationContext(), "회원등록에 실패하였습니다", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });
    }
}


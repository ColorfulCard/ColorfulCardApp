package org.techtown.db_6;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class JoinActivity extends AppCompatActivity {

    private EditText edit_id ,edit_pwd,edit_pwd2,edit_name, edit_email, edit_num;
    private Button button_Join ,button_CheckID ,button_send;
    private TextView checkIDResult,emailText,CheckPwEN;
    private boolean validate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        edit_id = findViewById(R.id.edit_id);
        edit_pwd = findViewById(R.id.edit_pwd);
        edit_pwd2 = findViewById(R.id.edit_pwd2);
        edit_name = findViewById(R.id.edit_name);
        edit_email = findViewById(R.id.edit_email);
        button_Join = (Button) findViewById(R.id.button3);
        button_CheckID = (Button) findViewById(R.id.button7);
        button_send=findViewById(R.id.btn_email);
        CheckPwEN = findViewById(R.id.CheckPwEN);
        checkIDResult=findViewById(R.id.overlap_result);
        emailText=findViewById(R.id.text_email);


        button_send.setOnClickListener(new View.OnClickListener() {  //인증번호 전송 버튼 클릭시
            @Override
            public void onClick(View v) {
                String email = edit_email.getText().toString();
                if(email.equals("")) { //사용자가 입력하지 않았을 시
                  emailText.setText("이메일을 입력하십시오");
                  return;
                }
                //이메일 형식에 맞는지 검사 부분
                // @랑 @뒤에 . 하나라도 있는지 보면될듯,, com형식으로 안끝나는 이메일들도 있어가지구 예: 저희학교메일 @ynu.ac.kr
                Server server = new Server();
                RetrofitService service2 = server.getRetrofitService();
                Call<UserProfile> call = service2.getUserProfilebyEmail(email);
                call.enqueue(new Callback<UserProfile>() {
                    @Override
                    public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                        emailText.setTextColor(0xAAef484a);
                        emailText.setText("이미 가입된 이메일입니다");
                    }

                    @Override
                    public void onFailure(Call<UserProfile> call, Throwable t) {

                        emailText.setTextColor(0xFF000000);
                        emailText.setText("인증번호를 전송합니다.");
                        edit_email.setEnabled(false); //이메일값 고정
                    }
                });
            }
        });


        button_CheckID.setOnClickListener(new View.OnClickListener() {  //ID중복검사 버튼

            @Override
            public void onClick(View view) {
                String id = edit_id.getText().toString();

                if (validate) {
                    return; //검증 완료
                }

                if (id.equals("")) { //아이디 입력안했을 때
                    Toast.makeText(getApplicationContext(), "아이디를 입력하세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                Server server = new Server();
                RetrofitService service1 = server.getRetrofitService();
                Call<UserProfile> call = service1.getUserProfile(id);

                call.enqueue(new Callback<UserProfile>() {

                    @Override
                    public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {

                        if (response.isSuccessful()) {

                            checkIDResult.setTextColor(0xAAef484a);
                            checkIDResult.setText("이미 가입된 아이디입니다");

                        } else {
                            Log.d("tag", "response 실패");
                        }
                    }

                    @Override
                    public void onFailure(Call<UserProfile> call, Throwable t) {
                        Log.d("tag", "테이블에 존재하지 않는 ID라 등록가능" + t.getMessage());
                        if(t.getMessage().equals("End of input at line 1 column 1 path $"))
                        {
                            checkIDResult.setTextColor(0xFF000000);
                            checkIDResult.setText("사용가능한 아이디입니다");
                            edit_id.setEnabled(false); //아이디값 고정
                            validate = true; //검증 완료
                        }

                    }

                });
            }
        });

        edit_pwd.addTextChangedListener(new TextWatcher() { //비밀번호에 리스너를 달아서
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() >= 6) {
                    Pattern p = Pattern.compile("^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&]).{6,}.$");
                    Matcher m = p.matcher(s.toString());

                    if (!m.matches()) {
                        CheckPwEN.setText("사용할 수 없는 비밀번호입니다.");
                    } else {
                        CheckPwEN.setText("사용할 수 있는 비밀번호입니다.");
                    }

                }else{

                    CheckPwEN.setText("6글자 이상 입력해주세요.");
                }
            }
        });





        button_Join.setOnClickListener(new View.OnClickListener() {  //회원가입버튼
            @Override
            public void onClick(View v) {

                String id = edit_id.getText().toString();
                String pwd = edit_pwd.getText().toString();
                String pwd2 = edit_pwd2.getText().toString();
                String name = edit_name.getText().toString();

                if(id.equals("") || pwd.equals("") || pwd2.equals("") || name.equals("")) {
                    Toast.makeText(getApplicationContext(), "모두 입력하였는지 확인해주세요", Toast.LENGTH_SHORT).show();
                }
                else if(validate == false){
                    Toast.makeText(getApplicationContext(), "아이디 중복확인을 진행해주세요", Toast.LENGTH_SHORT).show();
                }
                else if (pwd.equals(pwd2) == false) {

                    Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Server server = new Server();
                    RetrofitService service1 = server.getRetrofitService();
                    //인터페이스 객체구현
                    Call<UserProfile> call = service1.postUserProfile(id, pwd, name);
                    //사용할 메소드 선언
                    call.enqueue(new Callback<UserProfile>() {

                        @Override
                        public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {

                            if (response.isSuccessful()) {
                                //메인스레드 작업가능
                                Log.d("tag", "회원등록성공\n");
                                Toast.makeText(getApplicationContext(), "회원가입 완료", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(JoinActivity.this,LoginActivity.class);
                                startActivity(intent);
                                finish();

                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), "회원가입 실패", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<UserProfile> call, Throwable t) {
                            Log.d("tag", "네트워크 문제로 회원등록 실패" + t.getMessage());
                            Toast.makeText(getApplicationContext(), "회원가입 실패, 네트워크를 확인하세요", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });
    }
}


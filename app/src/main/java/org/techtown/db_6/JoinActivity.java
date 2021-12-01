package org.techtown.db_6;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.MessagingException;
import javax.mail.SendFailedException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class JoinActivity extends AppCompatActivity {

    private EditText edit_id, edit_pwd, edit_pwd2, edit_name, edit_email, edit_num;
    private Button button_Join, button_CheckID, button_send;
    private TextView checkIDPW, emailText;
    private boolean validateID = false;     //아이디 중복검사했는지
    private boolean validatePW = false;     //비밀번호 형식통과했는지
    private boolean validateEmailFormat = false; //이메일 형식통과했는지
    private boolean validateEmail = false;   //이메일 중복검사했는지

    private MainHandler handler;
    private int sysNumber;                  //랜덤한 인증번호 6자리


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        handler = new MainHandler();

        edit_id = findViewById(R.id.edit_id);
        edit_pwd = findViewById(R.id.edit_pwd);
        edit_pwd2 = findViewById(R.id.edit_pwd2);
        edit_name = findViewById(R.id.edit_name);
        edit_email = findViewById(R.id.edit_email);
        edit_num = findViewById(R.id.edit_certify);
        button_Join = (Button) findViewById(R.id.button3);
        button_CheckID = (Button) findViewById(R.id.button7);
        button_send = findViewById(R.id.btn_email);
        checkIDPW = findViewById(R.id.checkIDPW);
        emailText = findViewById(R.id.text_email);


        //이메일 형식에 맞는지 검사 부분
        // @랑 @뒤에 . 하나라도 있는지 보면될듯,, com형식으로 안끝나는 이메일들도 있어가지구 예: 저희학교메일 @ynu.ac.kr
        edit_email.addTextChangedListener(new TextWatcher() { //비밀번호에 리스너를 달아서
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Pattern p = Pattern.compile("^[0-9a-zA-Z._%+-]+@[0-9a-zA-Z.-]+\\.[a-zA-Z]{2,6}$");
                Matcher m = p.matcher(s.toString());
                Log.d("tag", s.toString());

                if (!m.matches()) {
                    Log.d("tag", "정규식 성립안됨");
                    emailText.setTextColor(0xAAef484a);
                    emailText.setText("이메일형식이 잘못되었습니다.");
                    validateEmailFormat = false;
                } else {
                    Log.d("tag", "정규식 성립됨");
                    emailText.setText("");
                    validateEmailFormat = true;

                }
            }
        });

        button_send.setOnClickListener(new View.OnClickListener() {  //인증번호 전송 버튼 클릭시
            @Override
            public void onClick(View v) {

                String email = edit_email.getText().toString();
                if (validateEmail) {
                    return; //검증 완료
                }

                if (email.equals("")) { //사용자가 입력하지 않았을 시
                    emailText.setTextColor(0xFF6E6E6E);
                    Toast.makeText(getApplicationContext(), "이메일을 입력하세요", Toast.LENGTH_SHORT).show();
                    emailText.setText("이메일을 입력하십시오");
                    return;
                }

                if (validateEmailFormat == false) {

                    Toast.makeText(getApplicationContext(), "이메일형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show();

                }

                //이메일 체크하여 DB에 가입되어있지 않은 이메일인 경우 해당 메일로 인증번호 전송, 아닐경우 메세지 출력
                checkEmail(email);
            }
        });

        edit_pwd.addTextChangedListener(new TextWatcher() {
            //비밀번호에 리스너를 달아서 비밀번호 형식에 맞는지 검사 부분

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
                        checkIDPW.setTextColor(0xAAef484a);
                        checkIDPW.setText("영문자,숫자,특수문자 조합");
                        validatePW = false;
                    } else {
                        checkIDPW.setTextColor(0xFF6E6E6E);
                        checkIDPW.setText("사용 가능한 비밀번호입니다.");
                        validatePW = true;
                    }

                } else    //6글자 미만
                {
                    checkIDPW.setTextColor(0xAAef484a);
                    checkIDPW.setText("영문자,숫자,특수문자 조합 6자리 이상");
                    validatePW = false;
                }
            }
        });

        button_CheckID.setOnClickListener(new View.OnClickListener() {  //ID중복검사 버튼

            @Override
            public void onClick(View view) {
                String id = edit_id.getText().toString();

                if (validateID) {
                    return; //검증 완료
                }

                if (id.equals("")) { //아이디 입력안했을 때
                    Toast.makeText(getApplicationContext(), "아이디를 입력하세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                //DB에  가입된 아이디인지 체크하는 함수
                checkId(id);
            }
        });

        button_Join.setOnClickListener(new View.OnClickListener() {  //회원가입버튼
            @Override
            public void onClick(View v) {
                Log.d("tag", "회원가입버튼클릭");
                String id = edit_id.getText().toString();
                String pwd = edit_pwd.getText().toString();
                String pwd2 = edit_pwd2.getText().toString();
                String name = edit_name.getText().toString();
                String email = edit_email.getText().toString();
                String userNumber = edit_num.getText().toString();

                if (id.equals("") || pwd.equals("") || pwd2.equals("") || name.equals("")) {
                    Toast.makeText(getApplicationContext(), "모두 입력하였는지 확인해주세요", Toast.LENGTH_SHORT).show();
                } else if (validateID == false) {
                    Toast.makeText(getApplicationContext(), "아이디 중복확인을 진행해주세요", Toast.LENGTH_SHORT).show();
                } else if (validatePW == false) {
                    Toast.makeText(getApplicationContext(), "유효한 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else if (validateEmail == false) {
                    Toast.makeText(getApplicationContext(), "이메일 인증번호 전송을 진행해주세요", Toast.LENGTH_SHORT).show();
                } else if (pwd.equals(pwd2) == false) {
                    Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show();
                } else if (!userNumber.equals(String.valueOf(sysNumber))) {
                    Toast.makeText(getApplicationContext(), "인증번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show();
                } else {
                    joinApplication(id, pwd, name, email);      //모든 조건 완료시 회원 가입 시키는 함수
                }
            }
        });
    }

    private void joinApplication(String id, String pwd, String name, String email) {

        Server server = new Server();
        RetrofitService service1 = server.getRetrofitService();
        //인터페이스 객체구현
        Call<UserProfile> call = service1.postUserProfile(id, pwd, name, email);
        //사용할 메소드 선언
        call.enqueue(new Callback<UserProfile>() {

            @Override
            public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {

                if (response.isSuccessful()) {
                    //메인스레드 작업가능
                    Log.d("tag", "회원등록성공\n");
                    Toast.makeText(getApplicationContext(), "회원가입 완료", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(JoinActivity.this, LoginActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_left_enter, R.anim.slide_left_exit);
                    finish();

                } else {
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

    private void checkEmail(String email) {

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

                emailText.setTextColor(0xFF6E6E6E);
                emailText.setText("인증번호를 전송합니다.");
                edit_email.setEnabled(false); //이메일값 고정
                validateEmail = true; //검증 완료

                Random random = new Random(); //인증번호 발생시키는 부분
                int range = (int) Math.pow(10, 6);
                int trim = (int) Math.pow(10, 5);
                sysNumber = random.nextInt(range) + trim;
                if (sysNumber > range) {
                    sysNumber = sysNumber - trim;
                }
                Log.d("tag", sysNumber + "");

                SendNumberByMailThread thread = new SendNumberByMailThread(email, String.valueOf(sysNumber));
                thread.start();
            }
        });
    }

    private void checkId(String id) {

        Server server = new Server();
        RetrofitService service1 = server.getRetrofitService();
        Call<UserProfile> call = service1.getUserProfile(id);

        call.enqueue(new Callback<UserProfile>() {

            @Override
            public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {

                if (response.isSuccessful()) {

                    checkIDPW.setTextColor(0xAAef484a);
                    checkIDPW.setText("이미 가입된 아이디입니다");

                } else {
                    Log.d("tag", "response 실패");
                }
            }

            @Override
            public void onFailure(Call<UserProfile> call, Throwable t) {
                Log.d("tag", "테이블에 존재하지 않는 ID라 등록가능" + t.getMessage());
                if (t.getMessage().equals("End of input at line 1 column 1 path $")) {

                    checkIDPW.setTextColor(0xFF6E6E6E);
                    checkIDPW.setText("사용가능한 아이디입니다");
                    edit_id.setEnabled(false); //아이디값 고정
                    validateID = true; //검증 완료
                }

            }

        });
    }

    class SendNumberByMailThread extends Thread{

        String receptEmail;
        String content;

        public SendNumberByMailThread(String email,String number){

            this.receptEmail=email;
            this.content="안녕하세요, 대구시 컬러풀 카드앱입니다.<br>아래의 인증번호 6자리를 인증번호 입력창에 입력 후 회원가입을 진행해주세요.<br><br>인증번호: "+number+"<br>";
        }

        @Override
        public void run()
        {
            Message message=handler.obtainMessage(); //메인스레드 핸들러의 메시지 객체 가져오기
            try{
                NaverMailSender mailSender = new NaverMailSender();
                Log.d("tag","sender Make");
                mailSender.sendMail("[컬러풀 카드앱] 애플리케이션 회원가입 인증번호",
                        content, receptEmail);

                message.what= StateSet.MailMsg.MSG_SUCCESS;
                handler.sendMessage(message);

            }catch (SendFailedException e) {

                message.what= StateSet.MailMsg.MSG_FAIL;
                handler.sendMessage(message);

            } catch (MessagingException e) {

            } catch (Exception e) { e.printStackTrace(); }
        }
    }

    class MainHandler extends Handler {
        @Override
        public void handleMessage(Message message){
            switch(message.what)
            {

                case StateSet.MailMsg.MSG_SUCCESS:
                    emailText.setText("인증번호 전송완료");
                    Toast.makeText(getApplicationContext(), "해당 메일에서 인증번호를 확인하십시오", Toast.LENGTH_SHORT).show();
                    break;
                case StateSet.MailMsg.MSG_FAIL:
                    emailText.setText("이메일 형식이 잘못되었습니다");
                    Toast.makeText(getApplicationContext(), "이메일 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_left_enter, R.anim.slide_left_exit);
    }

}


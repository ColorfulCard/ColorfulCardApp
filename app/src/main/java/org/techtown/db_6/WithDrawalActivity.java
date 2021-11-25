package org.techtown.db_6;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WithDrawalActivity extends AppCompatActivity {

    private EditText edit_id, edit_pw, edit_name;
    private Button btn_withdraw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdrawal);

        edit_id= findViewById(R.id.edit_id);
        edit_pw= findViewById(R.id.edit_pw);
        edit_name= findViewById(R.id.edit_name);
        btn_withdraw= findViewById(R.id.button2);

        btn_withdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String id= edit_id.getText().toString();
                String pw= edit_pw.getText().toString();
                String name= edit_name.getText().toString();

                if(id.equals("")||pw.equals("")||name.equals("")){
                    Toast.makeText(getApplicationContext(), "아이디,비밀번호,이름 모두 입력하십시오", Toast.LENGTH_SHORT).show();
                    return;
                }

                checkJoinUser(id,pw,name);


            }
        });
    }

    private void checkJoinUser(String id, String pw, String name) {

        Server server = new Server();
        RetrofitService service1 = server.getRetrofitService();
        //인터페이스 객체구현
        Call<UserProfile> call = service1.getUserProfile(id);
        call.enqueue(new Callback<UserProfile>() {
            @Override
            public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {

                UserProfile result = response.body();
                if (result.getPwd().equals(pw) && result.getName().equals(name)) {
                    Log.d("tag","찾음");
                    deleteUser(id);
                } else {
                    Toast.makeText(getApplicationContext(), "입력하신 회원정보와 해당 아이디로 가입된 회원정보가가 일치하지 않습니다.\n본인이 맞습니까?", Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<UserProfile> call, Throwable t) {

                Toast.makeText(getApplicationContext(), "해당 아이디 \""+id+"\"로 가입된 회원은 존재하지 않습니다", Toast.LENGTH_LONG).show();

            }
        });

    }
        private void deleteUser(String id) {

            Server server = new Server();
            RetrofitService service1 = server.getRetrofitService();
            //인터페이스 객체구현
            Call<Integer> call = service1.deleteUser(id);
            call.enqueue(new Callback<Integer>() {
                @Override
                public void onResponse(Call<Integer> call, Response<Integer> response) {

                    Integer result= response.body();
                    if((!result.equals("")) && result.intValue()==1){
                        Toast.makeText(getApplicationContext(), id+"님의 회원탈퇴가 완료되었습니다.", Toast.LENGTH_LONG).show();
                       Intent intent = new Intent(WithDrawalActivity.this, LoginActivity.class);
                       startActivity(intent);
                       finish();
                    }
                }

                @Override
                public void onFailure(Call<Integer> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), id+"님의 회원탈퇴가 취소되었습니다. 다시한번 시도해주십시오.", Toast.LENGTH_LONG).show();

                }
            });

        }

    }

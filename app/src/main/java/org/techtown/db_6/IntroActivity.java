package org.techtown.db_6;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.os.Handler;

//대기화면 Activity
public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        //핸들러 선언
         Handler handler = new Handler();


         handler.postDelayed(new Runnable(){ //1.5초 후 실행됨
            @Override
            public void run() {
                //시간 지난 후 실행할 동작 코딩
                Intent intent = new Intent (getApplicationContext(), LoginActivity.class); //Intro화면에서 Login 화면으로 넘어가는 intent
                startActivity(intent); //intent 시작
                finish();
            }
        },1500);
    }

    @Override
    protected void onPause(){
        super.onPause();
        finish();
    }
}
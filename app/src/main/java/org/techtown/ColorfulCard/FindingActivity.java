package org.techtown.ColorfulCard;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class FindingActivity extends AppCompatActivity {

    private TextView findID, findPW;
    private  FindIDFragment iDFragment;
    private FindPWFragment pwFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_finding);
        findID= findViewById(R.id.textView);
        findPW= findViewById(R.id.textView4);

        iDFragment= new FindIDFragment();
        pwFragment = new FindPWFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, iDFragment).commitAllowingStateLoss();


        findID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findID.setTextColor(0xFF6E6E6E); //비밀번호 클릭시 진한회색
                findPW.setTextColor(0xFFFFFFFF);  //비밀번호는 흰색으로

                getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, iDFragment).commitAllowingStateLoss();
                Log.d("tag","아이디찾기 클릭됨");
            }
        });

        findPW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findPW.setTextColor(0xFF6E6E6E); //비밀번호 클릭시 진한회색
                findID.setTextColor(0xFFFFFFFF);  //아이디는 흰색으로
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, pwFragment).commitAllowingStateLoss();
                Log.d("tag","비밀번호찾기 클릭됨");

            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_left_enter, R.anim.slide_left_exit);

    }
}

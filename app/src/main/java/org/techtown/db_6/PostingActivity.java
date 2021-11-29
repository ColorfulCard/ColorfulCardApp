package org.techtown.db_6;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class PostingActivity extends AppCompatActivity {

    BottomNavigationView navigationView;
    Context mContext;
    Posting posting;
    TextView pid,pcontent,pdate,hcnt,ccnt,vcnt;
    Button deleteBt;
    private String userID= BoardActivity.userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posting);
        posting= (Posting) getIntent().getSerializableExtra("choicePosting");

        navigationView= findViewById(R.id.navigationView);
        navigationView.setOnNavigationItemSelectedListener(new ItemSelectedListener());
        mContext= this.getApplicationContext();

        pid=findViewById(R.id.pid);
        pcontent= findViewById(R.id.pcontent);
        pdate=findViewById(R.id.pdate);
        hcnt=findViewById(R.id.hcnt);
        ccnt=findViewById(R.id.ccnt);
        vcnt=findViewById(R.id.vcnt);
        deleteBt= findViewById(R.id.deleteBt);


        pid.setText(posting.getPid());
        pcontent.setText(posting.getPcontent());
        pdate.setText(posting.getPdate());
        hcnt.setText(String.valueOf(posting.getHcnt()));
        ccnt.setText(String.valueOf(posting.getCcnt()));
        vcnt.setText(String.valueOf(posting.getVcnt()));

        if(posting.getPid().equals(userID)){

            deleteBt.setVisibility(View.VISIBLE);
            //본인 글이면 삭제하기 버튼 나타남
        }


    }// onCreate()..

    class ItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener{
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch(menuItem.getItemId())
            {

                case R.id.heart:
                    //공감하기 누른 경우
                    Toast.makeText(getApplicationContext(), "공감하기 버튼 누룸", Toast.LENGTH_SHORT).show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        navigationView.setItemTextColor(getResources().getColorStateList(R.color.colorheart, null));
                        navigationView.setItemIconTintList(getResources().getColorStateList(R.color.colorheart, null));
                    }
                    break;


                case R.id.comment:
                    Toast.makeText(getApplicationContext(), "댓글달기 버튼 누룸", Toast.LENGTH_SHORT).show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        navigationView.setItemTextColor(getResources().getColorStateList(R.color.colorcomment, null));
                        navigationView.setItemIconTintList(getResources().getColorStateList(R.color.colorcomment, null));
                    }
                    break;

            }// switch()..
            return true;
        }
    }// ItemSelectedListener class..


    class GetCmentThread extends Thread{


        @Override
        public void run() {
            super.run();
        }
    }

    class GetCcmmentsThread extends Thread{

        @Override
        public void run() {
            super.run();
        }
    }

    class CheckHeartThread extends Thread{

        @Override
        public void run() {
            super.run();
        }
    }

    class InsertCmentThread extends Thread{

        @Override
        public void run() {
            super.run();
        }
    }

}// MainActivity class..
package org.techtown.db_6;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class PostingActivity extends AppCompatActivity {

    BottomNavigationView navigationView;
    Context mContext;
    Posting posting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posting);
        posting= (Posting) getIntent().getSerializableExtra("choicePosting");

        navigationView= findViewById(R.id.navigationView);

        navigationView.setOnNavigationItemSelectedListener(new ItemSelectedListener());
        mContext= this.getApplicationContext();
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


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        navigationView.setItemTextColor(getResources().getColorStateList(R.color.colorcomment, null));
                        navigationView.setItemIconTintList(getResources().getColorStateList(R.color.colorcomment, null));}

                        Toast.makeText(getApplicationContext(), "댓글달기 버튼 누룸", Toast.LENGTH_SHORT).show();
                        break;

            }// switch()..
            return true;
        }
    }// ItemSelectedListener class..


}// MainActivity class..
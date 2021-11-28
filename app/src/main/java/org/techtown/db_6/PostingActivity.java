package org.techtown.db_6;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class PostingActivity extends AppCompatActivity {

    BottomNavigationView navigationView;
    Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posting);

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
                   //navigationView.item
                     //      navigationView.getItemIconTintList() = ContextCompat.getColorStateList(mContext, R.color.colorheart);
                    //navigationView.itemTextColor = ContextCompat.getColorStateList(this, R.color.color_bnv2)


                    break;

                case R.id.comment:
                    Toast.makeText(getApplicationContext(), "댓글달기 버튼 누룸", Toast.LENGTH_SHORT).show();

            }// switch()..
            return true;
        }
    }// ItemSelectedListener class..


}// MainActivity class..

    }
}
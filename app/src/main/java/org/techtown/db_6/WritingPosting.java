package org.techtown.db_6;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class WritingPosting extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writing_posting);

        Button completeBt = (Button)findViewById(R.id.completeBt) ;
        Button postingListBt = (Button)findViewById(R.id.postingListBt);

        completeBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(WritingPosting.this, BordActivity.class);
                startActivity(intent);

            }
        });

        postingListBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(WritingPosting.this, BordActivity.class);
                startActivity(intent);

            }
        });















    }
}
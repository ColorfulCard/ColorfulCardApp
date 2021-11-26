package org.techtown.db_6;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;

public class PostingActivity extends AppCompatActivity {
    String[] item;
    private Spinner Sortingspinner;
    private ImageButton WritingBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posting);

        WritingBtn = (ImageButton) findViewById(R.id.writting);
        Sortingspinner = (Spinner)findViewById(R.id.orderby);

        item = new String[]{"최신순","조회수","공감수"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
            Sortingspinner.setAdapter(adapter);


        WritingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(PostingActivity.this, WritingPosting.class);
                startActivity(intent);

            }
        });






    }

}
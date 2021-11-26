package org.techtown.db_6;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class PostingActivity extends AppCompatActivity {
    String[] item;
    private Spinner Sortingspinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posting);

        Sortingspinner = (Spinner)findViewById(R.id.SortingSpinner);

        item = new String[]{"최신순","조회수","공감수"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
            Sortingspinner.setAdapter(adapter);


    }

}
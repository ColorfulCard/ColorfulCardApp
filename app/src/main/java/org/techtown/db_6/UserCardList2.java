package org.techtown.db_6;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class UserCardList2 extends AppCompatActivity {

    private ArrayList<DataItem> dataList;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_card_list);

        ImageButton imageButton;

        this.initializeData();

        ImageButton imageButton2 = findViewById(R.id.imageButton);
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.addItemDecoration(
                new DividerItemDecoration(this, R.drawable.line_divider));

        LinearLayoutManager manager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);


        recyclerView.setLayoutManager(manager); // LayoutManager 등록

        recyclerView.setAdapter(new MyAdapter(dataList)); // Adapter 등록

        imageButton2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                Toast.makeText(getApplicationContext(),"ok",Toast.LENGTH_LONG).show();

            }

        });


    }

    public void initializeData()
    {
        dataList = new ArrayList<>();

        dataList.add(new DataItem("카드이름", "잔액", button ,Code.ViewType.Notmeal));
        // dataList.add(new DataItem(" ", null,button2,  Code.ViewType.PLUS));
        dataList.add(new DataItem("안녕하세요", "사용자2",button,  Code.ViewType.mealCard));

    }


}
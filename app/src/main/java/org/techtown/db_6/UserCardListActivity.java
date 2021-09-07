package org.techtown.db_6;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class UserCardListActivity extends AppCompatActivity {

    Intent intent;
    User user; //사용자 클래스

    private ArrayList<DataItem> dataList;
    private Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        intent = getIntent();
        user = (User) intent.getSerializableExtra("user");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_card_list);
        ImageButton imageButton = findViewById(R.id.imageButton);
        TextView textView3= findViewById(R.id.textView3);



        if(user.getCards().isEmpty())
            textView3.setText("등록된 카드가 없습니다");
        else{
            this.initializeData();

            RecyclerView recyclerView = findViewById(R.id.recyclerview);
            recyclerView.addItemDecoration(
                    new DividerItemDecoration(this, R.drawable.line_divider));

            LinearLayoutManager manager
                    = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);

            recyclerView.setLayoutManager(manager); // LayoutManager 등록
            recyclerView.setAdapter(new MyAdapter(dataList)); // Adapter 등록

        }

        imageButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                Intent intent = new Intent(UserCardListActivity.this, RegiCardActivity.class);
                intent.putExtra("user",user);
                startActivity(intent);
                finish();
            }

        });


    }

    public void initializeData()
    {
        dataList = new ArrayList<>();

        ArrayList<String[]> allBalances = user.getCardBalances();

        int i=0;

        for( UserCard card : user.getCards() ){

            Log.d("tag",i+"번째 카드");
            if(card.getCardType().equals("0"))  //급식카드
            {
                dataList.add(new DataItem(card.getCardName(), user.getCardBalances().get(i)[3], button, Code.ViewType.mealCard, user.getCardBalances().get(i),""));

            }else if(card.getCardType().equals("1")) //부식카드
            {
                dataList.add(new DataItem(card.getCardName(), user.getCardBalances().get(i)[3], button, Code.ViewType.sideMealCard, user.getCardBalances().get(i),""));
            }
            else //교육카드
            {
                dataList.add(new DataItem(card.getCardName(), user.getCardBalances().get(i)[3], button, Code.ViewType.eduCard, user.getCardBalances().get(i),""));
            }
            i++;
        }

    }


}


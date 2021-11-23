package org.techtown.db_6;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class UserCardListActivity extends AppCompatActivity {

    private Intent intent;
    private UserCard user; //사용자 클래스
    private ArrayList<DataItem.CardData> dataList;
    private ImageButton imageButton;
    private TextView noRegiCardText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        intent = getIntent();
        user = (UserCard) intent.getSerializableExtra("user");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_card_list);
        imageButton = findViewById(R.id.imageButton);
        noRegiCardText = findViewById(R.id.textView3);

        if(user.getCards().isEmpty())
            noRegiCardText.setText("등록된 카드가 없습니다");
        else{
            this.initializeData();

            RecyclerView recyclerView = findViewById(R.id.recyclerview);
            recyclerView.addItemDecoration(
                    new DividerItemDecoration(this, R.drawable.line_divider));

            LinearLayoutManager manager
                    = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);

            recyclerView.setLayoutManager(manager); // LayoutManager 등록
            recyclerView.setAdapter(new UserCardListAdapter(dataList)); // Adapter 등록

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
        String meal="0";
        String sideMeal="1";

        for( Card card : user.getCards() ){

            Log.d("tag",i+"번째 카드");
            if(card.getCardType().equals(meal))  //급식카드
            {
                dataList.add(new DataItem.CardData(card.getCardName(), user.getCardBalances().get(i)[3],  StateSet.ViewType.mealCard, user.getCardBalances().get(i) , card.getCardNum() , card.getCardType() ,user.getId()));

            }else if(card.getCardType().equals(sideMeal)) //부식카드
            {
                dataList.add(new DataItem.CardData(card.getCardName(), user.getCardBalances().get(i)[3],  StateSet.ViewType.sideMealCard, user.getCardBalances().get(i), card.getCardNum() ,card.getCardType() ,user.getId()));
            }
            else //교육카드
            {
                dataList.add(new DataItem.CardData(card.getCardName(), user.getCardBalances().get(i)[3],  StateSet.ViewType.eduCard, user.getCardBalances().get(i) , card.getCardNum() ,card.getCardType(), user.getId()));
            }
            i++;
        }

    }

}
package org.techtown.db_6;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserCardList extends AppCompatActivity {

    Intent intent;
    User user; //사용자 클래스

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        intent = getIntent();
        user = (User) intent.getSerializableExtra("user");
        System.out.println("사용자 카드"+ user.getCards());
        ArrayList<String[]> allBalances = user.getCardBalances();
        String[] balances= allBalances.get(0);
        for(int i=0;i<balances.length;i++)
        {
            System.out.print(balances[i]+ " ");
        }

        System.out.println("사용자 이름: "+user.getName());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_card_list);

    }
}
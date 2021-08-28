package org.techtown.db_6;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

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
        System.out.println("사용자 이름: "+user.getName());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_card_list);

        {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://sw-env.eba-weppawy7.ap-northeast-2.elasticbeanstalk.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();


            RetrofitService service1 = retrofit.create(RetrofitService.class);
            Log.d("tag", "1\n");

            Call<List<UserCard>> call = service1.getUserCardList(user.getId());

            Log.d("tag", "2\n");
            call.enqueue(new Callback<List<UserCard>>() {

                @Override
                public void onResponse(Call<List<UserCard>> call, Response<List<UserCard>> response) {
                    Log.d("tag", "respose성공\n");
                    if (response.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "로그인 성공", Toast.LENGTH_SHORT).show();
                        List<UserCard> result = response.body();
                        for(int i = 0; i<result.size(); i++)
                        {
                            UserCard card = result.get(i);
                            Log.d("tag","\n"+card.toString()+" "+i);
                            user.addCard(card);
                        }

                    } else {
                        Log.d("tag", "실패");
                    }

                    for(UserCard card: user.getCards())
                    {
                        System.out.println(card);
                    }
                }

                @Override
                public void onFailure(Call<List<UserCard>> call, Throwable t) {
                    Log.d("tag", "실패2" + t.getMessage());
                }

            });
        }
    }
}
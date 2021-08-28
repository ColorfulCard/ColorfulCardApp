package org.techtown.db_6;

import androidx.appcompat.app.AppCompatActivity;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_card_list);

        {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://sw-env.eba-weppawy7.ap-northeast-2.elasticbeanstalk.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();


            RetrofitService service1 = retrofit.create(RetrofitService.class);
            Log.d("tag", "1\n");

            Call<List<UserCard>> call = service1.getAll("swlove");

            Log.d("tag", "2\n");
            call.enqueue(new Callback<List<UserCard>>() {

                @Override
                public void onResponse(Call<List<UserCard>> call, Response<List<UserCard>> response) {
                    Log.d("tag", "3\n");
                    if (response.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "비밀번호가 틀립니다", Toast.LENGTH_SHORT).show();
                        List<UserCard> result = response.body();
                        for(int i = 0; i<result.size(); i++)
                        {
                            Log.d("tag","\n"+result.get(i).toString());

                        }

                    } else {
                        Log.d("tag", "실패");
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
package org.techtown.db_6;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchMemberStoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://sw-env.eba-weppawy7.ap-northeast-2.elasticbeanstalk.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitService service1 = retrofit.create(RetrofitService.class);
        Call<List<MemberStore>> call = service1.getStorebyName("김밥");
        call.enqueue(new Callback<List<MemberStore>>() {

            @Override
            public void onResponse(Call<List<MemberStore>> call, Response<List<MemberStore>> response) {
                if (response.isSuccessful()) {
                    List<MemberStore> result = response.body();
                    System.out.println(result.toString());
                } else {
                    Log.d("tag", "검색정보 가져오기 실패");
                }
            }

            @Override
            public void onFailure(Call<List<MemberStore>> call, Throwable t) {
                Log.d("tag", "검색정보 가져오기 실패2" + t.getMessage());

            }
        });
    }
}

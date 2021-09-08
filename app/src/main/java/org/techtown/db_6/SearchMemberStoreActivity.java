package org.techtown.db_6;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Toast;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchMemberStoreActivity extends AppCompatActivity {

    final int MSG_SUCCESS_SEARCH =1;
    final int MSG_SEARCH_NO_WORD=2;
    final int MSG_FAIL=0;

    SearchView searchbar;
    private List<MemberStore> results;
    private ArrayList<MapDataItem> dataList;
    RecyclerView recyclerView;
    MainHandler handler;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_memberstore);

        handler = new MainHandler();

        searchbar=findViewById(R.id.searchbar);
        searchbar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //텍스트 입력후 검색 버튼이 눌렸을 때 이벤트
                SearchMemberStoreThread thread = new SearchMemberStoreThread(query);
                thread.start();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //검색 글 한자 한자 눌렀을 때 이벤트
                return false;
            }
        });

        recyclerView = findViewById(R.id.recyclerview2);
        recyclerView.addItemDecoration(
                new DividerItemDecoration(this, R.drawable.line_divider));

        LinearLayoutManager manager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);

        recyclerView.setLayoutManager(manager); // LayoutManager 등록


    }

    class SearchMemberStoreThread extends  Thread{

        private String searchWord=null;
        public SearchMemberStoreThread(String searchWord){
            this.searchWord=searchWord;
        }

        @Override
        public void run() {

            Message message=handler.obtainMessage();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://sw-env.eba-weppawy7.ap-northeast-2.elasticbeanstalk.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            RetrofitService service1 = retrofit.create(RetrofitService.class);
            Call<List<MemberStore>> call = service1.getStorebyName(searchWord);
            call.enqueue(new Callback<List<MemberStore>>() {

                @Override
                public void onResponse(Call<List<MemberStore>> call, Response<List<MemberStore>> response) {
                    if (response.isSuccessful()) {
                        results = response.body();
                        if(results.isEmpty())
                        {
                            Log.d("tag", "검색 결과 아무것도 없음");
                            message.what=MSG_SEARCH_NO_WORD;
                            handler.sendMessage(message);
                        }
                        else //검색결과 있음
                        {
                            System.out.println(results.toString());//확인용 프린트
                            message.what=MSG_SUCCESS_SEARCH;
                            handler.sendMessage(message);
                        }
                    }else{
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

    class MainHandler extends Handler {

        @Override
        public void handleMessage(Message message) {
            switch (message.what) {
                case MSG_SUCCESS_SEARCH:
                    //메인스레드 작업, 리사이클러뷰 데이터 전달,부르기
                    initializeData();
                    recyclerView.setAdapter(new SearchResultListAdapter(dataList)); // Adapter 등록
                    break;

                case MSG_SEARCH_NO_WORD:
                    //검색결과 없음 뷰에 검색결과없음 정보 띄우기
                    break;

            }

        }
    }

    private void initializeData() {

        dataList = new ArrayList<>();
        int i=0;
        for( MemberStore store : results ){
            Log.d("tag",i+"번째 검색결과 가맹점"); //확인용
            dataList.add(new MapDataItem(store.getStore_name(),store.getStore_address(),store.getStore_type(),Code.ViewType.searchResult));
            i++;
        }
    }
}

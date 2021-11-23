package org.techtown.db_6;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchStoreActivity extends AppCompatActivity {


    private SearchView searchbar;
    private TextView no_result;
    private List<MemberStore> results;
    private ArrayList<DataItem.MapData> dataList;
    private RecyclerView recyclerView;
    private MainHandler handler;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_store);
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

        no_result=findViewById(R.id.no_result);

    }

    class SearchMemberStoreThread extends  Thread{

        private String searchWord=null;
        public SearchMemberStoreThread(String searchWord){
            this.searchWord=searchWord;
        }

        @Override
        public void run() {

            Message message=handler.obtainMessage();

            Server server = new Server();
            RetrofitService service1 = server.getRetrofitService();
            Call<List<MemberStore>> call = service1.getStorebyName(searchWord);
            call.enqueue(new Callback<List<MemberStore>>() {

                @Override
                public void onResponse(Call<List<MemberStore>> call, Response<List<MemberStore>> response) {
                    if (response.isSuccessful()) {
                        results = response.body();
                        if(results.isEmpty())
                        {
                            Log.d("tag", "검색 결과 아무것도 없음");
                            message.what= StateSet.SearchMsg.MSG_SEARCH_NO_WORD;
                            message.obj=searchWord;
                            handler.sendMessage(message);
                        }
                        else //검색결과 있음
                        {
                            System.out.println(results.toString());//확인용 프린트
                            message.what= StateSet.SearchMsg.MSG_SUCCESS_SEARCH;
                            handler.sendMessage(message);
                        }
                    }else{
                        Log.d("tag", "검색정보 가져오기 실패");
                    }
                }

                @Override
                public void onFailure(Call<List<MemberStore>> call, Throwable t) {
                    Log.d("tag", "검색정보 가져오기 실패2" + t.getMessage());
                    message.what= StateSet.SearchMsg.MSG_FAIL;
                    handler.sendMessage(message);
                }
            });

        }
    }

    class MainHandler extends Handler {

        @Override
        public void handleMessage(Message message) {
            switch (message.what) {
                case StateSet.SearchMsg.MSG_SUCCESS_SEARCH:
                    //메인스레드 작업, 리사이클러뷰 데이터 전달,부르기
                    if(no_result.getText().length()>0){
                        //이전 검색결과 없음 기록이 남아있다면
                        no_result.setText(null);
                    }
                    initializeData();
                    recyclerView.setVisibility(View.VISIBLE);
                    recyclerView.setAdapter(new SearchStoreListAdapter(dataList)); // Adapter 등록
                    break;

                case StateSet.SearchMsg.MSG_SEARCH_NO_WORD:
                    //검색결과 없음 뷰에 검색결과없음 정보 띄우기
                    recyclerView.setVisibility(View.GONE);
                    no_result.setText( "' "+(String)message.obj+" '에 관한 검색결과 없음");
                    break;

                case StateSet.SearchMsg.MSG_FAIL:
                    Toast.makeText(getApplicationContext(), "네트워크 상태를 확인해주세요", Toast.LENGTH_SHORT).show();
                    break;

            }
        }

    }


    private void initializeData() {

        dataList = new ArrayList<>();
        for(MemberStore store: results){
            dataList.add(new DataItem.MapData(store, StateSet.ViewType.searchResult));
        }
    }
}

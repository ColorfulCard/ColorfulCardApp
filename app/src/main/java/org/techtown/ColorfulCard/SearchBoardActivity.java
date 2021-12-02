package org.techtown.ColorfulCard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchBoardActivity extends AppCompatActivity {


    private SearchView searchbar;
    private TextView no_result,searchMent;
    private List<Posting> results;
    private RecyclerView recyclerView;
    private ImageView searchGalssImage;
    private Button cancelBt;
    private MainHandler handler;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_board);

        userID=getIntent().getStringExtra("userID");
        searchbar=findViewById(R.id.searchbar);
        searchGalssImage=findViewById(R.id.searchGlassImage);
        no_result=findViewById(R.id.no_result);
        searchMent=findViewById(R.id.searchMent);
        recyclerView=findViewById(R.id.recyclerview2);
        cancelBt=(Button)findViewById(R.id.button6);

        handler = new MainHandler();

        searchbar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //텍스트 입력후 검색 버튼이 눌렸을 때 이벤트
                SearchPostingThread thread = new SearchPostingThread(query);
                thread.start();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //검색 글 한자 한자 눌렀을 때 이벤트
                return false;
            }
        });

        cancelBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_left_enter, R.anim.slide_left_exit);
            }
        });

        recyclerView.addItemDecoration(
                new DividerItemDecoration(this, R.drawable.line_divider));

        LinearLayoutManager manager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);

        recyclerView.setLayoutManager(manager); // LayoutManager 등록

    }

    class SearchPostingThread extends Thread{

        private String searchWord=null;

        public SearchPostingThread(String searchWord){
            this.searchWord=searchWord;

        }
        @Override
        public void run() {
            super.run();

            Message message=handler.obtainMessage();

            Server server = new Server();
            RetrofitService service= server.getRetrofitService();
            Call<List<Posting>> call= service.getPostingbyContent(searchWord);
            call.enqueue(new Callback<List<Posting>>() {
                @Override
                public void onResponse(Call<List<Posting>> call, Response<List<Posting>> response) {
                    if(response.isSuccessful()){
                        results=response.body();

                        if(results.isEmpty()){
                            Log.d("tag","검색 결과 아무것도 없음");
                            message.what= StateSet.SearchMsg.MSG_SEARCH_NO_WORD;
                            message.obj=searchWord;
                            handler.sendMessage(message);
                        }
                        else
                        {
                            message.what= StateSet.SearchMsg.MSG_SUCCESS_SEARCH;
                            handler.sendMessage(message);
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<Posting>> call, Throwable t) {
                    message.what= StateSet.SearchMsg.MSG_FAIL;
                    handler.sendMessage(message);
                }
            });

        }
    }

    class MainHandler extends Handler{

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){

                case StateSet.SearchMsg.MSG_SUCCESS_SEARCH:
                    if(no_result.getText().length()>0){
                        //이전 검색결과 없음 기록이 남아있다면
                        no_result.setText(null);
                    }
                    searchGalssImage.setVisibility(View.INVISIBLE);
                    searchMent.setVisibility(View.INVISIBLE);

                    recyclerView.setVisibility(View.VISIBLE);
                    recyclerView.setAdapter(new PostingListAdapter((ArrayList<Posting>)results,userID,"SearchBoardActivity"));
                    break;

                case StateSet.SearchMsg.MSG_SEARCH_NO_WORD:
                    //검색결과 없음 뷰에 검색결과없음 정보 띄우기
                    recyclerView.setVisibility(View.GONE);
                    if(searchGalssImage.getVisibility()==View.VISIBLE){
                        searchGalssImage.setVisibility(View.INVISIBLE);
                        searchMent.setVisibility(View.INVISIBLE);
                    }
                    no_result.setText( "' "+(String)msg.obj+" '에 관한 포스팅 내용 검색결과 없음");
                    break;

                case StateSet.SearchMsg.MSG_FAIL:
                    Toast.makeText(getApplicationContext(), "네트워크 상태를 확인해주세요", Toast.LENGTH_SHORT).show();
                    break;

            }
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_left_enter, R.anim.slide_left_exit);
    }
}
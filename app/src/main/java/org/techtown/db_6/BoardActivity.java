package org.techtown.db_6;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BoardActivity extends AppCompatActivity {

    private Spinner sortingSpinner;
    private String[] item={"최신순","조회수","공감수"};
    private ImageButton WritingBtn;
    private TextView no_result;
    static public String userID;
    private Intent intent;


    private int offset=0;
    private MainHandler handler;
    private RecyclerView recyclerView;
    private PostingListAdapter adapter;
    private Boolean isLoading=false;

    private ArrayList<Posting> postings= new ArrayList<>();
    private String attribute=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        intent = getIntent();
        userID=intent.getStringExtra("userID");
        handler = new MainHandler();


        recyclerView= findViewById(R.id.recyclerView_3);
        recyclerView.addItemDecoration(
                new DividerItemDecoration(this, R.drawable.line_divider2));

        LinearLayoutManager manager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);

        recyclerView.setLayoutManager(manager); // LayoutManager 등록


        WritingBtn = (ImageButton) findViewById(R.id.writting);
        sortingSpinner = (Spinner)findViewById(R.id.orderby);
        no_result= findViewById(R.id.no_result);


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        sortingSpinner.setAdapter(adapter);


        WritingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(BoardActivity.this, WritePostingActivity.class);
                intent.putExtra("userID",userID);
                startActivity(intent);

            }
        });


        sortingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                isLoading=false;

                switch(position){
                    case 0 :
                        attribute="pdate";
                        break;

                    case 1:
                        attribute="vcnt";
                        break;

                    case 2:
                        attribute="hcnt";
                        break;
                }

                GetPostingsOfBoardThread thread = new GetPostingsOfBoardThread(0,attribute);
                thread.start();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




    }


    private class GetPostingsOfBoardThread extends Thread{

        int offset;
        String attribute;

        public GetPostingsOfBoardThread(int offset, String attribute){
            this.offset=offset;
            this.attribute=attribute;
        }

        @Override
        public void run() {
            super.run();
            Log.d("tag","스레드실행");
            Message message= handler.obtainMessage();

            Server server= new Server();
            RetrofitService service= server.getRetrofitService();
            Call<List<Posting>> call= service.getBoardPosting(offset,attribute);


            call.enqueue(new Callback<List<Posting>>() {
                @Override
                public void onResponse(Call<List<Posting>> call, Response<List<Posting>> response) {

                    if(response.isSuccessful()){

                        if( response.body().isEmpty()){

                            if(offset==0) {    //아예 작성된 글이 없는 경우
                                message.what = StateSet.PostingMsg.MSG_NO_POSTINGS;

                            }
                            else {  //스크롤해서 DB의 모든 게시글을 다 들고온 경우
                                message.what= StateSet.PostingMsg.MSG_ALREADY_GET_ALLPOSTINGS;

                            }
                        }
                        else if(offset==0) {
                            //처음 들고왔을 때
                            postings = (ArrayList<Posting>) response.body();
                            message.what = StateSet.PostingMsg.MSG_SUCCESS_GET_FIRST;

                        }
                        else
                        {
                            //로딩 후 들고오는 경우
                            List<Posting> results= response.body();
                            for(Posting posting: results){
                                postings.add(posting);
                                Log.d("tag",posting.getPno()+"");
                            }
                            message.what=StateSet.PostingMsg.MSG_SUCCESS_GETPOSTINGS;

                        }

                        handler.sendMessage(message);
                    }

                }

                @Override
                public void onFailure(Call<List<Posting>> call, Throwable t) {
                    message.what= StateSet.PostingMsg.MSG_FAIL;
                    handler.sendMessage(message);
                }
            });

        }
    }

    class MainHandler extends Handler{

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch(msg.what){
                case StateSet.PostingMsg.MSG_SUCCESS_GET_FIRST:
                    if(no_result.getVisibility()==View.VISIBLE){
                        no_result.setVisibility(View.GONE);
                    }
                    //처음 뷰로 보여주는거, 스크롤 초기화
                    recyclerView.setVisibility(View.VISIBLE);
                    adapter = new PostingListAdapter(postings);
                    recyclerView.setAdapter(adapter);
                    initScrollListner();
                    break;

                case StateSet.PostingMsg.MSG_SUCCESS_GETPOSTINGS:
                    adapter.notifyDataSetChanged();
                    isLoading = false;
                    break;

                case StateSet.PostingMsg.MSG_ALREADY_GET_ALLPOSTINGS:
                    Toast.makeText(getApplicationContext(),"모든 게시글을 불러왔습니다.",Toast.LENGTH_LONG).show();
                    break;

                case StateSet.PostingMsg.MSG_NO_POSTINGS:
                    no_result.setVisibility(View.VISIBLE);
                    break;

                case StateSet.PostingMsg.MSG_FAIL:
                    Toast.makeText(getApplicationContext(), "네트워크 상태를 확인해주세요", Toast.LENGTH_SHORT).show();
                    break;

            }

        }
    }

    private void initScrollListner(){

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull @NotNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull @NotNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager= (LinearLayoutManager)recyclerView.getLayoutManager();

                if(!isLoading){
                    if(layoutManager!=null && layoutManager.findLastCompletelyVisibleItemPosition()==postings.size()-1){
                        //맨 마지막 포스팅이라면?
                        loadMore();
                        isLoading=true;
                    }
                }
            }
        });
    }

    private void loadMore(){

        postings.add(null);
        adapter.notifyItemInserted(postings.size()-1);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                postings.remove(postings.size()-1);
                int scrollPosition = postings.size();
                adapter.notifyItemRemoved(scrollPosition);

                GetPostingsOfBoardThread thread = new GetPostingsOfBoardThread(postings.size(), attribute); //일단 pdate
                thread.start();

            }
        },1000);

    }

}
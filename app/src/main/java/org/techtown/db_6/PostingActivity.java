package org.techtown.db_6;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostingActivity extends AppCompatActivity {


    private Button grayheartBt;
    private Button pinkheartBt;
    private Button cmentBt;
    private Boolean Chheart;
    private EditText input1;
    Posting posting;
    TextView pid,pcontent,pdate,hcnt,ccnt,vcnt;
    Button deleteBt;
    static public String userID= BoardActivity.userID;
    private ArrayList<Comment> comments = new ArrayList<>();
    private ArrayList<Ccomment> ccomments = new ArrayList<>();
    private MainHandler handler;
    private ArrayList<DataItem.CommentData> dataList = new ArrayList<>();;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posting);
        grayheartBt = (Button)findViewById(R.id.button9);
        pinkheartBt = (Button)findViewById(R.id.button11);
        cmentBt = (Button)findViewById(R.id.button10);
        input1 = (EditText) findViewById(R.id.input1);
        Chheart = false;
        posting= (Posting) getIntent().getSerializableExtra("choicePosting");

        pid=findViewById(R.id.pid);
        pcontent= findViewById(R.id.pcontent);
        pdate=findViewById(R.id.pdate);
        hcnt=findViewById(R.id.hcnt);
        ccnt=findViewById(R.id.ccnt);
        vcnt=findViewById(R.id.vcnt);
        deleteBt= findViewById(R.id.deleteBt);

        recyclerView= findViewById(R.id.recyclerView4);

        pid.setText(posting.getPid());
        pcontent.setText(posting.getPcontent());
        pdate.setText(posting.getPdate());
        hcnt.setText(String.valueOf(posting.getHcnt()));
        ccnt.setText(String.valueOf(posting.getCcnt()));
        vcnt.setText(String.valueOf(posting.getVcnt()));

        grayheartBt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {if(!Chheart){
                Log.d("Log","false");
                //false인 경우 즉 회색하트 보여주기
                grayheartBt.setVisibility(View.INVISIBLE);
                pinkheartBt.setVisibility(View.VISIBLE);
                Chheart = true;

            }
            }
        });

        pinkheartBt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {if(Chheart){
                Log.d("Log","false");
                //false인 경우 즉 회색하트 보여주기
                grayheartBt.setVisibility(View.VISIBLE);
                pinkheartBt.setVisibility(View.INVISIBLE);
                Chheart = false;

            }
            }
        });


        final InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        cmentBt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {
                        input1.requestFocus();
                        input1.setVisibility(View.VISIBLE);
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(input1,0);
                         grayheartBt.setVisibility(View.INVISIBLE);
                         pinkheartBt.setVisibility(View.INVISIBLE);
                         cmentBt.setVisibility(View.INVISIBLE);

                    }
                });














        if(posting.getPid().equals(userID)){

            deleteBt.setVisibility(View.VISIBLE);
            //본인 글이면 삭제하기 버튼 나타남
        }

        handler = new MainHandler();

        //들어올 때 해당포스팅에 달린 댓글 대댓글있으면 들고와야한다.
        if(posting.getCcnt()>0){
            GetCmentsThread getCmentThread= new GetCmentsThread(posting.getPno());
            getCmentThread.start();
        }


    }// onCreate()..

    class GetCmentsThread extends Thread{

        int pno;
        public GetCmentsThread(int pno){
            this.pno=pno;
        }

        @Override
        public void run() {
            super.run();

            Message message = handler.obtainMessage();

            Server server = new Server();
            RetrofitService service = server.getRetrofitService();
            Call<List<Comment>> call = service.getComment(pno);

            call.enqueue(new Callback<List<Comment>>() {
                @Override
                public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {

                    if(response.isSuccessful()){

                        comments= (ArrayList<Comment>)response.body();
                        message.what= StateSet.BoardMsg.MSG_SUCCESS_GETCMENTS;
                        handler.sendMessage(message);
                    }
                }

                @Override
                public void onFailure(Call<List<Comment>> call, Throwable t) {
                    message.what= StateSet.BoardMsg.MSG_FAIL;
                    handler.sendMessage(message);

                }
            });

        }
    }

    void getCcmentsOfCment (int pno, int cno, int i){

        Server server= new Server();
        RetrofitService service= server.getRetrofitService();
        Call<List<Ccomment>> call= service.getCcomment(pno,cno);

        call.enqueue(new Callback<List<Ccomment>>() {
            @Override
            public void onResponse(Call<List<Ccomment>> call, Response<List<Ccomment>> response) {

                List<Ccomment> results= response.body();
                if(response.isSuccessful())
                    if(i==0) {
                        ccomments = (ArrayList<Ccomment>) results;
                    }
                    else
                    {
                        for(Ccomment ccment: results) {
                            ccomments.add(ccment);
                        }
                    }
            }

            @Override
            public void onFailure(Call<List<Ccomment>> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"네트워크를 확인하세요.",Toast.LENGTH_SHORT).show();
            }
        });
    }

    class CheckHeartThread extends Thread{

        @Override
        public void run() {
            super.run();
        }
    }

    class InsertCmentThread extends Thread{

        @Override
        public void run() {
            super.run();
        }
    }

    class DeletePostingOfBoard extends Thread{

        @Override
        public void run() {
            super.run();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void updatePostingOfBoard(){


    }

    //



    class MainHandler extends Handler{

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            switch(msg.what){
                case StateSet.BoardMsg.MSG_SUCCESS_GETCMENTS:

                    if(comments.isEmpty()){
                        break;
                    }
                    int i=0;

                    for(Comment cment:comments){

                        dataList.add( new DataItem.CommentData(cment,StateSet.ViewType.comment));
                        Log.d("tag","dataList에 cno:"+cment.getCno()+"추가함");
                        if(cment.getCccnt()>0){
                            int prevSize= ccomments.size();
                            getCcmentsOfCment(posting.getPno(),cment.getCno(),i++);

                            for(int j=prevSize ;j<ccomments.size();j++){
                                Ccomment ccment= ccomments.get(j);
                                dataList.add( new DataItem.CommentData(ccment,StateSet.ViewType.ccomment));
                                Log.d("tag","dataList에 cno:"+ccment.getCno()+"의 ccno:"+ccment.getCcno()+"추가함");
                            }
                        }
                    }

                    recyclerView.setAdapter(new CmentListAdapter(dataList));
                    break;

                case StateSet.BoardMsg.MSG_FAIL:
                    Toast.makeText(getApplicationContext(),"네트워크를 확인하세요.",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
}// MainActivity class..
package org.techtown.db_6;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostingActivity extends AppCompatActivity {

    BottomNavigationView navigationView;
    Context mContext;
    Posting posting;
    TextView pid,pcontent,pdate,hcnt,ccnt,vcnt;
    Button deleteBt;
    static public String userID= BoardActivity.userID;
    private ArrayList<Comment> comments = new ArrayList<>();
    private ArrayList<Ccomment> ccomments = new ArrayList<>();
    private MainHandler handler;
    private ArrayList<DataItem.CommentData> dataList = new ArrayList<>();;
    private RecyclerView recyclerView;
    private boolean isHeartPosting = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posting);
        posting= (Posting) getIntent().getSerializableExtra("choicePosting");

        navigationView= findViewById(R.id.navigationView);
        navigationView.setOnNavigationItemSelectedListener(new ItemSelectedListener());
        mContext= this.getApplicationContext();

        pid=findViewById(R.id.pid);
        pcontent= findViewById(R.id.pcontent);
        pdate=findViewById(R.id.pdate);
        hcnt=findViewById(R.id.hcnt);
        ccnt=findViewById(R.id.ccnt);
        vcnt=findViewById(R.id.vcnt);
        deleteBt= findViewById(R.id.deleteBt);

        recyclerView= findViewById(R.id.recyclerView4);

        recyclerView.addItemDecoration(
                new DividerItemDecoration(this, R.drawable.line_divider2));

        LinearLayoutManager manager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);

        recyclerView.setLayoutManager(manager); // LayoutManager 등록



        pid.setText(posting.getPid());
        pcontent.setText(posting.getPcontent());
        pdate.setText(posting.getPdate());
        hcnt.setText(String.valueOf(posting.getHcnt()));
        ccnt.setText(String.valueOf(posting.getCcnt()));
        vcnt.setText(String.valueOf(posting.getVcnt()));


        handler = new MainHandler();


        if(posting.getPid().equals(userID)){


            deleteBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(PostingActivity.this);
                    builder.setMessage("작성하신 글을 삭제하시겠습니까?");
                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            DeletePostingThread  thread = new DeletePostingThread(posting.getPno());
                            thread.start();
                        }
                    });
                    builder.setNegativeButton("취소",null);
                    builder.show();
                }
            });

            deleteBt.setVisibility(View.VISIBLE);
            //본인 글이면 삭제하기 버튼 나타남
        }


        //들어올 때 해당포스팅에 달린 댓글 대댓글있으면 들고와야한다.
        if(posting.getCcnt()>0){
            GetCmentsThread getCmentsThread= new GetCmentsThread(posting.getPno());
            getCmentsThread.start();
        }

        //들어올 때 해당포스팅이 사용자가 공감한 글인지 아닌지를 체크해야한다.
        CheckHeartPostingThread checkHeartThread = new CheckHeartPostingThread(userID,posting.getPno());
        checkHeartThread.start();


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

    class CheckHeartPostingThread extends Thread{

        private String userID;
        private int pno;

        public CheckHeartPostingThread(String userID, int pno){

            this.userID=userID;
            this.pno=pno;
        }

        @Override
        public void run() {
            super.run();

            Message message= handler.obtainMessage();

            Server server = new Server();
            RetrofitService service= server.getRetrofitService();
            Call<List<Integer>> call = service.getHeartPress(userID);

            call.enqueue(new Callback<List<Integer>>() {
                @Override
                public void onResponse(Call<List<Integer>> call, Response<List<Integer>> response) {

                    if(response.isSuccessful()){
                        List<Integer> heartPostings= response.body();

                        if(heartPostings.isEmpty()){
                                return;
                        }
                        for(Integer results: heartPostings){
                            if(results.equals(pno)){

                                message.what= StateSet.BoardMsg.MSG_SUCCESS_HEARTPRESS;
                                handler.sendMessage(message);
                                break;
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<Integer>> call, Throwable t) {
                    message.what= StateSet.BoardMsg.MSG_FAIL;
                    handler.sendMessage(message);

                }
            });
        }
    }

    class InsertCmentThread extends Thread{


        @Override
        public void run() {
            super.run();
        }
    }

    class DeletePostingThread extends Thread{

        int pno;
        public DeletePostingThread(int pno){
            this.pno=pno;
        }

        @Override
        public void run() {
            super.run();

            Message message = handler.obtainMessage();

            Server server= new Server();
            RetrofitService service= server.getRetrofitService();
            Call<Integer> call= service.deleteBoardPosting(pno);
            call.enqueue(new Callback<Integer>() {
                @Override
                public void onResponse(Call<Integer> call, Response<Integer> response) {

                    if(response.isSuccessful()){
                        Integer result= response.body();

                        if(result.intValue()==1){

                            message.what= StateSet.BoardMsg.MSG_SUCCESS_DEL_POSTING;
                            handler.sendMessage(message);

                        }else{
                            message.what= StateSet.BoardMsg.MSG_FAIL;
                            handler.sendMessage(message);

                        }
                    }
                }

                @Override
                public void onFailure(Call<Integer> call, Throwable t) {
                    message.what= StateSet.BoardMsg.MSG_FAIL;
                    handler.sendMessage(message);

                }
            });

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void updatePostingOfBoard(){


    }

    class ItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener{


        private Boolean isHeartPress=false;

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch(menuItem.getItemId())
            {

                case R.id.heart:
                    //공감하기 누른 경우
                    isHeartPress =!isHeartPress;

                    Toast.makeText(getApplicationContext(), "공감하기 버튼 누룸", Toast.LENGTH_SHORT).show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                        if(isHeartPress==true) {
                            navigationView.setItemTextColor(getResources().getColorStateList(R.color.colorheart, null));
                            navigationView.setItemIconTintList(getResources().getColorStateList(R.color.colorheart, null));
                        }
                        else
                        {
                            navigationView.setItemTextColor(getResources().getColorStateList(R.color.ddark_gray, null));
                            navigationView.setItemIconTintList(getResources().getColorStateList(R.color.ddark_gray, null));
                        }
                    }
                    break;


                case R.id.comment:
                    Toast.makeText(getApplicationContext(), "댓글달기 버튼 누룸", Toast.LENGTH_SHORT).show();

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                        if(isHeartPress==true){
                            navigationView.setItemTextColor(getResources().getColorStateList(R.color.colorheart, null));
                            navigationView.setItemIconTintList(getResources().getColorStateList(R.color.colorheart, null));

                        }
                        else{
                            navigationView.setItemTextColor(getResources().getColorStateList(R.color.ddark_gray, null));
                            navigationView.setItemIconTintList(getResources().getColorStateList(R.color.ddark_gray, null));
                        }
                        //키보드 올려주기기
                    }
                    break;

            }// switch()..
            return true;
        }
    }// ItemSelectedListener class..


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

                case StateSet.BoardMsg.MSG_SUCCESS_DEL_POSTING:

                    Toast.makeText(getApplicationContext(),"게시글이 삭제되었습니다.",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(PostingActivity.this,BoardActivity.class);
                    intent.putExtra("userID",userID);
                    startActivity(intent);
                    finish();

                    break;
                case StateSet.BoardMsg.MSG_SUCCESS_HEARTPRESS:

                    isHeartPosting = true;
                    //공감된 글이다.. 네비게이션 바 색깔 눌러진 상태로 변경하기!

                case StateSet.BoardMsg.MSG_FAIL:
                    Toast.makeText(getApplicationContext(),"네트워크를 확인하세요.",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
}// MainActivity class..
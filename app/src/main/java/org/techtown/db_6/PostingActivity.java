package org.techtown.db_6;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostingActivity extends AppCompatActivity {

    Context mContext;
    public static Posting posting;
    private TextView pid,pcontent,pdate;
    public static TextView hcnt,ccnt,vcnt;
    private ImageButton deleteBt;

    private String userID;
    private ArrayList<Comment> comments = new ArrayList<>();
    private ArrayList<Ccomment> ccomments = new ArrayList<>();
    private MainHandler handler;
    private ArrayList<DataItem.CommentData> dataList = new ArrayList<>();;
    private RecyclerView recyclerView;
    private CmentListAdapter adapter;
    public static boolean isHeartPosting = false;
    public static boolean isHeartPress=false;

    public static Button grayheartBt;
    public static Button pinkheartBt;
    public static Button cmentBt;
    public  ImageButton sendBt;
    public static EditText input1;
    public static ImageButton sendBt_Ccment;

    private String prevActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posting);
        posting= (Posting) getIntent().getSerializableExtra("choicePosting");
        userID= getIntent().getStringExtra("userID");
        prevActivity= getIntent().getStringExtra("prevActivity");

        Log.d("tag",userID+"포스팅에서");
        mContext= this.getApplicationContext();

        pid=findViewById(R.id.pid);
        pcontent= findViewById(R.id.pcontent);
        pdate=findViewById(R.id.pdate);
        hcnt=findViewById(R.id.hcnt);
        ccnt=findViewById(R.id.ccnt);
        vcnt=findViewById(R.id.vcnt);
        deleteBt= findViewById(R.id.deleteBt);

        grayheartBt = (Button)findViewById(R.id.button9);
        pinkheartBt = (Button)findViewById(R.id.button11);
        sendBt = (ImageButton)findViewById(R.id.imageButton2);
        sendBt_Ccment= (ImageButton)findViewById(R.id.imageButton3);
        cmentBt = (Button)findViewById(R.id.button10);
        input1 = (EditText) findViewById(R.id.input1);

        recyclerView= findViewById(R.id.recyclerView4);

        //   recyclerView.addItemDecoration(
        //          new DividerItemDecoration(this, R.drawable.line_divider2));

        LinearLayoutManager manager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);

        recyclerView.setLayoutManager(manager); // LayoutManager 등록

        pid.setText(posting.getPid());
        pcontent.setText(posting.getPcontent());
        pdate.setText(posting.getPdate());
        hcnt.setText(String.valueOf(posting.getHcnt()));
        ccnt.setText(String.valueOf(posting.getCcnt()));
        vcnt.setText(String.valueOf(posting.getVcnt()+1));



        grayheartBt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {
                if(!isHeartPress){
                    grayheartBt.setVisibility(View.INVISIBLE);
                    pinkheartBt.setVisibility(View.VISIBLE);
                    isHeartPress = true;

                    posting.addHcnt(+1);
                    hcnt.setText(String.valueOf(posting.getHcnt()));

                }
            }
        });

        pinkheartBt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {
                if(isHeartPress){

                    grayheartBt.setVisibility(View.VISIBLE);
                    pinkheartBt.setVisibility(View.INVISIBLE);
                    isHeartPress = false;

                    posting.addHcnt(-1);
                    hcnt.setText(String.valueOf(posting.getHcnt()));

                }
            }
        });


        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        //댓글달기 클릭시 키보드+입력창 올라옴
        cmentBt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                input1.post(new Runnable() {
                    @Override
                    public void run() {

                        grayheartBt.setVisibility(View.INVISIBLE);
                        pinkheartBt.setVisibility(View.INVISIBLE);
                        cmentBt.setVisibility(View.INVISIBLE);
                        input1.setVisibility(View.VISIBLE);
                        sendBt.setVisibility(View.VISIBLE);
                        input1.setFocusableInTouchMode(true);
                        input1.requestFocus();
                        InputMethodManager imm= (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(input1, 0);
                    }
                });
            }

        });


        sendBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String cment = input1.getText().toString();

                if (cment.equals("")) {

                    Toast.makeText(getApplicationContext(), "내용을 입력해주세요", Toast.LENGTH_SHORT).show();
                }
                else {

                    InsertCmentThread insertCmentThread= new InsertCmentThread(posting.getPno(), cment);
                    insertCmentThread.start();

                    if(isHeartPosting||isHeartPress){
                        pinkheartBt.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        grayheartBt.setVisibility(View.VISIBLE);
                    }
                    cmentBt.setVisibility(View.VISIBLE);
                    input1.setVisibility(View.GONE);
                    input1.setText("");
                    sendBt.setVisibility(View.GONE);

                    imm.hideSoftInputFromWindow(input1.getWindowToken(), 0);
                }
            }
        });


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

        if(posting.getHcnt()>0) {
            //들어올 때 해당포스팅이 사용자가 공감한 글인지 아닌지를 체크해야한다.
            CheckHeartPostingThread checkHeartThread = new CheckHeartPostingThread(posting.getPno());
            checkHeartThread.start();
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
                        message.what= StateSet.BoardMsg.MSG_SUCCESS_GET_CMENTS;
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

    class GetCcmentsThread extends Thread {

        private int pno;
        private int cno;

        public GetCcmentsThread(int pno, int cno){

            this.pno=pno;
            this.cno=cno;
        }

        @Override
        public void run() {
            super.run();

            Message message = handler.obtainMessage();

            Server server= new Server();
            RetrofitService service= server.getRetrofitService();
            Call<List<Ccomment>> call= service.getCcomment(pno);

            call.enqueue(new Callback<List<Ccomment>>() {
                @Override
                public void onResponse(Call<List<Ccomment>> call, Response<List<Ccomment>> response) {

                    if(response.isSuccessful()){

                        List<Ccomment> results= response.body();
                        ccomments= (ArrayList<Ccomment>)results;
                        message.what= StateSet.BoardMsg.MSG_SUCCESS_GET_CCMENTS;
                        handler.sendMessage(message);
                    }
                }
                @Override
                public void onFailure(Call<List<Ccomment>> call, Throwable t) {
                    message.what= StateSet.BoardMsg.MSG_FAIL;
                    handler.sendMessage(message);
                }
            });


        }

    }

    class CheckHeartPostingThread extends Thread{

        private int pno;

        public CheckHeartPostingThread(int pno){

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
                            return;     //공감누른 글이 없음
                        }

                        for(Integer results: heartPostings){
                            if(results.equals(pno)){
                                //해당글이 공감글임
                                message.what= StateSet.BoardMsg.MSG_SUCCESS_HEARTPRESS;
                                handler.sendMessage(message);
                                Log.d("tag","공감글이다.");
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

        private int pno;
        private String cment;
        private int cno;

        public InsertCmentThread(int pno, String cment){

            this.pno=pno;
            this.cment=cment;

            if(comments.isEmpty()){
                cno=1;
            }else{

                for(Comment comment: comments){         //제일큰 cno검증
                    if(comment.getCno()>cno)
                    {
                        cno=comment.getCno();
                    }
                }
                cno++;
            }
        }

        @Override
        public void run() {
            super.run();

            Message message= handler.obtainMessage();

            Server server = new Server();
            RetrofitService service= server.getRetrofitService();
            Log.d("tag",pno+" "+cno+" "+ userID+" "+cment+"인서트시도");
            Call<Comment>call = service.postComment(pno,cno,userID,cment);

            call.enqueue(new Callback<Comment>() {
                @Override
                public void onResponse(Call<Comment> call, Response<Comment> response) {
                    if(response.isSuccessful()){

                        Comment result= response.body();

                        if(!result.equals(null)){
                            Log.d("tag","인서트성공");
                            updateCmentCnt(pno,"plus");
                            comments.add(result);
                            dataList.add(new DataItem.CommentData(result,StateSet.ViewType.comment));

                            message.what= StateSet.BoardMsg.MSG_SUCCESS_INSERT_CMENT;
                            handler.sendMessage(message);
                        }
                    }
                }

                @Override
                public void onFailure(Call<Comment> call, Throwable t) {
                    message.what= StateSet.BoardMsg.MSG_FAIL;
                    handler.sendMessage(message);
                }
            });

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

        if(input1.getVisibility()==View.VISIBLE){

            if(isHeartPosting||isHeartPress){
                pinkheartBt.setVisibility(View.VISIBLE);
            }
            else
            {
                grayheartBt.setVisibility(View.VISIBLE);
            }
            cmentBt.setVisibility(View.VISIBLE);
            input1.setVisibility(View.GONE);
            input1.setText("");
            sendBt.setVisibility(View.GONE);
        }
        else {

            //공감하기,조회수 바뀐정보있으면 업데이트
            updatePostingOfBoard(posting.getPno());


            //검색 결과로 들어온 액티비티라면
            if(prevActivity.equals("SearchBoardActivity")){

                //이전에 불렀던 검색 액티비티가 남아있으며 거기로 넘어간다.
                super.onBackPressed();
                finish();
                overridePendingTransition(R.anim.slide_left_enter, R.anim.slide_left_exit);

            }
            else {

                //게시판 조회에서 바로 들어온 액티비티라면 다시조회에서 boardActivity 띄우기
                super.onBackPressed();

                Intent intent= new Intent(PostingActivity.this, BoardActivity.class);
                intent.putExtra("userID",userID);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_left_enter, R.anim.slide_left_exit);
                finish();
            }

        }
    }

    private void updateCmentCnt(int pno, String sign){

        Server server =new Server();
        RetrofitService service= server.getRetrofitService();
        Call<Integer> call= service.putCommentCnt(pno,sign);
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                Log.d("tag","댓글수 1증가됨");
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {

            }
        });
    }

    private void updatePostingOfBoard(int pno){


        Server server =new Server();
        RetrofitService service= server.getRetrofitService();

        Call<Integer> call= service.putViewsCnt(posting.getPno());
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if(response.isSuccessful())
                {
                    //조회수 1증가
                    Log.d("tag","조회수 1증가됨");
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {

            }
        });

        //공감 유무변경
        Call<Integer> call2;
        Call<Integer> call3;

        if(isHeartPosting)  //원래 공감글인데
        {
            if(!isHeartPress)    //하트 해제된 상태라면
            {
                call2 = service.putHeartCnt(pno,"minus");
                call2.enqueue(new Callback<Integer>() {
                    @Override
                    public void onResponse(Call<Integer> call, Response<Integer> response) {
                        if(response.isSuccessful())
                        {
                            Log.d("tag","hcnt--됨");
                        }
                    }

                    @Override
                    public void onFailure(Call<Integer> call, Throwable t) {

                    }
                });

                call3= service.deleteHeartPress(pno,userID);
                call3.enqueue(new Callback<Integer>() {
                    @Override
                    public void onResponse(Call<Integer> call, Response<Integer> response) {
                        if(response.isSuccessful())
                        {
                            Log.d("tag","공감하기 해제됨");
                        }
                    }

                    @Override
                    public void onFailure(Call<Integer> call, Throwable t) {

                    }
                });

            }
        }else{
            //원래 공감아닌 글인데
            if(isHeartPress)    //하트 눌러진 상태라면
            {
                call2=service.putHeartCnt(pno,"plus");
                call2.enqueue(new Callback<Integer>() {
                    @Override
                    public void onResponse(Call<Integer> call, Response<Integer> response) {
                        if(response.isSuccessful())
                        {
                            Log.d("tag","hcnt++됨");
                        }
                    }

                    @Override
                    public void onFailure(Call<Integer> call, Throwable t) {

                    }
                });

                call3= service.postHeartPress(pno,userID);
                call3.enqueue(new Callback<Integer>() {
                    @Override
                    public void onResponse(Call<Integer> call, Response<Integer> response) {
                        if(response.isSuccessful())
                        {
                            Log.d("tag","공감하기 등록됨");
                        }
                    }

                    @Override
                    public void onFailure(Call<Integer> call, Throwable t) {

                    }
                });

            }
        }
    }


    class MainHandler extends Handler{

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            switch(msg.what){
                case StateSet.BoardMsg.MSG_SUCCESS_GET_CMENTS:

                    //일부러 따로해줌
                    Boolean cmentWithCcment=false;
                    for(Comment cment:comments){

                        if(cment.getCccnt()>0){

                            GetCcmentsThread getCcmentsThread = new GetCcmentsThread(posting.getPno(),cment.getCno());
                            getCcmentsThread.start();
                            cmentWithCcment=true;
                            break; //하나라도 대댓글 가진 댓글이 있다면 그 포스팅에 달린 모든 대댓글을 전부 다 들고옴
                        }
                    }

                    if(cmentWithCcment==false){ //대댓글 하나도 없을 경우 바로 보여줌

                        for(Comment cment:comments){

                            dataList.add( new DataItem.CommentData(cment,StateSet.ViewType.comment));
                            Log.d("tag","dataList에 cno:"+cment.getCno()+"추가함");
                            Log.d("tag",cment.getCccnt()+"=cccnt");
                        }

                        recyclerView.setVisibility(View.VISIBLE);
                        adapter=new CmentListAdapter(dataList,userID);
                        recyclerView.setAdapter(adapter);

                    }
                    break;

                case StateSet.BoardMsg.MSG_SUCCESS_GET_CCMENTS:

                    //대댓글 받아오면 dataList를 리사이클러뷰 구성할 수있도록 순서 맞춰줘야함.
                    //에혀 걍 DB에서 댓글별로 대댓글 각각 들고오는거 말고 포스팅별로 대댓글 전체 다 들고오는게 훨씬 더 편했을 듯.  ---->수정함 12/01

                    for(Comment cment: comments){

                        dataList.add( new DataItem.CommentData(cment,StateSet.ViewType.comment));
                        Log.d("tag","dataList에 cno:"+cment.getCno()+" 추가함 cccnt="+cment.getCccnt());

                        for(Ccomment ccment: ccomments)
                        {
                            if(cment.getCno()==ccment.getCno()){
                                dataList.add(new DataItem.CommentData(ccment,StateSet.ViewType.ccomment));
                                Log.d("tag", "dataList에 cno:" + ccment.getCno() + "의 ccno:" + ccment.getCcno() + " 추가함");

                            }
                        }
                    }

                    Log.d("tag",dataList.size()+"");
                    //나중에 ccnt 수 완벽히 맞춰지면 카운트랑 비교해서 리사이클러뷰 보여주기 지금약간 불안정함 코드가
                    if(dataList.size()==posting.getCcnt())
                    {
                        //처음 뷰로 보여주는거
                        Log.d("tag","리사이클러뷰보여줌");
                        recyclerView.setVisibility(View.VISIBLE);
                        adapter = new CmentListAdapter(dataList,userID);
                        recyclerView.setAdapter(adapter);

                    }
                    break;


                case StateSet.BoardMsg.MSG_SUCCESS_DEL_POSTING:

                    Toast.makeText(getApplicationContext(),"게시글이 삭제되었습니다.",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(PostingActivity.this,BoardActivity.class);
                    intent.putExtra("userID",userID);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_left_enter, R.anim.slide_left_exit);
                    finish();
                    break;

                case StateSet.BoardMsg.MSG_SUCCESS_INSERT_CMENT:
                    //리사이클러뷰 인서트된 댓글 포함해서 다시 보여주기
                    adapter.notifyDataSetChanged();
                    ccnt.setText(String.valueOf((posting.getCcnt()+1)));
                    posting.addCcnt(+1);
                    break;


                case StateSet.BoardMsg.MSG_SUCCESS_HEARTPRESS:
                    Log.d("tag","공감글인거 확인함");
                    isHeartPosting = true;
                    isHeartPress=true;
                    //공감된 글이다.. 버튼 눌러진 상태로 변경하기!
                    grayheartBt.setVisibility(View.INVISIBLE);
                    pinkheartBt.setVisibility(View.VISIBLE);
                    break;

                case StateSet.BoardMsg.MSG_FAIL:
                    Toast.makeText(getApplicationContext(),"네트워크를 확인하세요.",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
}// MainActivity class..
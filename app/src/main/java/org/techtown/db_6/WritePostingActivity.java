package org.techtown.db_6;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WritePostingActivity extends AppCompatActivity {

    Button completeBt;
    Button postingListBt;
    EditText inputContent;
    TextView NotifyWordCnt;
    boolean checkPostingContent= false;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userID=getIntent().getStringExtra("userID");

        setContentView(R.layout.activity_write_posting);
        completeBt = (Button)findViewById(R.id.completeBt) ;
        postingListBt = (Button)findViewById(R.id.postingListBt);
        inputContent = findViewById(R.id.inputContent);
        NotifyWordCnt = findViewById(R.id.NotifyWordCnt);

        inputContent.addTextChangedListener(new TextWatcher() {
            //사용자가 글 쓸때마다 몇글자 적었는지 알려줌

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //사용자가 글을 썼다면
                if (s.toString().length() > 0) {
                    NotifyWordCnt.setText(s.toString().length() + "/300");
                    checkPostingContent = true;
                } else {//아니라면
                    NotifyWordCnt.setText("0/300");
                    checkPostingContent = false;
                }
            }
        });



        completeBt.setOnClickListener(new View.OnClickListener() {
            //작성완료 버튼 클릭 시
            @Override
            public void onClick(View v) {

                if(checkPostingContent) {
                    //사용자가 글을 작성한 경우
                    insertPostingToBoard();
                 }else{

                    Toast.makeText(getApplicationContext(), "내용을 작성해주세요.", Toast.LENGTH_SHORT).show();

                }

            }
        });

        postingListBt.setOnClickListener(new View.OnClickListener() {
            //글목록 버튼 클릭 시
            @Override
            public void onClick(View v) {

               finish();

            }
        });

    }

    private void insertPostingToBoard(){

        Server server= new Server();
        RetrofitService service= server.getRetrofitService();
        Call<Integer> call= service.postBoardPosting(userID,inputContent.getText().toString());

        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if(response.isSuccessful()){
                    Integer result= response.body();

                    if(result.intValue()==1){

                        Intent intent = new Intent(WritePostingActivity.this, BoardActivity.class);
                        intent.putExtra("userID",userID);
                        startActivity(intent);

                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "게시글 작성이 취소되었습니다. 다시 시도하십시오.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "네트워크를 확인해주세요.", Toast.LENGTH_SHORT).show();

            }
        });

    }
}
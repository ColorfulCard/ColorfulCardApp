package org.techtown.db_6;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class WritePostingActivity extends AppCompatActivity {

    Button completeBt;
    Button postingListBt;
    EditText inputContent;
    TextView NotifyWordCnt;
    boolean checkPostingContent= false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

                Intent intent = new Intent(WritePostingActivity.this, BoardActivity.class);
                startActivity(intent);

            }
        });

        postingListBt.setOnClickListener(new View.OnClickListener() {
            //글목록 버튼 클릭 시
            @Override
            public void onClick(View v) {


                if(checkPostingContent) {
                    //사용자가 글을 작성한 경우
                    Intent intent = new Intent(WritePostingActivity.this, BoardActivity.class);
                    startActivity(intent);
                }else{

                    Toast.makeText(getApplicationContext(), "내용을 작성해주세요.", Toast.LENGTH_SHORT).show();

                }
            }
        });















    }
}
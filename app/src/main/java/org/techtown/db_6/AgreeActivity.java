package org.techtown.db_6;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

public class AgreeActivity extends AppCompatActivity {
    private Button startbutton1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agree);

        CheckBox checkBox=findViewById(R.id.checkBox2);
        CheckBox checkBox2=findViewById(R.id.checkBox3);
        CheckBox checkBox3=findViewById(R.id.checkBox4);
        CheckBox checkBox4=findViewById(R.id.checkBox5);
        startbutton1 = (Button) findViewById(R.id.startbutton1);


        checkBox.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if(checkBox.isChecked()){
                    checkBox2.setChecked(true);
                    checkBox3.setChecked(true);
                    checkBox4.setChecked(true);
                }
                else{
                    checkBox2.setChecked(false);
                    checkBox3.setChecked(false);
                    checkBox4.setChecked(false);
                }

            }
        });
        checkBox2.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){

                if(checkBox.isChecked()){
                    checkBox.setChecked(false);
                }
                else if(checkBox2.isChecked()&&checkBox3.isChecked()&&checkBox4.isChecked()){
                    checkBox.setChecked(true);
                }

            }
        });

        checkBox3.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if(checkBox.isChecked()){
                    checkBox.setChecked(false);

                }
                else if(checkBox2.isChecked()&&checkBox3.isChecked()&&checkBox4.isChecked()){
                    checkBox.setChecked(true);
                }

            }
        });

        checkBox4.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if(checkBox.isChecked()){
                    checkBox.setChecked(false);
                }
                else if(checkBox2.isChecked()&&checkBox3.isChecked()&&checkBox4.isChecked()){
                    checkBox.setChecked(true);
                }
            }

        });

        startbutton1.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(checkBox.isChecked()&&checkBox2.isChecked()&&checkBox3.isChecked()&&checkBox4.isChecked()){
                    Intent i = new Intent(getApplicationContext(), JoinActivity.class);
                    startActivity(i);
                }
                else if(checkBox2.isChecked()&&checkBox3.isChecked()){
                    Intent i = new Intent(getApplicationContext(), JoinActivity.class);
                    startActivity(i);
                }
                else{

                    Toast.makeText(getApplicationContext(),"필수약관동의에 체크해주세요",Toast.LENGTH_LONG).show();


                }

            }
        });
    }


}
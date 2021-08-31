package org.techtown.db_6;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegiCardActivity extends AppCompatActivity {
    Intent intent;
    User user;

    private EditText et_cardName, editTextNumberDecimal, editTextNumberDecimal2, editTextNumberDecimal3, editTextNumberDecimal4;
    private CheckBox chMeal, chBusic;
    private Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        intent = getIntent();
        user = (User) intent.getSerializableExtra("user");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_card);


        et_cardName = findViewById(R.id.et_cardName);
        editTextNumberDecimal =  findViewById(R.id.editTextNumberDecimal);
        editTextNumberDecimal2 = findViewById(R.id.editTextNumberDecimal2);
        editTextNumberDecimal3 = findViewById(R.id.editTextNumberDecimal3);
        editTextNumberDecimal4 = findViewById(R.id.editTextNumberDecimal4);

        chMeal = (CheckBox) findViewById(R.id.chMeal);
        chBusic = (CheckBox) findViewById(R.id.chBusic);

        button = findViewById(R.id.button);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String cardName = et_cardName.getText().toString();
                StringBuilder TextNumberDecimal = new StringBuilder();
                TextNumberDecimal.append(editTextNumberDecimal.getText().toString());
                TextNumberDecimal.append(editTextNumberDecimal2.getText().toString());
                TextNumberDecimal.append(editTextNumberDecimal3.getText().toString());
                TextNumberDecimal.append(editTextNumberDecimal4.getText().toString());

                Log.d("tag","\n"+TextNumberDecimal);
                //값이 16자리 무사히 합쳐졌는지

                boolean mealCard = true;

                if(chMeal.isChecked()) {
                    mealCard = true;
                }
                if(chBusic.isChecked()){
                    mealCard = false;

                }


                if(cardName.equals("") || editTextNumberDecimal.equals("") || editTextNumberDecimal2.equals("") || editTextNumberDecimal3.equals("") || editTextNumberDecimal4.equals("")
                        || (chMeal.isChecked()== false&&chBusic.isChecked()== false) || (chMeal.isChecked()== true&&chBusic.isChecked()== true))
                //연산순위가 ==가 &&보다 더 높음

                {
                    Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("http://sw-env.eba-weppawy7.ap-northeast-2.elasticbeanstalk.com/")
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();


                    RetrofitService service1 = retrofit.create(RetrofitService.class);
                    Log.d("tag", "1\n");

                    Call<UserCard> call = service1.postUserCard(TextNumberDecimal.toString(),"swlove2",cardName,mealCard);

                    Log.d("tag", "2\n");
                    call.enqueue(new Callback<UserCard>() {

                        @Override
                        public void onResponse(Call<UserCard> call, Response<UserCard> response) {
                            if (response.isSuccessful()) {
                                UserCard result = response.body();
                                Intent intent = new Intent(RegiCardActivity.this, HomeActivity.class); //일단은 로그인 성공하면 해당 id가 가진 카드리스트 보여주는 화면으로 이동
                                intent.putExtra("user",user);
                                startActivity(intent);

                                Log.d("tag",result.toString());

                            } else {
                                Log.d("tag", "실패");
                            }

                        }

                        @Override
                        public void onFailure(Call<UserCard> call, Throwable t) {
                            Log.d("tag", "실패2" + t.getMessage());
                        }
                    });
                }
            }
        });

    }
}
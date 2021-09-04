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

                boolean mealCard = true;

                if(chMeal.isChecked()) {
                    mealCard = true;
                }
                if(chBusic.isChecked()){
                    mealCard = false;

                }

                if(cardName.equals("") || editTextNumberDecimal.equals("") || editTextNumberDecimal2.equals("") || editTextNumberDecimal3.equals("") || editTextNumberDecimal4.equals("")
                        || (chMeal.isChecked()== false&&chBusic.isChecked()== false) || (chMeal.isChecked()== true&&chBusic.isChecked()== true))
                {
                    Toast.makeText(getApplicationContext(), "모두 기입하였는지 확인하세요", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("http://sw-env.eba-weppawy7.ap-northeast-2.elasticbeanstalk.com/")
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();


                    RetrofitService service1 = retrofit.create(RetrofitService.class);
                    Call<UserCard> call = service1.postUserCard(TextNumberDecimal.toString(),user.getId(),cardName,mealCard);
                    call.enqueue(new Callback<UserCard>() {

                        @Override
                        public void onResponse(Call<UserCard> call, Response<UserCard> response) {
                            if (response.isSuccessful()) {
                                UserCard result = response.body();
                                Intent intent = new Intent(RegiCardActivity.this, HomeActivity.class); //일단은 로그인 성공하면 해당 id가 가진 카드리스트 보여주는 화면으로 이동
                                user.clearCardBalances();
                                intent.putExtra("user",user);
                                startActivity(intent);
                                finish();

                            } else {
                                Log.d("tag", "get한 후 카드 하나만 받아오게 해서 난 실패임 spring수정하면됨");
                                UserCard result = response.body();
                                Intent intent = new Intent(RegiCardActivity.this, HomeActivity.class);
                                user.clearCardBalances();
                                intent.putExtra("user",user);
                                startActivity(intent);
                                finish();
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
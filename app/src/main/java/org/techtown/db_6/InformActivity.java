package org.techtown.db_6;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NotNull;

public class InformActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inform);

        ImageView meal = findViewById(R.id.imageView5);
        ImageView sideMeal = findViewById(R.id.imageView6);
        ImageView edu = findViewById(R.id.imageView7);

        TextView tv1 = findViewById(R.id.textView15);
        TextView tv2 = findViewById(R.id.textView16);
        TextView tv3 = findViewById(R.id.textView20);
        TextView tv4 = findViewById(R.id.textView21);
        TextView tv5 = findViewById(R.id.textView17);
        TextView tv6 = findViewById(R.id.textView18);
        TextView tv7 = findViewById(R.id.textView14);
        TextView tv8 = findViewById(R.id.textView22);
        TextView tv9 = findViewById(R.id.textView23);
        TextView tv10 = findViewById(R.id.textView24);
        TextView tv11 = findViewById(R.id.textView25);

        Button mealBt = findViewById(R.id.button8);
        Button sideBt = findViewById(R.id.button12);
        Button eduBt = findViewById(R.id.button13);

        mealBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mealBt.setTextColor(0xFFFF9800);
                sideBt.setTextColor(0xFF6E6E6E);
                eduBt.setTextColor(0xFF6E6E6E);

                Log.d("tag", "급식누름");
                meal.setVisibility(View.VISIBLE);
                sideMeal.setVisibility(View.INVISIBLE);
                edu.setVisibility(View.INVISIBLE);

                tv1.setText("1일 충전 금액 : 5,000원\n월/일 사용한도내에서 사용 가능함\n당월 잔액은 이월되며, 연말(12월31일 24시:00분)에 소멸 됨\n매월 1일 새벽 0시부터 0시30분까지\n자동충전 시간이므로 사용이 불가함");
                tv2.setText("사용한도액 (1식 사용자 기준)");
                tv3.setText("1일 1회 최대 15,000원 사용 가능\n1일 한도내에서 카드 사용 횟수 상관없이 사용이 가능\n토.공 급식대상자는 평일 사용 불가함\n방학 중 급식대상자는 방학기간에만 사용 가능함\n평일 대상자는 토.공.일은 사용 불가함");
                tv4.setText("사용한도액 (2식.3식 사용자 기준)");
                tv5.setText("1일 1회 최대 15,000원이며, 2회 30,000원\n내에서 사용 횟수 관계 없이 자유롭게 사용 가능함");
                tv6.setText("사용처");
                tv7.setText("지정 음식점,\n편의점(GS25, CU, 세븐일레븐, e-mart24, e-mart푸드코트)");
                tv8.setText("편의점 사용방법");
                tv9.setText("편의점은 지정된 품목만 구입 가능함 \n(술. 담배.생활용품 등은 구입 불가함)");
                tv10.setText("비고");
                tv11.setText("지정 가맹점 이외에는 사용 불가\n병행 사용불가(급식은 급식가맹점에서만 사용가능)");

            }
        });

        sideBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mealBt.setTextColor(0xFF6E6E6E);
                sideBt.setTextColor(0xFFFF9800);
                eduBt.setTextColor(0xFF6E6E6E);

                Log.d("tag", "부식누름");
                meal.setVisibility(View.INVISIBLE);
                sideMeal.setVisibility(View.VISIBLE);
                edu.setVisibility(View.INVISIBLE);


                tv1.setText("1일 충전 금액 : 5,000원\n월/일 사용한도내에서 사용 가능함\n당월 잔액은 이월되며, 연말(12월31일 24시:00분)에 소멸 됨\n매월 1일 새벽 0시부터 0시30분까지\n자동충전 시간이므로 사용이 불가함");
                tv2.setText("사용한도액 (1식 사용자 기준)");
                tv3.setText("1일 최대 30,000원 사용 가능함\n평일, 토,공 관계없이 사용 가능함\n1일 한도 내에서 사용 횟수 관계없이 사용이 가능함");
                tv4.setText("사용한도액 (2식.3식 사용자 기준)");
                tv5.setText("1일 1회 최대 30,000원이며, 2회 60,000원\n내에서 사용 횟수 관계 없이 자유롭게 사용 가능함");
                tv6.setText("사용처");
                tv7.setText("지정 부식가맹점");
                tv8.setText("편의점 사용방법");
                tv9.setText("지정 부식가맹점");
                tv10.setText("비고");
                tv11.setText("지정 가맹점 이외에는 사용 불가\n병행 사용불가(부식은 부식가맹점에서만 사용가능)");

            }
        });

        eduBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mealBt.setTextColor(0xFF6E6E6E);
                sideBt.setTextColor(0xFF6E6E6E);
                eduBt.setTextColor(0xFFFF9800);

                Log.d("tag", "교육누름");


                meal.setVisibility(View.INVISIBLE);
                sideMeal.setVisibility(View.INVISIBLE);
                edu.setVisibility(View.VISIBLE);

                tv1.setText("교육카드 상세설명");
                tv2.setText("지원금액");
                tv3.setText("초등생 : 75,000원\n중등생 : 85,000원\n고등생 : 160,000원");
                tv4.setText("사용방법");
                tv5.setText("지자체 지원 : 50%\n학원 부담 : 40%\n본인 부담 : 10%");
                tv6.setText("사용기간 및 시기");
                tv7.setText("매년 03월 ~ 12월 사용\n매월 수강 신청 시 언제든지 결제 가능");
                tv8.setText("지원금액 충전방법 및 소멸");
                tv9.setText("매월 01일 충전\n매월 말일 소멸");
                tv10.setText("비고");
                tv11.setText("지원 금액에 따른 학원 수강료의 차이에 따라 학원 및 본인 부담금은 변경 될 수 있음.");

            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_left_enter, R.anim.slide_left_exit);
    }
}

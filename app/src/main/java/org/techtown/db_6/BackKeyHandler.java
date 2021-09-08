package org.techtown.db_6;

import android.app.Activity;
import android.widget.Toast;

public class BackKeyHandler {

    private long backKeyPressedTime = 0;
    private Activity activity;//뒤로가기 버튼이 동작할 activity에 클래스를 동작시킴
    private Toast toast;

    public BackKeyHandler(Activity activity){
        this.activity=activity;
    }

    public void onBackPressed(){
        if(System.currentTimeMillis()>backKeyPressedTime+2000){

            backKeyPressedTime = System.currentTimeMillis();

            showGuide();
            return;
        }
        if(System.currentTimeMillis()<=backKeyPressedTime+2000){
            activity.finish();
            toast.cancel();
        }
    }

    private void showGuide(){
        toast=Toast.makeText(activity,"뒤로 버튼을 한번 더 누르시면 종료됩니다.",Toast.LENGTH_SHORT);
        toast.show();
    }

    private void showGuide(String msg){
        toast=Toast.makeText(activity,msg,Toast.LENGTH_SHORT);
        toast.show();
    }

}




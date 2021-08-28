package org.techtown.db_6;

import com.google.gson.annotations.SerializedName;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

//서버에 사용자 프로필 post&get 할 때 쓰이는 클래스 (로그인,회원가입)
public class UserProfile {

    @SerializedName("id")
    private String id;

    @SerializedName("pwd")
    private String pwd;
    // @SerializedName으로 일치시켜 주지않을 경우엔 클래스 변수명이 일치해야함

    @SerializedName("name")
    private String name;

    public UserProfile(String id, String pwd, String name) {
    }

    public String getPwd() {
        return pwd;
    }
    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }

    Boolean isValidID(){
        if(id.equals(""))
            return false;
        else
            return true;
    }
}

    
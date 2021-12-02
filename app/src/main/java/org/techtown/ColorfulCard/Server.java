package org.techtown.ColorfulCard;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Server {

    private  Retrofit retrofit ;
    private RetrofitService service;

    public Server(){
        retrofit= new Retrofit.Builder()
                .baseUrl("http://sw-env.eba-weppawy7.ap-northeast-2.elasticbeanstalk.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
   public RetrofitService getRetrofitService(){
        service= retrofit.create(RetrofitService.class);
        return service;
    }

}

package org.techtown.db_6;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetrofitService {

    // @GET( EndPoint-자원위치(URI) )
    // @POST("user/15")
    //Call<UserProfile> setPostBody(@Body UserProfile post);

    @FormUrlEncoded
    @POST("user/{id}")
    Call<UserProfile> setPostField(
            @Path("id") String id,
            @Field("pwd") String pwd,
            @Field("name") String name
    );

    @GET("user/{id}")
    Call<UserProfile> getPosts(@Path("id") String id);

    @FormUrlEncoded
    @POST("card/{cardNum}")
    Call<UserCard> postUserCard(
            @Path("cardNum") String cardNum,
            @Field("id") String id,
            @Field("cardName") String cardName,
            @Field("mealCard") boolean mealCard
    );


    // @GET("userCount/{id}")
    //  Call<Integer> getUserCount(@Path("id") String id);



}
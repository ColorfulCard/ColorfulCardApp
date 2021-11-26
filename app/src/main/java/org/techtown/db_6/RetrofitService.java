package org.techtown.db_6;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RetrofitService {

    // @GET( EndPoint-자원위치(URI) )
    // @POST("user/15")
    //Call<UserProfile> setPostBody(@Body UserProfile post);

    //--------------------------UserProfile---------------------------//
    @FormUrlEncoded
    @POST("user/{id}")
    Call<UserProfile> postUserProfile(
            @Path("id") String id,
            @Field("pwd") String pwd,
            @Field("name") String name,
            @Field("email") String email
    );

    @GET("user/{id}")
    Call<UserProfile> getUserProfile(@Path("id") String id);

    @GET("userEmail/{email}")
    Call<UserProfile> getUserProfilebyEmail(@Path("email") String email);

    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "/user/delete", hasBody = true)
    Call<Integer> deleteUser (@Field("id") String id);

    //-------------------------------UserCard------------------------------//

    @GET("cardID/{id}")
    Call<List<Card>> getUserCardList(@Path("id") String id);

    @FormUrlEncoded
    @POST("card/{cardNum}")
    Call<Card> postUserCard(
            @Path("cardNum") String cardNum,
            @Field("id") String id,
            @Field("cardName") String cardName,
            @Field("cardType") String cardType
    );

    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "/card/delete", hasBody = true)
    Call<Integer> deleteUserCard(@Field("cardNum") String cardNum,
                                 @Field("id") String id,
                                 @Field("cardName") String cardName,
                                 @Field("cardType") String cardType);


    //-------------------------------MemberStore------------------------------//
    @GET("storeType/{stype}")
    Call<List<MemberStore>> getStorebyType (@Path("stype") String stype);

    @GET("storeName/{sname}") //이름으로 검색해서 가져오는 것
    Call<List<MemberStore>> getStorebyName (@Path("sname") String sname);

    @GET("storeAll")
    Call<List<MemberStore>> getAllStore();

    //-------------------------------FavoriteStore------------------------------//

    @GET("favorStore/{uid}")
    Call<List<Integer>> getFavoriteStore(@Path("uid") String uid);

    @FormUrlEncoded
    @POST("favorStore/{uid}")
    Call<Integer> postFavoriteStore(
            @Path("uid") String uid,
            @Field("sid")int sid);

    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "/favorStore/delete", hasBody = true)
    Call<Integer> deleteFavoriteStore(@Field("uid") String uid,
                                 @Field("sid") int sid);



}
package org.techtown.db_6;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.PUT;
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


    //-------------------------------Posting------------------------------//

    @GET("posting/{offset}/{attribute}")
    Call<List<Posting>> getBoardPosting(@Path("offset") int offset, @Path("attribute") String attribute);

    @GET("postingContent/{pcontent}")
    Call<List<Posting>> getPostingbyContent(@Path("pcontent")String pcontent);

    @FormUrlEncoded
    @POST("posting/{pid}")
    Call<Integer> postBoardPosting(@Path("pid") String pid, @Field("pcontent") String pcontent);

    @PUT("posting/ccnt/{pno}")
    Call<Integer> putCommentCnt(@Path("pno")int pno,@Field("sign") String sign);  //sign에 plus or minus

    @PUT("posting/hcnt/{pno}")
    Call<Integer> putHeartCnt(@Path("pno")int pno,@Field("sign")String sign);   //sign에 plus or minus                                                                                    )

    @PUT("posting/vcnt/{pno}")
    Call<Integer> putViewsCnt(@Path("pno")int pno);

    @FormUrlEncoded
    @DELETE("posting/delete")
    Call<Integer> deleteBoardPosting(@Field("pno")int pno);


    //-------------------------------Comment------------------------------//

    @GET("comment/{pno}")
    Call<List<Comment>> getComment(@Path("pno")int pno);

    @FormUrlEncoded
    @POST("comment/{pno}")
    Call<Integer> postComment(@Path("pno")int pno, @Field("cno")int cno, @Field("cid")String cid, @Field("cment")String cment);

    @PUT("comment/{pno}")
    Call<Integer> putCcommentCnt(@Path("pno")int pno, @Field("cno")int cno, @Field("sign") String sign); //sign==plus or minus

    @FormUrlEncoded
    @DELETE("comment/delete")
    Call<Integer> deleteComment(@Field("pno")int pno,@Field("cno")int cno);


    //-------------------------------Ccomment------------------------------//

    @GET("ccomment/{pno}/{cno}")
    Call<List<Ccomment>> getCcomment(@Path("pno")int pno,@Path("cno")int cno);

    @FormUrlEncoded
    @POST("ccomment/{pno}")
    Call<Integer> postCcomment(@Path("pno")int pno, @Field("cno")int cno, @Field("ccno")int ccno, @Field("ccid")String ccid, @Field("ccment")String ccment);

    @FormUrlEncoded
    @DELETE("ccomment/delete")
    Call<Integer> deleteCcomment(@Field("pno")int pno, @Field("cno")int cno, @Field("ccno") int ccno);


    //-------------------------------HeartPress------------------------------//

    @GET("heartPress/{hid}")
    Call<List<Integer>> getHeartPress(@Path("hid")String hid);

    @FormUrlEncoded
    @POST("heartPress/{pno}")
    Call<Integer> postHeartPress(@Path("pno")int pno, @Field("hid")String hid);

    @FormUrlEncoded
    @DELETE("heartPress/delete")
    Call<Integer> deleteHeartPress(@Field("pno")int pno, @Field("hid") String hid);
}
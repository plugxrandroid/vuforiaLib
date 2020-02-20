package com.unity3d.player;



import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by SURYA-RGV on 27-09-2018.
 */

public interface Api {

    String BASE_URL = "https://rnd.plugxr.com/";

    String BASE_URL_UNITY = "https://rnd.plugxr.com";

    @POST("api/v2/AppUserDetails")
    @FormUrlEncoded
    Call<Userdetails> postUserDetails(@Field("device_id") String android_id, @Field("location") String location, @Field("fcm_token") String fcm_token, @Field("device_type") String device_type, @Field("app_id") String app_id);



}

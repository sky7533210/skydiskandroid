package com.sky.disk.retrofit;

import java.util.List;
import java.util.Map;

import com.sky.disk.bean.Chunk;
import com.sky.disk.bean.FileBean;
import com.sky.disk.bean.State;

import android.R.integer;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;

public interface Api {
	@FormUrlEncoded
	@POST("mlogin")
    Call<State> login(@Field("phone") String phone,@Field("password")String pwd);
	
	@FormUrlEncoded
	@POST("getsmscode")
    Call<State> sendAuthCode(@Field("phone") String phone,@Field("what") String what);
	
	@FormUrlEncoded
	@POST("updatepsw")
    Call<State> updatePsw(@Field("phone") String phone,@Field("password") String password,@Field("smscode") String smscod);
	
	@FormUrlEncoded
	@POST("register")
    Call<State> register(@Field("username") String username, @Field("phone") String phone,@Field("password") String password,@Field("smscode") String smscod);
	
	@FormUrlEncoded
	@POST("file/checkmd5")
    Call<State> checkmd5(@Field("md5") String md5,@Field("name") String name,@Field("size") long size,@Field("parentid") int parentid);
	
	
	
	@Multipart
	@POST("file/upload")
    Call<ResponseBody> upload(@PartMap Map<String, RequestBody> params);
	
	
	
	@GET("file/mlist")
	Call<List<FileBean>> list(@Query("parentid") int parentid);
	
	@GET("file/upload")
	Call<ResponseBody> checkUpload(@QueryMap Map<String, String> chunk);
	
	@GET("file/download/{id}")
	Call<ResponseBody> download(@Path("id") String id);
	
	@GET("file/mdelete/{id}")
	Call<State> delete(@Path("id") long id);
	
	@GET("file/mrename/{id}")
	Call<State> rename(@Path("id") int id, @Query("name") String name);
	
	@Streaming
	@GET("file/download/{id}")
	Call<ResponseBody> download1(@Path("id") String id);
	
}

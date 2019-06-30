package com.sky.disk.activity;


import com.sky.disk.R;
import com.sky.disk.application.ExitApplication;
import com.sky.disk.bean.State;
import com.sky.disk.retrofit.Api;
import com.sky.disk.retrofit.Http;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends Activity {


	private final static String TAG="SKY云盘";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);
		
		  
		ExitApplication.getInstance().addActivity(this);
		
		final EditText tv_phone=(EditText)findViewById(R.id.et_phone);
		final EditText tv_pwd=(EditText)findViewById(R.id.et_password);
		ImageButton button=(ImageButton)findViewById(R.id.bt_log);
		
		final Button btnFindPwd=(Button)findViewById(R.id.miss_pas);
		Button btnRegister=(Button)findViewById(R.id.btn_register);
		
		
		Retrofit retrofit=Http.getRetrofit(getApplicationContext());
		
		final Api api = retrofit.create(Api.class);
			
		button.setOnClickListener(new View.OnClickListener() {		
			@Override
			public void onClick(View v) {
				
												
				Call<State> call= api.login(tv_phone.getText().toString(), tv_pwd.getText().toString());
				call.enqueue(new Callback<State>() {					
					@Override
					public void onResponse(Call<State> arg0, Response<State> response) {						
						if (response.isSuccessful()) {
						
							State state = response.body();
							
							if(state.getSuccess()==1) {
								Intent intent=new Intent(LoginActivity.this, MainActivity.class);
								startActivity(intent);								
							}else {
								Toast.makeText(LoginActivity.this, state.getMsg(), Toast.LENGTH_LONG).show();
							}		
		                }else {
		                	Toast.makeText(LoginActivity.this, "服务器故障", Toast.LENGTH_LONG).show();
		                }
					}
					
					@Override
					public void onFailure(Call<State> arg0, Throwable t) {
						Toast.makeText(LoginActivity.this, "服务器故障", Toast.LENGTH_LONG).show();
						Log.e("xyh", "onFailure: " + t.getMessage());
					}
				});
				
			}
		});
		
		btnFindPwd.setOnClickListener(new View.OnClickListener() {						

			@Override
			public void onClick(View v) {
				Intent intent=new Intent(LoginActivity.this, FindPwdActivity.class);
				startActivity(intent);			
			}
		});
		
		btnRegister.setOnClickListener(new View.OnClickListener() {						

			@Override
			public void onClick(View v) {
				Intent intent=new Intent(LoginActivity.this, RegisterActivity.class);
				startActivity(intent);			
			}
		});
		
	}
	private long exitTime=0;
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_BACK) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {					                
				Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
				 exitTime = System.currentTimeMillis();
			} else {
				ExitApplication.getInstance().exit();
			}
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
}

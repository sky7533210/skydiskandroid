package com.sky.disk.activity;

import com.sky.disk.R;
import com.sky.disk.bean.State;
import com.sky.disk.retrofit.Api;
import com.sky.disk.retrofit.Http;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RegisterActivity extends Activity {
	
	private Api api;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.adduser);
		
		final EditText et_user=(EditText)findViewById(R.id.reg_user);
		final EditText et_pass=(EditText)findViewById(R.id.reg_password);
		final EditText et_repass=(EditText)findViewById(R.id.reg_password2);
		Button btn_getSmsCode=(Button)findViewById(R.id.reg_miss_pas);
		final EditText et_smscode=(EditText)findViewById(R.id.reg_code);
		final ImageButton btn_ok=(ImageButton)findViewById(R.id.reg_ok);
		
		Retrofit retrofit=Http.getRetrofit(null);
		api= retrofit.create(Api.class);
		
		btn_getSmsCode.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {			
				String phone= et_user.getText().toString();			
				if(!"".equals(phone)) {
					//发送请求
					Call<State> call= api.sendAuthCode(phone, "reg");
					call.enqueue(new Callback<State>() {
						
						@Override
						public void onResponse(Call<State> arg0, Response<State> response) {
							State state=response.body();
							if(state.getSuccess()==1) {
								btn_ok.setEnabled(true);
							} 
							Toast.makeText(RegisterActivity.this, state.getMsg(), Toast.LENGTH_LONG).show();							
						}
						
						@Override
						public void onFailure(Call<State> arg0, Throwable arg1) {
							Log.i("register", arg1.getMessage());
							Toast.makeText(RegisterActivity.this, "请求失败", Toast.LENGTH_LONG).show();
						}
					});
					
				}else {
					Toast.makeText(RegisterActivity.this, "手机号不能为空", Toast.LENGTH_LONG).show();
				}
			}
		});
		
		btn_ok.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {			
				String phone= et_user.getText().toString();
				String password=et_pass.getText().toString();
				String repassword=et_repass.getText().toString();
				String smscod=et_smscode.getText().toString();
				if(!"".equals(phone)&&!"".equals(password)&&!"".equals(repassword)&&!"".equals(smscod)) {
					//发送请求
					
					if(!password.equals(repassword)) {
						Toast.makeText(RegisterActivity.this, "两次密码输入不同", Toast.LENGTH_LONG).show();
						return;
					}
					
					Call<State> call= api.register(phone, phone, password, smscod);
					call.enqueue(new Callback<State>() {
						
						@Override
						public void onResponse(Call<State> arg0, Response<State> response) {
							State state=response.body();						
							Toast.makeText(RegisterActivity.this, state.getMsg(), Toast.LENGTH_LONG).show();							
						}
						
						@Override
						public void onFailure(Call<State> arg0, Throwable arg1) {
							Log.i("register", arg1.getMessage());
							Toast.makeText(RegisterActivity.this, "请求失败", Toast.LENGTH_LONG).show();
						}
					});
					
				}else {
					Toast.makeText(RegisterActivity.this, "输入不能为空", Toast.LENGTH_LONG).show();
				}
			}
		});
		
	}
}

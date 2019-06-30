package com.sky.disk.activity;

import com.sky.disk.R;
import com.sky.disk.bean.State;
import com.sky.disk.retrofit.Api;
import com.sky.disk.retrofit.Http;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FindPwdActivity extends Activity{
	
	private Api api;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.reset_password);
		
		final EditText et_user=(EditText)findViewById(R.id.user);
		final EditText et_pwd=(EditText)findViewById(R.id.password);
		final EditText et_auth_code=(EditText)findViewById(R.id.code);
		Button btn_getCode=(Button)findViewById(R.id.miss_pas);
		final ImageButton btn_ok=(ImageButton)findViewById(R.id.btnok);
		btn_ok.setEnabled(false);
		Retrofit retrofit=Http.getRetrofit(null);
		api= retrofit.create(Api.class);
		
		btn_getCode.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {			
				String phone= et_user.getText().toString();			
				if(!"".equals(phone)) {
					//发送请求
					Call<State> call= api.sendAuthCode(phone, "psw");
					call.enqueue(new Callback<State>() {
						
						@Override
						public void onResponse(Call<State> arg0, Response<State> response) {
							State state=response.body();
							if(state.getSuccess()==1) {
								btn_ok.setEnabled(true);
							} 
							Toast.makeText(FindPwdActivity.this, state.getMsg(), Toast.LENGTH_LONG).show();							
						}
						
						@Override
						public void onFailure(Call<State> arg0, Throwable arg1) {
							Log.i("findpwd", arg1.getMessage());
							Toast.makeText(FindPwdActivity.this, "请求失败", Toast.LENGTH_LONG).show();
						}
					});
					
				}else {
					Toast.makeText(FindPwdActivity.this, "手机号不能为空", Toast.LENGTH_LONG).show();
				}
			}
		});
		
		btn_ok.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String phone= et_user.getText().toString();
				String pwd=et_pwd.getText().toString();
				String authCode=et_auth_code.getText().toString();
				if(!"".equals(phone)&&!"".equals(pwd)&&!"".equals(authCode)) {
					Call<State> call=api.updatePsw(phone, pwd, authCode);
					call.enqueue(new Callback<State>() {

						@Override
						public void onFailure(Call<State> arg0, Throwable t) {
							Log.i("findpwd", t.getMessage());
							Toast.makeText(FindPwdActivity.this, "请求失败", Toast.LENGTH_LONG).show();
						}

						@Override
						public void onResponse(Call<State> arg0, Response<State> response) {
							Toast.makeText(FindPwdActivity.this, response.body().getMsg(), Toast.LENGTH_LONG).show();
						}
					});
					
				}else {
					Toast.makeText(FindPwdActivity.this, "手机号或密码或验证码不能为空", Toast.LENGTH_LONG).show();
				}
			}
		});
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_BACK) {
			finish();		
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
}

package com.sky.disk.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;

import com.sky.disk.R;
import com.sky.disk.adapter.FileAdapter;
import com.sky.disk.adapter.FileListAdapter;
import com.sky.disk.application.ExitApplication;
import com.sky.disk.bean.FileBean;
import com.sky.disk.util.FileChooseUtil;
import com.sky.disk.view.MyListView;
import com.sky.disk.view.PopupWindowImage;
import com.sky.disk.view.PopupWindowTrans;

public class MainActivity extends Activity implements MyListView.LoadListener{

	private MyListView listView;
	
	private FileListAdapter adapter;	
	
	private PopupWindowImage popupWindowImage;
	
	private PopupWindowTrans popupWindowTrans;
	

	private FileAdapter fileAdapter;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		ExitApplication.getInstance().addActivity(this);	
		
		listView = (MyListView) findViewById(R.id.listview);
		listView.setInteface(this);
		
		adapter = new FileListAdapter(new ArrayList<FileBean>(), MainActivity.this);           
        listView.setAdapter(adapter);
        
        
        fileAdapter=new FileAdapter(this);
        
        popupWindowTrans=new PopupWindowTrans(getApplicationContext(), listView,fileAdapter);
        
        
		     				
		fileAdapter.list(0);
		
		listView.setOnItemClickListener(fileAdapter);
		listView.setOnItemLongClickListener(fileAdapter);
	}
	
	
	
	public void updateFileListView(List<FileBean> fileBeans) {
		adapter.setItem(fileBeans);
		adapter.notifyDataSetChanged();
	}
	
	public void updateFileListView() {
		adapter.notifyDataSetChanged();
	}
	public void updateFileListView(int id) {
		adapter.removeItemById(id);
		updateFileListView();
	}
	public void showImagePopupWindow(Bitmap bitmap) {
		if(popupWindowImage==null)
			popupWindowImage=new PopupWindowImage(getApplicationContext(), listView);
		popupWindowImage.show(bitmap);
	}
	
	private long exitTime=0;
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_BACK) {
			try {
				int currentParentId= fileAdapter.stackPop();
				fileAdapter.list(currentParentId);
				
			} catch (EmptyStackException e) {
				if ((System.currentTimeMillis() - exitTime) > 2000) {					                
					Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
					 exitTime = System.currentTimeMillis();
				} else {
					ExitApplication.getInstance().exit();
				}
			}
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
	@Override
	public void PullLoad() {	
		new Handler().postDelayed(new Runnable() {			 
			@Override
			public void run() {
				fileAdapter.refresh();
				listView.loadComplete();
			}
		}, 1500);
		
	}
	
	
	public boolean onCreateOptionsMenu(Menu menu) {
        //导入菜单布局
        getMenuInflater().inflate(R.menu.option_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        //创建菜单项的点击事件
        switch (item.getItemId()) {
        case R.id.transmission:
            popupWindowTrans.show();
            break;
        case R.id.mune_setting:
            Toast.makeText(this, "点击了设置", Toast.LENGTH_SHORT).show();

            break;
        case R.id.mune_out:
            Toast.makeText(this, "点击了退出", Toast.LENGTH_SHORT).show();
            break;

        default:
            break;
        }

        return super.onOptionsItemSelected(item);
    }



	public void showDownloadFile(FileBean fileBean) {
		popupWindowTrans.setImgAndNameDown(fileBean);
		popupWindowTrans.show();
	}
	private void updateDownloadProgress(int size,boolean isFinish) {
		popupWindowTrans.updateProgressDown(size, isFinish);
	}
	
	public void showUploadFile(FileBean fileBean) {
		popupWindowTrans.setImgAndNameUp(fileBean);
		popupWindowTrans.show();
	}
	private void updateUploadProgress(int size,boolean isFinish) {
		popupWindowTrans.updateProgressUp(size, isFinish);
	}
	
	public Handler getHandler() {
		return handler;
	}
	
	private Handler handler=new Handler(new Handler.Callback() {
		@Override
		public boolean handleMessage(Message msg) {
			switch (msg.what) {			
			case 1:
				updateDownloadProgress(msg.arg1, false);
				break;
			case 2:
				Toast.makeText(MainActivity.this, "下载完成", Toast.LENGTH_LONG).show();
				updateDownloadProgress(msg.arg1, true);
				break;
			case 3:
				updateUploadProgress(msg.arg1, false);
				break;
			case 4:
				Toast.makeText(MainActivity.this, "上传完成", Toast.LENGTH_LONG).show();
				updateUploadProgress(msg.arg1, true);
				break;
				
			}
			return false;
		}
	});


	public void openFileManager() {
		if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(this, "没有外部存储", Toast.LENGTH_LONG).show();
            return;
        }

		File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/skydiskdownload");
     
        //调用系统文件管理器打开指定路径目录
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setDataAndType(Uri.fromFile(file.getParentFile()), "*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, 0);
	}

@Override
protected void onActivityResult(int requestCode,int resultCode,Intent data){//选择文件返回
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode==RESULT_OK){
        switch(requestCode){
              case 0:
              Uri uri=data.getData();
              String chooseFilePath=FileChooseUtil.getInstance(this).getChooseFileResultPath(uri);
              
              fileAdapter.upload(chooseFilePath);
              
              Log.d("11111111","选择文件返回："+chooseFilePath);
              Toast.makeText(this, chooseFilePath, Toast.LENGTH_LONG).show();
              break;
        }
    }
}
}
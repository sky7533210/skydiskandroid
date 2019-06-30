package com.sky.disk.view;

import java.io.File;

import com.sky.disk.R;
import com.sky.disk.adapter.FileAdapter;
import com.sky.disk.bean.FileBean;
import com.sky.disk.util.Constant;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class PopupWindowTrans{
	private PopupWindow popupWindow;
	private View parent;
	private ImageView imageView;
	private TextView tv_filename;
	private TextView tv_progress;
	private ProgressBar progressBar;
	private FileBean fileBean;
	private Button btn_download;
	
	private FileAdapter fileAdapter;
		
	public PopupWindowTrans(final Context context,View parent,FileAdapter fileAdapter) {
		this.parent=parent;
		this.fileAdapter=fileAdapter;
		View contentView = LayoutInflater.from(context).inflate(R.layout.tabhost, null);
		popupWindow = new PopupWindow(contentView, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT, true);
		popupWindow.setTouchable(true);
		popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));		
		TabHost tabHost = (TabHost)contentView.findViewById(R.id.tabhost);
        tabHost.setup();

        tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("上传列表").setContent(R.id.tab01));
        tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator("下载列表").setContent(R.id.tab02));
        
        progressBar=(ProgressBar) contentView.findViewById(R.id.progressBar1);    
        imageView=(ImageView)contentView.findViewById(R.id.iv_file_icon_down);
        tv_filename=(TextView)contentView.findViewById(R.id.tv_file_name_down);
        tv_progress=(TextView)contentView.findViewById(R.id.tv_file_progress);
        
        btn_download=(Button) contentView.findViewById(R.id.btn_download);
        
        TextView tv_file_loaction = (TextView)contentView.findViewById(R.id.tv_file_location);
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			File file = Environment.getExternalStorageDirectory();
			tv_file_loaction.setText("文件下载至:"+file.getAbsolutePath()+"/skydiskdownload");
        }else {
        	tv_file_loaction.setText("没有可用的存储");
        }
        tabHost.setOnTabChangedListener(new OnTabChangeListener(){    
            @Override  
            // tabId是newTabSpec参数设置的tab页名，并不是layout里面的标识符id
            public void onTabChanged(String tabId) {  
               if (tabId.equals("tab1")) {   //第一个标签  
                   Toast.makeText(context, "点击标签页一", Toast.LENGTH_SHORT).show();
               }  
               if (tabId.equals("tab2")) {   //第二个标签  
                   Toast.makeText(context, "点击标签页二", Toast.LENGTH_SHORT).show();
               }              
            }              
           });
	}
	public void show(){
		popupWindow.showAtLocation(parent, 1, 0, 0);
	}
	public void setImgAndName(FileBean fileBean) {
		this.fileBean=fileBean;
		btn_download.setBackgroundColor(Color.rgb(0, 160, 233));
		btn_download.setOnClickListener(fileAdapter);
		imageView.setImageResource(Constant.getImageResource(fileBean.getType()));
		tv_filename.setText(fileBean.getName());
		progressBar.setProgress(0);
		tv_progress.setText("0kb/"+Constant.intToString(fileBean.getSize()));
	}
	public void updateProgress(int size,boolean isFinish) {
		float progress= size*1.0f/fileBean.getSize();		
		progressBar.setProgress((int) (progress*100));
		tv_progress.setText(Constant.intToString(size)+"/"+Constant.intToString(fileBean.getSize()));
		if(isFinish)
			btn_download.setText("打开");
	}
}

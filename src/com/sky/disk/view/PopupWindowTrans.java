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
	private ImageView imageViewDown;
	private TextView tv_filenameDown;
	private TextView tv_progressDown;
	private ProgressBar progressBarDown;
	private FileBean fileBean;
	private Button btn_download;
	
	private FileAdapter fileAdapter;
	private ProgressBar progressBarUp;
	private ImageView imageViewUp;
	private TextView tv_filenameUp;
	private TextView tv_progressup;
	private Button btn_upload;
		
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

        tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("�����б�").setContent(R.id.tab01));
        tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator("�ϴ��б�").setContent(R.id.tab02));
        
        progressBarDown=(ProgressBar) contentView.findViewById(R.id.progressBar1);    
        imageViewDown=(ImageView)contentView.findViewById(R.id.iv_file_icon_down);
        tv_filenameDown=(TextView)contentView.findViewById(R.id.tv_file_name_down);
        tv_progressDown=(TextView)contentView.findViewById(R.id.tv_file_progress);
        
        btn_download=(Button) contentView.findViewById(R.id.btn_download);
        
        Button btn_select_file=(Button) contentView.findViewById(R.id.btn_select);
        btn_select_file.setOnClickListener(fileAdapter);
        
        TextView tv_file_loaction = (TextView)contentView.findViewById(R.id.tv_file_location);
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			File file = Environment.getExternalStorageDirectory();
			tv_file_loaction.setText("�ļ�������:"+file.getAbsolutePath()+"/skydiskdownload");
        }else {
        	tv_file_loaction.setText("û�п��õĴ洢");
        }
        
        progressBarUp=(ProgressBar) contentView.findViewById(R.id.progressBar2);    
        imageViewUp=(ImageView)contentView.findViewById(R.id.iv_file_icon_up);
        tv_filenameUp=(TextView)contentView.findViewById(R.id.tv_file_name_up);
        tv_progressup=(TextView)contentView.findViewById(R.id.tv_file_progress_up);
        btn_upload=(Button) contentView.findViewById(R.id.btn_upload);
        
        tabHost.setOnTabChangedListener(new OnTabChangeListener(){    
            @Override  
            // tabId��newTabSpec�������õ�tabҳ����������layout����ı�ʶ��id
            public void onTabChanged(String tabId) {  
               if (tabId.equals("tab1")) {   //��һ����ǩ  
                   Toast.makeText(context, "�����ǩҳһ", Toast.LENGTH_SHORT).show();
               }  
               if (tabId.equals("tab2")) {   //�ڶ�����ǩ  
                   Toast.makeText(context, "�����ǩҳ��", Toast.LENGTH_SHORT).show();
               }              
            }              
           });
	}
	public void show(){
		popupWindow.showAtLocation(parent, 1, 0, 0);
	}
	public void setImgAndNameDown(FileBean fileBean) {
		this.fileBean=fileBean;
		btn_download.setBackgroundColor(Color.rgb(0, 160, 233));
		btn_download.setOnClickListener(fileAdapter);
		btn_download.setText("��ͣ");
		imageViewDown.setImageResource(Constant.getImageResource(fileBean.getType()));
		tv_filenameDown.setText(fileBean.getName());
		progressBarDown.setProgress(0);
		tv_progressDown.setText("0kb/"+Constant.intToString(fileBean.getSize()));
	}
	public void updateProgressDown(int size,boolean isFinish) {
		float progress= size*1.0f/fileBean.getSize();		
		progressBarDown.setProgress((int) (progress*100));
		tv_progressDown.setText(Constant.intToString(size)+"/"+Constant.intToString(fileBean.getSize()));
		if(isFinish)
			btn_download.setText("��");
	}
	public void setImgAndNameUp(FileBean fileBean) {
		this.fileBean=fileBean;
		btn_upload.setBackgroundColor(Color.rgb(0, 160, 233));
		btn_upload.setOnClickListener(fileAdapter);
		btn_upload.setText("��ͣ");
		imageViewUp.setImageResource(Constant.getImageResource(fileBean.getType()));
		tv_filenameUp.setText(fileBean.getName());
		progressBarUp.setProgress(0);
		tv_progressup.setText("0kb/"+Constant.intToString(fileBean.getSize()));
	}
	public void updateProgressUp(int size,boolean isFinish) {
		float progress= size*1.0f/fileBean.getSize();		
		progressBarUp.setProgress((int) (progress*100));
		tv_progressup.setText(Constant.intToString(size)+"/"+Constant.intToString(fileBean.getSize()));
		if(isFinish)
			btn_upload.setText("���");
	}
}

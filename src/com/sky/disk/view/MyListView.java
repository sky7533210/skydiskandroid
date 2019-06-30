package com.sky.disk.view;

import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sky.disk.R;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;

public class MyListView extends ListView implements AbsListView.OnScrollListener{
	 
	private View headview; //头文件
	private int firstVisible; //下拉
	private LoadListener loadListener; //接口回调
	private int headHeight; //头文件高度
	private int Yload;//位置
	boolean isLoading;//加载状态
	private TextView headtxt;//头文件textview显示加载文字
	private ProgressBar progressBar;//加载进度
	
	public MyListView(Context context) {
		super(context);
		Init(context);
	}
	
	public MyListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		Init(context);
	}
	
	public MyListView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		Init(context);
	}
	
	private void Init(Context context) {
		//拿到头布局文件xml
		headview=LinearLayout.inflate(context, R.layout.head, null);
		headtxt=(TextView) headview.findViewById(R.id.headtxt);
		
		progressBar=(ProgressBar) headview.findViewById(R.id.headprogress);
		headview.measure(0, 0);
		headHeight=headview.getMeasuredHeight();
		headview.setPadding(0,-headHeight, 0, 0);
		//添加到listview头部
		this.addHeaderView(headview);
		//设置拉动监听
		this.setOnScrollListener(this);
	}
 
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			Yload=(int) ev.getY();
			break;
 
		case MotionEvent.ACTION_MOVE:
			int moveY=(int) ev.getY();
			int paddingY=headHeight+(moveY-Yload)/2;
			if(paddingY<0){
				headtxt.setText("下拉刷新........");
				progressBar.setVisibility(View.GONE);
			}
			if(paddingY>0){
				headtxt.setText("松开刷新........");
				progressBar.setVisibility(View.GONE);
			}
			headview.setPadding(0, paddingY, 0, 0);
			break;
		}
		return super.onTouchEvent(ev);
	}
	
	public void onScrollStateChanged(AbsListView view, int scrollState) {	
		if(firstVisible==0&&scrollState==SCROLL_STATE_IDLE){
			headview.setPadding(0, 0, 0, 0);
			headtxt.setText("正在刷新.......");
			progressBar.setVisibility(View.VISIBLE);
			loadListener.PullLoad();
		}
	}
	
	
	//接口回调
	public interface LoadListener{		
		void PullLoad();
	}
 
 
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		this.firstVisible=firstVisibleItem;
	}
	
	//加载完成
	public void loadComplete(){
		isLoading=false;
		headview.setPadding(0, -headHeight, 0, 0);
	}
	
	public void setInteface(LoadListener loadListener){
		this.loadListener=loadListener;
	}
 
}

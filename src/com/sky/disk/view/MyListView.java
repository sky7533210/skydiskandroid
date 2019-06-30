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
	 
	private View headview; //ͷ�ļ�
	private int firstVisible; //����
	private LoadListener loadListener; //�ӿڻص�
	private int headHeight; //ͷ�ļ��߶�
	private int Yload;//λ��
	boolean isLoading;//����״̬
	private TextView headtxt;//ͷ�ļ�textview��ʾ��������
	private ProgressBar progressBar;//���ؽ���
	
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
		//�õ�ͷ�����ļ�xml
		headview=LinearLayout.inflate(context, R.layout.head, null);
		headtxt=(TextView) headview.findViewById(R.id.headtxt);
		
		progressBar=(ProgressBar) headview.findViewById(R.id.headprogress);
		headview.measure(0, 0);
		headHeight=headview.getMeasuredHeight();
		headview.setPadding(0,-headHeight, 0, 0);
		//��ӵ�listviewͷ��
		this.addHeaderView(headview);
		//������������
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
				headtxt.setText("����ˢ��........");
				progressBar.setVisibility(View.GONE);
			}
			if(paddingY>0){
				headtxt.setText("�ɿ�ˢ��........");
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
			headtxt.setText("����ˢ��.......");
			progressBar.setVisibility(View.VISIBLE);
			loadListener.PullLoad();
		}
	}
	
	
	//�ӿڻص�
	public interface LoadListener{		
		void PullLoad();
	}
 
 
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		this.firstVisible=firstVisibleItem;
	}
	
	//�������
	public void loadComplete(){
		isLoading=false;
		headview.setPadding(0, -headHeight, 0, 0);
	}
	
	public void setInteface(LoadListener loadListener){
		this.loadListener=loadListener;
	}
 
}

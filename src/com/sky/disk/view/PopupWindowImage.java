package com.sky.disk.view;

import com.sky.disk.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.PopupWindow;

public class PopupWindowImage{
	private PopupWindow popupWindow;
	private View parent;
	
	private ImageView imageView;
	
	public PopupWindowImage(Context context,View parent) {
		this.parent=parent;
		View contentView = LayoutInflater.from(context).inflate(R.layout.image_show, null);
		popupWindow = new PopupWindow(contentView, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT, true);
		popupWindow.setTouchable(true);
		popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		imageView = (ImageView) contentView.findViewById(R.id.imageView_show);
	}
	public void show(Bitmap bitmap){
		imageView.setImageBitmap(bitmap);
		popupWindow.showAtLocation(parent, 1, 0, 0);
	}
}

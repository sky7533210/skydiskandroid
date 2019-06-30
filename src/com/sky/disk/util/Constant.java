package com.sky.disk.util;

import java.util.HashMap;
import java.util.Map;

import com.sky.disk.R;

public class Constant {
	public final static Map<String, Integer> resouseMap=new HashMap<String,Integer>();
	static {
		resouseMap.put("-1", R.drawable.icon_list_folder);
		resouseMap.put(".apk", R.drawable.icon_list_apk);
		resouseMap.put(".mp3", R.drawable.icon_list_audiofile);
		resouseMap.put(".bt", R.drawable.icon_list_bt);
		resouseMap.put(".zip", R.drawable.icon_list_compressfile);
		resouseMap.put(".docx", R.drawable.icon_list_doc);
		resouseMap.put(".xls", R.drawable.icon_list_excel);
		resouseMap.put(".html", R.drawable.icon_list_html);
		resouseMap.put(".pdf", R.drawable.icon_list_pdf);
		resouseMap.put(".jpg", R.drawable.icon_list_pic);
		resouseMap.put(".png", R.drawable.icon_list_pic);
		resouseMap.put(".ppt", R.drawable.icon_list_ppt);
		resouseMap.put(".txt", R.drawable.icon_list_txtfile);
		resouseMap.put("", R.drawable.icon_list_unknown);
		resouseMap.put(null, R.drawable.icon_list_unknown);
		resouseMap.put(".mp4", R.drawable.icon_list_video);	
	}
	public static int getImageResource(String type) {
		Integer drawable=Constant.resouseMap.get(type);
		if(drawable==null)
			return Constant.resouseMap.get("");
		else
			return drawable;
	}
	public static String intToString(int size) {
		if(size>1024*1024) {
			return String.format("%.2f", size/1024/1024.0)+"MB";
		}else if(size>1024) {
			return String.format("%.2f", size/1024.0)+"KB";
		}else {
			return size+"B";
		}
	}
}

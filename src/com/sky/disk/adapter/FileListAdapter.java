package com.sky.disk.adapter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.sky.disk.R;
import com.sky.disk.bean.FileBean;
import com.sky.disk.util.Constant;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class FileListAdapter extends BaseAdapter{
	private List<FileBean> list = null;
	private Context context = null;

	public FileListAdapter(List<FileBean> list, Context context) {
		this.list = list;
		this.context = context;		
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return list.get(position).getId();
	}
	
	public void setItem(List<FileBean> list) {
		this.list=list;
	}
	
	public void removeItemById(int id) {
		Iterator<FileBean> iterator=list.iterator();
		while(iterator.hasNext()) {
			FileBean fileBean=iterator.next();
			if(fileBean.getId()==id) {
				iterator.remove();
				break;
			}
		}
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder item = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.filelist_item, null);
			item = new ViewHolder();
			item.icon = (ImageView) convertView.findViewById(R.id.iv_file_icon);
			item.name = (TextView) convertView.findViewById(R.id.tv_file_name);
			item.time_size = (TextView) convertView.findViewById(R.id.tv_file_time_size);
			//item.checkBox = (CheckBox) convertView.findViewById(R.id.checkbox);
			convertView.setTag(item);// 绑定ViewHolder对象
		} else {
			item = (ViewHolder) convertView.getTag();// 取出ViewHolder对象
		}
		/** 设置TextView显示的内容，即我们存放在动态数组中的数据 */	
		
		item.name.setText(list.get(position).getName());
		
		Integer size=list.get(position).getSize();
		if(size!=null) {
			item.time_size.setText(list.get(position).getCreate_time()+"     "+Constant.intToString(size));			
			item.icon.setImageResource(Constant.getImageResource(list.get(position).getType()));					
		}else {
			item.icon.setImageResource(Constant.resouseMap.get("-1"));
			item.time_size.setText(list.get(position).getCreate_time());
		}
		
		//item.checkBox.setChecked(false);
		
		return convertView;
	}	

}

class ViewHolder {
	public ImageView icon;
	public TextView name;
	public TextView time_size;
	//public CheckBox checkBox;
}
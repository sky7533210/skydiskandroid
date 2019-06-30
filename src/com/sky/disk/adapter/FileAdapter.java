package com.sky.disk.adapter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.acl.LastOwnerException;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Stack;

import com.sky.disk.R;
import com.sky.disk.activity.MainActivity;
import com.sky.disk.bean.FileBean;
import com.sky.disk.bean.State;
import com.sky.disk.retrofit.Api;
import com.sky.disk.retrofit.Http;
import com.sky.disk.util.Download;
import com.sky.disk.util.OpenFiles;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FileAdapter implements OnClickListener,OnItemClickListener, OnItemLongClickListener {
	private Api api;

	private boolean flag = false;

	private Stack<Integer> stackParentId;

	private int currentParentId = 0;

	private MainActivity mainActivity;
	
	private Handler handler;
	
	private Download download;
	
	private FileBean currentDownloadFileBean;

	private File file;

	public FileAdapter(MainActivity mainActivity) {
		Retrofit retrofit = Http.getRetrofit(null);
		api = retrofit.create(Api.class);
		stackParentId = new Stack<Integer>();
		this.mainActivity = mainActivity;
		handler=mainActivity.getHandler();
	}

	public void list(final int parentid) {
		Call<List<FileBean>> call = api.list(parentid);

		call.enqueue(new Callback<List<FileBean>>() {

			@Override
			public void onResponse(Call<List<FileBean>> arg0, Response<List<FileBean>> arg1) {
				if (flag) {

					stackParentId.push(currentParentId);
					currentParentId = parentid;
					flag = false;
				}
				mainActivity.updateFileListView(arg1.body());
			}

			@Override
			public void onFailure(Call<List<FileBean>> arg0, Throwable arg1) {
				Toast.makeText(mainActivity, "失败", Toast.LENGTH_LONG).show();
			}
		});
	}

	// 监听listview item点击
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		String fileType = ((FileBean) parent.getItemAtPosition(position)).getType();
		int fileId = ((FileBean) parent.getItemAtPosition(position)).getId();
		if ("-1".equals(fileType)) {
			flag = true;
			list(fileId);
		} else if (".png".equals(fileType) || ".jpg".equals(fileType)) {
			downloadImage(fileId);
		}
	}

	// 监听listview item 长按弹出菜单
	@Override
	public boolean onItemLongClick(final AdapterView<?> parent, View view, int position, final long id) {
		PopupMenu popup = new PopupMenu(mainActivity.getApplicationContext(), view);
		MenuInflater inflater = popup.getMenuInflater();
		inflater.inflate(R.menu.menu, popup.getMenu());

		final FileBean fileBean = (FileBean) parent.getItemAtPosition(position);

		popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				switch (item.getItemId()) {
				case R.id.download:
					download(fileBean);
					break;
				case R.id.rename:
					// 弹出对话框
					final EditText et = new EditText(mainActivity);
					et.setText(fileBean.getName());
					new AlertDialog.Builder(mainActivity).setTitle("请输新的文件名")
							.setIcon(android.R.drawable.sym_def_app_icon).setView(et)
							.setPositiveButton("确定", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialogInterface, int i) {
									// 按下确定键后的事件

									String name = et.getText().toString();
									if ("".equals(name))
										Toast.makeText(mainActivity.getApplicationContext(), "文件名不能为空",
												Toast.LENGTH_LONG).show();
									else
										fileRename(fileBean, name);
								}
							}).setNegativeButton("取消", null).show();
					break;
				case R.id.delete:
					fileDelete(id);
					break;
				default:
					break;
				}
				return false;
			}
		});
		popup.show();
		return false;
	}

	public void downloadImage(final int id) {

		String _id = Base64.encodeToString(String.valueOf(id).getBytes(), Base64.DEFAULT);
		Call<ResponseBody> call = api.download(_id);

		call.enqueue(new Callback<ResponseBody>() {

			@Override
			public void onResponse(Call<ResponseBody> arg0, Response<ResponseBody> response) {
				ResponseBody body = response.body();

				try {
					byte[] data = body.bytes();
					Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
					mainActivity.showImagePopupWindow(bitmap);

				} catch (IOException e) {
					e.printStackTrace();
					Toast.makeText(mainActivity, "错误", Toast.LENGTH_LONG).show();
				}

			}

			@Override
			public void onFailure(Call<ResponseBody> arg0, Throwable arg1) {
				Toast.makeText(mainActivity, "失败", Toast.LENGTH_LONG).show();
			}
		});

	}

	private void fileDelete(final long id) {
		Call<State> call = api.delete(id);
		call.enqueue(new Callback<State>() {

			@Override
			public void onResponse(Call<State> arg0, Response<State> response) {
				State state = response.body();
				if (state.getSuccess() == 1) {
					mainActivity.updateFileListView((int) id);
				}
				Toast.makeText(mainActivity, state.getMsg(), Toast.LENGTH_LONG).show();
			}

			@Override
			public void onFailure(Call<State> arg0, Throwable arg1) {
				Log.i("111", arg1.getMessage());
				Toast.makeText(mainActivity, "删除失败", Toast.LENGTH_LONG).show();
			}
		});
	}

	private void fileRename(final FileBean fileBean, final String name) {
		Call<State> call = api.rename(fileBean.getId(), name);
		call.enqueue(new Callback<State>() {

			@Override
			public void onResponse(Call<State> arg0, Response<State> response) {
				State state = response.body();
				if (state.getSuccess() == 1) {
					fileBean.setName(name);
					mainActivity.updateFileListView();
				}
				Toast.makeText(mainActivity, state.getMsg(), Toast.LENGTH_LONG).show();
			}

			@Override
			public void onFailure(Call<State> arg0, Throwable arg1) {
				Log.i("111", arg1.getMessage());
				Toast.makeText(mainActivity, "重命名失败", Toast.LENGTH_LONG).show();
			}
		});
	}

	private void download(final FileBean fileBean) {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			file = Environment.getExternalStorageDirectory();

			final File file1 = new File(file, "skydiskdownload");

			if (!file1.exists()) {
				boolean issuccess = file1.mkdir();
				Log.i("fileadapter", issuccess + "");
			}

			String _id = Base64.encodeToString(String.valueOf(fileBean.getId()).getBytes(), Base64.DEFAULT);
			Call<ResponseBody> call = api.download1(_id);
			call.enqueue(new Callback<ResponseBody>() {
				

				@Override
				public void onResponse(Call<ResponseBody> arg0, Response<ResponseBody> response) {
					ResponseBody body = response.body();
					InputStream is = body.byteStream();
					FileOutputStream fos = null;

					File file2 = new File(file1, fileBean.getName());

					try {
						if (!file2.exists())
							file2.createNewFile();
						fos = new FileOutputStream(file2);

						Log.i("show", "show");
						Message msg=Message.obtain(handler);
						msg.what=0;
						msg.obj=fileBean;
						msg.sendToTarget();				
						
						currentDownloadFileBean=fileBean;
						
						download= new Download(is, fos, new Download.ProgressCallBack() {

							@Override
							public void onProgress(int size) {
								Log.i("onProgress", "" + size);
								Message msg=Message.obtain(handler);
								msg.what=1;
								msg.arg1=size;
								msg.sendToTarget();	
							}

							@Override
							public void onFinish() {
								Log.i("onFinish", "onFinish");
								Message msg=Message.obtain(handler);
								msg.what=2;
								msg.arg1=fileBean.getSize();
								msg.sendToTarget();	
							}
						});
						download.start();
					} catch (IOException e) {
						e.printStackTrace();
						try {
							if (is != null)
								is.close();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				}

				@Override
				public void onFailure(Call<ResponseBody> arg0, Throwable arg1) {
					Log.i("111", arg1.getMessage());
					Toast.makeText(mainActivity, "下载失败", Toast.LENGTH_LONG).show();
				}
			});

		} else {
			Toast.makeText(mainActivity, "no ExternalStorage", Toast.LENGTH_LONG).show();
		}

	}

	// 下拉刷新listview
	public void refresh() {
		list(currentParentId);
	}

	// 点击返回键弹栈
	public int stackPop() throws EmptyStackException {
		currentParentId = stackParentId.pop();
		return currentParentId;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_download:
			Button btn=(Button) v;				
			switch (btn.getText().toString()) {
			case "暂停":
				if(download!=null) {
					download.pause();
					btn.setText("继续");
				}
				break;
			case "继续":
				if(download!=null) {
					download.goOn();
					btn.setText("暂停");
				}
				break;
			case "打开":
				if(currentDownloadFileBean!=null) {
					File file=new File(this.file.getAbsolutePath()+"/skydiskdownload/"+currentDownloadFileBean.getName());
					OpenFiles.open(mainActivity, file);
				}
				break;			
			}			
			break;
		}		
	}

}

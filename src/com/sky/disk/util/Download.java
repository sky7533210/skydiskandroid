package com.sky.disk.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Download extends Thread{
	private InputStream is;
	private OutputStream os;
	private ProgressCallBack pcb;
	private boolean flag=false;
	public Download(InputStream is,OutputStream os,ProgressCallBack pcb) {
		this.is=is;
		this.os=os;
		this.pcb=pcb;
	}
	@Override
	public void run() {
		byte[] buf = new byte[1024 * 2];															
		int len = 0;
		int count=0;
		try {
			
			long start=System.currentTimeMillis();
			while ((len=is.read(buf))!=-1) {
				
				if(flag) {
					synchronized (this) {
						try {
							this.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
				
				
				count+=len;
				os.write(buf, 0, len);
				long end=System.currentTimeMillis();
				if(end-start>500) {
					pcb.onProgress(count);
					start=end;
				}
			}
			pcb.onFinish();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null)
					is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if (os != null)
					os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public interface ProgressCallBack{
		void onProgress(int size);
		void onFinish();
	}
	public void pause() {
		flag=true;
	}
	public void goOn() {
		synchronized (this) {
			flag=false;
			this.notify();
		}
	}
}


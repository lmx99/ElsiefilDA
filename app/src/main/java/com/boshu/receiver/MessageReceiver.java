package com.boshu.receiver;

import android.content.Context;
import android.content.Intent;

import com.lifeisle.jekton.receiver.LogisticsUpdateReceiver;
import com.tencent.android.tpush.XGPushBaseReceiver;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushRegisterResult;
import com.tencent.android.tpush.XGPushShowedResult;
import com.tencent.android.tpush.XGPushTextMessage;

public class MessageReceiver extends XGPushBaseReceiver{
  private 	Intent it=new Intent("bobo.com");;
	@Override
	public void onDeleteTagResult(Context arg0, int arg1, String arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNotifactionClickedResult(Context arg0,
			XGPushClickedResult arg1) {
		// TODO Auto-generated method stub
		System.out.println("jjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjj");
		
	}

	@Override
	public void onNotifactionShowedResult(Context arg0, XGPushShowedResult arg1) {
		// TODO Auto-generated method stub
		System.out.println("推送的信息："+arg1.getContent()+"===================");
	
		arg0.sendBroadcast(it);
	}

	@Override
	public void onRegisterResult(Context arg0, int arg1,
			XGPushRegisterResult arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSetTagResult(Context arg0, int arg1, String arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTextMessage(Context arg0, XGPushTextMessage arg1) {
		// TODO Auto-generated method stub
		System.out.println(arg1.toString()+"*******");
		String title=arg1.getTitle();
		String conent= arg1.getContent();
		if(title.equals("order")){
		    Intent it=new Intent(LogisticsUpdateReceiver.ACTION_LOGISTICS_UPDATE);
		    it.putExtra(LogisticsUpdateReceiver.EXTRA_ORDER_UPDATE, arg1.getContent());
		    arg0.sendBroadcast(it);
		}
		System.out.println(arg1.getCustomContent()+"----------");
	}

	@Override
	public void onUnregisterResult(Context arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

}

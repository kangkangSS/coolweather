package com.coolweather.app.receiver;

import com.coolweather.app.service.AutoUpdateService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class UpdataWeatherBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO �Զ����ɵķ������
		Intent i=new Intent(context,AutoUpdateService.class);
		context.startActivity(i);
		
	}

}

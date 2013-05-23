package com.gm.wine.app.ui;

import com.gm.wine.app.AppManager;

import android.app.Activity;
import android.os.Bundle;

/**
 * Ӧ�ó���Activity�Ļ���
 * @author qingang
 *
 */
public class BaseActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//���Activity����ջ
		AppManager.getAppManager().addActivity(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		//����Activity&�Ӷ�ջ���Ƴ�
		AppManager.getAppManager().finishActivity(this);
	}
}

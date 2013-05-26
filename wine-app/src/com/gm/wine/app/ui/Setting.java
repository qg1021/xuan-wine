package com.gm.wine.app.ui;

import java.io.File;

import com.gm.wine.app.AppContext;
import com.gm.wine.app.AppManager;
import com.gm.wine.app.R;
import com.gm.wine.app.common.FileUtils;
import com.gm.wine.app.common.MethodsCompat;
import com.gm.wine.app.common.UIHelper;
import com.gm.wine.app.common.UpdateManager;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class Setting extends PreferenceActivity{
	
	SharedPreferences mPreferences;
	Preference account;
	Preference myinfo;
	Preference cache;
	Preference feedback;
	Preference update;
	Preference about;
	CheckBoxPreference httpslogin;
	CheckBoxPreference loadimage;
	CheckBoxPreference scroll;
	CheckBoxPreference voice;
	CheckBoxPreference checkup;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//���Activity����ջ
		AppManager.getAppManager().addActivity(this);
		
		//������ʾPreferences
		addPreferencesFromResource(R.xml.preferences);
		//���SharedPreferences
		mPreferences = PreferenceManager.getDefaultSharedPreferences(this);		
		
		ListView localListView = getListView();
		localListView.setBackgroundColor(0);
		localListView.setCacheColorHint(0);
		((ViewGroup)localListView.getParent()).removeView(localListView);
		ViewGroup localViewGroup = (ViewGroup)getLayoutInflater().inflate(R.layout.setting, null);
		((ViewGroup)localViewGroup.findViewById(R.id.setting_content)).addView(localListView, -1, -1);
		setContentView(localViewGroup);
	      
	    
		final AppContext ac = (AppContext)getApplication();
		
		//��¼��ע��
		account = (Preference)findPreference("account");
		if(ac.isLogin()){
			account.setTitle(R.string.main_menu_logout);
		}else{
			account.setTitle(R.string.main_menu_login);
		}
		account.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
			public boolean onPreferenceClick(Preference preference) {
				UIHelper.loginOrLogout(Setting.this);
				account.setTitle(R.string.main_menu_login);
				return true;
			}
		});
		
		//�ҵ�����
		myinfo = (Preference)findPreference("myinfo");
		myinfo.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
			public boolean onPreferenceClick(Preference preference) {
				UIHelper.showUserInfo(Setting.this);
				return true;
			}
		});
		
		//https��¼
		httpslogin = (CheckBoxPreference)findPreference("httpslogin");
		httpslogin.setChecked(ac.isHttpsLogin());
		if(ac.isHttpsLogin()){
			httpslogin.setSummary("��ǰ�� HTTPS ��¼");
		}else{
			httpslogin.setSummary("��ǰ�� HTTP ��¼");
		}
		httpslogin.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
			public boolean onPreferenceClick(Preference preference) {
				ac.setConfigHttpsLogin(httpslogin.isChecked());
				if(httpslogin.isChecked()){
					httpslogin.setSummary("��ǰ�� HTTPS ��¼");
				}else{
					httpslogin.setSummary("��ǰ�� HTTP ��¼");
				}
				return true;
			}
		});
		
		//����ͼƬloadimage
		loadimage = (CheckBoxPreference)findPreference("loadimage");
		loadimage.setChecked(ac.isLoadImage());
		if(ac.isLoadImage()){
			loadimage.setSummary("ҳ�����ͼƬ (Ĭ����WIFI�����¼���ͼƬ)");
		}else{
			loadimage.setSummary("ҳ�治����ͼƬ (Ĭ����WIFI�����¼���ͼƬ)");
		}
		loadimage.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
			public boolean onPreferenceClick(Preference preference) {
				UIHelper.changeSettingIsLoadImage(Setting.this,loadimage.isChecked());
				if(loadimage.isChecked()){
					loadimage.setSummary("ҳ�����ͼƬ (Ĭ����WIFI�����¼���ͼƬ)");
				}else{
					loadimage.setSummary("ҳ�治����ͼƬ (Ĭ����WIFI�����¼���ͼƬ)");
				}
				return true;
			}
		});
		
		//���һ���
		scroll = (CheckBoxPreference)findPreference("scroll");
		scroll.setChecked(ac.isScroll());
		if(ac.isScroll()){
			scroll.setSummary("���������һ���");
		}else{
			scroll.setSummary("�ѹر����һ���");
		}
		scroll.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
			public boolean onPreferenceClick(Preference preference) {
				ac.setConfigScroll(scroll.isChecked());
				if(scroll.isChecked()){
					scroll.setSummary("���������һ���");
				}else{
					scroll.setSummary("�ѹر����һ���");
				}
				return true;
			}
		});
		
		//��ʾ����
		voice = (CheckBoxPreference)findPreference("voice");
		voice.setChecked(ac.isVoice());
		if(ac.isVoice()){
			voice.setSummary("�ѿ�����ʾ����");
		}else{
			voice.setSummary("�ѹر���ʾ����");
		}
		voice.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
			public boolean onPreferenceClick(Preference preference) {
				ac.setConfigVoice(voice.isChecked());
				if(voice.isChecked()){
					voice.setSummary("�ѿ�����ʾ����");
				}else{
					voice.setSummary("�ѹر���ʾ����");
				}
				return true;
			}
		});
		
		//����������
		checkup = (CheckBoxPreference)findPreference("checkup");
		checkup.setChecked(ac.isCheckUp());
		checkup.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
			public boolean onPreferenceClick(Preference preference) {
				ac.setConfigCheckUp(checkup.isChecked());
				return true;
			}
		});
		
		//���㻺���С		
		long fileSize = 0;
		String cacheSize = "0KB";		
		File filesDir = getFilesDir();
		File cacheDir = getCacheDir();
		
		fileSize += FileUtils.getDirSize(filesDir);
		fileSize += FileUtils.getDirSize(cacheDir);		
		//2.2�汾���н�Ӧ�û���ת�Ƶ�sd���Ĺ���
		if(AppContext.isMethodsCompat(android.os.Build.VERSION_CODES.FROYO)){
			File externalCacheDir = MethodsCompat.getExternalCacheDir(this);
			fileSize += FileUtils.getDirSize(externalCacheDir);
		}		
		if(fileSize > 0)
			cacheSize = FileUtils.formatFileSize(fileSize);
		
		//�������
		cache = (Preference)findPreference("cache");
		cache.setSummary(cacheSize);
		cache.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
			public boolean onPreferenceClick(Preference preference) {
				UIHelper.clearAppCache(Setting.this);
				cache.setSummary("0KB");
				return true;
			}
		});
		
		//�������
		feedback = (Preference)findPreference("feedback");
		feedback.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
			public boolean onPreferenceClick(Preference preference) {
				UIHelper.showFeedBack(Setting.this);
				return true;
			}
		});
		
		//�汾����
		update = (Preference)findPreference("update");
		update.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
			public boolean onPreferenceClick(Preference preference) {
				UpdateManager.getUpdateManager().checkAppUpdate(Setting.this, true);
				return true;
			}
		});
		
		//��������
		about = (Preference)findPreference("about");
		about.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
			public boolean onPreferenceClick(Preference preference) {
				UIHelper.showAbout(Setting.this);
				return true;
			}
		});
		
	}
	public void back(View paramView)
	{
		finish();
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		
		if(intent.getBooleanExtra("LOGIN", false)){
			account.setTitle(R.string.main_menu_logout);
		}				
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		//����Activity&�Ӷ�ջ���Ƴ�
		AppManager.getAppManager().finishActivity(this);
	}	
}


package com.gm.wine.app.bean;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import com.gm.wine.app.AppException;
import com.gm.wine.app.common.StringUtils;
import com.google.gson.Gson;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

/**
 * Ӧ�ó������ʵ����
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-3-21
 */
public class Update implements Serializable{
	
	public final static String UTF8 = "UTF-8";
	public final static String NODE_ROOT = "oschina";
	
	private int versionCode;
	private String versionName;
	private String downloadUrl;
	private String updateLog;
	
	public int getVersionCode() {
		return versionCode;
	}
	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}
	public String getVersionName() {
		return versionName;
	}
	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}
	public String getDownloadUrl() {
		return downloadUrl;
	}
	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}
	public String getUpdateLog() {
		return updateLog;
	}
	public void setUpdateLog(String updateLog) {
		this.updateLog = updateLog;
	}
	
	public static Update parse(String data) throws IOException, AppException {
		return new Gson().fromJson(data, Update.class);
	}
}

package com.gm.wine.app.bean;

import java.io.Serializable;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;


import com.gm.wine.app.common.StringUtils;


/**
 * �ӿ�URLʵ����
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-3-21
 */
public class URLs implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public final static String HOST = "192.168.0.104:8088/wine-webapp";//  www.crafts-bamboo.com/wine
	public final static String HTTP = "http://";
	
	private final static String URL_SPLITTER = "/";
	private final static String URL_UNDERLINE = "_";
	
	private final static String URL_API_HOST = HTTP + HOST + URL_SPLITTER;
	public final static String LOGIN_VALIDATE_HTTP = URL_API_HOST + "loginvalidate.action";//��¼��֤
	public final static String NEWS_LIST = URL_API_HOST+"news.action";//������Ѷ�б�
	public final static String NEWS_DETAIL = URL_API_HOST+"news!detail.action";//������ϸ
	
	public final static String NOTICE_LIST = URL_API_HOST+"notice.action";//���Թ���
	
	public final static String USER_REGISTER=URL_API_HOST+"register.action";//�û�ע��
	public final static String USER_PASSWORD=URL_API_HOST+"modifypass.action";//�޸�����
	
	public final static String PRODUCT_LIST = URL_API_HOST+"product.action";//��Ʒ�б�
	public final static String PRODUCT_DETAIL = URL_API_HOST+"product!detail.action";//������ϸ
	

	public final static String UPDATE_VERSION = URL_API_HOST+"appversion.action";
	
	public final static int URL_OBJ_TYPE_NEWS = 0x001;
	public final static int URL_OBJ_TYPE_PRODUCT = 0x002;
	public final static int URL_OBJ_TYPE_NOTICE = 0x003;
	public final static int URL_OBJ_TYPE_OTHER = 0x004;
	
	private int objId;
	private String objKey = "";
	private int objType;
	
	public int getObjId() {
		return objId;
	}
	public void setObjId(int objId) {
		this.objId = objId;
	}
	public String getObjKey() {
		return objKey;
	}
	public void setObjKey(String objKey) {
		this.objKey = objKey;
	}
	public int getObjType() {
		return objType;
	}
	public void setObjType(int objType) {
		this.objType = objType;
	}
	/**
	 * ת��URLΪURLsʵ��
	 * @param path
	 * @return ����ת�������ӷ���null
	 */
	public final static URLs parseURL(String path) {
		if(StringUtils.isEmpty(path))return null;
		path = formatURL(path);
		URLs urls = new URLs();

		urls.setObjKey(path);
		urls.setObjType(URL_OBJ_TYPE_OTHER);

		return urls;
	}

	/**
	 * ����url���objId
	 * @param path
	 * @param url_type
	 * @return
	 */
	private final static String parseObjId(String path, String url_type){
		String objId = "";
		int p = 0;
		String str = "";
		String[] tmp = null;
		p = path.indexOf(url_type) + url_type.length();
		str = path.substring(p);
		if(str.contains(URL_SPLITTER)){
			tmp = str.split(URL_SPLITTER);
			objId = tmp[0];
		}else{
			objId = str;
		}
		return objId;
	}
	
	/**
	 * ����url���objKey
	 * @param path
	 * @param url_type
	 * @return
	 */
	private final static String parseObjKey(String path, String url_type){
		path = URLDecoder.decode(path);
		String objKey = "";
		int p = 0;
		String str = "";
		String[] tmp = null;
		p = path.indexOf(url_type) + url_type.length();
		str = path.substring(p);
		if(str.contains("?")){
			tmp = str.split("?");
			objKey = tmp[0];
		}else{
			objKey = str;
		}
		return objKey;
	}
	
	/**
	 * ��URL���и�ʽ����
	 * @param path
	 * @return
	 */
	private final static String formatURL(String path) {
		if(path.startsWith("http://") || path.startsWith("https://"))
			return path;
		return "http://" + URLEncoder.encode(path);
	}
	
	
}

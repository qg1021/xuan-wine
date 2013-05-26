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
	
	
}

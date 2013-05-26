package com.gm.wine.app.bean;

import java.io.Serializable;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;

import com.gm.wine.app.common.StringUtils;


/**
 * 接口URL实体类
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
	public final static String LOGIN_VALIDATE_HTTP = URL_API_HOST + "loginvalidate.action";//登录验证
	public final static String NEWS_LIST = URL_API_HOST+"news.action";//新闻资讯列表
	public final static String NEWS_DETAIL = URL_API_HOST+"news!detail.action";//新闻明细
	
	public final static String NOTICE_LIST = URL_API_HOST+"notice.action";//留言公告
	
	public final static String USER_REGISTER=URL_API_HOST+"register.action";//用户注册
	public final static String USER_PASSWORD=URL_API_HOST+"modifypass.action";//修改密码
	
	public final static String PRODUCT_LIST = URL_API_HOST+"product.action";//产品列表
	public final static String PRODUCT_DETAIL = URL_API_HOST+"product!detail.action";//新闻明细
	

	public final static String UPDATE_VERSION = URL_API_HOST+"appversion.action";
	
	
}

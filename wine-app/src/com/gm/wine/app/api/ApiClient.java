package com.gm.wine.app.api;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.commons.httpclient.params.HttpMethodParams;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.gm.wine.app.AppContext;
import com.gm.wine.app.AppException;
import com.gm.wine.app.bean.Result;
import com.gm.wine.app.bean.URLs;
import com.gm.wine.app.bean.Update;
import com.gm.wine.app.bean.User;
import com.gm.wine.vo.NewsList;
import com.gm.wine.vo.NewsVO;
import com.gm.wine.vo.NoticeList;
import com.gm.wine.vo.NoticeVO;
import com.gm.wine.vo.ProductList;
import com.gm.wine.vo.ProductVO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class ApiClient {

	public static final String UTF_8 = "UTF-8";
	public static final String DESC = "descend";
	public static final String ASC = "ascend";

	private final static int TIMEOUT_CONNECTION = 20000;
	private final static int TIMEOUT_SOCKET = 20000;
	private final static int RETRY_TIME = 3;

	private static String appCookie;
	private static String appUserAgent;

	public static void cleanCookie() {
		appCookie = "";
	}

	private static String getCookie(AppContext appContext) {
		if (appCookie == null || appCookie == "") {
			appCookie = appContext.getProperty("cookie");
		}
		return appCookie;
	}

	private static String getUserAgent(AppContext appContext) {
		if (appUserAgent == null || appUserAgent == "") {
			StringBuilder ua = new StringBuilder("OSChina.NET");
			ua.append('/' + appContext.getPackageInfo().versionName + '_'
					+ appContext.getPackageInfo().versionCode);// App�汾
			ua.append("/Android");// �ֻ�ϵͳƽ̨
			ua.append("/" + android.os.Build.VERSION.RELEASE);// �ֻ�ϵͳ�汾
			ua.append("/" + android.os.Build.MODEL); // �ֻ��ͺ�
			ua.append("/" + appContext.getAppId());// �ͻ���Ψһ��ʶ
			appUserAgent = ua.toString();
		}
		return appUserAgent;
	}

	private static HttpClient getHttpClient() {
		HttpClient httpClient = new HttpClient();
		// ���� HttpClient ���� Cookie,���������һ��Ĳ���
		httpClient.getParams().setCookiePolicy(
				CookiePolicy.BROWSER_COMPATIBILITY);
		// ���� Ĭ�ϵĳ�ʱ���Դ������
		httpClient.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler());
		// ���� ���ӳ�ʱʱ��
		httpClient.getHttpConnectionManager().getParams()
				.setConnectionTimeout(TIMEOUT_CONNECTION);
		// ���� ����ݳ�ʱʱ��
		httpClient.getHttpConnectionManager().getParams()
				.setSoTimeout(TIMEOUT_SOCKET);
		// ���� �ַ�
		httpClient.getParams().setContentCharset(UTF_8);
		return httpClient;
	}

	private static GetMethod getHttpGet(String url, String cookie,
			String userAgent) {
		GetMethod httpGet = new GetMethod(url);
		// ���� ����ʱʱ��
		httpGet.getParams().setSoTimeout(TIMEOUT_SOCKET);
		httpGet.setRequestHeader("Host", URLs.HOST);
		httpGet.setRequestHeader("Connection", "Keep-Alive");
		httpGet.setRequestHeader("Cookie", cookie);
		httpGet.setRequestHeader("User-Agent", userAgent);
		return httpGet;
	}

	private static PostMethod getHttpPost(String url, String cookie,
			String userAgent) {
		PostMethod httpPost = new PostMethod(url);
		// ���� ����ʱʱ��
		httpPost.getParams().setSoTimeout(TIMEOUT_SOCKET);
		httpPost.setRequestHeader("Host", URLs.HOST);
		httpPost.setRequestHeader("Connection", "Keep-Alive");
		httpPost.setRequestHeader("Cookie", cookie);
		httpPost.setRequestHeader("User-Agent", userAgent);
		return httpPost;
	}

	private static String _MakeURL(String p_url, Map<String, Object> params) {
		StringBuilder url = new StringBuilder(p_url);
		if (url.indexOf("?") < 0) {
			url.append('?');
		}

		for (String name : params.keySet()) {
			url.append('&');
			url.append(name);
			url.append('=');
			url.append(String.valueOf(params.get(name)));
			// ����URLEncoder����
			// url.append(URLEncoder.encode(String.valueOf(params.get(name)),
			// UTF_8));
		}

		return url.toString().replace("?&", "?");
	}
	/**
	 * ��ȡ����ͼƬ
	 * 
	 * @param url
	 * @return
	 */
	public static Bitmap getNetBitmap(String url) throws AppException {
		// System.out.println("image_url==> "+url);
		HttpClient httpClient = null;
		GetMethod httpGet = null;
		Bitmap bitmap = null;
		int time = 0;
		do {
			try {
				httpClient = getHttpClient();
				httpGet = getHttpGet(url, null, null);
				int statusCode = httpClient.executeMethod(httpGet);
				if (statusCode != HttpStatus.SC_OK) {
					throw AppException.http(statusCode);
				}
				InputStream inStream = httpGet.getResponseBodyAsStream();
				bitmap = BitmapFactory.decodeStream(inStream);
				inStream.close();
				break;
			} catch (HttpException e) {
				time++;
				if (time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
					}
					continue;
				}
				// ����������쳣��������Э�鲻�Ի��߷��ص�����������
				e.printStackTrace();
				throw AppException.http(e);
			} catch (IOException e) {
				time++;
				if (time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
					}
					continue;
				}
				// ���������쳣
				e.printStackTrace();
				throw AppException.network(e);
			} finally {
				// �ͷ�����
				httpGet.releaseConnection();
				httpClient = null;
			}
		} while (time < RETRY_TIME);
		return bitmap;
	}
	/**
	 * ���汾����
	 * 
	 * @param url
	 * @return
	 */
	public static Update checkVersion(AppContext appContext)
			throws AppException {
		try {
			return Update.parse(http_get(appContext, URLs.UPDATE_VERSION));
		} catch (Exception e) {
			if (e instanceof AppException) {
				throw (AppException) e;
			}
			throw AppException.network(e);
		}
	}
	/**
	 * get����URL
	 * 
	 * @param url
	 * @throws AppException
	 */
	private static String http_get(AppContext appContext, String url)
			throws AppException {
		String data = "";
		String cookie = getCookie(appContext);
		String userAgent = getUserAgent(appContext);

		HttpClient httpClient = null;
		GetMethod httpGet = null;

		String responseBody = "";
		int time = 0;
		do {
			try {
				httpClient = getHttpClient();
				httpGet = getHttpGet(url, cookie, userAgent);
				int statusCode = httpClient.executeMethod(httpGet);
				if (statusCode != HttpStatus.SC_OK) {
					throw AppException.http(statusCode);
				}
				responseBody = httpGet.getResponseBodyAsString();
				// System.out.println("XMLDATA=====>"+responseBody);
				break;
			} catch (HttpException e) {
				time++;
				if (time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
					}
					continue;
				}
				// ����������쳣��������Э�鲻�Ի��߷��ص�����������
				e.printStackTrace();
				throw AppException.http(e);
			} catch (IOException e) {
				time++;
				if (time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
					}
					continue;
				}
				// ���������쳣
				e.printStackTrace();
				throw AppException.network(e);
			} finally {
				// �ͷ�����
				httpGet.releaseConnection();
				httpClient = null;
			}
		} while (time < RETRY_TIME);

		if (responseBody.contains("data")) {
			try {
				Gson g = new Gson();
				data = g.fromJson(responseBody, Result.class).getData();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return data;
	}
	/**
	 * ����post����
	 * 
	 * @param url
	 * @param params
	 * @param files
	 * @throws AppException
	 */
	private static String _post(AppContext appContext, String url,
			Map<String, Object> params) throws AppException {
		// System.out.println("post_url==> "+url);
		String data = "";
		String cookie = getCookie(appContext);
		String userAgent = getUserAgent(appContext);

		HttpClient httpClient = null;
		PostMethod httpPost = null;

		// post�?������
		int length = (params == null ? 0 : params.size());
		Part[] parts = new Part[length];
		int i = 0;
		if (params != null) {
			for (String name : params.keySet()) {
				parts[i++] = new StringPart(name, String.valueOf(params
						.get(name)), UTF_8);
				// System.out.println("post_key==> "+name+"    value==>"+String.valueOf(params.get(name)));
			}
		}

		String responseBody = "";
		int time = 0;
		do {
			try {
				httpClient = getHttpClient();
				httpPost = getHttpPost(url, cookie, userAgent);
				httpPost.setRequestEntity(new MultipartRequestEntity(parts,
						httpPost.getParams()));
				int statusCode = httpClient.executeMethod(httpPost);
				if (statusCode != HttpStatus.SC_OK) {
					throw AppException.http(statusCode);
				} else if (statusCode == HttpStatus.SC_OK) {
					Cookie[] cookies = httpClient.getState().getCookies();
					String tmpcookies = "";
					for (Cookie ck : cookies) {
						tmpcookies += ck.toString() + ";";
					}
					// ����cookie
					if (appContext != null && tmpcookies != "") {
						appContext.setProperty("cookie", tmpcookies);
						appCookie = tmpcookies;
					}
				}
				responseBody = httpPost.getResponseBodyAsString();
				// System.out.println("XMLDATA=====>"+responseBody);
				break;
			} catch (HttpException e) {
				time++;
				if (time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
					}
					continue;
				}
				// ����������쳣��������Э�鲻�Ի��߷��ص�����������
				e.printStackTrace();
				throw AppException.http(e);
			} catch (IOException e) {
				time++;
				if (time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
					}
					continue;
				}
				// ���������쳣
				e.printStackTrace();
				throw AppException.network(e);
			} finally {
				// �ͷ�����
				httpPost.releaseConnection();
				httpClient = null;
			}
		} while (time < RETRY_TIME);

		if (responseBody.contains("data")) {
			try {
				Gson g = new Gson();
				data = g.fromJson(responseBody, Result.class).getData();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return data;
	}
	/**
	 * 
	 * 新闻列表
	 * 
	 * @since 2013-6-3
	 * @author qingang
	 * @param appContext
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 * @throws AppException
	 */
	public static NewsList getNewsList(AppContext appContext,
			final int pageIndex, final int pageSize) throws AppException {
		String url = _MakeURL(URLs.NEWS_LIST, new HashMap<String, Object>() {
			{
				put("pageNo", pageIndex);
				put("pageSize", pageSize);
			}
		});

		try {
			return new Gson().fromJson(http_get(appContext, url),
					new TypeToken<NewsList>() {
					}.getType());
		} catch (Exception e) {
			if (e instanceof AppException) {
				throw (AppException) e;
			}
			throw AppException.network(e);
		}
	}
	/**
	 * 
	 * 产品列表
	 * 
	 * @since 2013-6-3
	 * @author qingang
	 * @param appContext
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 * @throws AppException
	 */
	public static ProductList getProductList(AppContext appContext,
			final int pageIndex, final int pageSize) throws AppException {
		String url = _MakeURL(URLs.PRODUCT_LIST, new HashMap<String, Object>() {
			{
				put("pageNo", pageIndex);
				put("pageSize", pageSize);
			}
		});

		try {
			return new Gson().fromJson(http_get(appContext, url),
					new TypeToken<ProductList>() {
					}.getType());
		} catch (Exception e) {
			if (e instanceof AppException) {
				throw (AppException) e;
			}
			throw AppException.network(e);
		}
	}
	/**
	 * 
	 * 留言公告列表
	 * 
	 * @since 2013-6-3
	 * @author qingang
	 * @param appContext
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 * @throws AppException
	 */
	public static NoticeList getNoticeList(AppContext appContext,
			final int pageIndex, final int pageSize) throws AppException {
		String url = _MakeURL(URLs.NOTICE_LIST, new HashMap<String, Object>() {
			{
				put("pageNo", pageIndex);
				put("pageSize", pageSize);
			}
		});

		try {
			return new Gson().fromJson(http_get(appContext, url),
					new TypeToken<NoticeList>() {
					}.getType());
		} catch (Exception e) {
			if (e instanceof AppException) {
				throw (AppException) e;
			}
			throw AppException.network(e);
		}
	}
	/**
	 * 新闻明细
	 * 
	 * @param url
	 * @param news_id
	 * @return
	 * @throws AppException
	 */
	public static NewsVO getNewsDetail(AppContext appContext, final long news_id)
			throws AppException {
		String url = _MakeURL(URLs.NEWS_DETAIL, new HashMap<String, Object>() {
			{
				put("id", news_id);
			}
		});

		try {
			return new Gson().fromJson(http_get(appContext, url), NewsVO.class);
		} catch (Exception e) {
			if (e instanceof AppException) {
				throw (AppException) e;
			}
			throw AppException.network(e);
		}
	}
	/**
	 * 
	 * 产品详情
	 * 
	 * @since 2013-6-3
	 * @author qingang
	 * @param appContext
	 * @param product_id
	 * @return
	 * @throws AppException
	 */
	public static ProductVO getProductDetail(AppContext appContext,
			final long product_id) throws AppException {
		String url = _MakeURL(URLs.PRODUCT_DETAIL,
				new HashMap<String, Object>() {
					{
						put("id", product_id);
					}
				});

		try {
			return new Gson().fromJson(http_get(appContext, url),
					ProductVO.class);
		} catch (Exception e) {
			if (e instanceof AppException) {
				throw (AppException) e;
			}
			throw AppException.network(e);
		}
	}
	/**
	 * 
	 * 留言明细
	 * 
	 * @since 2013-6-3
	 * @author qingang
	 * @param appContext
	 * @param product_id
	 * @return
	 * @throws AppException
	 */
	public static NoticeVO getNoticeDetail(AppContext appContext,
			final long notice_id) throws AppException {
		String url = _MakeURL(URLs.PRODUCT_DETAIL,
				new HashMap<String, Object>() {
					{
						put("id", notice_id);
					}
				});

		try {
			return new Gson().fromJson(http_get(appContext, url),
					new TypeToken<NoticeVO>() {
					}.getType());
		} catch (Exception e) {
			if (e instanceof AppException) {
				throw (AppException) e;
			}
			throw AppException.network(e);
		}
	}
	/**
	 * ��¼�� �Զ�����cookie
	 * 
	 * @param url
	 * @param username
	 * @param pwd
	 * @return
	 * @throws AppException
	 */
	public static User login(AppContext appContext, String username, String pwd)
			throws AppException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("username", username);
		params.put("pwd", pwd);
		params.put("keep_login", 1);

		String loginurl = URLs.LOGIN_VALIDATE_HTTP;

		try {
			return new Gson().fromJson(_post(appContext, loginurl, params),
					User.class);
		} catch (Exception e) {
			if (e instanceof AppException) {
				throw (AppException) e;
			}
			throw AppException.network(e);
		}
	}

}

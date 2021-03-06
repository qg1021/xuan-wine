package com.gm.wine.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Hashtable;
import java.util.Properties;
import java.util.UUID;


import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.webkit.CacheManager;

import com.gm.wine.app.api.ApiClient;
import com.gm.wine.app.bean.User;
import com.gm.wine.app.common.CyptoUtils;
import com.gm.wine.app.common.StringUtils;
import com.gm.wine.app.common.UIHelper;
import com.gm.wine.vo.NewsList;
import com.gm.wine.vo.NewsVO;
import com.gm.wine.vo.NoticeList;
import com.gm.wine.vo.NoticeVO;
import com.gm.wine.vo.ProductList;
import com.gm.wine.vo.ProductVO;
import com.gm.wine.vo.UserVO;

public class AppContext extends Application {
	public static final int NETTYPE_WIFI = 0x01;
	public static final int NETTYPE_CMWAP = 0x02;
	public static final int NETTYPE_CMNET = 0x03;

	public static final int PAGE_SIZE = 20;

	public static final int PAGE_SIZE_10 = 10;
	private static final int CACHE_TIME = 60 * 60000;

	private boolean login = false;
	private long loginUid = 0;
	private final Hashtable<String, Object> memCacheRegion = new Hashtable<String, Object>();

	private final Handler unLoginHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
				UIHelper.ToastMessage(AppContext.this,
						getString(R.string.msg_login_error));
				UIHelper.showLoginDialog(AppContext.this);
			}
		}
	};

	@Override
	public void onCreate() {
		super.onCreate();

		Thread.setDefaultUncaughtExceptionHandler(AppException
				.getAppExceptionHandler());
	}

	public boolean isAudioNormal() {
		AudioManager mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
		return mAudioManager.getRingerMode() == AudioManager.RINGER_MODE_NORMAL;
	}

	public boolean isAppSound() {
		return isAudioNormal() && isVoice();
	}

	public boolean isNetworkConnected() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		return ni != null && ni.isConnectedOrConnecting();
	}

	public int getNetworkType() {
		int netType = 0;
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if (networkInfo == null) {
			return netType;
		}
		int nType = networkInfo.getType();
		if (nType == ConnectivityManager.TYPE_MOBILE) {
			String extraInfo = networkInfo.getExtraInfo();
			if (!StringUtils.isEmpty(extraInfo)) {
				if (extraInfo.toLowerCase().equals("cmnet")) {
					netType = NETTYPE_CMNET;
				} else {
					netType = NETTYPE_CMWAP;
				}
			}
		} else if (nType == ConnectivityManager.TYPE_WIFI) {
			netType = NETTYPE_WIFI;
		}
		return netType;
	}

	public static boolean isMethodsCompat(int VersionCode) {
		int currentVersion = android.os.Build.VERSION.SDK_INT;
		return currentVersion >= VersionCode;
	}

	public PackageInfo getPackageInfo() {
		PackageInfo info = null;
		try {
			info = getPackageManager().getPackageInfo(getPackageName(), 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace(System.err);
		}
		if (info == null) {
			info = new PackageInfo();
		}
		return info;
	}

	public String getAppId() {
		String uniqueID = getProperty(AppConfig.CONF_APP_UNIQUEID);
		if (StringUtils.isEmpty(uniqueID)) {
			uniqueID = UUID.randomUUID().toString();
			setProperty(AppConfig.CONF_APP_UNIQUEID, uniqueID);
		}
		return uniqueID;
	}

	public boolean isLogin() {
		return login;
	}
	/**
	 * 初始化用户登录信息
	 */
	public void initLoginInfo() {
		UserVO loginUser = getLoginInfo();
		if(loginUser!=null && loginUser.getId()>0 && loginUser.isRememberMe()){
			this.loginUid = loginUser.getId();
			this.login = true;
		}else{
			this.Logout();
		}
	}

	public long getLoginUid() {
		return this.loginUid;
	}

	public void Logout() {
		ApiClient.cleanCookie();
		this.cleanCookie();
		this.login = false;
		this.loginUid = 0;
	}

	public Handler getUnLoginHandler() {
		return this.unLoginHandler;
	}

	public NewsList getNewsList(int pageIndex, boolean isRefresh)
			throws AppException {
		NewsList list = null;
		String key = "wine_newslist_" + "_" + pageIndex + "_" + PAGE_SIZE;
		if (isNetworkConnected() && (!isReadDataCache(key) || isRefresh)) {
			try {
				list = ApiClient.getNewsList(this, pageIndex, PAGE_SIZE);
				if (list != null && pageIndex == 0) {

					list.setCacheKey(key);
					saveObject(list, key);
				}
			} catch (AppException e) {
				e.printStackTrace();
				list = (NewsList) readObject(key);
				if (list == null) {
					throw e;
				}
			}
		} else {
			list = (NewsList) readObject(key);
			if (list == null) {
				list = new NewsList();
			}
		}
		return list;
	}
	/**
	 * 新闻详情
	 * @param news_id
	 * @param isRefresh
	 * @return
	 * @throws AppException
	 */
	public NewsVO getNewsDetail(long news_id, boolean isRefresh) throws AppException {		
		NewsVO news = null;
		String key = "winenews_"+news_id;
		if(isNetworkConnected() && (!isReadDataCache(key) || isRefresh)) {
			try{
				news = ApiClient.getNewsDetail(this, news_id);
				if(news != null){
					news.setCacheKey(key);
					saveObject(news, key);
				}
			}catch(AppException e){
				news = (NewsVO)readObject(key);
				if(news == null)
					throw e;
			}
		} else {
			news = (NewsVO)readObject(key);
			if(news == null)
				news = new NewsVO();
		}
		return news;		
	}
	/**
	 * 产品列表
	 * @param pageIndex
	 * @param isRefresh
	 * @return
	 * @throws AppException
	 */
	public ProductList getProductList(int pageIndex, boolean isRefresh)
			throws AppException {
		ProductList list = null;
		String key = "wine_productlist_" + "_" + pageIndex + "_" + PAGE_SIZE_10;
		if (isNetworkConnected() && (!isReadDataCache(key) || isRefresh)) {
			try {
				list = ApiClient.getProductList(this, pageIndex, PAGE_SIZE_10);
				if (list != null && pageIndex == 0) {

					list.setCacheKey(key);
					saveObject(list, key);
				}
			} catch (AppException e) {
				list = (ProductList) readObject(key);
				if (list == null) {
					throw e;
				}
			}
		} else {
			list = (ProductList) readObject(key);
			if (list == null) {
				list = new ProductList();
			}
		}
		return list;
	}
	/**
	 * 产品明细
	 * @param product_id
	 * @param isRefresh
	 * @return
	 * @throws AppException
	 */
	public ProductVO getProductDetail(long product_id, boolean isRefresh) throws AppException {		
		ProductVO product = null;
		String key = "wineproduct_"+product_id;
		if(isNetworkConnected() && (!isReadDataCache(key) || isRefresh)) {
			try{
				product = ApiClient.getProductDetail(this, product_id);
				if(product != null){
					product.setCacheKey(key);
					saveObject(product, key);
				}
			}catch(AppException e){
				product = (ProductVO)readObject(key);
				if(product == null)
					throw e;
			}
		} else {
			product = (ProductVO)readObject(key);
			if(product == null)
				product = new ProductVO();
		}
		return product;		
	}
	/**
	 * 留言公告
	 * @param pageIndex
	 * @param isRefresh
	 * @return
	 * @throws AppException
	 */
	public NoticeList getNoticeList(int pageIndex, boolean isRefresh)
			throws AppException {
		NoticeList list = null;
		String key = "wine_noticelist_" + "_" + pageIndex + "_" + PAGE_SIZE_10;
		if (isNetworkConnected() && (!isReadDataCache(key) || isRefresh)) {
			try {
				list = ApiClient.getNoticeList(this, pageIndex, PAGE_SIZE_10);
				if (list != null && pageIndex == 0) {

					list.setCacheKey(key);
					saveObject(list, key);
				}
			} catch (AppException e) {
				list = (NoticeList) readObject(key);
				if (list == null) {
					throw e;
				}
			}
		} else {
			list = (NoticeList) readObject(key);
			if (list == null) {
				list = new NoticeList();
			}
		}
		return list;
	}
	/**
	 * 留言明细
	 * @param notice_id
	 * @param isRefresh
	 * @return
	 * @throws AppException
	 */
	public NoticeVO getNoticeDetail(long notice_id, boolean isRefresh) throws AppException {		
		NoticeVO notice = null;
		String key = "winenotice_"+notice_id;
		if(isNetworkConnected() && (!isReadDataCache(key) || isRefresh)) {
			try{
				notice = ApiClient.getNoticeDetail(this, notice_id);
				if(notice != null){
					notice.setCacheKey(key);
					saveObject(notice, key);
				}
			}catch(AppException e){
				notice = (NoticeVO)readObject(key);
				if(notice == null)
					throw e;
			}
		} else {
			notice = (NoticeVO)readObject(key);
			if(notice == null)
				notice = new NoticeVO();
		}
		return notice;		
	}


	public void cleanLoginInfo() {
		this.loginUid = 0;
		this.login = false;
		removeProperty("user.uid", "user.name", "user.face", "user.account",
				"user.pwd", "user.location", "user.followers", "user.fans",
				"user.score", "user.isRememberMe");
	}

	public Bitmap getUserFace(String key) throws AppException {
		FileInputStream fis = null;
		try {
			fis = openFileInput(key);
			return BitmapFactory.decodeStream(fis);
		} catch (Exception e) {
			throw AppException.run(e);
		} finally {
			try {
				fis.close();
			} catch (Exception e) {
			}
		}
	}

	public boolean isLoadImage() {
		String perf_loadimage = getProperty(AppConfig.CONF_LOAD_IMAGE);
		if (StringUtils.isEmpty(perf_loadimage)) {
			return true;
		} else {
			return StringUtils.toBool(perf_loadimage);
		}
	}

	public void setConfigLoadimage(boolean b) {
		setProperty(AppConfig.CONF_LOAD_IMAGE, String.valueOf(b));
	}

	public boolean isVoice() {
		String perf_voice = getProperty(AppConfig.CONF_VOICE);
		if (StringUtils.isEmpty(perf_voice)) {
			return true;
		} else {
			return StringUtils.toBool(perf_voice);
		}
	}

	public void setConfigVoice(boolean b) {
		setProperty(AppConfig.CONF_VOICE, String.valueOf(b));
	}

	public boolean isCheckUp() {
		String perf_checkup = getProperty(AppConfig.CONF_CHECKUP);

		if (StringUtils.isEmpty(perf_checkup)) {
			return true;
		} else {
			return StringUtils.toBool(perf_checkup);
		}
	}

	public void setConfigCheckUp(boolean b) {
		setProperty(AppConfig.CONF_CHECKUP, String.valueOf(b));
	}

	public boolean isScroll() {
		String perf_scroll = getProperty(AppConfig.CONF_SCROLL);

		if (StringUtils.isEmpty(perf_scroll)) {
			return false;
		} else {
			return StringUtils.toBool(perf_scroll);
		}
	}

	public void setConfigScroll(boolean b) {
		setProperty(AppConfig.CONF_SCROLL, String.valueOf(b));
	}

	public boolean isHttpsLogin() {
		String perf_httpslogin = getProperty(AppConfig.CONF_HTTPS_LOGIN);

		if (StringUtils.isEmpty(perf_httpslogin)) {
			return false;
		} else {
			return StringUtils.toBool(perf_httpslogin);
		}
	}

	public void setConfigHttpsLogin(boolean b) {
		setProperty(AppConfig.CONF_HTTPS_LOGIN, String.valueOf(b));
	}

	public void cleanCookie() {
		removeProperty(AppConfig.CONF_COOKIE);
	}

	private boolean isReadDataCache(String cachefile) {
		return readObject(cachefile) != null;
	}

	private boolean isExistDataCache(String cachefile) {
		boolean exist = false;
		File data = getFileStreamPath(cachefile);
		if (data.exists()) {
			exist = true;
		}
		return exist;
	}

	public boolean isCacheDataFailure(String cachefile) {
		boolean failure = false;
		File data = getFileStreamPath(cachefile);
		if (data.exists()
				&& (System.currentTimeMillis() - data.lastModified()) > CACHE_TIME) {
			failure = true;
		} else if (!data.exists()) {
			failure = true;
		}
		return failure;
	}

	public void clearAppCache() {
		File file = CacheManager.getCacheFileBaseDir();
		if (file != null && file.exists() && file.isDirectory()) {
			for (File item : file.listFiles()) {
				item.delete();
			}
			file.delete();
		}
		deleteDatabase("webview.db");
		deleteDatabase("webview.db-shm");
		deleteDatabase("webview.db-wal");
		deleteDatabase("webviewCache.db");
		deleteDatabase("webviewCache.db-shm");
		deleteDatabase("webviewCache.db-wal");

		clearCacheFolder(getFilesDir(), System.currentTimeMillis());
		clearCacheFolder(getCacheDir(), System.currentTimeMillis());

		Properties props = getProperties();
		for (Object key : props.keySet()) {
			String _key = key.toString();
			if (_key.startsWith("temp")) {
				removeProperty(_key);
			}
		}
	}

	private int clearCacheFolder(File dir, long curTime) {
		int deletedFiles = 0;
		if (dir != null && dir.isDirectory()) {
			try {
				for (File child : dir.listFiles()) {
					if (child.isDirectory()) {
						deletedFiles += clearCacheFolder(child, curTime);
					}
					if (child.lastModified() < curTime) {
						if (child.delete()) {
							deletedFiles++;
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return deletedFiles;
	}

	public void setMemCache(String key, Object value) {
		memCacheRegion.put(key, value);
	}

	public Object getMemCache(String key) {
		return memCacheRegion.get(key);
	}

	public void setDiskCache(String key, String value) throws IOException {
		FileOutputStream fos = null;
		try {
			fos = openFileOutput("cache_" + key + ".data", Context.MODE_PRIVATE);
			fos.write(value.getBytes());
			fos.flush();
		} finally {
			try {
				fos.close();
			} catch (Exception e) {
			}
		}
	}

	public String getDiskCache(String key) throws IOException {
		FileInputStream fis = null;
		try {
			fis = openFileInput("cache_" + key + ".data");
			byte[] datas = new byte[fis.available()];
			fis.read(datas);
			return new String(datas);
		} finally {
			try {
				fis.close();
			} catch (Exception e) {
			}
		}
	}

	public boolean saveObject(Serializable ser, String file) {
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		try {
			fos = openFileOutput(file, MODE_PRIVATE);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(ser);
			oos.flush();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				oos.close();
			} catch (Exception e) {
			}
			try {
				fos.close();
			} catch (Exception e) {
			}
		}
	}

	public Serializable readObject(String file) {
		if (!isExistDataCache(file)) {
			return null;
		}
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		try {
			fis = openFileInput(file);
			ois = new ObjectInputStream(fis);
			return (Serializable) ois.readObject();
		} catch (FileNotFoundException e) {
		} catch (Exception e) {
			e.printStackTrace();

			if (e instanceof InvalidClassException) {
				File data = getFileStreamPath(file);
				data.delete();
			}
		} finally {
			try {
				ois.close();
			} catch (Exception e) {
			}
			try {
				fis.close();
			} catch (Exception e) {
			}
		}
		return null;
	}

	public boolean containsProperty(String key) {
		Properties props = getProperties();
		return props.containsKey(key);
	}

	public void setProperties(Properties ps) {
		AppConfig.getAppConfig(this).set(ps);
	}

	public Properties getProperties() {
		return AppConfig.getAppConfig(this).get();
	}

	public void setProperty(String key, String value) {
		AppConfig.getAppConfig(this).set(key, value);
	}

	public String getProperty(String key) {
		return AppConfig.getAppConfig(this).get(key);
	}
	public void removeProperty(String... key) {
		AppConfig.getAppConfig(this).remove(key);
	}

	public UserVO loginVerify(String account, String pwd) throws AppException {
		return ApiClient.login(this, account, pwd);
	}
	public boolean pubMessage(NoticeVO notice) throws AppException{
		return ApiClient.pubMessage(this, notice.getTitle(), notice.getContent(), notice.getUser().getId());
	}
	public UserVO reg(String account,String password,String name) throws AppException{
		return ApiClient.reg(this, account,password,name);
	}
	public UserVO modifyPass(String account,String opassword,String npassword) throws AppException{
		return ApiClient.modifyPass(this, account,opassword,npassword);
	}

	public UserVO getLoginInfo() {
		UserVO lu = new UserVO();
		lu.setId(StringUtils.toInt(getProperty("user.uid"), 0));
		lu.setName(getProperty("user.name"));
		lu.setLoginName(getProperty("user.account"));
		lu.setPassword(CyptoUtils.decode("wineApp", getProperty("user.pwd")));
		lu.setRememberMe(StringUtils.toBool(getProperty("user.isRememberMe")));
		return lu;
	}

	public void saveLoginInfo(final UserVO user) {
		this.loginUid = user.getId();
		this.login = true;
		setProperties(new Properties() {
			{
				setProperty("user.uid", String.valueOf(user.getId()));
				setProperty("user.name", user.getName());
				setProperty("user.account", user.getLoginName());
				setProperty("user.pwd",
						CyptoUtils.encode("wineApp", user.getPassword()));
				setProperty("user.isRememberMe",
						String.valueOf(user.isRememberMe()));
			}
		});
	}
	
}

package com.gm.wine.app.ui;


import com.gm.wine.app.AppContext;
import com.gm.wine.app.AppException;
import com.gm.wine.app.R;
import com.gm.wine.app.common.StringUtils;
import com.gm.wine.app.common.UIHelper;

import com.gm.wine.vo.NewsVO;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;

import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import android.webkit.WebView;

import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

/**
 * 新闻详情
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-3-21
 */
public class NewsDetail extends BaseActivity {

	private FrameLayout mHeader;
	private ImageView mHome;
	private ImageView mRefresh;
	private TextView mHeadTitle;
	private ProgressBar mProgressbar;
	private ScrollView mScrollView;
    private ViewSwitcher mViewSwitcher;
    


	
	private TextView mTitle;
	private TextView mAuthor;
	private TextView mPubDate;

	
	private WebView mWebView;
    private Handler mHandler;
    private NewsVO newsDetail;
    private long newsId;
	
	private final static int VIEWSWITCH_TYPE_DETAIL = 0x001;
	private final static int VIEWSWITCH_TYPE_COMMENTS = 0x002;
	
	private final static int DATA_LOAD_ING = 0x001;
	private final static int DATA_LOAD_COMPLETE = 0x002;
	private final static int DATA_LOAD_FAIL = 0x003;

	private GestureDetector gd;
	private boolean isFullScreen;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_detail);
        
        this.initView();        
        this.initData();
        

    	
    	//注册双击全屏事件
    	this.regOnDoubleEvent();
    }
    
    //初始化视图控件
    @SuppressLint("SetJavaScriptEnabled")
    private void initView()
    {
		newsId = getIntent().getLongExtra("news_id", 0);
		
		
    	
    	mHeader = (FrameLayout)findViewById(R.id.news_detail_header);
    	mHome = (ImageView)findViewById(R.id.news_detail_home);
    	mRefresh = (ImageView)findViewById(R.id.news_detail_refresh);
    	mHeadTitle = (TextView)findViewById(R.id.news_detail_head_title);
    	mProgressbar = (ProgressBar)findViewById(R.id.news_detail_head_progress);
    	mViewSwitcher = (ViewSwitcher)findViewById(R.id.news_detail_viewswitcher);
    	mScrollView = (ScrollView)findViewById(R.id.news_detail_scrollview);
    	

    	
    	mTitle = (TextView)findViewById(R.id.news_detail_title);
    	mAuthor = (TextView)findViewById(R.id.news_detail_author);
    	mPubDate = (TextView)findViewById(R.id.news_detail_date);
    
    	
    
    	
    	mWebView = (WebView) findViewById(R.id.news_detail_webview);
		mWebView.getSettings().setSupportZoom(true);
		mWebView.getSettings().setBuiltInZoomControls(true);
		mWebView.getSettings().setDefaultFontSize(15);
        UIHelper.addWebImageShow(this, mWebView);
    	
    	mHome.setOnClickListener(homeClickListener);
    	mRefresh.setOnClickListener(refreshClickListener);
    	//mDetail.setOnClickListener(detailClickListener);
 
    	

    	

    }
    
    //初始化控件数据
	private void initData()
	{		
		mHandler = new Handler()
		{
			public void handleMessage(Message msg) 
			{				
				if(msg.what == 1)
				{	
					headButtonSwitch(DATA_LOAD_COMPLETE);					
					
					mTitle.setText(newsDetail.getTitle());
					mAuthor.setText(newsDetail.getSource());
					mPubDate.setText(StringUtils.friendly_time(newsDetail.getPublishdate()));
			
					
				
					
					String body = UIHelper.WEB_STYLE + newsDetail.getDesciption();					
					//读取用户设置：是否加载文章图片--默认有wifi下始终加载图片
					boolean isLoadImage;
					AppContext ac = (AppContext)getApplication();
					if(AppContext.NETTYPE_WIFI == ac.getNetworkType()){
						isLoadImage = true;
					}else{
						isLoadImage = ac.isLoadImage();
					}
					if(isLoadImage){
						// 过滤掉 img标签的width,height属性
						body = body.replaceAll(
								"(<img[^>]*?)\\s+width\\s*=\\s*\\S+", "$1");
						body = body.replaceAll(
								"(<img[^>]*?)\\s+height\\s*=\\s*\\S+", "$1");

						// 添加点击图片放大支持
						body = body.replaceAll("(<img[^>]+src=\")(\\S+)\"",
								"$1$2\" onClick=\"javascript:mWebViewImageListener.onImageClick('$2')\"");
					}else{
						//过滤掉 img标签
						body = body.replaceAll("<\\s*img\\s+([^>]*)\\s*>","");
					}
					
							
					
					mWebView.loadDataWithBaseURL(null, body, "text/html", "utf-8",null);
					mWebView.setWebViewClient(UIHelper.getWebViewClient());	
					

				}
				else if(msg.what == 0)
				{
					headButtonSwitch(DATA_LOAD_FAIL);
					
					UIHelper.ToastMessage(NewsDetail.this, R.string.msg_load_is_null);
				}
			
			}
		};
		
		initData(newsId, false);
	}
	
    private void initData(final long news_id, final boolean isRefresh)
    {		
    	headButtonSwitch(DATA_LOAD_ING);
    	
		new Thread(){
			public void run() {
                Message msg = new Message();
				try {
					newsDetail = ((AppContext)getApplication()).getNewsDetail(news_id, isRefresh);
	                msg.what = (newsDetail!=null && newsDetail.getId()>0) ? 1 : 0;
	                //msg.obj = (newsDetail!=null) ? newsDetail.getNotice() : null;//通知信息
	            } catch (AppException e) {
	                e.printStackTrace();
	            	msg.what = -1;
	            	msg.obj = e;
	            }				
                mHandler.sendMessage(msg);
			}
		}.start();
    }
    

    
    /**
     * 头部按钮展示
     * @param type
     */
    private void headButtonSwitch(int type) {
    	switch (type) {
		case DATA_LOAD_ING:
			mScrollView.setVisibility(View.GONE);
			mProgressbar.setVisibility(View.VISIBLE);
			mRefresh.setVisibility(View.GONE);
			break;
		case DATA_LOAD_COMPLETE:
			mScrollView.setVisibility(View.VISIBLE);
			mProgressbar.setVisibility(View.GONE);
			mRefresh.setVisibility(View.VISIBLE);
			break;
		case DATA_LOAD_FAIL:
			mScrollView.setVisibility(View.GONE);
			mProgressbar.setVisibility(View.GONE);
			mRefresh.setVisibility(View.VISIBLE);
			break;
		}
    }
    
	private View.OnClickListener homeClickListener = new View.OnClickListener() {
		public void onClick(View v) {	
			UIHelper.showHome(NewsDetail.this);
		}
	};
	
	private View.OnClickListener refreshClickListener = new View.OnClickListener() {
		public void onClick(View v) {	
			initData(newsId, true);
			
		}
	};

	

	


	

	

	
	/**
	 * 注册双击全屏事件
	 */
	private void regOnDoubleEvent(){
		gd = new GestureDetector(this,new GestureDetector.SimpleOnGestureListener(){
			@Override
			public boolean onDoubleTap(MotionEvent e) {
				isFullScreen = !isFullScreen;
				if (!isFullScreen) {   
                    WindowManager.LayoutParams params = getWindow().getAttributes();   
                    params.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);   
                    getWindow().setAttributes(params);   
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);  
                    mHeader.setVisibility(View.VISIBLE);
                   
                } else {    
                    WindowManager.LayoutParams params = getWindow().getAttributes();   
                    params.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;   
                    getWindow().setAttributes(params);   
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);   
                    mHeader.setVisibility(View.GONE);
                    
                }
				return true;
			}
		});
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		gd.onTouchEvent(event);
		return super.dispatchTouchEvent(event);
	}
}

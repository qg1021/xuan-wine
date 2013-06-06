package com.gm.wine.app.ui;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.gm.wine.app.AppContext;
import com.gm.wine.app.AppException;
import com.gm.wine.app.R;
import com.gm.wine.app.common.BitmapManager;
import com.gm.wine.app.common.StringUtils;
import com.gm.wine.app.common.UIHelper;
import com.gm.wine.vo.ProductVO;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

/**
 * 问答详情
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-3-21
 */
public class ProductDetail extends BaseActivity {

	private FrameLayout mHeader;
	private TextView mHeadTitle;
	private ProgressBar mProgressbar;
	private ScrollView mScrollView;
    private ViewSwitcher mViewSwitcher;

	private ImageView mBack;
	private ImageView mRefresh;
	
	private TextView mName;
	private TextView mPubDate;
	private TextView mPrice;
	private ImageView mPic;

	
	private WebView mWebView;
    private Handler mHandler;
    
    private ProductVO productVO;
    
    private long productId;
    private GestureDetector gd;
	private boolean isFullScreen;
	private BitmapManager bmpManager;

    
	private final static int VIEWSWITCH_TYPE_DETAIL = 0x001;
	private final static int VIEWSWITCH_TYPE_COMMENTS = 0x002;
	
	private final static int DATA_LOAD_ING = 0x001;
	private final static int DATA_LOAD_COMPLETE = 0x002;
	private final static int DATA_LOAD_FAIL = 0x003;

    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_detail);
        
        this.initView();        
        this.initData();
        

    	
    	//注册双击全屏事件
    	this.regOnDoubleEvent();
    }
    
    //初始化视图控件
    @SuppressLint("SetJavaScriptEnabled")
    private void initView()
    {
    	productId = getIntent().getLongExtra("product_id", 0);

    	
    	mHeader = (FrameLayout)findViewById(R.id.product_detail_header);
    	mBack = (ImageView)findViewById(R.id.product_detail_back);
    	mRefresh = (ImageView)findViewById(R.id.product_detail_refresh);
    	mHeadTitle = (TextView)findViewById(R.id.product_detail_head_title);
    	mProgressbar = (ProgressBar)findViewById(R.id.product_detail_head_progress);
    	mViewSwitcher = (ViewSwitcher)findViewById(R.id.product_detail_viewswitcher);
    	mScrollView = (ScrollView)findViewById(R.id.product_detail_scrollview);

    	
    	mName = (TextView)findViewById(R.id.product_detail_name);
    	mPubDate = (TextView)findViewById(R.id.product_detail_date);
    	mPrice = (TextView)findViewById(R.id.product_detail_price);
    	mPic = (ImageView)findViewById(R.id.product_detail_pic);

    	
    	mWebView = (WebView)findViewById(R.id.product_detail_webview);
    	mWebView.getSettings().setSupportZoom(true);
    	mWebView.getSettings().setBuiltInZoomControls(true);
    	mWebView.getSettings().setDefaultFontSize(15);
    	UIHelper.addWebImageShow(this, mWebView);
    	
    	mBack.setOnClickListener(UIHelper.finish(this));
    	mRefresh.setOnClickListener(refreshClickListener);
    	bmpManager = new BitmapManager(BitmapFactory.decodeResource(
    			this.getResources(), R.drawable.widget_dface_loading));
   

    }
    
    //初始化控件数据
	private void initData()
	{		
		mHandler = new Handler()
		{
			public void handleMessage(Message msg) {

				headButtonSwitch(DATA_LOAD_COMPLETE);
				
				if(msg.what == 1)
				{					
					mName.setText(productVO.getName());
					mPubDate.setText(StringUtils.friendly_time(productVO.getPubdate()));
					mPrice.setText(productVO.getPrice());
					String picurl = productVO.getPicurl();
					if (StringUtils.isEmpty(picurl)) {
						mPic.setImageResource(R.drawable.widget_dface);
					} else {
						bmpManager.loadBitmap(picurl, mPic);
					}
					mPic.setOnClickListener(imageClickListener);
					mPic.setTag(picurl);
					
					
		
					
					String body = UIHelper.WEB_STYLE + productVO.getRemark();
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
					
					UIHelper.ToastMessage(ProductDetail.this, R.string.msg_load_is_null);
				}
				else if(msg.what == -1 && msg.obj != null)
				{
					headButtonSwitch(DATA_LOAD_FAIL);
					
					((AppException)msg.obj).makeToast(ProductDetail.this);
				}
			}
		};
		
		initData(productId, false);
	}
	
    private void initData(final long product_id, final boolean isRefresh)
    {
    	headButtonSwitch(DATA_LOAD_ING);
		
		new Thread(){
			public void run() {
                Message msg = new Message();
				try {
					productVO = ((AppContext)getApplication()).getProductDetail(product_id, isRefresh);
	                msg.what = (productVO!=null && productVO.getId()>0) ? 1 : 0;
	               
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
    
    private View.OnClickListener refreshClickListener = new View.OnClickListener() {
		public void onClick(View v) {	
			initData(productId, true);
			
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
	private View.OnClickListener imageClickListener = new View.OnClickListener(){
	public void onClick(View v) {
		UIHelper.showImageZoomDialog(v.getContext(), (String)v.getTag());
	}
};
}

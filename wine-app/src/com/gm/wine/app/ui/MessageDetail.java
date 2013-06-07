package com.gm.wine.app.ui;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.gm.wine.app.AppConfig;
import com.gm.wine.app.AppContext;
import com.gm.wine.app.AppException;
import com.gm.wine.app.R;
import com.gm.wine.app.adapter.ListViewMessageAdapter;
import com.gm.wine.app.adapter.ListViewMessageDetailAdapter;
import com.gm.wine.app.common.StringUtils;
import com.gm.wine.app.common.UIHelper;
import com.gm.wine.app.widget.PullToRefreshListView;
import com.gm.wine.vo.NoticeVO;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ViewSwitcher;

/**
 * 留言详情
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-3-21
 */
public class MessageDetail extends BaseActivity {
	
	private ImageView mBack;
	private ImageView mRefresh;
	private LinearLayout mLinearlayout;
	private ProgressBar mProgressbar;
	
	private PullToRefreshListView mLvMessage;
	private ListViewMessageDetailAdapter lvMessageAdapter;
	private List<NoticeVO> lvMessageData = new ArrayList<NoticeVO>();


    
    private View lvHeader;
    private TextView username;
    private TextView date;
    private TextView commentCount;
    private WebView content;
    private ImageView image;
    private Handler mHandler;
    private NoticeVO messageDetail;
    
    private long messageId;



	

	
 
	
	private final static int DATA_LOAD_ING = 0x001;
	private final static int DATA_LOAD_COMPLETE = 0x002;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_detail);
        
        //初始化视图控件
        this.initView();
        //初始化控件数据
        this.initData();   

    }
    
    /**
     * 头部加载展示
     * @param type
     * @param action 1-init 2-refresh
     */
    private void headButtonSwitch(int type,int action) {
    	switch (type) {
		case DATA_LOAD_ING:
			if(action==1)mLinearlayout.setVisibility(View.GONE);
			mProgressbar.setVisibility(View.VISIBLE);
			mRefresh.setVisibility(View.GONE);
			break;
		case DATA_LOAD_COMPLETE:
			mLinearlayout.setVisibility(View.VISIBLE);
			mProgressbar.setVisibility(View.GONE);
			mRefresh.setVisibility(View.VISIBLE);
			break;
		}
    }
    
    //初始化视图控件
    private void initView()
    {    	
		messageId = getIntent().getLongExtra("message_id", 0);
		
    	
    	
    	
    	mBack = (ImageView)findViewById(R.id.message_detail_back);
    	mRefresh = (ImageView)findViewById(R.id.message_detail_refresh);
    	mLinearlayout = (LinearLayout)findViewById(R.id.message_detail_linearlayout);
    	mProgressbar = (ProgressBar)findViewById(R.id.message_detail_head_progress);
    	
    	
    	mBack.setOnClickListener(UIHelper.finish(this));
    	mRefresh.setOnClickListener(refreshClickListener);
    	
    	

    	
    	lvHeader = View.inflate(this, R.layout.message_detail_content, null);

    	username = (TextView)lvHeader.findViewById(R.id.message_detail_username);
    	date = (TextView)lvHeader.findViewById(R.id.message_detail_date);
    	commentCount = (TextView)lvHeader.findViewById(R.id.message_detail_commentCount);
    	image = (ImageView)lvHeader.findViewById(R.id.tweet_listitem_image);
    	
    	content = (WebView)lvHeader.findViewById(R.id.message_detail_content);
    	content.getSettings().setJavaScriptEnabled(false);
    	content.getSettings().setSupportZoom(true);
    	content.getSettings().setBuiltInZoomControls(true);
    	content.getSettings().setDefaultFontSize(12);
    	


    	lvMessageAdapter = new ListViewMessageDetailAdapter(this, lvMessageData, R.layout.message_detail_listitem); 
    	mLvMessage = (PullToRefreshListView)findViewById(R.id.message_list_listview);
    	
    	mLvMessage.addHeaderView(lvHeader);//把动弹详情放进listview头部
    	//mLvMessage.addFooterView(lvMessage_footer);//添加底部视图  必须在setAdapter前
        mLvMessage.setAdapter(lvMessageAdapter); 
        mLvMessage.setOnScrollListener(new AbsListView.OnScrollListener() {
        

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				loadMessageDetail(messageId, mHandler, true);
				
			}
        });



    }
    
    //初始化控件数据
	private void initData()
	{		
    	//加载动弹
    	mHandler = new Handler(){
			public void handleMessage(Message msg) {
				
				headButtonSwitch(DATA_LOAD_COMPLETE,1);
				
				if(msg.what == 1){
					username.setText(messageDetail.getUser().getName());
					date.setText(StringUtils.friendly_time(messageDetail.getCreatedate()));
					commentCount.setText(messageDetail.getReplyNum()+"");
					
					String body = UIHelper.WEB_STYLE + messageDetail.getContent();
					body = body.replaceAll("(<img[^>]*?)\\s+width\\s*=\\s*\\S+","$1");
					body = body.replaceAll("(<img[^>]*?)\\s+height\\s*=\\s*\\S+","$1");

					content.loadDataWithBaseURL(null, body, "text/html", "utf-8",null);
					content.setWebViewClient(UIHelper.getWebViewClient());
					
					lvMessageData.clear();//先清除原有数据
					lvMessageData.addAll(messageDetail.getChilds());
					

				}else if(msg.what == 0){
						UIHelper.ToastMessage(MessageDetail.this, R.string.msg_load_is_null);	
				}else{
					((AppException)msg.obj).makeToast(MessageDetail.this);
				}
			}
		};
    	this.loadMessageDetail(messageId, mHandler, false);
	}

	
	/**
	 * 线程加载留言详情
	 * @param tweetId
	 * @param handler
	 */
	private void loadMessageDetail(final long  message_id, final Handler handler, final boolean isRefresh){
		
		this.headButtonSwitch(DATA_LOAD_ING,1);
		
		new Thread(){
			public void run() {
				Message msg = new Message();
				try {
					messageDetail = ((AppContext)getApplication()).getNoticeDetail(message_id, isRefresh);
					msg.what = (messageDetail!=null && messageDetail.getId()>0) ? 1 : 0;
				} catch (AppException e) {
	            	e.printStackTrace();
	            	msg.what = -1;
	            	msg.obj = e;
				}
				handler.sendMessage(msg);
			}
		}.start();
	}


	

    

	
    private View.OnClickListener refreshClickListener = new View.OnClickListener() {
		public void onClick(View v) {	
			loadMessageDetail(messageId, mHandler, true);
		}
	};
	

	

	
}

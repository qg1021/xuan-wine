package com.gm.wine.app.ui;


import com.gm.wine.app.AppConfig;
import com.gm.wine.app.AppContext;
import com.gm.wine.app.AppException;
import com.gm.wine.app.R;
import com.gm.wine.app.common.StringUtils;
import com.gm.wine.app.common.UIHelper;
import com.gm.wine.vo.NoticeVO;
import com.gm.wine.vo.UserVO;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

/**
 * 发表帖子
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-3-21
 */
public class MessagePub extends BaseActivity{

	private ImageView mBack;
	private EditText mTitle;
	private EditText mContent;
	private Button mPublish;
    private ProgressDialog mProgress;
	private NoticeVO nv;
	private InputMethodManager imm;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.question_pub);
		
		this.initView();
		
	}
	
    //初始化视图控件
    private void initView()
    {    	
    	imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
    	
    	mBack = (ImageView)findViewById(R.id.question_pub_back);
    	mPublish = (Button)findViewById(R.id.question_pub_publish);
    	mTitle = (EditText)findViewById(R.id.question_pub_title);
    	mContent = (EditText)findViewById(R.id.question_pub_content);

    	
    	mBack.setOnClickListener(UIHelper.finish(this));
    	mPublish.setOnClickListener(publishClickListener);

    	//编辑器添加文本监听
    	mTitle.addTextChangedListener(UIHelper.getTextWatcher(this, AppConfig.TEMP_POST_TITLE));
    	mContent.addTextChangedListener(UIHelper.getTextWatcher(this, AppConfig.TEMP_POST_CONTENT));
    	
    	//显示临时编辑内容
    	UIHelper.showTempEditContent(this, mTitle, AppConfig.TEMP_POST_TITLE);
    	UIHelper.showTempEditContent(this, mContent, AppConfig.TEMP_POST_CONTENT);

    }
	

    
	private View.OnClickListener publishClickListener = new View.OnClickListener() {
		public void onClick(View v) {
			//隐藏软键盘
			imm.hideSoftInputFromWindow(v.getWindowToken(), 0);  
			
			String title = mTitle.getText().toString();
			if(StringUtils.isEmpty(title)){
				UIHelper.ToastMessage(v.getContext(), "请输入标题");
				return;
			}
			String content = mContent.getText().toString();
			if(StringUtils.isEmpty(content)){
				UIHelper.ToastMessage(v.getContext(), "请输入留言内容");
				return;
			}
			
			final AppContext ac = (AppContext)getApplication();
			if(!ac.isLogin()){
				UIHelper.showLoginDialog(MessagePub.this);
				return;
			}
			
			mProgress = ProgressDialog.show(v.getContext(), null, "发布中···",true,true); 
			
			nv = new NoticeVO();
			nv.setTitle(title);
			nv.setContent(content);
			nv.setUser(new UserVO(ac.getLoginUid(),null,null));
		
			
			final Handler handler = new Handler(){
				public void handleMessage(Message msg) {
					if(mProgress!=null)mProgress.dismiss();
					if(msg.what == 1){
						boolean res = (Boolean)msg.obj;
						
						if(res){
							UIHelper.ToastMessage(MessagePub.this, "发布成功");
							//清除之前保存的编辑内容
							ac.removeProperty(AppConfig.TEMP_POST_TITLE,AppConfig.TEMP_POST_CONTENT);
							//跳转到文章详情
							finish();
						}else{
							UIHelper.ToastMessage(MessagePub.this, "发布失败");
						}
						
					}
					else {
						((AppException)msg.obj).makeToast(MessagePub.this);
					}
				}
			};
			new Thread(){
				public void run() {
					Message msg = new Message();
					boolean isPub  = false;
					try {
						isPub = ac.pubMessage(nv);
						msg.what = 1;
						msg.obj = isPub;
		            } catch (AppException e) {
		            	e.printStackTrace();
						msg.what = -1;
						msg.obj = e;
		            }
					handler.sendMessage(msg);
				}
			}.start();
		}
	};
}

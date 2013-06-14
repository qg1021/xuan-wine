package com.gm.wine.app.ui;




import com.gm.wine.app.AppContext;
import com.gm.wine.app.AppException;
import com.gm.wine.app.R;
import com.gm.wine.app.api.ApiClient;
import com.gm.wine.app.bean.User;
import com.gm.wine.app.common.StringUtils;
import com.gm.wine.app.common.UIHelper;
import com.gm.wine.vo.UserVO;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ViewSwitcher;

/**
 * 用户注册对话框
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-3-21
 */
public class RegDialog extends BaseActivity{
	
	private ViewSwitcher mViewSwitcher;
	private ImageButton btn_close;
	private Button btn_login;
	private Button btn_reg;
	private AutoCompleteTextView mAccount;
	private EditText mPwd;
	private EditText mName;
	private AnimationDrawable loadingAnimation;
	private View loginLoading;
	private int curLoginType;
	private InputMethodManager imm;

	
	public final static int LOGIN_OTHER = 0x00;
	public final static int LOGIN_MAIN = 0x01;
	public final static int LOGIN_SETTING = 0x02;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reg_dialog);
        
        imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        
        curLoginType = getIntent().getIntExtra("LOGINTYPE", LOGIN_OTHER);
        
        mViewSwitcher = (ViewSwitcher)findViewById(R.id.regdialog_view_switcher);       
        loginLoading = (View)findViewById(R.id.reg_loading);
        mAccount = (AutoCompleteTextView)findViewById(R.id.reg_account);
        mPwd = (EditText)findViewById(R.id.reg_password);
        mName = (EditText)findViewById(R.id.reg_name);
   
        
        btn_close = (ImageButton)findViewById(R.id.reg_close_button);
        btn_close.setOnClickListener(UIHelper.finish(this));        
        
        btn_login = (Button)findViewById(R.id.login_btn_reg);
        btn_reg= (Button)findViewById(R.id.reg_btn_reg);
        btn_reg.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				//隐藏软键盘
				imm.hideSoftInputFromWindow(v.getWindowToken(), 0);  
				
				String account = mAccount.getText().toString();
				String pwd = mPwd.getText().toString();
				String name = mName.getText().toString();
				//boolean isRememberMe = chb_rememberMe.isChecked();
				//判断输入
				if(StringUtils.isEmpty(account)){
					UIHelper.ToastMessage(v.getContext(), getString(R.string.msg_login_email_null));
					return;
				}
				if(StringUtils.isEmpty(pwd)){
					UIHelper.ToastMessage(v.getContext(), getString(R.string.msg_login_pwd_null));
					return;
				}
				if(StringUtils.isEmpty(name)){
					UIHelper.ToastMessage(v.getContext(), getString(R.string.msg_login_name_null));
					return;
				}
				
		        btn_close.setVisibility(View.GONE);
		        loadingAnimation = (AnimationDrawable)loginLoading.getBackground();
		        loadingAnimation.start();
		        mViewSwitcher.showNext();
		        
		        reg(account, pwd, name);
			}
		});
        btn_login.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				//隐藏软键盘
				imm.hideSoftInputFromWindow(v.getWindowToken(), 0);  
				
				finish();
				UIHelper.showLoginDialog(RegDialog.this);
			}
		});

        
    }
    
  //登录验证
    private void reg(final String account, final String password, final String name) {
		final Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				if(msg.what == 1){
					UserVO user = (UserVO)msg.obj;
					if(user != null){
						UIHelper.ToastMessage(RegDialog.this, R.string.msg_reg_success);
					
						finish();
					}
				}else if(msg.what == 0){
					mViewSwitcher.showPrevious();
					btn_close.setVisibility(View.VISIBLE);
					UIHelper.ToastMessage(RegDialog.this, getString(R.string.msg_reg_fail)+msg.obj);
				}else if(msg.what == -1){
					mViewSwitcher.showPrevious();
					btn_close.setVisibility(View.VISIBLE);
					((AppException)msg.obj).makeToast(RegDialog.this);
				}
			}
		};
		new Thread(){
			public void run() {
				Message msg =new Message();
				try {
					AppContext ac = (AppContext)getApplication(); 
	                UserVO user = ac.reg(account, password, name);
	                user.setLoginName(account);
	                user.setPassword(password);
	                user.setName(name);
	                int res = user.getErrorCode();
	                if(res==0){
	                	//ac.saveLoginInfo(user);//保存登录信息
	                	msg.what = 1;//成功
	                	msg.obj = user;
	                }else{
	                	msg.what = 0;//失败
	                	msg.obj = user.getErrorMessage();
	                }
	            } catch (AppException e) {
	            	e.printStackTrace();
			    	msg.what = -1;
			    	msg.obj = e;
	            }
				handler.sendMessage(msg);
			}
		}.start();
    }
    
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	if(keyCode == KeyEvent.KEYCODE_BACK) {
    		this.onDestroy();
    	}
    	return super.onKeyDown(keyCode, event);
    }
}

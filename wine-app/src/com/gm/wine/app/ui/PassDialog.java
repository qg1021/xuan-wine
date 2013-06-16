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
import android.widget.TextView;
import android.widget.ViewSwitcher;

/**
 * 修改密码对话框
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-3-21
 */
public class PassDialog extends BaseActivity{
	
	private ViewSwitcher mViewSwitcher;
	private ImageButton btn_close;
	private Button btn_pass;
	private EditText mOPwd;
	private EditText mNPwd;
	private TextView mName;
	private AnimationDrawable loadingAnimation;
	private View loginLoading;
	private InputMethodManager imm;

	

	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pass_dialog);
        
        imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        

        
        mViewSwitcher = (ViewSwitcher)findViewById(R.id.passdialog_view_switcher);       
        loginLoading = (View)findViewById(R.id.reg_loading);
        mOPwd = (EditText)findViewById(R.id.reg_old_password);
        mNPwd = (EditText)findViewById(R.id.reg_new_password);
        mName = (TextView)findViewById(R.id.pass_account);
        final AppContext ac = (AppContext)getApplication();
        mName.setText(ac.getLoginInfo().getLoginName());
        
        btn_close = (ImageButton)findViewById(R.id.pass_close_button);
        btn_close.setOnClickListener(UIHelper.finish(this));        
        

        btn_pass= (Button)findViewById(R.id.pass_btn_pass);
        btn_pass.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				//隐藏软键盘
				imm.hideSoftInputFromWindow(v.getWindowToken(), 0);  
				
		
				String opwd = mOPwd.getText().toString();
				String npwd = mNPwd.getText().toString();

				if(StringUtils.isEmpty(opwd)){
					UIHelper.ToastMessage(v.getContext(), getString(R.string.msg_old_pwd_null));
					return;
				}
				if(StringUtils.isEmpty(npwd)){
					UIHelper.ToastMessage(v.getContext(), getString(R.string.msg_new_pwd_null));
					return;
				}

				
		        btn_close.setVisibility(View.GONE);
		        loadingAnimation = (AnimationDrawable)loginLoading.getBackground();
		        loadingAnimation.start();
		        mViewSwitcher.showNext();
		        
		        modifyPass( opwd, npwd);
			}
		});


        
    }
    
  //登录验证
    private void modifyPass(final String opassword, final String npassword) {
		final Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				if(msg.what == 1){
					AppContext ac = (AppContext)getApplication(); 
					UserVO user = (UserVO)msg.obj;
					if(user != null){
						UIHelper.ToastMessage(PassDialog.this, user.getErrorMessage());
						ac.Logout();
						finish();
					}
				}else if(msg.what == 0){
					mViewSwitcher.showPrevious();
					btn_close.setVisibility(View.VISIBLE);
					UIHelper.ToastMessage(PassDialog.this,msg.obj.toString());
				}else if(msg.what == -1){
					mViewSwitcher.showPrevious();
					btn_close.setVisibility(View.VISIBLE);
					((AppException)msg.obj).makeToast(PassDialog.this);
				}
			}
		};
		new Thread(){
			public void run() {
				Message msg =new Message();
				try {
					AppContext ac = (AppContext)getApplication(); 
	                UserVO user = ac.modifyPass(ac.getLoginInfo().getLoginName(), opassword, npassword);
	                int res = user.getErrorCode();
	                if(res==0){
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

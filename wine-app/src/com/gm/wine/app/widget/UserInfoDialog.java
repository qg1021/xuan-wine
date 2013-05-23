package com.gm.wine.app.widget;


import com.gm.wine.app.R;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.WindowManager.LayoutParams;

/**
 * 用户信息对话框控�?
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-7-2
 */
public class UserInfoDialog extends Dialog {
	
	private LayoutParams lp;

	public UserInfoDialog(Context context) {
		super(context, R.style.Dialog);		
		setContentView(R.layout.user_center_content);
		
		// 设置点击对话框之外能消失
		setCanceledOnTouchOutside(true);
		// 设置window属�?
		lp = getWindow().getAttributes();
		lp.gravity = Gravity.TOP;
		lp.dimAmount = 0; // 去背景遮�?
		lp.alpha = 1.0f;
		lp.y = 55;
		getWindow().setAttributes(lp);

	}
}

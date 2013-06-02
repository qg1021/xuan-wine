package com.gm.wine.app.widget;



import com.gm.wine.app.R;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * ������Toast��ʾ�ؼ�(�����ֲ���)
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-8-30
 */
public class NewDataToast extends Toast{
	
	private MediaPlayer mPlayer;
	private boolean isSound;
	
	public NewDataToast(Context context) {
		this(context, false);
	}
	
	public NewDataToast(Context context, boolean isSound) {
		super(context);
		
		this.isSound = isSound;

        mPlayer = MediaPlayer.create(context, R.raw.newdatatoast);
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){
			@Override
			public void onCompletion(MediaPlayer mp) {
				mp.release();
			}        	
        });

    }

	@Override
	public void show() {
		super.show();
		
		if(isSound){
			mPlayer.start();
		}
	}
	
	/**
	 * �����Ƿ񲥷�����
	 */
	public void setIsSound(boolean isSound) {
		this.isSound = isSound;
	}
	
	/**
	 * ��ȡ�ؼ�ʵ��
	 * @param context
	 * @param text ��ʾ��Ϣ
	 * @param isSound �Ƿ񲥷�����
	 * @return
	 */
	public static NewDataToast makeText(Context context, CharSequence text, boolean isSound) {
		NewDataToast result = new NewDataToast(context, isSound);
		
        LayoutInflater inflate = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        
        View v = inflate.inflate(R.layout.new_data_toast, null);
        v.setMinimumWidth(dm.widthPixels);//���ÿؼ���С���Ϊ�ֻ���Ļ���
        
        TextView tv = (TextView)v.findViewById(R.id.new_data_toast_message);
        tv.setText(text);
        
        result.setView(v);
        result.setDuration(600);
        result.setGravity(Gravity.TOP, 0, (int)(dm.density*75));

        return result;
    }
	
}

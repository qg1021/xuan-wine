package com.gm.wine.app.common;



import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.gm.wine.app.AppContext;
import com.gm.wine.app.AppManager;
import com.gm.wine.app.Main;
import com.gm.wine.app.R;
import com.gm.wine.app.ui.LoginDialog;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.StyleSpan;
import android.view.Menu;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class UIHelper {

	public final static int LISTVIEW_ACTION_INIT = 0x01;
	public final static int LISTVIEW_ACTION_REFRESH = 0x02;
	public final static int LISTVIEW_ACTION_SCROLL = 0x03;
	public final static int LISTVIEW_ACTION_CHANGE_CATALOG = 0x04;
	
	public final static int LISTVIEW_DATA_MORE = 0x01;
	public final static int LISTVIEW_DATA_LOADING = 0x02;
	public final static int LISTVIEW_DATA_FULL = 0x03;
	public final static int LISTVIEW_DATA_EMPTY = 0x04;
	
	public final static int LISTVIEW_DATATYPE_NEWS = 0x01;
	public final static int LISTVIEW_DATATYPE_BLOG = 0x02;
	public final static int LISTVIEW_DATATYPE_POST = 0x03;
	public final static int LISTVIEW_DATATYPE_TWEET = 0x04;
	public final static int LISTVIEW_DATATYPE_ACTIVE = 0x05;
	public final static int LISTVIEW_DATATYPE_MESSAGE = 0x06;
	public final static int LISTVIEW_DATATYPE_COMMENT = 0x07;
	
	public final static int REQUEST_CODE_FOR_RESULT = 0x01;
	public final static int REQUEST_CODE_FOR_REPLY = 0x02;
	
	/** ����ͼƬƥ�� */
	private static Pattern facePattern = Pattern.compile("\\[{1}([0-9]\\d*)\\]{1}");
	
	/** ȫ��web��ʽ */
	public final static String WEB_STYLE = "<style>* {font-size:16px;line-height:20px;} p {color:#333;} a {color:#3E62A6;} img {max-width:310px;} " +
			"img.alignleft {float:left;max-width:120px;margin:0 10px 5px 0;border:1px solid #ccc;background:#fff;padding:2px;} " +
			"pre {font-size:9pt;line-height:12pt;font-family:Courier New,Arial;border:1px solid #ddd;border-left:5px solid #6CE26C;background:#f6f6f6;padding:5px;} " +
			"a.tag {font-size:15px;text-decoration:none;background-color:#bbd6f3;border-bottom:2px solid #3E6D8E;border-right:2px solid #7F9FB6;color:#284a7b;margin:2px 2px 2px 0;padding:2px 4px;white-space:nowrap;}</style>";
	/**
	 * ��ʾ��ҳ
	 * @param activity
	 */
	public static void showHome(Activity activity)
	{
		Intent intent = new Intent(activity,Main.class);
		activity.startActivity(intent);
		activity.finish();
	}
	
	/**
	 * ��ʾ��¼ҳ��
	 * @param activity
	 */
	public static void showLoginDialog(Context context)
	{
		Intent intent = new Intent(context,LoginDialog.class);
		if(context instanceof Main)
			intent.putExtra("LOGINTYPE", LoginDialog.LOGIN_MAIN);
		else if(context instanceof Setting)
			intent.putExtra("LOGINTYPE", LoginDialog.LOGIN_SETTING);
		else
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}
	
	/**
	 * ��ʾ��������
	 * @param context
	 * @param newsId
	 */
	public static void showNewsDetail(Context context, int newsId)
	{
		Intent intent = new Intent(context, NewsDetail.class);
		intent.putExtra("news_id", newsId);
		context.startActivity(intent);
	}
	
	/**
	 * ��ʾ��������
	 * @param context
	 * @param postId
	 */
	public static void showQuestionDetail(Context context, int postId)
	{
		Intent intent = new Intent(context, QuestionDetail.class);
		intent.putExtra("post_id", postId);
		context.startActivity(intent);
	}
	
	/**
	 * ��ʾ���Tag�����б�
	 * @param context
	 * @param tag
	 */
	public static void showQuestionListByTag(Context context, String tag)
	{
		Intent intent = new Intent(context, QuestionTag.class);
		intent.putExtra("post_tag", tag);
		context.startActivity(intent);
	}
	
	/**
	 * ��ʾ��Ҫ����ҳ��
	 * @param context
	 */
	public static void showQuestionPub(Context context)
	{
		Intent intent = new Intent(context, QuestionPub.class);
		context.startActivity(intent);
	}
	
	/**
	 * ��ʾ�������鼰����
	 * @param context
	 * @param tweetId
	 */
	public static void showTweetDetail(Context context, int tweetId)
	{
		Intent intent = new Intent(context, TweetDetail.class);
		intent.putExtra("tweet_id", tweetId);
		context.startActivity(intent);
	}
	
	/**
	 * ��ʾ����һ��ҳ��
	 * @param context
	 */
	public static void showTweetPub(Activity context)
	{
		Intent intent = new Intent(context, TweetPub.class);
		context.startActivityForResult(intent, REQUEST_CODE_FOR_RESULT);
	}
	public static void showTweetPub(Activity context,String atme,int atuid)
	{
		Intent intent = new Intent(context, TweetPub.class);
		intent.putExtra("at_me", atme);
		intent.putExtra("at_uid", atuid);
		context.startActivityForResult(intent, REQUEST_CODE_FOR_RESULT);
	}
	
	/**
	 * ��ʾ��������
	 * @param context
	 * @param blogId
	 */
	public static void showBlogDetail(Context context, int blogId)
	{
		Intent intent = new Intent(context, BlogDetail.class);
		intent.putExtra("blog_id", blogId);
		context.startActivity(intent);
	}
	
	/**
	 * ��ʾ�������
	 * @param context
	 * @param ident
	 */
	public static void showSoftwareDetail(Context context, String ident)
	{
		Intent intent = new Intent(context, SoftwareDetail.class);
		intent.putExtra("ident", ident);
		context.startActivity(intent);
	}
	
	/**
	 * ���ų����ӵ����ת
	 * @param context
	 * @param newsId
	 * @param newsType
	 * @param objId
	 */
	public static void showNewsRedirect(Context context, News news)
	{
		String url = news.getUrl();
		//urlΪ��-�ɷ���
		if(StringUtils.isEmpty(url)) {
			int newsId = news.getId();
			int newsType = news.getNewType().type;
			String objId = news.getNewType().attachment;
			switch (newsType) {
				case News.NEWSTYPE_NEWS:
					showNewsDetail(context, newsId);
					break;
				case News.NEWSTYPE_SOFTWARE:
					showSoftwareDetail(context, objId);
					break;
				case News.NEWSTYPE_POST:
					showQuestionDetail(context, StringUtils.toInt(objId));
					break;
				case News.NEWSTYPE_BLOG:
					showBlogDetail(context, StringUtils.toInt(objId));
					break;
			}
		} else {
			showUrlRedirect(context, url);
		}
	}
	
	/**
	 * ��̬�����ת��������š����ӵ�
	 * @param context
	 * @param id
	 * @param catalog 0����  1����  2����  3����  4����  
	 */
	public static void showActiveRedirect(Context context, Active active)
	{
		String url = active.getUrl();
		//urlΪ��-�ɷ���
		if(StringUtils.isEmpty(url)) {
			int id = active.getObjectId();
			int catalog = active.getActiveType();
			switch (catalog) {
				case Active.CATALOG_OTHER:
					//����-����ת
					break;
				case Active.CATALOG_NEWS:
					showNewsDetail(context, id);
					break;
				case Active.CATALOG_POST:
					showQuestionDetail(context, id);
					break;
				case Active.CATALOG_TWEET:
					showTweetDetail(context, id);
					break;
				case Active.CATALOG_BLOG:
					showBlogDetail(context, id);
					break;
			}
		} else {
			showUrlRedirect(context, url);
		}
	}

	/**
	 * ��ʾ���۷���ҳ��
	 * @param context
	 * @param id ����|����|������id
	 * @param catalog 1���� 2���� 3���� 4��̬
	 */
	public static void showCommentPub(Activity context, int id, int catalog)
	{
		Intent intent = new Intent(context, CommentPub.class);
		intent.putExtra("id", id);
		intent.putExtra("catalog", catalog);
		context.startActivityForResult(intent, REQUEST_CODE_FOR_RESULT);
	}
	
	/**
	 * ��ʾ���ۻظ�ҳ��
	 * @param context
	 * @param id
	 * @param catalog
	 * @param replyid
	 * @param authorid
	 */
	public static void showCommentReply(Activity context, int id, int catalog, int replyid, int authorid, String author, String content)
	{
		Intent intent = new Intent(context, CommentPub.class);
		intent.putExtra("id", id);
		intent.putExtra("catalog", catalog);
		intent.putExtra("reply_id", replyid);
		intent.putExtra("author_id", authorid);
		intent.putExtra("author", author);
		intent.putExtra("content", content);
		if(catalog == CommentList.CATALOG_POST)
			context.startActivityForResult(intent, REQUEST_CODE_FOR_REPLY);
		else
			context.startActivityForResult(intent, REQUEST_CODE_FOR_RESULT);
	}
	
	/**
	 * ��ʾ���ԶԻ�ҳ��
	 * @param context
	 * @param catalog
	 * @param friendid
	 */
	public static void showMessageDetail(Context context, int friendid, String friendname)
	{
		Intent intent = new Intent(context, MessageDetail.class);
		intent.putExtra("friend_name", friendname);
		intent.putExtra("friend_id", friendid);
		context.startActivity(intent);
	}
	
	/**
	 * ��ʾ���Իظ�����
	 * @param context
	 * @param friendId �Է�id
	 * @param friendName �Է�����
	 */
	public static void showMessagePub(Activity context, int friendId, String friendName)
	{
    	Intent intent = new Intent();
		intent.putExtra("user_id", ((AppContext)context.getApplication()).getLoginUid());
		intent.putExtra("friend_id", friendId);
		intent.putExtra("friend_name", friendName);
		intent.setClass(context, MessagePub.class);
		context.startActivityForResult(intent, REQUEST_CODE_FOR_RESULT);
	}
	
	/**
	 * ��ʾת�����Խ���
	 * @param context
	 * @param friendName �Է�����
	 * @param messageContent ��������
	 */
	public static void showMessageForward(Activity context, String friendName, String messageContent)
	{
    	Intent intent = new Intent();
		intent.putExtra("user_id", ((AppContext)context.getApplication()).getLoginUid());
		intent.putExtra("friend_name", friendName);
		intent.putExtra("message_content", messageContent);
		intent.setClass(context, MessageForward.class);
		context.startActivity(intent);
	}
	
	/**
	 * ����ϵͳ��װ�˵�Ӧ�÷���
	 * @param context
	 * @param title
	 * @param url
	 */
	public static void showShareMore(Activity context,final String title,final String url)
	{
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_SUBJECT, "����" + title);
		intent.putExtra(Intent.EXTRA_TEXT, title + " " +url);
		context.startActivity(Intent.createChooser(intent, "ѡ�����"));
	}
	
	/**
	 * ����'����΢��'��'��Ѷ΢��'�ĶԻ���
	 * @param context ��ǰActivity
	 * @param title	����ı���
	 * @param url ���������
	 */
	public static void showShareDialog(final Activity context,final String title,final String url)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setIcon(android.R.drawable.btn_star);
		builder.setTitle(context.getString(R.string.share));
		builder.setItems(R.array.app_share_items,new DialogInterface.OnClickListener(){
			AppConfig cfgHelper = AppConfig.getAppConfig(context);
			AccessInfo access = cfgHelper.getAccessInfo();
			public void onClick(DialogInterface arg0, int arg1) {
				switch (arg1) {
					case 0://����΢��
						//���������
						final String shareMessage = title + " " +url;
						//��ʼ��΢��
						if(SinaWeiboHelper.isWeiboNull())
			    		{
			    			SinaWeiboHelper.initWeibo();
			    		}
						//�ж�֮ǰ�Ƿ��½��
				        if(access != null)
				        {   
				        	SinaWeiboHelper.progressDialog = new ProgressDialog(context); 
				        	SinaWeiboHelper.progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); 
				        	SinaWeiboHelper.progressDialog.setMessage(context.getString(R.string.sharing));
				        	SinaWeiboHelper.progressDialog.setCancelable(true);
				        	SinaWeiboHelper.progressDialog.show();
				        	new Thread()
				        	{
								public void run() 
								{						        	
									SinaWeiboHelper.setAccessToken(access.getAccessToken(), access.getAccessSecret(), access.getExpiresIn());
									SinaWeiboHelper.shareMessage(context, shareMessage);
				        		}
				        	}.start();
				        }
				        else
				        {
				        	SinaWeiboHelper.authorize(context, shareMessage);
				        }
						break;
					case 1://��Ѷ΢��
						QQWeiboHelper.shareToQQ(context, title, url);
						break;
					case 2://����
						showShareMore(context, title, url);
						break;
				}				
			}
		});
		builder.create().show();
	}
	
	/**
	 * �ղز���ѡ���
	 * @param context
	 * @param thread
	 */
	public static void showFavoriteOptionDialog(final Activity context,final Thread thread)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setIcon(R.drawable.ic_dialog_menu);
		builder.setTitle(context.getString(R.string.select));
		builder.setItems(R.array.favorite_options,new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface arg0, int arg1) {
				switch (arg1) {
					case 0://ɾ��
						thread.start();
						break;
				}				
			}
		});
		builder.create().show();
	}
	
	/**
	 * ��Ϣ�б����ѡ���
	 * @param context
	 * @param msg
	 * @param thread
	 */
	public static void showMessageListOptionDialog(final Activity context,final Messages msg,final Thread thread)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setIcon(R.drawable.ic_dialog_menu);
		builder.setTitle(context.getString(R.string.select));
		builder.setItems(R.array.message_list_options,new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface arg0, int arg1) {
				switch (arg1) {
					case 0://�ظ�
						showMessagePub(context,msg.getFriendId(),msg.getFriendName());
						break;
					case 1://ת��
						showMessageForward(context,msg.getFriendName(),msg.getContent());
						break;
					case 2://ɾ��
						thread.start();
						break;
				}				
			}
		});
		builder.create().show();
	}
	
	/**
	 * ��Ϣ�������ѡ���
	 * @param context
	 * @param msg
	 * @param thread
	 */
	public static void showMessageDetailOptionDialog(final Activity context,final Comment msg,final Thread thread)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setIcon(R.drawable.ic_dialog_menu);
		builder.setTitle(context.getString(R.string.select));
		builder.setItems(R.array.message_detail_options,new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface arg0, int arg1) {
				switch (arg1) {
					case 0://ת��
						showMessageForward(context,msg.getAuthor(),msg.getContent());
						break;
					case 1://ɾ��
						thread.start();
						break;
				}				
			}
		});
		builder.create().show();
	}
	
	/**
	 * ���۲���ѡ���
	 * @param context
	 * @param id ĳ�����ţ����ӣ�������id ����ĳ����Ϣ�� friendid
	 * @param catalog �������������ͣ�1����  2����  3����  4��̬
	 * @param comment �������۶������ڻ�ȡ����id&������authorid
	 * @param thread ����ɾ�����۵��̣߳�����ɾ��������null
	 */
	public static void showCommentOptionDialog(final Activity context,final int id,final int catalog,final Comment comment,final Thread thread)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setIcon(R.drawable.ic_dialog_menu);
		builder.setTitle(context.getString(R.string.select));
		if(thread != null)
		{
			builder.setItems(R.array.comment_options_2,new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface arg0, int arg1) {
					switch (arg1) {
						case 0://�ظ�
							showCommentReply(context,id,catalog,comment.getId(),comment.getAuthorId(),comment.getAuthor(),comment.getContent());
							break;
						case 1://ɾ��
							thread.start();
							break;
					}				
				}
			});
		}
		else
		{
			builder.setItems(R.array.comment_options_1,new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface arg0, int arg1) {
					switch (arg1) {
						case 0://�ظ�
							showCommentReply(context,id,catalog,comment.getId(),comment.getAuthorId(),comment.getAuthor(),comment.getContent());
							break;
					}				
				}
			});
		}
		builder.create().show();
	}
	
	/**
	 * �����б����
	 * @param context
	 * @param thread
	 */
	public static void showBlogOptionDialog(final Context context,final Thread thread)
	{
		new AlertDialog.Builder(context)
		.setIcon(android.R.drawable.ic_dialog_info)
		.setTitle(context.getString(R.string.delete_blog))
		.setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				if(thread != null)
					thread.start();
				else
					ToastMessage(context, R.string.msg_noaccess_delete);
				dialog.dismiss();
			}
		})
		.setNegativeButton(R.string.cancle, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		})
		.create().show();
	}
	
	/**
	 * ��������ѡ���
	 * @param context
	 * @param thread
	 */
	public static void showTweetOptionDialog(final Context context,final Thread thread)
	{
		new AlertDialog.Builder(context)
		.setIcon(android.R.drawable.ic_dialog_info)
		.setTitle(context.getString(R.string.delete_tweet))
		.setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				if(thread != null)
					thread.start();
				else
					ToastMessage(context, R.string.msg_noaccess_delete);
				dialog.dismiss();
			}
		})
		.setNegativeButton(R.string.cancle, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		})
		.create().show();
	}
	
	/**
	 * �Ƿ����·��������ٶԻ���
	 * @param context
	 * @param thread
	 */
	public static void showResendTweetDialog(final Context context,final Thread thread)
	{
		new AlertDialog.Builder(context)
		.setIcon(android.R.drawable.ic_dialog_info)
		.setTitle(context.getString(R.string.republish_tweet))
		.setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				if(context == TweetPub.mContext && TweetPub.mMessage != null)
					TweetPub.mMessage.setVisibility(View.VISIBLE);
				thread.start();
			}
		})
		.setNegativeButton(R.string.cancle, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		})
		.create().show();
	}
	
	/**
	 * ��ʾͼƬ�Ի���
	 * @param context
	 * @param imgUrl
	 */
	public static void showImageDialog(Context context, String imgUrl)
	{
		Intent intent = new Intent(context, ImageDialog.class);
		intent.putExtra("img_url", imgUrl);
		context.startActivity(intent);
	}
	public static void showImageZoomDialog(Context context, String imgUrl)
	{
		Intent intent = new Intent(context, ImageZoomDialog.class);
		intent.putExtra("img_url", imgUrl);
		context.startActivity(intent);
	}
	
	/**
	 * ��ʾϵͳ���ý���
	 * @param context
	 */
	public static void showSetting(Context context)
	{
		Intent intent = new Intent(context, Setting.class);
		context.startActivity(intent);
	}	
	

	

	
	/**
	 * ��ʾ�ҵ�����
	 * @param context
	 */
	public static void showUserInfo(Activity context)
	{
		AppContext ac = (AppContext)context.getApplicationContext();
		if(!ac.isLogin()){
			showLoginDialog(context);
		}else{
			Intent intent = new Intent(context, UserInfo.class);
			context.startActivity(intent);
		}
	}
	
	/**
	 * ��ʾ�û���̬
	 * @param context
	 * @param uid
	 * @param hisuid
	 * @param hisname
	 */
	public static void showUserCenter(Context context, int hisuid, String hisname)
	{
    	Intent intent = new Intent(context, UserCenter.class);
		intent.putExtra("his_id", hisuid);
		intent.putExtra("his_name", hisname);
		context.startActivity(intent);
	}
	
	/**
	 * ��ʾ�û��ղؼ�
	 * @param context
	 */
	public static void showUserFavorite(Context context)
	{
		Intent intent = new Intent(context, UserFavorite.class);
		context.startActivity(intent);
	}
	
	/**
	 * ��ʾ�û�����
	 * @param context
	 */
	public static void showUserFriend(Context context, int friendType, int followers, int fans)
	{
		Intent intent = new Intent(context, UserFriend.class);
		intent.putExtra("friend_type", friendType);
		intent.putExtra("friend_followers", followers);
		intent.putExtra("friend_fans", fans);
		context.startActivity(intent);
	}
	
	/**
	 * ������ʾ�û�ͷ��
	 * @param imgFace
	 * @param faceURL
	 */
	public static void showUserFace(final ImageView imgFace,final String faceURL)
	{
		showLoadImage(imgFace,faceURL,imgFace.getContext().getString(R.string.msg_load_userface_fail));
	}
	
	/**
	 * ������ʾͼƬ
	 * @param imgFace
	 * @param faceURL
	 * @param errMsg
	 */
	public static void showLoadImage(final ImageView imgView,final String imgURL,final String errMsg)
	{
		//��ȡ����ͼƬ
		if(StringUtils.isEmpty(imgURL) || imgURL.endsWith("portrait.gif")){
			Bitmap bmp = BitmapFactory.decodeResource(imgView.getResources(), R.drawable.widget_dface);
			imgView.setImageBitmap(bmp);
			return;
		}
		
		//�Ƿ��л���ͼƬ
    	final String filename = FileUtils.getFileName(imgURL);
    	//Environment.getExternalStorageDirectory();����/sdcard
    	String filepath = imgView.getContext().getFilesDir() + File.separator + filename;
		File file = new File(filepath);
		if(file.exists()){
			Bitmap bmp = ImageUtils.getBitmap(imgView.getContext(), filename);
			imgView.setImageBitmap(bmp);
			return;
    	}
		
		//�������ȡ&д��ͼƬ����
		String _errMsg = imgView.getContext().getString(R.string.msg_load_image_fail);
		if(!StringUtils.isEmpty(errMsg))
			_errMsg = errMsg;
		final String ErrMsg = _errMsg;
		final Handler handler = new Handler(){
			public void handleMessage(Message msg) {
				if(msg.what==1 && msg.obj != null){
					imgView.setImageBitmap((Bitmap)msg.obj);
					try {
                    	//дͼƬ����
						ImageUtils.saveImage(imgView.getContext(), filename, (Bitmap)msg.obj);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}else{
					ToastMessage(imgView.getContext(), ErrMsg);
				}
			}
		};
		new Thread(){
			public void run() {
				Message msg = new Message();
				try {
					Bitmap bmp = ApiClient.getNetBitmap(imgURL);
					msg.what = 1;
					msg.obj = bmp;
				} catch (AppException e) {
					e.printStackTrace();
	            	msg.what = -1;
	            	msg.obj = e;
				}
				handler.sendMessage(msg);
			}
		}.start();
	}
	
	/**
	 * url��ת
	 * @param context
	 * @param url
	 */
	public static void showUrlRedirect(Context context, String url){
		URLs urls = URLs.parseURL(url);
		if(urls != null){
			showLinkRedirect(context, urls.getObjType(), urls.getObjId(), urls.getObjKey());
		}else{
			openBrowser(context, url);
		}
	}
	
	public static void showLinkRedirect(Context context, int objType, int objId, String objKey){
		switch (objType) {
			case URLs.URL_OBJ_TYPE_NEWS:
				showNewsDetail(context, objId);
				break;
			case URLs.URL_OBJ_TYPE_QUESTION:
				showQuestionDetail(context, objId);
				break;
			case URLs.URL_OBJ_TYPE_QUESTION_TAG:
				showQuestionListByTag(context, objKey); 
				break;
			case URLs.URL_OBJ_TYPE_SOFTWARE:
				showSoftwareDetail(context, objKey);
				break;
			case URLs.URL_OBJ_TYPE_ZONE:
				showUserCenter(context, objId, objKey);
				break;
			case URLs.URL_OBJ_TYPE_TWEET:
				showTweetDetail(context, objId);
				break;
			case URLs.URL_OBJ_TYPE_BLOG:
				showBlogDetail(context, objId);
				break;
			case URLs.URL_OBJ_TYPE_OTHER:
				openBrowser(context, objKey);
				break;
		}
	}
	
	/**
	 * �������
	 * @param context
	 * @param url
	 */
	public static void openBrowser(Context context, String url){
		try {
			Uri uri = Uri.parse(url);  
			Intent it = new Intent(Intent.ACTION_VIEW, uri);  
			context.startActivity(it);
		} catch (Exception e) {
			e.printStackTrace();
			ToastMessage(context, "�޷��������ҳ", 500);
		} 
	}
		
	/**
	 * ��ȡwebviewClient����
	 * @return
	 */
	public static WebViewClient getWebViewClient(){
		return new WebViewClient(){
			@Override
			public boolean shouldOverrideUrlLoading(WebView view,String url) {
				showUrlRedirect(view.getContext(), url);
				return true;
			}
		};
	}
	
	/**
	 * ��ȡTextWatcher����
	 * @param context
	 * @param tmlKey
	 * @return
	 */
	public static TextWatcher getTextWatcher(final Activity context, final String temlKey) {
		return new TextWatcher() {		
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				//���浱ǰEditText���ڱ༭������
				((AppContext)context.getApplication()).setProperty(temlKey, s.toString());
			}		
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}		
			public void afterTextChanged(Editable s) {}
		};
	}
	
	/**
	 * �༭����ʾ����Ĳݸ�
	 * @param context
	 * @param editer
	 * @param temlKey
	 */
	public static void showTempEditContent(Activity context, EditText editer, String temlKey) {
		String tempContent = ((AppContext)context.getApplication()).getProperty(temlKey);
		if(!StringUtils.isEmpty(tempContent)) {
			SpannableStringBuilder builder = parseFaceByText(context, tempContent);
			editer.setText(builder);
			editer.setSelection(tempContent.length());//���ù��λ��
		}
	}
	
	/**
	 * ��[12]֮����ַ����滻Ϊ����
	 * @param context
	 * @param content
	 */
	public static SpannableStringBuilder parseFaceByText(Context context, String content) {
		SpannableStringBuilder builder = new SpannableStringBuilder(content);
		Matcher matcher = facePattern.matcher(content);
		while (matcher.find()) {
			//ʹ��������ʽ�ҳ����е�����
			int position = StringUtils.toInt(matcher.group(1));
			int resId = 0;
			try {
				if(position > 65 && position < 102)
					position = position-1;
				else if(position > 102)
					position = position-2;
				resId = GridViewFaceAdapter.getImageIds()[position];
				Drawable d = context.getResources().getDrawable(resId);
				d.setBounds(0, 0, 35, 35);//���ñ���ͼƬ����ʾ��С
				ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BOTTOM);
				builder.setSpan(span, matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			} catch (Exception e) {
			}
		}
		return builder;
	}
	
	/**
	 * �������
	 * @param cont
	 * @param editer
	 */
	public static void showClearWordsDialog(final Context cont, final EditText editer, final TextView numwords)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(cont);
		builder.setTitle(R.string.clearwords);
		builder.setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				//�������
				editer.setText("");
				numwords.setText("160");
			}
		});
		builder.setNegativeButton(R.string.cancle, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.show();
	}
	
	/**
	 * ����֪ͨ�㲥
	 * @param context
	 * @param notice
	 */
	public static void sendBroadCast(Context context, Notice notice){
		if(!((AppContext)context.getApplicationContext()).isLogin() || notice==null) return;
		Intent intent = new Intent("net.oschina.app.action.APPWIDGET_UPDATE"); 
		intent.putExtra("atmeCount", notice.getAtmeCount());
		intent.putExtra("msgCount", notice.getMsgCount());
		intent.putExtra("reviewCount", notice.getReviewCount());
		intent.putExtra("newFansCount", notice.getNewFansCount());
		context.sendBroadcast(intent);
	}
	
	/**
	 * ���͹㲥-��������
	 * @param context
	 * @param notice
	 */
	public static void sendBroadCastTweet(Context context, int what, Result res, Tweet tweet){
		if(res==null && tweet==null) return;
		Intent intent = new Intent("net.oschina.app.action.APP_TWEETPUB"); 
		intent.putExtra("MSG_WHAT", what);
		if(what == 1)
			intent.putExtra("RESULT", res);
		else
			intent.putExtra("TWEET", tweet);
		context.sendBroadcast(intent);
	}
	
	/**
	 * ��϶�̬�Ķ����ı�
	 * @param objecttype
	 * @param objectcatalog
	 * @param objecttitle
	 * @return
	 */
	public static SpannableString parseActiveAction(String author,int objecttype,int objectcatalog,String objecttitle){
		String title = "";
		int start = 0;
		int end = 0;
		if(objecttype==32 && objectcatalog==0){
			title = "�����˿�Դ�й�";
		}
		else if(objecttype==1 && objectcatalog==0){
			title = "����˿�Դ��Ŀ "+objecttitle;
		}
		else if(objecttype==2 && objectcatalog==1){
			title = "�����������ʣ�"+objecttitle;
		}
		else if(objecttype==2 && objectcatalog==2){
			title = "�������»��⣺"+objecttitle;
		}
		else if(objecttype==3 && objectcatalog==0){
			title = "�����˲��� "+objecttitle;
		}
		else if(objecttype==4 && objectcatalog==0){
			title = "����һƪ���� "+objecttitle;
		}
		else if(objecttype==5 && objectcatalog==0){
			title = "������һ�δ��� "+objecttitle;
		}
		else if(objecttype==6 && objectcatalog==0){
			title = "������һ��ְλ��"+objecttitle;
		}
		else if(objecttype==16 && objectcatalog==0){
			title = "������ "+objecttitle+" ��������";
		}
		else if(objecttype==17 && objectcatalog==1){
			title = "�ش������⣺"+objecttitle;
		}
		else if(objecttype==17 && objectcatalog==2){
			title = "�ظ��˻��⣺"+objecttitle;
		}
		else if(objecttype==17 && objectcatalog==3){
			title = "�� "+objecttitle+" �Ի�����������";
		}
		else if(objecttype==18 && objectcatalog==0){
			title = "�ڲ��� "+objecttitle+" ��������";
		}
		else if(objecttype==19 && objectcatalog==0){
			title = "�ڴ��� "+objecttitle+" ��������";
		}
		else if(objecttype==20 && objectcatalog==0){
			title = "��ְλ "+objecttitle+" ��������";
		}
		else if(objecttype==101 && objectcatalog==0){
			title = "�ظ��˶�̬��"+objecttitle;
		}
		else if(objecttype==100){
			title = "�����˶�̬";
		}
		title = author + " " + title;
		SpannableString sp = new SpannableString(title);
		//�����û��������С���Ӵ֡����� 
		sp.setSpan(new AbsoluteSizeSpan(14,true), 0, author.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		sp.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, author.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        sp.setSpan(new ForegroundColorSpan(Color.parseColor("#0e5986")), 0, author.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //���ñ��������С������ 
        if(!StringUtils.isEmpty(objecttitle)){
        	start = title.indexOf(objecttitle);
			if(objecttitle.length()>0 && start>0){
				end = start + objecttitle.length();  
				sp.setSpan(new AbsoluteSizeSpan(14,true), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		        sp.setSpan(new ForegroundColorSpan(Color.parseColor("#0e5986")), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
        }
        return sp;
	}
	
	/**
	 * ��϶�̬�Ļظ��ı�
	 * @param name
	 * @param body
	 * @return
	 */
	public static SpannableString parseActiveReply(String name,String body){
		SpannableString sp = new SpannableString(name+"��"+body);
		//�����û�������Ӵ֡����� 
		sp.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, name.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        sp.setSpan(new ForegroundColorSpan(Color.parseColor("#0e5986")), 0, name.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sp;
	}
	
	/**
	 * �����Ϣ�ı�
	 * @param name
	 * @param body
	 * @return
	 */
	public static SpannableString parseMessageSpan(String name,String body,String action){
		SpannableString sp = null;
		int start = 0;
		int end = 0;
		if(StringUtils.isEmpty(action)){
			sp = new SpannableString(name + "��" + body);
			end = name.length();
		}else{
			sp = new SpannableString(action + name + "��" + body);
			start = action.length();
			end = start + name.length();
		}
		//�����û�������Ӵ֡����� 
		sp.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		sp.setSpan(new ForegroundColorSpan(Color.parseColor("#0e5986")), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sp;
	}
	
	/**
	 * ��ϻظ������ı�
	 * @param name
	 * @param body
	 * @return
	 */
	public static SpannableString parseQuoteSpan(String name,String body){
		SpannableString sp = new SpannableString("�ظ���"+name+"\n"+body);
		//�����û�������Ӵ֡����� 
		sp.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 3, 3+name.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        sp.setSpan(new ForegroundColorSpan(Color.parseColor("#0e5986")), 3, 3+name.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sp;
	}
	
	/**
	 * ����Toast��Ϣ
	 * @param msg
	 */
	public static void ToastMessage(Context cont,String msg)
	{
		Toast.makeText(cont, msg, Toast.LENGTH_SHORT).show();
	}
	public static void ToastMessage(Context cont,int msg)
	{
		Toast.makeText(cont, msg, Toast.LENGTH_SHORT).show();
	}
	public static void ToastMessage(Context cont,String msg,int time)
	{
		Toast.makeText(cont, msg, time).show();
	}
	
	/**
	 * ������ؼ����¼�
	 * @param activity
	 * @return
	 */
	public static View.OnClickListener finish(final Activity activity)
	{
		return new View.OnClickListener() {
			public void onClick(View v) {
				activity.finish();
			}
		};
	}	
	
	/**
	 * ��ʾ��������
	 * @param context
	 */
	public static void showAbout(Context context)
	{
		Intent intent = new Intent(context, About.class);
		context.startActivity(intent);
	}
	
	/**
	 * ��ʾ�û�����
	 * @param context
	 */
	public static void showFeedBack(Context context)
	{
		Intent intent = new Intent(context, FeedBack.class);
		context.startActivity(intent);
	}
	
	/**
	 * �˵���ʾ��¼��ǳ�
	 * @param activity
	 * @param menu
	 */
	public static void showMenuLoginOrLogout(Activity activity,Menu menu)
	{
		if(((AppContext)activity.getApplication()).isLogin()){
			menu.findItem(R.id.main_menu_user).setTitle(R.string.main_menu_logout);
			menu.findItem(R.id.main_menu_user).setIcon(R.drawable.ic_menu_logout);
		}else{
			menu.findItem(R.id.main_menu_user).setTitle(R.string.main_menu_login);
			menu.findItem(R.id.main_menu_user).setIcon(R.drawable.ic_menu_login);
		}
	}
	
	/**
	 * �������ʾ��¼��ǳ�
	 * @param activity
	 * @param qa
	 */
	public static void showSettingLoginOrLogout(Activity activity,QuickAction qa)
	{
		if(((AppContext)activity.getApplication()).isLogin()){
			qa.setIcon(MyQuickAction.buildDrawable(activity, R.drawable.ic_menu_logout));
			qa.setTitle(activity.getString(R.string.main_menu_logout));
		}else{
			qa.setIcon(MyQuickAction.buildDrawable(activity, R.drawable.ic_menu_login));
			qa.setTitle(activity.getString(R.string.main_menu_login));
		}
	}
	
	/**
	 * ������Ƿ���ʾ����ͼƬ
	 * @param activity
	 * @param qa
	 */
	public static void showSettingIsLoadImage(Activity activity,QuickAction qa)
	{
		if(((AppContext)activity.getApplication()).isLoadImage()){
			qa.setIcon(MyQuickAction.buildDrawable(activity, R.drawable.ic_menu_picnoshow));
			qa.setTitle(activity.getString(R.string.main_menu_picnoshow));
		}else{
			qa.setIcon(MyQuickAction.buildDrawable(activity, R.drawable.ic_menu_picshow));
			qa.setTitle(activity.getString(R.string.main_menu_picshow));
		}
	}
	
	/**
	 * �û���¼��ע��
	 * @param activity
	 */
	public static void loginOrLogout(Activity activity)
	{
		AppContext ac = (AppContext)activity.getApplication();
		if(ac.isLogin()){
			ac.Logout();
			ToastMessage(activity, "���˳���¼");
		}else{
			showLoginDialog(activity);
		}
	}
	
	/**
	 * �����Ƿ����ͼƬ��ʾ
	 * @param activity
	 */
	public static void changeSettingIsLoadImage(Activity activity)
	{
		AppContext ac = (AppContext)activity.getApplication();
		if(ac.isLoadImage()){
			ac.setConfigLoadimage(false);
			ToastMessage(activity, "���������²�����ͼƬ");
		}else{
			ac.setConfigLoadimage(true);
			ToastMessage(activity, "���������¼���ͼƬ");
		}
	}
	public static void changeSettingIsLoadImage(Activity activity,boolean b)
	{
		AppContext ac = (AppContext)activity.getApplication();
		ac.setConfigLoadimage(b);
	}
	
	/**
	 * ���app����
	 * @param activity
	 */
	public static void clearAppCache(Activity activity)
	{
		final AppContext ac = (AppContext)activity.getApplication();
		final Handler handler = new Handler(){
			public void handleMessage(Message msg) {
				if(msg.what==1){
					ToastMessage(ac, "��������ɹ�");
				}else{
					ToastMessage(ac, "�������ʧ��");
				}
			}
		};
		new Thread(){
			public void run() {
				Message msg = new Message();
				try {				
					ac.clearAppCache();
					msg.what = 1;
				} catch (Exception e) {
					e.printStackTrace();
	            	msg.what = -1;
				}
				handler.sendMessage(msg);
			}
		}.start();
	}
	
	/**
	 * ����App�쳣��������
	 * @param cont
	 * @param crashReport
	 */
	public static void sendAppCrashReport(final Context cont, final String crashReport)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(cont);
		builder.setIcon(android.R.drawable.ic_dialog_info);
		builder.setTitle(R.string.app_error);
		builder.setMessage(R.string.app_error_message);
		builder.setPositiveButton(R.string.submit_report, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				//�����쳣����
				Intent i = new Intent(Intent.ACTION_SEND);
				//i.setType("text/plain"); //ģ����
				i.setType("message/rfc822") ; //���
				i.putExtra(Intent.EXTRA_EMAIL, new String[]{"jxsmallmouse@163.com"});
				i.putExtra(Intent.EXTRA_SUBJECT,"��Դ�й�Android�ͻ��� - ���󱨸�");
				i.putExtra(Intent.EXTRA_TEXT,crashReport);
				cont.startActivity(Intent.createChooser(i, "���ʹ��󱨸�"));
				//�˳�
				AppManager.getAppManager().AppExit(cont);
			}
		});
		builder.setNegativeButton(R.string.sure, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				//�˳�
				AppManager.getAppManager().AppExit(cont);
			}
		});
		builder.show();
	}
	
	/**
	 * �˳�����
	 * @param cont
	 */
	public static void Exit(final Context cont)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(cont);
		builder.setIcon(android.R.drawable.ic_dialog_info);
		builder.setTitle(R.string.app_menu_surelogout);
		builder.setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				//�˳�
				AppManager.getAppManager().AppExit(cont);
			}
		});
		builder.setNegativeButton(R.string.cancle, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.show();
	}
}

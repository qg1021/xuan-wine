package com.gm.wine.app.adapter;

import java.util.List;

import com.gm.wine.app.AppContext;
import com.gm.wine.app.R;
import com.gm.wine.app.common.StringUtils;
import com.gm.wine.app.widget.LinkView;
import com.gm.wine.vo.NoticeVO;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 用户留言详情Adapter类
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-3-21
 */
public class ListViewMessageDetailAdapter extends BaseAdapter {
	private Context 					context;//运行上下文
	private List<NoticeVO> 				listItems;//数据集合
	private LayoutInflater 				listContainer;//视图容器
	private int 						itemViewResource;//自定义项视图源
	static class ListItemView{				//自定义控件集合  

			public LinkView username;  
		    public TextView date;  
		    public LinearLayout contentll;
	 }  

	/**
	 * 实例化Adapter
	 * @param context
	 * @param data
	 * @param resource
	 */
	public ListViewMessageDetailAdapter(Context context, List<NoticeVO> data,int resource) {
		this.context = context;			
		this.listContainer = LayoutInflater.from(context);	//创建视图容器并设置上下文
		this.itemViewResource = resource;
		this.listItems = data;
	}
	
	public int getCount() {
		return listItems.size();
	}

	public Object getItem(int arg0) {
		return null;
	}

	public long getItemId(int arg0) {
		return 0;
	}
	   
	/**
	 * ListView Item设置
	 */
	public View getView(int position, View convertView, ViewGroup parent) {
		//Log.d("method", "getView");
		
		//自定义视图
		ListItemView  listItemView = null;
		
		if (convertView == null) {
			//获取list_item布局文件的视图
			convertView = listContainer.inflate(this.itemViewResource, null);
			
			listItemView = new ListItemView();
			//获取控件对象
			listItemView.username = (LinkView)convertView.findViewById(R.id.messagedetail_listitem_username);
			listItemView.date = (TextView)convertView.findViewById(R.id.messagedetail_listitem_date);
			listItemView.contentll = (LinearLayout)convertView.findViewById(R.id.messagedetail_listitem_contentll);
		
			
			//设置控件集到convertView
			convertView.setTag(listItemView);
		}else {
			listItemView = (ListItemView)convertView.getTag();
		}
		
		//设置文字和图片
		NoticeVO msg = listItems.get(position);
		listItemView.username.setLinkText("<font color='#0e5986'><b>" + msg.getUser().getName() + "</b></font>：" + msg.getContent());
		//listItemView.username.setText(UIHelper.parseMessageSpan(msg.getAuthor(), msg.getContent(), ""));
		//listItemView.username.parseLinkText();
		listItemView.username.setTag(msg);//设置隐藏参数(实体类)
		listItemView.date.setText(StringUtils.friendly_time(msg.getCreatedate()));;
		listItemView.contentll.setBackgroundResource(R.drawable.review_bg_left);

		return convertView;
	}
	

    
}
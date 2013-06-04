package com.gm.wine.app.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gm.wine.app.R;
import com.gm.wine.app.common.StringUtils;
import com.gm.wine.app.common.UIHelper;
import com.gm.wine.vo.NoticeVO;

/**
 * 用户留言Adapter类
 * 
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-3-21
 */
public class ListViewMessageAdapter extends BaseAdapter {
	private final List<NoticeVO> listItems;// 数据集合
	private final LayoutInflater listContainer;// 视图容器
	private final int itemViewResource;// 自定义项视图源
	static class ListItemView { // 自定义控件集合
		public TextView username;
		public TextView date;
		public TextView messageCount;
	}

	/**
	 * 实例化Adapter
	 * 
	 * @param context
	 * @param data
	 * @param resource
	 */
	public ListViewMessageAdapter(Context context, List<NoticeVO> data,
			int resource) {
		this.listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
		this.itemViewResource = resource;
		this.listItems = data;
	}

	@Override
	public int getCount() {
		return listItems.size();
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	/**
	 * ListView Item设置
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// Log.d("method", "getView");

		// 自定义视图
		ListItemView listItemView = null;

		if (convertView == null) {
			// 获取list_item布局文件的视图
			convertView = listContainer.inflate(this.itemViewResource, null);

			listItemView = new ListItemView();
			// 获取控件对象
			listItemView.username = (TextView) convertView
					.findViewById(R.id.message_listitem_username);
			listItemView.date = (TextView) convertView
					.findViewById(R.id.message_listitem_date);
			listItemView.messageCount = (TextView) convertView
					.findViewById(R.id.message_listitem_messageCount);

			// 设置控件集到convertView
			convertView.setTag(listItemView);
		} else {
			listItemView = (ListItemView) convertView.getTag();
		}

		// 设置文字和图片
		NoticeVO msg = listItems.get(position);
		listItemView.username.setText(UIHelper.parseMessageSpan(msg.getUser()
				.getName(), msg.getTitle(), ""));
		listItemView.username.setTag(msg);// 设置隐藏参数(实体类)
		listItemView.date
				.setText(StringUtils.friendly_time(msg.getCreatedate()));
		listItemView.messageCount.setText("共有 " + msg.getReplyNum()
				+ " 条回复");

		return convertView;
	}

}
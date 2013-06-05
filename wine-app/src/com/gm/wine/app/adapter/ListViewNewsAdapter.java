package com.gm.wine.app.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gm.wine.app.R;
import com.gm.wine.app.common.StringUtils;
import com.gm.wine.vo.NewsVO;

public class ListViewNewsAdapter extends BaseAdapter {
	private final Context context;
	private final List<NewsVO> listItems;
	private final LayoutInflater listContainer;
	private final int itemViewResource;
	static class ListItemView {
		public TextView title;
		public TextView source;
		public TextView date;
		public ImageView flag;
	}


	public ListViewNewsAdapter(Context context, List<NewsVO> data, int resource) {
		this.context = context;
		this.listContainer = LayoutInflater.from(context);
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
	 * ListView Item����
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// Log.d("method", "getView");

		ListItemView listItemView = null;

		if (convertView == null) {

			convertView = listContainer.inflate(this.itemViewResource, null);

			listItemView = new ListItemView();

			listItemView.title = (TextView) convertView
					.findViewById(R.id.news_listitem_title);
			listItemView.source = (TextView) convertView
					.findViewById(R.id.news_listitem_source);
			listItemView.date = (TextView) convertView
					.findViewById(R.id.news_listitem_date);
			listItemView.flag = (ImageView) convertView
					.findViewById(R.id.news_listitem_flag);

			convertView.setTag(listItemView);
		} else {
			listItemView = (ListItemView) convertView.getTag();
		}

		NewsVO news = listItems.get(position);

		listItemView.title.setText(news.getTitle());
		listItemView.title.setTag(news);
		listItemView.source.setText(news.getSource());
		listItemView.date.setText(StringUtils.friendly_time(news
				.getPublishdate()));
		if (StringUtils.isToday(news.getPublishdate())) {
			listItemView.flag.setVisibility(View.VISIBLE);
		} else {
			listItemView.flag.setVisibility(View.GONE);
		}

		return convertView;
	}
}

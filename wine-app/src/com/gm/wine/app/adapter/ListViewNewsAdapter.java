package com.gm.wine.app.adapter;

import java.util.List;

import com.gm.wine.app.R;
import com.gm.wine.app.common.StringUtils;
import com.gm.wine.vo.NewsVO;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListViewNewsAdapter extends BaseAdapter {
	private Context 					context;//����������
	private List<NewsVO> 					listItems;//���ݼ���
	private LayoutInflater 				listContainer;//��ͼ����
	private int 						itemViewResource;//�Զ�������ͼԴ 
	static class ListItemView{				//�Զ���ؼ�����  
	        public TextView title;  
		    public TextView source;
		    public TextView date;  
		    public ImageView flag;
	 }  

	/**
	 * ʵ����Adapter
	 * @param context
	 * @param data
	 * @param resource
	 */
	public ListViewNewsAdapter(Context context, List<NewsVO> data,int resource) {
		this.context = context;			
		this.listContainer = LayoutInflater.from(context);	//������ͼ����������������
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
	 * ListView Item����
	 */
	public View getView(int position, View convertView, ViewGroup parent) {
		//Log.d("method", "getView");
		
		//�Զ�����ͼ
		ListItemView  listItemView = null;
		
		if (convertView == null) {
			//��ȡlist_item�����ļ�����ͼ
			convertView = listContainer.inflate(this.itemViewResource, null);
			
			listItemView = new ListItemView();
			//��ȡ�ؼ�����
			listItemView.title = (TextView)convertView.findViewById(R.id.news_listitem_title);
			listItemView.source = (TextView)convertView.findViewById(R.id.news_listitem_source);
			listItemView.date= (TextView)convertView.findViewById(R.id.news_listitem_date);
			listItemView.flag= (ImageView)convertView.findViewById(R.id.news_listitem_flag);
			
			//���ÿؼ�����convertView
			convertView.setTag(listItemView);
		}else {
			listItemView = (ListItemView)convertView.getTag();
		}	
		
		//�������ֺ�ͼƬ
		NewsVO news = listItems.get(position);
		
		listItemView.title.setText(news.getTitle());
		listItemView.title.setTag(news);//�������ز���(ʵ����)
		listItemView.source.setText(news.getSource());
		listItemView.date.setText(StringUtils.friendly_time(news.getPublishdate()));
		if(StringUtils.isToday(news.getPublishdate()))
			listItemView.flag.setVisibility(View.VISIBLE);
		else
			listItemView.flag.setVisibility(View.GONE);
		
		return convertView;
	}
}

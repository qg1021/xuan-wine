package com.gm.wine.app.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gm.wine.app.R;
import com.gm.wine.app.common.BitmapManager;
import com.gm.wine.app.common.StringUtils;
import com.gm.wine.vo.ProductVO;

/**
 * 产品Adapter类
 * 
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-3-21
 */
public class ListViewProductAdapter extends BaseAdapter {
	private final List<ProductVO> listItems;// 数据集合
	private final LayoutInflater listContainer;// 视图容器
	private final int itemViewResource;// 自定义项视图源
	private final BitmapManager bmpManager;
	static class ListItemView { // 自定义控件集合
		public ImageView pic;
		public TextView name;
		public TextView date;
		public TextView price;
	}

	/**
	 * 实例化Adapter
	 * 
	 * @param context
	 * @param data
	 * @param resource
	 */
	public ListViewProductAdapter(Context context, List<ProductVO> data,
			int resource) {
		this.listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
		this.itemViewResource = resource;
		this.listItems = data;
		this.bmpManager = new BitmapManager(BitmapFactory.decodeResource(
				context.getResources(), R.drawable.widget_dface_loading));
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
			listItemView.pic = (ImageView) convertView
					.findViewById(R.id.question_listitem_pic);
			listItemView.name = (TextView) convertView
					.findViewById(R.id.question_listitem_name);
			listItemView.price = (TextView) convertView
					.findViewById(R.id.question_listitem_price);
			listItemView.date = (TextView) convertView
					.findViewById(R.id.question_listitem_date);

			// 设置控件集到convertView
			convertView.setTag(listItemView);
		} else {
			listItemView = (ListItemView) convertView.getTag();
		}

		// 设置文字和图片
		ProductVO post = listItems.get(position);
		String faceURL = post.getPicurl();
		if (faceURL.endsWith("portrait.gif") || StringUtils.isEmpty(faceURL)) {
			listItemView.pic.setImageResource(R.drawable.widget_dface);
		} else {
			bmpManager.loadBitmap(faceURL, listItemView.pic);
		}
		// listItemView.face.setOnClickListener(faceClickListener);
		listItemView.pic.setTag(post);

		listItemView.name.setText(post.getName());
		listItemView.name.setTag(post);// 设置隐藏参数(实体类)
		listItemView.date.setText(StringUtils.friendly_time(post.getPubdate()));
		listItemView.price.setText("价格：" + post.getPrice());

		return convertView;
	}

	// private View.OnClickListener faceClickListener = new
	// View.OnClickListener(){
	// public void onClick(View v) {
	// Post post = (Post)v.getTag();
	// UIHelper.showUserCenter(v.getContext(), post.getAuthorId(),
	// post.getAuthor());
	// }
	// };
}
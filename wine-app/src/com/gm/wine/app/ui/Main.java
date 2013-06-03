package com.gm.wine.app.ui;

import greendroid.widget.MyQuickAction;
import greendroid.widget.QuickActionGrid;
import greendroid.widget.QuickActionWidget;
import greendroid.widget.QuickActionWidget.OnQuickActionClickListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import com.gm.wine.app.AppContext;
import com.gm.wine.app.AppException;
import com.gm.wine.app.R;
import com.gm.wine.app.adapter.ListViewMessageAdapter;
import com.gm.wine.app.adapter.ListViewNewsAdapter;
import com.gm.wine.app.adapter.ListViewProductAdapter;
import com.gm.wine.app.common.StringUtils;
import com.gm.wine.app.common.UIHelper;
import com.gm.wine.app.widget.NewDataToast;
import com.gm.wine.app.widget.PullToRefreshListView;
import com.gm.wine.app.widget.ScrollLayout;
import com.gm.wine.vo.NewsList;
import com.gm.wine.vo.NewsVO;
import com.gm.wine.vo.NoticeVO;
import com.gm.wine.vo.ProductList;
import com.gm.wine.vo.ProductVO;

/**
 * 应用程序首页
 * 
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-3-21
 */
public class Main extends BaseActivity {
	public static final int QUICKACTION_LOGIN_OR_LOGOUT = 0;
	public static final int QUICKACTION_USERINFO = 1;
	public static final int QUICKACTION_SETTING = 2;
	public static final int QUICKACTION_EXIT = 3;

	private AppContext appContext; // 全局Context

	private ScrollLayout mScrollLayout;
	private int mViewCount;
	private int mCurSel;

	private ImageView mHeadLogo;
	private TextView mHeadTitle;
	private ProgressBar mHeadProgress;
	private ImageButton mHeadPub_post;

	private RadioButton fbNews; // 新闻资讯
	private RadioButton fbProduct; // 产品中心
	private RadioButton fbNotice; // 留言公告
	private ImageView fbSetting;

	private ListViewNewsAdapter lvNewsAdapter;
	private ListViewProductAdapter lvProductAdapter;
	private ListViewMessageAdapter lvMessageAdapter;

	private PullToRefreshListView lvNews;
	private PullToRefreshListView lvProduct;
	private PullToRefreshListView lvMessage;

	private final List<NewsVO> lvNewsData = new ArrayList<NewsVO>();
	private final List<ProductVO> lvProductData = new ArrayList<ProductVO>();
	private final List<NoticeVO> lvMessageData = new ArrayList<NoticeVO>();

	private View lvNews_footer;
	private View lvProduct_footer;
	private View lvMessage_footer;

	private TextView lvNews_foot_more;
	private TextView lvProduct_foot_more;
	private TextView lvMessage_foot_more;

	private ProgressBar lvNews_foot_progress;
	private ProgressBar lvProduct_foot_progress;
	private ProgressBar lvMessage_foot_progress;

	private int lvNewsSumData;
	private int lvProductSumData;
	private int lvMessageSumData;

	private Handler lvNewsHandler;
	private Handler lvProductHandler;
	private Handler lvMessageHandler;

	private QuickActionWidget mGrid; // 快捷栏控件

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		appContext = (AppContext) getApplication();
		// 网络连接判断
		if (!appContext.isNetworkConnected()) {
			UIHelper.ToastMessage(this, R.string.network_not_connected);
		}
		this.initHeadView();
		this.initFootBar();
		this.initQuickActionGrid();
		this.initFrameListView();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}
	@Override
	protected void onResume() {
		super.onResume();
		if (mViewCount == 0) {
			mViewCount = 4;
		}
		if (mCurSel == 0 && !fbNews.isChecked()) {
			fbNews.setChecked(true);
			fbProduct.setChecked(false);
			fbNotice.setChecked(false);
		}
		// 读取左右滑动配置
		mScrollLayout.setIsScroll(appContext.isScroll());
	}

	/**
	 * 初始化新闻列表
	 */
	private void initNewsListView() {
		lvNewsAdapter = new ListViewNewsAdapter(this, lvNewsData,
				R.layout.news_listitem);
		lvNews_footer = getLayoutInflater().inflate(R.layout.listview_footer,
				null);
		lvNews_foot_more = (TextView) lvNews_footer
				.findViewById(R.id.listview_foot_more);
		lvNews_foot_progress = (ProgressBar) lvNews_footer
				.findViewById(R.id.listview_foot_progress);
		lvNews = (PullToRefreshListView) findViewById(R.id.frame_listview_news);
		lvNews.addFooterView(lvNews_footer);// 添加底部视图 必须在setAdapter前
		lvNews.setAdapter(lvNewsAdapter);
		lvNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 点击头部、底部栏无效
				if (position == 0 || view == lvNews_footer) {
					return;
				}

				NewsVO news = null;
				// 判断是否是TextView
				if (view instanceof TextView) {
					news = (NewsVO) view.getTag();
				} else {
					TextView tv = (TextView) view
							.findViewById(R.id.news_listitem_title);
					news = (NewsVO) tv.getTag();
				}
				if (news == null) {
					return;
				}

				// ��ת����������
				// UIHelper.showNewsRedirect(view.getContext(), news);
			}
		});
		lvNews.setOnScrollListener(new AbsListView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				lvNews.onScrollStateChanged(view, scrollState);

				// 数据为空--不用继续下面代码了
				if (lvNewsData.isEmpty()) {
					return;
				}

				// 判断是否滚动到底部
				boolean scrollEnd = false;
				try {
					if (view.getPositionForView(lvNews_footer) == view
							.getLastVisiblePosition()) {
						scrollEnd = true;
					}
				} catch (Exception e) {
					scrollEnd = false;
				}

				int lvDataState = StringUtils.toInt(lvNews.getTag());
				if (scrollEnd && lvDataState == UIHelper.LISTVIEW_DATA_MORE) {
					lvNews.setTag(UIHelper.LISTVIEW_DATA_LOADING);
					lvNews_foot_more.setText(R.string.load_ing);
					lvNews_foot_progress.setVisibility(View.VISIBLE);
					// 当前pageIndex
					int pageIndex = lvNewsSumData / AppContext.PAGE_SIZE;
					loadLvNewsData(pageIndex, lvNewsHandler,
							UIHelper.LISTVIEW_ACTION_SCROLL);
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				lvNews.onScroll(view, firstVisibleItem, visibleItemCount,
						totalItemCount);
			}
		});
		lvNews.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
			@Override
			public void onRefresh() {
				loadLvNewsData(0, lvNewsHandler,
						UIHelper.LISTVIEW_ACTION_REFRESH);
			}
		});
	}
	/**
	 * 初始化产品列表
	 */
	private void initProductsListView() {
		lvProductAdapter = new ListViewProductAdapter(this, lvProductData,
				R.layout.question_listitem);
		lvProduct_footer = getLayoutInflater().inflate(
				R.layout.listview_footer, null);
		lvProduct_foot_more = (TextView) lvNews_footer
				.findViewById(R.id.listview_foot_more);
		lvProduct_foot_progress = (ProgressBar) lvNews_footer
				.findViewById(R.id.listview_foot_progress);
		lvProduct = (PullToRefreshListView) findViewById(R.id.frame_listview_question);
		lvProduct.addFooterView(lvProduct_footer);// 添加底部视图 必须在setAdapter前
		lvProduct.setAdapter(lvProductAdapter);
		lvProduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 点击头部、底部栏无效
				if (position == 0 || view == lvProduct_footer) {
					return;
				}

				ProductVO p = null;
				// 判断是否是TextView
				if (view instanceof TextView) {
					p = (ProductVO) view.getTag();
				} else {
					TextView tv = (TextView) view
							.findViewById(R.id.question_listitem_name);
					p = (ProductVO) tv.getTag();
				}
				if (p == null) {
					return;
				}

				// 跳转到问答详情
				// UIHelper.showQuestionDetail(view.getContext(), post.getId());
			}
		});
		lvProduct.setOnScrollListener(new AbsListView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				lvProduct.onScrollStateChanged(view, scrollState);

				// 数据为空--不用继续下面代码了
				if (lvProductData.isEmpty()) {
					return;
				}

				// 判断是否滚动到底部
				boolean scrollEnd = false;
				try {
					if (view.getPositionForView(lvProduct_footer) == view
							.getLastVisiblePosition()) {
						scrollEnd = true;
					}
				} catch (Exception e) {
					scrollEnd = false;
				}

				int lvDataState = StringUtils.toInt(lvProduct.getTag());
				if (scrollEnd && lvDataState == UIHelper.LISTVIEW_DATA_MORE) {
					lvProduct.setTag(UIHelper.LISTVIEW_DATA_LOADING);
					lvProduct_foot_more.setText(R.string.load_ing);
					lvProduct_foot_progress.setVisibility(View.VISIBLE);
					// 当前pageIndex
					int pageIndex = lvProductSumData / AppContext.PAGE_SIZE_10;
					loadLvProductData(pageIndex, lvProductHandler,
							UIHelper.LISTVIEW_ACTION_SCROLL);
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				lvProduct.onScroll(view, firstVisibleItem, visibleItemCount,
						totalItemCount);
			}
		});
		lvProduct
				.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
					@Override
					public void onRefresh() {
						loadLvProductData(0, lvProductHandler,
								UIHelper.LISTVIEW_ACTION_REFRESH);
					}
				});
	}

	/**
	 * 
	 * 线程加载新闻列表信息
	 * 
	 * @since 2013-6-3
	 * @author qingang
	 * @param pageIndex
	 * @param handler
	 * @param action
	 */
	private void loadLvNewsData(final int pageIndex, final Handler handler,
			final int action) {
		mHeadProgress.setVisibility(ProgressBar.VISIBLE);
		new Thread() {
			@Override
			public void run() {
				Message msg = new Message();
				boolean isRefresh = false;
				if (action == UIHelper.LISTVIEW_ACTION_REFRESH
						|| action == UIHelper.LISTVIEW_ACTION_SCROLL) {
					isRefresh = true;
				}
				try {
					NewsList list = appContext
							.getNewsList(pageIndex, isRefresh);
					msg.what = list.getPageSize();
					msg.obj = list;
				} catch (AppException e) {
					e.printStackTrace();
					msg.what = -1;
					msg.obj = e;
				}
				msg.arg1 = action;
				msg.arg2 = UIHelper.LISTVIEW_DATATYPE_NEWS;
				handler.sendMessage(msg);
			}
		}.start();
	}
	/**
	 * 
	 * 线程加载产品信息
	 * 
	 * @since 2013-6-3
	 * @author qingang
	 * @param pageIndex
	 * @param handler
	 * @param action
	 */
	private void loadLvProductData(final int pageIndex, final Handler handler,
			final int action) {
		mHeadProgress.setVisibility(ProgressBar.VISIBLE);
		new Thread() {
			@Override
			public void run() {
				Message msg = new Message();
				boolean isRefresh = false;
				if (action == UIHelper.LISTVIEW_ACTION_REFRESH
						|| action == UIHelper.LISTVIEW_ACTION_SCROLL) {
					isRefresh = true;
				}
				try {
					ProductList list = appContext.getProductList(pageIndex,
							isRefresh);
					msg.what = list.getPageSize();
					msg.obj = list;
				} catch (AppException e) {
					e.printStackTrace();
					msg.what = -1;
					msg.obj = e;
				}
				msg.arg1 = action;
				msg.arg2 = UIHelper.LISTVIEW_DATATYPE_NEWS;
				handler.sendMessage(msg);
			}
		}.start();
	}

	/**
	 * 
	 * 初始化头部控件
	 * 
	 * @since 2013-6-3
	 * @author qingang
	 */
	private void initHeadView() {
		mHeadLogo = (ImageView) findViewById(R.id.main_head_logo);
		mHeadTitle = (TextView) findViewById(R.id.main_head_title);
		mHeadProgress = (ProgressBar) findViewById(R.id.main_head_progress);
		mHeadPub_post = (ImageButton) findViewById(R.id.main_head_pub_post);

		mHeadPub_post.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// UIHelper.showQuestionPub(v.getContext());
			}
		});

	}

	/**
	 * ��ʼ���ײ���
	 */
	private void initFootBar() {
		fbNews = (RadioButton) findViewById(R.id.main_footbar_news);
		fbProduct = (RadioButton) findViewById(R.id.main_footbar_product);
		fbNotice = (RadioButton) findViewById(R.id.main_footbar_notice);
		fbSetting = (ImageView) findViewById(R.id.main_footbar_setting);
		fbSetting.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// չʾ�����&�ж��Ƿ��¼&�Ƿ��������ͼƬ
				UIHelper.showSettingLoginOrLogout(Main.this,
						mGrid.getQuickAction(0));
				mGrid.show(v);
			}
		});
	}

	/**
	 * ��ʼ�������
	 */
	private void initQuickActionGrid() {
		mGrid = new QuickActionGrid(this);
		mGrid.addQuickAction(new MyQuickAction(this, R.drawable.ic_menu_login,
				R.string.main_menu_login));
		mGrid.addQuickAction(new MyQuickAction(this, R.drawable.ic_menu_myinfo,
				R.string.main_menu_myinfo));
		mGrid.addQuickAction(new MyQuickAction(this,
				R.drawable.ic_menu_setting, R.string.main_menu_setting));
		mGrid.addQuickAction(new MyQuickAction(this, R.drawable.ic_menu_exit,
				R.string.main_menu_exit));

		mGrid.setOnQuickActionClickListener(mActionListener);
	}

	/**
	 * �����item����¼�
	 */
	private final OnQuickActionClickListener mActionListener = new OnQuickActionClickListener() {
		@Override
		public void onQuickActionClicked(QuickActionWidget widget, int position) {
			switch (position) {
				case QUICKACTION_LOGIN_OR_LOGOUT :// �û���¼-ע��
					UIHelper.loginOrLogout(Main.this);
					break;
				case QUICKACTION_USERINFO :// �ҵ�����
					UIHelper.showUserInfo(Main.this);
					break;
				case QUICKACTION_SETTING :// ����
					UIHelper.showSetting(Main.this);
					break;
				case QUICKACTION_EXIT :// �˳�
					UIHelper.Exit(Main.this);
					break;
			}
		}
	};

	/**
	 * ��ʼ������ListView
	 */
	private void initFrameListView() {
		// ��ʼ��listview�ؼ�
		this.initNewsListView();
		// ����listview���
		this.initFrameListViewData();
	}

	/**
	 * ��ʼ������ListView���
	 */
	private void initFrameListViewData() {
		// ��ʼ��Handler
		lvNewsHandler = this.getLvHandler(lvNews, lvNewsAdapter,
				lvNews_foot_more, lvNews_foot_progress, AppContext.PAGE_SIZE);

		// ������Ѷ���
		if (lvNewsData.isEmpty()) {
			loadLvNewsData(0, lvNewsHandler, UIHelper.LISTVIEW_ACTION_INIT);
		}
	}

	/**
	 * ��ȡlistview�ĳ�ʼ��Handler
	 * 
	 * @param lv
	 * @param adapter
	 * @return
	 */
	private Handler getLvHandler(final PullToRefreshListView lv,
			final BaseAdapter adapter, final TextView more,
			final ProgressBar progress, final int pageSize) {
		return new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what >= 0) {
					// listview��ݴ���
					handleLvData(msg.what, msg.obj, msg.arg2, msg.arg1);

					if (msg.what < pageSize) {
						lv.setTag(UIHelper.LISTVIEW_DATA_FULL);
						adapter.notifyDataSetChanged();
						more.setText(R.string.load_full);
					} else if (msg.what == pageSize) {
						lv.setTag(UIHelper.LISTVIEW_DATA_MORE);
						adapter.notifyDataSetChanged();

					}
					// ����֪ͨ�㲥
					// if(notice != null){
					// UIHelper.sendBroadCast(lv.getContext(), notice);
					// }
					// //�Ƿ����֪ͨ��Ϣ
					// if(isClearNotice){
					// ClearNotice(curClearNoticeType);
					// isClearNotice = false;//����
					// curClearNoticeType = 0;
					// }
				} else if (msg.what == -1) {
					// ���쳣--��ʾ���س��� & ����������Ϣ
					lv.setTag(UIHelper.LISTVIEW_DATA_MORE);
					more.setText(R.string.load_error);
					((AppException) msg.obj).makeToast(Main.this);
				}
				if (adapter.getCount() == 0) {
					lv.setTag(UIHelper.LISTVIEW_DATA_EMPTY);
					more.setText(R.string.load_empty);
				}
				progress.setVisibility(ProgressBar.GONE);
				mHeadProgress.setVisibility(ProgressBar.GONE);
				if (msg.arg1 == UIHelper.LISTVIEW_ACTION_REFRESH) {
					lv.onRefreshComplete(getString(R.string.pull_to_refresh_update)
							+ new Date().toLocaleString());
					lv.setSelection(0);
				} else if (msg.arg1 == UIHelper.LISTVIEW_ACTION_CHANGE_CATALOG) {
					lv.onRefreshComplete();
					lv.setSelection(0);
				}
			}
		};
	}

	/**
	 * listview��ݴ���
	 * 
	 * @param what
	 *            ����
	 * @param obj
	 *            ���
	 * @param objtype
	 *            �������
	 * @param actiontype
	 *            ��������
	 * @return notice ֪ͨ��Ϣ
	 */
	private void handleLvData(int what, Object obj, int objtype, int actiontype) {
		// Notice notice = null;
		switch (actiontype) {
			case UIHelper.LISTVIEW_ACTION_INIT :
			case UIHelper.LISTVIEW_ACTION_REFRESH :
			case UIHelper.LISTVIEW_ACTION_CHANGE_CATALOG :
				int newdata = 0;// �¼������-ֻ��ˢ�¶����Ż�ʹ�õ�
				switch (objtype) {
					case UIHelper.LISTVIEW_DATATYPE_NEWS :
						NewsList nlist = (NewsList) obj;
						// notice = nlist.getNotice();
						lvNewsSumData = what;
						if (actiontype == UIHelper.LISTVIEW_ACTION_REFRESH) {
							if (lvNewsData.size() > 0) {
								for (NewsVO news1 : nlist.getNewsList()) {
									boolean b = false;
									for (NewsVO news2 : lvNewsData) {
										if (news1.getId() == news2.getId()) {
											b = true;
											break;
										}
									}
									if (!b) {
										newdata++;
									}
								}
							} else {
								newdata = what;
							}
						}
						lvNewsData.clear();// �����ԭ�����
						lvNewsData.addAll(nlist.getNewsList());
						break;

				}
				if (actiontype == UIHelper.LISTVIEW_ACTION_REFRESH) {
					// ��ʾ�¼������
					if (newdata > 0) {
						NewDataToast.makeText(
								this,
								getString(R.string.new_data_toast_message,
										newdata), appContext.isAppSound())
								.show();
					} else {
						NewDataToast.makeText(this,
								getString(R.string.new_data_toast_none), false)
								.show();
					}
				}
				break;
			case UIHelper.LISTVIEW_ACTION_SCROLL :
				switch (objtype) {
					case UIHelper.LISTVIEW_DATATYPE_NEWS :
						NewsList list = (NewsList) obj;
						// notice = list.getNotice();
						lvNewsSumData += what;
						if (lvNewsData.size() > 0) {
							for (NewsVO news1 : list.getNewsList()) {
								boolean b = false;
								for (NewsVO news2 : lvNewsData) {
									if (news1.getId() == news2.getId()) {
										b = true;
										break;
									}
								}
								if (!b) {
									lvNewsData.add(news1);
								}
							}
						} else {
							lvNewsData.addAll(list.getNewsList());
						}
						break;

				}
				break;
		}
	}

}

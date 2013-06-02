package com.gm.wine.app.ui;





import greendroid.widget.MyQuickAction;
import greendroid.widget.QuickActionGrid;
import greendroid.widget.QuickActionWidget;
import greendroid.widget.QuickActionWidget.OnQuickActionClickListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;









import com.gm.wine.app.AppContext;
import com.gm.wine.app.AppException;
import com.gm.wine.app.R;
import com.gm.wine.app.adapter.ListViewNewsAdapter;
import com.gm.wine.app.common.StringUtils;
import com.gm.wine.app.common.UIHelper;
import com.gm.wine.app.widget.NewDataToast;
import com.gm.wine.app.widget.PullToRefreshListView;
import com.gm.wine.vo.NewsList;
import com.gm.wine.vo.NewsVO;

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

public class Main extends BaseActivity {
    public static final int QUICKACTION_LOGIN_OR_LOGOUT = 0;
    public static final int QUICKACTION_USERINFO = 1;
    public static final int QUICKACTION_SETTING = 2;
    public static final int QUICKACTION_EXIT = 3;
    
	private AppContext appContext;//ȫ��Context
	
	private ImageView mHeadLogo;
	private TextView mHeadTitle;
	private ProgressBar mHeadProgress;
	private ImageButton mHeadPub_post;
	
	private RadioButton fbNews;//��Ѷ
	private RadioButton fbProduct;//��Ʒ����
	private RadioButton fbNotice;//���Թ���
	private ImageView fbSetting;
	
	private ListViewNewsAdapter lvNewsAdapter;
	private PullToRefreshListView lvNews;
	private List<NewsVO> lvNewsData = new ArrayList<NewsVO>();
	private View lvNews_footer;
	private TextView lvNews_foot_more;
	private ProgressBar lvNews_foot_progress;
	private int lvNewsSumData;
	private Handler lvNewsHandler;
	
	private QuickActionWidget mGrid;//������ؼ�
	


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		appContext = (AppContext)getApplication();
	        //���������ж�
		if(!appContext.isNetworkConnected())
			UIHelper.ToastMessage(this, R.string.network_not_connected);
		this.initHeadView();
		this.initFootBar();
		this.initQuickActionGrid();
		this.initFrameListView();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}
	/**
     * ��ʼ�������б�
     */
    private void initNewsListView()
    {
        lvNewsAdapter = new ListViewNewsAdapter(this, lvNewsData, R.layout.news_listitem);        
        lvNews_footer = getLayoutInflater().inflate(R.layout.listview_footer, null);
        lvNews_foot_more = (TextView)lvNews_footer.findViewById(R.id.listview_foot_more);
        lvNews_foot_progress = (ProgressBar)lvNews_footer.findViewById(R.id.listview_foot_progress);
        lvNews = (PullToRefreshListView)findViewById(R.id.frame_listview_news);
        lvNews.addFooterView(lvNews_footer);//��ӵײ���ͼ  ������setAdapterǰ
        lvNews.setAdapter(lvNewsAdapter); 
        lvNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        		//���ͷ�����ײ�����Ч
        		if(position == 0 || view == lvNews_footer) return;
        		
        		NewsVO news = null;        		
        		//�ж��Ƿ���TextView
        		if(view instanceof TextView){
        			news = (NewsVO)view.getTag();
        		}else{
        			TextView tv = (TextView)view.findViewById(R.id.news_listitem_title);
        			news = (NewsVO)tv.getTag();
        		}
        		if(news == null) return;
        		
        		//��ת����������
        		//UIHelper.showNewsRedirect(view.getContext(), news);
        	}        	
		});
        lvNews.setOnScrollListener(new AbsListView.OnScrollListener() {
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				lvNews.onScrollStateChanged(view, scrollState);
				
				//����Ϊ��--���ü������������
				if(lvNewsData.isEmpty()) return;
				
				//�ж��Ƿ�������ײ�
				boolean scrollEnd = false;
				try {
					if(view.getPositionForView(lvNews_footer) == view.getLastVisiblePosition())
						scrollEnd = true;
				} catch (Exception e) {
					scrollEnd = false;
				}
				
				int lvDataState = StringUtils.toInt(lvNews.getTag());
				if(scrollEnd && lvDataState==UIHelper.LISTVIEW_DATA_MORE)
				{
					lvNews.setTag(UIHelper.LISTVIEW_DATA_LOADING);
					lvNews_foot_more.setText(R.string.load_ing);
					lvNews_foot_progress.setVisibility(View.VISIBLE);
					//��ǰpageIndex
					int pageIndex = lvNewsSumData/AppContext.PAGE_SIZE;
					loadLvNewsData( pageIndex, lvNewsHandler, UIHelper.LISTVIEW_ACTION_SCROLL);
				}
			}
			public void onScroll(AbsListView view, int firstVisibleItem,int visibleItemCount, int totalItemCount) {
				lvNews.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
			}
		});
        lvNews.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
            public void onRefresh() {
            	loadLvNewsData( 0, lvNewsHandler, UIHelper.LISTVIEW_ACTION_REFRESH);
            }
        });					
    }
	/**
     * �̼߳�����������
     * @param catalog ����
     * @param pageIndex ��ǰҳ��
     * @param handler ������
     * @param action ������ʶ
     */
	private void loadLvNewsData(final int pageIndex,final Handler handler,final int action){ 
		mHeadProgress.setVisibility(ProgressBar.VISIBLE);		
		new Thread(){
			public void run() {				
				Message msg = new Message();
				boolean isRefresh = false;
				if(action == UIHelper.LISTVIEW_ACTION_REFRESH || action == UIHelper.LISTVIEW_ACTION_SCROLL)
					isRefresh = true;
				try {					
					NewsList list = appContext.getNewsList(pageIndex, isRefresh);				
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
     * ��ʼ��ͷ����ͼ
     */
    private void initHeadView()
    {
    	mHeadLogo = (ImageView)findViewById(R.id.main_head_logo);
    	mHeadTitle = (TextView)findViewById(R.id.main_head_title);
    	mHeadProgress = (ProgressBar)findViewById(R.id.main_head_progress);
    	mHeadPub_post = (ImageButton)findViewById(R.id.main_head_pub_post);
    	

    	mHeadPub_post.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				//UIHelper.showQuestionPub(v.getContext());
			}
		});

    }
    /**
     * ��ʼ���ײ���
     */
    private void initFootBar()
    {
    	fbNews = (RadioButton)findViewById(R.id.main_footbar_news);
    	fbProduct = (RadioButton)findViewById(R.id.main_footbar_product);
    	fbNotice = (RadioButton)findViewById(R.id.main_footbar_notice);
    	fbSetting = (ImageView)findViewById(R.id.main_footbar_setting);
    	fbSetting.setOnClickListener(new View.OnClickListener() {
    		public void onClick(View v) {    			
    			//չʾ�����&�ж��Ƿ��¼&�Ƿ��������ͼƬ
    			UIHelper.showSettingLoginOrLogout(Main.this, mGrid.getQuickAction(0));
    			mGrid.show(v);
    		}
    	});    	
    }
    /**
     * ��ʼ�������
     */
    private void initQuickActionGrid() {
        mGrid = new QuickActionGrid(this);
        mGrid.addQuickAction(new MyQuickAction(this, R.drawable.ic_menu_login, R.string.main_menu_login));
        mGrid.addQuickAction(new MyQuickAction(this, R.drawable.ic_menu_myinfo, R.string.main_menu_myinfo));
        mGrid.addQuickAction(new MyQuickAction(this, R.drawable.ic_menu_setting, R.string.main_menu_setting));
        mGrid.addQuickAction(new MyQuickAction(this, R.drawable.ic_menu_exit, R.string.main_menu_exit));
        
        mGrid.setOnQuickActionClickListener(mActionListener);
    }
    
    /**
     * �����item����¼�
     */
    private OnQuickActionClickListener mActionListener = new OnQuickActionClickListener() {
        public void onQuickActionClicked(QuickActionWidget widget, int position) {
    		switch (position) {
    		case QUICKACTION_LOGIN_OR_LOGOUT://�û���¼-ע��
    			UIHelper.loginOrLogout(Main.this);
    			break;
    		case QUICKACTION_USERINFO://�ҵ�����
    			UIHelper.showUserInfo(Main.this);
    			break;
    		case QUICKACTION_SETTING://����
    			UIHelper.showSetting(Main.this);
    			break;
    		case QUICKACTION_EXIT://�˳�
    			UIHelper.Exit(Main.this);
    			break;
    		}
        }
    };
    /**
     * ��ʼ������ListView
     */
    private void initFrameListView()
    {
    	//��ʼ��listview�ؼ�
		this.initNewsListView();
		//����listview����
		this.initFrameListViewData();
    }
    /**
     * ��ʼ������ListView����
     */
    private void initFrameListViewData()
    {
        //��ʼ��Handler
        lvNewsHandler = this.getLvHandler(lvNews, lvNewsAdapter, lvNews_foot_more, lvNews_foot_progress, AppContext.PAGE_SIZE);
     	
    	
        //������Ѷ����
		if(lvNewsData.isEmpty()) {
			loadLvNewsData( 0, lvNewsHandler, UIHelper.LISTVIEW_ACTION_INIT);
		}
    }
    /**
     * ��ȡlistview�ĳ�ʼ��Handler
     * @param lv
     * @param adapter
     * @return
     */
    private Handler getLvHandler(final PullToRefreshListView lv,final BaseAdapter adapter,final TextView more,final ProgressBar progress,final int pageSize){
    	return new Handler(){
			public void handleMessage(Message msg) {
				if(msg.what >= 0){
					//listview���ݴ���
				 handleLvData(msg.what, msg.obj, msg.arg2, msg.arg1);
					
					if(msg.what < pageSize){
						lv.setTag(UIHelper.LISTVIEW_DATA_FULL);
						adapter.notifyDataSetChanged();
						more.setText(R.string.load_full);
					}else if(msg.what == pageSize){
						lv.setTag(UIHelper.LISTVIEW_DATA_MORE);
						adapter.notifyDataSetChanged();

					}
					//����֪ͨ�㲥
//					if(notice != null){
//						UIHelper.sendBroadCast(lv.getContext(), notice);
//					}
//					//�Ƿ����֪ͨ��Ϣ
//					if(isClearNotice){
//						ClearNotice(curClearNoticeType);
//						isClearNotice = false;//����
//						curClearNoticeType = 0;
//					}
				}
				else if(msg.what == -1){
					//���쳣--��ʾ���س��� & ����������Ϣ
					lv.setTag(UIHelper.LISTVIEW_DATA_MORE);
					more.setText(R.string.load_error);
					((AppException)msg.obj).makeToast(Main.this);
				}
				if(adapter.getCount()==0){
					lv.setTag(UIHelper.LISTVIEW_DATA_EMPTY);
					more.setText(R.string.load_empty);
				}
				progress.setVisibility(ProgressBar.GONE);
				mHeadProgress.setVisibility(ProgressBar.GONE);
				if(msg.arg1 == UIHelper.LISTVIEW_ACTION_REFRESH){
					lv.onRefreshComplete(getString(R.string.pull_to_refresh_update) + new Date().toLocaleString());
					lv.setSelection(0);
				}else if(msg.arg1 == UIHelper.LISTVIEW_ACTION_CHANGE_CATALOG){
					lv.onRefreshComplete();
					lv.setSelection(0);
				}
			}
		};
    }
    /**
     * listview���ݴ���
     * @param what ����
     * @param obj ����
     * @param objtype ��������
     * @param actiontype ��������
     * @return notice ֪ͨ��Ϣ
     */
    private void handleLvData(int what,Object obj,int objtype,int actiontype){
    	//Notice notice = null;
		switch (actiontype) {
			case UIHelper.LISTVIEW_ACTION_INIT:
			case UIHelper.LISTVIEW_ACTION_REFRESH:
			case UIHelper.LISTVIEW_ACTION_CHANGE_CATALOG:
				int newdata = 0;//�¼�������-ֻ��ˢ�¶����Ż�ʹ�õ�
				switch (objtype) {
					case UIHelper.LISTVIEW_DATATYPE_NEWS:
						NewsList nlist = (NewsList)obj;
						//notice = nlist.getNotice();
						lvNewsSumData = what;
						if(actiontype == UIHelper.LISTVIEW_ACTION_REFRESH){
							if(lvNewsData.size() > 0){
								for(NewsVO news1 : nlist.getNewsList()){
									boolean b = false;
									for(NewsVO news2 : lvNewsData){
										if(news1.getId() == news2.getId()){
											b = true;
											break;
										}
									}
									if(!b) newdata++;
								}
							}else{
								newdata = what;
							}
						}
						lvNewsData.clear();//�����ԭ������
						lvNewsData.addAll(nlist.getNewsList());
						break;

				}
				if(actiontype == UIHelper.LISTVIEW_ACTION_REFRESH){
					//��ʾ�¼�������
					if(newdata >0){
						NewDataToast.makeText(this, getString(R.string.new_data_toast_message, newdata), appContext.isAppSound()).show();
					}else{
						NewDataToast.makeText(this, getString(R.string.new_data_toast_none), false).show();
					}
				}
				break;
			case UIHelper.LISTVIEW_ACTION_SCROLL:
				switch (objtype) {
					case UIHelper.LISTVIEW_DATATYPE_NEWS:
						NewsList list = (NewsList)obj;
						//notice = list.getNotice();
						lvNewsSumData += what;
						if(lvNewsData.size() > 0){
							for(NewsVO news1 : list.getNewsList()){
								boolean b = false;
								for(NewsVO news2 : lvNewsData){
									if(news1.getId() == news2.getId()){
										b = true;
										break;
									}
								}
								if(!b) lvNewsData.add(news1);
							}
						}else{
							lvNewsData.addAll(list.getNewsList());
						}
						break;

				}
				break;
		}
    }

}

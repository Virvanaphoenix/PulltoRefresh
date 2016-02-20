package com.anna.fresh.activity;

import java.util.ArrayList;
import java.util.List;

import com.anna.fresh.R;
import com.anna.fresh.view.RefreshListView;
import com.anna.fresh.view.RefreshListView.OnRefreshListener;
import com.iflytek.voiceads.AdError;
import com.iflytek.voiceads.AdKeys;
import com.iflytek.voiceads.IFLYAdListener;
import com.iflytek.voiceads.IFLYAdSize;
import com.iflytek.voiceads.IFLYFullScreenAd;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private RefreshListView refresh;
	private List<String> list=new ArrayList<String>();
	private MyAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		initView();
		initData();
		
	}
	private Handler h=new Handler(){
		public void handleMessage(android.os.Message msg) {
			adapter.notifyDataSetChanged();
			refresh.completeRefresh();
		};
	};
	

	private void initView() {
		createAd();
		setContentView(R.layout.activity_main);
		refresh = (RefreshListView) findViewById(R.id.refresh);
	}
		private void initData() {
		for (int i = 0; i <15; i++) {
			list.add("RefreshListView原来的数据--"+i);
		}
//		final View headView=View.inflate(this,R.layout.head,null);
		/**
		 * 隐藏headView的两张方法
		 * 1、onLayout执行完之后去获取
		 * 2、onMeasure执行完去获取
		 */
//		headView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
//			
//			@Override
//			public void onGlobalLayout() {
//				headView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//				int height = headView.getHeight();
//				headView.setPadding(0, -height,0,0);
//				refresh.addHeaderView(headView);
//			}
//		});
//		headView.measure(0, 0);//通知系统重新测量
//		int height = headView.getMeasuredHeight();
//		headView.setPadding(0, -height,0,0);
//		refresh.addHeaderView(headView);
		
		
		adapter = new MyAdapter();
		refresh.setAdapter(adapter);
		refresh.setOnRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onPullRefresh() {
				Log.e("MainActivity","正在执行刷新。。。。。");
				//请求数据，更新数据
				requestDataFromServer(false);
			}

			@Override
			public void onLoadingMore() {
				requestDataFromServer(true);
			}
		});
	}
	/*
	 * 模拟向服务器请求数据
	 */
	private void requestDataFromServer(final boolean isLoading){
		new Thread(){
			public void run() {
				SystemClock.sleep(3000);
				if(isLoading){
					list.add("加载到新的数据--Anna");
					list.add("加载到新的数据--Rain");
					list.add("加载到新的数据--Bone");
				}else{
					list.add(0, "更新到最新的数据");
				}
				//更新UI
				h.sendEmptyMessage(0);
			};
		}.start();
	}
	
	class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View contentView, ViewGroup parent) {
			TextView textView=new TextView(MainActivity.this);
			textView.setPadding(20,20,20,20);
			textView.setTextSize(18);
			textView.setText(list.get(position));
			return textView;
		}
		
	}
	private IFLYFullScreenAd ad;
	private void createAd() {
		ad = IFLYFullScreenAd.createFullScreenAd(this,
				"8129289E334CB6DE5874FDF61EEFA68B");
		ad.setAdSize(IFLYAdSize.FULLSCREEN);
		ad.setParameter(AdKeys.SHOW_TIME_FULLSCREEN, "6000");
		ad.setParameter(AdKeys.DOWNLOAD_ALERT, "true");
		ad.loadAd(new IFLYAdListener() {
			
			@Override
			public void onAdReceive() {
				// TODO Auto-generated method stub
				ad.showAd();
			}
			
			@Override
			public void onAdFailed(AdError arg0) {
				Toast.makeText(
						getApplicationContext(),
						arg0.getErrorCode() + "****"
								+ arg0.getErrorDescription(), 0).show();
				
			}
			
			@Override
			public void onAdExposure() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAdClose() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAdClick() {
				// TODO Auto-generated method stub
				
			}
		});
	}

}







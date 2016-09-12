package com.tc.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.AnimationUtils;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tc.utils.Coords;
import com.tc.utils.TimerUtils;
import com.tc.whoami.R;
import com.tc.whoami.RecognizeActivity;

/**
 * 左滑 + 接触面积
 * 
 * @author hgs
 *
 */
public class RecognizeFragmentOne extends BaseFragment implements
		OnTouchListener {
	private ViewPager vp;
	private ArrayList<View> listView;
	private FragmentManager fm;
	private MyPagerAdapter mAdapter;
	// private boolean haveChange = false;
	private ImageView iv_touch;
	private float maxSize = 0;
	private List<Coords> viewPagerCoordsList = new ArrayList<Coords>();
	private float leftSlidingDownSize;
	private TimerUtils timerUtils;
	private long betweenTime;
	private boolean haveTouch = false;
	private View iv_pager_left;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		fm = getActivity().getSupportFragmentManager();
		timerUtils = new TimerUtils();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.recognize_fragment_three, null);
		init(view);
		return view;
	}

	private void init(View view) {
		vp = (ViewPager) view.findViewById(R.id.recognize_viewpager);
		listView = new ArrayList<View>();
		View v1 = LayoutInflater.from(getActivity()).inflate(
				R.layout.pager_layout1, null);
		iv_pager_left = v1.findViewById(R.id.iv_pager_left);
		iv_pager_left.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.left_anim));
		View v2 = LayoutInflater.from(getActivity()).inflate(
				R.layout.recognize_touch_point, null);
		initV2(v2);
		listView.add(v1);
		listView.add(v2);
		mAdapter = new MyPagerAdapter(listView);
		vp.setAdapter(mAdapter);
		vp.setOnTouchListener(this);
	}

	private void initV2(View v2) {
		iv_touch = (ImageView) v2.findViewById(R.id.recognize_touch_view);
		iv_touch.setOnTouchListener(this);
	}

	class MyPagerAdapter extends PagerAdapter {
		private List<View> listView;

		public MyPagerAdapter(List<View> lv) {
			this.listView = lv;
		}

		@Override
		public int getCount() {
			return listView.size();
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			Log.i("rthree", position + "");
			container.removeView(listView.get(position));
		}

		@Override
		public boolean isViewFromObject(View view, Object arg1) {
			// view = listView.get(getCount());
			return view == arg1;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(listView.get(position));
			return listView.get(position);
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		int id = v.getId();
		switch (id) {
		case R.id.recognize_viewpager:
			touchRV(event);
			break;
		case R.id.recognize_touch_view:
			touchRTV(event);
			return true;
		}
		return false;
	}

	// 左滑 特征 数据提取
	private void touchRV(MotionEvent event) {
		int action = event.getAction();
		int x = (int) event.getX();
		int y = (int) event.getY();
		float size = event.getSize();
		int curItem = vp.getCurrentItem();
		if (curItem == 0) {// 当为ViewPager的初始页时有效
			switch (action) {
			case MotionEvent.ACTION_DOWN:
				viewPagerCoordsList.clear();
				leftSlidingDownSize = size;
				Log.i("touchRV", "DOWN");
				viewPagerCoordsList.add(new Coords(x, y));
				timerUtils.startRecord();
				break;
			case MotionEvent.ACTION_MOVE:
				// Log.i("touchRV",""+ maxSize);
				viewPagerCoordsList.add(new Coords(x, y));
				break;
			case MotionEvent.ACTION_UP:
				timerUtils.stopRecord();
				betweenTime = timerUtils.getBetweenTime();
				viewPagerCoordsList.add(new Coords(x, y));
				Log.i("touchRV", "UP");
				break;
			}
		}
	}

	/**
	 * 圆心 处touch事件
	 * 
	 * @param event
	 */
	private void touchRTV(MotionEvent event) {
		int action = event.getAction();
		float size = event.getSize();
		if (size > maxSize) {
			maxSize = size;// 得到最大面积
		}
		if (!haveTouch)// 防止多次点击产生异常
			switch (action) {
			case MotionEvent.ACTION_DOWN:
				maxSize = 0;
				break;
			case MotionEvent.ACTION_UP:
				Log.i("touchSize", "" + maxSize);
				handler.sendEmptyMessageDelayed(0x1, 1000);// 延长一秒发送
				haveTouch = true;
				break;
			}
	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 0x1) {
				// 数据暂存
				RecognizeActivity.rud
						.setLeftSlidingDownSize(leftSlidingDownSize);
				RecognizeActivity.rud
						.setViewPagerCoordsList(viewPagerCoordsList);
				RecognizeActivity.rud.addBetweenTime(betweenTime);
				RecognizeActivity.rud.setTouchMaxSize(maxSize);
				// 替换到fragment3
				RecognizeFragmentTwo fragment2 = new RecognizeFragmentTwo();// 启动到第三个fragment
				fm.beginTransaction()
						.replace(R.id.recognize_frameLayout, fragment2)
						.commit();
			}
		}
	};
	
	
}

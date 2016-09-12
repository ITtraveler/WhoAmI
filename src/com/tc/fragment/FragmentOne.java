package com.tc.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.tc.utils.FileUtils;
import com.tc.utils.SharedPreferenceUtils;
import com.tc.utils.TimerUtils;
import com.tc.whoami.R;

/**
 * ��������ȡ
 * 
 * @author hgs
 *
 */
public class FragmentOne extends BaseFragment implements OnTouchListener {

	private FragmentManager fm;
	private FragmentTwo fragment2;
	private ArrayList<View> listView;
	private ViewPager viewPager;
	private SharedPreferences preference;
	private String username;
	//private int mLocation = 0;// ��¼��ǰViewPager��λ��
	private int oldLocation = 0;
	///private boolean havaNext = false;// �ж��Ƿ��л�����һ��pager
	private TimerUtils timerUtils;
	private StringBuilder sb = new StringBuilder();
	private int count = 0;
	//private boolean firstUse;
	private ImageView iv_left,iv_left_gesture;
	private final int PAGERCOUNT = 4;
	private ImageView iv2, iv3, iv4, iv5;
	private View view;
	private boolean haveWrite = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		fm = getActivity().getSupportFragmentManager();
		preference = getActivity().getSharedPreferences(
				SharedPreferenceUtils.NAME, Context.MODE_PRIVATE);
		username = preference.getString("username", "default");
		timerUtils = new TimerUtils();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_one, null);
		// view.setOnTouchListener(this);
		init(view);
		return view;
	}

	private void init(View view) {
		changePointShow(view, 0);// ��ʼ���� λ�õ㣬��ΪPagerChange�����������ڸս���ʱ����
		viewPager = (ViewPager) view.findViewById(R.id.viewpager);
		viewPager.setOnTouchListener(this);
		initViewPager(viewPager);

	}

	/**
	 * ��ʼ��ViewPager
	 * 
	 * @param viewPager2
	 */
	private void initViewPager(ViewPager viewPager2) {
		listView = new ArrayList<View>();
		View v1 = LayoutInflater.from(getActivity()).inflate(
				R.layout.pager_layout1, null);
		iv_left_gesture = (ImageView)v1.findViewById(R.id.iv_pager_left);
		iv_left_gesture.startAnimation(AnimationUtils.loadAnimation(getActivity(),
				R.anim.left_anim));
		listView.add(v1);

		for (int i = 0; i < 4; i++) {
			View view = LayoutInflater.from(getActivity()).inflate(
			R.layout.pager_layout2, null);
			startAnim(view);
			listView.add(view);
		}
		

		View v3 = LayoutInflater.from(getActivity()).inflate(
				R.layout.pager_layout3, null);
		listView.add(v3);

		// ��һ��
		Button bn = (Button) v3.findViewById(R.id.bn_next);
		bn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				fragment2 = new FragmentTwo();
				fm.beginTransaction().replace(R.id.frameLayout, fragment2)
						.commit();
				clearAnim();
			}
		});

		viewPager.setAdapter(new MyPagerAdapter(listView));
		viewPager.addOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {
				changePointShow(view, arg0);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}

	/**
	 * ��ʼ����
	 * @param v
	 */
	public void startAnim(View v) {
		iv_left = (ImageView) v.findViewById(R.id.left_hint);
		iv_left.startAnimation(AnimationUtils.loadAnimation(getActivity(),
				R.anim.left_anim));
	}

	/**
	 *  �������
	 */
	public void clearAnim() {
		iv_left.clearAnimation();
	}
	
	/**
	 * ViewPager������
	 * @author hgs
	 *
	 */
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
		// StringBuilder sb = new StringBuilder();
		float x = event.getX(0);
		float y = event.getY(0);
		int curItem = viewPager.getCurrentItem();
		//System.out.println("curItem:" + curItem);
		// ֻ�е�����һ����ʱ�� ��д���ĵ���Ŀ����Ϊ�� ��ֹ��¼��Ч��ֵ����û�л�����һ��pagerʱ���������¼value��
		motionEvent(event, x, y);
		return false;
	}

	private void motionEvent(MotionEvent event, float x, float y) {

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			int curLocation = viewPager.getCurrentItem();
			if (curLocation == oldLocation) {//�������ԭ�ȵ�λ�ã�������ɾ��
				sb.delete(0, sb.length());
			} else {//��������д���ļ��б���
				writeToFile();
				oldLocation = curLocation;
				count++;
			}
			timerUtils.startRecord();// ��ʼ��¼ʱ��
			String motion = "Motion horizontal:";
			String downSize = event.getSize() + "\n";
			String downs = "hdown x:" + x + "   y:" + y + "\n";
			sb.append(motion + downSize + downs);
			break;
		case MotionEvent.ACTION_MOVE:
			String moves = "hmove" + count + " x:" + x + "   y:" + y;
			sb.append(moves);
			break;
		case MotionEvent.ACTION_UP:

			timerUtils.stopRecord();// ֹͣ��¼ʱ��
			long betweenTime = timerUtils.getBetweenTime();// �õ�ʱ������
			System.out.println("hup x:" + x + "   y:" + y);
			String up = "\nhup x:" + x + "   y:" + y + "\nhbetweenTime:"
					+ betweenTime + "\n";
			sb.append(up);
			if (!haveWrite&& oldLocation == PAGERCOUNT) {
				writeToFile();
				haveWrite = true;
			}
			break;
		default:
			break;
		}
	}

	private void writeToFile() {
		FileUtils.writeFile(FileUtils.generatePath(0), FileUtils.TAG_NAME0,
				sb.toString(), username);
		sb.delete(0, sb.length());// ���sb�е�����
	}

	/**
	 * λ�õ����ʾ
	 * @param v
	 * @param position
	 */
	private void changePointShow(View v, int position) {
		LinearLayout linearLayout = (LinearLayout) v
				.findViewById(R.id.location_point_h);
		// ImageView iv1 = (ImageView) v.findViewById(R.id.iv_point_one);

		switch (position) {
		case 0:
			linearLayout.setVisibility(View.GONE);
			iv2 = (ImageView) v.findViewById(R.id.iv_point_two);
			iv3 = (ImageView) v.findViewById(R.id.iv_point_three);
			iv4 = (ImageView) v.findViewById(R.id.iv_point_four);
			iv5 = (ImageView) v.findViewById(R.id.iv_point_five);
			break;
		case 1:
			linearLayout.setVisibility(View.VISIBLE);
			iv2.setImageResource(R.drawable.ovel_point);
			break;
		case 2:
			iv2.setImageResource(R.drawable.ovel_alpha_point);
			iv3.setImageResource(R.drawable.ovel_point);
			break;
		case 3:
			iv3.setImageResource(R.drawable.ovel_alpha_point);
			iv4.setImageResource(R.drawable.ovel_point);
			break;
		case 4:
			iv4.setImageResource(R.drawable.ovel_alpha_point);
			iv5.setImageResource(R.drawable.ovel_point);
			break;
		case 5:
			linearLayout.setVisibility(View.GONE);
			break;
		}
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		iv_left_gesture.clearAnimation();
	}
}

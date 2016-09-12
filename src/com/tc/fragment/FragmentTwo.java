package com.tc.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.tc.defineviewgroup.VerticalSliding;
import com.tc.defineviewgroup.VerticalSliding.OnPageScrollListener;
import com.tc.utils.FileUtils;
import com.tc.utils.SharedPreferenceUtils;
import com.tc.utils.TimerUtils;
import com.tc.whoami.R;

/**
 * 上滑特征提取
 * 
 * @author hgs
 *
 */
public class FragmentTwo extends BaseFragment implements OnTouchListener {
	private Fragment fragment3;
	private FragmentManager fm;
	private int layoutId[] = { R.layout.sliding_layout1,
			R.layout.sliding_layout2, R.layout.sliding_layout3 };
	private SharedPreferences preference;
	private String username;
	private TimerUtils timerUtils;
	private StringBuilder sb = new StringBuilder();
	private VerticalSliding vs;
	private int count = 0;
	private int oldLocation = 0;
	private ImageView iv_up;
	private final int PAGERCOUNT = 4;
	private ImageView iv2;
	private ImageView iv3;
	private ImageView iv4;
	private ImageView iv5;
	private View view;
	private View iv_pager_up;

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
		view = inflater.inflate(R.layout.fragment_two, null);
		init(view);
		return view;

	}

	// 初始化控件
	private void init(final View view) {
		changePointShow(view, 0);
		vs = (VerticalSliding) view.findViewById(R.id.verticalsliding);
		ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);
		View view1 = getActivity().getLayoutInflater().inflate(layoutId[0],
				null);
		iv_pager_up = view1.findViewById(R.id.iv_pager_up);
		iv_pager_up.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.up_anim));
		vs.addView(view1, lp);
		View view2 = getActivity().getLayoutInflater().inflate(layoutId[1],
				null);
		vs.addView(view2, lp);

		View view3 = getActivity().getLayoutInflater().inflate(layoutId[1],
				null);
		vs.addView(view3, lp);
		View view4 = getActivity().getLayoutInflater().inflate(layoutId[1],
				null);
		vs.addView(view4, lp);
		View view5 = getActivity().getLayoutInflater().inflate(layoutId[1],
				null);
		vs.addView(view5, lp);
		View view6 = getActivity().getLayoutInflater().inflate(layoutId[2],
				null);
		vs.addView(view6, lp);
		startAnim(view2);
		startAnim(view3);
		startAnim(view4);
		startAnim(view5);
		Button bn = (Button) view6.findViewById(R.id.bn_next);
		bn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				fragment3 = new FragmentThree();
				fm.beginTransaction().replace(R.id.frameLayout, fragment3)
						.commit();
				clearAnim();
			}
		});
		vs.setOnTouchListener(this);
		vs.setOnPageScrollListener(new OnPageScrollListener() {
			
			@Override
			public void onPageChanged(int position) {
				System.out.println("pos:"+position);
				changePointShow(view, position);
			}
		});
	}

	public void startAnim(View v) {
		iv_up = (ImageView) v.findViewById(R.id.t1_next);
		iv_up.startAnimation(AnimationUtils.loadAnimation(getActivity(),
				R.anim.up_anim));
	}

	public void clearAnim() {
		iv_up.clearAnimation();
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {

		float x = event.getX(0);
		float y = event.getY(0);
		motionEvent(event, x, y);
		return false;
	}

	private void motionEvent(MotionEvent event, float x, float y) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			int curLocation = vs.getCurPagerID();
			// System.out.println("curl:" + curLocation + "oldl:" +
			// oldLocation);
			if (curLocation == oldLocation) {
				sb.delete(0, sb.length());
			} else {
				writeToFile();
				oldLocation = curLocation;
				count++;
			}
			timerUtils.startRecord();
			// Toast.makeText(getActivity(), "down", 100).show();
			System.out.println("vdown x:" + x + "   y:" + y);
			String motion = "Motion vertical:";
			String downSize = event.getSize() + "\n";
			String downs = "vdown x:" + x + "   y:" + y + "\n";
			// FileUtils.writeFile(motion + downSize + downs, username);
			sb.append(motion + downSize + downs);
			break;
		case MotionEvent.ACTION_MOVE:
			// Toast.makeText(getActivity(), "move", 100).show();
			System.out.println("move x:" + x + "   y:" + y);
			String moves = "vmove" + count + " x:" + x + "   y:" + y;
			// FileUtils.writeFile(moves, username);
			sb.append(moves);
			break;
		case MotionEvent.ACTION_UP:
			timerUtils.stopRecord();// 停止记录时间

			long betweenTime = timerUtils.getBetweenTime();// 得到时间区间
			System.out.println("up x:" + x + "   y:" + y);
			// Toast.makeText(getActivity(), "up", 100).show()
			String up = "\nvup x:" + x + "   y:" + y + "\nvbetweenTime:"
					+ betweenTime + "\n";
			// FileUtils.writeFile(up, username);
			sb.append(up);
			if (oldLocation == PAGERCOUNT) {
				writeToFile();
			}

			break;
		default:
			break;
		}
	}

	private void writeToFile() {
		FileUtils.writeFile(FileUtils.generatePath(0), FileUtils.TAG_NAME0,
				sb.toString(), username);
		sb.delete(0, sb.length());// 清空sb中的数据
	}
	
	private void changePointShow(View v, int position) {
		LinearLayout linearLayout = (LinearLayout) v
				.findViewById(R.id.location_point_v);
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
			iv5.setImageResource(R.drawable.ovel_point);
			break;
		case 4:
			iv5.setImageResource(R.drawable.ovel_alpha_point);
			iv4.setImageResource(R.drawable.ovel_point);
			break;
		case 5:
			linearLayout.setVisibility(View.GONE);
			break;
		}
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		iv_pager_up.clearAnimation();
	}
}

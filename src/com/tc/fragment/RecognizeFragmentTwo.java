package com.tc.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tc.defineviewgroup.VerticalSliding;
import com.tc.dialog.TextDialog;
import com.tc.utils.Coords;
import com.tc.utils.TimerUtils;
import com.tc.whoami.R;
import com.tc.whoami.RecognizeActivity;

/**
 * 此处用于提取上滑+反应速度
 * 
 * @author hgs
 *
 */
public class RecognizeFragmentTwo extends BaseFragment implements
		OnTouchListener, OnClickListener {
	private final int THEME_READ = 0;
	private final int THEME_GREEN = 1;
	private final int THEME_BLUE = 2;
	private final int THEME_SOON = 3;
	private final int THEME_COMPLETE = 4;
	private final int MAXCOUNT = 3;//最大次数
	private final long TIME_OVER = 1500;//超时
	private ImageView iv_explanation;
	private TextView tv_title_content, tv_action_hint, tv_score, tv_count;
	private Button bn_reStart, bn_complete;
	private RelativeLayout relativeLayout;

	private int TAGCOUNT = 0;
	private boolean haveToGreen = false;
	private List<Integer> reationV = new ArrayList<Integer>();// 反应速度集合
	private TimerUtils timerUtils = new TimerUtils();
	private long curScore;
	private int average = 0;
	private int count = 0;
	VerticalSliding vs;
	private FragmentManager fm;
	// private float maxSize = 0;
	// private List<Float> touchSize = new ArrayList<Float>();
	private List<Coords> upSlidingCoordsList = new ArrayList<Coords>();// 上滑操作的轨迹坐标集
	private float upSlidingDownSize;
	private long betweenTime;
	private ImageView iv_cirle_in, iv_cirle_out;
	private ImageView iv_pager_up;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		fm = getActivity().getSupportFragmentManager();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.recognize_fragment_two,
				container, false);
		init(view);
		return view;
	}

	// 初始化 一些工作
	private void init(View view) {
		vs = (VerticalSliding) view
				.findViewById(R.id.recognize_verticalsliding);
		ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);
		View v1 = getActivity().getLayoutInflater().inflate(
				R.layout.sliding_layout1, null);
		View v2 = getActivity().getLayoutInflater().inflate(
				R.layout.fragment_four, null);
		initV2(v2);
		startAnim(v1);
		vs.setOnTouchListener(this);
		vs.addView(v1, lp);
		vs.addView(v2, lp);
	}

	/**
	 * 开始动画
	 * 
	 * @param v
	 */
	public void startAnim(View v) {
		iv_pager_up = (ImageView) v.findViewById(R.id.iv_pager_up);
		iv_pager_up.startAnimation(AnimationUtils.loadAnimation(getActivity(),
				R.anim.up_anim));
	}

	public void clearAnim() {
		iv_pager_up.clearAnimation();
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		int viewId = v.getId();
		int action = event.getAction();
		switch (viewId) {
		case R.id.recognize_verticalsliding:
			touchVerticalsliding(action, event);
			break;
		}
		return false;
	}

	/**
	 * 
	 * 
	 * @param action
	 * @param event
	 */

	private void touchVerticalsliding(int action, MotionEvent event) {
		int x = (int) event.getX();
		int y = (int) event.getY();
		float size = event.getSize();
		int curPagerID = vs.getCurPagerID();
		System.out.println("curID:" + curPagerID);
		if (curPagerID == 0) {
			switch (action) {
			case MotionEvent.ACTION_DOWN:
				upSlidingCoordsList.clear();
				upSlidingDownSize = size;
				upSlidingCoordsList.add(new Coords(x, y));
				timerUtils.startRecord();
				break;
			case MotionEvent.ACTION_MOVE:
				upSlidingCoordsList.add(new Coords(x, y));
				break;
			case MotionEvent.ACTION_UP:
				timerUtils.stopRecord();
				betweenTime = timerUtils.getBetweenTime();
				upSlidingCoordsList.add(new Coords(x, y));
				System.out.println("usc _ up:" + curPagerID);
				handler.sendEmptyMessage(0x2);
				break;
			}
		}
	}

	private void initV2(View view) {
		// 小游戏说明
		iv_explanation = (ImageView) view.findViewById(R.id.ff_iv_explanation);
		iv_explanation.setOnClickListener(this);

		// 圈圈
		iv_cirle_in = (ImageView) view.findViewById(R.id.ff_iv_cricle_in);// 内圈
		iv_cirle_out = (ImageView) view.findViewById(R.id.ff_iv_cricle_out);// 外圈

		relativeLayout = (RelativeLayout) view
				.findViewById(R.id.ff_relativelayout);
		tv_title_content = (TextView) view.findViewById(R.id.ff_title_content);
		tv_action_hint = (TextView) view.findViewById(R.id.ff_action);
		relativeLayout.setOnClickListener(this);
		tv_score = (TextView) view.findViewById(R.id.ff_score);
		tv_count = (TextView) view.findViewById(R.id.ff_count);
		bn_reStart = (Button) view.findViewById(R.id.ff_restart);
		bn_complete = (Button) view.findViewById(R.id.ff_complete);
		bn_reStart.setOnClickListener(this);
		bn_complete.setOnClickListener(this);
		tv_count.setText("次数：0/" + MAXCOUNT);
	}

	// set in/out circle visible;
	private void setCircleVisible(int visible) {
		iv_cirle_in.setVisibility(visible);
		iv_cirle_out.setVisibility(visible);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ff_iv_explanation:
			explanation();
			break;
		case R.id.ff_relativelayout:
			action();
			break;
		case R.id.ff_restart:
			restart();
			break;
		case R.id.ff_complete:
			complete();
			break;
		}
	}

	/**
	 * 显示 游戏说明对话框
	 */
	private void explanation() {
		showDialog();
	}

	/**
	 * 游戏的操作
	 */
	private void action() {
		switch (TAGCOUNT) {
		case 0:
			actionOne();
			break;
		case 1:
			actionTwo();
			break;
		case 2:
			actionThree();
			break;
		case 3:
			actionFour();
			break;
		}
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0x1:
				changeTheme(1);// 变成绿色
				timerUtils.startRecord();
				haveToGreen = true;
				break;
			case 0x2:
				showDialog();
				break;
			}
		}
	};

	// tagcount = 0
	private void actionOne() {
		changeTheme(0);
		TAGCOUNT++;
		double rand = Math.random();
		if (rand < 0.1)
			rand = rand + 0.2;
		long time = Long.valueOf((int) (5000 * rand));
		handler.sendEmptyMessageDelayed(0x1, time);
	}

	// tagcount = 1
	private void actionTwo() {
		if (haveToGreen) {// 是否变绿了
			TAGCOUNT++;
			timerUtils.stopRecord();
			curScore = timerUtils.getBetweenTime();// 获取期间的时间
			// reationV.add((int) curScore);// 将当前时间存储起来
			haveToGreen = false;
			changeTheme(THEME_BLUE);
			if (curScore >= TIME_OVER) {// 超时提醒
				showAlertDialog();
			} else {
				reationV.add((int) curScore);// 将当前时间存储起来
			}
		} else {
			changeTheme(THEME_SOON);// 给位太快主题
			handler.removeMessages(0x1);// 取消变绿
			TAGCOUNT = 3;
		}
	}

	// tagcount = 2
	private void actionThree() {
		TAGCOUNT = 0;
		count++;
		tv_score.setText("平均成绩：" + getAverageScore() + " ms");

		tv_count.setText("次数：" + count + "/" + MAXCOUNT);

		if (count < MAXCOUNT) {
			actionOne();
		} else {
			relativeLayout.setClickable(false);
			changeTheme(THEME_COMPLETE);

			bn_reStart.setVisibility(View.VISIBLE);
			bn_complete.setVisibility(View.VISIBLE);
		}
	}

	// tagcount = 3
	private void actionFour() {
		TAGCOUNT = 0;
		actionOne();
	}

	private void changeTheme(int THEME) {
		switch (THEME) {
		case THEME_READ:
			setCircleVisible(View.GONE);
			int red = getResources().getColor(R.color.red1);
			theme(". . .", "当颜色变绿时请立即点击", red);
			break;
		case THEME_GREEN:
			int green = getResources().getColor(R.color.green1);
			theme(". . .", "点击", green);
			break;
		case THEME_BLUE:
			int size = reationV.size()+1;
			int blue1 = getResources().getColor(R.color.blue1);
			theme("第" + size + "次的测试结果：", " " + curScore + " ms\n\n点击继续", blue1);

			break;
		case THEME_SOON:
			int blue2 = getResources().getColor(R.color.blue1);
			theme("", "太快啦，点击重新开始吧！\n\n点击继续", blue2);
			break;
		case THEME_COMPLETE:
			setCircleVisible(View.VISIBLE);
			int blue3 = getResources().getColor(R.color.blue1);
			tv_action_hint.setTextSize(25);
			theme("完成测试,平均成绩：", average + " ms", blue3);
			break;
		}
	}

	private void theme(String title_content, String action_content, int bgColor) {
		tv_title_content.setText(title_content);
		tv_action_hint.setText(action_content);
		relativeLayout.setBackgroundColor(bgColor);
	}

	/**
	 * 显示对话框
	 */
	private void showDialog() {
		new TextDialog(getActivity(), "", "");
	}

	/**
	 * 超时警告对话框
	 */
	private void showAlertDialog() {
		TextDialog alertDialog = new TextDialog(getActivity(), "超时提醒",
				"由于你长时间未操作，超时，需要您重新提取此次数据。");
		count--;
	}

	/**
	 * 得到平均分
	 * 
	 * @return
	 */
	private int getAverageScore() {
		average = 0;
		int size = reationV.size();
		if (size != 0) {
			for (int i = 0; i < size; i++) {
				average = average + reationV.get(i);
			}
			average = average / size;
		}
		return average;
	}

	/**
	 * 重新开始
	 */
	public void restart() {
		tv_action_hint.setTextSize(20);
		tv_score.setText("平均成绩：0 ms");
		tv_count.setText("次数：0/" + MAXCOUNT);
		count = 0;
		relativeLayout.setClickable(true);
		bn_reStart.setVisibility(View.GONE);
		bn_complete.setVisibility(View.GONE);
		reationV.clear();
		actionOne();

	}

	/**
	 * 完成
	 */
	public void complete() {

		StringBuilder sb = new StringBuilder();
		sb.append("反应速度：\n");
		for (int i = 0; i < reationV.size(); i++) {
			sb.append("v:" + reationV.get(i) + "\n");
		}
		sb.append("平均反应速度：" + getAverageScore() + "ms");

		// 数据暂存
		RecognizeActivity.rud.setUpSlidingCoordsList(upSlidingCoordsList);
		RecognizeActivity.rud.setUpSlidingDownSize(upSlidingDownSize);
		RecognizeActivity.rud.addBetweenTime(betweenTime);
		RecognizeActivity.rud.setReactionV(getAverageScore());
		clearAnim();
		Fragment fragment3 = new RecognizeFragmentThree();
		fm.beginTransaction().replace(R.id.recognize_frameLayout, fragment3)
				.commit();
	}

}

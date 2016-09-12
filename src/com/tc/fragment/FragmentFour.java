package com.tc.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tc.dialog.TextDialog;
import com.tc.utils.FileUtils;
import com.tc.utils.SharedPreferenceUtils;
import com.tc.utils.TimerUtils;
import com.tc.whoami.R;

/**
 * 测试反应速度
 * 
 * @author hgs
 *
 */
public class FragmentFour extends BaseFragment implements OnClickListener {
	private final int THEME_READ = 0;
	private final int THEME_GREEN = 1;
	private final int THEME_BLUE = 2;
	private final int THEME_SOON = 3;
	private final int THEME_COMPLETE = 4;
	private final long TIME_OVER = 1500;
	private ImageView iv_explanation;
	private TextView tv_title_content, tv_action_hint, tv_score, tv_count;
	private Button bn_reStart, bn_complete;
	private RelativeLayout relativeLayout;
	private int TAGCOUNT = 0;
	private boolean haveToGreen = false;
	private List<Integer> results = new ArrayList<Integer>();
	private TimerUtils timerUtils = new TimerUtils();
	private long curScore;
	private int average = 0;
	private int count = 0;
	private FragmentManager fm;
	private SharedPreferences preference;
	private String username;
	private ImageView iv_cirle_in, iv_cirle_out;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActivity().getActionBar().hide();
		fm = getActivity().getSupportFragmentManager();
		preference = getActivity().getSharedPreferences(
				SharedPreferenceUtils.NAME, Context.MODE_PRIVATE);
		username = preference.getString("username", "default");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_four, null);
		init(view);
		return view;
	}

	private void init(View view) {
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
		explanation();
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

	// 显示游戏说明对话框
	private void explanation() {
		// MessDialogFragment dialog =
		// MessDialogFragment.newInstance("Hello World");
		// dialog.show(fm, "EXPLANATION");
		showDialog();
	}

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
		case 4:
			actionFive();
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
			haveToGreen = false;
			changeTheme(THEME_BLUE);
			if (curScore >= TIME_OVER) {// 超时提醒
				showAlertDialog();
			} else {
				results.add((int) curScore);// 将当前时间存储起来
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
		tv_score.setText("平均成绩：" + getAverageScore() + " ms");
		count++;
		tv_count.setText("次数：" + count + "/5");
		if (count < 5) {
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

	// tagcount = 4
	private void actionFive() {

	}

	// 改变主题
	private void changeTheme(int THEME) {
		switch (THEME) {
		case THEME_READ:
			setCircleVisible(View.GONE);
			int red = getResources().getColor(R.color.red1);
			theme(". . .", "当颜色变绿时请点击", red);
			break;
		case THEME_GREEN:
			int green = getResources().getColor(R.color.green1);
			theme(". . .", "点击", green);
			break;
		case THEME_BLUE:
			int size = results.size()+1;
			int blue1 = getResources().getColor(R.color.blue1);
			theme("第" + size + "次的测试结果：", " " + curScore + " ms\n\n点击继续", blue1);
			break;
		case THEME_SOON:
			int blue2 = getResources().getColor(R.color.blue1);
			theme("", "太快了，点击重新开始吧！\n\n点击继续", blue2);
			break;
		case THEME_COMPLETE:
			setCircleVisible(View.VISIBLE);
			int blue3 = getResources().getColor(R.color.blue1);
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
		new TextDialog(getActivity(), "超时提醒", "由于你长时间未操作，超时，需要您重新提取此次数据。");
		count--;
	}

	private int getAverageScore() {
		average = 0;
		int size = results.size();
		if (size != 0) {
			for (int i = 0; i < size; i++) {
				average = average + results.get(i);
			}
			average = average / size;
		}
		return average;
	}

	public void restart() {
		tv_score.setText("平均成绩：0 ms");
		tv_count.setText("次数：0/5");
		count = 0;
		relativeLayout.setClickable(true);
		bn_reStart.setVisibility(View.GONE);
		bn_complete.setVisibility(View.GONE);
		results.clear();
		actionOne();
	}

	// 完成
	public void complete() {
		// getActivity().finish();
		// startActivity(new Intent(getActivity(), MainActivity.class));
		FragmentFive fragment5 = new FragmentFive();
		fm.beginTransaction().replace(R.id.frameLayout, fragment5).commit();
		StringBuilder sb = new StringBuilder();
		sb.append("反应速度：\n");
		for (int i = 0; i < results.size(); i++) {
			sb.append("v:" + results.get(i) + "\n");
		}
		sb.append("平均反应速度：" + getAverageScore() + "ms");
		FileUtils.writeFile(FileUtils.generatePath(0), FileUtils.TAG_NAME0,
				sb.toString(), username);

	}
}

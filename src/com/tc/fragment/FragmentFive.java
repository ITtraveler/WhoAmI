package com.tc.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.tc.dialog.ImageGuideDialog;
import com.tc.dialog.TextDialog;
import com.tc.utils.FileUtils;
import com.tc.utils.SharedPreferenceUtils;
import com.tc.whoami.R;

/**
 * 用户拇指长度提取，为了方面将提取拇指最大折痕到拇指与屏幕最长可触点的距离。提取数据次数为5次
 * 
 * @author hgs
 *
 */
public class FragmentFive extends BaseFragment implements OnClickListener,
		OnSeekBarChangeListener {
	private SeekBar seekBar;
	private TextView tv_value;
	private Button bn_confirm;
	private StringBuilder sb;
	private int curValue;
	private int count = 0;
	private FragmentManager fm;
	private SharedPreferences preference;
	private String username;
	private TextView tv_count;
	private ImageView iv_explanation;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sb = new StringBuilder();
		fm = getActivity().getSupportFragmentManager();
		preference = getActivity().getSharedPreferences(
				SharedPreferenceUtils.NAME, Context.MODE_PRIVATE);
		username = preference.getString("username", "default");
		showExplanationDialog();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_thumb_long, container,
				false);
		iv_explanation = (ImageView) view.findViewById(R.id.ftl_iv_explanation);
		seekBar = (SeekBar) view.findViewById(R.id.seekBar);
		tv_value = (TextView) view.findViewById(R.id.show_sb_value);
		tv_count = (TextView) view.findViewById(R.id.tv_tLong_count);
		bn_confirm = (Button) view.findViewById(R.id.bn_confirm);
		bn_confirm.setOnClickListener(this);
		iv_explanation.setOnClickListener(this);
		seekBar.setOnSeekBarChangeListener(this);
		return view;
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		tv_value.setText("当前值：" + progress);
		curValue = progress;
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {

	}

	//
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ftl_iv_explanation:
			actionExplanation();
			System.out.println("ok///////");
			break;
		case R.id.bn_confirm:
			actionConfirm();
			break;
		default:
			break;
		}

	}

	// 说明按钮
	private void actionExplanation() {
		showExplanationDialog();
	}
	private void showExplanationDialog(){
		new ImageGuideDialog(getActivity());
	}
	// 确认按钮
	private void actionConfirm() {
		if (count < 4) {// 5次拇指长度提取
			if (curValue > 10) {// 防止无效值,此处将10作为标准，否则值无效
				count++;
				tv_count.setText("" + count);
				sb.append("long" + count + ":" + curValue);
			} else {
				// MessDialogFragment dialog =
				// MessDialogFragment.newInstance("此次提取值无效，请重新提取！");
				// dialog.show(fm, "Dialog");
				new TextDialog(getActivity(), "错误提示", "此次提取值无效，请重新提取！");
			}
			if (count == 5) {
				bn_confirm.setText("下一项");
				seekBar.setVisibility(View.GONE);
			}
			seekBar.setProgress(0);

		} else {
			writeToFile();
			fm.beginTransaction().replace(R.id.frameLayout, new FragmentSix())
					.commit();
		}
	}

	private void writeToFile() {
		FileUtils.writeFile(FileUtils.generatePath(0), FileUtils.TAG_NAME0,
				"\n" + sb.toString(), username);
		sb.delete(0, sb.length());// 清空sb中的数据
	}
}

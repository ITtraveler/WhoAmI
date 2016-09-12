package com.tc.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.tc.dialog.MessDialogFragment;
import com.tc.whoami.R;
import com.tc.whoami.RecognizeActivity;

public class RecognizeFragmentThree extends BaseFragment implements
		OnClickListener, OnSeekBarChangeListener {
	private SeekBar seekBar;
	private TextView tv_value;
	private Button bn_confirm;
	private int curValue;
	private FragmentManager fm;
	private TextView tv_count;
	private TextView tv_hint;
	private ImageView iv_explanation;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		fm = getActivity().getSupportFragmentManager();
		showExplanationDialog();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_thumb_long, container,
				false);
		iv_explanation = (ImageView)view.findViewById(R.id.ftl_iv_explanation);
		seekBar = (SeekBar) view.findViewById(R.id.seekBar);
		tv_hint = (TextView) view.findViewById(R.id.tv_tLong_hint);
		tv_hint.setText("请使用左手的大拇指进行指距测量，拇指接近手掌的折痕与手机边缘对齐，点击拖动条。");
//		tv_hint.setVisibility(View.GONE);
		tv_value = (TextView) view.findViewById(R.id.show_sb_value);
		tv_count = (TextView) view.findViewById(R.id.tv_tLong_count);
		tv_count.setVisibility(View.INVISIBLE);
		bn_confirm = (Button) view.findViewById(R.id.bn_confirm);
		bn_confirm.setEnabled(false);
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
		bn_confirm.setEnabled(true);
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {

	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ftl_iv_explanation:
			actionExplanation();
			break;
		case R.id.bn_confirm:
			actionConfirm();
			break;
		default:
			break;
		}

	}

	private void actionConfirm() {
		if (curValue > 10) {
			RecognizeActivity.rud.setThumbLong(curValue);
			Fragment fragment4 = new RecognizeFragmentFour();
			fm.beginTransaction().replace(R.id.recognize_frameLayout, fragment4).commit();
		}else {
			MessDialogFragment dialog = MessDialogFragment.newInstance("此次提取值无效，请重新提取！");
			dialog.show(fm, "Dialog");
		}		
	}

	// 说明按钮
	private void actionExplanation() {
		showExplanationDialog();
	}
	private void showExplanationDialog(){
		new ImageGuideDialog(getActivity());
	}

}

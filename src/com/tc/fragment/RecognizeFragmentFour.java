package com.tc.fragment;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tc.utils.GenerateAnalysisResult;
import com.tc.whoami.R;
import com.tc.whoami.RecognizeActivity;

public class RecognizeFragmentFour extends BaseFragment implements
		OnClickListener, SensorEventListener {
	private FragmentManager fm;
	private ImageView iv_lu, iv_ld, iv_ru, iv_rd;
	private SensorManager sm;
	private StringBuilder sb = new StringBuilder();
	private String content;
	private float [] gXs = new float[4];
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		fm = getActivity().getSupportFragmentManager();
		// 使用传感器
		sm = (SensorManager) getActivity().getSystemService(
				getActivity().SENSOR_SERVICE);
		Sensor sensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);// 传感器类型设为加速传感器
		sm.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_custom_hand, container,
				false);
		init(view);
		return view;
	}

	private void init(View view) {
		iv_lu = (ImageView) view.findViewById(R.id.iv_click_lu);
		iv_ld = (ImageView) view.findViewById(R.id.iv_click_ld);
		iv_ru = (ImageView) view.findViewById(R.id.iv_click_ru);
		iv_rd = (ImageView) view.findViewById(R.id.iv_click_rd);
		iv_lu.setOnClickListener(this);
		iv_ld.setOnClickListener(this);
		iv_ru.setOnClickListener(this);
		iv_rd.setOnClickListener(this);
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			float x = event.values[0];
			float y = event.values[1];
			float z = event.values[2];
			content = "" + x;
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {

	}

	@Override
	public void onClick(View v) {
		int viewId = v.getId();
		switch (viewId) {
		case R.id.iv_click_lu:
			iv_lu.setVisibility(View.GONE);
			iv_ru.setVisibility(View.VISIBLE);
			gXs[0] = Float.valueOf(content);
			break;
		case R.id.iv_click_ru:
			iv_ru.setVisibility(View.GONE);
			iv_ld.setVisibility(View.VISIBLE);
			gXs[1] = Float.valueOf(content);
			break;
		case R.id.iv_click_ld:
			iv_ld.setVisibility(View.GONE);
			iv_rd.setVisibility(View.VISIBLE);
			gXs[2] = Float.valueOf(content);
			break;
		
		case R.id.iv_click_rd:
			iv_rd.setVisibility(View.GONE);
			// iv_ru.setVisibility(View.VISIBLE);
			gXs[3] = Float.valueOf(content);
			complete();
			break;
		}
	}

	/**
	 * 数据收集完成
	 */
	private void complete() {
		new GenerateAnalysisResult().dealGDate(sb, gXs);
		RecognizeActivity.rud.setGesture(sb.toString());
		Fragment fragmentScore = new RecognizeFragmentScore();
		fm.beginTransaction().replace(R.id.recognize_frameLayout, fragmentScore).commit();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		sm.unregisterListener(this);// 注销加速器的监听器3
	}
}

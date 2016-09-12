package com.tc.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tc.utils.DatabaseUtils;
import com.tc.utils.FileUtils;
import com.tc.utils.GenerateAnalysisResult;
import com.tc.utils.SharedPreferenceUtils;
import com.tc.whoami.R;

/**
 * ��������˫���жϣ�ȡ��ͨ�������켣�ж�������
 * 
 * @author hgs
 *
 */
public class FragmentSix extends BaseFragment implements SensorEventListener,
		OnClickListener {
	private ImageView iv_lu, iv_ld, iv_ru, iv_rd;
	private TextView show_hint;
	private SensorManager sm;
	private StringBuilder sb;
	private String content;
	private SharedPreferences preference;
	private String username;
	private DatabaseUtils dbUtils;
	private String psw;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		preference = getActivity().getSharedPreferences(
				SharedPreferenceUtils.NAME, Context.MODE_PRIVATE);
		username = preference.getString("username", "default");
		psw = preference.getString("password", "123456");
		// ʹ�ô�����
		sm = (SensorManager) getActivity().getSystemService(
				getActivity().SENSOR_SERVICE);
		Sensor sensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);// ������������Ϊ���ٴ�����
		sm.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI);
		dbUtils = new DatabaseUtils(getActivity());
		sb = new StringBuilder();
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
		show_hint = (TextView) view.findViewById(R.id.tv_hint);
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
			content = ""+x;
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
			sb.append("�жϹ����֣�luX:"+content);
			break;
		case R.id.iv_click_ld:
			iv_ld.setVisibility(View.GONE);
			iv_rd.setVisibility(View.VISIBLE);
			sb.append("ldX:"+content);
			break;
		case R.id.iv_click_ru:
			iv_ru.setVisibility(View.GONE);
			iv_ld.setVisibility(View.VISIBLE);
			sb.append("ruX:"+content);
			break;
		case R.id.iv_click_rd:
			iv_rd.setVisibility(View.GONE);
			sb.append("rdX:"+content);
			//iv_ru.setVisibility(View.VISIBLE);
			System.out.println(sb.toString());
			complete();
			handler.sendEmptyMessageDelayed(0x1, 2000);
			break;
		}
	}
	
	Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(msg.what == 0x1){
				getActivity().finish();
			}
		}
	};
	/**
	 * ���������ȡ
	 */
	private void complete() {
		//show_complete.setVisibility(View.VISIBLE);//��ʾ�����ʾ
		show_hint.setText("�����ռ����");
		writeToFile();
		new GenerateAnalysisResult(username);//���ɷ������浥
		dbUtils.openDatabase();
		dbUtils.insert("insert into user(username,password,behaviourFileName,reportFileName)"
				+ "values('"
				+ username
				+ "','"
				+ psw
				+ "','behaviour_"
				+ username + "','report_" + username + "')");
		dbUtils.closeDB();
	}

	private void writeToFile() {
		FileUtils.writeFile(FileUtils.generatePath(0), FileUtils.TAG_NAME0,
				"\n"+sb.toString(), username);
		sb.delete(0, sb.length());// ���sb�е�����
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		sm.unregisterListener(this);//ע���������ļ�����3
	}
}

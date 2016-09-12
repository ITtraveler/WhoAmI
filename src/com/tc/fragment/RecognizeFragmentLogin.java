package com.tc.fragment;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.tc.utils.DatabaseUtils;
import com.tc.utils.User;
import com.tc.whoami.LoginResultActivity;
import com.tc.whoami.R;

@SuppressLint("HandlerLeak")
public class RecognizeFragmentLogin extends BaseFragment implements
		OnClickListener {
	private ImageView iv_eye;
	private Button bn_login;
	private EditText et_psw;
	private boolean isEye = false;
	private ImageView iv_back;
	private EditText et_username;
	private DatabaseUtils dbUtils;
	protected List<User> userList;
	private String username;
	private String password;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		username = getArguments().getString("username");
		password = getArguments().getString("password");
		dbUtils = new DatabaseUtils(getActivity());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.login_layout, container, false);
		init(view);
		return view;
	}

	private void init(View view) {
		et_username = (EditText) view.findViewById(R.id.username);
		et_psw = (EditText) view.findViewById(R.id.psw);
		iv_back = (ImageView) view.findViewById(R.id.back);
		iv_eye = (ImageView) view.findViewById(R.id.eye_psw);
		bn_login = (Button) view.findViewById(R.id.bn_login);
		iv_back.setOnClickListener(this);
		iv_eye.setOnClickListener(this);
		bn_login.setOnClickListener(this);
		
		et_username.setText(username);
		et_psw.setText(password);
		// RecognizeActivity.rud.getAvgBetweenTime();
		/*
		 * dialog = new HoldOnDialog(getActivity()).init(); new Thread(new
		 * Runnable() {
		 * 
		 * @Override public void run() { FeatureMatchUtil fmu = new
		 * FeatureMatchUtil( RecognizeActivity.rud, getActivity());
		 * fmu.reportMatchFilter(); userList = fmu.getOptimalResult(); int
		 * lenght = userList.size(); if (lenght > 1) {// ����õ��Ľ������2�������Ի��� //
		 * listDialog = new ListDialog(getActivity(), userList);
		 * handler.sendEmptyMessage(0x2); // timer.schedule(timerTask,100); }
		 * else if (lenght == 1) { et_username.post(new Runnable() {
		 * 
		 * @Override public void run() { User user = userList.get(0);
		 * et_username.setText(user.getName()); et_psw.setText(user.getPsw()); }
		 * }); } else if (lenght == 0) { System.out.println("û���ҵ�ƥ����û���");
		 * handler.sendEmptyMessage(0x3); } // fmu.printCurList();
		 * handler.sendEmptyMessage(0X1); } }).start();
		 */
	}

	/*
	 * Handler handler = new Handler() {
	 * 
	 * @Override public void handleMessage(Message msg) {
	 * super.handleMessage(msg); if (msg.what == 0x1) { dialog.cancel();
	 * RecognizeActivity.rud.init();// ��̬rud ���³�ʼ�� } else if (msg.what == 0x2) {
	 * listDialog = new ListDialog(getContext(), userList, et_username);
	 * listDialog.init(); } else if (msg.what == 0x3) {
	 * Toast.makeText(getContext(), "û���ҵ�ƥ����û���", Toast.LENGTH_SHORT) .show(); }
	 * } };
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			getActivity().finish();
			break;
		case R.id.eye_psw:// ����ɼ��Դ���
			if (!isEye) {// ������ʱ
				et_psw.setInputType(InputType.TYPE_CLASS_TEXT);
				isEye = true;
			} else {
				et_psw.setInputType(InputType.TYPE_CLASS_TEXT
						| InputType.TYPE_TEXT_VARIATION_PASSWORD);
				isEye = false;
			}
			break;
		case R.id.bn_login:
			String username = et_username.getText().toString().trim();
			String psw = et_psw.getText().toString().trim();
			login(username, psw);
			break;
		}

	}

	// ���봦��
	private void login(String username, String psw) {
		dbUtils.openDatabase();
		Cursor cursor = dbUtils.query("select username,password from user");
		boolean isExit = false;
		while (cursor.moveToNext()) {
			String u = cursor.getString(0);
			if (username.equals(u)) {
				String p = cursor.getString(1);
				isExit = true;
				if (psw.endsWith(p)) {
					Intent intent = new Intent(this.getActivity(),
							LoginResultActivity.class);
					startActivity(intent);
				} else {
					Toast.makeText(getActivity(), "�������", Toast.LENGTH_SHORT)
							.show();
				}
				break;
			}
		}
		cursor.close();
		if (!isExit) {
			Toast.makeText(getActivity(), "�˺Ų����ڡ�", Toast.LENGTH_SHORT).show();
		}
		dbUtils.closeDB();
	}

}

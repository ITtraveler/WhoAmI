package com.tc.whoami;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.tc.utils.FileUtils;
import com.tc.utils.SharedPreferenceUtils;
import com.tc.utils.StringUtils;

public class RegistActivity extends BaseActivity {
	private EditText et_username, et_psw1, et_psw2;
	private SharedPreferenceUtils spUtils;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.regist_layout);
		spUtils = new SharedPreferenceUtils(this);
		init();
	}

	private void init() {
		et_username = (EditText) findViewById(R.id.username);
		et_psw1 = (EditText) findViewById(R.id.et_psw);
		et_psw2 = (EditText) findViewById(R.id.et_psw2);
	}

	public void bn_hand(View v) {
		// String name[] = FileUtils.getAllFileName(FileUtils.BEHAVIOURPATH);
		 //System.out.println(name[0]);
		String username = et_username.getText().toString().trim();
		String psw1 = et_psw1.getText().toString().trim();
		String psw2 = et_psw2.getText().toString().trim();
		FileUtils.createFile(FileUtils.generatePath(0));
		if (!FileUtils.chackFileExist(FileUtils.generatePath(0),
				FileUtils.TAG_NAME0, username)) {// 文件不存在时
			if (username.length() < 1 || psw1.length() < 1 || psw2.length() < 1) {
				toast("请填写完整信息！");
			} else {
				if (StringUtils.checkLong(username, 3, 15)) {
					if (StringUtils.compare(psw1, psw2)) {
						spUtils.buildPreferences(SharedPreferenceUtils.NAME,
								Context.MODE_PRIVATE);
						spUtils.editor("username", username);
						spUtils.editor("password", psw1);
						startActivity(RegistActivity.this,
								GuideTipsActivity.class);
						this.finish();
					} else {
						toast("密码不匹配，请检测！");
					}
				} else {
					toast("用户名请保持在3-15个字符。");
				}
			}
		} else {
			toast("用户已存在请重新命名");
		}
	}

	public void back(View v) {
		this.finish();
	}
}

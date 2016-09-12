package com.tc.whoami;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.tc.dialog.ImageGuideDialog;
import com.tc.utils.DatabaseUtils;
import com.tc.utils.SharedPreferenceUtils;

public class MainActivity extends BaseActivity {
	DatabaseUtils dbUtils;
	SharedPreferenceUtils spUtils;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		TextView tv = (TextView)findViewById(R.id.mark1);
		spUtils = new SharedPreferenceUtils(this);
		spUtils.buildPreferences("isFistUse", Context.MODE_PRIVATE);
		dbUtils = new DatabaseUtils(this);
		initDatabase();
		initView();
		
	}

	private void initView() {
		ImageView iv_mark = (ImageView) findViewById(R.id.main_mark);
		iv_mark.startAnimation(AnimationUtils.loadAnimation(this,
				R.anim.logo_rotate));
		ImageView iv_bg = (ImageView) findViewById(R.id.main_bg);
		iv_bg.startAnimation(AnimationUtils.loadAnimation(this, R.anim.logo_bg));
	}

	private void initDatabase() {
		// 判断是否为第一次使用应用，是则对数据库进行初始化
		boolean isFirst = spUtils.getBoolean(SharedPreferenceUtils.Key1);
		if (isFirst) {
			dbUtils.openDatabase();
			dbUtils.create(DatabaseUtils.SQL_CT);
			dbUtils.closeDB();
			spUtils.editor(SharedPreferenceUtils.Key1, false);
		}
	}

	public void regist(View v) {
		startActivity(MainActivity.this, RegistActivity.class);
	}

	public void recognize(View v) {
		startActivity(MainActivity.this, RecognizeActivity.class);
	}

	public void account_manager(View v) {
		startActivity(MainActivity.this, AccountManagerActivity.class);
	}
}

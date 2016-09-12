package com.tc.whoami;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;

import com.tc.fragment.FragmentOne;

public class ExtractActivity extends FragmentActivity {
	private FragmentManager fm;
	private FragmentTransaction transaction;
	private Fragment fragment1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.extract_layout);
		getActionBar().hide();
		init();
	}

	private void init() {
		fragment1 = new FragmentOne();
		fm = getSupportFragmentManager();
		transaction = fm.beginTransaction();
		transaction.add(R.id.frameLayout, fragment1);
		transaction.addToBackStack(null);
		transaction.show(fragment1);
		transaction.commit();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			// 这里重写返回键
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}

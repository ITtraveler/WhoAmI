package com.tc.whoami;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;

import com.tc.fragment.RecognizeFragmentOne;
import com.tc.utils.RecognizeUserData;

public class RecognizeActivity extends FragmentActivity {
	private Fragment fragment1;
	private FragmentManager fm;
	private FragmentTransaction transaction;
	public static RecognizeUserData rud = new RecognizeUserData();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recognize_layout);
		getActionBar().hide();
		// frameLayout = (FrameLayout) findViewById(R.id.recognize_frameLayout);
		rud.init();
		init();
	}

	private void init() {
		fragment1 = new RecognizeFragmentOne();
		fm = getSupportFragmentManager();
		transaction = fm.beginTransaction();
		transaction.add(R.id.recognize_frameLayout, fragment1);
		transaction.addToBackStack(null);
		transaction.show(fragment1);
		transaction.commit();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}

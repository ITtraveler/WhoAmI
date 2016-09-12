package com.tc.whoami;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class BaseActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getActionBar().hide();
	}

	public void toast(String s) {
		Toast.makeText(this, s, 500).show();
	}

	public void startActivity(Context context, Class clas) {
		Intent intent = new Intent(context, clas);
		startActivity(intent);
	}
	
	@Override
	public void finish() {
		super.finish();
	}
	
}

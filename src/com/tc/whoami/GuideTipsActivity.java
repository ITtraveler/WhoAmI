package com.tc.whoami;

import android.os.Bundle;
import android.view.View;

public class GuideTipsActivity extends BaseActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.guide_tips_layout);
	}

	public void bn_start(View v) {
		this.finish();
		startActivity(GuideTipsActivity.this, ExtractActivity.class);
	}

	public void back(View v) {
		this.finish();
	}
}

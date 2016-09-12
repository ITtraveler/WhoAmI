package com.tc.dialog;

import android.app.Dialog;
import android.content.Context;

public class BaseDialog implements DialogFace{
	private Context context;

	public BaseDialog(Context context) {
		this.context = context;
	}
	
	@Override
	public Dialog init() {
		return null;
	}

	@Override
	public boolean haveCancel() {
		return false;
	}
	
	public Context getContext() {
		return context;
	}
}

package com.tc.dialog;

import com.tc.whoami.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;

public class ImageGuideDialog extends BaseDialog{

	public ImageGuideDialog(Context context) {
		super(context);
		init();
	}
	@Override
	public Dialog init() {
		View view = LayoutInflater.from(getContext()).inflate(
				R.layout.dialog_image_layout, null);
		final Dialog dialog = new AlertDialog.Builder(getContext()).create();
		// dialog.setTitle("给自己起个名字吧！");
		dialog.show();
		dialog.getWindow().setContentView(view);
		// dialog.setCancelable(false);// 是对话框不能按返回键取消
		dialog.setCanceledOnTouchOutside(false);// 使对话框不能按旁边取消
		dialog.getWindow().clearFlags(
				WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

		view.findViewById(R.id.dialog_bn).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.cancel();
					}
				});
		return dialog;
	}
}

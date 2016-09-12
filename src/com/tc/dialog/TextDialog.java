package com.tc.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.TextView;

import com.tc.whoami.R;

public class TextDialog extends BaseDialog {

	private Context context;
	private String title;
	private String content;
	private boolean haveCancel = false;
	
	public TextDialog(Context context, String title, String content) {
		super(context);
		this.context = context;
		this.title = title;
		this.content = content;
		init();
	}

	@Override
	public Dialog init() {
		View li = LayoutInflater.from(context).inflate(
				R.layout.explanation_dialog_layout, null);
		if (!title.isEmpty() && title.trim().length() > 0) {
			TextView tv_title = (TextView) li.findViewById(R.id.dialog_title);
			tv_title.setText(title);
		}
		if (!content.isEmpty() && content.trim().length() > 0) {
			TextView tv = (TextView) li
					.findViewById(R.id.dialog_explanation_content);
			tv.setText(content);
		}

		final Dialog dialog = new AlertDialog.Builder(context).create();
		// dialog.setTitle("给自己起个名字吧！");
		dialog.show();
		dialog.getWindow().setContentView(li);
		// dialog.setCancelable(false);// 是对话框不能按返回键取消
		dialog.setCanceledOnTouchOutside(false);// 使对话框不能按旁边取消
		dialog.getWindow().clearFlags(
				WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

		li.findViewById(R.id.dialog_bn_ok).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.cancel();
						haveCancel = true;
					}
				});
		return dialog;
	}
	
	@Override
	public boolean haveCancel() {
		return haveCancel;
	}
}

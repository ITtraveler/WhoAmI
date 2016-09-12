package com.tc.dialog;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.tc.whoami.R;

public class HoldOnDialog extends BaseDialog {
	
	public HoldOnDialog(Context context) {
		super(context);
	}

	@Override
	public Dialog init() {
		View li = LayoutInflater.from(getContext()).inflate(
				R.layout.dialog_holdon_layout, null);
		Dialog dialog = new AlertDialog.Builder(getContext()).create();
		dialog.show();
		dialog.getWindow().setContentView(li);
		dialog.setCancelable(false);// 是对话框不能按返回键取消
		dialog.setCanceledOnTouchOutside(false);// 使对话框不能按旁边取消
		dialog.getWindow().clearFlags(
				WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
		ImageView iv = (ImageView) li.findViewById(R.id.iv_rotate);
		AnimatorSet set = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(),
				R.animator.animation_rotate);
		set.setTarget(iv);
		set.start();
		return dialog;
	}
}

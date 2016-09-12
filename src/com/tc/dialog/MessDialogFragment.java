package com.tc.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.tc.whoami.R;

public class MessDialogFragment extends DialogFragment implements
		android.content.DialogInterface.OnClickListener {
	
	private static String message;
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		super.onCreateDialog(savedInstanceState);
		View view = LayoutInflater.from(getActivity()).inflate(
				R.layout.dialog_message, null);
		TextView mess = (TextView)view.findViewById(R.id.dialog_mess);
		mess.setText(""+message);
		Dialog dialog = new AlertDialog.Builder(getActivity()).setView(view)
		.setTitle("贴心小提示").setNegativeButton("确定", this).show();
		dialog.setCanceledOnTouchOutside(true);//取消边缘屏幕取消dialog
		return dialog;
	}

	public static MessDialogFragment newInstance(String content){
		message = content;
		return new MessDialogFragment();
	}
	@Override
	public void onClick(DialogInterface dialog, int which) {
		dialog.cancel();
	}

}

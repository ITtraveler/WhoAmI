package com.tc.fragment;

import com.tc.whoami.R;

import android.gesture.Gesture;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * �ּ�������ȡ ���ѷ���
 * 
 * @author hgs
 *
 */

public class FragmentFiveOld extends BaseFragment implements OnGesturePerformedListener{

	private GestureOverlayView gov;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.gesture_layout, container, false);
		init(v);
		return v;
	}

	private void init(View v) {
		gov = (GestureOverlayView)v.findViewById(R.id.gestureView);
		gov.addOnGesturePerformedListener(this);
	}

	//�����ƻ��ƽ���ʱ��
	@Override
	public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
		
		
	}
}

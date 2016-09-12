package com.tc.fragment;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tc.utils.FileUtils;
import com.tc.utils.SharedPreferenceUtils;
import com.tc.whoami.R;

/**
 * 接触面积特征提取
 * 
 * @author hgs
 *
 */
public class FragmentThree extends BaseFragment implements OnTouchListener {
	private float maxSize = 0;
	private SharedPreferences preference;
	private String username;
	private ImageView touch_point;
	private Animator animator_breathing;
	private int count = 0;
	private TextView show_count, tv_hint;
	private Button bn_next;
	private FragmentManager fm;
	private FragmentFour fragment4;
	private boolean canTouch = true;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		fm = getActivity().getSupportFragmentManager();
		preference = getActivity().getSharedPreferences(
				SharedPreferenceUtils.NAME, Context.MODE_PRIVATE);
		username = preference.getString("username", "default");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_three, null);
		init(view);
		return view;
	}

	private void init(View view) {
		touch_point = (ImageView) view.findViewById(R.id.iv_touch_view);
		touch_point.setOnTouchListener(this);
		animator_breathing = AnimatorInflater.loadAnimator(getActivity(),
				R.animator.animator_breathing);
		animator_breathing.setTarget(touch_point);

		show_count = (TextView) view.findViewById(R.id.tv_count);
		tv_hint = (TextView) view.findViewById(R.id.tv_hint);
		bn_next = (Button) view.findViewById(R.id.bn_next);
		bn_next.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				fragment4 = new FragmentFour();
				fm.beginTransaction().replace(R.id.frameLayout, fragment4)
						.commit();
			}
		});
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (canTouch) {
			float size = event.getSize(0);// 得到接触大小
			if (size > maxSize) {
				maxSize = size;// 记录最大接触大小
			}
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				action_down();
				System.out.println("down");
				break;
			case MotionEvent.ACTION_UP:
				System.out.println("up");
				action_up();
				canTouch = false;
				handler.sendEmptyMessageDelayed(0x1, 1000);//延迟发送
				break;
			default:
				break;
			}
			
		}
		return true;// ImageView要返回true，up等才有效
	}

	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.what == 0x1){
				canTouch = true;//设置可点击
			}
		};
	};
	private void action_down() {
		animator_breathing.start();
	}

	/**
	 * 当手拿起来时记录接触最大的面积
	 */
	private void action_up() {
		if (count < 9) {
			FileUtils.writeFile(FileUtils.generatePath(0), FileUtils.TAG_NAME0,
					"maxSize:" + maxSize + "\n", username);
			maxSize = 0;// 使maxSize初始化
			animator_breathing.cancel();
			show_count.setText(++count + "");
		} else {
			tv_hint.setVisibility(View.GONE);
			show_count.setText(10 + "");
			bn_next.setVisibility(View.VISIBLE);
		}
	}
}

package com.tc.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tc.dialog.HoldOnDialog;
import com.tc.utils.FeatureMatchUtil;
import com.tc.utils.User;
import com.tc.whoami.R;
import com.tc.whoami.RecognizeActivity;
/**
 * 展示识别成绩
 * @author hgs
 *
 */
public class RecognizeFragmentScore extends BaseFragment {
	private ListView list;
	private FragmentManager fm;
	private int OPTIMALSCORE = 75;// 110总分，75分作为优秀分
	private List<User> lu ;
	private MyAdapter mAdapter;
	private Dialog dialog;
	private TextView tv_recommend_user;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		fm = getActivity().getSupportFragmentManager();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.recognize_result_layout,
				container, false);
		init(view);
		return view;
	}

	private void init(View view) {
		TextView title = (TextView) view.findViewById(R.id.title);
		title.setText("分析结果");
		// 左上角返回键
		ImageView iv_back = (ImageView) view.findViewById(R.id.back);
		iv_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getActivity().finish();
			}
		});

		list = (ListView) view.findViewById(R.id.recognizeResult_list);
		// 启动等待对话框
		dialog = new HoldOnDialog(getContext()).init();
		// 在新线程中处理耗时的匹配任务
		new Thread(new Runnable() {

			@Override
			public void run() {
				/**继续数据匹配*/
				FeatureMatchUtil fmu = new FeatureMatchUtil(
						RecognizeActivity.rud, getActivity());
				
				fmu.reportMatchFilter();
				lu = fmu.getOptimalResult();
				handler.sendEmptyMessage(0x1);// 通知任务完成
			}
		}).start();

		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				User user = lu.get(position);
				String username = user.getName();
				String password = user.getPsw();
				System.out.println("psw：" + password);
				int score = user.getScore();
				RecognizeFragmentLogin four = new RecognizeFragmentLogin();
				Bundle bundle = new Bundle();
				bundle.putString("username", username);
				if (score >= OPTIMALSCORE) {// 如果达到一定分值（75）则给出密码。
					bundle.putString("password", password);
				}
				four.setArguments(bundle);
				fm.beginTransaction().replace(R.id.recognize_frameLayout, four)
						.commit();
			}

		});
		// 最优用户，推荐
		tv_recommend_user = (TextView) view.findViewById(R.id.recommend_user);

	}

	// 推荐用户设置
	private void recommendUser(TextView tv_recommend_user) {
		if (lu.size() >= 1 && lu.get(0).getScore() >= OPTIMALSCORE) {// 多个用户时
			tv_recommend_user.setVisibility(View.VISIBLE);
			tv_recommend_user.setText("推荐用户：" + lu.get(0).getName());
			tv_recommend_user.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					RecognizeFragmentLogin four = new RecognizeFragmentLogin();
					Bundle bundle = new Bundle();
					bundle.putString("username", lu.get(0).getName());
					bundle.putString("password", lu.get(0).getPsw());
					four.setArguments(bundle);
					fm.beginTransaction()
							.replace(R.id.recognize_frameLayout, four).commit();
				}
			});
		}
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 0x1) {
				if (lu.size() == 0) {
					Toast.makeText(getActivity(), "没能找到与您匹配的用户，您可能是陌生人。",
							Toast.LENGTH_LONG).show();
					dialog.cancel();
				} else { 
					mAdapter = new MyAdapter(lu);
					list.setAdapter(mAdapter);
					recommendUser(tv_recommend_user);
					dialog.cancel();
					RecognizeActivity.rud.init();
				}
			}
		}
	};

	// 自定义适配器。
	class MyAdapter extends BaseAdapter {
		private List<User> lu;
		ViewHolder viewHolder;

		MyAdapter(List<User> lu) {
			this.lu = lu;
		}

		@Override
		public int getCount() {
			return lu.size();
		}

		@Override
		public Object getItem(int position) {
			return lu.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				viewHolder = new ViewHolder();
				convertView = LayoutInflater.from(getActivity()).inflate(
						R.layout.recognize_list_layout, parent, false);
				viewHolder.username = (TextView) convertView
						.findViewById(R.id.list_username);
				viewHolder.score = (TextView) convertView
						.findViewById(R.id.list_score);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			viewHolder.username.setText(lu.get(position).getName());
			viewHolder.score.setText(lu.get(position).getScore() + "");
			return convertView;
		}

		class ViewHolder {
			TextView username;
			TextView score;
		}
	}
}

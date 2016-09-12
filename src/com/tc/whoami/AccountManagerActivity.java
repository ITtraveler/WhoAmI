package com.tc.whoami;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.TextView;

import com.tc.utils.DatabaseUtils;
import com.tc.utils.FileUtils;
import com.tc.utils.User;

public class AccountManagerActivity extends BaseActivity {
	private List<User> userList = new ArrayList<User>();
	private List<String> usernameList = new ArrayList<String>();
	private DatabaseUtils dbUtils;
	private MyAdapter mAdapter1;
	private ImageView iv_back;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.account_manager_layout);
		getActionBar().hide();
		dbUtils = new DatabaseUtils(this);
		init();

	}

	private void init() {

		TextView tv_title = (TextView) findViewById(R.id.title);
		tv_title.setText("账号管理");
		iv_back = (ImageView) findViewById(R.id.back);
		iv_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		initDB();
		ListView list = (ListView) findViewById(R.id.am_listView);
		mAdapter1 = new MyAdapter(usernameList);
		list.setAdapter(mAdapter1);
		list.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				myPopMenu(view, position);
				return true;
			}
		});
	}

	// 初始化数据库
	private void initDB() {
		dbUtils.openDatabase();
		Cursor c = dbUtils.query("select username,password from user");
		while (c.moveToNext()) {
			userList.add(new User(c.getString(0), c.getString(1)));
			usernameList.add(c.getString(0));
		}
		c.close();
	}

	// 弹出菜单
	public void myPopMenu(View v, final int position) {
		PopupMenu p = new PopupMenu(this, v);
		p.getMenuInflater().inflate(R.menu.menu, p.getMenu());
		p.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				int id = item.getItemId();
				switch (id) {
				case R.id.del:// 删除操作
					String username = usernameList.get(position);
					dbUtils.delete("delete from user where username = '"
							+ username + "'");// 从数据库中删除
					userList.remove(position);// 内中移除
					usernameList.remove(position);
					handler.sendEmptyMessage(0x1);// 更新listView
					FileUtils.deleteFile(FileUtils.BEHAVIOURPATH,
							FileUtils.TAG_NAME0 + username);// 删除behavior目录文件
					FileUtils.deleteFile(FileUtils.REPORTPATH,
							FileUtils.TAG_NAME1 + username);// 删除report目录文件
					break;
				}
				return true;
			}
		});
		p.show();
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 0x1) {
				mAdapter1.notifyDataSetChanged();
			}
		}
	};

	@Override
	public void finish() {
		super.finish();
		dbUtils.closeDB();
	}

	// 自定义适配器
	class MyAdapter extends BaseAdapter {
		List<String> list;
		ViewHolder viewHolder;

		MyAdapter(List<String> list) {
			this.list = list;
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				viewHolder = new ViewHolder();
				convertView = getLayoutInflater().inflate(
						R.layout.simple_list_layout2, null);
				viewHolder.tv = (TextView) convertView
						.findViewById(R.id.list_content);
				viewHolder.tv.setTextColor(Color.WHITE);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			viewHolder.tv.setText(list.get(position).toString());
			return convertView;
		}

		class ViewHolder {
			public TextView tv;
		}
	}
}

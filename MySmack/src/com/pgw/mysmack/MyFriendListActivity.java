package com.pgw.mysmack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterGroup;

import com.pgw.mysmack.utils.XmppTool;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * ∫√”—¡–±Ì
 * @author tz
 *
 */
public class MyFriendListActivity extends Activity implements OnItemClickListener {
	private ListView lv_friend_list;
	private List<RosterEntry> friendList=new ArrayList<RosterEntry>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_friend_list);
		initView();
		initData();
	}
	private void initData() {
		Collection<RosterGroup> groups = XmppTool.getConn().getRoster().getGroups();
		for (RosterGroup rosterGroup : groups) {
			Collection<RosterEntry> entries = rosterGroup.getEntries();
			for (RosterEntry rosterEntry : entries) {
				friendList.add(rosterEntry);
			}
		}
		
		lv_friend_list.setAdapter(new MyFriendListAdapter());	
		
	}
	private void initView() {
		lv_friend_list=(ListView) findViewById(R.id.friend_list);
		lv_friend_list.setOnItemClickListener(this);
	}
	
	class MyFriendListAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return friendList.size();
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(int postion, View view, ViewGroup arg2) {
			if (view == null) {
				view=View.inflate(getApplicationContext(), R.layout.friend_list_item, null);
				ViewHodler vh=new ViewHodler();
				vh.iv_icon=(ImageView) view.findViewById(R.id.icon);
				vh.tv_name=(TextView) view.findViewById(R.id.username);
				view.setTag(vh);
			}
			ViewHodler vh = (ViewHodler) view.getTag();
			vh.tv_name.setText(friendList.get(postion).getName());
			
			return view;
		}
		
	}
	
	  class ViewHodler{
		 public	 ImageView iv_icon;
		 public	 TextView tv_name;
		}

	@Override
	public void onItemClick(AdapterView<?> adapter, View view, int postion, long arg3) {
		RosterEntry rosterEntry = friendList.get(postion);
		Intent intent =new Intent(MyFriendListActivity.this, ChatActivity.class);
		intent.putExtra("name", rosterEntry.getName());
		intent.putExtra("user", rosterEntry.getUser());
		startActivity(intent);
	}
}

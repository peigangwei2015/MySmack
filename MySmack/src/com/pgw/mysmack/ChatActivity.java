package com.pgw.mysmack;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.pgw.mysmack.utils.XmppTool;

public class ChatActivity extends Activity implements OnClickListener,
		ChatManagerListener, MessageListener {

	private String user;
	private String name;
	private TextView tv_title;
	private TextView tv_show;
	private EditText et_in_msg;
	private Button bt_send;
	private ChatManager chatManager;
	private Chat chat;

	private Handler handler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			String strMsg = msg.getData().getString("msg");
			String strFrom = msg.getData().getString("from");
			strFrom=strFrom.substring(0, strFrom.indexOf("@"));
			tv_show.setText(tv_show.getText() + strFrom + " : " + strMsg
					+ "\r\n");
		};
	};
	private SharedPreferences sp ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);		 sp =getSharedPreferences("msg", Context.MODE_PRIVATE);
		name = getIntent().getExtras().getString("name");
		user = getIntent().getExtras().getString("user");
		initView();
		initChat();

	}

	private void initChat() {
		chatManager = XmppTool.getConn().getChatManager();
		chat = chatManager.createChat(user, null);
		chatManager.addChatListener(this);
	}

	private void initView() {
		// TODO Auto-generated method stub
		tv_title = (TextView) findViewById(R.id.title);
		tv_title.setText("与 " + name + " 聊天");
		tv_show = (TextView) findViewById(R.id.show);
		et_in_msg = (EditText) findViewById(R.id.in_msg);
		bt_send = (Button) findViewById(R.id.send);
		bt_send.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.send:
			sendMsg();
			break;
		}
	}
	@Override
	protected void onStart() {
		super.onStart();
		tv_show.setText(sp.getString(user, ""));
		
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		Editor edit = sp.edit();
		edit.putString(user, tv_show.getText().toString());
		edit.commit();
	}

	/**
	 * 发送信息
	 */
	private void sendMsg() {
		String msg = et_in_msg.getText().toString();
		if (TextUtils.isEmpty(msg)) {
			Toast.makeText(getApplicationContext(), "对不起，不能发送空消息！",
					Toast.LENGTH_SHORT).show();
			return;
		}

		try {
			chat.sendMessage(msg);
			et_in_msg.setText("");
			tv_show.setText(tv_show.getText() + "我：" + msg + "\r\n");
		} catch (XMPPException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void chatCreated(Chat chat, boolean arg1) {
		chat.addMessageListener(this);
	}

	@Override
	public void processMessage(Chat chat, Message msg) {
		String result = msg.getFrom() + ":" + msg.getBody();
		if (msg.getBody() == null) {
			return;
		}
		android.os.Message message = handler.obtainMessage();
		Bundle bundle = new Bundle();
		bundle.putString("msg", msg.getBody());
		bundle.putString("from", msg.getFrom());
		message.setData(bundle);
		message.sendToTarget();
	}
}

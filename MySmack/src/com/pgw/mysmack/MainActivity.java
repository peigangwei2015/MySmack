package com.pgw.mysmack;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pgw.mysmack.utils.XmppTool;

public class MainActivity extends Activity implements OnClickListener {
	protected static final String TAG = "MainActivity";
	protected static final int LOGIN_SUCCESS = 0;
	protected static final int LOGIN_FAIL = 1;
	private EditText et_username, et_password, et_server_ip;
	private Button bt_cancle, bt_login;
	private SharedPreferences sp;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {
			// 登录成功
			case LOGIN_SUCCESS:
				Toast.makeText(getApplicationContext(), "登录成功",
						Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(MainActivity.this,
						MyFriendListActivity.class);
				startActivity(intent);
				break;
			// 登录失败
			case LOGIN_FAIL:
				Toast.makeText(getApplicationContext(), "登录失败",
						Toast.LENGTH_SHORT).show();
				break;

			}

		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		sp = getSharedPreferences("config", Context.MODE_PRIVATE);
		initView();
	}

	/**
	 * 初始化视图
	 */
	private void initView() {
		et_username = (EditText) findViewById(R.id.username);
		et_password = (EditText) findViewById(R.id.password);
		et_server_ip = (EditText) findViewById(R.id.server_ip);

		bt_cancle = (Button) findViewById(R.id.cancle);
		bt_login = (Button) findViewById(R.id.login);

		bt_cancle.setOnClickListener(this);
		bt_login.setOnClickListener(this);

		recoveryLoginInfo();
	}

	/**
	 * 恢复登录信息
	 */
	private void recoveryLoginInfo() {
		et_username.setText(sp.getString("username", ""));
		et_server_ip.setText(sp.getString("serverIp", ""));
		et_password.setText(sp.getString("password", ""));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.cancle:
			cancle();
			break;
		case R.id.login:
			login();
			break;
		}
	}

	/**
	 * 登录
	 */
	private void login() {
		final String username = et_username.getText().toString();
		final String password = et_password.getText().toString();
		final String serverIp = et_server_ip.getText().toString();
		if (TextUtils.isEmpty(password) || TextUtils.isEmpty(username)
				|| TextUtils.isEmpty(serverIp)) {
			Toast.makeText(getApplicationContext(), "用户名密码和IP地址都不能为空！", 0)
					.show();
			return;
		}
		// 保存登录信息
		saveLoginInfo(username, password, serverIp);
		
		new Thread(){
			public void run() {
				Message message = handler.obtainMessage();
				try {
					XmppTool.setServerIp(serverIp);
					Log.v(TAG, username);
					Log.v(TAG, password);
					XmppTool.getConn().login(username, password);
					Log.v(TAG, "登录成功！");
					message.what = LOGIN_SUCCESS;
				} catch (XMPPException e) {
					e.printStackTrace();
					message.what = LOGIN_FAIL;
				} finally {
					message.sendToTarget();
				}
			};
		}.start();

	}

	/**
	 * 
	 * 保存用户信息
	 * 
	 * @param username
	 * @param password
	 * @param serverIp
	 */
	private void saveLoginInfo(String username, String password, String serverIp) {
		Editor edit = sp.edit();
		edit.putString("username", username);
		edit.putString("password", password);
		edit.putString("serverIp", serverIp);
		edit.commit();
	}

	private void cancle() {
		finish();
	}

}

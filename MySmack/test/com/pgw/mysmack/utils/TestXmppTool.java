package com.pgw.mysmack.utils;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import android.test.AndroidTestCase;
import android.util.Log;

public class TestXmppTool extends AndroidTestCase{
	private static final String TAG = "TestXmppTool";

	public void testIsLogin(){
		XMPPConnection conn = XmppTool.getConn();
		try {
			conn.login("admin", "admin");
			String user = conn.getUser();
			Log.v(TAG, user);
		} catch (XMPPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}

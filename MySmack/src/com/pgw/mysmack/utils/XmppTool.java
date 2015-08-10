package com.pgw.mysmack.utils;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

public class XmppTool {

	private static XMPPConnection con = null;
	private static String serverIp = "192.168.0.18";
	private static boolean LoginOk = true;;

	private static final int port = 5222;

	/**
	 * ������
	 */
	private static void openConnection() {
		try {
			ConnectionConfiguration connConfig = new ConnectionConfiguration(
					serverIp, port);
			connConfig.setDebuggerEnabled(false);
			con = new XMPPConnection(connConfig);
			con.connect();
		} catch (XMPPException xe) {
			xe.printStackTrace();
		}

	}

	/**
	 * ���÷�����IP��ַ
	 * 
	 * @param serverIp
	 *            ������IP��ַ
	 */
	public static void setServerIp(String serverIp) {
		if (!XmppTool.serverIp.equals(serverIp)) {
			XmppTool.serverIp = serverIp;
			closeConnection();
			openConnection();
		}
	}

	/**
	 * ��ȡ������
	 * 
	 * @return
	 */
	public static XMPPConnection getConn() {
		if (con == null) {
			openConnection();
		}
		return con;
	}


	/**
	 * �ر�����
	 */
	public static void closeConnection() {
				con.disconnect();
				con = null;
	}
}

package socket;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Logger;

import types.MessageTypes;

public class BragConnClient {
	public static void main(String[] args) {
		System.out.println("brag conn client start");
		BragConnClient bcc = new BragConnClient();
		bcc.start();
	}

	public void start() {
		while (true) {
			try {
				Log.e("","begin to connect to server\n");
				connectToServer();
				monitorEvent();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				Log.e("","connect fail, wait 3s");
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private String serverIP = "localhost";
	private int port = 8839;
	private final int intlen = 4;
	private SocketChannel channel = null;
	private Selector selector = null;

	RecvDispatch recvDispatch = new RecvDispatch();

	public void wirteData(SocketChannel channel) {
		ByteBuffer byteBuffer = ByteBuffer.allocate(20);
		byteBuffer.clear();
		String s = "helloworld";
		byteBuffer.put(s.getBytes());
		byteBuffer.flip();
		try {
			channel.write(byteBuffer);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void connectToServer() throws IOException {

		channel = SocketChannel.open();
		channel.configureBlocking(false);

		channel.connect(new InetSocketAddress(serverIP, port));
		selector = Selector.open();
		channel.register(selector, SelectionKey.OP_CONNECT);
	}

	private void monitorEvent() throws IOException {
		while (true) {
			selector.select();
			Set<SelectionKey> selectedKeys = selector.selectedKeys();
			Iterator<SelectionKey> it = selectedKeys.iterator();
			while (it.hasNext()) {
				SelectionKey key = (SelectionKey) it.next();

				if (key.isConnectable()) {
//					Logger.getGlobal().info("connect key:" + key.toString());
					Log.e("BragClient","connect key:" + key.toString());
					handleConnect(key);

				} else if (key.isReadable()) {
//					Logger.getGlobal().info("read key " + key.toString());
					Log.e("BragClient","read key " + key.toString());
					recvDispatch.dispatch(channel);
				}
				it.remove();
			}
		}
	}

	private void handleConnect(SelectionKey key) throws IOException {
		SocketChannel sc = (SocketChannel)key.channel();
		if (sc.isConnectionPending()) {
//			System.out.println("is pending");
			Log.e("BragClient","is pending");
			boolean ret = sc.finishConnect();
//			System.out.println("is finish " + ret);
			Log.e("BragClient","is finish " + ret);
		}
		Log.e("BragClient","configure blocking false" );
		sc.configureBlocking(false);

		sc.register(selector, SelectionKey.OP_READ);
		channel = sc;
		// wirteData(channel);

		sendData(MessageTypes.MESSAGE_LOGIN, buildLoginData().toString());
	}

	public void sendData(int messageType, String jsonData) {
		Log.e("走了没","走了没");
		ByteBuffer respBuf = ByteBuffer.allocate(jsonData.length() + 20);
		respBuf.clear();
		respBuf.putInt(messageType);
		respBuf.putInt(jsonData.length());
		respBuf.put(jsonData.getBytes());

		// switch buff from write to read
		respBuf.flip();

		// if exception is throw, it means socket was disabled.

		try {
			channel.write(respBuf);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Logger.getGlobal().info("response data");
		Log.e("真没走吗？","真没走吗？");
	}

	private JSONObject buildLoginData() {
		JSONObject json = new JSONObject();

		try {
			json.put("loginType", 1);
			json.put("loginNum", "2356453");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}

}

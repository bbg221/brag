package niotest;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Logger;

import org.json.JSONException;
import org.json.JSONObject;

public class BragConnClient {
	public static void main(String[] args) {
		System.out.println("brag conn client start");
		BragConnClient bcc = new BragConnClient();

		bcc.start();
	}

	private void start() {
		while (true) {
			try {
				connectToServer();
				monitorEvent();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private String serverIP = "localhost";
	private int port = 8839;
	private SocketChannel channel = null;
	private Selector selector = null;

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
					Logger.getGlobal().info("connect key:" + key.toString());
					handleConnect(key);

				} else if (key.isReadable()) {
					Logger.getGlobal().info("read key " + key.toString());
					RecvDataPost.getInstance().readData(channel);
				}
				it.remove();
			}
		}
	}
	
	private void handleConnect(SelectionKey key) throws IOException {

		if (channel.isConnectionPending()) {
			System.out.println("is pending");
			boolean ret = channel.finishConnect();
			System.out.println("is finish " + ret);
		}

		channel.configureBlocking(false);

		channel.register(selector, SelectionKey.OP_READ);

		ConnSend.getInstance().setSocketChannel(channel);
	}

}

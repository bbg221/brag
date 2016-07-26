package niotest;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.logging.Logger;

import org.json.JSONObject;

public class ConnSend {
	private SocketChannel sc;
	public void setSocketChannel(SocketChannel sc) {
		this.sc = sc;
	}
	
	public void sendData(int messageType, JSONObject jsonData) {
		ByteBuffer respBuf = ByteBuffer.allocate(jsonData.toString().length() + 20);
		respBuf.clear();

		respBuf.putInt(messageType);
		respBuf.putInt(jsonData.toString().length());
		respBuf.put(jsonData.toString().getBytes());

		// switch buff from write to read
		respBuf.flip();

		// if exception is throw, it means socket was disabled.

		try {
			sc.write(respBuf);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Logger.getGlobal().info("response data");
	}
}

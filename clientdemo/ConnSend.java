package niotest;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ConnSend {
	
	private ConnSend(){}

    private static final ConnSend instance = new ConnSend();

    public static ConnSend getInstance(){
        return instance;
    }
	
	
	private SocketChannel sc;
	private GsonBuilder gb = new GsonBuilder();
	private Gson gson = gb.create();
	
	public void setSocketChannel(SocketChannel sc) {
		this.sc = sc;
	}	
	
	public boolean sendData(int messageType, Object src) {
		
		boolean ret = true;
		String jsonData = gson.toJson(src);
		
		ByteBuffer respBuf = ByteBuffer.allocate(jsonData.length() + 20);
		respBuf.clear();

		respBuf.putInt(messageType);
		respBuf.putInt(jsonData.length());
		respBuf.put(jsonData.getBytes());

		// switch buff from write to read
		respBuf.flip();

		// if exception is throw, it means socket was disabled.

		try {
			sc.write(respBuf);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ret = false;
		}

		Logger.getGlobal().info("send data " + messageType + ", result:" + ret);
		return ret;
	}
}

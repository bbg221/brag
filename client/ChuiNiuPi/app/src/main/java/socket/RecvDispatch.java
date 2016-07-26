package socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import types.MessageTypes;


public class RecvDispatch {
	private int intlen = 4;
	
	private Map<Integer, RecvMessageHandler> handlerMap = new HashMap<>();
	
	public void setMessageHandler(int messageId, RecvMessageHandler handler) {
		handlerMap.put(messageId, handler);
	}
	
	public void dispatch(SocketChannel sc) throws IOException {
		ByteBuffer typeBuf = ByteBuffer.allocate(intlen);
		ByteBuffer lenBuf = ByteBuffer.allocate(intlen);
		
		ByteBuffer[] bufferArray = {typeBuf, lenBuf};
		
		long readNum = sc.read(bufferArray);
		if (0 == readNum)
		{
			// a empty message, it doesn't make sense.
			return;
		} else if (0 > readNum) {
			IOException e = new IOException("read num : -1");
			throw e;
		}
		
		typeBuf.flip();
		lenBuf.flip();
		
		int messageType = typeBuf.getInt();
		int messageLen = lenBuf.getInt();

		int type = MessageTypes.getDomain(messageType);
		Logger.getGlobal().info("read something from  network messageType:" + 
				messageType + ", len:" + messageLen + ", domain:" + type);
		JSONObject jsonData = getDataFromSc(messageLen, sc);

		if (null != handlerMap.get(messageType)) {
			handlerMap.get(messageType).handleMessage(messageType, jsonData);
		}
	}
	
	private JSONObject getDataFromSc(int messageLen, SocketChannel sc) throws IOException {
		ByteBuffer  dataBuf = ByteBuffer.allocate(messageLen);
		
		long readNum = sc.read(dataBuf);
		if (0 >= readNum) {
			IOException e = new IOException("read num :" + readNum);
			throw e;
		}
		
		dataBuf.flip();
		
		JSONObject loginJson = new JSONObject();
		
		try {
			loginJson = new JSONObject(byteBufferToString(dataBuf));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return loginJson;		
	}
	
	public static String byteBufferToString(ByteBuffer buffer) {
		CharBuffer charBuffer = null;
		try {
			Charset charset = Charset.forName("UTF-8");
			CharsetDecoder decoder = charset.newDecoder();
			charBuffer = decoder.decode(buffer);
			buffer.flip();
			return charBuffer.toString();
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
}

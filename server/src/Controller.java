import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import business.DataSendHandler;
import business.LoginHandler;
import business.LoginProcess;
import business.UserProcess;
import connection.ConnHandler;
import global.GlobalInstance;
import tools.Tools;
import types.MessageTypes;

public class Controller implements ConnHandler, LoginHandler, DataSendHandler{
	private final int intlen = 4;
	
	// save the map of userId and socketChannel
	private Map<Integer, SocketChannel> socketMap = new HashMap<>();
	private Map<SocketChannel, Integer> userIdMap = new HashMap<>();
	
	private LoginProcess loginProcess = new LoginProcess(this, this);
	private UserProcess userProcess = new UserProcess(this);
	
	public static void main(String[] args) {
		Controller ctr = new Controller();
		ctr.init();
		ctr.start();
	}
	
	public void init() {
		GlobalInstance.getInstance().getConnHelper().setConnHandler(this);
	}
	
	public void start() {
		GlobalInstance.getInstance().getConnHelper().start();
	}

	@Override
	public void handleRead(SocketChannel sc) throws Exception{
		// TODO Auto-generated method stub
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

		switch (MessageTypes.getDomain(messageType)) {
		case MessageTypes.DOMAIN_LOGIN:
			loginProcess.processLoginData(messageType, jsonData, sc);
			break;
		case MessageTypes.DOMAIN_USERDATA:
			userProcess.processUserData(messageType, jsonData);
			break;
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
			loginJson = new JSONObject(Tools.byteBufferToString(dataBuf));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return loginJson;		
	}

	@Override
	public void connectionDisabled(SocketChannel sc) {
		if (null == userIdMap.get(sc))
		{
			return;
		}
		
		int userId = userIdMap.get(sc);
		userIdMap.remove(sc);
		socketMap.remove(userId);
		Logger.getGlobal().info("remove socket map, userId: " + userId);
	}
	
	@Override
	public void saveSocketChannel(int userId, SocketChannel sc) {
		socketMap.put(userId, sc);
		userIdMap.put(sc, userId);
		Logger.getGlobal().info("save socket map, userID: " + userId);
	}


	private SocketChannel getScByUserId(int userId) {		
		return socketMap.get(userId);
	}

	@Override
	public void sendData(int userId, int messageType, JSONObject jsonData) {
		ByteBuffer respBuf = ByteBuffer.allocate(jsonData.toString().length() + 20);
		respBuf.clear();

		respBuf.putInt(messageType);
		respBuf.putInt(jsonData.toString().length());
		respBuf.put(jsonData.toString().getBytes());

		// switch buff from write to read
		respBuf.flip();

		// if exception is throw, it means socket was disabled.
		if (null != getScByUserId(userId)) {
			try {
				getScByUserId(userId).write(respBuf);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}			
		Logger.getGlobal().info("response data");
		
	}
	
	
}

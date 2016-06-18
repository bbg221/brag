package brag;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import brag.networkWithClient.ConnHandler;

public class Controller implements ConnHandler, LoginHandler {
	
	private final int intlen = 4;
	
	// save the map of userId and socketChannel
	private Map<Integer, SocketChannel> socketMap = new HashMap<>();
	private Map<SocketChannel, Integer> userIdMap = new HashMap<>();
	
	private LoginProcess loginProcess = new LoginProcess();
	
	public static void main(String[] args) {
		Controller ctr = new Controller();
		ctr.init();
		ctr.start();
	}
	
	public void init() {
		GlobalInstance.getInstance().getConnHelper().setConnHandler(this);
		loginProcess.setLoginHandler(this);
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
		
		
		
		switch (MessageTypes.getDomain(messageType)) {
		case MessageTypes.DOMAIN_LOGIN:
			loginProcess.processLoginData(messageType, messageLen, sc);
		}
				
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


}

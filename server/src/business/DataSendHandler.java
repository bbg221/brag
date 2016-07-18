package business;

import org.json.JSONObject;

public interface DataSendHandler {
	//public SocketChannel getScByUserId(int userId);
	
	public void sendData(int userId, int messageType, JSONObject jsonData);
}

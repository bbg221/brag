package socket;

import org.json.JSONObject;

public interface RecvMessageHandler {
	public void handleMessage(int messageId, JSONObject jsonData);
}

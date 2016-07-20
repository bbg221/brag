package business;

import java.io.IOException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import global.GlobalInstance;
import types.MessageTypes;
import types.MessageValueTypes;
import types.User;

public class UserProcess {
	
	private DataSendHandler dataSender;
	
	public UserProcess(DataSendHandler dh) {
		this.dataSender = dh;
	}

	public void processUserData(int messageType, JSONObject jsonData) throws IOException{
		
		switch (messageType) {
		case MessageTypes.MESSAGE_SEND_USER:
			sendUserData(jsonData);
			break;
		case MessageTypes.MESSAGE_REFRESH_PIC:
			refreshPic(jsonData);
			break;
		case MessageTypes.MESSAGE_CHANGE_NAME:
			changeName(jsonData);
			break;
		case MessageTypes.MESSAGE_CHANGE_PIC:
			changePic(jsonData);
			break;
		case MessageTypes.MESSAGE_GET_FRIEND:
			getFriends(jsonData);
		case MessageTypes.MESSAGE_ADD_FRIEND:
			addFriend(jsonData);
			break;
		case MessageTypes.MESSAGE_DEL_FRIEND:
			delFriend(jsonData);
			break;
		}
	}
	
	private void delFriend(JSONObject jsonData) {
		int userId = 0;
		int addId = 0;
		
		try {
			userId = jsonData.getInt(MessageValueTypes.userId);
			addId = jsonData.getInt(MessageValueTypes.addId);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		GlobalInstance.getInstance().getDbHandler().delFriend(userId, addId);
		
	}

	private void addFriend(JSONObject jsonData) {
		int userId = 0;
		int addId = 0;
		
		try {
			userId = jsonData.getInt(MessageValueTypes.userId);
			addId = jsonData.getInt(MessageValueTypes.addId);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		GlobalInstance.getInstance().getDbHandler().addFriend(userId, addId);
	}

	private void getFriends(JSONObject jsonData) {
		int userId = 0;
		
		try {
			userId = jsonData.getInt(MessageValueTypes.userId);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int[] friendsId = GlobalInstance.getInstance().getDbHandler().getFriends(userId);
		
		JSONArray jsonArr = new JSONArray();
		
		for (int i = 0; i < friendsId.length; i++) {
			User user = GlobalInstance.getInstance().getDbHandler().getUserDataById(friendsId[i]);
			
			jsonArr.put(convertUser(user));
		}
		
		JSONObject jsonFriends = new JSONObject();
		try {
			jsonFriends.put(MessageValueTypes.users, jsonArr);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		dataSender.sendData(userId, MessageTypes.MESSAGE_GET_FRIEND_RSP, jsonFriends);
	}

	private void changePic(JSONObject jsonData) {
		// TODO Auto-generated method stub
		
	}

	private void changeName(JSONObject jsonData) {
		int userId = 0;
		String newName = "";
		
		try {
			userId = jsonData.getInt(MessageValueTypes.userId);
			newName = jsonData.getString(MessageValueTypes.userName);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		GlobalInstance.getInstance().getDbHandler().changeUserName(userId, newName);		
	}

	private void sendUserData(JSONObject jsonData)
	{
		int userId = 0;
		String userName = "";
		int age = 0;
		int sex = 0;
				
		try {
			userId = jsonData.getInt(MessageValueTypes.userId);
			userName = jsonData.getString(MessageValueTypes.userName);
			age = jsonData.getInt(MessageValueTypes.age);
			sex = jsonData.getInt(MessageValueTypes.sex);			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//save 
		GlobalInstance.getInstance().getDbHandler().updateUserData(userId, userName, sex, age);		
	}
	
	private void refreshPic(JSONObject jsonData) {
		// to do
	}
	
	private JSONObject convertUser(User user) {
		JSONObject userJson = new JSONObject();
		
		try {
			userJson.put(MessageValueTypes.userId, user.userId);
			userJson.put(MessageValueTypes.age, user.age);
			userJson.put(MessageValueTypes.sex, user.sex);
			userJson.put(MessageValueTypes.brag_count, user.brag_count);
			userJson.put(MessageValueTypes.equal_count, user.equal_count);
			userJson.put(MessageValueTypes.fail_count, user.fail_count);
			userJson.put(MessageValueTypes.win_count, user.win_count);
			userJson.put(MessageValueTypes.userName, user.name);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return userJson;
	}
}

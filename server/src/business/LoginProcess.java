package business;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.logging.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import global.GlobalInstance;
import types.DataState;
import types.MessageValueTypes;
import types.MessageTypes;

public class LoginProcess {
	private LoginHandler loginHandler;
	private DataSendHandler dataSender;

	private static int visitorLoginNum = 0;

	public LoginProcess(LoginHandler lh, DataSendHandler ds) {
		this.loginHandler = lh;
		this.dataSender = ds;
	}
	
	public void processLoginData(int messageType, JSONObject jsonData, SocketChannel sc) throws IOException {

		if (MessageTypes.MESSAGE_LOGIN == messageType) {
			processUserLogin(jsonData, sc);
		}
	}

	private void processUserLogin(JSONObject jsonData, SocketChannel sc) throws IOException {
		Logger.getGlobal().info("processUserLogin");

		int loginType = 0;
		String loginNum = "";
		try {
			loginType = jsonData.getInt(MessageValueTypes.loginType);
			loginNum = jsonData.getString(MessageValueTypes.loginNum);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		int userId = createUserId(loginType, loginNum);
		loginHandler.saveSocketChannel(userId, sc);
		int needRefreshFlag = handleLoginForDataBase(userId, userId, loginNum);
		Logger.getGlobal().info("refresh flag is " + needRefreshFlag);
		sendLoginResp(userId, needRefreshFlag);
	}

	private int createUserId(int loginType, String loginNum) {
		if (MessageValueTypes.LOGIN_VISTOR == loginType) {
			loginNum = "" + visitorLoginNum++;
		}

		return ("" + loginType + loginNum).hashCode();
	}

	private int handleLoginForDataBase(int userId, int loginType, String loginNum) {
		int needRefreshFlag = 0;

		// visitor never save into database.
		if (MessageValueTypes.LOGIN_VISTOR == loginType) {
			return createNeedUpdateFlag(1, 1); // means not need update
		}

		DataState isChanged = GlobalInstance.getInstance().getDbHandler().isUserDataChanged(userId);

		// user is not existed.
		if (isChanged.INVALID == isChanged.name_changed) {
			boolean ret = GlobalInstance.getInstance().getDbHandler().insertUser(userId, loginType, loginNum);
			if (ret) {
				Logger.getGlobal().info("save user to database success, Id is : " + userId);
			} else {
				Logger.getGlobal().info("save user to database failed, Id is : " + userId);
			}
			needRefreshFlag = createNeedUpdateFlag(0, 0);
		} else {
			needRefreshFlag = createNeedUpdateFlag(isChanged.name_changed, isChanged.picture_changed);
		}

		return needRefreshFlag;
	}

	private int createNeedUpdateFlag(int nameChanged, int pictureChanged) {
		Logger.getGlobal().info("namechagned:" + nameChanged + ", pictureChanged" + pictureChanged);
		int needNameUpdate = 1; // 01b
		int needPictureUpdate = 2; // 10b

		int needUpdateFlag = 0;

		if (0 == nameChanged) {
			needUpdateFlag |= needNameUpdate;
		}

		if (0 == pictureChanged) {
			needUpdateFlag |= needPictureUpdate;
		}

		return needUpdateFlag;
	}

	private void sendLoginResp(int userId, int needRefreshFlag) throws IOException {
		int rspType = MessageTypes.MESSAGE_LOGIN_RSP;

		JSONObject rspJson = new JSONObject();

		try {
			rspJson.put(MessageValueTypes.userId, userId);
			rspJson.put(MessageValueTypes.needRefreshFlag, needRefreshFlag);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		dataSender.sendData(userId, rspType, rspJson);
		
		Logger.getGlobal().info("response MESSAGE_LOGIN_RSP");
	}
}

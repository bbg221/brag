package business;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.logging.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import global.GlobalInstance;
import tools.Tools;
import types.DataState;
import types.LoginTypes;
import types.MessageTypes;

public class LoginProcess {
private LoginHandler loginHandler;
	
	private static int visitorLoginNum = 0;
	
	public void setLoginHandler(LoginHandler lh) {
		this.loginHandler = lh;
	}
	
	public void processLoginData(int messageType, int messageLen, SocketChannel sc) throws IOException{
		if (MessageTypes.MESSAGE_LOGIN == messageType) {
			processUserLogin(messageLen, sc);
		}
	}
	
	private void processUserLogin(int messageLen, SocketChannel sc) throws IOException
	{
		if (messageLen <= 0) {
			Logger.getGlobal().info("message received from login is empty");
			return;
		}
		
		Logger.getGlobal().info("processUserLogin");
		
		ByteBuffer  dataBuf = ByteBuffer.allocate(messageLen);
		
		long readNum = sc.read(dataBuf);
		if (0 == readNum)
		{
			// a empty message, it doesn't make sense.
			return;
		} else if (0 > readNum) {
			IOException e = new IOException("read num : -1");
			throw e;
		}
		
		dataBuf.flip();
		
		try {
			JSONObject loginJson = new JSONObject(Tools.byteBufferToString(dataBuf));
		
			Logger.getGlobal().info("revc" + readNum + " data: " + loginJson.toString());
			int loginType = 0;
			String loginNum = "";
			try {
				loginType = loginJson.getInt("loginType");
				loginNum = loginJson.getString("loginNum");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			int userId = createUserId(loginType, loginNum);
			loginHandler.saveSocketChannel(userId, sc);
			int needRefreshFlag = handleLoginForDataBase(userId, userId, loginNum);
			Logger.getGlobal().info("refresh flag is " + needRefreshFlag);
			sendLoginResp(sc, userId, needRefreshFlag);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	private int createUserId(int loginType, String loginNum)
	{
		if (LoginTypes.LOGIN_VISTOR == loginType)
		{
			loginNum = "" + visitorLoginNum++;
		}
		
		return ("" + loginType + loginNum).hashCode();
	}
	
	private int handleLoginForDataBase(int userId, int loginType, String loginNum)
	{
		int needRefreshFlag = 0;
		
		// visitor never save into database.
		if (LoginTypes.LOGIN_VISTOR == loginType) {
			return createNeedUpdateFlag(1,1); // means not need update
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
	
	private int createNeedUpdateFlag(int nameChanged, int pictureChanged)
	{
		Logger.getGlobal().info("namechagned:" + nameChanged + ", pictureChanged" + pictureChanged);
		int needNameUpdate = 1;     // 01b
		int needPictureUpdate = 2;  // 10b
		
		int needUpdateFlag = 0;
		
		if (0 == nameChanged) {
			needUpdateFlag |= needNameUpdate;
		}
		
		if (0 == pictureChanged) {
			needUpdateFlag |= needPictureUpdate;
		}
		
		return needUpdateFlag;
	}
	
	private void sendLoginResp(SocketChannel sc, int userId, int needRefreshFlag) throws IOException {
		ByteBuffer respBuf = ByteBuffer.allocate(1024);
		respBuf.clear();
		
		int rspType = MessageTypes.MESSAGE_LOGIN_RSP;
		
		JSONObject rspJson = new JSONObject();
		
		try {
			rspJson.put("userId", userId);
			rspJson.put("needRefreshFlag", needRefreshFlag);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		respBuf.putInt(rspType);
		respBuf.putInt(rspJson.toString().length());
		respBuf.put(rspJson.toString().getBytes());
		
		// switch buff from write to read
		respBuf.flip();
		
		// if exception is throw, it means socket was disabled.
		sc.write(respBuf);
		Logger.getGlobal().info("response MESSAGE_LOGIN_RSP");
	}
}

package business;

import org.json.JSONException;
import org.json.JSONObject;

import pork.PorkShuffler;
import tools.Tools;
import types.MessageValueTypes;
import types.PorkAction;

public class PorkProcess {
	
	private int userA;
	private int userB;
	
	private int firstUser;
	
	public PorkProcess(int userA, int userB) {
		this.userA = userA;
		this.userB = userB;
		this.firstUser = userA;
	}
	
	public void setFirstUser(int userId) {
		this.firstUser = userId;
	}
	
	private boolean isPlayer(int userId) {
		return userId == userA || userId == userB;				
	}
	
	private int getAnotherUser(int userId) {
		return userId == userA ? userB : userA;
	}
	
	public void start() {
		PorkShuffler ps = new PorkShuffler();
		ps.shuffle();
		
		int[] firstPork = ps.getFistPartPorks();
		int[] secondPork = ps.getSecondPartPorks();
		
		
	}
	
	public void handlePorkProcess(JSONObject jsonData) {
		PorkData porkData = getPorkData(jsonData);
		
		
	}
	
	private PorkData getPorkData(JSONObject jsonData) {
		PorkData porkData = new PorkData();
				
		try {
			porkData.action = jsonData.getInt(MessageValueTypes.porkAction);
			porkData.userId = jsonData.getInt(MessageValueTypes.userId);
			porkData.porks = Tools.covertStringToArr(jsonData.getString(MessageValueTypes.porkData));
		} catch (JSONException e) {
			porkData = new PorkData();
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	class PorkData {
		public int userId;
		public int action;
		public int fakePork;
		public int[] porks;
		
		public PorkData() {
			this.action = PorkAction.invalid;			
		}
	}
}

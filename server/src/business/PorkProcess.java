package business;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import global.GlobalInstance;
import pork.PorkRule;
import pork.PorkShuffler;
import tools.Tools;
import types.MessageTypes;
import types.MessageValueTypes;
import types.PorkAction;

public class PorkProcess {

	enum GameState {
		init, gaming, waiting, end;
	}

	private GameState gameState = GameState.init;
	private int userA;
	private int userB;

	private int firstUser;
	private int expectUser; // expect who comes next.

	// save how many times user faked successfully in a round
	private Map<Integer, Integer> bragRoundMap = new HashMap<>();
	
	// fakes times in this game.
	private Map<Integer, Integer> bragMap = new HashMap<>(); 

	// save how many pork in user's hands, this map used for server calculation
	private Map<Integer, Integer> handsCountMap = new HashMap<>(); 
	
	// save the rest count from client.
	private Map<Integer, Integer> restCountMap = new HashMap<>();
	
	private DataSendHandler dataSender;

	private PorkRule porkRule;

	public PorkProcess(int userA, int userB, DataSendHandler dataSender) {
		this.userA = userA;
		this.userB = userB;
		this.dataSender = dataSender;
		this.firstUser = userA;

		clearBragRoundMap();
	}

	private void clearBragRoundMap() {
		bragRoundMap.put(userA, 0);
		bragRoundMap.put(userB, 0);
	}

	private void clearBragMap() {
		bragMap.put(userA, 0);
		bragMap.put(userB, 0);
	}

	private void selfAddBragRoundMap(int userId) {
		int brag = bragRoundMap.get(userId);
		brag++;
		bragRoundMap.put(userId, brag);
	}

	private void addRoundBragToMain(int userId) {
		int bragRound = bragRoundMap.get(userId);
		int bragMain = bragMap.get(userId);

		bragMain += bragRound;

		bragMap.put(userId, bragMain);
	}

	private void initRestCountMap(int restCount) {
		restCountMap.put(userA, restCount);
		restCountMap.put(userB, restCount);
	}
	
	private void initHandsCountMap(int porkCount) {
		handsCountMap.put(userA, porkCount);
		handsCountMap.put(userB, porkCount);
	}

	private void addHandsCount(int userId, int addCount) {
		int porkCount = handsCountMap.get(userId);
		porkCount += addCount;

		handsCountMap.put(userId, porkCount);
	}

	private void reduceHandsCount(int userId, int reduceCount) {
		int porkCount = handsCountMap.get(userId);
		porkCount -= reduceCount;

		handsCountMap.put(userId, porkCount);
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

		clearBragMap();
		PorkShuffler ps = new PorkShuffler();
		ps.shuffle();

		PorkData firstPorkData = new PorkData();
		firstPorkData.action = PorkAction.initSend;
		firstPorkData.userId = firstUser;
		firstPorkData.porks = ps.getFistPartPorks();

		PorkData secondPorkData = new PorkData();
		secondPorkData.action = PorkAction.initWait;
		secondPorkData.userId = getAnotherUser(firstUser);
		secondPorkData.porks = ps.getSecondPartPorks();

		dataSender.sendData(firstUser, MessageTypes.MESSAGE_GAME_DATA, firstPorkData.convertToJson());
		dataSender.sendData(getAnotherUser(firstUser), MessageTypes.MESSAGE_GAME_DATA, secondPorkData.convertToJson());

		initHandsCountMap(firstPorkData.porks.length);
		initRestCountMap(firstPorkData.porks.length);
		
		expectUser = firstUser;
		gameState = GameState.gaming;
	}

	public void handlePorkProcess(JSONObject jsonData) {
		PorkData porkData = new PorkData();
		porkData.parseJson(jsonData);

		if (!isPlayer(porkData.userId)) {
			return;
		}
		
		if (PorkAction.invalid == porkData.action) {
			return;
		}
		
		

		if (PorkAction.giveUp == porkData.action || PorkAction.wantEqual == porkData.action
				|| PorkAction.agreeEqual == porkData.action || PorkAction.refuseEqual == porkData.action) {
			handlRequirement(porkData.action, porkData.userId);
			return;
		}

		if (expectUser != porkData.userId) {
			Logger.getGlobal().info("wrong user comes, user:" + porkData.userId + ", expect user:" + expectUser);
			return;
		}

		restCountMap.put(porkData.userId, porkData.porkCount);
		
		switch (porkData.action) {

		case firstHand:
			handleFirstHand(porkData.userId, porkData.fakePork, porkData.porks);
			break;
		case addPork:
			handleAddPork(porkData.userId, porkData.porks);
			break;
		case believe:
			handleBelieve(porkData.userId);
			break;
		case unbelieve:
			handleUnbelieve(porkData.userId);
			break;
		default:
			break;
		}
	}

	private void handleUnbelieve(int userId) {
		boolean isFaked = porkRule.unbelieve();
		int[] porks = porkRule.getPorks();
		int anotherUser = getAnotherUser(userId);
		PorkData porkData = new PorkData();

		if (isFaked) { // userId win this round due to another player was faked
						// last hands.
			porkData.action = PorkAction.unbelieveWin;
			dataSender.sendData(userId, MessageTypes.MESSAGE_GAME_DATA, porkData.convertToJson());

			porkData.action = PorkAction.unbelieveFail;
			porkData.porks = porks;
			dataSender.sendData(anotherUser, MessageTypes.MESSAGE_GAME_DATA, porkData.convertToJson());

			expectUser = userId;

		} else { // userId lose this round due to another player was send the
					// true pork as he said in last hand.

			porkData.action = PorkAction.unbelieveWin;
			dataSender.sendData(anotherUser, MessageTypes.MESSAGE_GAME_DATA, porkData.convertToJson());

			porkData.action = PorkAction.unbelieveFail;
			porkData.porks = porks;
			dataSender.sendData(userId, MessageTypes.MESSAGE_GAME_DATA, porkData.convertToJson());

			expectUser = anotherUser;
		}

		addRoundBragToMain(expectUser);
	}

	private void handleBelieve(int userId) {
		porkRule.believe();
		int anotherUser = getAnotherUser(userId);
		expectUser = anotherUser;

		addRoundBragToMain(anotherUser);

		PorkData porkData = new PorkData();
		porkData.action = PorkAction.believeSend;

		dataSender.sendData(anotherUser, MessageTypes.MESSAGE_GAME_DATA, porkData.convertToJson());
	}

	private void handleAddPork(int userId, int[] porks) {
		boolean isFake = porkRule.addPork(porks);
		int anotherUser = getAnotherUser(userId);
		expectUser = anotherUser;

		if (isFake) {
			selfAddBragRoundMap(userId);
		}

		if (!isFake && 0 == restCountMap.get(anotherUser) && GameState.end != gameState) {
			// this guy is win.
			gameState = GameState.end;

			// server know the game is end now, but don't tell players, because
			// the other player has something else to do.
			GlobalInstance.getInstance().getDbHandler().saveFail(anotherUser);
			GlobalInstance.getInstance().getDbHandler().saveWin(userId, bragMap.get(userId));
		}

		if (0 == restCountMap.get(anotherUser) && GameState.end != gameState) {
			gameState = GameState.end;

			GlobalInstance.getInstance().getDbHandler().saveFail(userId);
			GlobalInstance.getInstance().getDbHandler().saveWin(anotherUser, bragMap.get(anotherUser));
		}
				
		PorkData porkData = new PorkData();
		porkData.action = PorkAction.PorkTransmit;
		porkData.porks = new int[porks.length];
		porkData.porkCount = restCountMap.get(userId);

		dataSender.sendData(anotherUser, MessageTypes.MESSAGE_GAME_DATA, porkData.convertToJson());
	}

	private void handleFirstHand(int userId, int fakePork, int[] porks) {
		// the first hand comes, should clear brag data of last round.
		clearBragRoundMap(); 

		boolean isFake = porkRule.firstHand(fakePork, porks);
		int anotherUser = getAnotherUser(userId);
		expectUser = anotherUser;

		if (isFake) {
			selfAddBragRoundMap(userId);
		}

		if (!isFake && 0 == restCountMap.get(anotherUser) && GameState.end != gameState) {
			// this guy is win.
			gameState = GameState.end;

			// server know the game is end now, but don't tell players, because
			// the other player has something else to do.
			GlobalInstance.getInstance().getDbHandler().saveFail(anotherUser);
			GlobalInstance.getInstance().getDbHandler().saveWin(userId, bragMap.get(userId));
		}

		PorkData porkData = new PorkData();
		porkData.action = PorkAction.PorkTransmit;
		porkData.porkCount = restCountMap.get(userId);
		porkData.porks = new int[porks.length];

		dataSender.sendData(anotherUser, MessageTypes.MESSAGE_GAME_DATA, porkData.convertToJson());

	}

	private void handlRequirement(PorkAction action, int userId) {

		int anotherUser = getAnotherUser(userId);
		PorkData porkData = new PorkData();

		if (PorkAction.giveUp == action) {

			GlobalInstance.getInstance().getDbHandler().saveFail(userId);
			GlobalInstance.getInstance().getDbHandler().saveWin(anotherUser, bragMap.get(anotherUser));

			porkData.action = PorkAction.giveupWin;
			gameState = GameState.end;

		} else if (PorkAction.wantEqual == action) {

			porkData.action = PorkAction.wantEqual;

		} else if (PorkAction.refuseEqual == action) {

			porkData.action = PorkAction.refuseEqualRsp;

		} else if (PorkAction.agreeEqual == action) {

			GlobalInstance.getInstance().getDbHandler().saveEqual(userId);
			GlobalInstance.getInstance().getDbHandler().saveEqual(anotherUser);

			porkData.action = PorkAction.agreeEqualRsp;
			gameState = GameState.end;
		}

		dataSender.sendData(anotherUser, MessageTypes.MESSAGE_GAME_DATA, porkData.convertToJson());
	}

	class PorkData {
		public int userId;
		public int fakePork;
		public int porkCount; // client->server: self left port count,
								// server->client: other player left pork count
		public PorkAction action;
		public int[] porks;

		private void init() {
			action = PorkAction.invalid;
			fakePork = -1;
			porks = new int[0];
		}

		public PorkData() {
			init();
		}

		public JSONObject convertToJson() {
			JSONObject json = new JSONObject();

			try {
				json.put(MessageValueTypes.porkAction, action);
				json.put(MessageValueTypes.userId, userId);
				json.put(MessageValueTypes.fakePork, fakePork);
				json.put(MessageValueTypes.porkCount, porkCount);
				json.put(MessageValueTypes.porkData, Tools.covertArrToString(porks));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				json = new JSONObject();
				e.printStackTrace();
			}

			return json;
		}

		public void parseJson(JSONObject jsonData) {
			try {
				action = (PorkAction) jsonData.get(MessageValueTypes.porkAction);
				userId = jsonData.getInt(MessageValueTypes.userId);
				fakePork = jsonData.getInt(MessageValueTypes.fakePork);
				porkCount = jsonData.getInt(MessageValueTypes.porkCount);
				porks = Tools.covertStringToArr(jsonData.getString(MessageValueTypes.porkData));
			} catch (JSONException e) {
				init();
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}

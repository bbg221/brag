package types;

public class MessageTypes {
	private static final int DOMAIN_OFFSET = 16;
	
	public static final int DOMAIN_LOGIN = 1;
	public static final int DOMAIN_USERDATA = 2;
	public static final int DOMAIN_FIER = 3;
	public static final int DOMAIN_RANGE = 4;
	public static final int DOMAIN_GAME = 5;
	public static final int DOMAIN_NEWS = 6;
			
	// 登录类型
	// message {"loginType":"1", "loginNum":"43624272754"}
	public static final int MESSAGE_LOGIN              	= (DOMAIN_LOGIN << DOMAIN_OFFSET) + 1;
	// message {"userId":"23154657624353", "needRefreshFlag":"1"}
	public static final int MESSAGE_LOGIN_RSP          	= (DOMAIN_LOGIN << DOMAIN_OFFSET) + 2;
		
	// 处理用户数据类型
	// message {"userId":"1234567efse", "userName":"hello world", "age":"20", "sex":"1"}  0->man, 1->lady, 2->visitor
	public static final int MESSAGE_SEND_USER   	 	= (DOMAIN_USERDATA << DOMAIN_OFFSET) + 0;
	// to do
	public static final int MESSAGE_REFRESH_PIC			= (DOMAIN_USERDATA << DOMAIN_OFFSET) + 1;
	// {"userId":"1234567efse", "userName":"hello world"}
	public static final int MESSAGE_CHANGE_NAME 		= (DOMAIN_USERDATA << DOMAIN_OFFSET) + 2;
	// to do
	public static final int MESSAGE_CHANGE_PIC  		= (DOMAIN_USERDATA << DOMAIN_OFFSET) + 3;
	// {"userId":"1234567efse"}
	public static final int MESSAGE_GET_FRIEND 			= (DOMAIN_USERDATA << DOMAIN_OFFSET) + 4;
	// this is a JSONObject array. {"users":"jsonArr"} , 
	// each member of jsonArr is {"userId":"35323", "age":"21", "sex":"1","brag_count":"3","equal_count":"3","fail_count":"1","win_count":"2","userName":"jack"}
	public static final int MESSAGE_GET_FRIEND_RSP 		= (DOMAIN_USERDATA << DOMAIN_OFFSET) + 5;
	
	// IM先不做
	public static final int MESSAGE_GET_LETTERLIST 		= (DOMAIN_USERDATA << DOMAIN_OFFSET) + 6;
	public static final int MESSAGE_GET_LETTERLIST_RSP 	= (DOMAIN_USERDATA << DOMAIN_OFFSET) + 7;
	public static final int MESSAGE_GET_LETTERDATA 		= (DOMAIN_USERDATA << DOMAIN_OFFSET) + 8;
	public static final int MESSAGE_GET_LETTERDATA_RSP 	= (DOMAIN_USERDATA << DOMAIN_OFFSET) + 9;	
	
	// {"userId":"3241234", "addId":"3241"}
	public static final int MESSAGE_ADD_FRIEND 			= (DOMAIN_USERDATA << DOMAIN_OFFSET) + 10;
	//public static final int MESSAGE_ADD_FRIEND_RSP 		= (DOMAIN_USERDATA << DOMAIN_OFFSET) + 11;
	
	// {"userId":"3241234", "delId":"3241"}
	public static final int MESSAGE_DEL_FRIEND 			= (DOMAIN_USERDATA << DOMAIN_OFFSET) + 12;
	//public static final int MESSAGE_DEL_FRIEND_RSP 		= (DOMAIN_USERDATA << DOMAIN_OFFSET) + 13;
	
	
	// 首页公告类型
	public static final int MESSAGE_GET_NEWS 			= (DOMAIN_NEWS << DOMAIN_OFFSET) + 1;
	public static final int MESSAGE_SEND_NEWS 			= (DOMAIN_NEWS << DOMAIN_OFFSET) + 2;
	
	//对战类型
	public static final int MESSAGE_FIGHT_REQ 			= (DOMAIN_FIER << DOMAIN_OFFSET) + 1;
	public static final int MESSAGE_FIGHT_RSP 			= (DOMAIN_FIER << DOMAIN_OFFSET) + 2;
	public static final int MESSAGE_CANCEL_FIGHT_REQ 	= (DOMAIN_FIER << DOMAIN_OFFSET) + 3;
	public static final int MESSAGE_SEARCH_REQ 			= (DOMAIN_FIER << DOMAIN_OFFSET) + 4;
	public static final int MESSAGE_SEARCH_RSP 			= (DOMAIN_FIER << DOMAIN_OFFSET) + 5;
	public static final int MESSAGE_SEND_GAUNT 			= (DOMAIN_FIER << DOMAIN_OFFSET) + 6;
	public static final int MESSAGE_SEND_CANCEL_GAUNT 	= (DOMAIN_FIER << DOMAIN_OFFSET) + 7;
	
	//排行榜类型
	public static final int MESSAGE_GETRANGE_REQ 		= (DOMAIN_RANGE << DOMAIN_OFFSET) + 1;
	public static final int MESSAGE_GETRANGE_RSP 		= (DOMAIN_RANGE << DOMAIN_OFFSET) + 2;
	
	
	public static final int MESSAGE_READ_RADOM			= (DOMAIN_GAME << DOMAIN_OFFSET) +1;
	public static final int MESSAGE_READ_SPEC			= (DOMAIN_GAME << DOMAIN_OFFSET) +2;
	public static final int MESSAGE_GAME_DATA			= (DOMAIN_GAME << DOMAIN_OFFSET) +3;
	
	public static int getDomain(int messageType)
	{
		return messageType >> DOMAIN_OFFSET;
	}
}

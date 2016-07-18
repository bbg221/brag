package types;

public class MessageValueTypes {
	public static final int LOGIN_VISTOR = 0;
	public static final int LOGIN_QQ = 1;
	public static final int LOGIN_WECHAT = 2;
	

	
	// JSON of login message {"loginType":"1", "loginNum":"43624272754"}
	public static final String loginType = "loginType";
	public static final String loginNum  = "loginNum";
	
	// JSON of login response message {"userId":"23154657624353", "needRefreshFlag":"1"} 
	// needRefreshFlag:  01b:needNameUpdate, 10b:needPictureUpdate   combine by "|"  
	public static final String userId = "userId";
	public static final String needRefreshFlag = "needRefreshFlag";
	

	public static final String userName = "userName";	
	public static final String userPic = "userPic";
	public static final String sex = "sex";
	public static final String age = "age";
	public static final String brag_count = "brag_count";
	public static final String equal_count = "equal_count";
	public static final String fail_count = "fail_count";
	public static final String win_count = "win_count";
	
	public static final String users = "users";
	
	public static final String addId = "addId";
	public static final String delId = "delId";
}

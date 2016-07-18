package types;

public class MessageTypes {
	private static final int DOMAIN_OFFSET = 16;
	
	public static final int DOMAIN_LOGIN = 1;
	public static final int DOMAIN_USERDATA = 2;
	public static final int DOMAIN_FIER = 3;
	public static final int DOMAIN_RANGE = 4;
	public static final int DOMAIN_GAME = 5;
	public static final int DOMAIN_NEWS = 6;
			
	// ��¼����
	public static final int MESSAGE_LOGIN              	= (DOMAIN_LOGIN << DOMAIN_OFFSET) + 1;
	public static final int MESSAGE_LOGIN_RSP          	= (DOMAIN_LOGIN << DOMAIN_OFFSET) + 2;
		
	// �����û���������
	public static final int MESSAGE_SEND_USER   	 	= (DOMAIN_USERDATA << DOMAIN_OFFSET) + 0;
	public static final int MESSAGE_REFRESH_PIC			= (DOMAIN_USERDATA << DOMAIN_OFFSET) + 1;
	public static final int MESSAGE_CHANGE_NAME 		= (DOMAIN_USERDATA << DOMAIN_OFFSET) + 2;
	public static final int MESSAGE_CHANGE_PIC  		= (DOMAIN_USERDATA << DOMAIN_OFFSET) + 3;
	public static final int MESSAGE_GET_FRIEND 			= (DOMAIN_USERDATA << DOMAIN_OFFSET) + 4;
	public static final int MESSAGE_GET_FRIEND_RSP 		= (DOMAIN_USERDATA << DOMAIN_OFFSET) + 5;
	
	// IM�Ȳ���
	public static final int MESSAGE_GET_LETTERLIST 		= (DOMAIN_USERDATA << DOMAIN_OFFSET) + 6;
	public static final int MESSAGE_GET_LETTERLIST_RSP 	= (DOMAIN_USERDATA << DOMAIN_OFFSET) + 7;
	public static final int MESSAGE_GET_LETTERDATA 		= (DOMAIN_USERDATA << DOMAIN_OFFSET) + 8;
	public static final int MESSAGE_GET_LETTERDATA_RSP 	= (DOMAIN_USERDATA << DOMAIN_OFFSET) + 9;	
	
	public static final int MESSAGE_ADD_FRIEND 			= (DOMAIN_USERDATA << DOMAIN_OFFSET) + 10;
	public static final int MESSAGE_ADD_FRIEND_RSP 		= (DOMAIN_USERDATA << DOMAIN_OFFSET) + 11;
	public static final int MESSAGE_DEL_FRIEND 			= (DOMAIN_USERDATA << DOMAIN_OFFSET) + 12;
	public static final int MESSAGE_DEL_FRIEND_RSP 		= (DOMAIN_USERDATA << DOMAIN_OFFSET) + 13;
	
	
	// ��ҳ��������
	public static final int MESSAGE_GET_NEWS 			= (DOMAIN_NEWS << DOMAIN_OFFSET) + 1;
	public static final int MESSAGE_SEND_NEWS 			= (DOMAIN_NEWS << DOMAIN_OFFSET) + 2;
	
	//��ս����
	public static final int MESSAGE_FIGHT_REQ 			= (DOMAIN_FIER << DOMAIN_OFFSET) + 1;
	public static final int MESSAGE_FIGHT_RSP 			= (DOMAIN_FIER << DOMAIN_OFFSET) + 2;
	public static final int MESSAGE_CANCEL_FIGHT_REQ 	= (DOMAIN_FIER << DOMAIN_OFFSET) + 3;
	public static final int MESSAGE_SEARCH_REQ 			= (DOMAIN_FIER << DOMAIN_OFFSET) + 4;
	public static final int MESSAGE_SEARCH_RSP 			= (DOMAIN_FIER << DOMAIN_OFFSET) + 5;
	public static final int MESSAGE_SEND_GAUNT 			= (DOMAIN_FIER << DOMAIN_OFFSET) + 6;
	public static final int MESSAGE_SEND_CANCEL_GAUNT 	= (DOMAIN_FIER << DOMAIN_OFFSET) + 7;
	
	//���а�����
	public static final int MESSAGE_GETRANGE_REQ 		= (DOMAIN_RANGE << DOMAIN_OFFSET) + 1;
	public static final int MESSAGE_GETRANGE_RSP 		= (DOMAIN_RANGE << DOMAIN_OFFSET) + 2;
		
	public static int getDomain(int messageType)
	{
		return messageType >> DOMAIN_OFFSET;
	}
}

package database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.logging.Logger;

import types.DataState;
import types.LastLoginDate;
import types.Range;
import types.User;

public class DbHandler {
private DbHelper dbHelper = new DbHelper();
	
	private final int rangeShowNumber = 6;	
	
	public boolean insertUser(int userId, int loginType, String loginNumber)
	{
		boolean ret = false;
		
		dbHelper.getConn();
		
		String insertSql = "insert into tb_user_data (id, login_type, login_number) values (" + userId + ", " + loginType + ", " + loginNumber + ")";
		ret = dbHelper.setSql(insertSql);
		
		dbHelper.closeConn();
		
		return ret;
	}
	

	public User getUserDataById(int userId)
	{
		String getUserSql = "select id, name, sex, age, win_count, fail_count, equal_count, brag_count from tb_user_data where id=" + userId;
		User userData = new User();
		
		dbHelper.getConn();
		
		ResultSet rs = dbHelper.getSql(getUserSql);
		
		try {
			while (rs.next())
			{
				
				userData.userId = rs.getInt("id");
				userData.name = rs.getString("name");
				userData.sex = rs.getInt("sex");
				userData.age = rs.getInt("age");
				userData.win_count = rs.getInt("win_count");
				userData.fail_count = rs.getInt("fail_count");
				userData.equal_count = rs.getInt("equal_count");
				userData.brag_count = rs.getInt("brag_count");

				// userId is unique, so only one user data can be got.
				break;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			dbHelper.closeConn();
		}
		
		return userData;
	}
	
	public DataState isUserDataChanged(int userId)
	{
		String getUserSql = "select name_changed, picture_changed from tb_user_data where id=" + userId;
		
		Logger.getGlobal().info("before get sql ");
		dbHelper.getConn();
		long mid = System.nanoTime();
		
		ResultSet rs = dbHelper.getSql(getUserSql);
		
		DataState isChanged = new DataState();
		
		try {
			while (rs.next())
			{
				isChanged.name_changed = rs.getInt("name_changed");
				isChanged.picture_changed = rs.getInt("picture_changed");
								
				// userId is unique, so only one user data can be got.
				break;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			dbHelper.closeConn();
		}
		Logger.getGlobal().info("after get sql " + (System.nanoTime() - mid));
		return isChanged;
	}
	
	// update means use the newest name of 
	public boolean updateUserData(int userId, String userName, int sex, int age)
	{
		boolean ret = false;
		
		String updateSql = "update tb_user_data set name='" + userName + "', sex=" + sex + ", age=" + age + " where id=" + userId;
		
		dbHelper.getConn();
		ret = dbHelper.setSql(updateSql);
		dbHelper.closeConn();
		
		return ret;
	}
	
	public boolean changeUserName(int userId, String userName)
	{
		boolean ret = false;
		int nameChanged = 1;
		String updateSql = "update tb_user_data set name='" + userName + "', name_changed=" + nameChanged + " where id=" + userId;
		
		dbHelper.getConn();
		ret = dbHelper.setSql(updateSql);
		dbHelper.closeConn();
		
		return ret;
	}
	
	public boolean changePicture(int userId)
	{
		boolean ret = false;
		int pirctureChanged = 1;
		String updateSql = "update tb_user_data set picture_changed='" + pirctureChanged + " where id=" + userId;
		
		dbHelper.getConn();
		ret = dbHelper.setSql(updateSql);
		dbHelper.closeConn();
		
		return ret;
	}
	
	// used for search people.
	public User[] getUsersByKeywords(String keywords)
	{
		String getUsersSql = "select id, name, sex, age, win_count, fail_count, equal_count, brag_count " +
							 "from tb_user_data where login_number like '%" + keywords + "%' or name like '%" + keywords + "%'"; 
		
		dbHelper.getConn();		
		ResultSet rs = dbHelper.getSql(getUsersSql);
		
		int rows = 0;
		try {
			rows = rs.getRow();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		User[] userDatas = new User[rows];
		
		int count = 0;
		
		try {
			while (rs.next())
			{
				if (count == rows) {
					Logger.getGlobal().info("count is longger than rows, error.");
					break;
				}
				userDatas[count].userId = rs.getInt("id");
				userDatas[count].name = rs.getString("name");
				userDatas[count].sex = rs.getInt("sex");
				userDatas[count].age = rs.getInt("age");
				userDatas[count].win_count = rs.getInt("win_count");
				userDatas[count].fail_count = rs.getInt("fail_count");
				userDatas[count].equal_count = rs.getInt("equal_count");
				userDatas[count].brag_count = rs.getInt("brag_count");
				
				count++;				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			dbHelper.closeConn();
		}
		
		
		
		return userDatas;
	}
	
	public Range[] getWeekRange()
	{		
		String weakRageSql = "select id, name, sex, age, week_win, week_brag, week_fail, week_equal from tb_user_data " +
							"order by week_win, week_brag, week_equal desc, week_fail asc";		
		String getTopSql = "select top " + rangeShowNumber + " from (" + weakRageSql + ")";

		dbHelper.getConn();		
		ResultSet rs = dbHelper.getSql(getTopSql);
		
		int rows = 0;
		try {
			rows = rs.getRow();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		Range[] rangeDatas = new Range[rows];
		
		int count = 0;
		
		try {
			while (rs.next())
			{
				if (count == rows) {
					Logger.getGlobal().info("count is longger than rows, error.");
					break;
				}
				rangeDatas[count].id = rs.getInt("id");
				rangeDatas[count].name = rs.getString("name");
				rangeDatas[count].sex = rs.getInt("sex");
				rangeDatas[count].age = rs.getInt("age");
				rangeDatas[count].win = rs.getInt("week_win");
				rangeDatas[count].brag = rs.getInt("week_brag");
				rangeDatas[count].fail = rs.getInt("week_fail");
				rangeDatas[count].equal = rs.getInt("week_equal");
				
				count++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			dbHelper.closeConn();
		}
		
		return rangeDatas;
	}
	
	public Range[] getMonthRange()
	{
		String monthRageSql = "select id, name, sex, age, month_win, month_brag, month_fail, month_equal from tb_user_data " +
							"order by month_win, month_brag, month_equal desc, month_fail asc";
		
		String getTopSql = "select top " + rangeShowNumber + " from (" + monthRageSql + ")";
		
		
		dbHelper.getConn();		
		ResultSet rs = dbHelper.getSql(getTopSql);
		
		int rows = 0;
		try {
			rows = rs.getRow();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		Range[] rangeDatas = new Range[rows];
		
		int count = 0;
		
		try {
			while (rs.next())
			{
				if (count == rows) {
					Logger.getGlobal().info("count is longger than rows, error.");
					break;
				}
				rangeDatas[count].id = rs.getInt("id");
				rangeDatas[count].name = rs.getString("name");
				rangeDatas[count].sex = rs.getInt("sex");
				rangeDatas[count].age = rs.getInt("age");
				rangeDatas[count].win = rs.getInt("month_win");
				rangeDatas[count].brag = rs.getInt("month_brag");
				rangeDatas[count].fail = rs.getInt("month_fail");
				rangeDatas[count].equal = rs.getInt("month_equal");
				
				count++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			dbHelper.closeConn();
		}
				
		return rangeDatas;
	}
	
	public Range[] getAllRange()
	{
		String allRageSql = "select id, name, sex, age, win_count, fail_count, equal_count, brag_count from tb_user_data " +
				"order by win_count, brag_count, equal_count desc, fail_count asc";

		String getTopSql = "select top " + rangeShowNumber + " from (" + allRageSql + ")";
		
		dbHelper.getConn();		
		ResultSet rs = dbHelper.getSql(getTopSql);
		
		int rows = 0;
		try {
			rows = rs.getRow();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		Range[] rangeDatas = new Range[rows];

		int count = 0;
		
		try {
		while (rs.next())
		{
			if (count == rows) {
				Logger.getGlobal().info("count is longger than rows, error.");
				break;
			}
			rangeDatas[count].id = rs.getInt("id");
			rangeDatas[count].name = rs.getString("name");
			rangeDatas[count].sex = rs.getInt("sex");
			rangeDatas[count].age = rs.getInt("age");
			rangeDatas[count].win = rs.getInt("win_count");
			rangeDatas[count].brag = rs.getInt("fail_count");
			rangeDatas[count].fail = rs.getInt("equal_count");
			rangeDatas[count].equal = rs.getInt("brag_count");
			
			count++;
		}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			dbHelper.closeConn();
		}
				
		return rangeDatas;
	}
	
	// get the last login date to clean the week range or month range. do this every time when one round is end.
	private LastLoginDate getLastLoginDate(int userId)
	{
		String LastLoginSql = "select last_login_month, last_login_week from tb_user_data where id=" + userId;
		LastLoginDate lastLoginDate = new LastLoginDate();
		
		dbHelper.getConn();
		
		ResultSet rs = dbHelper.getSql(LastLoginSql);
		
		try {
			while (rs.next())
			{
				lastLoginDate.last_login_month = rs.getInt("last_login_month");
				lastLoginDate.last_login_week = rs.getInt("last_login_week");
				
				// userId is unique, so only one user data can be got.
				break;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			dbHelper.closeConn();
		}
		
		return lastLoginDate;
	}
	
	private boolean preprocessRange(int userId)
	{
		boolean ret = false;
		int needClean = 0;  // 0 means clean all, 1 means clean week, 2 means no need clean.
				
		//get current date.
		Calendar c = Calendar.getInstance();
		int currMonth = c.get(Calendar.MONTH);
		int currWeekInYear = c.get(Calendar.WEEK_OF_YEAR);
		
		LastLoginDate lastLoginDate = getLastLoginDate(userId);

		if (lastLoginDate.INVALID_DATE != lastLoginDate.last_login_week && currWeekInYear == lastLoginDate.last_login_week)
		{
			needClean = 2;
		}
		else if (lastLoginDate.INVALID_DATE != lastLoginDate.last_login_month && currMonth == lastLoginDate.last_login_month)
		{
			needClean = 1;
		}
		
		String preprocessSql = "";
		String preSql = "update tb_user_data set ";
		String cleanMonth = "month_win=0, month_brag=0, month_fail=0, month_equal=0,";
		String cleanWeek = "week_win=0, week_brag=0, week_fail=0, week_equal=0 ";
		String updateMonth = "last_login_month=" + currMonth + ", ";
		String updateWeek = "last_login_week=" + currWeekInYear + ", ";
		String sufSql = "where id=" + userId;
		
		if (0 == needClean) // clean all
		{
			preprocessSql = preSql + updateMonth + updateWeek + cleanMonth + cleanWeek + sufSql;
		} else if (1 == needClean) // only clean week
		{
			preprocessSql = preSql + updateWeek + cleanWeek + sufSql;
		}
		else // no need clean anything.
		{
			return true;
		}
		// clean week range or month range
		dbHelper.getConn();
		ret = dbHelper.setSql(preprocessSql);
		dbHelper.closeConn();
		
		return ret;
	}
	
	private boolean saveRangeData(int userId, String processSql)
	{
		boolean ret = false;
		
		if (false == (ret = preprocessRange(userId)))
		{
			return ret;
		}
				
		dbHelper.getConn();
		ret = dbHelper.setSql(processSql);
		dbHelper.closeConn();
		
		return ret;
	}
	
	public boolean saveWin(int userId, int brag)
	{
		String winSql = "update tb_user_data set win_count=win_count+1, month_win=month_win+1, weak_win=weak_win+1, " + 
				"brag_count=brag_count+" + brag + ", month_brag=month_brag+" + brag + ", weak_brag=weak_brag+" + brag +
				" where id=" + userId;
				
		return saveRangeData(userId, winSql);
	}
	
	public boolean saveFail(int userId)
	{
		String failSql = "update tb_user_data set fail_count=fail_count+1, month_fail=month_fail+1, weak_fail=weak_fail+1 " + 
				" where id=" + userId;
		
		return saveRangeData(userId, failSql);
	}
	
	public boolean saveEqual(int userId)
	{
		String failSql = "update tb_user_data set equal_count=equal_count+1, month_equal=month_equal+1, weak_equal=weak_equal+1 " + 
				" where id=" + userId;
		
		return saveRangeData(userId, failSql);
	}
}

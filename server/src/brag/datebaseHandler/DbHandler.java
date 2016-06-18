package brag.datebaseHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
	
	// json {"id":int, "name":String, "sex":int, "age":int, "win_count":int, "fail_count":int, "equal_count":int, "brag_count":int}
	public String getUserDataById(int userId)
	{
		String getUserSql = "select id, name, sex, age, win_count, fail_count, equal_count, brag_count from tb_user_data where id=" + userId;
		JSONObject json = new JSONObject();
		
		dbHelper.getConn();
		
		ResultSet rs = dbHelper.getSql(getUserSql);
		
		try {
			while (rs.next())
			{
				json.put("id", rs.getInt("id"));
				json.put("name", rs.getString("name"));
				json.put("sex", rs.getInt("sex"));
				json.put("age", rs.getInt("age"));
				json.put("win_count", rs.getInt("win_count"));
				json.put("fail_count", rs.getInt("fail_count"));
				json.put("equal_count", rs.getInt("equal_count"));
				json.put("brag_count", rs.getInt("brag_count"));
				
				// userId is unique, so only one user data can be got.
				break;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			dbHelper.closeConn();
		}
		
		return json.toString();
	}
	
	public String isUserDataChanged(int userId)
	{
		String getUserSql = "select name_changed, picture_changed from tb_user_data where id=" + userId;
		JSONObject json = new JSONObject();
		
		dbHelper.getConn();
		
		ResultSet rs = dbHelper.getSql(getUserSql);
		
		try {
			while (rs.next())
			{
				json.put("name_changed", rs.getInt("name_changed"));
				json.put("picture_changed", rs.getInt("picture_changed"));
				
				// userId is unique, so only one user data can be got.
				break;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			dbHelper.closeConn();
		}
		
		return json.toString();
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
	
	// get the flag if user changed the name or picture manually.
	public String getNameOrPictureChanged(int userId)
	{
		String changedSql = "get name_changed, picture_changed from tb_user_data where id=" + userId;
		
		JSONObject json = new JSONObject();
		
		dbHelper.getConn();		
		ResultSet rs = dbHelper.getSql(changedSql);
		
		try {
			while (rs.next())
			{
				json.put("name_changed", rs.getInt("name_changed"));
				json.put("picture_changed", rs.getInt("picture_changed"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return json.toString();
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
	public String getUsersByKeywords(String keywords)
	{
		String getUsersSql = "select id, name, sex, age, win_count, fail_count, equal_count, brag_count " +
							 "from tb_user_data where login_number like '%" + keywords + "%' or name like '%" + keywords + "%'"; 
		JSONObject json = new JSONObject();
		JSONArray jsonMembers = new JSONArray();
		
		dbHelper.getConn();		
		ResultSet rs = dbHelper.getSql(getUsersSql);
		
		try {
			while (rs.next())
			{
				JSONObject jsonMember = new JSONObject();
				jsonMember.put("id", rs.getInt("id"));
				jsonMember.put("name", rs.getString("name"));
				jsonMember.put("sex", rs.getInt("sex"));
				jsonMember.put("age", rs.getInt("age"));
				jsonMember.put("win_count", rs.getInt("win_count"));
				jsonMember.put("fail_count", rs.getInt("fail_count"));
				jsonMember.put("equal_count", rs.getInt("equal_count"));
				jsonMember.put("brag_count", rs.getInt("brag_count"));
				
				jsonMembers.put(jsonMember);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			dbHelper.closeConn();
		}
		
		try {
			json.put("users", jsonMembers);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return json.toString();
	}
	
	public String getWeekRange()
	{
		String weakRageSql = "select id, name, sex, age, week_win, week_brag, week_fail, week_equal from tb_user_data " +
							"order by week_win, week_brag, week_equal desc, week_fail asc";
		
		JSONObject json = new JSONObject();
		JSONArray jsonMembers = new JSONArray();
		
		dbHelper.getConn();		
		ResultSet rs = dbHelper.getSql(weakRageSql);
		
		int count = 0;
		
		try {
			while (rs.next())
			{
				JSONObject jsonMember = new JSONObject();
				jsonMember.put("id", rs.getInt("id"));
				jsonMember.put("name", rs.getString("name"));
				jsonMember.put("sex", rs.getInt("sex"));
				jsonMember.put("age", rs.getInt("age"));
				jsonMember.put("week_win", rs.getInt("week_win"));
				jsonMember.put("week_brag", rs.getInt("week_brag"));
				jsonMember.put("week_fail", rs.getInt("week_fail"));
				jsonMember.put("week_equal", rs.getInt("week_equal"));
				
				jsonMembers.put(jsonMember);
				
				count++;
				if (rangeShowNumber == count)
				{
					break;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			dbHelper.closeConn();
		}
		
		try {
			json.put("users", jsonMembers);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return json.toString();
	}
	
	public String getMonthRange()
	{
		String weakRageSql = "select id, name, sex, age, month_win, month_brag, month_fail, month_equal from tb_user_data " +
							"order by month_win, month_brag, month_equal desc, month_fail asc";
		
		JSONObject json = new JSONObject();
		JSONArray jsonMembers = new JSONArray();
		
		dbHelper.getConn();		
		ResultSet rs = dbHelper.getSql(weakRageSql);
		
		int count = 0;
		
		try {
			while (rs.next())
			{
				JSONObject jsonMember = new JSONObject();
				jsonMember.put("id", rs.getInt("id"));
				jsonMember.put("name", rs.getString("name"));
				jsonMember.put("sex", rs.getInt("sex"));
				jsonMember.put("age", rs.getInt("age"));
				jsonMember.put("month_win", rs.getInt("month_win"));
				jsonMember.put("month_brag", rs.getInt("month_brag"));
				jsonMember.put("month_fail", rs.getInt("month_fail"));
				jsonMember.put("month_equal", rs.getInt("month_equal"));
				
				jsonMembers.put(jsonMember);
				
				count++;
				if (rangeShowNumber == count)
				{
					break;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			dbHelper.closeConn();
		}
		
		try {
			json.put("users", jsonMembers);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return json.toString();
	}
	
	public String getAllRange()
	{
		String allRageSql = "select id, name, sex, age, win_count, fail_count, equal_count, brag_count from tb_user_data " +
				"order by win_count, brag_count, equal_count desc, fail_count asc";

		JSONObject json = new JSONObject();
		JSONArray jsonMembers = new JSONArray();
		
		dbHelper.getConn();		
		ResultSet rs = dbHelper.getSql(allRageSql);
		
		int count = 0;
		
		try {
		while (rs.next())
		{
			JSONObject jsonMember = new JSONObject();
			jsonMember.put("id", rs.getInt("id"));
			jsonMember.put("name", rs.getString("name"));
			jsonMember.put("sex", rs.getInt("sex"));
			jsonMember.put("age", rs.getInt("age"));
			jsonMember.put("win_count", rs.getInt("win_count"));
			jsonMember.put("fail_count", rs.getInt("fail_count"));
			jsonMember.put("equal_count", rs.getInt("equal_count"));
			jsonMember.put("brag_count", rs.getInt("brag_count"));
			
			jsonMembers.put(jsonMember);
			
			count++;
			if (rangeShowNumber == count)
			{
				break;
			}
		}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			dbHelper.closeConn();
		}
		
		try {
			json.put("users", jsonMembers);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return json.toString();
	}
	
	// get the last login date to clean the week range or month range. do this every time when one round is end.
	private String getLastLoginDate(int userId)
	{
		String LastLoginSql = "select last_login_month, last_login_week from tb_user_data where id=" + userId;
		JSONObject json = new JSONObject();
		
		dbHelper.getConn();
		
		ResultSet rs = dbHelper.getSql(LastLoginSql);
		
		try {
			while (rs.next())
			{
				json.put("last_login_month", rs.getInt("last_login_month"));
				json.put("last_login_week", rs.getString("last_login_week"));
				
				// userId is unique, so only one user data can be got.
				break;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			dbHelper.closeConn();
		}
		
		return json.toString();
	}
	
	private boolean preprocessRange(int userId)
	{
		boolean ret = false;
		int needClean = 0;  // 0 means clean all, 1 means clean week, 2 means no need clean.
				
		//get current date.
		Calendar c = Calendar.getInstance();
		int currMonth = c.get(Calendar.MONTH);
		int currWeekInYear = c.get(Calendar.WEEK_OF_YEAR);
		
		String lastLoginDate = getLastLoginDate(userId);
		try {
			JSONObject lastLoginJson = new JSONObject(lastLoginDate);
			if (0 != lastLoginJson.length() && currWeekInYear == lastLoginJson.getInt("last_login_week"))
			{
				needClean = 2;
			}
			else if (0 != lastLoginJson.length() && currMonth == lastLoginJson.getInt("last_login_month"))
			{
				needClean = 1;
			}			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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

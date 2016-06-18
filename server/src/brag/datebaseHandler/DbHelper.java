package brag.datebaseHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DbHelper {
	private Connection conn = null;
	
	public DbHelper()
	{
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("can not find the driver for mysql");
			e.printStackTrace();
		}
	}
	
	public void getConn()
	{
		String url = "jdbc:mysql://localhost:3306/brag";
		String username = "root";
		String password = "123456";
		
		try {
			conn = DriverManager.getConnection(url, username, password);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("can not connected to mysql.");
			e.printStackTrace();
		}		
	}
	
	public void closeConn()
	{
		if (conn != null)
		{
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	
	public boolean setSql(String sql)
	{
		boolean isSuccess = false;
		
		try {
			if (conn != null)
			{
				PreparedStatement statement = conn.prepareStatement(sql);
				statement.executeUpdate();
				isSuccess = true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return isSuccess;
	}
	
	public ResultSet getSql(String sql)
	{
		ResultSet rs = null;
		
		try {
			PreparedStatement statement = conn.prepareStatement(sql);
			rs = statement.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		return rs;
	}
}
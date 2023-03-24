package operate;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class Log {

	public static boolean Log_log(String 用户名, String passId) throws Exception, IOException {
		
		Connection con=jdbc.Control.getcon();

		boolean result = false;
		String sql = "select * from log where 用户名='" + 用户名 + "' AND passId='" + passId + "'";
		PreparedStatement pstmt = con.prepareStatement(sql);
		// 设置参数
		ResultSet resultSet = pstmt.executeQuery(sql);
		if (resultSet.next()) {
			result = true;
		}

		resultSet.close();
		pstmt.close();
		con.close();

		return result;
	}

	public static boolean register(String text, String text2) throws Exception {
		Connection con=jdbc.Control.getcon();

		//检查注册时用户名是否重复
		boolean exist_user = false;
		String sql = "select * from log where 用户名='" + text + "'";
		PreparedStatement pstmt = con.prepareStatement(sql);
		ResultSet resultSet = pstmt.executeQuery(sql);
		if (resultSet.next()) {
			exist_user = true;
		}
		if (exist_user == true) {
			resultSet.close();
			pstmt.close();
			con.close();
			return false;
		} else {
			sql = "insert into log(用户名,passId,身份) VALUES (?,?,?)";
			pstmt = con.prepareStatement(sql);
			// 设置参数
			pstmt.setString(1, text);
			pstmt.setString(2, text2);
			pstmt.setString(3, "用户");

			pstmt.executeUpdate();
			pstmt.close();
			con.close();
			return true;
		}

	}

	public static boolean Log_Delete(String name) throws Exception {
		Connection con=jdbc.Control.getcon();

		int getid = -1;
		String sql1 = "select Id from log where 用户名='" + name + "'";
		String sql2 = "DELETE FROM client WHERE Id=" + getid;
		String sql3 = "DELETE FROM log where 用户名='" + name + "'";
		PreparedStatement pstmt;
		ResultSet resultSet;

		try {
			// 开启事务，三条SQL语句合并为一条事务
			con.setAutoCommit(false);
			pstmt = con.prepareStatement(sql1);
			resultSet = pstmt.executeQuery(sql1);
			while (resultSet.next()) {
				getid = resultSet.getInt("Id");
			}

			pstmt = con.prepareStatement(sql2);
			pstmt.executeUpdate();

			pstmt = con.prepareStatement(sql3);
			pstmt.executeUpdate();
			// 提交事物
			con.commit();
			resultSet.close();
			pstmt.close();
			con.close();
		} catch (Exception e) {
			con.rollback();
			e.printStackTrace();
		}
		return true;
	}

	public static boolean Logmanage_log(String text, String text2) throws IOException, Exception {	
		Connection con=jdbc.Control.getcon();
		
		boolean result = false;
		String sql = "select * from log where 用户名='" + text + "' AND passId='" + text2 + "' AND 身份='管理员'";
		PreparedStatement pstmt = con.prepareStatement(sql);
		ResultSet resultSet = pstmt.executeQuery(sql);
		if (resultSet.next()) {
			result = true;
		}

		resultSet.close();
		pstmt.close();
		con.close();

		return result;
	}
}

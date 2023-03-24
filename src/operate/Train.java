package operate;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;


public class Train {

	public static List<List<String>> train_check(String start, String end) throws Exception, IOException {
		List<List<String>> re = new ArrayList<List<String>>();
		Connection con=jdbc.Control.getcon();

		// JDBC不支持别名
		String sql1 = "SELECT * FROM stop WHERE 站名='" + start + "'";
		String sql2 = "SELECT * FROM stop WHERE 站名='" + end + "'";
		PreparedStatement pstmt1 = con.prepareStatement(sql1);
		ResultSet resultSet1 = pstmt1.executeQuery(sql1);

		while (resultSet1.next()) {
			List<String> one_line1 = new ArrayList<String>();
			String 车次号 = resultSet1.getString("车次号");
			Time 发车时间 = resultSet1.getTime("发车时间");
			PreparedStatement pstmt2 = con.prepareStatement(sql2);
			ResultSet resultSet2 = pstmt2.executeQuery(sql2);
			while (resultSet2.next()) {
				String 车次号_end = resultSet2.getString("车次号");
				Time 到站时间 = resultSet2.getTime("到站时间");
				if (车次号.equals(车次号_end) && 发车时间.before(到站时间)) {
					one_line1.add(车次号);
					one_line1.add(发车时间.toString());
					one_line1.add(到站时间.toString());
					re.add(one_line1);
				}
			}
			resultSet2.close();
			pstmt2.close();
		}

		resultSet1.close();
		pstmt1.close();

		con.close();

		return re;
	}

	public static String buyticket(String start, String end, String 车次, String 用户名) throws Exception {
		Connection con=jdbc.Control.getcon();

		String result = null;
		Time 出发时间 = null;
		Time 到站时间 = null;
		String 乘车人 = null;
		int 购买用户ID = -1;

		String sql1 = "SELECT * FROM stop WHERE 站名='" + start + "' AND 车次号='" + 车次 + "'";
		PreparedStatement pstmt1 = con.prepareStatement(sql1);
		ResultSet resultSet1 = pstmt1.executeQuery(sql1);
		while (resultSet1.next()) {
			出发时间 = resultSet1.getTime("发车时间");
		}
		if (出发时间 == null) {
			result = "始发站不存在";
			return result;
		}

		String sql2 = "SELECT * FROM stop WHERE 站名='" + end + "' AND 车次号='" + 车次 + "'";
		pstmt1 = con.prepareStatement(sql2);
		resultSet1 = pstmt1.executeQuery(sql2);
		while (resultSet1.next()) {
			到站时间 = resultSet1.getTime("到站时间");
		}
		if (到站时间 == null) {
			result = "终点站不存在";
			return result;
		}

		String sql4 = "SELECT * FROM client WHERE id=(select ID from log where 用户名='" + 用户名 + "')";
		pstmt1 = con.prepareStatement(sql4);
		resultSet1 = pstmt1.executeQuery(sql4);
		while (resultSet1.next()) {
			乘车人 = resultSet1.getString("trueName");
			购买用户ID = resultSet1.getInt("ID");
		}
		if (乘车人 == null) {
			result = "请完善用户信息";
			return result;
		}

		String sql3 = "insert into useorder(出发站,目的站,发车时间,到站时间,车次,乘车人,购买用户ID) VALUES (?,?,?,?,?,?,?)";
		pstmt1 = con.prepareStatement(sql3);
		pstmt1.setString(1, start);
		pstmt1.setString(2, end);
		pstmt1.setTime(3, 出发时间);
		pstmt1.setTime(4, 到站时间);
		pstmt1.setString(5, 车次);
		pstmt1.setString(6, 乘车人);
		pstmt1.setInt(7, 购买用户ID);
		pstmt1.executeUpdate();
		result = "购买成功";

		resultSet1.close();
		pstmt1.close();
		con.close();

		return result;
	}

	public static List<List<String>> train_new_check(String start, String end) throws Exception {
		List<List<String>> re = new ArrayList<List<String>>();
		Connection con=jdbc.Control.getcon();
		// JDBC不支持别名
		String sql1 = "SELECT * FROM stop WHERE 站名='" + start + "'";
		String sql2 = "SELECT * FROM stop WHERE 站名='" + end + "'";
		int start_index = 0;
		int end_index = 0;
		PreparedStatement pstmt1 = con.prepareStatement(sql1);
		ResultSet resultSet1 = pstmt1.executeQuery(sql1);

		while (resultSet1.next()) {
			List<String> one_line1 = new ArrayList<String>();
			String 车次号 = resultSet1.getString("车次号");
			Time 发车时间 = resultSet1.getTime("发车时间");
			PreparedStatement pstmt2 = con.prepareStatement(sql2);
			ResultSet resultSet2 = pstmt2.executeQuery(sql2);
			while (resultSet2.next()) {
				String 车次号_end = resultSet2.getString("车次号");
				Time 到站时间 = resultSet2.getTime("到站时间");
				if (车次号.equals(车次号_end) && 发车时间.before(到站时间)) {
					one_line1.add(车次号);
					one_line1.add(发车时间.toString());
					one_line1.add(到站时间.toString());
					start_index = resultSet1.getInt("经过次序");
					end_index = resultSet2.getInt("经过次序");

					String shopname = null;
					for (int i = start_index; i <= end_index; i++) {
						String sql3 = "SELECT * FROM stop WHERE 车次号='" + 车次号 + "' and 经过次序=" + i + "";
						PreparedStatement pstmt3 = con.prepareStatement(sql3);
						ResultSet resultSet3 = pstmt3.executeQuery(sql3);
						while (resultSet3.next()) {
							if (shopname == null) {
								shopname = resultSet3.getString("站名");
							} else {
								shopname = shopname + "-" + resultSet3.getString("站名");
							}
						}
						resultSet3.close();
						pstmt3.close();
					}
					one_line1.add(shopname);
					re.add(one_line1);
				}
			}
			resultSet2.close();
			pstmt2.close();
		}

		resultSet1.close();
		pstmt1.close();

		con.close();

		return re;
	}

	public static List<List<String>> train_new_checkleft(String start, String end, String 列车号) throws Exception {
		List<List<String>> re = new ArrayList<List<String>>();
		Connection con=jdbc.Control.getcon();

		String sql1 = "SELECT * FROM stop WHERE 站名='" + start + "' and 车次号='" + 列车号 + "'";
		String sql2 = "SELECT * FROM stop WHERE 站名='" + end + "' and 车次号='" + 列车号 + "'";
		int start_index = 0;
		int end_index = 0;
		PreparedStatement pstmt1 = con.prepareStatement(sql1);
		ResultSet resultSet1 = pstmt1.executeQuery(sql1);

		while (resultSet1.next()) {
			start_index = resultSet1.getInt("经过次序");
		}
		pstmt1.close();
		resultSet1.close();

		pstmt1 = con.prepareStatement(sql2);
		resultSet1 = pstmt1.executeQuery(sql2);
		while (resultSet1.next()) {
			end_index = resultSet1.getInt("经过次序");
		}
		pstmt1.close();
		resultSet1.close();

		String sql3 = "SELECT * FROM leftover" + 列车号 + " GROUP BY 日期";
		int data_num = 0;
		pstmt1 = con.prepareStatement(sql3);
		resultSet1 = pstmt1.executeQuery(sql3);
		while (resultSet1.next()) {
			List<String> one_line1 = new ArrayList<String>();
			one_line1.add(resultSet1.getString("日期"));
			re.add(one_line1);
			data_num++;
		}
		pstmt1.close();
		resultSet1.close();

		String sql_select = "select count(*) from leftover" + 列车号 + " where ";
		int i = start_index;
		while (true) {
			sql_select += (change(i) + "=0 ");
			if (i == end_index - 1) {
				break;
			}
			i++;
			sql_select += " and ";
		}

		for (int x = 0; x < data_num; x++) {
			String sql_select_shangwu = sql_select + " and 日期='" + re.get(x).get(0)
					+ "' and 车厢号 in (select 车厢号 from carriage where 座位类型='商务座' and 车次号='" + 列车号 + "');";
			pstmt1 = con.prepareStatement(sql_select_shangwu);
			resultSet1 = pstmt1.executeQuery(sql_select_shangwu);
			while (resultSet1.next()) {
				re.get(x).add(resultSet1.getString("count(*)"));
			}
			pstmt1.close();
			resultSet1.close();

			String sql_select_1 = sql_select + " and 日期='" + re.get(x).get(0)
					+ "' and 车厢号 in (select 车厢号 from carriage where 座位类型='一等座' and 车次号='" + 列车号 + "');";
			pstmt1 = con.prepareStatement(sql_select_1);
			resultSet1 = pstmt1.executeQuery(sql_select_1);
			while (resultSet1.next()) {
				re.get(x).add(resultSet1.getString("count(*)"));
			}
			pstmt1.close();
			resultSet1.close();

			String sql_select_2 = sql_select + " and 日期='" + re.get(x).get(0)
					+ "' and 车厢号 in (select 车厢号 from carriage where 座位类型='二等座' and 车次号='" + 列车号 + "');";
			pstmt1 = con.prepareStatement(sql_select_2);
			resultSet1 = pstmt1.executeQuery(sql_select_2);
			while (resultSet1.next()) {
				re.get(x).add(resultSet1.getString("count(*)"));
			}
			pstmt1.close();
			resultSet1.close();

		}
		con.close();
		return re;
	}

	private static String change(int i) {
		if (i == 1)
			return "一";
		else if (i == 2)
			return "二";
		else if (i == 3)
			return "三";
		else if (i == 4)
			return "四";
		else if (i == 5)
			return "五";
		else if(i==6)
			return "六";
		else if (i == 7)
			return "七";
		else if (i == 8)
			return "八";
		else if (i == 9)
			return "九";
		else if (i == 10)
			return "十";
		else if(i==11)
			return "十一";

		return null;
	}

	public static String newbuyticket(String data, String type_seat, String start, String end, String 车次, String 用户名)
			throws Exception {
		Connection con=jdbc.Control.getcon();

		String result = null;
		String 出发时间 = null;
		String 到站时间 = null;
		String 乘车人 = null;
		int 购买用户ID = -1;

		String sql1 = "SELECT * FROM stop WHERE 站名='" + start + "' and 车次号='" + 车次 + "'";
		String sql2 = "SELECT * FROM stop WHERE 站名='" + end + "' and 车次号='" + 车次 + "'";
		int start_index = 0;
		int end_index = 0;
		PreparedStatement pstmt1 = con.prepareStatement(sql1);
		ResultSet resultSet1 = pstmt1.executeQuery(sql1);

		while (resultSet1.next()) {
			start_index = resultSet1.getInt("经过次序");
		}
		pstmt1.close();
		resultSet1.close();

		pstmt1 = con.prepareStatement(sql2);
		resultSet1 = pstmt1.executeQuery(sql2);
		while (resultSet1.next()) {
			end_index = resultSet1.getInt("经过次序");
		}
		pstmt1.close();
		resultSet1.close();

		String sql1_1 = "SELECT * FROM stop WHERE 站名='" + start + "' AND 车次号='" + 车次 + "'";
		pstmt1 = con.prepareStatement(sql1_1);
		resultSet1 = pstmt1.executeQuery(sql1_1);
		while (resultSet1.next()) {
			出发时间 = resultSet1.getString("发车时间");
		}
		if (出发时间 == null) {
			result = "始发站不存在";
			return result;
		}
		pstmt1.close();
		resultSet1.close();

		String sql2_2 = "SELECT * FROM stop WHERE 站名='" + end + "' AND 车次号='" + 车次 + "'";
		pstmt1 = con.prepareStatement(sql2_2);
		resultSet1 = pstmt1.executeQuery(sql2_2);
		while (resultSet1.next()) {
			到站时间 = resultSet1.getString("到站时间");
		}
		if (到站时间 == null) {
			result = "终点站不存在";
			return result;
		}
		pstmt1.close();
		resultSet1.close();

		String sql4 = "SELECT * FROM client WHERE id=(select ID from log where 用户名='" + 用户名 + "')";
		pstmt1 = con.prepareStatement(sql4);
		resultSet1 = pstmt1.executeQuery(sql4);
		while (resultSet1.next()) {
			乘车人 = resultSet1.getString("trueName");
			购买用户ID = resultSet1.getInt("ID");
		}
		if (乘车人 == null) {
			result = "请完善用户信息";
			return result;
		}
		pstmt1.close();
		resultSet1.close();

		String sql_select = "select * from leftover" + 车次 + " where ";
		int i = start_index;
		while (true) {
			sql_select += (change(i) + "=0 ");
			if (i == end_index - 1) {
				break;
			}
			i++;
			sql_select += " and ";
		}

		String 车厢 = null;
		String 座位 = null;
		String sql_select_shangwu = sql_select + " and 日期='" + data
				+ "' and 车厢号 in (select 车厢号 from carriage where 座位类型='" + type_seat + "' and 车次号='" + 车次 + "');";
		pstmt1 = con.prepareStatement(sql_select_shangwu);
		resultSet1 = pstmt1.executeQuery(sql_select_shangwu);
		while (resultSet1.next()) {
			车厢 = resultSet1.getString("车厢号");
			座位 = resultSet1.getString("座位号");
			break;
		}
		pstmt1.close();
		resultSet1.close();

		boolean continue_judge=true;
		try {
			String ins = "insert into useorder(出发站,目的站,发车时间,到站时间,车次,乘车人,购买用户ID,车厢,座位,日期) VALUES (?,?,?,?,?,?,?,?,?,?);";
			PreparedStatement pstmt2 = con.prepareStatement(ins);
			pstmt2.setString(1, start);
			pstmt2.setString(2, end);
			pstmt2.setString(3, 出发时间);
			pstmt2.setString(4, 到站时间);
			pstmt2.setString(5, 车次);
			pstmt2.setString(6, 乘车人);
			pstmt2.setInt(7, 购买用户ID);
			pstmt2.setString(8, 车厢);
			pstmt2.setString(9, 座位);
			pstmt2.setString(10, data);

			pstmt2.executeUpdate();
			result = "购买成功";

			pstmt2.close();
		} catch (Exception e) {
			result = "操作失败，当前乘坐人在该事件已有行程";
			continue_judge=false;
		}
		
		if(continue_judge==true) {
			String sql_left="Update leftover"+ 车次 + " Set ";
			int t = start_index;
			while (true) {
				sql_left += (change(t) + "=1");
				if (t == end_index - 1) {
					break;
				}
				t++;
				sql_left += ",";
			}
			
			sql_left+=" where 日期='"+data+"' and 车厢号='"+车厢+"' and 座位号='"+座位+"'";
			System.out.println(sql_left);
			
			PreparedStatement pstmt3 = con.prepareStatement(sql_left);
			pstmt3.executeUpdate();
			pstmt3.close();
		}

		con.close();
		return result;

	}

	public static List<List<String>> showorder(String 用户名) throws Exception {
		List<List<String>> re = new ArrayList<List<String>>();
		Connection con=jdbc.Control.getcon();

		String sql1 = "SELECT * FROM useorder WHERE 购买用户ID=(select ID from log where 用户名='"+用户名+"') ORDER BY 日期 ASC";
		PreparedStatement pstmt1 = con.prepareStatement(sql1);
		ResultSet resultSet1 = pstmt1.executeQuery(sql1);

		while (resultSet1.next()) {
			List<String> one_line1 = new ArrayList<String>();
			one_line1.add(resultSet1.getString("编号"));
			one_line1.add(resultSet1.getString("日期"));
			one_line1.add(resultSet1.getString("出发站"));
			one_line1.add(resultSet1.getString("目的站"));
			one_line1.add(resultSet1.getString("发车时间"));
			one_line1.add(resultSet1.getString("到站时间"));
			one_line1.add(resultSet1.getString("乘车人"));
			one_line1.add(resultSet1.getString("车次"));
			one_line1.add(resultSet1.getString("车厢"));
			one_line1.add(resultSet1.getString("座位"));
			
			re.add(one_line1);
		}
		pstmt1.close();
		resultSet1.close();
		con.close();
		return re;
	}

	public static String releaseticket(String 编号, String 日期, String 车次, String 车厢, String 座位,
			String start, String end) throws Exception {
		String result=null;
		Connection con=jdbc.Control.getcon();
		
		String sql1_1 = "SELECT * FROM stop WHERE 站名='" + start + "' and 车次号='" + 车次 + "'";
		String sql2_2 = "SELECT * FROM stop WHERE 站名='" + end + "' and 车次号='" + 车次 + "'";
		int start_index = 0;
		int end_index = 0;
		PreparedStatement pstmt1 = con.prepareStatement(sql1_1);
		ResultSet resultSet1 = pstmt1.executeQuery(sql1_1);

		while (resultSet1.next()) {
			start_index = resultSet1.getInt("经过次序");
		}

		pstmt1 = con.prepareStatement(sql2_2);
		resultSet1 = pstmt1.executeQuery(sql2_2);
		while (resultSet1.next()) {
			end_index = resultSet1.getInt("经过次序");
		}
	
		String sql1 = "Delete FROM useorder WHERE 编号='"+编号+"'";
		String sql_left="Update leftover"+ 车次 + " Set ";
		int t = start_index;
		while (true) {
			sql_left += (change(t) + "=0");
			if (t == end_index - 1) {
				break;
			}
			t++;
			sql_left += ",";
		}
		
		sql_left+=" where 日期='"+日期+"' and 车厢号='"+车厢+"' and 座位号='"+座位+"'";

		try {
			con.setAutoCommit(false);
			
			pstmt1=con.prepareStatement(sql_left);
			pstmt1.executeUpdate();
			
			pstmt1=con.prepareStatement(sql1);
			pstmt1.executeUpdate();
			
			con.commit();
			resultSet1.close();
			pstmt1.close();
			con.close();
			result="成功";
		}catch(Exception e) {
			con.rollback();
			e.printStackTrace();
			result="错误";
		}
		return result;
	}

	public static String getdata(String text) {
		Connection con=jdbc.Control.getcon();
		String result=" ";
		String sql1 = "SELECT 日期 FROM leftover"+text+" Group by 日期";
		PreparedStatement pstmt1;
		try {
			pstmt1 = con.prepareStatement(sql1);
			ResultSet resultSet1 = pstmt1.executeQuery(sql1);

			while (resultSet1.next()) {
				result+=resultSet1.getString("日期");
				result+="  ";
			}
			resultSet1.close();
			pstmt1.close();
			con.close();
			
		} catch (SQLException e) {
			result="列车号不存在";
			e.printStackTrace();
		}
		
		return result;
		
	}

	public static String change(String before, String after,String text) {
		String result=null;
		Connection con=jdbc.Control.getcon();
		String sql_pre="Select * from stop where 车次号='"+text+"'";
		PreparedStatement pstmt1;
		String pre=" ";
		try {
			pstmt1 = con.prepareStatement(sql_pre);
			ResultSet resultSet1 = pstmt1.executeQuery(sql_pre);

			while (resultSet1.next()) {
				int index=resultSet1.getInt("经过次序");
				pre+=change(index)+"=0,";
			}
			resultSet1.close();
			
		} catch (SQLException e) {
			result="错误";
			e.printStackTrace();
		}
		String sql1 = "Update leftover"+text+" Set "+pre+"日期='"+after+"' where 日期='"+before+"'";
		System.out.print(sql1);
		try {
			pstmt1 = con.prepareStatement(sql1);
			pstmt1.executeUpdate();
		
			pstmt1.close();
			con.close();
			result="更新成功";
			
		} catch (SQLException e) {
			result="错误";
			e.printStackTrace();
		}
		return result;
	}

}

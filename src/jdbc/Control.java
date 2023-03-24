package jdbc;

import java.awt.EventQueue;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import com.alibaba.druid.pool.DruidDataSourceFactory;



public class Control {
	
	static Properties prop=null;
	static DataSource dataSource=null;
	
	public static Connection getcon() {
		Connection con=null;
		try {
			con = dataSource.getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return con;
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					prop = new Properties();
					prop.load(new FileInputStream("src/druid.properties"));
					dataSource=DruidDataSourceFactory.createDataSource(prop);
					@SuppressWarnings("unused")
					Windows window = new Windows();
					
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	
	public static String[][] getArray(List<List<String>> list){
		  String[][] result = new String[list.size()][];
		  for (int i = 0; i < list.size(); i++) {
		   result[i] = list.get(i).toArray(new String[list.get(i).size()]);
		  }
		  return result;
		 }
	
		
}

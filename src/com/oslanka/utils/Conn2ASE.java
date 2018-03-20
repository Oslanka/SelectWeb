package com.oslanka.utils;



import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class Conn2ASE {

    public static Connection connect()  {
        try {
//        	if(conn==null){
        		Class.forName(Configs.driverStr).newInstance();
        		Properties sysProps = System.getProperties();
        		sysProps.put("user", Configs.user); // 璁剧疆鏁版嵁搴撹闂敤鎴峰悕
        		sysProps.put("password", Configs.password); // 瀵嗙爜
        		Connection	conn = DriverManager.getConnection(Configs.url, sysProps);
//        	}
        		
            return conn;
           // String sql = "select ydxh  from cghyd "; // 琛�
            //ResultSet rs = stmt.executeQuery(sql);
         
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static void dissConnect(Connection conn,Statement stmt,ResultSet rs){
    	try {
    		if(rs!=null) rs .close();
    		if(stmt!=null) stmt .close();
			if(conn!=null) conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
}
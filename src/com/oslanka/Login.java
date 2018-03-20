package com.oslanka;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.oslanka.utils.Conn2ASE;
import com.oslanka.utils.ResponseJsonUtils;

/**
 * MyServlet的具体对象只有一个，属于单例模式。 然后 可以新建一个MyServletTwo类，即可以存在多个Servlet类 线程不安全的，效率高
 *
 * @author 给最苦
 * @version V17.10.20
 */
public class Login implements Servlet {

	/**
	 * 生命周期方法 它会在Servlet对象被销毁之前，执行一次. 展示Servlet对象留下的遗言 一般会在服务器关闭的时候，才会执行
	 *
	 */
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		System.out.println("执行destroy()...");
	}

	/**
	 * 获取Servlet的配置文件
	 *
	 */
	@Override
	public ServletConfig getServletConfig() {
		// TODO Auto-generated method stub
		System.out.println("执行getServletConfig()...");
		return null;
	}

	/**
	 * 获取Servlet的信息
	 *
	 * @return 返回对Servlet的描述信息
	 */
	@Override
	public String getServletInfo() {
		// TODO Auto-generated method stub
		System.out.println("执行getServletInfo()...");

		return null;
	}

	/**
	 * 生命周期方法,由tomcat来调用 它会在Servlet对象出生后，执行一次.
	 *
	 * @param config
	 *            tomcat负责传入这个参数
	 */
	@Override
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		System.out.println("执行init()...");
	}

	/**
	 * 生命周期方法 会被调用多次,每次处理请求都是在调用service方法 浏览器 进入一次指定的地址就会 调用service方法
	 *
	 */
	@Override
	public void service(ServletRequest request, ServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String user = request.getParameter("user");
		String password = request.getParameter("password");
		System.out.println("执行service()..." + user + password);

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("isLogin", false);
		boolean isLogin = login(user, password);
		data.put("isLogin", isLogin);
		if (isLogin) {
			Connection conn = Conn2ASE.connect();
			Statement stmt = null;
			try {
				stmt = conn.createStatement();
			} catch (SQLException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			if (stmt == null)
				return;
			String sql = "select realname,username,password from t_user where" + " username = '" + user
					+ "' and password = '" + password + "';";
			System.out.println("232---------->" + sql);
			ResultSet rsnew;
			try {
				rsnew = stmt.executeQuery(sql);
				Map<String, Object> loginBean = new HashMap<String, Object>();
				while (rsnew.next()) {
					loginBean.put("username", rsnew.getString("username"));
					loginBean.put("nickName", rsnew.getString("realname"));
					loginBean.put("password", rsnew.getString("password"));
				}
				data.put("data", loginBean);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		ResponseJsonUtils.json(response, (Object) data);

	}

	private static boolean login(String user, String password) {
		Connection conn = Conn2ASE.connect();
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		if (stmt == null)
			return false;

		/*try {
			String sql = "create table t_qd_tp1 (code integer not null PRIMARY KEY" + ",ydh integer DEFAULT NULL"
					+ ",sctime timestamp DEFAULT NULL" + ",scr varchar(100) DEFAULT NULL"
					+ ", tp long binary DEFAULT NULL" + ");";
//			sql = "delete from t_user ;";
//			stmt.executeUpdate(sql);
//			sql = "delete from t_qd_tp ;";
//			stmt.executeUpdate(sql);
			sql = "alter table t_qd_tp alter COLUMN ydh5 varchar(50)";
			System.out.println(sql);
			stmt.executeUpdate(sql);
//			sql="insert into t_qd_tp(ydh2,ydh4,ydh5,scr,sctime) values('','',23318030601,'李四','2018-03-20 09:53:35.701');";
//			System.out.println(sql);
//			stmt.executeUpdate(sql);
//			sql = "insert into t_user(username,password,realname) values ('a','b','张三')";
//			System.out.println(sql);
//			stmt.executeUpdate(sql);
		} catch (SQLException e2) { // TODO Auto-generated catch block
			e2.printStackTrace();
		}*/

		try {
			System.out.println("count" + stmt.executeQuery("select count(*) from t_user "));
			// stmt.executeUpdate("insert into t_user(username,password) values
			// "
			// + "('"+user+"','"+password+"') ;");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("e" + e.getMessage());
			try {
				stmt.executeUpdate("create table t_user (username varchar(50) not null primary key,"
						+ "password varchar(20) not null );");
				// stmt.executeUpdate("insert into t_user(username,password)
				// values "
				// + "("+user+","+password+") ;");
				return true;
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				System.out.println(e1.getMessage());
				e1.printStackTrace();
				return false;
			}
		}

		try {
			String select = "select password from " + "t_user where username = '" + user + "' and password ='"
					+ password + "';";
			ResultSet rs = stmt.executeQuery(select);

			System.out.println("1");
			while (rs.next()) {
				System.out.println("2");
				return true;
			}

			System.out.println("3");
			// stmt.executeUpdate("insert into t_user(username,password) values
			// "
			// + "('"+user+"','"+password+"');");
			System.out.println("haha");
			Conn2ASE.dissConnect(conn, stmt, rs);
			return false;
		} catch (SQLException e) {
			System.out.println("haha" + e.getMessage());
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Conn2ASE.dissConnect(stmt,null);
		return false;

	}

}
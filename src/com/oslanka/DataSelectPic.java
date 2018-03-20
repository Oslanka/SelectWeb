package com.oslanka;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oslanka.utils.Conn2ASE;
import com.oslanka.utils.ResponseJsonUtils;

/**
 * MyServlet的具体对象只有一个，属于单例模式。 然后 可以新建一个MyServletTwo类，即可以存在多个Servlet类 线程不安全的，效率高
 *
 * @author 给最苦
 * @version V17.10.20
 */
public class DataSelectPic extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public DataSelectPic() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		service(request, response);
	}

	public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("code", -1);
		String ydh = request.getParameter("ydh");
		String user = new String( request.getParameter("user").getBytes("iso-8859-1"), "UTF-8");
		String start = request.getParameter("start");
		String end = request.getParameter("end");

		String sql1 = (ydh == null || ydh == "") ? " 1=1 " : " ydh = '" + ydh + "'";
		String sql2 = (user == null || user == "") ? " 1=1 " : " scr like '%" + user + "%'";
		String sql3 = (start == null || start == "") ? " 1=1 "
				: " sctime >= '" + testStringToTimestamp(start + " 00:00:00") + "'";
		String sql4 = (end == null || end == "") ? " 1=1 "
				: " sctime <= '" + testStringToTimestamp(end + " 23:59:59") + "'";

		String sql = "select code,ydh,sctime,scr from t_qd_tp " + "where " + sql1 + " and " + sql2 + " and " + sql3
				+ " and " + sql4 + " order by sctime desc;";

		System.out.println(ydh + start + end + sql);
		Connection conn = Conn2ASE.connect();
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
		} catch (SQLException e2) {

			e2.printStackTrace();
		}

		try {
			ResultSet rs = stmt.executeQuery(sql);
			List<Map<String, Object>> array = new ArrayList<Map<String, Object>>();
			while (rs.next()) {
				Map<String, Object> dataMap = new HashMap<String, Object>();
				dataMap.put("id", rs.getString("code"));
				dataMap.put("ydh", rs.getString("ydh"));
				dataMap.put("sctime", rs.getString("sctime"));
				dataMap.put("who", rs.getString("scr"));
				array.add(dataMap);
			}
			data.put("code", 0);
			data.put("size", array.size());
			// if (array.size() < 10) {
			data.put("data", array.size() > 10 ? array.subList(0, 10) : array);
			// }
			Conn2ASE.dissConnect(conn, stmt, rs);

		} catch (SQLException e) {
			System.out.println("haha" + e.getMessage());
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ResponseJsonUtils.json(response, data);
		// ResponseJsonUtils.json(response,selectCG(buildPath,webroot,request,data));

	}

	public Timestamp testStringToTimestamp(String time) {
		// 注：String的类型必须形如： yyyy-mm-dd hh:mm:ss[.f...] 这样的格式，中括号表示可选，否则报错！！！
		// 如果String为其他格式，可考虑重新解析下字符串，再重组~~
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		// 方法一:优势在于可以灵活的设置字符串的形式。
		System.out.println(time);
		Date date = null;
		try {
			date = sdf.parse(time);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (date != null) {
			Timestamp ts = new Timestamp(date.getTime()); // 2011-05-09 //
															// 11:49:45.0
			System.out.println(ts);
			return ts;
		}
		return null;
	}

}
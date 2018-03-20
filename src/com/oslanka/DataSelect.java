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
public class DataSelect extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public DataSelect() {
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
		String ydxh = request.getParameter("ydxh");
		String yhzh = request.getParameter("yhzh");
		if (yhzh != null)
			yhzh = new String(yhzh.getBytes("iso-8859-1"), "UTF-8");
		// request.getParameter("yhzh");// 目的区域
		String tyr = request.getParameter("tyr");
		if (tyr != null)
			tyr = new String(tyr.getBytes("iso-8859-1"), "UTF-8");// 目的区域

		String sql1 = (ydxh == null || ydxh == "") ? " 1=1 " : " ydxh = '" + ydxh + "'";
		String sql2 = (yhzh == null || yhzh == "") ? " 1=1 " : " yhzh like '%" + yhzh + "%'";
		String sql3 = (tyr == null || tyr == "") ? " 1=1 " : " tyr like '%" + tyr + "%'";
		String sql = "select ydh,yhzh,tyr,tyrq,ydxh,jbren,yhzh,shdw from cghyd where " + sql1 + " and " + sql2 + " and "
				+ sql3+" order by tyrq desc";

		System.out.println(ydxh + yhzh + tyr + sql);
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
				dataMap.put("ydh", rs.getString("ydxh"));
				dataMap.put("yhzh", rs.getString("yhzh"));
				dataMap.put("tyr", rs.getString("tyr"));

				dataMap.put("tyrq", rs.getString("tyrq"));
				dataMap.put("ydxh", rs.getString("ydxh"));
				dataMap.put("jbren", rs.getString("jbren"));
				dataMap.put("yhzh", rs.getString("yhzh"));
				dataMap.put("shdw", rs.getString("shdw"));

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

}
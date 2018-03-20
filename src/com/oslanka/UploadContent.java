package com.oslanka;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.oslanka.utils.Conn2ASE;
import com.oslanka.utils.ResponseJsonUtils;

public class UploadContent extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public UploadContent() {
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
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("code", -1);
		String ydh = request.getParameter("ydh");
		String yhzh = request.getParameter("yhzh");// 目的区域
		String tyr = request.getParameter("tyr");// 目的区域

		
		String user=request.getParameter("user");;
		if (user != null)
			user = new String(user.getBytes("iso-8859-1"), "UTF-8");
		
		
		Timestamp sctime =new Timestamp(System.currentTimeMillis()); 
		if(ydh==null||ydh=="") {
			data.put("message", "ydh 不允许空");
			ResponseJsonUtils.json(response, data);
			return;
		}
		if(user==null||user=="") {
			data.put("message", "user 不允许空");
			ResponseJsonUtils.json(response, data);
			return;
		}
		String sql="insert into t_qd_tp(ydh,scr,sctime) values("+ydh+",'"+user+"','"+sctime+"');";
//		String insert = "insert into t_qd_tp(ydh,scr) values(?,?)";
//		String sql = "select tyrq,ydxh,jbren,yhzh,shdw from cghyd where " + sql1 + " and " + sql2 + " and " + sql3;
		System.out.println(sql);
		Connection conn = Conn2ASE.connect();
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		try {
			 stmt.executeUpdate(sql);
			 sql="select code,sctime,ydh from t_qd_tp where "
				 		+ "ydh='"+ydh+"' and sctime = '"+sctime+"' ;";
			 ResultSet rs =stmt.executeQuery(sql);
			 System.out.println(sql);
				List<Map<String, Object>> array = new ArrayList<Map<String, Object>>();
	            while (rs.next()) {
	            	
	            	Map<String, Object> dataMap = new HashMap<String, Object>();
	            	dataMap.put("code" , rs.getString("code"));
	            	dataMap.put("ydh" , rs.getString("ydh"));
	            	dataMap.put("sctime" , rs.getString("sctime"));
	            	array.add(dataMap);
	            }
	            data.put("code", 0);
	    		data.put("data", array);
			Conn2ASE.dissConnect(conn, stmt, rs);

		} catch (SQLException e) {
			System.out.println("haha" + e.getMessage());
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ResponseJsonUtils.json(response, data);
	}
}

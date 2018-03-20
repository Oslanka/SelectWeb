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

public class ChangePassword extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public ChangePassword() {
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
		String user = request.getParameter("user");

		String nickName = request.getParameter("nickName");
		if (nickName != null)
			nickName = new String(nickName.getBytes("iso-8859-1"), "UTF-8");

		String oldPass = request.getParameter("oldPass");// 目的区域
		String newPass = request.getParameter("newPass");// 目的区域

		String sql = "select realname,username,password from t_user where" + " username = '" + user
				+ "' and password = '" + oldPass + "';";
		// String insert = "insert into t_qd_tp(ydh,scr) values(?,?)";
		// String sql = "select tyrq,ydxh,jbren,yhzh,shdw from cghyd where " +
		// sql1 + " and " + sql2 + " and " + sql3;
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

			ResultSet rs = stmt.executeQuery(sql);
			System.out.println(sql);

			boolean isLogin = false;
			while (rs.next()) {
				isLogin = true;
			}
			rs.close();
			data.put("isLogin", isLogin);
			data.put("update", false);
			if (isLogin) {
				sql = "update t_user set realname ='" + nickName + "',password='" + newPass + "'  where"
						+ " username = '" + user + "' and password = '" + oldPass + "';";
				stmt.executeUpdate(sql);
				data.put("update", true);
				sql = "select realname,username,password from t_user where" + " username = '" + user
						+ "' and password = '" + newPass + "';";
				System.out.println("232---------->" + sql);
				ResultSet rsnew = stmt.executeQuery(sql);
				Map<String, Object> loginBean = new HashMap<String, Object>();
				while (rsnew.next()) {
					loginBean.put("username", rsnew.getString("username"));
					loginBean.put("nickName", rsnew.getString("realname"));
					loginBean.put("password", rsnew.getString("password"));
				}
				data.put("data", loginBean);
				Conn2ASE.dissConnect(conn, stmt, rsnew);
			}
			data.put("code", 0);
		} catch (SQLException e) {
			System.out.println("haha" + e.getMessage());
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ResponseJsonUtils.json(response, data);
	}
}

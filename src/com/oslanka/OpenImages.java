package com.oslanka;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oslanka.utils.Conn2ASE;

/**
 * 打开本地图片
 */
@WebServlet("/Images")
public class OpenImages extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String basePath = "E:/FTP/武汉局FTP";

	public OpenImages() {
		super();
	}
	  protected void doPost(HttpServletRequest request,
	           HttpServletResponse response) throws ServletException, IOException {
	       // TODO Auto-generated method stub
	       doGet(request, response);
	   }
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 支持跨域访问
		try {
			response.setHeader("Access-Control-Allow-Origin", "*");

			String id = request.getParameter("id");

			Connection conn = Conn2ASE.connect();
			
			String sqlSelect = "select tp from t_qd_tp where code = " + id + ";";
			System.out.println(sqlSelect);
			Statement stmt = null;
			try {
				stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sqlSelect);
				while (rs.next()) {
					OutputStream out = response.getOutputStream();
					out.write(rs.getBytes("tp"));
					out.close();
					System.out.println("out 了");
					break;
				}
				rs.close();
				stmt.close();
			} catch (SQLException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}

			// filepath = new String(filepath.getBytes("ISO-8859-1"),
			// "UTF-8");//处理请求参数路乱码

			// String buildPath =
			// this.getServletContext().getRealPath("/WEB-INF/upload");

			// 读取本地图片输入流
			/*
			 * FileInputStream fis = new FileInputStream(buildPath +"/"+
			 * filepath); //得到文件大小 int i = fis.available(); //byte数组用于存放图片字节数据
			 * byte[] buff = new byte[i];
			 * 
			 * fis.read(buff); fis.close();
			 * 
			 * //设置发送到客户端的响应内容类型 response.setContentType("image/*");
			 */

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
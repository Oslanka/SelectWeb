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

public class UploadImage extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public UploadImage() {
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
		// 得到上传文件的保存目录，将上传的文件存放于WEB-INF目录下，不允许外界直接访问，保证上传文件的安全
		String savePath = this.getServletContext().getRealPath("/WEB-INF/upload");
		PrintWriter out = response.getWriter();
		File file = new File(savePath);
		// 判断上传文件的保存目录是否存在
		if (!file.exists() && !file.isDirectory()) {
			System.out.println(savePath + "目录不存在，需要创建");
			// 创建目录
			file.mkdir();
		}
		// 返回值提示
		int message = -1;
		// 创建一个DiskFileItemFactory工厂
		DiskFileItemFactory factory = new DiskFileItemFactory();
		try {
			// 创建一个文件上传解析器
			ServletFileUpload upload = new ServletFileUpload(factory);
			// 判断提交上来的数据是否是上传表单的数据
			if (!ServletFileUpload.isMultipartContent(request)) {
				// 按照传统方式获取数据
				return;
			}
			System.out.println("isM = " + ServletFileUpload.isMultipartContent(request));
			// 使用ServletFileUpload解析器解析上传数据，解析结果返回的是一个List<FileItem>集合，每一个FileItem对应一个Form表单的输入
			List<FileItem> list = upload.parseRequest(request);
			for (FileItem item : list) {
				// 如果fileitem中封装的是普通输入项的数据
				if (item.isFormField()) {
					String name = item.getFieldName();
					// 转码
					String value = item.getString("UTF-8");
					value = new String(value.getBytes("iso-8859-1"), "UTF-8");
					System.out.println(name + "=" + value);
				} else {
					// 如果fileitem中封装的是上传文件
					// 得到文件名
					String filename = item.getName();
					System.out.println("filename=" + filename);
					if (filename == null || filename.trim().equals("")) {
						continue;
					}
					// 处理获取到的上传文件的文件名的路径部分，只保留文件名部分
					filename = filename.substring(filename.lastIndexOf("\\") + 1);
					// 获取item中的上传文件的输入流
					String code = filename;
					InputStream in = item.getInputStream();

					Connection conn = Conn2ASE.connect();

					boolean isHas = false;
					/*
					 * String sqlSelect=
					 * "select count(*) from t_qd_tp where ydh = "+ydh+";";
					 * Statement stmt = null; try { stmt =
					 * conn.createStatement(); ResultSet rs
					 * =stmt.executeQuery(sqlSelect); isHas=false; while
					 * (rs.next()) { isHas=true; } rs.close(); stmt.close(); }
					 * catch (SQLException e2) { // TODO Auto-generated catch
					 * block e2.printStackTrace(); }
					 */
					PreparedStatement ptmt = null;

					// if(!isHas){
					// System.out.println("insert");
					// String sql="insert into t_qd_tp(ydh,tp) values(?,?);";
					//
					//
					// try {
					// ptmt=conn.prepareStatement(sql);
					// ptmt.setString(1,ydh);
					// ptmt.setBinaryStream(2, in,
					// in.available());
					// ptmt.execute();
					// } catch (Exception e) {
					// // TODO Auto-generated catch block
					// e.printStackTrace();
					// }
					// }else{
					System.out.println("update");
					String sql = "update t_qd_tp set tp=? where code=?;";

					try {
						ptmt = conn.prepareStatement(sql);
						ptmt.setBinaryStream(1, in, in.available());
						ptmt.setString(2, code);
					
						ptmt.execute();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					// }

					/*
					 * //创建缓冲区 byte buffer[] = new byte[1024];
					 * //创建输出流对象，用于将缓冲区的数据读出到保存路径 FileOutputStream output = new
					 * FileOutputStream(savePath+ "\\" + filename);
					 * //判断输入流中的数据是否已经读完 int len = 0;
					 * //循环将输入流读入到缓冲区当中，(len=in.read(buffer))>0就表示输入流中还有数据 while
					 * ((len = in.read(buffer)) > 0) {
					 * //使用FileOutputStream输出流将缓冲区的数据写入到指定的目录(savePath + "\\" +
					 * filename)当中 output.write(buffer, 0, len); }
					 */
					in.close();

					try {
						ptmt.close();
						conn.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					// output.close();

					message = 0;
				}
			}
		} catch (FileUploadException e) {
			message = -1;
			e.printStackTrace();
		}
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("code", message);
		ResponseJsonUtils.json(response, data);
		// out.print(message);
	}
}

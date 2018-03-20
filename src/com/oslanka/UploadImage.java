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
		// �õ��ϴ��ļ��ı���Ŀ¼�����ϴ����ļ������WEB-INFĿ¼�£����������ֱ�ӷ��ʣ���֤�ϴ��ļ��İ�ȫ
		String savePath = this.getServletContext().getRealPath("/WEB-INF/upload");
		PrintWriter out = response.getWriter();
		File file = new File(savePath);
		// �ж��ϴ��ļ��ı���Ŀ¼�Ƿ����
		if (!file.exists() && !file.isDirectory()) {
			System.out.println(savePath + "Ŀ¼�����ڣ���Ҫ����");
			// ����Ŀ¼
			file.mkdir();
		}
		// ����ֵ��ʾ
		int message = -1;
		// ����һ��DiskFileItemFactory����
		DiskFileItemFactory factory = new DiskFileItemFactory();
		try {
			// ����һ���ļ��ϴ�������
			ServletFileUpload upload = new ServletFileUpload(factory);
			// �ж��ύ�����������Ƿ����ϴ���������
			if (!ServletFileUpload.isMultipartContent(request)) {
				// ���մ�ͳ��ʽ��ȡ����
				return;
			}
			System.out.println("isM = " + ServletFileUpload.isMultipartContent(request));
			// ʹ��ServletFileUpload�����������ϴ����ݣ�����������ص���һ��List<FileItem>���ϣ�ÿһ��FileItem��Ӧһ��Form��������
			List<FileItem> list = upload.parseRequest(request);
			for (FileItem item : list) {
				// ���fileitem�з�װ������ͨ�����������
				if (item.isFormField()) {
					String name = item.getFieldName();
					// ת��
					String value = item.getString("UTF-8");
					value = new String(value.getBytes("iso-8859-1"), "UTF-8");
					System.out.println(name + "=" + value);
				} else {
					// ���fileitem�з�װ�����ϴ��ļ�
					// �õ��ļ���
					String filename = item.getName();
					System.out.println("filename=" + filename);
					if (filename == null || filename.trim().equals("")) {
						continue;
					}
					// �����ȡ�����ϴ��ļ����ļ�����·�����֣�ֻ�����ļ�������
					filename = filename.substring(filename.lastIndexOf("\\") + 1);
					// ��ȡitem�е��ϴ��ļ���������
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
					 * //���������� byte buffer[] = new byte[1024];
					 * //����������������ڽ������������ݶ���������·�� FileOutputStream output = new
					 * FileOutputStream(savePath+ "\\" + filename);
					 * //�ж��������е������Ƿ��Ѿ����� int len = 0;
					 * //ѭ�������������뵽���������У�(len=in.read(buffer))>0�ͱ�ʾ�������л������� while
					 * ((len = in.read(buffer)) > 0) {
					 * //ʹ��FileOutputStream�������������������д�뵽ָ����Ŀ¼(savePath + "\\" +
					 * filename)���� output.write(buffer, 0, len); }
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

package util;

import java.sql.Connection;
import java.sql.DriverManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import listener.Guard;

public class DButil {
	private static final String url = "jdbc:mysql://localhost/eshop";
	private static final String username = "root";
	private static final String pswd = "";
	private static String error_message = "";
	
	public static String getErrorMessage() {
		return error_message;
	}
	
	public static Connection getConnection(HttpServletRequest request) {
		try {
			HttpSession session = request.getSession();
			Connection c = (Connection)session.getAttribute("connection");
			if (c == null) {
				Class.forName("com.mysql.cj.jdbc.Driver");
				c = DriverManager.getConnection(url, username, pswd);
				session.setAttribute("connection", c);
				Guard g = new Guard(c);
			}
			return c;
		} catch (Exception e) { 
			error_message = e.getMessage();
			return null; 
		}
	}
}
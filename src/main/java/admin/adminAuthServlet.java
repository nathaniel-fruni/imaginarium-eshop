package admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import util.DButil;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.mindrot.jbcrypt.BCrypt;


public class adminAuthServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public adminAuthServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		
		HttpSession session = request.getSession();
		session.setMaxInactiveInterval(900);
		
		try {
			if (badConnection(out, request)) {
				return;
			}
			if (verificationOk(out, request)) {
	              response.sendRedirect("adminServlet?operation=1");
	        } else {
	        	response.sendRedirect("admin.html?incorrectLogin=true");
	        }
		} catch (Exception e) { out.println(e); }
	}
	
	private boolean badConnection(PrintWriter out, HttpServletRequest request) {
	    if (DButil.getConnection(request) != null) return false;
	    else {
	    	out.println(DButil.getErrorMessage());
	    	return true;
	    }
	}
	
	protected boolean verificationOk(PrintWriter out, HttpServletRequest request) {
        try {
            String email = request.getParameter("email");
            String inputPassword = request.getParameter("pwd");

            String sql = "SELECT id, email, passwd, name, surname, role FROM users WHERE email = ?";
            try (PreparedStatement preparedStatement = DButil.getConnection(request).prepareStatement(sql)) {
                preparedStatement.setString(1, email);

                try (ResultSet rs = preparedStatement.executeQuery()) {
                    if (rs.next()) {
                        String hashedPassword = rs.getString("passwd");

                        if (BCrypt.checkpw(inputPassword, hashedPassword)) {
                            HttpSession session = request.getSession();
                            setSessionAttributes(session, rs);

                            if (session.getAttribute("role").equals("admin")) {
                                return true;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
        	e.printStackTrace();
			out.println("Unexpected error occured.");
        }
        return false;
    }
	
	private void setSessionAttributes(HttpSession session, ResultSet resultSet) throws SQLException {
		session.setAttribute("ID", resultSet.getInt("id"));
		session.setAttribute("email", resultSet.getString("email"));
		session.setAttribute("name", resultSet.getString("name"));
		session.setAttribute("surname", resultSet.getString("surname"));
		session.setAttribute("role", resultSet.getString("role"));
    }
	
	public static boolean isLoggedin(HttpServletRequest request) {
		HttpSession session = request.getSession();
		if (session == null || session.getAttribute("ID") == null || session.getAttribute("role") == null || session.getAttribute("role").equals("customer")) {
            return false;
        }
		return true;
	} 

}

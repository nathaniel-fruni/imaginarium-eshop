package customer;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import util.DButil;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.Statement;

public class customerAuthServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public customerAuthServlet() {
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
	              response.sendRedirect("mainServlet?operation=1");
	        } else {
	        	response.sendRedirect("index.html?incorrectLogin=true");
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
			String pwd = request.getParameter("pwd");
			Statement stmt = DButil.getConnection(request).createStatement();
			String sql = "SELECT MAX(id) AS iid, COUNT(id) AS pocet FROM users WHERE email = '" + email + "' AND passwd = '" + pwd + "'";
			ResultSet rs = stmt.executeQuery(sql);
			rs.next();
			
			HttpSession session = request.getSession();
			if (rs.getInt("pocet") == 1) {
				sql = "SELECT id, email, name, surname, address, discount, notes, role FROM users WHERE id = '" + rs.getInt("iid") + "'";
				rs = stmt.executeQuery(sql);
				rs.next();
				session.setAttribute("ID", rs.getInt("id"));
				session.setAttribute("email", rs.getString("email"));
				session.setAttribute("name", rs.getString("name"));
				session.setAttribute("surname", rs.getString("surname"));
				session.setAttribute("address", rs.getString("address"));
				session.setAttribute("discount", rs.getInt("discount"));
				session.setAttribute("notes", rs.getString("notes"));
				session.setAttribute("role", rs.getString("role"));
				if (((String) session.getAttribute("role")).equals("customer")) {
					return true;
				}
			} else {
				session.invalidate();
			}
			
			rs.close();
			stmt.close();
		} catch (Exception e) { out.println(e); }
		return false;
	}
	
	public static boolean isLoggedin(HttpServletRequest request) {
		HttpSession session = request.getSession();
		if (session == null || session.getAttribute("ID") == null || session.getAttribute("role") == null || session.getAttribute("role").equals("admin")) {
            return false;
        }
		return true;
	} 

}

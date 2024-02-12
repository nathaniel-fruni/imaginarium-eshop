package customer;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import util.DButil;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.Statement;

public class registrationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public registrationServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		String operation = request.getParameter("operation");
		
		if (operation == null) { response.sendRedirect("index.html"); return;}
		
		out.println("<!DOCTYPE html>\r\n"
				+ "<html lang=\"sk\">\r\n"
				+ "<head>\r\n"
				+ "    <meta charset=\"UTF-8\">\r\n"
				+ "    <title>E-shop</title>\r\n"
				+ "    <meta charset=\"utf-8\">\r\n"
				+ "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\r\n"
				+ "    <title>Imaginarium</title>\r\n"
				+ "    <link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css\" type=\"text/css\">\r\n"
				+ "    <link rel=\"stylesheet\" href=\"style.css\">\r\n"
				+ "</head>\r\n"
				+ "<body style=\"background-image: linear-gradient(to left bottom, rgba(189, 195, 199, .75), rgba(44, 62, 80, .75)); background-size: 100%;\">\r\n"
				+ "  <div class=\"py-3 text-center\">\r\n"
				+ "    <div class=\"container-fluid\">\r\n"
				+ "      <div class=\"row\">\r\n"
				+ "        <div class=\"mx-auto col-md-7\">\r\n"
				+ "          <div class=\"mx-auto col-7\" >\r\n"
				+ "            <h1 class=\"mb-2 text-center\">Registration</h1>\r\n"
				+ "            <form action=\"registrationServlet?operation=register\" method=\"post\">\r\n"
				+ "              <div class=\"form-group\"> <input type=\"text\" class=\"form-control\" name=\"name\" placeholder=\"Name\" required=\"\"></div>\r\n"
				+ "              <div class=\"form-group\"> <input type=\"text\" class=\"form-control\" name=\"surname\" placeholder=\"Surname\" required=\"\"></div>\r\n"
				+ "              <div class=\"form-group\"> <input type=\"text\" class=\"form-control\" name=\"street\" placeholder=\"Street\" required=\"\"></div>\r\n"
				+ "              <div class=\"form-group\"> <input type=\"number\" class=\"form-control\" name=\"house_number\" placeholder=\"House Number\" required=\"\"></div>\r\n"
				+ "              <div class=\"form-group\"> <input type=\"text\" class=\"form-control\" name=\"zip\" placeholder=\"Zip Code\" required=\"\" pattern=\"^0[1-9]\\d{3}$|^[1-9]\\d{4}$\" title=\"Enter a zip code between 01001 and 99999\"></div>\r\n"
				+ "              <div class=\"form-group\"> <input type=\"text\" class=\"form-control\" name=\"city\" placeholder=\"City\" required=\"\"></div>\r\n"
				+ "              <div class=\"form-group\"> <input type=\"email\" class=\"form-control\" name=\"email\" placeholder=\"Your Email\" required=\"\"></div>\r\n"
				+ "              <div class=\"form-group\"> <input type=\"password\" class=\"form-control\" name=\"passwd\" placeholder=\"Password\" required=\"\"></div>\r\n"
				+ "              <button type=\"submit\" class=\"btn btn-outline-primary btn-block\">Register</button>\r\n"
				+ "            </form><br>\r\n");
        if(operation.equals("register")) { register(out, request); }
		out.println("          </div>\r\n"
				+ "        </div>\r\n"
				+ "      </div>\r\n"
				+ "    </div>\r\n"
				+ "  </div>\r\n"
				+ "</body>"
				+ "</html>");
		
	}
	
	private void register(PrintWriter out, HttpServletRequest request) {
		String name = request.getParameter("name");
		String surname = request.getParameter("surname");
		String street = request.getParameter("street");
		String house_number = request.getParameter("house_number");
		String zip = request.getParameter("zip");
		String city = request.getParameter("city");
		String email = request.getParameter("email");
		String passwd = request.getParameter("passwd");
		try {
	    	Statement stmt = DButil.getConnection(request).createStatement();
	    	String sql = "SELECT count(ID) AS pocet FROM users WHERE " 
			          + "(email='"+email+"')";
			ResultSet rs = stmt.executeQuery(sql);
			rs.next();
			int pocet = rs.getInt("pocet");
			
			if (pocet == 0) {
			    sql = "INSERT INTO users (email, passwd, name, surname, address, discount, notes, role) values ("+
			    	  "'" + email + "', "+
			    	  "'" + passwd + "', "+
			    	  "'" + name + "', "+
			    	  "'" + surname + "', "+
			    	  "'" + street + " " + house_number + ", " + zip + " " + city + "', "+
			    	  "'0', " +
			    	  "'', " +
			    	  "'customer')";
			    int ex = stmt.executeUpdate(sql);
		    	if (ex > 0) {
		    		out.println("<div class=\"align-items-center justify-content-center d-flex flex-column\" >\r\n"
		    				+ "    <h3 class=\"text-center text-success\">Registration successful!</h3><a class=\"btn btn-outline-primary\" href=\"index.html\">Log in</a>\r\n"
		    				+ "  </div>");
		    	}
			} else {
				out.println("<div>\r\n"
						+ "   <h4 class=\"text-center text-danger\">Registration unsuccessful, email already exists!</h4>\r\n"
						+ "  </div>");
			} 
			
			rs.close();
			stmt.close();
		  } catch (Exception e) { out.println(e.getMessage()); }
	}

}

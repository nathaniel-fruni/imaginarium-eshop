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

public class adminServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public adminServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();

		if (!adminAuthServlet.isLoggedin(request)) response.sendRedirect("admin.html");

		String operation = request.getParameter("operation");
		if (operation == null) { response.sendRedirect("admin.html"); return;}
		
		if (operation.equals("logout")) {
			try {
				logout(request, response);
			} catch (IOException e) {
		        e.printStackTrace();
		        out.println("An error occurred during logout.");
			}
			return;
		}
		if (operation.equals("changeRole")) { changeRole(request, out); }
		if (operation.equals("changeStatus")) { changeStatus(request, out); }
		if (operation.equals("deleteOrder")) { deleteOrder(request, out); }

		createHtmlBegining(out, request);
	    createHeader(out, request);
	    if (operation.equals("showOrders")) { showOrders(out, request); }
	    else { createMain(out, request, response); }
	    createFooter(out, request);
	    createHtmlEnd(out, request);
	}
	
	public static void createHtmlBegining(PrintWriter out, HttpServletRequest request) {
		out.println("<!DOCTYPE html>\r\n"
				+ "<html lang=\"sk\">\r\n"
				+ "<head>\r\n"
				+ "<meta charset=\"utf-8\">\r\n"
				+ "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\r\n"
				+ "<title>Imaginarium - Admin</title>\r\n"
				+ "<link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css\" type=\"text/css\">\r\n"
				+ "<link rel=\"stylesheet\" href=\"styles/style.css\">\r\n"
				+ "<script src=\"js/navbar-ontop.js\"></script>\r\n"
				+ "</head>\r\n"
				+ "<body>");
	}
	
	public static void createHtmlEnd(PrintWriter out, HttpServletRequest request) {
		out.println("<script src=\"https://code.jquery.com/jquery-3.3.1.min.js\"></script>\r\n"
				+ "<script src=\"https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js\" integrity=\"sha384-ZMP7rVo3mIykV+2+9J3UJ46jBk0WLaUAdn689aCwoqbBJiSnjAK/l8WvCWPIPm49\" crossorigin=\"anonymous\"></script>\r\n"
				+ "<script src=\"https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js\" integrity=\"sha384-ChfqqxuZUCnJSK3+MXmPNIyE6ZbWh2IMqE241rYiqJxyMiZ6OW/JmZQ5stwEULTy\" crossorigin=\"anonymous\"></script>\r\n"
				+ "</body>\r\n"
				+ "</html>");
	}
	
	public static void createHeader(PrintWriter out, HttpServletRequest request) {
		HttpSession ses = request.getSession();
		String displayedName = (String)ses.getAttribute("name") + " " + (String)ses.getAttribute("surname");
		
		out.println("<nav class=\"navbar navbar-expand-md navbar-dark bg-dark fixed-top p-1\" style=\"background: linear-gradient(to bottom, rgba(0,0,0,0.2), rgba(0,0,0,0.8));\">\r\n"
				+ "    <div class=\"container\"> <button class=\"navbar-toggler navbar-toggler-right border-0\" type=\"button\" data-toggle=\"collapse\" data-target=\"#navbar13\">\r\n"
				+ "        <span class=\"navbar-toggler-icon\"></span>\r\n"
				+ "      </button>\r\n"
				+ "      <div class=\"collapse navbar-collapse\" id=\"navbar13\"> <a class=\"navbar-brand d-none d-md-block\" href=\"adminServlet?operation=1\">\r\n"
				+ "          <i class=\"fa d-inline fa-lg fa-superpowers\"></i>\r\n"
				+ "          <b> IMAGINARIUM - ADMIN</b>\r\n"
				+ "        </a>\r\n"
				+ "        <ul class=\"navbar-nav mx-auto\">\r\n"
				+ "          <li class=\"nav-item\"> <a class=\"nav-link\" href=\"#\">Welcome, "+displayedName+"</a> </li>\r\n"
				+ "        </ul>\r\n"
				+ "        <ul class=\"navbar-nav\">\r\n"
				+ "          <li class=\"nav-item\"> <a class=\"nav-link\" href=\"adminServlet?operation=logout\">\r\n"
				+ "              <i class=\"fa fa-fw fa-sign-out\"></i>Log out </a> </li>\r\n"
				+ "        </ul>\r\n"
				+ "      </div>\r\n"
				+ "    </div>\r\n"
				+ "  </nav>");
	}
	
	public static void createFooter(PrintWriter out, HttpServletRequest request) {
		out.println("<div class=\"py-3\">\r\n"
				+ "      <div class=\"container\">\r\n"
				+ "        <div class=\"row\">\r\n"
				+ "          <div class=\"col-12 text-center\">\r\n"
				+ "            <p class=\"mb-0\">© 2023 Imaginarium</p>\r\n"
				+ "          </div>\r\n"
				+ "        </div>\r\n"
				+ "      </div>\r\n"
				+ "    </div>\r\n"
				+ "  </div>");
	}
	
	private void createMain(PrintWriter out, HttpServletRequest request, HttpServletResponse response) {
		out.println("<div class=\"py-5\">\r\n"
				+ "    <div class=\"container\">\r\n"
				+ "      <div class=\"row\">\r\n"
				+ "        <div class=\"col-12\">\r\n"
				+ "          <h1 class=\"display-4 text-center\">User List</h1>\r\n"
				+ "        </div>\r\n"
				+ "      </div>\r\n"
				+ "      <div class=\"row\">\r\n"
				+ "        <div class=\"col-12\">\r\n"
				+ "          <h3 class=\"text-primary mb-3\">Customers</h3>\r\n"
				+ "        </div>\r\n"
				+ "      </div>");
		
		try {
			String sql = "SELECT id, name, surname, role FROM `users` WHERE role=\"customer\"";
			int userId = getUserID(request, response);
			
			try (PreparedStatement stmt = DButil.getConnection(request).prepareStatement(sql);
					ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
			        out.println("<div class=\"row\">\r\n"
			        		+ "        <div class=\"col-3\">\r\n"
			        		+ "          <h5>"+rs.getString("name")+ " "+rs.getString("surname")+"</h5>\r\n"
			        		+ "        </div>\r\n"
			        		+ "        <form>\r\n"
			        		+ "          <input type=\"hidden\" name=\"id\" value=\""+rs.getInt("id")+"\">\r\n"
			        		+ "          <input type=\"hidden\" name=\"role\" value=\""+rs.getString("role")+"\">\r\n"
			        		+ "          <input type=\"hidden\" name=\"operation\" value=\"changeRole\">\r\n"
			        		+ "          <div class=\"col-3\">\r\n"
			        		+ "            <button type=\"submit\" class=\"btn btn-link text-success\">\r\n"
			        		+ "              <h5 style=\"margin: 0;\" onmouseover=\"this.style.textDecoration='underline'\" onmouseout=\"this.style.textDecoration='none'\">promote to admin</h5>\r\n"
			        		+ "            </button>\r\n"
			        		+ "          </div>\r\n"
			        		+ "        </form>\r\n"
			        		+ "        <form>\r\n"
			        		+ "          <input type=\"hidden\" name=\"id\" value=\""+rs.getInt("id")+"\" style=\"\">\r\n"
			        		+ "          <input type=\"hidden\" name=\"name\" value=\""+rs.getString("name")+"\">\r\n"
					    	+ "          <input type=\"hidden\" name=\"surname\" value=\""+rs.getString("surname")+"\">\r\n"
			        	    + "          <input type=\"hidden\" name=\"operation\" value=\"showOrders\">\r\n"
			        		+ "          <div class=\"col-3\">\r\n"
			        		+ "            <button type=\"submit\" class=\"btn btn-link text-light\">\r\n"
			        		+ "              <h5 style=\"margin: 0;\" onmouseover=\"this.style.textDecoration='underline'\" onmouseout=\"this.style.textDecoration='none'\">view orders</h5>\r\n"
			        		+ "            </button>\r\n"
			        		+ "          </div>\r\n"
			        		+ "        </form>\r\n"
			        		+ "      </div>");
			     }
			}  
		     
		     out.println("<div class=\"d-flex justify-content-center\">\r\n"
		     		+ "        <div class=\"section w-75 p-3\">\r\n"
		     		+ "          <div class=\"container\">\r\n"
		     		+ "            <hr style=\"border-color: white;\">\r\n"
		     		+ "          </div>\r\n"
		     		+ "        </div>\r\n"
		     		+ "      </div>\r\n"
		     		+ "      <div class=\"row\">\r\n"
		     		+ "        <div class=\"col-12\">\r\n"
		     		+ "          <h3 class=\"text-primary mb-3\">Admins</h3>\r\n"
		     		+ "        </div>\r\n"
		     		+ "      </div>");
		     
		     sql = "SELECT id, name, surname, role FROM `users` WHERE role=\"admin\" AND id != ?";
		     try (PreparedStatement stmt = DButil.getConnection(request).prepareStatement(sql)) {
		    	 stmt.setInt(1, userId);
		    	 try (ResultSet rs = stmt.executeQuery()) {
		    		 while(rs.next()) {
				    	 out.println(" <div class=\"row\">\r\n"
				    	 		+ "        <div class=\"col-md-3\">\r\n"
				    	 		+ "          <h5>"+rs.getString("name") + " "+rs.getString("surname")+"</h5>\r\n"
				    	 		+ "        </div>\r\n"
				    	 		+ "        <form>\r\n"
				    	 		+ "          <input type=\"hidden\" name=\"id\" value=\""+rs.getInt("id")+"\">\r\n"
				    	 	    + "          <input type=\"hidden\" name=\"role\" value=\""+rs.getString("role")+"\">\r\n"
				    	 		+ "          <input type=\"hidden\" name=\"operation\" value=\"changeRole\">\r\n"
				    	 		+ "          <div class=\"col-3\">\r\n"
				    	 		+ "            <button type=\"submit\" class=\"btn btn-link text-danger\">\r\n"
				    	 		+ "              <h5 style=\"margin: 0;\" onmouseover=\"this.style.textDecoration='underline'\" onmouseout=\"this.style.textDecoration='none'\">revoke admin privileges</h5>\r\n"
				    	 		+ "            </button>\r\n"
				    	 		+ "          </div>\r\n"
				    	 		+ "        </form>\r\n"
				    	 		+ "      </div>");
				     }
		    	 }
		     }
		     
		     out.println("</div>\r\n"
		     		+ "    <div class=\"section text-center p-3\">\r\n"
		     		+ "      <div class=\"container\">\r\n"
		     		+ "        <hr style=\"border-color: white;\">\r\n"
		     		+ "      </div>\r\n"
		     		+ "    </div>");
		} catch (IOException e) {
			e.printStackTrace();
			out.println("An error occured while getting user ID."); 
		} catch (Exception e) { 
			e.printStackTrace();
			out.println("An error occured when displaying users."); 
		}
	}
	
	private void showOrders(PrintWriter out, HttpServletRequest request) {
		out.println("<div class=\"py-5\">\r\n"
				+ "    <div class=\"container\">\r\n"
				+ "      <div class=\"row\">\r\n"
				+ "        <div class=\"col-md-12\">\r\n"
				+ "          <h1 class=\"display-4 text-center\">Orders: "+request.getParameter("name")+" "+request.getParameter("surname")+"</h1>\r\n"
				+ "        </div>\r\n"
				+ "      </div>\r\n"
				+ "      <div class=\"row\">\r\n"
				+ "        <div class=\"col-3 col-md-4\">\r\n"
				+ "          <h4>Order number</h4>\r\n"
				+ "        </div>\r\n"
				+ "        <div class=\"col-3 col-md-4\">\r\n"
				+ "          <h4>Status</h4>\r\n"
				+ "        </div>\r\n"
				+ "        <div class=\"col-md-4\"></div>\r\n"
				+ "      </div>");
		try {
			String sql = "SELECT * FROM orders WHERE customer_id = ?";
			try (PreparedStatement stmt = DButil.getConnection(request).prepareStatement(sql)) {
				stmt.setInt(1, Integer.parseInt(request.getParameter("id")));
				try (ResultSet rs = stmt.executeQuery()) {
					while(rs.next()) {
				    	out.println("<div class=\"row\">\r\n"
				    			+ "        <div class=\"col-3 col-md-4 d-flex align-items-center\">\r\n"
				    			+ "          <h4>"+rs.getInt("order_number")+"</h4>\r\n"
				    			+ "        </div>\r\n"
				    			+ "        <div class=\"col-3 col-md-4 align-items-center d-flex\">\r\n"
				    			+ "          <form action=\"adminServlet\" method=\"post\">\r\n"
				    			+ "            <select class=\"rounded bg-dark mr-2\" name=\"selectedStatus\">\r\n"
				    			+ "              <option value=\"new\" "+((rs.getString("status").equals("new")) ? "selected" : "")+">new</option>\r\n"
				    			+ "              <option value=\"proccessing\" "+((rs.getString("status").equals("proccessing")) ? "selected" : "")+">proccessing</option>\r\n"
				    			+ "              <option value\"confirmed\" "+((rs.getString("status").equals("confirmed")) ? "selected" : "")+">confirmed</option>\r\n"
				    			+ "              <option value=\"shipped\" "+((rs.getString("status").equals("shipped")) ? "selected" : "")+">shipped</option>\r\n"
				    			+ "              <option value=\"delivered\" "+((rs.getString("status").equals("delivered")) ? "selected" : "")+">delivered</option>\r\n"
				    			+ "            </select>\r\n"
				    			+ "            <input type=\"hidden\" name=\"operation\" value=\"changeStatus\">\r\n"
				    			+ "            <input type=\"hidden\" name=\"order_id\" value=\""+rs.getInt("id")+"\" style=\"\">\r\n"
				    			+ "            <button type=\"submit\" class=\"btn btn-sm btn-outline-light\">Change status</button>\r\n"
				    			+ "          </form>\r\n"
				    			+ "        </div>\r\n"
				    			+ "        <div class=\"col-3 col-md-4 d-flex align-items-center\">\r\n"
				    			+ "          <form class=\"\">\r\n"
				    			+ "            <input type=\"hidden\" name=\"order_id\" value=\""+rs.getInt("id")+"\">\r\n"
				    			+ "            <input type=\"hidden\" name=\"operation\" value=\"deleteOrder\">\r\n"
				    			+ "            <button type=\"submit\" class=\"btn btn-link text-danger\">\r\n"
				    			+ "              <h5 style=\"margin: 0px; text-decoration: none;\" onmouseover=\"this.style.textDecoration='underline'\" onmouseout=\"this.style.textDecoration='none'\">Delete</h5>\r\n"
				    			+ "            </button>\r\n"
				    			+ "          </form>\r\n"
				    			+ "        </div>\r\n"
				    			+ "      </div>");
				    }
				}
			}
		    out.println("<div class=\"mt-3 text-center\"><a href=\"adminServlet?operation=1\" class=\"btn btn-outline-light\">Back</a></div>");
		} catch (Exception e) {
			e.printStackTrace();
			out.println("An error occured when displaying orders.");
		}
		out.println("</div>\r\n"
				+ "    <div class=\"section text-center p-3\">\r\n"
				+ "      <div class=\"container\">\r\n"
				+ "        <hr style=\"border-color: white;\">\r\n"
				+ "      </div>\r\n"
				+ "    </div>");
	}
	
	private void changeRole(HttpServletRequest request, PrintWriter out) {
		try {
			String sql = "UPDATE users SET role = ? WHERE id = ?";
			String role;
			
            if (request.getParameter("role").equals("customer")) {
            	role = "admin";
            } else {
            	role = "customer";
            }
            
            try (PreparedStatement stmt = DButil.getConnection(request).prepareStatement(sql)) {
            	stmt.setString(1, role);
            	stmt.setInt(2, Integer.parseInt(request.getParameter("id")));
            	stmt.executeUpdate();
            }
        } catch (Exception e) {
        	e.printStackTrace();
			out.println("An error occured when updating user role.");
        }
	}
	
	private void changeStatus(HttpServletRequest request, PrintWriter out) {
		try {
			String sql = "UPDATE orders SET status = ? WHERE id = ?";
			try (PreparedStatement stmt = DButil.getConnection(request).prepareStatement(sql)) {
				stmt.setString(1, request.getParameter("selectedStatus"));
				stmt.setInt(2, Integer.parseInt(request.getParameter("order_id")));
				stmt.executeUpdate();
			}    
        } catch (Exception e) {
        	e.printStackTrace();
			out.println("An error occured when updating order status.");
        }
	}
	
	private void deleteOrder(HttpServletRequest request, PrintWriter out) {
		try {
			String deleteOrderQuery = "DELETE FROM orders WHERE id = ?";
			String deleteOrderItemsQuery = "DELETE FROM order_items WHERE order_id = ?";
			
			try (PreparedStatement OrderStmt = DButil.getConnection(request).prepareStatement(deleteOrderQuery);
				 PreparedStatement OrderItemsStmt = DButil.getConnection(request).prepareStatement(deleteOrderItemsQuery)) {
				
				OrderStmt.setInt(1, Integer.parseInt(request.getParameter("order_id")));
				OrderStmt.executeUpdate();
				
				OrderItemsStmt.setInt(1, Integer.parseInt(request.getParameter("order_id")));
				OrderItemsStmt.executeUpdate();
			}
        } catch (Exception e) {
        	e.printStackTrace();
			out.println("An error occured when deleting order.");
        }
	}
	
	public static void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
	    HttpSession session = request.getSession(false);  
	    if (session != null) {
	        session.invalidate();
	    }
	    response.sendRedirect("index.html");
	}
	
	private Integer getUserID(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession(false); 
	    if (session != null) {
	    	Integer admin_id = (Integer)session.getAttribute("ID");
			return admin_id;
	    }
	    response.sendRedirect("index.html");
	    return 0;
	}

}

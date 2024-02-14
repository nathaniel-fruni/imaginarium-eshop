package customer;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import util.DButil;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ordersServlet extends mainServlet {
	private static final long serialVersionUID = 1L;
       
    public ordersServlet() {
        super();
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();

		if (!customerAuthServlet.isLoggedin(request)) response.sendRedirect("index.html");
		
		String operation = request.getParameter("operation");
		if (operation == null) { response.sendRedirect("index.html"); return;}
		
		if (operation.equals("logout")) {
			try {
				mainServlet.logout(request, response);
			} catch (IOException e) {
		        e.printStackTrace();
		        out.println("An error occurred during logout.");
			}
			return;
		}
		
		mainServlet.createHtmlBegining(out, request);
		mainServlet.createHeader(out, request);
		if (operation.equals("showOrder")) { showOrder(out, request); }
		else { createMain(out, request, response); }
	    mainServlet.createFooter(out, request);
	    mainServlet.createHtmlEnd(out, request);
	}
	
	private void createMain(PrintWriter out, HttpServletRequest request, HttpServletResponse response) {
		out.println("<div class=\"bg-dark py-3 mx-5 mt-5 mb-2\" style=\"	background-image: linear-gradient(to bottom, rgba(0,0,0,0.2), rgba(0,0,0,0.8));	background-position: top left;	background-size: 100%;	background-repeat: repeat;\">\r\n"
				+ "    <div class=\"container\">\r\n"
				+ "      <div class=\"row\">\r\n"
				+ "        <div class=\"col-md-12\">\r\n"
				+ "          <h1 class=\"text-center\" style=\"	text-shadow: 0px 0px 4px white;\">Orders</h1>\r\n"
				+ "        </div>\r\n"
				+ "      </div>\r\n");
		out.println("<div class=\"row justify-content-center\">\r\n"
				+ "        <div class=\"col-md-2 justify-content-center align-items-center d-flex\"><span>Order number</span></div>\r\n"
				+ "        <div class=\"col-md-2 justify-content-center align-items-center d-flex\"><span>Order date</span></div>\r\n"
				+ "        <div class=\"col-md-2 justify-content-center align-items-center d-flex\"><span>Amount</span></div>\r\n"
				+ "        <div class=\"col-md-2 justify-content-center align-items-center d-flex\"><span>Status</span></div>\r\n"
				+ "        <div class=\"col-md-2 justify-content-center align-items-center d-flex\">\r\n"
				+ "        </div>\r\n"
				+ "      </div>");
				try {
					String sql = "SELECT * FROM `orders` WHERE customer_id = ?";
					int userId = getUserID(request, response);
					
					try (PreparedStatement stmt = DButil.getConnection(request).prepareStatement(sql)) {
						stmt.setInt(1, userId);
					    try (ResultSet rs = stmt.executeQuery()) {
					    	while (rs.next()) {
								out.println("<div class=\"row justify-content-center\">\r\n"
										+ "        <div class=\"col-md-2 justify-content-center align-items-center d-flex\"><span>"+rs.getInt("order_number")+"</span></div>\r\n"
										+ "        <div class=\"col-md-2 justify-content-center align-items-center d-flex\"><span>"+rs.getDate("order_date")+"</span></div>\r\n"
										+ "        <div class=\"col-md-2 justify-content-center align-items-center d-flex\"><span>"+rs.getDouble("amount")+"€</span></div>\r\n"
										+ "        <div class=\"col-md-2 justify-content-center align-items-center d-flex\"><span>"+rs.getString("status")+"</span></div>\r\n"
										+ "        <div class=\"col-md-2 justify-content-center align-items-center d-flex\">\r\n"
										+ "          <div class=\"d-flex align-items-center justify-content-center\">\r\n"
										+ "            <form action=\"ordersServlet\" method=\"post\">\r\n"
										+ "              <input type=\"hidden\" name=\"id\" value=\""+rs.getInt("id")+"\">\r\n"
									    + "              <input type=\"hidden\" name=\"order_number\" value=\""+rs.getInt("order_number")+"\">\r\n"
									    + "              <input type=\"hidden\" name=\"order_date\" value=\""+rs.getDate("order_date")+"\">\r\n"
									    + "              <input type=\"hidden\" name=\"amount\" value=\""+rs.getDouble("amount")+"\">\r\n"
									    + "              <input type=\"hidden\" name=\"status\" value=\""+rs.getString("status")+"\">\r\n"
										+ "              <input type=\"hidden\" name=\"operation\" value=\"showOrder\" style=\"\">\r\n"
										+ "              <button type=\"submit\" class=\"btn btn-link text-primary\">Order Details</button>\r\n"
										+ "            </form>\r\n"
										+ "          </div>\r\n"
										+ "        </div>\r\n"
										+ "      </div>");
							}
					    }
					}
				} catch (Exception e) {
					e.printStackTrace();
			        out.println("An error occured displaying orders.");
				}
				out.println("</div>\r\n"
						+ "</div>"
						+ "<div class=\"col-md-12 text-center\"><a class=\"btn btn-outline-light\" href=\"mainServlet?operation=1\">Back to home</a></div>");
	}
	
	private void showOrder(PrintWriter out, HttpServletRequest request) {
		out.println("<div class=\"bg-dark py-3 m-5\" style=\" background-image: linear-gradient(to bottom, rgba(0,0,0,0.2), rgba(0,0,0,0.8)); background-position: top left; background-size: 100%;	background-repeat: repeat;\">\r\n"
				+ "    <div class=\"container\">\r\n"
				+ "      <div class=\"row\">\r\n"
				+ "        <div class=\"col-md-12\">\r\n"
				+ "          <h1 class=\"text-center\" style=\"	text-shadow: 0px 0px 4px white;\">Order "+request.getParameter("order_number")+"</h1>\r\n"
				+ "        </div>\r\n"
				+ "      </div>\r\n"
				+ "      <div class=\"row d-flex justify-content-center\">\r\n"
				+ "        <div class=\"col-md-3 justify-content-center align-items-center d-flex\">\r\n"
				+ "          <p class=\"text-primary\">Oder date: "+request.getParameter("order_date")+"</p>\r\n"
				+ "        </div>\r\n"
				+ "        <div class=\"col-md-3 justify-content-center align-items-center d-flex\">\r\n"
				+ "          <p class=\"text-primary\">Status: "+request.getParameter("status")+"</p>\r\n"
				+ "        </div>\r\n"
				+ "      </div>\r\n"
				+ "</div>");
		try {
			String sql = "SELECT products.book_name AS book_name, products.author_name AS author_name, products.picture_name AS picture_name,\r\n"
					+ "order_items.price AS price, order_items.quantity AS quantity\r\n"
					+ "FROM `order_items`\r\n"
					+ "INNER JOIN products ON (order_items.product_id = products.id)\r\n"
					+ "WHERE order_id = ?";
			try (PreparedStatement stmt = DButil.getConnection(request).prepareStatement(sql)) {
				stmt.setInt(1, Integer.parseInt(request.getParameter("id")));
				try (ResultSet rs = stmt.executeQuery()) {
					while (rs.next()) {
				    	out.println("<div class=\"row mb-3\">\r\n"
				    			+ "        <div class=\"col-md-4\">\r\n"
				    			+ "          <p class=\"text-center\"><b>"+rs.getString("book_name")+" - "+rs.getString("author_name")+"</b></p><img class=\"img-fluid d-block mx-auto\" src=\"images/books/"+rs.getString("picture_name")+"\" height=\"120\" width=\"70\">\r\n"
				    			+ "        </div>\r\n"
				    			+ "        <div class=\"col-md-4 text-center\">\r\n"
				    			+ "          <p class=\"text-center\">Price: "+rs.getDouble("price")*rs.getInt("quantity")+"€</p>\r\n"
				    			+ "        </div>\r\n"
				    			+ "        <div class=\"col-md-4\">\r\n"
				    			+ "          <p class=\"text-center\">Quantity: "+rs.getInt("quantity")+"</p>\r\n"
				    			+ "        </div>\r\n"
				    			+ "      </div>");
				    }
				}
			}
		} catch (Exception e) { 
			e.printStackTrace();
	        out.println("An error occured displaying an order.");
		}
		out.println("<div class=\"row d-flex justify-content-end m-3\">\r\n"
				+ "      <p class=\"text-primary\">Amount: "+request.getParameter("amount")+"€</p>\r\n"
				+ "    </div>\r\n");
		out.println("<div class=\"col-md-12 text-center\"><a class=\"btn btn-outline-light\" href=\"ordersServlet?operation=1\">Back to orders</a></div></div>");
	}

}

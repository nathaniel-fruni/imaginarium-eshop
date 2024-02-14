package customer;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import util.DButil;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class basketServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public basketServlet() {
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
		if (operation.equals("removeItem")) { removeItem(out, request, Integer.parseInt(request.getParameter("product_id"))); }
		if (operation.equals("increaseQuantity")) { increaseQuantity(out, request, response); }
		if (operation.equals("decreaseQuantity")) { decreaseQuantity(out, request, response); }
		
		mainServlet.createHtmlBegining(out, request);
		mainServlet.createHeader(out, request);
		if (operation.equals("placeOrder")) {
			if (placeOrder(out, request, response)) {
				createOrderSuccessfulPage(out, request);
			} else {
				createOrderUnsuccessfulPage(out, request);
			}
		} else {
			createMain(out, request, response);
		}
	    mainServlet.createFooter(out, request);
	    mainServlet.createHtmlEnd(out, request);
	}
	
    private void createMain(PrintWriter out, HttpServletRequest request, HttpServletResponse response) {
    	double amount = 0;
    	out.println("<div class=\"bg-dark py-3 mx-5 mt-5 mb-2\" style=\"	background-image: linear-gradient(to bottom, rgba(0,0,0,0.2), rgba(0,0,0,0.8));	background-position: top left;	background-size: 100%;	background-repeat: repeat;\">\r\n"
				+ "    <div class=\"container\">\r\n"
				+ "      <div class=\"row\">\r\n"
				+ "        <div class=\"col-md-12\">\r\n"
				+ "          <h1 class=\"text-center\" style=\"	text-shadow: 0px 0px 4px white;\">Basket</h1>\r\n"
				+ "        </div>\r\n"
				+ "      </div>\r\n");
				try {
					String sql = "SELECT products.id AS product_id, products.book_name AS book_name, products.author_name AS author_name, products.picture_name AS picture_name, \r\n"
				    		+ "basket.price AS price, basket.quantity AS quantity\r\n"
				    		+ "FROM `basket`\r\n"
				    		+ "INNER JOIN products ON (basket.product_id=products.id)\r\n"
				    		+ "WHERE customer_id = ?";
					int customer_id = mainServlet.getUserID(request, response);
					
					try (PreparedStatement stmt = DButil.getConnection(request).prepareStatement(sql);) {
						stmt.setInt(1, customer_id);
						try (ResultSet rs = stmt.executeQuery()) {
							while (rs.next()) {
						    	out.println("<div class=\"row mb-3\">\r\n"
						    			+ "        <div class=\"col-md-3\">\r\n"
						    			+ "          <p class=\"text-center\"><b>"+rs.getString("book_name")+" - "+rs.getString("author_name")+"</b></p><img class=\"img-fluid d-block mx-auto\" src=\"images/books/"+rs.getString("picture_name")+"\" height=\"120\" width=\"70\">\r\n"
						    			+ "        </div>\r\n"
						    			+ "        <div class=\"col-md-3 text-center\">\r\n"
						    			+ "          <p class=\"text-center\">Item price: "+rs.getDouble("price")/rs.getInt("quantity")+"€</p>\r\n"
						    			+ "        </div>\r\n"
						    			+ "        <div class=\"col-md-3 text-center\">\r\n"
						    			+ "          <p class=\"text-center\">Price: "+rs.getDouble("price")+"€</p>\r\n"
						    			+ "        </div>\r\n"
						    			+ "        <div class=\"col-md-3\" >\r\n"
						    			+ "          <div class=\"d-flex align-items-center justify-content-center\">\r\n"
						    			+ "            <form action=\"basketServlet\" method=\"post\" class=\"text-center\">\r\n"
						    			+ "              <input type=\"hidden\" name=\"operation\" value=\"decreaseQuantity\">\r\n"
						    			+ "              <input type=\"hidden\" name=\"product_id\" value=\""+rs.getInt("product_id")+"\">\r\n"
						    			+ "              <input type=\"hidden\" name=\"quantity\" value=\""+rs.getInt("quantity")+"\">"
						    			+ "              <button type=\"submit\" class=\"btn btn-link text-dark p-0\"><i class=\"fa fa-minus-square-o text-body\"></i></button>\r\n"
						    			+ "            </form>\r\n"
						    			+ "            <p class=\"text-center mx-2 mt-2\">Quantity: "+rs.getInt("quantity")+"</p>\r\n"
						    			+ "            <form action=\"basketServlet\" method=\"post\" class=\"text-center\">\r\n"
						    			+ "              <input type=\"hidden\" name=\"operation\" value=\"increaseQuantity\">\r\n"
						    			+ "              <input type=\"hidden\" name=\"product_id\" value=\""+rs.getInt("product_id")+"\">\r\n"
						    			+ "              <button type=\"submit\" class=\"btn btn-link text-dark p-0\"><i class=\"fa fa-plus-square-o text-body\" ></i></button>\r\n"
						    			+ "            </form>\r\n"
						    			+ "          </div>\r\n"
						    			+ "          <form action=\"basketServlet\" method=\"post\" class=\"text-center\">\r\n"
						    			+ "            <input type=\"hidden\" name=\"operation\" value=\"removeItem\">\r\n"
						    			+ "            <input type=\"hidden\" name=\"product_id\" value=\""+rs.getInt("product_id")+"\">\r\n"
						    			+ "            <input type=\"submit\" class=\"btn btn-sm text-danger btn-dark\" value=\"Remove item\">\r\n"
						    			+ "          </form>\r\n"
						    			+ "        </div>"
						    			+ "      </div>");	
						    	amount = Math.round((amount + rs.getDouble("price")) * 100.0) / 100.0;
						    }
						}
					}
					
				    out.println("<div class=\"row d-flex justify-content-end m-3\">\r\n"
							+ "      <p class=\"text-primary\">Amount: "+amount+"€</p>\r\n"
							+ "    </div>\r\n"
							+ "  </div>");
				} catch (Exception e) {
					e.printStackTrace();
			        out.println("An error occurred displaying basket content.");
				}
		out.println("</div>"
				+ "</div>");
		out.println("    <div class=\"container\">\r\n"
				+ "<div class=\"row\">\r\n"
				+ "      <div class=\"col-md-6 text-center\\ text-left\" ><a class=\"btn btn-outline-light\" href=\"mainServlet?operation=1\">Back</a></div>\r\n"
				+ "      <div class=\"col-md-6 text-center\">\r\n"
				+ "        <form action=\"basketServlet?operation=placeOrder\" method=\"post\" class=\"text-right\"> "
				+ "         <input type=\"hidden\" name=\"operation\" value=\"placeOrder\"> "
				+ "         <input type=\"hidden\" name=\"amount\" value=\""+amount+"\"> "
				+ "         <input type=\"submit\" value=\"Place an order\" class=\"btn btn-outline-primary\"> "
				+ "        </form>\r\n"
				+ "      </div>\r\n"
				+ "    </div>\r\n"
				+ "    </div>\r\n");
	}
    
    private void createOrderSuccessfulPage(PrintWriter out, HttpServletRequest request) {
    	out.println("<div class=\"py-5\" >\r\n"
    			+ "    <div class=\"container\">\r\n"
    			+ "      <div class=\"row\">\r\n"
    			+ "        <div class=\"px-5 col-md-8 text-center mx-auto\">\r\n"
    			+ "          <h3 class=\"text-success display-4\"> <b>Order successful</b></h3>\r\n"
    			+ "          <h2 class=\"my-3\">Thank you for your purchase!</h2>\r\n"
    			+ "          <p class=\"mb-3\">Your order has been successfully placed and is now being processed. We appreciate your business and look forward to serving you again.</p> <a href=\"mainServlet?operation=1\" class=\"btn btn-outline-light\">Back to home</a>\r\n"
    			+ "        </div>\r\n"
    			+ "      </div>\r\n"
    			+ "    </div>\r\n"
    			+ "  </div>");
    }
    
    private void createOrderUnsuccessfulPage(PrintWriter out, HttpServletRequest request) {
    	out.println("<div class=\"py-5\">\r\n"
    			+ "    <div class=\"container\">\r\n"
    			+ "      <div class=\"row\">\r\n"
    			+ "        <div class=\"px-5 col-md-8 text-center mx-auto\">\r\n"
    			+ "          <h3 class=\"text-primary display-4\"> <b class=\"text-danger\">Order unsuccessful!</b></h3>\r\n"
    			+ "          <h2 class=\"my-3\">We apologise, but your order was not placed.</h2>\r\n"
    			+ "          <p class=\"mb-3\">It seems that the availability of some items in your basket has changed.<br><br>The quantity of affected items has been adjusted to the maximum available stock or removed from your basket if currently out of stock.&nbsp;</p> <a href=\"mainServlet?operation=1\" class=\"btn btn-outline-light\">Back to home</a>\r\n"
    			+ "        </div>\r\n"
    			+ "      </div>\r\n"
    			+ "    </div>\r\n"
    			+ "  </div>");
    }
    
    private void removeItem(PrintWriter out, HttpServletRequest request, int product_id) {
    	try {
    		String sql = "DELETE FROM basket WHERE product_id = ?";
    		try (PreparedStatement stmt = DButil.getConnection(request).prepareStatement(sql)) {
    			stmt.setInt(1, product_id);
    			stmt.executeUpdate();
    		}
    	} catch (Exception e) {
    		e.printStackTrace();
	        out.println("An error occurred removing an item from basket.");
    	}
    }
    
    private void increaseQuantity(PrintWriter out, HttpServletRequest request, HttpServletResponse response) {
    	try {
    		String sql = "UPDATE basket SET price = ROUND(price + (price / quantity), 2), quantity = quantity + 1 "
    				+ "WHERE product_id = ? AND customer_id = ?";
    		int customer_id = mainServlet.getUserID(request, response);
    		
    		try (PreparedStatement stmt = DButil.getConnection(request).prepareStatement(sql)) {
    			stmt.setInt(1, Integer.parseInt(request.getParameter("product_id")));
    			stmt.setInt(2, customer_id);
    			stmt.executeUpdate();
    		}
    	} catch (Exception e) {
    		e.printStackTrace();
	        out.println("An error occurred increasing an item quantity from basket.");
    	}
    }
    
    private void decreaseQuantity(PrintWriter out, HttpServletRequest request, HttpServletResponse response) {
    	// if quantity == 1, delete the product from basket
    	if (Integer.parseInt(request.getParameter("quantity")) == 1) {
    		removeItem(out, request, Integer.parseInt(request.getParameter("product_id")));
    		return;
    	}
    	
    	// else decrease quantity
    	try {
    		String sql = "UPDATE basket SET price = ROUND(price - (price / quantity), 2), quantity = quantity - 1 "
    				+ "WHERE product_id = ? AND customer_id = ?";
    		int customer_id = mainServlet.getUserID(request, response);
    		
    		try (PreparedStatement stmt = DButil.getConnection(request).prepareStatement(sql)) {
    			stmt.setInt(1, Integer.parseInt(request.getParameter("product_id")));
    			stmt.setInt(2, customer_id);
    			stmt.executeUpdate();
    		}
    	} catch (Exception e) {
    		e.printStackTrace();
	        out.println("An error decreasing an item quantity from basket.");
    	}
    }
    
    private synchronized boolean placeOrder(PrintWriter out, HttpServletRequest request, HttpServletResponse response) {
    	try {
    		int customer_id = mainServlet.getUserID(request, response);
        	double amount = Double.parseDouble(request.getParameter("amount"));
        	
        	// if items are in stock and basket is not empty
        	if (inStock(out, request, customer_id) && amount > 0) {
        		int order_number = getOrderNumber(out, request);
        	    insertOrder(out, request, customer_id, order_number, amount); // write to orders table
        	    insertOrderItems(out, request, customer_id); // write to order_items table
        	    return true;
        	} else { // if not enough items in stock
        		updateUnavaibleItems(out, request, customer_id);
        		return false;
        	}
    	} catch (IOException e) {
    		e.printStackTrace();
	        out.println("An error occurred getting user id.");
	        return false;
    	}
    }
    
    private boolean inStock(PrintWriter out, HttpServletRequest request, int customer_id) {
    	try {
    		String sql = "SELECT basket.product_id AS product_id, basket.quantity AS basket_quantity, "
		    		+ "stock.quantity AS stock_quantity \r\n"
		    		+ "FROM `basket` \r\n"
		    		+ "INNER JOIN stock ON (basket.product_id=stock.product_id) \r\n"
		    		+ "WHERE basket.customer_id = ?";
    		
    		try (PreparedStatement stmt = DButil.getConnection(request).prepareStatement(sql)) {
    			stmt.setInt(1, customer_id);
    			try (ResultSet rs = stmt.executeQuery()) {
    				boolean inStock = true;
    				while (rs.next()) {
    			    	if (rs.getInt("stock_quantity") < rs.getInt("basket_quantity")) {
    			    		inStock = false;
    			    		break;
    			    	}
    			    }
    				return inStock;
    			}
    		}
    	} catch (Exception e) {
    		e.printStackTrace();
	        out.println("An error occurred.");
	        return false;
    	}
    }
    
    private int getOrderNumber(PrintWriter out, HttpServletRequest request) {
    	int order_number = -1;
    	try {
    		String sql = "SELECT COALESCE(MAX(order_number), 0) AS last_order_number FROM `orders`";
    		try (PreparedStatement stmt = DButil.getConnection(request).prepareStatement(sql);
    				ResultSet rs = stmt.executeQuery();) {
    			rs.next();
    			order_number = rs.getInt("last_order_number") + 1;
    		}
    	} catch (Exception e) {
    		e.printStackTrace();
	        out.println("An error occurred.");
    	}
    	return order_number;
    }
    
    private int getLastOrderId(PrintWriter out, HttpServletRequest request) {
    	int order_id = -1;
    	try {
    		String sql = "SELECT COALESCE(MAX(id), 0) AS order_id FROM `orders`";
    		try (PreparedStatement stmt = DButil.getConnection(request).prepareStatement(sql);
    				ResultSet rs = stmt.executeQuery();) {
    			rs.next();
    			order_id = rs.getInt("order_id");
    		}
    	} catch (Exception e) {
    		e.printStackTrace();
	        out.println("An error occurred.");
    	}
    	return order_id;
    }
    
    private void insertOrder(PrintWriter out, HttpServletRequest request, int customer_id, int order_number, double amount) {
    	try {
    		String sql = "INSERT INTO orders (order_number, order_date, customer_id, amount, status) "
    				+ "VALUES (?, CURDATE(), ?, ?, 'new')";
    		try (PreparedStatement stmt = DButil.getConnection(request).prepareStatement(sql)) {
    			stmt.setInt(1, order_number);
    			stmt.setInt(2, customer_id);
    			stmt.setDouble(3, amount);
    			stmt.executeUpdate();
    		}
    	} catch (Exception e) {
    		e.printStackTrace();
	        out.println("An error occurred.");
    	}
    }
    
    private void insertOrderItems(PrintWriter out, HttpServletRequest request, int customer_id) {
    	try {
    		String orderItemsSql = "SELECT basket.product_id AS product_id, basket.price AS price, "
    				+ "basket.quantity AS basket_quantity \r\n"
		    		+ "FROM `basket` \r\n"
		    		+ "INNER JOIN stock ON (basket.product_id=stock.product_id)\r\n"
		    		+ "WHERE basket.customer_id = ?";
    		String deleteSql = "DELETE FROM `basket` WHERE customer_id = ?";
    		
    		try (PreparedStatement orderItemsStmt = DButil.getConnection(request).prepareStatement(orderItemsSql);
    			 PreparedStatement deleteStmt = DButil.getConnection(request).prepareStatement(deleteSql)) {
    			
    			orderItemsStmt.setInt(1, customer_id);
    			try (ResultSet rs = orderItemsStmt.executeQuery()) {
    				while(rs.next()) {
    	    			// insert items into order_items
    	    			insertOrderItem(out, request, rs.getInt("product_id"), rs.getDouble("price"), rs.getInt("basket_quantity"));
    	        		
    	        		// delete items from stock
    	        		deleteFromStock(out, request, rs.getInt("product_id"), rs.getInt("basket_quantity"));
    	    		}
    			}
    			
    			// delete items from basket
    			deleteStmt.setInt(1, customer_id);
    			deleteStmt.executeUpdate();
    			
    		}
    	} catch (Exception e) {
    		e.printStackTrace();
	        out.println("An error occured.");
    	}
    }
    
    private void insertOrderItem(PrintWriter out, HttpServletRequest request, int product_id, double price, int basket_quantity) {
    	int order_id = getLastOrderId(out, request);
    	try {
    		String sql = "INSERT INTO order_items (order_id, product_id, price, quantity) "
    				+ "VALUES (?, ?, ?, ?)";
    		try (PreparedStatement stmt = DButil.getConnection(request).prepareStatement(sql)) {
    			stmt.setInt(1, order_id);
    			stmt.setInt(2, product_id);
    			stmt.setDouble(3, price);
    			stmt.setInt(4, basket_quantity);
    			stmt.executeUpdate();
    		}
    	} catch (Exception e) {
    		e.printStackTrace();
	        out.println("An error occured.");
    	}
    }
    
    private void deleteFromStock(PrintWriter out, HttpServletRequest request, int product_id, int basket_quantity) {
    	try {
    		String sql = "UPDATE stock SET quantity = quantity - ? WHERE product_id = ?";
    		try (PreparedStatement stmt = DButil.getConnection(request).prepareStatement(sql)) {
    			stmt.setInt(1, basket_quantity);
    			stmt.setInt(2, product_id);
    			stmt.executeUpdate();
    		}
    	} catch (Exception e) {
    		e.printStackTrace();
	        out.println("An error occured.");
    	}
    }
    
    private void updateUnavaibleItems(PrintWriter out, HttpServletRequest request, int customer_id) {
    	try {
    		String sql = "SELECT basket.product_id AS product_id, basket.quantity AS basket_quantity, "
		    		+ "stock.quantity AS stock_quantity \r\n"
		    		+ "FROM `basket` \r\n"
		    		+ "INNER JOIN stock ON (basket.product_id=stock.product_id) \r\n"
		    		+ "WHERE basket.customer_id = ?";
    		try (PreparedStatement stmt = DButil.getConnection(request).prepareStatement(sql)) {
    			stmt.setInt(1, customer_id);
    			try (ResultSet rs = stmt.executeQuery()) {
    				while (rs.next()) {
    			    	if (rs.getInt("stock_quantity") < rs.getInt("basket_quantity")) {
    			    		setMaximumQuantity(out, request, rs.getInt("product_id"), rs.getInt("stock_quantity"));
    			    	}
    			    }
    			}
    		}
    	} catch (Exception e) {
    		e.printStackTrace();
	        out.println("An error occured.");
    	}
    }
    
    private void setMaximumQuantity(PrintWriter out, HttpServletRequest request, int product_id, int stock_quantity) {
    	// if there is 0 in stock, delete the item from basket
    	if (stock_quantity == 0) { removeItem(out, request, product_id); return;}
    	try {
    		String sql = "UPDATE basket SET quantity = "+stock_quantity+" WHERE product_id = "+product_id;
    		try (PreparedStatement stmt = DButil.getConnection(request).prepareStatement(sql)) {
    			stmt.setInt(1, stock_quantity);
    			stmt.setInt(2, product_id);
    			stmt.executeUpdate();
    		}
    	} catch (Exception e) {
    		e.printStackTrace();
	        out.println("An error occured.");
    	}
    }

}

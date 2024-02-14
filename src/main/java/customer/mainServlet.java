package customer;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import util.DButil;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class mainServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public mainServlet() {
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
				logout(request, response);
			} catch (IOException e) {
		        e.printStackTrace();
		        out.println("An error occurred during logout.");
			}
			return;
		}
		
		createHtmlBegining(out, request);
	    createHeader(out, request);
	    if (operation.equals("showBook")) { showBook(out, request); }
	    else {
	    	if (operation.equals("buy")) { 
		    	addToBasket(out, request, response); 
		    	createAddedToBasketPage(out);
		    }
		    else { createMain(out, request); }
	    }
	    createFooter(out, request);
	    createHtmlEnd(out, request);
	}
	
	public static void createHtmlBegining(PrintWriter out, HttpServletRequest request) {
		out.println("<!DOCTYPE html>\r\n"
				+ "<html lang=\"sk\">\r\n"
				+ "<head>\r\n"
				+ "<meta charset=\"utf-8\">\r\n"
				+ "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\r\n"
				+ "<title>Imaginarium</title>\r\n"
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
				+ "      <div class=\"collapse navbar-collapse\" id=\"navbar13\"> <a class=\"navbar-brand d-none d-md-block\" href=\"mainServlet?operation=1\">\r\n"
				+ "          <i class=\"fa d-inline fa-lg fa-superpowers\"></i>\r\n"
				+ "          <b> IMAGINARIUM</b>\r\n"
				+ "        </a>\r\n"
				+ "        <ul class=\"navbar-nav mx-auto\">\r\n"
				+ "          <li class=\"nav-item\"> <a class=\"nav-link\" href=\"#\">Welcome, "+displayedName+"</a> </li>\r\n"
				+ "        </ul>\r\n"
				+ "        <ul class=\"navbar-nav\">\r\n"
				+ "          <li class=\"nav-item\"> <a class=\"nav-link\" href=\"ordersServlet?operation=1\">\r\n"
				+ "              <i class=\"fa fa-fw fa-shopping-bag\"></i>Orders </a> </li>\r\n"
				+ "          <li class=\"nav-item\"> <a class=\"nav-link\" href=\"basketServlet?operation=1\">\r\n"
				+ "              <i class=\"fa fa-fw fa-shopping-cart\"></i>Basket </a> </li>\r\n"
				+ "          <li class=\"nav-item\"> <a class=\"nav-link\" href=\"mainServlet?operation=logout\">\r\n"
				+ "              <i class=\"fa fa-fw fa-sign-out\"></i>Log out </a> </li>\r\n"
				+ "        </ul>\r\n"
				+ "      </div>\r\n"
				+ "    </div>\r\n"
				+ "  </nav>");
	}
	
	public static void createFooter(PrintWriter out, HttpServletRequest request) {
		out.println("<div class=\"pb-3 section text-center\">\r\n"
				+ "    <div class=\"container\">\r\n"
				+ "      <hr style=\"border-color: white;\">\r\n"
				+ "    </div>\r\n"
				+ "  </div>\r\n"
				+ "  <div class=\"py-3\">\r\n"
				+ "    <div class=\"container\">\r\n"
				+ "      <div class=\"row\">\r\n"
				+ "        <div class=\"col-md-12 text-center\">\r\n"
				+ "          <p class=\"mb-0\">© 2023 Imaginarium</p>\r\n"
				+ "        </div>\r\n"
				+ "      </div>\r\n"
				+ "    </div>\r\n"
				+ "  </div>");
	}
	
	private void createMain(PrintWriter out, HttpServletRequest request) {
		HttpSession session = request.getSession();
		Integer discount = (Integer)session.getAttribute("discount");
		double price = 0.0;
		
		out.println("<div class=\"section-fade-out pt-5\" style=\"background-image: url(&quot;images/background.jpg&quot;); background-position: bottom;\">\r\n"
				+ "    <div class=\"container mt-5 pt-5\">\r\n"
				+ "      <div class=\"row\">\r\n"
				+ "        <div class=\"col-md-6 my-5 text-lg-left text-center align-self-center\">\r\n"
				+ "          <h1 class=\"display-1\"><span style=\"	font-size: 80px;\" class=\"\">Books for you</span></h1>\r\n"
				+ "          <p class=\"lead\">&nbsp; Dive into a World of Imagination</p>\r\n"
				+ "        </div>\r\n"
				+ "        <div class=\"col-lg-6\"><img class=\"img-fluid d-block my-3 mx-auto w-50\" src=\"images/books.png\" width=\"100\"></div>\r\n"
				+ "      </div>\r\n"
				+ "    </div>\r\n"
				+ "  </div>");
		out.println("<div class=\"py-5\">\r\n"
				+ "    <div class=\"container\">\r\n"
				+ "      <div class=\"row\">\r\n"
				+ "        <div class=\"col-md-12\">\r\n"
				+ "          <h1 class=\"display-4 text-center\">Choose your book</h1>\r\n"
				+ "        </div>\r\n"
				+ "      </div>\r\n"
				+ "    </div>\r\n"
				+ "    <div class=\"container\">\r\n"
				+ "      <div class=\"row\">");
		
		// books
		try {
			String sql = "SELECT products.id AS id, products.book_name AS book_name, products.author_name AS author_name, "
		    		+ "products.long_description AS long_description, products.short_description AS short_description, products.price AS price, "
		    		+ "products.picture_name AS picture_name, stock.quantity AS quantity FROM `products` "
		    		+ "INNER JOIN stock ON (products.id=stock.product_id) "
		    		+ "WHERE quantity > 0";
			try (PreparedStatement stmt = DButil.getConnection(request).prepareStatement(sql);
					ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
			    	price = rs.getDouble("price")*(100-discount)/100;
			    	double roundedPrice = Math.round(price * 100.0) / 100.0;
			        out.println("<div class=\"col-md-6 p-2 col-lg-3\">\r\n"
			        		+ "          <div class=\"card bg-dark text-center text-light rounded-0\">\r\n"
			        		+ "            <div class=\"card-body p-3 d-flex flex-column justify-content-between align-items-center\" style=\"background-image: linear-gradient(to bottom, rgba(0,0,0,0.2), rgba(0,0,0,0.8)); height: 450px;\">\r\n"
			        		+ "              <h5>"+rs.getString("author_name")+"</h5>\r\n"
			        		+ "              <img src=\"images/books/"+rs.getString("picture_name")+"\" class=\"img-fluid w-50 mb-2\" alt=\"Book Cover\" style=\"width: 100%; height: 180px; object-fit: cover;\"\">\r\n"
			        		+ "              <h4 class=\"my-2\"> <b>"+rs.getString("book_name")+"</b> </h4\r\n"
			        		+ "              <p>"+rs.getString("short_description")+"</p>\r\n"
			        		+ "              <form action='mainServlet' method='post'>\r\n"
			        		+ "              <input type='hidden' name='id' value= '"+rs.getInt("id")+"'>\r\n"
			        		+ "              <input type='hidden' name='price' value= '"+roundedPrice+"'>\r\n"
			        		+ "              <input type='hidden' name='operation' value= 'showBook'>\r\n"
			        		+ "              <input type='submit' value='Buy "+roundedPrice+"€' class='btn mt-2 btn-outline-light'>\r\n"
			        		+ "              </form>"
			        		+ "            </div>\r\n"
			        		+ "          </div>\r\n"
			        		+ "        </div>");
			     }
			}
		} catch (Exception e) {
			e.printStackTrace();
	        out.println("An error occurred while displaying books.");
		}
		
		out.println("</div>\r\n"
				+ "    </div>\r\n"
				+ "  </div>");
	}
	
	private void showBook(PrintWriter out, HttpServletRequest request) {
		try {
			String sql = "SELECT * FROM products WHERE id = ?";
			try (PreparedStatement stmt = DButil.getConnection(request).prepareStatement(sql)) {
				stmt.setInt(1, Integer.parseInt(request.getParameter("id")));
				try (ResultSet rs = stmt.executeQuery()) {
					while(rs.next()) {
				    	out.println("<div class=\"py-5\">\r\n"
								+ "    <div class=\"container\">\r\n"
								+ "      <div class=\"row\">\r\n"
								+ "        <div class=\"col-md-4\"><img class=\"img-fluid d-block mx-auto p-3\" src=\"images/books/"+rs.getString("picture_name")+"\"></div>\r\n"
								+ "        <div class=\"col-md-8 p-3\">\r\n"
								+ "          <div class=\"row\">\r\n"
								+ "            <div class=\"col-md-12\">\r\n"
								+ "              <div class=\"row\">\r\n"
								+ "                <h2>"+rs.getString("author_name")+"</h2>\r\n"
								+ "              </div>\r\n"
								+ "              <div class=\"row\">\r\n"
								+ "                <h1>"+rs.getString("book_name")+"</h1>\r\n"
								+ "              </div>\r\n"
								+ "              <div class=\"row\">\r\n"
								+ "                <p>"+rs.getString("long_description")+"</p>\r\n"
								+ "                <p>\r\n"
								+ "                </p>\r\n"
								+ "                <h4>"+request.getParameter("price")+"€</h4>\r\n"
								+ "              </div>\r\n"
								+ "              <div class=\"row\">\r\n"
								+ "                <form action=\"mainServlet\" method=\"post\">\r\n"
								+ "                  <input type=\"hidden\" name=\"id\" value=\""+rs.getInt("id")+"\">\r\n"
								+ "                  <input type=\"hidden\" name=\"price\" value=\""+request.getParameter("price")+"\">\r\n"
								+ "                  <input type=\"hidden\" name=\"operation\" value=\"buy\">\r\n"
								+ "                  <input type=\"submit\" value=\"Add to basket\" class=\"btn mt-2 btn-outline-light\">\r\n"
								+ "                </form>\r\n"
								+ "              </div>\r\n"
								+ "            </div>\r\n"
								+ "          </div>\r\n"
								+ "        </div>\r\n"
								+ "        <div class=\"pt-2 pl-3\">\r\n"
								+ "          <div class=\"container\">\r\n"
								+ "            <div class=\"row\">\r\n"
								+ "              <div class=\"col-md-12\"><a class=\"btn btn-outline-light\" href=\"mainServlet?operation=1\" >Back</a></div>\r\n"
								+ "            </div>\r\n"
								+ "          </div>\r\n"
								+ "        </div>"
								+ "      </div>\r\n"
								+ "    </div>\r\n"
								+ "  </div>");
				    }
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
	        out.println("An error occurred while displaying book info.");
		}
	}
	
	private void createAddedToBasketPage(PrintWriter out) {
		out.println("<div class=\"py-5\">\r\n"
				+ "    <div class=\"container\">\r\n"
				+ "      <div class=\"row\">\r\n"
				+ "        <div class=\"px-5 col-md-8 text-center mx-auto\">\r\n"
				+ "          <h3 class=\"display-4\"> <b class=\"text-success\">Added to basket!</b></h3>\r\n"
				+ "          <h2 class=\"my-3\">You can continue shopping or proceed to checkout.</h2>\r\n"
				+ "          <a href=\"mainServlet?operation=1\" class=\"btn btn-outline-light mr-2\" >Continue shopping</a>\r\n"
				+ "          <a href=\"basketServlet?operation=1\" class=\"btn btn-outline-light\" >Checkout</a>\r\n"
				+ "        </div>\r\n"
				+ "      </div>\r\n"
				+ "    </div>\r\n"
				+ "  </div>");
	}
	
	private void addToBasket(PrintWriter out, HttpServletRequest request, HttpServletResponse response) {
		int product_id = Integer.parseInt(request.getParameter("id"));
	    double price = Double.parseDouble(request.getParameter("price"));
	    Connection con = DButil.getConnection(request);
	    
	    
	    try {
	    	Integer customer_id = getUserID(request, response);
	    	String checkIfExistsQuery = "SELECT count(id) AS pocet FROM basket WHERE " 
			          + "(customer_id= ?) AND (product_id = ?)";
	    	String insertQuery = "INSERT INTO basket (customer_id, product_id, price, quantity) values (?, ?, ?, 1)";
	    	String updateQuery = "UPDATE basket SET price = ROUND(price + (price / quantity), 2), quantity = quantity + 1 "
	    			+ "WHERE product_id = ? AND customer_id = ?";
	    	
	    	try (PreparedStatement checkIfExistsStmt = con.prepareStatement(checkIfExistsQuery);
	             PreparedStatement insertStmt = con.prepareStatement(insertQuery);
	             PreparedStatement updateStmt = con.prepareStatement(updateQuery)) {
	    		
	    		// check if the product already exists in the basket
	    		checkIfExistsStmt.setInt(1, customer_id);
	            checkIfExistsStmt.setInt(2, product_id);
	            try (ResultSet rs = checkIfExistsStmt.executeQuery()) {
	            	rs.next();
	            	int quantity = rs.getInt("pocet");
	            	
	            	if (quantity == 0) {
	                    // if the product is not in the basket, insert it
	                    insertStmt.setInt(1, customer_id);
	                    insertStmt.setInt(2, product_id);
	                    insertStmt.setDouble(3, price);
	                    insertStmt.executeUpdate();
	                } else {
	                    // if the product is already in the basket, update the quantity
	                    updateStmt.setDouble(1, product_id);
	                    updateStmt.setInt(2, customer_id);
	                    updateStmt.executeUpdate();
	                }
	            }
	    	}
		  } catch (IOException e) {
			  e.printStackTrace();
			  out.println("An error occured while getting user ID."); 
		  } catch (Exception e) {
			  e.printStackTrace();
		      out.println("Unexpected error occured.");
		  }
	}
	
	public static void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
	    HttpSession session = request.getSession(false);  
	    if (session != null) {
	        session.invalidate();
	    }
	    response.sendRedirect("index.html");
	}
	
	protected static Integer getUserID(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession(false); 
	    if (session != null) {
	    	Integer customer_id = (Integer)session.getAttribute("ID");
			return customer_id;
	    }
	    response.sendRedirect("index.html");
	    return 0;
	}

}

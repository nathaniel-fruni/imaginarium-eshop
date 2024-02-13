package listener;

import java.sql.Connection;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSessionBindingEvent;
import jakarta.servlet.http.HttpSessionBindingListener;

public class Guard implements HttpSessionBindingListener {
	private Connection connection;
	
	public Guard(Connection c) {
		connection = c;
	}
	
	@Override
	public void valueBound(HttpSessionBindingEvent event) {}
	
	@Override
	public void valueUnbound(HttpSessionBindingEvent event) {
		try {
			if (connection != null && !connection.isClosed()) {
                connection.close();
            }

            HttpServletResponse response = (HttpServletResponse) event.getSession()
                    .getServletContext().getAttribute("response");

            if (response != null) {
                response.sendRedirect("index.html");
            }
		} catch (Exception e) { e.printStackTrace(); }
	}
}
package cz.inited.ofy;

import java.io.IOException;
import javax.servlet.http.*;

@SuppressWarnings("serial")
public class OfyServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/plain");
		resp.getWriter().println("9mad$");
	}
}

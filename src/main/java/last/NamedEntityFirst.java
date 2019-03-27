package last;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class NamedEntityFirst extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6741297773865979416L;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	String hashtag = request.getParameter("param");
	System.out.println(hashtag);
	}

}

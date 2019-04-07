package last;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

/**
 * Servlet implementation class HashtagServlet
 */
public class HashtagServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		ArrayList<Hashtag> list = new ArrayList<Hashtag>();
		MongoClient client = new MongoClient("localhost", 27017);
		@SuppressWarnings("deprecation")
		DB database = client.getDB("mzk");
		DBCollection collection = database.getCollection("hashtags");
		DBCursor cursor = collection.find();
		while(cursor.hasNext()){
			DBObject object = cursor.next();
			Hashtag hash_obj = new Hashtag();
			hash_obj.set_id((Integer)object.get("_id"));
			hash_obj.setDesc((String)object.get("desc"));
			list.add(hash_obj);
		}
	request.setAttribute("hashtags", list);
	
	client.close();
	RequestDispatcher rd=request.getRequestDispatcher("/index.jsp");
	rd.forward(request,response);
			}


}

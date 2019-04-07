package last;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.parser.ParseException;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

public class NamedEntityFirst extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6741297773865979416L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String topic = request.getParameter("param");
		boolean containFlag = false;
		MongoClient client = new MongoClient();
		DB database = client.getDB("mzk");
		Set<String> collectionNames = database.getCollectionNames();
		if (!collectionNames.contains(topic)) {
			NamedEntityAndRelativeWordsTweetId namedEntityObject = new NamedEntityAndRelativeWordsTweetId(client,
					database, topic);
			try {
				namedEntityObject.evaluate();
			} catch (ParseException e1) {
				e1.printStackTrace();
			}

			AprioriWithTweets<String> serviceImpl = new AprioriWithTweets<String>(client, database, topic);
			List<Set<String>> data = null;
			try {
				data = serviceImpl.readTransactions();
			} catch (ParseException e) {
				e.printStackTrace();
			}
			Map<Set<String>, Integer> frequentItemSets = serviceImpl.generateFrequentItemSets(data, 2);
			// add topic at end
			try {
				serviceImpl.printCandidates(frequentItemSets);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		client.close();

		request.setAttribute("topic", topic);
		RequestDispatcher rd = request.getRequestDispatcher("/graphPage.jsp");
		rd.forward(request, response);
	}

}

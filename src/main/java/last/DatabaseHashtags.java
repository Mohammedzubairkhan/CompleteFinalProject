package last;
import java.io.*;
import java.net.UnknownHostException;
import com.mongodb.*;
import com.mongodb.MongoClient;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.util.*;
public class DatabaseHashtags {

	public static void main(String[] args) {
		JSONArray arr = new JSONArray();
		JSONParser parser = new JSONParser();
		 JSONObject jsonObject = null;
		try
        {
            Object object = parser
                    .parse(new FileReader("M:\\TweetRepo\\twitter-trendstweet-for-october-2017\\October.txt"));
            
            //convert Object to JSONObject
            jsonObject = (JSONObject)object;
        } catch(Exception e) {	
        	e.printStackTrace();
        }

		JSONObject obj = (JSONObject)jsonObject.get("10-01-2017");
		System.out.print(obj.keySet());
		Iterator value = obj.keySet().iterator();

		try {
			MongoClient client = new MongoClient();
			DB database = client.getDB("mzk");
			DBCollection collection = database.getCollection("hashtags");
			int i = 1;
			while(value.hasNext()) {
				DBObject hashtag = new BasicDBObject("_id", i).append("desc", value.next());
				collection.insert(hashtag);i++;
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
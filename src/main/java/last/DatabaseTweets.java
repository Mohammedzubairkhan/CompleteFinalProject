package last;

import java.io.FileReader;
import java.net.UnknownHostException;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class DatabaseTweets {
	public static void main(String[] args) {
		JSONArray arr = new JSONArray();
		JSONParser parser = new JSONParser();
		 JSONObject jsonObject = null;
		try
        {
            Object object = parser
                    .parse(new FileReader("M:\\TweetRepo\\twitter-trendstweet-for-october-2017\\October_tweets.txt"));
            
            //convert Object to JSONObject
            jsonObject = (JSONObject)object;
        } catch(Exception e) {	
        	e.printStackTrace();
        }
		
		JSONObject obj = (JSONObject)jsonObject.get("10-01-2017");
		Iterator tags = obj.keySet().iterator();
		Iterator desc = obj.values().iterator();
		
		

		try {
			MongoClient client = new MongoClient();
			DB database = client.getDB("mzk");
			DBCollection collection = database.getCollection("tweets");
			while(tags.hasNext()) {
				
				JSONObject description = (JSONObject)desc.next();  
				JSONObject tweetsFromDescription = (JSONObject)description.get("tweets"); //get tweets of specefic topics eg #sundaymorning
				JSONObject modifiedObject = new JSONObject(); 
				Iterator tweetsKeys = tweetsFromDescription.keySet().iterator();
				int i = 1; //primary key
				while(tweetsKeys.hasNext()){
					modifiedObject.put(String.valueOf(i), tweetsKeys.next()); // modify that object where tweet id = 1,2,3 and values = tweets
					i++;
				}
	
				DBObject object = new BasicDBObject("_id", tags.next() ).append("desc", modifiedObject);
				System.out.println(object);
				collection.insert(object);
				client.close();
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}

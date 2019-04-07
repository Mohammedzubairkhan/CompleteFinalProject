package last;

import java.util.*;

import javax.json.Json;

import edu.stanford.nlp.coref.data.CorefChain;
import edu.stanford.nlp.ling.*;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.semgraph.*;
import edu.stanford.nlp.trees.*;
import edu.stanford.nlp.util.CoreMap;
import java.util.*;
import org.apache.log4j.BasicConfigurator;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.util.JSON;

import java.io.*;

//This code is to store named entity words
public class NamedEntityAndRelativeWordsTweetId implements Serializable {

	MongoClient client;
	DB database;
	private String topic;

	public NamedEntityAndRelativeWordsTweetId(MongoClient clientInput, DB databaseInput, String topicInput) {
		client = clientInput;
		database = databaseInput;
		topic = topicInput;
	}

	public void evaluate() throws ParseException {

		DBCollection collectionSave = database.getCollection(topic);
		DBCollection collection = database.getCollection("tweets");

		Object input1 = collection.findOne(topic).get("desc");

		String inputText = new JSON().serialize(input1);

		JSONParser parser = new JSONParser();
		JSONObject input = (JSONObject) parser.parse(inputText);
		BasicConfigurator.configure();

		// creates a StanfordCoreNLP object, with POS tagging, lemmatization, NER,
		// parsing, and coreference resolution
		Properties props = new Properties();
		props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

		JSONObject json = new JSONObject();
		JSONObject jsonSimple = new JSONObject();

		boolean moreNeFlag = false;
		boolean flag = false;
		@SuppressWarnings("unchecked")
		Set<String> keys = input.keySet();

		for (String key : keys) {
			String text = (String) input.get(key);
			flag = false;

			// create an empty Annotation just with the given text
			Annotation document = new Annotation(text);

			// run all Annotators on this text
			pipeline.annotate(document);
			List<CoreMap> sentences = document.get(SentencesAnnotation.class);

			for (CoreMap sentence : sentences) {
				// traversing the words in the current sentenc
				// a CoreLabel is a CoreMap with additional token-specific methods
				String s = "";
				JSONArray pairs = new JSONArray();
				for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
					moreNeFlag = false;
					// this is the text of the token
					String word = token.get(TextAnnotation.class);
					// this is the POS tag of the token
					// String pos = token.get(PartOfSpeechAnnotation.class);
					// this is the NER label of the token
					String ne = token.get(NamedEntityTagAnnotation.class);
					if (!ne.equals("O")) {
						if (flag == false) {
							s += ne;
							flag = true;
						} else
							s += " " + ne;
						// json array to store all words and their corresponding named entities.
						JSONObject temp = new JSONObject();
						// to solve the problem of more ne of same types
						for (int i = 0; i < pairs.size(); i++) {
							JSONObject ob = (JSONObject) pairs.get(i);

							if (ob.containsKey(ne)) {
								String tempNe = (String) ob.get(ne);
								pairs.remove(i);
								temp.put(ne, tempNe + " " + word);
								pairs.add(temp);
								moreNeFlag = true;
								break;
							}
						}
						if (!moreNeFlag) {
							temp.put(ne, word);
							pairs.add(temp);
						}

					}
				}

				if ((!s.equals("")) || (!s.isEmpty())) {
					String index = String.valueOf(key);
					jsonSimple.put(index, s);
				}
				if (!pairs.isEmpty()) {

					String index = String.valueOf(key);
					json.put(index, pairs);
				}

			}

		}
		DBObject object = new BasicDBObject("_id", "mainTransactions").append("desc", jsonSimple);
		collectionSave.insert(object);

		object = new BasicDBObject("_id", "nePairing").append("desc", json);
		collectionSave.insert(object);

	}

}

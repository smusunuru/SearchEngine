package ZapposChallenge;

import java.io.*;
import java.net.*;

import org.json.simple.*;
import org.json.simple.parser.*;

public class Iterate {
	//final because this variable will never change.
	public static final String searchurl = "http://api.zappos.com/Search?key=52ddafbe3ee659bad97fcce7c53592916a6bfd73";
	
	
	//Code borrowed from http://rest.elkstein.org/2008/02/using-rest-in-java.html
	
	public static String httpGet(String urlStr) throws IOException {
		URL url = new URL(urlStr);
		HttpURLConnection conn =
		      (HttpURLConnection) url.openConnection();
	
		if (conn.getResponseCode() != 200) {
		    throw new IOException(conn.getResponseMessage());
		}
	
		// Buffer the result into a string
		BufferedReader rd = new BufferedReader(
	      new InputStreamReader(conn.getInputStream()));
		StringBuilder sb = new StringBuilder();
		String line;
		while ((line = rd.readLine()) != null) {
			sb.append(line);
		}
		rd.close();
	
		conn.disconnect();
		return sb.toString();
	}
	
	//Iterate through the search and make then into json objects (ZapposApi info)
	//convert all the products into json objects
	public static JSONObject parseReply(String reply) throws ParseException {
		JSONParser iterator = new JSONParser();
		//try {
			Object iterateobject = iterator.parse(reply);
			JSONObject obj = (JSONObject)iterateobject;
		//} catch (ParseException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		//}
		return obj;
		
	}
	
	public static JSONArray resultarray(JSONObject obj){
		Object result = obj.get("results");
		JSONArray resultarray = (JSONArray)result;
		return resultarray;
	}
	
}

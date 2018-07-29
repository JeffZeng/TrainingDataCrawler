package crawler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

import javafx.scene.image.Image;





public class ImageClawer {
	static int count=0;
	
	public ImageClawer() {
		super();
	}
	
	static public ArrayList<String>fetch(SearchEngineType type , Object ... query) throws IOException{
		
		ArrayList<String> result = new ArrayList<String>();
		HashMap<String, String> param = SearchEngine.getParam(type);
		System.out.println(String.format(param.get("url"), query));
		URL url = new URL(String.format(param.get("url"), query));
		
		HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.71 Safari/537.36");
		connection.setRequestProperty("Content-type", "txt/html");  
		connection.setRequestProperty("Accept-Charset", "utf-8");  
		
		InputStream stream = connection.getInputStream();
		char [] buf = new char [256];
		int ch;
		
		String response = "";
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
		while( (ch =  reader.read(buf))!= -1) 
			response =(response+new String(buf));
		reader.close();
		stream.close();
		
		String content = response;
		System.out.println(content);
		Pattern pattern=Pattern.compile(param.get("regx"));
		
		Matcher matcher = pattern.matcher(content);
		
		while(matcher.find()) {
			String imgUrl = matcher.group().split("\"ou\":")[1].replaceAll("\"", "");
			result.add(imgUrl);
			System.out.println((count++)+"==>"+imgUrl);
		}
		
		
		
		
		return result;
	}
	
	
}

package crawler;


import java.util.HashMap;



public class SearchEngine {
	final static String [] SEARCH_URL = new String [] { "https://www.google.com/search?tbm=isch&q=%s&ijn=%d&start=%d&tbs=isz:l"};
						
														
	final static String [] SEARCH_REGX = new String [] { "\"ou\":\"(https?://[^\"]+)\""};
	
	
	
	
    static HashMap<String, String> param = new HashMap<String, String>();
   
	
	
	static public HashMap<String, String> getParam(SearchEngineType type) {
		
		param.clear();
		param.put("url", SEARCH_URL[type.ordinal()]);
		param.put("regx", SEARCH_REGX[type.ordinal()]);
		return param;
		
	}
	
	
	

	
	
	
	

}

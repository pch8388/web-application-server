package webserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import util.HttpRequestUtils;
import util.IOUtils;

public class HttpRequest {
	private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
	
	private BufferedReader br = null;
	private Map<String,String> map;
	private Map<String,String> headerMap;
	
	public HttpRequest(InputStream in) {
		try{
			br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
			requestReader(br);
		}catch(IOException e) {
			e.printStackTrace();
		}
	}

	private void requestReader(BufferedReader br) throws IOException{
		String line = br.readLine();
		if(line == null) {
			return;
		}
		
		log.debug("request line : {} " ,line);
		String[] tokens = line.split(" ");

		map = new HashMap<>();
		map.put("method", tokens[0]);
		
		if(tokens[0].equals("GET")) {
			String[] temp = tokens[1].split("\\?"); 
			map.put("path", temp[0]);
			map.put("parameter", temp[1]);
		}
		
		
		headerMap = new HashMap<>();
		while(line != null && !line.equals("")) {
			line = br.readLine();
			log.debug("header : {}", line);
			
			if(line != null && line.contains(":")) {
				String[] temp = line.split(":");
				headerMap.put(temp[0], temp[1].trim());
			}
			
		}
		
		if(tokens[0].equals("POST")) {
			map.put("path", tokens[1]);
			String body = IOUtils.readData(br, Integer.parseInt(headerMap.get("Content-Length")));
			map.put("parameter", body);
		}
	}
	
	public String getMethod() {
		return map.get("method");
	}
	
	public String getPath() {
		return map.get("path");
	}
	
	public String getHeader(String str) {
		return headerMap.get(str);
	}
	
	public String getParameter(String str) {
		Map<String,String> newMap = new HashMap<>();
		newMap = HttpRequestUtils.parseQueryString(map.get("parameter"));
		return newMap.get(str);
	}
}

package webserver;

public class StringTokenHandle {
	String requestUrl(String line) {
		String[] tokens = line.split(" ");
		if(tokens[0].contains("?")) {
			tokens[0] = requestParse(tokens[0]);
		}
		return tokens[0];
	}
	
	String requestParse(String url) {
		int index = url.indexOf("?");
		String requestPath = url.substring(0, index);
		return requestPath;
	}
	
	String requestParam(String url) {
		int index = url.indexOf("?");
		String param = url.substring(index+1);
		return param;
	}
}

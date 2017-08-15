package webserver;

public class StringTokenHandle {
	String requestUrl(String line) {
		String[] tokens = line.split(" ");
		return tokens[0];
	}
}

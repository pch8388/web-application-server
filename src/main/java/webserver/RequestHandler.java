package webserver;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import db.DataBase;
import model.User;
import util.HttpRequestUtils;
import util.IOUtils;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }


    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (BufferedReader buf = new BufferedReader(new InputStreamReader(connection.getInputStream())); 
        		OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
        	
        	String line = buf.readLine();
        	log.debug("requestLine : " + line);
        	
        	if(line == null) {
        		return;
        	}
        	
        	String[] tokens = line.split(" ");
        	/*if(tokens[1].contains("?")) {
        		Map<String,String> map = HttpRequestUtils.parseQueryString(requestParam(tokens[1]));
        		user = userMapping(map);
        		
        		tokens[1] = requestParse(tokens[1]);
        		log.debug("User : "+user.toString());
    		}*/
        	
        	String[] temp;
        	String[] cookie = null;
        	int contentLength = 0;
        	while(!line.equals("")){
        		line = buf.readLine();
        		if(line.contains("Content-Length")) {
        			temp = line.split(" ");
        			contentLength = Integer.parseInt(temp[1]);
        		}
        		if(line.contains("Cookie")) {
        			cookie = line.split(" ");
        		}
        		log.debug("header : " + line);
        	}
        	
        	DataOutputStream dos = new DataOutputStream(out);
        	byte[] body;
        	
        	if(cookie[1].equals("logined=true")) {
        		body = Files.readAllBytes(new File("./webapp/index.html").toPath());
        		response200Header(dos, body.length);
        		responseBody(dos,body);
        	}
        	if(cookie[1].equals("logined=false")) {
        		body = Files.readAllBytes(new File("./webapp/login_failed.html").toPath());
        		response200Header(dos, body.length);
        		responseBody(dos,body);
        	}
        	
        	if(tokens[1].equals("/user/create")) {
        		line = IOUtils.readData(buf, contentLength);
        		Map<String,String> map = HttpRequestUtils.parseQueryString(line);
        		DataBase.addUser(userMapping(map));
        		log.debug("body : {}"+line);
        		body = Files.readAllBytes(new File("./webapp/index.html").toPath());
        		response302Header(dos, body.length);
        		responseBody(dos, body);
        	}
        	if(tokens[1].equals("/user/login")) {
        		line = IOUtils.readData(buf, contentLength);
        		Map<String,String> map = HttpRequestUtils.parseQueryString(line);
        		User user = DataBase.findUserById(map.get("userId"));
        		if(user.getPassword().equals(map.get("password"))) {
        			
        		}
        	}
        	
        	body = Files.readAllBytes(new File("./webapp"+tokens[1]).toPath());
            response200Header(dos, body.length);
            responseBody(dos, body);
        	
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
    
    
    private User userMapping(Map<String,String> map) {
    	String userId = (String)map.get("userId");
    	String password = (String)map.get("password");
    	String name = (String)map.get("name");
    	String email = (String)map.get("email");
    	return new User(userId,password,name,email);
    }
    
/*    private String requestParse(String url) {
		int index = url.indexOf("?");
		String requestPath = url.substring(0, index);
		return requestPath;
	}
	
	private String requestParam(String url) {
		int index = url.indexOf("?");
		String param = url.substring(index+1);
		return param;
	}
*/
    private void response302Header(DataOutputStream dos,int lengthOfBodyContent) {
    	try {
    		dos.writeBytes("HTTP/1.1 302 Found \r\n");
    		dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
    		dos.writeBytes("Content-Length: " + lengthOfBodyContent+ "\r\n");
    		dos.writeBytes("Location: http://localhost:8080/index.html");
    		dos.writeBytes("\r\n");
    	}catch(IOException e) {
    		log.error(e.getMessage());
    	}
    }
    
    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
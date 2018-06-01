package webserver;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
        	  BufferedReader reader = new BufferedReader(new InputStreamReader(in,"UTF-8"));
        	  DataOutputStream dos = new DataOutputStream(out);
        	  
        	  String line = "";
        	  int index = 0;
        	  String url = "";
        	  int contentLength = 0;
        	  
        	  while(!"".equals(line=reader.readLine())) {
        		  if(line == null) {
        			  return;
        		  }
        		  if(index == 0) {
        			  String[] tokens = line.split(" ");
        			  url = tokens[1];
        		  }
        		  if(line.contains("Content-Length")) {
        			  String[] tokens = line.split(" ");
        			  contentLength = Integer.parseInt(tokens[1]);
        		  }
        		  log.debug("line : "+line);  
        		  index++;
        	  }
        	  if(url.contains("/user/create")) {
        		  while(reader.ready()) {
        			  line = IOUtils.readData(reader, contentLength);
        			  log.debug("body line : "+line);
        		  }
        		  int indexOf = line.indexOf("?");
//            	  requestPath = url.substring(0, indexOf);
            	  String param = line.substring(indexOf+1);
            	  Map<String,String> map = HttpRequestUtils.parseQueryString(param);
            	  User user = new User(map.get("userId"), map.get("password"), map.get("name"), map.get("email"));
            	  log.debug("user============>>"+user.toString());
        	  }

        	  byte[] body = Files.readAllBytes(new File("./webapp"+url).toPath());
            response200Header(dos, body.length);
            responseBody(dos, body);
        } catch (IOException e) {
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

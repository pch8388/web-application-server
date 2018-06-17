package webserver;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpResponse {
    private Logger log = LoggerFactory.getLogger(HttpResponse.class);
    private OutputStream out;
    private Map<String,String> map;
    
    public HttpResponse(OutputStream out) {
        this.out = out;
        map = new HashMap<>();
    }

    public void forward(String string) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);
        byte[] body = Files.readAllBytes(new File("./webapp" + string).toPath());
        
        map.put("Content-Length", String.valueOf(body.length));
        if(string.endsWith("css")) {
            map.put("Content-Type", "text/css;charset=utf-8");
            response200Header(dos, map);
        }else {
            map.put("Content-Type", "text/html;charset=utf-8");
            response200Header(dos, map);
        }
        responseBody(dos, body);
    }

    public void sendRedirect(String string) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);
        byte[] body = Files.readAllBytes(new File("./webapp" + string).toPath());
        map.put("Location", string);
        response302Header(dos);
        responseBody(dos, body);
    }

    public void addHeader(String key, String value) {
        map.put(key, value);
    }
    
    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.writeBytes("\r\n");
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response200Header(DataOutputStream dos, Map<String,String> map) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            for(Map.Entry<String, String> entry : map.entrySet()) {
                dos.writeBytes(entry.getKey() + ": " + entry.getValue() + "\r\n");
            }
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
    
    private void response302Header(DataOutputStream dos) {
        try {
            dos.writeBytes("HTTP/1.1 302 Redirect \r\n");
            for(Map.Entry<String, String> entry : map.entrySet()) {
                dos.writeBytes(entry.getKey() + ": " + entry.getValue() + "\r\n");
            }
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}

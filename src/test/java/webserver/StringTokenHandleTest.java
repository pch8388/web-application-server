package webserver;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class StringTokenHandleTest {
	
	private StringTokenHandle st;
	
	@Before
	public void setup() {
		st = new StringTokenHandle();
	}
	
	@Test
	public void requestUrl() {
		assertEquals("/index.html", st.requestUrl("/index.html?aaaaaaaaaaa a b c d e f"));
		System.out.println("requestUrl");
	}
	
	@Test
	public void requestParse() {
		assertEquals("/user/create", st.requestParse("/user/create?userID=java&password=pass&name=Jaesung"));
	}
	
	@Test
	public void requestParam() {
		assertEquals("userID=java&password=pass&name=Jaesung", st.requestParam("/user/create?userID=java&password=pass&name=Jaesung"));
	}
}

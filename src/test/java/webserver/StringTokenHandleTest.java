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
		assertEquals("/index.html", st.requestUrl("/index.html a b c d e f"));
		System.out.println("requestUrl");
	}
}

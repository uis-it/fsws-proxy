
package no.uis.fsws.proxy;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class HelloWorldImplTest {

    @Test
	public void testSayHi() {
        FswsProxyImpl helloWorldImpl = new FswsProxyImpl();            	
        String response = helloWorldImpl.sayHi("Sam");
        assertEquals("HelloWorldImpl not properly saying hi", "Hello Sam", response);
    }
}

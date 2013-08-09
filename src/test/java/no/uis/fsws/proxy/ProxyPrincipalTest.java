/*
 Copyright 2013 University of Stavanger, Norway

 Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

package no.uis.fsws.proxy;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import no.uis.fsws.proxy.ProxyPrincipalImpl;

import org.junit.Test;

public class ProxyPrincipalTest {

  @Test
  public void testEquals() {
    Principal p1 = new ProxyPrincipalImpl("test", "Test");

    Principal p2 = new ProxyPrincipalImpl("test", "Test");

    assertThat(p1, equalTo(p2));
    
    Map<Principal, String> map = new HashMap<>();
    
    map.put(p1, "Test");
    map.put(p2, "Test2");
    
    assertThat(map.size(), is(1));
  }
}

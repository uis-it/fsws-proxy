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

package no.uis.fsws.proxy.impl;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class StudinfoServiceFactoryTest {

  @Test
  public void putTransformerURLAsString() {
    StudinfoServiceFactory factory = new StudinfoServiceFactory();

    String urlString = factory.getTransformerUrlsAsString();
    assertThat(urlString, is("{}"));

    factory.setUserTransformerUrl("test", "classpath:fspreprocess-uis.xsl");
    assertThat(factory.getTransformerUrlsAsString(), startsWith("{test="));
    assertThat(factory.getTransformerUrlsAsString(), endsWith("fspreprocess-uis.xsl}"));
  }
  
  @Test
  public void setTransformerUrlMap() throws Exception {
    StudinfoServiceFactory factory = new StudinfoServiceFactory();
    Map<String, URL> map = new HashMap<>();
    final URL testUrl = new URL("http://www.uis.no");
    map.put("test", testUrl);
    factory.setTransformerUrls(map);
    
    assertThat(factory.getTransformerUrl("test"), is(testUrl));
    
    map.clear();
    map.put("test2", testUrl);
    factory.setTransformerUrls(map);
    
    assertThat(factory.getTransformerUrl("test"), is(nullValue()));
  }
}

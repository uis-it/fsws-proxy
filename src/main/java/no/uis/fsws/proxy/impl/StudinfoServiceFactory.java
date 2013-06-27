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

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.springframework.beans.factory.annotation.Required;

import no.uis.fsws.proxy.FsWsServiceFactory;
import no.uis.fsws.proxy.ProxyPrincipal;
import no.uis.fsws.studinfo.StudInfoImport;
import no.uis.fsws.studinfo.impl.StudInfoImportImpl;
import no.usit.fsws.studinfo.StudInfoService;

/**
 * Creates a proxy for FW-WS studinfo.
 */
public class StudinfoServiceFactory implements FsWsServiceFactory<StudInfoImport> {

  private String fswsAddress;
  private String xmlSourceParser;
  private boolean copyXml;
  private Map<String, URL> transformerUrls;

  private Map<ProxyPrincipal, StudInfoImport> serviceCache = new HashMap<>();
  
  @Required
  public void setFswsAddress(String fswsAddress) {
    this.fswsAddress = fswsAddress;
  }

  @Required
  public void setTransformerUrls(Map<String, URL> transformerUrls) {
    this.transformerUrls = transformerUrls;
  }

  public void setXmlSourceParser(String xmlSourceParser) {
    this.xmlSourceParser = xmlSourceParser;
  }

  public void setCopyXml(boolean copyXml) {
    this.copyXml = copyXml;
  }

  @Override
  public StudInfoImport getService(ProxyPrincipal p) {
    
    StudInfoImport svc = serviceCache.get(p);
    if (svc == null) {
      synchronized(serviceCache) {
        svc = serviceCache.get(p);
        if (svc == null) {
          JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
          factory.setAddress(this.fswsAddress);
          factory.setServiceClass(StudInfoService.class);
          factory.setUsername(p.getName());
          factory.setPassword(p.getPassword());
          
          StudInfoService fsServiceStudInfo = (StudInfoService)factory.create();
          
          StudInfoImportImpl impl = new StudInfoImportImpl();
          impl.setCopyXML(this.copyXml);
          impl.setFsServiceStudInfo(fsServiceStudInfo);
          impl.setXmlSourceParser(this.xmlSourceParser);
          impl.setTransformerUrl(getTransformerUrl(p.getName()));
          svc = impl;
          serviceCache.put(p, impl);
        }
      }
    }
    return svc;
  }

  private URL getTransformerUrl(String username) {
    return transformerUrls.get(username);
  }
}

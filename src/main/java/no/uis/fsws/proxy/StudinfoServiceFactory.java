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

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.PreDestroy;

import lombok.NonNull;
import lombok.Setter;

import no.usit.fsws.studinfo.StudInfoService;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.beans.propertyeditors.URLEditor;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedOperationParameter;
import org.springframework.jmx.export.annotation.ManagedOperationParameters;
import org.springframework.jmx.export.annotation.ManagedResource;

/**
 * Creates a proxy for FW-WS studinfo.
 */
@ManagedResource(
  objectName = "uis:service=ws-fsws-proxy,component=studinfo-service-factory",
  description = "Studinfo Service Proxy",
  log = false
)
public class StudinfoServiceFactory implements FsWsServiceFactory<StudInfoImport> {

  @Setter(onMethod = @_(@Required)) @NonNull private String fswsAddress;
  @Setter private String xmlSourceParser;
  @Setter private boolean copyXml;
  private Map<String, URL> transformerUrls = new HashMap<>();

  private Map<ProxyPrincipal, StudInfoImport> serviceCache = new HashMap<>();
  
  @Required
  public void setTransformerUrls(Map<String, URL> urls) {
    synchronized(transformerUrls) {
      transformerUrls.clear();
      transformerUrls.putAll(urls);
    }
    synchronized(serviceCache) {
      serviceCache.clear();
    }
  }

  @ManagedOperation(description = "Get transformer URLs per username as a String")
  public String getTransformerUrlsAsString() {
    // TODO: this string could be cached
    synchronized(transformerUrls) {
      Map<String, URL> m = new TreeMap<>(transformerUrls);
      return m.toString();
    }
  }
  
  @ManagedOperation(description = "Set the user-dependent transformer URL")
  @ManagedOperationParameters(
    {
      @ManagedOperationParameter(name = "username", description = "the username"),
      @ManagedOperationParameter(name = "url", description = "string representation of the URL, Spring syntax")
    }
  )
  public void setUserTransformerUrl(String username, String url) {
    URLEditor urlEditor = new URLEditor();
    urlEditor.setAsText(url);
    URL trUrl = (URL)urlEditor.getValue();

    // do the clean-up successively to avoid dead-lock
    putTransformerUrl(username, trUrl);
    clearServiceCacheEntry(username);
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

  /**
   * Remove all service entries that have a principal with the given user name.
   * Typically there is just one principal with any given user name, 
   * but an authenticator implementation might choose to support multiple principal
   * with the same name but different credentials.  
   * @param username
   */
  private void clearServiceCacheEntry(String username) {
    synchronized(serviceCache) {
      List<ProxyPrincipal> toRemove = new ArrayList<ProxyPrincipal>();
      for (ProxyPrincipal p : serviceCache.keySet()) {
        if (p.getName().equals(username)) {
          toRemove.add(p);
        }
      }
      for (ProxyPrincipal p : toRemove) {
        serviceCache.remove(p);
      }
    }
  }
  
  private void putTransformerUrl(String username, URL trUrl) {
    synchronized(transformerUrls) {
      transformerUrls.put(username, trUrl);
    }
  }
  
  protected URL getTransformerUrl(String username) {
    synchronized(transformerUrls) {
      return transformerUrls.get(username);
    }
  }
  
  @PreDestroy
  public void close() {
      serviceCache.clear();
  }
}

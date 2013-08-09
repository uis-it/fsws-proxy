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

import lombok.NonNull;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

import org.apache.cxf.message.Message;
import org.apache.cxf.phase.PhaseInterceptorChain;
import org.springframework.beans.factory.annotation.Required;

/**
 * Common base class for Fsws Proxies.
 * @param <T>
 */
@Log4j
public abstract class AbstractFswsProxy<T> {
  /**
   * CXF context property name for the user principal.
   */
  public static final String PRINCIPAL = "fsws-proxy.principal";
  private static final String NO_SERVICE_CONFIGURED_FOR_USER = "No service configured for user";
  
  @Setter(onMethod = @_(@Required)) @NonNull private FsWsServiceFactory<T> serviceFactory;

  protected ProxyPrincipal getCurrentPrinciapl() {

    final Message m = PhaseInterceptorChain.getCurrentMessage();
    if (m != null) {
      return (ProxyPrincipal)m.getContextualProperty(PRINCIPAL);
    }
    return null;
  }

  /**
   * @throws IllegalArgumentException if either user or service not found
   * @return The service proxy for the given user 
   */
  public T getServiceForPrincipal() {
    ProxyPrincipal p = getCurrentPrinciapl();
    T svc = null;
    if (p != null) {
      svc = serviceFactory.getService(p);
      if (svc != null) {
        return svc;
      }
    }
    log.warn(NO_SERVICE_CONFIGURED_FOR_USER);
    throw new IllegalStateException(NO_SERVICE_CONFIGURED_FOR_USER);
  }
}

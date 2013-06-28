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

import lombok.NonNull;
import lombok.Setter;
import no.uis.fsws.proxy.FsWsServiceFactory;
import no.uis.fsws.proxy.ProxyPrincipal;

import org.apache.cxf.message.Message;
import org.apache.cxf.phase.PhaseInterceptorChain;
import org.springframework.beans.factory.annotation.Required;

/**
 * Common base class for Fsws Proxies.
 * @param <T>
 */
public abstract class AbstractFswsProxy<T> {
  /**
   * CXF context property name for the user principal.
   */
  public static final String PRINCIPAL = "fsws-proxy.principal";
  
  @Setter(onMethod = @_(@Required)) @NonNull private FsWsServiceFactory<T> serviceFactory;

  protected ProxyPrincipal getCurrentPrinciapl() {

    final Message m = PhaseInterceptorChain.getCurrentMessage();
    if (m != null) {
      return (ProxyPrincipal)m.getContextualProperty(PRINCIPAL);
    }
    return null;
  }

  public T getServiceForPrincipal() {
    ProxyPrincipal p = getCurrentPrinciapl();
    if (p == null) {
      return null;
    }
    return serviceFactory.getService(p);
  }
}

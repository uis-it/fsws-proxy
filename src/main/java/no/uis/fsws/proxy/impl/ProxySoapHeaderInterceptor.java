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

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import lombok.NonNull;
import lombok.Setter;
import lombok.extern.log4j.Log4j;


import org.apache.cxf.binding.soap.interceptor.SoapHeaderInterceptor;
import org.apache.cxf.configuration.security.AuthorizationPolicy;
import org.apache.cxf.endpoint.Endpoint;
import org.apache.cxf.message.Exchange;
import org.apache.cxf.message.Message;
import org.apache.cxf.transport.Conduit;
import org.apache.cxf.ws.addressing.EndpointReferenceType;
import org.apache.log4j.Level;
import org.springframework.beans.factory.annotation.Required;

/**
 * Authorization handler. Thanks to {@linkplain http://kpsoft-tech-blogs.blogspot.no/2011/10/apache-cxf-web-services-tomcat-basic.html}.
 */
@Log4j
public class ProxySoapHeaderInterceptor extends SoapHeaderInterceptor {

  private static final List<String> ZERO_CONTENT_LENGTH = Arrays.asList(new String[] {"0"});

  @Setter private String realm = "realm";
  @Setter(onMethod = @_(@Required)) @NonNull private Authorizer authorizer;

  private List<String> authenticateHeader;

  
  @PostConstruct
  protected void init() {
    this.authenticateHeader = Arrays.asList(new String[] {String.format("Basic realm=%s", this.realm)});
  }
  
  @Override
  public void handleMessage(Message message) {

    AuthorizationPolicy policy = message.get(AuthorizationPolicy.class);

    // If the policy is not set, the user did not specify credentials
    // A 401 is sent to the client to indicate that authentication is required
    if (policy == null) {
      log.warn("User attempted to log in with no credentials");
      sendErrorResponse(message, HttpURLConnection.HTTP_UNAUTHORIZED);
      return;
    }

    if (log.isInfoEnabled()) {
      log.info("Logged in user: " + policy.getUserName());
    }

    boolean authorized = false;
    ProxyPrincipal principal = authorizer.authenticate(policy.getUserName(), policy.getPassword());
    if (principal != null) {
      if (authorizer.hasAuthorization(principal, policy.getAuthorizationType(), policy.getAuthorization())) {
        message.setContextualProperty(AbstractFswsProxy.PRINCIPAL, principal);
        authorized = true;
      }
    }

    if (!authorized) {
      if (log.isEnabledFor(Level.WARN)) {
        log.warn("User not authorized: " + policy.getUserName());
      }
      sendErrorResponse(message, HttpURLConnection.HTTP_FORBIDDEN);
    }
  }

  private void sendErrorResponse(Message message, int responseCode) {
    Message outMessage = getOutMessage(message);
    outMessage.put(Message.RESPONSE_CODE, responseCode);

    // Set the response headers
    @SuppressWarnings("unchecked")
    Map<String, List<String>> responseHeaders = (Map<String, List<String>>)message.get(Message.PROTOCOL_HEADERS);
    if (responseHeaders != null) {
      responseHeaders.put("WWW-Authenticate", authenticateHeader);
      responseHeaders.put("Content-Length", ZERO_CONTENT_LENGTH);
    }
    message.getInterceptorChain().abort();
    try {
      getConduit(message).prepare(outMessage);
      close(outMessage);
    } catch(IOException e) {
      e.printStackTrace();
    }
  }

  private Message getOutMessage(Message inMessage) {
    Exchange exchange = inMessage.getExchange();
    Message outMessage = exchange.getOutMessage();
    if (outMessage == null) {
      Endpoint endpoint = exchange.get(Endpoint.class);
      outMessage = endpoint.getBinding().createMessage();
      exchange.setOutMessage(outMessage);
    }
    outMessage.putAll(inMessage);
    return outMessage;
  }

  private Conduit getConduit(Message inMessage) throws IOException {

    if (log.isTraceEnabled()) {
      log.trace("inmessage is: " + inMessage);
    }
    Exchange exchange = inMessage.getExchange();
    if (exchange == null && log.isInfoEnabled()) {
      log.info("Exchnage is null");
    }

    EndpointReferenceType target = exchange.get(EndpointReferenceType.class);
    if (target == null && log.isInfoEnabled()) {
      log.info("target is null");
    }

    Conduit conduit = exchange.getDestination().getBackChannel(inMessage, null, target);
    exchange.setConduit(conduit);

    return conduit;
  }

  private void close(Message outMessage) throws IOException {
    OutputStream os = outMessage.getContent(OutputStream.class);
    os.flush();
    os.close();
  }
}

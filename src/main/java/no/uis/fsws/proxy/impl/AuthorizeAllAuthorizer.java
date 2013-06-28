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

import java.security.Principal;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import no.uis.fsws.proxy.Authorizer;
import no.uis.fsws.proxy.ProxyPrincipal;

/**
 * Simple Authorizer that does not verify the password and authorizes all users. 
 */
public class AuthorizeAllAuthorizer implements Authorizer {

  @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
  private static final class SimplePrincipal implements ProxyPrincipal {
    @Getter private final String name;
    @Getter private final String password;
  }

  @Override
  public ProxyPrincipal authenticate(String username, String password) {
    return new SimplePrincipal(username, password);
  }

  @Override
  public boolean hasAuthorization(Principal principal, String authorizationType, String authorization) {
    return principal != null;
  }
}

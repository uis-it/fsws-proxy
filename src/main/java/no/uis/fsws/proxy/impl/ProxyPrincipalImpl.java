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

/**
 * A username/password principal. 
 */
public class ProxyPrincipalImpl implements Principal {

  private final String username;
  private final String password;

  ProxyPrincipalImpl(String username, String password) {
    this.username = username;
    this.password = password;
  }
  
  @Override
  public String getName() {
    return this.username;
  }

  public String getPassword() {
    return password;
  }
}

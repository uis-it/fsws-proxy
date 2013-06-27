
package no.uis.fsws.proxy;

import javax.jws.WebService;

@WebService(endpointInterface = "no.uis.fsws.proxy.FswsProxy")
public class FswsProxyImpl implements FswsProxy {

    public String sayHi(String text) {
        return "Hello " + text;
    }
}


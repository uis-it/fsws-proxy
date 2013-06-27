package no.uis.fsws.proxy;

import javax.jws.WebService;

@WebService
public interface FswsProxy {
    String sayHi(String text);
}


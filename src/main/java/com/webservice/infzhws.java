package com.webservice;

import javax.jws.WebParam;
import javax.jws.WebService;

@WebService(targetNamespace="http://webServices.tanzhonghe365.com/")
public interface infzhws {
    public String query(@WebParam(name = "QueryXmlDoc") String QueryXmlDoc);

    public String write(@WebParam(name = "WriteXmlDoc") String WriteXmlDoc);
}

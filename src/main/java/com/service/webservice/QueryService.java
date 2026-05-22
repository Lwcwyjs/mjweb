package com.service.webservice;

import com.commons.result.XmlQueryResult;

import java.io.UnsupportedEncodingException;

public interface QueryService {
    public XmlQueryResult dealCommand(String qybh, String jkxlh, String jkid, String sXmlDoc) throws UnsupportedEncodingException;
}

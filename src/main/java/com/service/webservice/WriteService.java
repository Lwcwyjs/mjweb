package com.service.webservice;

import com.commons.utils.XmlWriteResult;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;

public interface WriteService {
    public XmlWriteResult dealCommand(String qybh, String jkxlh, String jkid, String sXmlDoc) throws UnsupportedEncodingException, ParseException;
}

package com.webservice;

import com.commons.result.XmlQueryResult;
import com.commons.utils.DecodeHelper;
import com.commons.utils.XmlHelper;
import com.commons.utils.XmlWriteResult;
import com.service.webservice.QueryService;
import com.service.webservice.WriteService;
import org.apache.cxf.transport.http.AbstractHTTPDestination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.WebServiceContext;

@WebService(targetNamespace="http://webServices.tanzhonghe365.com/")
public class implzhws implements infzhws {

    private static final Logger logger = LoggerFactory.getLogger(implzhws.class);

    @Resource
    private WebServiceContext context;

    @Autowired
    private QueryService qDeal;

    @Autowired
    private WriteService wDeal;

    @Override
    public String query(String QueryXmlDoc) {
        XmlHelper xmlHelper = new XmlHelper();
        XmlQueryResult xmlQueryResult = new XmlQueryResult();
        String jkid = "";
        String jkxlh = "";
        String qybh = "";
        String ip = getClientIpCxf();
        //logger.info("调用query接口，jkid:" + jkid + "接收的xmldoc:" + QueryXmlDoc);

        try {
            if (xmlHelper.loadFromStr(QueryXmlDoc)) {
                xmlHelper = DecodeHelper.decodeText("/root/head", xmlHelper);
                qybh = xmlHelper.getInnerText("/root/head", "qybh");
                jkxlh = xmlHelper.getInnerText("/root/head", "jkxlh");
                jkid = xmlHelper.getInnerText("/root/head", "jkid");
                xmlQueryResult = qDeal.dealCommand(qybh, jkxlh,jkid, QueryXmlDoc);
            } else {
                xmlQueryResult.setCodeMessage(0, "非正常xml格式包", "0","");
            }
        } catch (Exception e) {
            logger.error("异常:" + e.getMessage());
            xmlQueryResult.setCodeMessage(-1, "异常:失败", "0","");
        }
        if (xmlQueryResult.getXml().length() <= 100000) {
            if(!jkid.equals("01Q00")) {
                //logger.info("jkid:"+jkid);
                //logger.info(xmlQueryResult.getXml());
            }
        }

        return xmlQueryResult.getXml();
    }

    @Override
    public String write(String WriteXmlDoc) {
        XmlHelper xmlHelper = new XmlHelper();
        XmlWriteResult xmlWriteResult = new XmlWriteResult();
        String jkid = "";
        String qybh = "";
        String jkxlh = "";
        String ip = getClientIpCxf();
        //logger.info("客户端ip:"+ip+"调用write接口，接收的xmldoc:"
        //+ (WriteXmlDoc.length() > 200 ? WriteXmlDoc.substring(0, 200) : WriteXmlDoc));
        try {
            if (xmlHelper.loadFromStr(WriteXmlDoc)) {
                xmlHelper = DecodeHelper.decodeText("/root/head", xmlHelper);
                qybh = xmlHelper.getInnerText("/root/head", "qybh");
                jkxlh = xmlHelper.getInnerText("/root/head", "jkxlh");
                jkid = xmlHelper.getInnerText("/root/head", "jkid");
                xmlWriteResult = wDeal.dealCommand(qybh, jkxlh,jkid, WriteXmlDoc);
            } else {
                xmlWriteResult.setCodeMessage("0", "非正常xml格式包");
            }
        } catch (Exception e) {
            logger.error("异常:" + e.getMessage());
            xmlWriteResult.setCodeMessage("-1", "异常:失败");
        }
        if (xmlWriteResult.getXml().length() <= 100000) {
            //logger.info(xmlWriteResult.getXml());
        }
        return xmlWriteResult.getXml();
    }

    public String getClientIpCxf() {
        try {
            javax.xml.ws.handler.MessageContext ctx = context.getMessageContext();
            HttpServletRequest request = (HttpServletRequest) ctx.get(AbstractHTTPDestination.HTTP_REQUEST);
            String ip = request.getRemoteAddr();
            return ip;
        } catch (Exception e) {
            System.out.println("无法获取对方主机IP");
            e.printStackTrace();
            return null;
        }
    }

}

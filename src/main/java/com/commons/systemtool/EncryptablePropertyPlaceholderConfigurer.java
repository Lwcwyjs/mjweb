package com.commons.systemtool;

import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

public class EncryptablePropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {  
//    private static final String key = "0002000200020002";  
	//秘钥在工具类中
  
    protected void processProperties(ConfigurableListableBeanFactory beanFactory, Properties props)  
        throws BeansException {  
            try {  
                String username = props.getProperty("oracle1.user");  
                if (username != null) {  
                    props.setProperty("oracle1.user", SymmetricEncoder.AESDncode("", username));  
                }  
                  
                String password = props.getProperty("oracle1.pwd");  
                if (password != null) {  
                    props.setProperty("oracle1.pwd", SymmetricEncoder.AESDncode("", password));  
                }  
                  
                String url = props.getProperty("oracle1.url");  
                if (url != null) {  
                    props.setProperty("oracle1.url", SymmetricEncoder.AESDncode("", url));  
                }  
                  
                String driverClassName = props.getProperty("oracle1.driver");  
                if(driverClassName != null){  
                    props.setProperty("oracle1.driver", SymmetricEncoder.AESDncode("", driverClassName));  
                }  
                  
                super.processProperties(beanFactory, props);  
            } catch (Exception e) {  
                e.printStackTrace();  
                throw new BeanInitializationException(e.getMessage());  
            }  
        }
}
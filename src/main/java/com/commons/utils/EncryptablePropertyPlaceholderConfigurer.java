package com.commons.utils;

import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

public class EncryptablePropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {
    //秘钥在工具类中

    protected void processProperties(ConfigurableListableBeanFactory beanFactory, Properties props)
            throws BeansException {
        try {
            String master_username = props.getProperty("master_username");
            if (master_username != null) {
                props.setProperty("master_username", master_username);
            }

            String master_password = props.getProperty("master_password");
            if (master_password != null) {
                props.setProperty("master_password", master_password);
            }

            String master_driverUrl = props.getProperty("master_driverUrl");
            if (master_driverUrl != null) {
                props.setProperty("master_driverUrl", master_driverUrl);
            }

            String slave_username = props.getProperty("slave_username");
            if (slave_username != null) {
                props.setProperty("slave_username",slave_username);
            }

            String slave_password = props.getProperty("slave_password");
            if (slave_password != null) {
                props.setProperty("slave_password",  slave_password);
            }

            String slave_driverUrl = props.getProperty("slave_driverUrl");
            if (slave_driverUrl != null) {
                props.setProperty("slave_driverUrl", slave_driverUrl);
            }

              /*  String driverClassName = props.getProperty("oracle1.driver");
                if(driverClassName != null){
                    props.setProperty("oracle1.driver", SymmetricEncoder.AESDncode("", driverClassName));
                } */

            super.processProperties(beanFactory, props);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BeanInitializationException(e.getMessage());
        }
    }
}
package com.dd.elk.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;

@Slf4j
@Configuration
public class DbConnectionConfig {

    @Value("${datasource.url}")
    String mysqlUrl;

    @Value("${datasource.driver-class-name}")
    String driverClassName;

    @Value("${datasource.username}")
    String userName;

    @Value("${datasource.password}")
    String passWord;

    @Bean("conn")
    public Connection getConnection(){
        Connection conn=null;
        try {
            Class.forName(driverClassName).newInstance();
            conn = DriverManager.getConnection(mysqlUrl + "user="+userName+"&password="+passWord);
        } catch (Exception e) {
            log.error("DbConnectionConfig.getConnection : ",e);
        }
        return conn;
    }
}

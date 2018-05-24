package com.mofan.xhs;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@MapperScan("com.mofan.xhs.repository")
@SpringBootApplication
public class XhsApplication {

    public static void main(String[] args) {
        SpringApplication.run(XhsApplication.class, args);
    }

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}

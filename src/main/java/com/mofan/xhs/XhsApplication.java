package com.mofan.xhs;

import com.mofan.xhs.util.SpringContextUtils;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@MapperScan("com.mofan.xhs.repository")
@SpringBootApplication
public class XhsApplication implements BeanFactoryAware {

    public static void main(String[] args) {
        SpringApplication.run(XhsApplication.class, args);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        SpringContextUtils.setBeanFactory(beanFactory);
    }

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}

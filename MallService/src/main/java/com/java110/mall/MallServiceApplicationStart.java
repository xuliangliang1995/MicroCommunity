package com.java110.mall;

import com.java110.core.annotation.Java110ListenerDiscovery;
import com.java110.event.service.BusinessServiceDataFlowEventPublishing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(
        scanBasePackages = {"com.java110.service", "com.java110.mall", "com.java110.core", "com.java110.cache","com.java110.config.properties.code", "com.java110.db"},
        excludeName = {"com.java110.core.smo.mall"}
)
@EnableDiscoveryClient
@Java110ListenerDiscovery(listenerPublishClass = BusinessServiceDataFlowEventPublishing.class,
        basePackages = {"com.java110.mall.listener"})
@EnableFeignClients(basePackages = {"com.java110.core.smo.community"})
public class MallServiceApplicationStart {

    public static void main(String[] args) {
        SpringApplication.run(MallServiceApplicationStart.class, args);
    }

}

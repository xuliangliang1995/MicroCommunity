package com.java110.mall.kafka;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author xuliangliang
 * @Classname MallServiceBean
 * @Description TODO
 * @Date 2020/3/23 21:19
 * @blame Java Team
 */
@Configuration
public class MallServiceBean {

    @Bean
    public MallServiceKafka listener() {
        return new MallServiceKafka();
    }

}

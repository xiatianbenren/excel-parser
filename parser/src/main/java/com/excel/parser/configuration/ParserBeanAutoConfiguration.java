package com.excel.parser.configuration;

import com.excel.parser.support.GlobalMetadata;
import com.excel.parser.support.MetaCollector;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ParserBeanAutoConfiguration {

    @Bean
    public MetaCollector metaCollector(){
        return new MetaCollector();
    }

    @Bean
    @ConditionalOnBean(MetaCollector.class)
    public GlobalMetadata entityExcelMapper(){
        return new GlobalMetadata();
    }
}

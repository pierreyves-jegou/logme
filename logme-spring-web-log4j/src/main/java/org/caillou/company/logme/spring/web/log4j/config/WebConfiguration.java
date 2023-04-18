package org.caillou.company.logme.spring.web.log4j.config;

import org.caillou.company.logme.spring.web.log4j.requestScope.RequestTrace;
import org.caillou.company.logme.spring.web.log4j.requestScope.RequestUUID;
import org.caillou.company.logme.spring.web.log4j.util.WebLevelExtractorService;
import org.caillou.company.util.LevelExtractorService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.context.annotation.RequestScope;

@Configuration
@ComponentScan(basePackages = "org.caillou.company.logme.spring.web.log4j")
public class WebConfiguration {

    @Bean
    @RequestScope
    public RequestUUID requestScopedUUID() {
        return new RequestUUID();
    }


    @Bean
    @RequestScope
    public RequestTrace requestTrace() { return new RequestTrace(); }


}

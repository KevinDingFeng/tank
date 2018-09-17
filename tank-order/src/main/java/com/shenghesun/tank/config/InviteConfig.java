package com.shenghesun.tank.config;

import javax.servlet.Filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.shenghesun.tank.filter.CheckTokenFilter;
import com.shenghesun.tank.filter.CrossFilter;

@Configuration
public class InviteConfig {

	@Bean
    public Filter crossFilter() {
    	System.out.println("----------------cross filter-----------------");
    	return new CrossFilter();
    }
	
	@Bean
    public Filter kevinTokenFilter() {
    	System.out.println("----------------check token filter-----------------");
    	return new CheckTokenFilter();
    }
 
    @Bean
    public FilterRegistrationBean tokenFilter() {
    	FilterRegistrationBean registration = new FilterRegistrationBean();
    	registration.setFilter(kevinTokenFilter());
    	registration.addUrlPatterns(CheckTokenFilter.URL_PATTERNS);
//    	registration.addInitParameter("", "");
    	registration.setName("checkTokenFilter");
    	
    	return registration;
    }
    
}

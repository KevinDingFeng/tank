package com.shenghesun.tank.config;

import javax.servlet.Filter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.shenghesun.tank.filter.CrossFilter;

@Configuration
public class InviteConfig {

	@Bean
    public Filter crossFilter() {
    	System.out.println("----------------cross filter-----------------");
    	return new CrossFilter();
    }
	
 }

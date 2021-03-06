package com.softart;

import java.nio.charset.Charset;

import javax.servlet.Filter;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.softart.repository.WordRepository;
import com.softart.service.XMLParser;

@SpringBootApplication
public class NOWServer {
	
	private static XMLParser parser;
	
	public static void main(String[] args) {
		SpringApplication.run(NOWServer.class, args);
		
		Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
            	while(true) {
	                try {
	            		parser.parseData();
	            		Thread.sleep(1000 * 30);
	                } catch (Exception e) {
	                    e.printStackTrace();
	                }
            	}
            }
        });
		t.start();
		
	}
	
	@Bean
	public CommandLineRunner getXMLParser(XMLParser xmlParser) {
		return (args) -> {
			this.parser = xmlParser;
		};
	}
	
	@Bean
    public HttpMessageConverter<String> responseBodyConverter() {
        return new StringHttpMessageConverter(Charset.forName("UTF-8"));
    }
 
    @Bean
    public Filter characterEncodingFilter() {
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        characterEncodingFilter.setForceEncoding(true);
        return characterEncodingFilter;
    }

}
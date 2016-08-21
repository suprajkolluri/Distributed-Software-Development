package edu.asu.se.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

/**
 * 
 * This is the default class that will load all the spring related
 * configurations.
 *
 */
@EnableWebMvc
@Configuration
@ComponentScan({ "edu.asu.se.*" })
@Import({ SecurityConfig.class, MongoDBConfig.class })
public class ApplicationConfig {

	/**
	 * This method will resolve the resource path to the views.
	 * 
	 * @return the InternalResourceViewResolver object
	 */
	@Bean
	public InternalResourceViewResolver getViewResolver() {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setPrefix("/WEB-INF/pages/");
		viewResolver.setSuffix(".jsp");
		return viewResolver;
	}

}

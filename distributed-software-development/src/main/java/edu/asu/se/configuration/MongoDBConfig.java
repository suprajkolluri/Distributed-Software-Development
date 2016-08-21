package edu.asu.se.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

import com.mongodb.MongoClient;

/**
 * 
 * This class is used to establish a connection to the mongoDB database.
 *
 */
@Configuration
@PropertySource("classpath:mongo-${env}.properties")
public class MongoDBConfig {

	@Autowired
    Environment env;
	

	@Bean
	public MongoDbFactory getMongoDbFactory() throws Exception {
		return new SimpleMongoDbFactory(
				new MongoClient(env.getProperty("host"), env.getProperty("port", Integer.class)),
				env.getProperty("dbName"));
	}

	@Bean
	public MongoTemplate getMongoTemplate() throws Exception {
		MongoTemplate mongoTemplate = new MongoTemplate(getMongoDbFactory());
		return mongoTemplate;
	}
}

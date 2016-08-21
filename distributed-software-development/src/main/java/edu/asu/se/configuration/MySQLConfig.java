package edu.asu.se.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

/*@Configuration*/
@PropertySource("classpath:mysql-${env}.properties")
public class MySQLConfig {

	@Value("${driverClassName}")
	String driverClassName;
	
	@Value("${dbName}")
	String dbName;
	
	@Value("${port}")
	String port;
	
	@Value("${host}")
	String host;
	
	@Value("${db}")
	String db;
	
	@Value("${userName}")
	String userName;
	
	@Value("${password}")
	String password;

	@Bean(name = "dataSource")
	public DriverManagerDataSource dataSource() {
		DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
		driverManagerDataSource.setDriverClassName(driverClassName);
		driverManagerDataSource.setUrl("jdbc:" + db + "://" + host + ":" + port + "/" + dbName);
		driverManagerDataSource.setUsername(userName);
		driverManagerDataSource.setPassword(password);
		return driverManagerDataSource;
	}
}

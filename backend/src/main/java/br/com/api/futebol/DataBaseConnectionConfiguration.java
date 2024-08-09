package br.com.api.futebol;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataBaseConnectionConfiguration {
	
	@Bean
	@ConfigurationProperties(prefix = "spring.datasource")
	public DataSource dataSource() {
		return DataSourceBuilder.create().build();
	}
	
	 @Bean
	    public Connection connection(DataSource dataSource) throws SQLException {
	        return dataSource.getConnection();
	    }

	public static void main(String[] args) throws SQLException {

		Connection connection = DriverManager.getConnection(
				"jdbc:mysql://localhost/football_teams?useTimezone=true&serverTimezone=UTC", "root", "root");

		connection.close();
	}
	
}

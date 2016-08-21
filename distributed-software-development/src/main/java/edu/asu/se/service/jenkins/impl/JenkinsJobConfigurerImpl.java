package edu.asu.se.service.jenkins.impl;

import java.net.HttpURLConnection;
import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.se.service.jenkins.GroovyScriptGenerator;
import edu.asu.se.service.jenkins.JenkinsJobConfigurer;

@Service
public class JenkinsJobConfigurerImpl implements JenkinsJobConfigurer {

	@Autowired
	GroovyScriptGenerator scriptGenerator;
	
	private final String USER_AGENT = "Mozilla/5.0";

	@Override
	public Boolean setupJob(String projectName, String rootPOMLoc) {
		// TODO replace booleans with user defined exceptions
		Boolean result = false;

		Boolean scriptCreation = scriptGenerator.generateScipt(projectName, rootPOMLoc);

		if (scriptCreation != Boolean.TRUE) {
			result = scriptCreation;
		} else {
			result = buildDSLJob();
		}

		return result;
	}

	private Boolean buildDSLJob() {
		// TODO HTTP GET MEthod to build jenkins job
		try {
		String url = "http://localhost:8080/jenkins/job/DSL%20Git%20projects%20builder/build";
		
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

		//add request header
		con.setRequestProperty("User-Agent", USER_AGENT);

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);
		
		}
		catch (Exception e)
		{
			
		}
		
		return true;
	}

}

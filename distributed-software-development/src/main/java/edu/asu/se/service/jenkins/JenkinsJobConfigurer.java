package edu.asu.se.service.jenkins;

public interface JenkinsJobConfigurer {

	public Boolean setupJob(String projectName, String rootPOMLoc);
}

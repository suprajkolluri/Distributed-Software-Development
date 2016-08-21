package edu.asu.se.dao;

import java.util.List;

import edu.asu.se.model.GitProjectDetails;
import edu.asu.se.model.UserActivityDetails;

public interface UserActivityDAO {

	public List<UserActivityDetails> getUserActivityDetails(GitProjectDetails details);
	
	public void insertUserActivityDetails(GitProjectDetails projDetails, UserActivityDetails userDetail);
}

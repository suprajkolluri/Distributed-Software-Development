package edu.asu.se.dao;

import java.util.List;

import edu.asu.se.model.GitProjectDetails;

public interface ProjectDetailsDAO {

	public List<GitProjectDetails> getProjectDetails(String userName);
	
	public void insertProjectDetails(String userName, GitProjectDetails gitProjectDetails);
	
	public GitProjectDetails getBranchDetails(GitProjectDetails details);
}

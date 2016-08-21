package edu.asu.se.dao;

import java.util.List;

import edu.asu.se.model.FileActivityDetails;
import edu.asu.se.model.GitProjectDetails;

public interface FileActivityDAO {

	public List<FileActivityDetails> getFileActivityDetails(GitProjectDetails details);
	
	public void insertFileActivityDetails(GitProjectDetails projDetails, FileActivityDetails fileDetail);
}

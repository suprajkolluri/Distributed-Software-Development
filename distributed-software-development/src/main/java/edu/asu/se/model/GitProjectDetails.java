package edu.asu.se.model;

import java.util.List;

import org.springframework.data.annotation.Id;

public class GitProjectDetails {

	@Id
	private String id;
	private String projectName;
	private String projectUrl;
	private String rootPOMLoc;
	private List<BranchDetails> branchDetails;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public List<BranchDetails> getBranchDetails() {
		return branchDetails;
	}

	public void setBranchDetails(List<BranchDetails> branchDetails) {
		this.branchDetails = branchDetails;
	}

	public String getProjectUrl() {
		return projectUrl;
	}

	public void setProjectUrl(String projectUrl) {
		this.projectUrl = projectUrl;
	}

	public String getRootPOMLoc() {
		return rootPOMLoc;
	}

	public void setRootPOMLoc(String rootPOMLoc) {
		this.rootPOMLoc = rootPOMLoc;
	}

}

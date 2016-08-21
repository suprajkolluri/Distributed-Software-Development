package edu.asu.se.model;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;

public class FileActivityDetails {
	@Id
	private String id;
	private String projName;
	private String branchName;
	private String fileName;
	private String filePath;
	private Date createdDate;
	private String lastCommittedBy;
	private int commits;
	private int locSinceLastCommit;
	private List<String> usersActivityDetailsId;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public int getLocSinceLastCommit() {
		return locSinceLastCommit;
	}

	public void setLocSinceLastCommit(int locSinceLastCommit) {
		this.locSinceLastCommit = locSinceLastCommit;
	}

	public String getLastCommittedBy() {
		return lastCommittedBy;
	}

	public void setLastCommittedBy(String lastCommittedBy) {
		this.lastCommittedBy = lastCommittedBy;
	}

	public int getCommits() {
		return commits;
	}

	public void setCommits(int commits) {
		this.commits = commits;
	}

	public List<String> getUsersActivityDetailsId() {
		return usersActivityDetailsId;
	}

	public void setUsersActivityDetailsId(List<String> usersActivityDetailsId) {
		this.usersActivityDetailsId = usersActivityDetailsId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProjName() {
		return projName;
	}

	public void setProjName(String projName) {
		this.projName = projName;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
}

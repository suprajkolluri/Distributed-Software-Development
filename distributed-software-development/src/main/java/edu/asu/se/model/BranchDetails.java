package edu.asu.se.model;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;

public class BranchDetails {
	@Id
	private String id;
	private String branchName;
	private Date createdDate;
	private List<String> applUsers;
	private String lastAppUser;
	private List<String> fileActivityDetailsId;
	private List<String> userActivityDetailsId;
	private List<String> codeStatisticsId;

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<String> getFileActivityDetailsId() {
		return fileActivityDetailsId;
	}

	public void setFileActivityDetailsId(List<String> fileActivityDetailsId) {
		this.fileActivityDetailsId = fileActivityDetailsId;
	}

	public List<String> getCodeStatisticsId() {
		return codeStatisticsId;
	}

	public void setCodeStatisticsId(List<String> codeStatisticsId) {
		this.codeStatisticsId = codeStatisticsId;
	}

	public List<String> getUserActivityDetailsId() {
		return userActivityDetailsId;
	}

	public void setUserActivityDetailsId(List<String> userActivityDetailsId) {
		this.userActivityDetailsId = userActivityDetailsId;
	}

	public List<String> getApplUsers() {
		return applUsers;
	}

	public void setApplUsers(List<String> applUsers) {
		this.applUsers = applUsers;
	}

	public String getLastAppUser() {
		return lastAppUser;
	}

	public void setLastAppUser(String lastAppUser) {
		this.lastAppUser = lastAppUser;
	}


}

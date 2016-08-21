package edu.asu.se.model;

import java.util.Date;

import org.springframework.data.annotation.Id;

public class UserActivityDetails {

	@Id
	private String id;
	private String projName;
	private String branchName;
	private String userName;
	private int onlineTime;
	private Date startDate;
	private int commitsMade;
	private int loc;
	private int buildbreaks;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getOnlineTime() {
		return onlineTime;
	}

	public void setOnlineTime(int onlineTime) {
		this.onlineTime = onlineTime;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public int getCommitsMade() {
		return commitsMade;
	}

	public void setCommitsMade(int commitsMade) {
		this.commitsMade = commitsMade;
	}

	public int getLoc() {
		return loc;
	}

	public void setLoc(int loc) {
		this.loc = loc;
	}

	public int getBuildbreaks() {
		return buildbreaks;
	}

	public void setBuildbreaks(int buildbreaks) {
		this.buildbreaks = buildbreaks;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
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

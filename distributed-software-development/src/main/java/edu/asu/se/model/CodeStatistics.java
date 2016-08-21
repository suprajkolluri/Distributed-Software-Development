package edu.asu.se.model;

import org.springframework.data.annotation.Id;

public class CodeStatistics {
	@Id
	private String id;
	private String projName;
	private String branchName;
	private int buildNumber;
	private String buildDate;
	private String triggeredBy;
	private String timeTaken;
	private int compilationErrors;
	private String buildStatus;

	public String getBuildDate() {
		return buildDate;
	}

	public void setBuildDate(String buildDate) {
		this.buildDate = buildDate;
	}

	public String getTriggeredBy() {
		return triggeredBy;
	}

	public void setTriggeredBy(String triggeredBy) {
		this.triggeredBy = triggeredBy;
	}

	public int getCompilationErrors() {
		return compilationErrors;
	}

	public void setCompilationErrors(int compilationErrors) {
		this.compilationErrors = compilationErrors;
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

	public String getBuildStatus() {
		return buildStatus;
	}

	public void setBuildStatus(String buildStatus) {
		this.buildStatus = buildStatus;
	}

	public String getTimeTaken() {
		return timeTaken;
	}

	public void setTimeTaken(String timeTaken) {
		this.timeTaken = timeTaken;
	}

	public int getBuildNumber() {
		return buildNumber;
	}

	public void setBuildNumber(int buildNumber) {
		this.buildNumber = buildNumber;
	}

}

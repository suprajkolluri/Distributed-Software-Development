package edu.asu.se.git.stats.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand.ListMode;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import edu.asu.se.dao.CodeStatsDAO;
import edu.asu.se.dao.FileActivityDAO;
import edu.asu.se.dao.UserActivityDAO;
import edu.asu.se.git.stats.GitJobs;
import edu.asu.se.model.BranchDetails;
import edu.asu.se.model.CodeStatistics;
import edu.asu.se.model.FileActivityDetails;
import edu.asu.se.model.GitProjectDetails;
import edu.asu.se.model.UserActivityDetails;

@Service
@PropertySource("classpath:application-${env}.properties")
public class GitStatAnalyzer implements GitJobs {

	@Autowired
	Environment env;

	@Autowired
	FileActivityDAO fileActivityDAO;

	@Autowired
	UserActivityDAO userActivityDAO;
	
	@Autowired
	CodeStatsDAO codeStatsDAO;

	@Override
	public void gitResult(String projectName, String branchName) {
		String gitFilePath = env.getProperty("DSLJobWSPath") + "/workspace/" + projectName.replaceAll("/", "-") + "-"
				+ branchName + "/.git";
		String buildLogFilePath = env.getProperty("DSLJobWSPath") + "/jobs/" + projectName.replaceAll("/", "-") + "-"
				+ branchName;
		FileRepositoryBuilder builder = new FileRepositoryBuilder();
		try {
			Repository repository = builder.setGitDir(new File(gitFilePath)).readEnvironment().findGitDir().build();
			listRepositoryContents(repository, projectName, branchName);
			repository.close();
			getBuildLogContents(buildLogFilePath, projectName, branchName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void getBuildLogContents(String buildLogFilePath, String projectName, String branchName)
			throws IOException {
		BufferedReader buildNumberData = null;
		BufferedReader logData = null;
		BufferedReader changeLogData = null;
		BufferedReader pollingLogData = null;

		String sCurrentLine;
		String bulidNumberFile = buildLogFilePath + "/nextBuildNumber";
		buildNumberData = new BufferedReader(new FileReader(bulidNumberFile));
		
		if ((sCurrentLine = buildNumberData.readLine()) != null) {
			CodeStatistics codeStatistics = new CodeStatistics();
			codeStatistics.setProjName(projectName);
			codeStatistics.setBranchName(branchName);
			int buildFolderName = Integer.parseInt(sCurrentLine) - 1;
			codeStatistics.setBuildNumber(buildFolderName);
			String logFile = buildLogFilePath + "/builds/" + buildFolderName + "/log";
			logData = new BufferedReader(new FileReader(logFile));
			while ((sCurrentLine = logData.readLine()) != null) {
				if (sCurrentLine.contains("[INFO] Total time:")) {
					codeStatistics.setTimeTaken(sCurrentLine.substring((sCurrentLine.indexOf(":") + 2)));
				}
				if (sCurrentLine.contains("[INFO]") && sCurrentLine.contains("errors")) {
					String compliationError = sCurrentLine.substring(sCurrentLine.indexOf("errors") - 2,
							sCurrentLine.indexOf("errors") - 1);
					compliationError = compliationError != null ? compliationError : "0";
					codeStatistics.setCompilationErrors(Integer.parseInt(compliationError));
				}
				if (sCurrentLine.contains("Finished:")) {
					codeStatistics.setBuildStatus(sCurrentLine.substring(sCurrentLine.indexOf(" ") + 1));
				}
			}

			String changeLogFile = buildLogFilePath + "/builds/" + buildFolderName + "/changelog.xml";
			changeLogData = new BufferedReader(new FileReader(changeLogFile));
			while ((sCurrentLine = changeLogData.readLine()) != null) {
				if (sCurrentLine.contains("committer")) {
					int startIndex = 10;
					int endIndex = sCurrentLine.indexOf("<") - 1;
					codeStatistics.setTriggeredBy(sCurrentLine.substring(startIndex, endIndex));
				}
			}

			String pollingLogFile = buildLogFilePath + "/builds/" + buildFolderName + "/polling.log";
			pollingLogData = new BufferedReader(new FileReader(pollingLogFile));
			while ((sCurrentLine = pollingLogData.readLine()) != null) {
				if (sCurrentLine.contains("Started on")) {
					int startIndex = 11;
					codeStatistics.setBuildDate(sCurrentLine.substring(startIndex));
				}
				if (sCurrentLine.contains("Done. Took")) {
					int startIndex = 11;
					if (codeStatistics.getTimeTaken() == null)
						codeStatistics.setTimeTaken(sCurrentLine.substring(startIndex));
				}
			}
			if(codeStatistics.getTriggeredBy() == null)
			{
				codeStatistics.setTriggeredBy("N/A");
			}
			GitProjectDetails projectDetail = createProjectDetails(projectName, branchName);
			codeStatsDAO.insertBuildActivityDetails(projectDetail, codeStatistics);
			
		}

		if (buildNumberData != null)
			buildNumberData.close();
		if (logData != null)
			logData.close();
		if (changeLogData != null)
			changeLogData.close();
		if (pollingLogData != null)
			pollingLogData.close();
	}

	private void listRepositoryContents(Repository repository, String projectName, String branchName)
			throws IOException, GitAPIException {

		@SuppressWarnings("resource")
		Git git = new Git(repository);
		int count = 0;
		List<Ref> call = git.branchList().setListMode(ListMode.ALL).call();
		GitProjectDetails projectDetail = null;
		for (Ref ref : call) {
			Ref head = repository.getRef(ref.getName());
			@SuppressWarnings("resource")
			RevWalk walk = new RevWalk(repository);

			RevCommit commit = walk.parseCommit(head.getObjectId());
			RevTree tree = commit.getTree();

			String fullPath = ref.getName();

			int index = fullPath.lastIndexOf("/");
			String currBranch = fullPath.substring(index + 1);

			if (currBranch.equals(branchName)) {				
				projectDetail = createProjectDetails(projectName, currBranch);
				System.out.println("Having tree: " + tree + currBranch);

				BranchDetails branch = new BranchDetails();
				branch.setBranchName(currBranch);
				HashMap<String, Integer> hm = new HashMap<String, Integer>();
				HashMap<String, Date> hstart = new HashMap<String, Date>();
				@SuppressWarnings("resource")
				TreeWalk treeWalk = new TreeWalk(repository);
				treeWalk.addTree(tree);
				treeWalk.setRecursive(true);
				while (treeWalk.next()) {
					Iterable<RevCommit> logs = git.log().call();
					logs = git.log().addPath(treeWalk.getPathString()).call();
					count = 0;

					Date createdDate = null;
					String name = "0";
					for (RevCommit rev : logs) {
						createdDate = rev.getAuthorIdent().getWhen();
						name = rev.getAuthorIdent().getName();
						if (hm.containsKey(rev.getAuthorIdent().getName())) {
							hm.put(rev.getAuthorIdent().getName(), hm.get(rev.getAuthorIdent().getName()) + 1);
						} else {
							hm.put(rev.getAuthorIdent().getName(), 1);
							hstart.put(rev.getAuthorIdent().getName(), rev.getAuthorIdent().getWhen());
						}
						count++;
					}

					String file = treeWalk.getPathString();
					int ind = file.lastIndexOf("/");
					String fileName = file.substring(ind + 1);
					String filePath = file.substring(0, ind + 1);

					FileActivityDetails fileDetail = new FileActivityDetails();
					fileDetail.setProjName(projectName);
					fileDetail.setBranchName(branchName);
					fileDetail.setFileName(fileName);
					fileDetail.setFilePath(filePath);
					fileDetail.setCreatedDate(createdDate);
					fileDetail.setCommits(count);
					fileDetail.setLastCommittedBy(name);
					fileActivityDAO.insertFileActivityDetails(projectDetail, fileDetail);

				}

				for (Entry<String, Integer> each : hm.entrySet()) {
					UserActivityDetails userDetail = new UserActivityDetails();
					userDetail.setProjName(projectName);
					userDetail.setBranchName(branchName);
					userDetail.setUserName(each.getKey());
					userDetail.setCommitsMade(each.getValue());
					userDetail.setStartDate(hstart.get(each.getKey()));
					userActivityDAO.insertUserActivityDetails(projectDetail, userDetail);
				}

				hm.clear();
				break;
			}
		}
	}
	
	private GitProjectDetails createProjectDetails(String projectName, String branchName)
	{
		GitProjectDetails gitProjectDetails = new GitProjectDetails();

		gitProjectDetails.setProjectName(projectName);
		List<BranchDetails> branches = new ArrayList<BranchDetails>();
		BranchDetails branchDetail = new BranchDetails();
		branchDetail.setBranchName(branchName);
		branches.add(branchDetail);
		gitProjectDetails.setBranchDetails(branches);
		
		return gitProjectDetails;
	}

}

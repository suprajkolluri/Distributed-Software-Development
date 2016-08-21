package edu.asu.se.dao.mongodb.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import edu.asu.se.dao.ProjectDetailsDAO;
import edu.asu.se.dao.UserDAO;
import edu.asu.se.model.BranchDetails;
import edu.asu.se.model.GitProjectDetails;
import edu.asu.se.model.User;

@Repository
public class ProjectDetailsDAOMongoImpl implements ProjectDetailsDAO {

	@Autowired
	MongoTemplate mongoTemplate;

	@Autowired
	UserDAO userDAO;

	private static final String USER_COLLECTION = "users";

	private static final String PROJECTDETAILS_COLLECTION = "projectDetails";

	@Override
	public List<GitProjectDetails> getProjectDetails(String userName) {
		User user = userDAO.findUserByName(userName);
		if (user.getProjects() != null) {
			Query query = new Query(Criteria.where("_id").in(user.getProjects()));
			return mongoTemplate.find(query, GitProjectDetails.class, "projectDetails");
		}
		return new ArrayList<GitProjectDetails>();
	}

	@Override
	public void insertProjectDetails(String userName, GitProjectDetails gitProjectDetails) {
		BranchDetails inputBranch = gitProjectDetails.getBranchDetails().get(0);
		if (gitProjectDetails.getProjectName() != "" && gitProjectDetails.getProjectUrl() != ""
				&& inputBranch.getBranchName() != "") {
			User user = userDAO.findUserByName(userName);
			GitProjectDetails availableProject = null;
			Query query = null;
			String projectId = "";
			query = new Query(Criteria.where("projectUrl").is(gitProjectDetails.getProjectUrl()));
			availableProject = mongoTemplate.findOne(query, GitProjectDetails.class, "projectDetails");

			// check if project is available and also if branch is available
			if (availableProject == null) {
				mongoTemplate.insert(gitProjectDetails, PROJECTDETAILS_COLLECTION);
				projectId = gitProjectDetails.getId();

			} else {
				gitProjectDetails.setId(availableProject.getId());
				if (getBranchDetails(gitProjectDetails).getBranchDetails() == null) {
					availableProject.getBranchDetails().add(inputBranch);
					mongoTemplate.save(availableProject, "projectDetails");
				} else {
					for (BranchDetails branchDetail : availableProject.getBranchDetails()) {
						if (branchDetail.getBranchName().equals(inputBranch.getBranchName())) {
							if (branchDetail.getApplUsers() != null) {								
								branchDetail.getApplUsers().add(inputBranch.getLastAppUser());
								break;
							}
							else{
								List<String> applUsers = new ArrayList<String>();
								applUsers.add(inputBranch.getLastAppUser());
								branchDetail.setApplUsers(applUsers);
								break;
							}							
						}
					}
					mongoTemplate.save(availableProject, "projectDetails");					
				}
				projectId = availableProject.getId();
			}

			// check if user is associated to that project
			if (user.getProjects() != null) {
				if (!user.getProjects().contains(projectId)) {
					user.getProjects().add(projectId);
					mongoTemplate.save(user, USER_COLLECTION);
				}
			} else {
				List<String> projects = new ArrayList<String>();
				projects.add(projectId);
				user.setProjects(projects);
				mongoTemplate.save(user, USER_COLLECTION);
			}
		}
	}

	@Override
	public GitProjectDetails getBranchDetails(GitProjectDetails details) {
		BasicDBObject idCriteria = new BasicDBObject();
		idCriteria.put("_id", details.getId());
		BasicDBObject branchNameCriteria = new BasicDBObject();
		branchNameCriteria.put("branchName", details.getBranchDetails().get(0).getBranchName());
		BasicDBObject eleMatch = new BasicDBObject();
		eleMatch.put("$elemMatch", branchNameCriteria);
		idCriteria.append("branchDetails", eleMatch);

		BasicDBObject query = new BasicDBObject();
		query.put("branchDetails.branchName", details.getBranchDetails().get(0).getBranchName());

		DBObject result = mongoTemplate.getCollection(PROJECTDETAILS_COLLECTION).findOne(query, idCriteria);
		if (result != null) {
			return mongoTemplate.getConverter().read(GitProjectDetails.class, result);
		} else
			return new GitProjectDetails();

	}
}

package edu.asu.se.dao.mongodb.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import edu.asu.se.dao.ProjectDetailsDAO;
import edu.asu.se.dao.UserActivityDAO;
import edu.asu.se.model.BranchDetails;
import edu.asu.se.model.GitProjectDetails;
import edu.asu.se.model.UserActivityDetails;

@Repository
public class UserActivityDAOMongoImpl implements UserActivityDAO {

	@Autowired
	MongoTemplate mongoTemplate;

	@Autowired
	ProjectDetailsDAO projectDetailsDAO;

	private static final String USERACTIVITY_COLLECTION = "UserActivityDetails";

	private static final String PROJECTDETAILS_COLLECTION = "projectDetails";

	@Override
	public List<UserActivityDetails> getUserActivityDetails(GitProjectDetails details) {
		GitProjectDetails branchDetails = projectDetailsDAO.getBranchDetails(details);
		List<String> userIds = branchDetails.getBranchDetails().get(0).getUserActivityDetailsId();
		if (userIds != null) {
			Query query = new Query(Criteria.where("userName").in(userIds).and("projName").is(details.getProjectName())
					.and("branchName").is(details.getBranchDetails().get(0).getBranchName()));
			return mongoTemplate.find(query, UserActivityDetails.class, USERACTIVITY_COLLECTION);
		} else {
			return new ArrayList<UserActivityDetails>();
		}
	}

	@Override
	public void insertUserActivityDetails(GitProjectDetails projDetails, UserActivityDetails userDetail) {
		Query query = new Query(Criteria.where("userName").is(userDetail.getUserName()).and("projName")
				.is(userDetail.getProjName()).and("branchName").is(userDetail.getBranchName()));
		UserActivityDetails availableUser = mongoTemplate.findOne(query, UserActivityDetails.class,
				USERACTIVITY_COLLECTION);
		if (availableUser != null) {
			userDetail.setId(availableUser.getId());
		}

		mongoTemplate.save(userDetail, USERACTIVITY_COLLECTION);

		String userId = userDetail.getUserName();

		query = new Query(Criteria.where("projectName").is(projDetails.getProjectName()));
		String branchName = projDetails.getBranchDetails().get(0).getBranchName();
		projDetails = mongoTemplate.findOne(query, GitProjectDetails.class, PROJECTDETAILS_COLLECTION);
		for (BranchDetails branchDetail : projDetails.getBranchDetails()) {
			if (branchDetail.getBranchName().equals(branchName)) {
				if (branchDetail.getUserActivityDetailsId() == null) {
					List<String> usernames = new ArrayList<String>();
					usernames.add(userId);
					branchDetail.setUserActivityDetailsId(usernames);
				} else if (!branchDetail.getUserActivityDetailsId().contains(userId)) {
					branchDetail.getUserActivityDetailsId().add(userId);
				}
			}
		}
		mongoTemplate.save(projDetails, PROJECTDETAILS_COLLECTION);
	}

}

package edu.asu.se.dao.mongodb.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import edu.asu.se.dao.CodeStatsDAO;
import edu.asu.se.dao.ProjectDetailsDAO;
import edu.asu.se.model.BranchDetails;
import edu.asu.se.model.CodeStatistics;
import edu.asu.se.model.GitProjectDetails;

@Repository
public class CodeStatsDAOMongoImpl implements CodeStatsDAO {

	@Autowired
	MongoTemplate mongoTemplate;

	@Autowired
	ProjectDetailsDAO projectDetailsDAO;

	private static final String CODESTATISTICS_COLLECTION = "CodeStatistics";

	private static final String PROJECTDETAILS_COLLECTION = "projectDetails";

	@Override
	public List<CodeStatistics> getCodeStatistics(GitProjectDetails details) {
		GitProjectDetails branchDetails = projectDetailsDAO.getBranchDetails(details);
		List<String> branchIds = branchDetails.getBranchDetails().get(0).getCodeStatisticsId();
		if (branchIds != null) {
			Query query = new Query(Criteria.where("_id").in(branchIds).and("projName").is(details.getProjectName())
					.and("branchName").is(details.getBranchDetails().get(0).getBranchName()));
			return mongoTemplate.find(query, CodeStatistics.class, CODESTATISTICS_COLLECTION);
		} else
			return new ArrayList<CodeStatistics>();
	}

	@Override
	public void insertBuildActivityDetails(GitProjectDetails projDetails, CodeStatistics buildDetail) {
		Query query = new Query(Criteria.where("buildNumber").is(buildDetail.getBuildNumber()).and("projName")
				.is(buildDetail.getProjName()).and("branchName").is(buildDetail.getBranchName()));
		CodeStatistics availableBuild = mongoTemplate.findOne(query, CodeStatistics.class, CODESTATISTICS_COLLECTION);
		if (availableBuild != null) {
			buildDetail.setId(availableBuild.getId());
		}

		mongoTemplate.save(buildDetail, CODESTATISTICS_COLLECTION);
		String buildId = buildDetail.getId();

		query = new Query(Criteria.where("projectName").is(projDetails.getProjectName()));
		String branchName = projDetails.getBranchDetails().get(0).getBranchName();
		projDetails = mongoTemplate.findOne(query, GitProjectDetails.class, PROJECTDETAILS_COLLECTION);
		for (BranchDetails branchDetail : projDetails.getBranchDetails()) {
			if (branchDetail.getBranchName().equals(branchName)) {
				if (branchDetail.getCodeStatisticsId() == null) {
					List<String> buildIds = new ArrayList<String>();
					buildIds.add(buildId);
					branchDetail.setCodeStatisticsId(buildIds);
				} else if (!branchDetail.getCodeStatisticsId().contains(buildId)) {
					branchDetail.getCodeStatisticsId().add(buildId);
				}
			}
		}
		mongoTemplate.save(projDetails, PROJECTDETAILS_COLLECTION);
	}

}

package edu.asu.se.dao.mongodb.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import edu.asu.se.dao.FileActivityDAO;
import edu.asu.se.dao.ProjectDetailsDAO;
import edu.asu.se.model.BranchDetails;
import edu.asu.se.model.FileActivityDetails;
import edu.asu.se.model.GitProjectDetails;

@Repository
public class FileActivityDAOMongoImpl implements FileActivityDAO {

	@Autowired
	MongoTemplate mongoTemplate;

	@Autowired
	ProjectDetailsDAO projectDetailsDAO;

	private static final String FILEACTIVITY_COLLECTION = "FileActivityDetails";

	private static final String PROJECTDETAILS_COLLECTION = "projectDetails";

	@Override
	public List<FileActivityDetails> getFileActivityDetails(GitProjectDetails details) {
		GitProjectDetails branchDetails = projectDetailsDAO.getBranchDetails(details);
		List<String> fileIds = branchDetails.getBranchDetails().get(0).getFileActivityDetailsId();
		if (fileIds != null) {
			Query query = new Query(Criteria.where("_id").in(fileIds).and("projName").is(details.getProjectName())
					.and("branchName").is(details.getBranchDetails().get(0).getBranchName()));
			return mongoTemplate.find(query, FileActivityDetails.class, FILEACTIVITY_COLLECTION);
		} else
			return new ArrayList<FileActivityDetails>();
	}

	@Override
	public void insertFileActivityDetails(GitProjectDetails projDetails, FileActivityDetails fileDetail) {
		String fileId = "";
		Query query = new Query(
				Criteria.where("fileName").is(fileDetail.getFileName()).and("filePath").is(fileDetail.getFilePath())
						.and("projName").is(fileDetail.getProjName()).and("branchName").is(fileDetail.getBranchName()));
		FileActivityDetails availableFile = mongoTemplate.findOne(query, FileActivityDetails.class,
				"FileActivityDetails");
		if (availableFile != null) {
			fileDetail.setId(availableFile.getId());
		}

		mongoTemplate.save(fileDetail, FILEACTIVITY_COLLECTION);

		fileId = fileDetail.getId();

		query = new Query(Criteria.where("projectName").is(projDetails.getProjectName()));
		String branchName = projDetails.getBranchDetails().get(0).getBranchName();
		projDetails = mongoTemplate.findOne(query, GitProjectDetails.class, "projectDetails");
		for (BranchDetails branchDetail : projDetails.getBranchDetails()) {
			if (branchDetail.getBranchName().equals(branchName)) {
				if (branchDetail.getFileActivityDetailsId() == null) {
					List<String> fileIds = new ArrayList<String>();
					fileIds.add(fileId);
					branchDetail.setFileActivityDetailsId(fileIds);
				} else if (!branchDetail.getFileActivityDetailsId().contains(fileId)) {
					branchDetail.getFileActivityDetailsId().add(fileId);
				}
			}
		}
		mongoTemplate.save(projDetails, PROJECTDETAILS_COLLECTION);
	}

}

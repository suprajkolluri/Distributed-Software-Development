package edu.asu.se.dao.mongodb.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import edu.asu.se.dao.UserDAO;
import edu.asu.se.model.User;

@Repository
public class UserDAOMongoImpl implements UserDAO {

	@Autowired
	MongoTemplate mongoTemplate;

	private static final String USER_COLLECTION = "users";

	@Override
	public boolean insertUserRegisterationDetails(User user) {
		user.setEnabled(true);
		user.setRole("ROLE_USER");
		User availableUser = findUserByName(user.getUsername());
		if (availableUser != null) {
			return false;
		}
		mongoTemplate.insert(user, USER_COLLECTION);
		return true;
	}

	@Override
	public User findUserByName(String userName) {
		Query query = new Query(Criteria.where("username").is(userName));
		return mongoTemplate.findOne(query, User.class, USER_COLLECTION);
	}

}

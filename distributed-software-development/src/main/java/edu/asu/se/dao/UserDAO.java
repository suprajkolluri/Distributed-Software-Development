package edu.asu.se.dao;

import edu.asu.se.model.User;

public interface UserDAO {

	public boolean insertUserRegisterationDetails(User user);

	public User findUserByName(String userName);

}

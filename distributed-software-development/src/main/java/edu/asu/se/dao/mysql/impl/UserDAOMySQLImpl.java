package edu.asu.se.dao.mysql.impl;

/*import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;

import edu.asu.se.dao.UserDAO;
import edu.asu.se.model.GitProjectDetails;
import edu.asu.se.model.User;*/

/*@Repository*/
public class UserDAOMySQLImpl {
}/*
	 * implements UserDAO {
	 * 
	 * @Autowired DataSource dataSource;
	 * 
	 * @Override public User findUserByName(String userName) { String sql =
	 * "select * from users where username=?"; Connection conn = null; try {
	 * conn = dataSource.getConnection(); PreparedStatement ps =
	 * conn.prepareStatement(sql); ps.setString(1, userName); User user = null;
	 * ResultSet rs = ps.executeQuery(); if (rs.next()) { user = new User();
	 * user.setId(rs.getString("id"));
	 * user.setUsername(rs.getString("username"));
	 * user.setPassword(rs.getString("password"));
	 * user.setEnabled(rs.getBoolean("enabled"));
	 * user.setRole(rs.getString("role")); } rs.close(); ps.close(); return
	 * user; } catch (SQLException e) { throw new RuntimeException(e); } finally
	 * { if (conn != null) { try { conn.close(); } catch (SQLException e) { } }
	 * } }
	 * 
	 * @Override public GitProjectDetails getProjectDetails(String userName) {
	 * // TODO Auto-generated method stub return null; } }
	 */
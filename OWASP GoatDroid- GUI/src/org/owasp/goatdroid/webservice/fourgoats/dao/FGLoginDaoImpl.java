/**
 * OWASP GoatDroid Project
 * 
 * This file is part of the Open Web Application Security Project (OWASP)
 * GoatDroid project. For details, please see
 * https://www.owasp.org/index.php/Projects/OWASP_GoatDroid_Project
 *
 * Copyright (c) 2012 - The OWASP Foundation
 * 
 * GoatDroid is published by OWASP under the GPLv3 license. You should read and accept the
 * LICENSE before you use, modify, and/or redistribute this software.
 * 
 * @author Jack Mannino (Jack.Mannino@owasp.org https://www.owasp.org/index.php/User:Jack_Mannino)
 * @created 2012
 */
package org.owasp.goatdroid.webservice.fourgoats.dao;

import java.sql.SQLException;
import java.util.HashMap;
import javax.sql.DataSource;
import org.owasp.goatdroid.webservice.fourgoats.LoginUtils;
import org.owasp.goatdroid.webservice.fourgoats.Salts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

@Repository
public class FGLoginDaoImpl extends BaseDaoImpl implements LoginDao {

	@Autowired
	public FGLoginDaoImpl(DataSource dataSource) {
		setDataSource(dataSource);
	}

	public boolean validateCredentials(String userName, String password)
			throws SQLException, DataAccessException {

		String sql = "SELECT username FROM app.fg_users WHERE username = ? and password = ?";
		SqlRowSet rs = getJdbcTemplate().queryForRowSet(
				sql,
				new Object[] {
						userName,
						LoginUtils.generateSaltedSHA512Hash(password,
								Salts.PASSWORD_HASH_SALT) });
		if (rs.next()) {
			return true;
		} else {
			return false;
		}
	}

	public void updateAuthInformation(String userName, String authToken) {

		String sql = "UPDATE app.fg_users SET authToken = ? WHERE userName = ?";
		getJdbcTemplate().update(sql, new Object[] { authToken, userName });
	}

	public HashMap<String, Boolean> getPreferences(String userName) {

		String sql = "SELECT autoCheckin, isPublic, isAdmin FROM app.fg_users "
				+ "WHERE userName = ?";
		SqlRowSet rs = getJdbcTemplate().queryForRowSet(sql, userName);
		HashMap<String, Boolean> preferences = new HashMap<String, Boolean>();
		rs.next();
		preferences.put("autoCheckin", rs.getBoolean("autoCheckin"));
		preferences.put("isPublic", rs.getBoolean("isPublic"));

		return preferences;
	}

	public String getAuthToken(String userName) {
		String sql = "SELECT authToken FROM app.fg_users WHERE username = ?";
		SqlRowSet rs = getJdbcTemplate().queryForRowSet(sql, userName);
		if (rs.next()) {
			if (rs.getString("authToken") != null)
				return rs.getString("authToken");
			else
				return "";
		} else {
			return "";
		}
	}
}

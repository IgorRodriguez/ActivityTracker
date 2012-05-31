package com.hersis.activitytracker.model;

import ch.qos.logback.classic.Logger;
import com.hersis.activitytracker.Activity;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Igor Rodriguez <igorrodriguezelvira@gmail.com>
 */
public class ActivityDao {
	private final Logger log = (Logger) LoggerFactory.getLogger("model.ActivityDao");
	private Dao dao;
	
	public ActivityDao() throws ClassNotFoundException, SQLException {
		dao = new Dao();
	}
	
	public ArrayList<Activity> getActivities(Connection conn) throws SQLException, ClassNotFoundException {
		final String sql = "SELECT * FROM APP.ACTIVITIES";
		ArrayList<Activity> activities = new ArrayList<>();
		
		try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
			while (rs.next()) {
				int idActivity = rs.getInt("ID_ACTIVITY");
				String name = rs.getString("NAME");
				String description = rs.getString("DESCRIPTION");
				activities.add(new Activity(idActivity, name, description));
			}
		} catch (SQLException ex) {
            log.error("Unable to read the activities from the database.\nMessage: {}\nError code: {}", 
					ex.getMessage(), ex.getErrorCode());
            throw ex;
        } 
		return activities;		
	}
}

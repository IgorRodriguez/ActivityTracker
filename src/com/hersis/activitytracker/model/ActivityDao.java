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
	
	public ActivityDao() {
	}
	
	public ArrayList<Activity> getActivities(Connection conn) throws SQLException, ClassNotFoundException {
		final String sql = "SELECT * FROM APP.ACTIVITIES ORDER BY NAME";
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
	
	public int insertActivity(Connection conn, Activity activity) throws SQLException {
		final String sql = "INSERT INTO APP.ACTIVITIES (" +
								"NAME, " +
								"DESCRIPTION) " +
							"VALUES (?, ?)";
				
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, activity.getName());
			stmt.setString(2, activity.getDescription());
			
			return stmt.executeUpdate();
		} catch (SQLException ex) {
            log.error("Unable to insert activity in the database.\nMessage: {}\nError code: {}", 
					ex.getMessage(), ex.getErrorCode());
            throw ex;
        } 
	}
	
	public int updateActivity(Connection conn, Activity oldActivity, Activity newActivity) throws SQLException {
		final String sql = "UPDATE APP.ACTIVITIES SET " +
								"NAME = ?, " +
								"DESCRIPTION = ? " +
							"WHERE NAME = ?";
				
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, newActivity.getName());
			stmt.setString(2, newActivity.getDescription());
			stmt.setString(3, oldActivity.getName());
			
			return stmt.executeUpdate();
		} catch (SQLException ex) {
            log.error("Unable to update activity in the database.\nMessage: {}\nError code: {}", 
					ex.getMessage(), ex.getErrorCode());
            throw ex;
        } 
	}
	
	public int deleteActivity(Connection conn, Activity activity) throws SQLException {
		final String sql = "DELETE FROM APP.ACTIVITIES " +
							"WHERE ID_ACTIVITY = ?";
				
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, activity.getIdActivity());
			
			return stmt.executeUpdate();
		} catch (SQLException ex) {
            log.error("Unable to delete activity from the database.\nMessage: {}\nError code: {}", 
					ex.getMessage(), ex.getErrorCode());
            throw ex;
        } 
	}
}

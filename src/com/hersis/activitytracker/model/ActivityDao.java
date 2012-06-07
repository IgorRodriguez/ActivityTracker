package com.hersis.activitytracker.model;

import ch.qos.logback.classic.Logger;
import com.hersis.activitytracker.Activity;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Observable;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Igor Rodriguez <igorrodriguezelvira@gmail.com>
 */
public class ActivityDao extends Observable {
	private static ActivityDao activityDao;
	private final Logger log = (Logger) LoggerFactory.getLogger("model.ActivityDao");
	
	private ActivityDao() {}
	
	public static ActivityDao getInstance() {
		if (activityDao == null) {
			activityDao = new ActivityDao();
		}
		return activityDao;
	}
		
	public boolean nameExists(Connection conn, String name) throws SQLException {
		final String sql = "SELECT * FROM APP.ACTIVITIES " +
							"WHERE NAME = '" + name + "'";
		boolean nameExists = false;
		
		try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
			if (rs.next()) {
				nameExists = true;
			}
		} catch (SQLException ex) {
            log.error("Unable to read the activities from the database.\nMessage: {}\nError code: {}", 
					ex.getMessage(), ex.getErrorCode());
            throw ex;
        } 
		return nameExists;		
	}
	
	public ArrayList<Activity> getActivities(Connection conn) throws SQLException {
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
		int affectedRows = -1;
		
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, activity.getName());
			stmt.setString(2, activity.getDescription());
			
			affectedRows = stmt.executeUpdate();
			// If there are changes, notify observers.
			if (affectedRows > 0) {
				setChanged();
				notifyObservers();
			}
		} catch (SQLException ex) {
            log.error("Unable to insert activity in the database.\nMessage: {}\nError code: {}", 
					ex.getMessage(), ex.getErrorCode());
            throw ex;
        } 
		
		return affectedRows;
	}
	
	public int updateActivity(Connection conn, Activity oldActivity, Activity newActivity) throws SQLException {
		final String sql = "UPDATE APP.ACTIVITIES SET " +
								"NAME = ?, " +
								"DESCRIPTION = ? " +
							"WHERE ID_ACTIVITY = ?";
		int affectedRows = -1;
				
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, newActivity.getName());
			stmt.setString(2, newActivity.getDescription());
			stmt.setInt(3, oldActivity.getIdActivity());
			
			affectedRows = stmt.executeUpdate();
			// If there are changes, notify observers.
			if (affectedRows > 0) {
				setChanged();
				notifyObservers();
			}
		} catch (SQLException ex) {
            log.error("Unable to update activity in the database.\nMessage: {}\nError code: {}", 
					ex.getMessage(), ex.getErrorCode());
            throw ex;
        } 
		return affectedRows;
	}
	
	public int deleteActivity(Connection conn, Activity activity) throws SQLException {
		final String sql = "DELETE FROM APP.ACTIVITIES " +
							"WHERE ID_ACTIVITY = ?";
		int affectedRows = -1;
				
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, activity.getIdActivity());
			
			affectedRows = stmt.executeUpdate();
			// If there are changes, notify observers.
			if (affectedRows > 0) {
				setChanged();
				notifyObservers();
			}
		} catch (SQLException ex) {
            log.error("Unable to delete activity from the database.\nMessage: {}\nError code: {}", 
					ex.getMessage(), ex.getErrorCode());
            throw ex;
        } 
		return affectedRows;
	}
	
	/**
	 * Orders the given list of <code>Activity</code> in the order in which his ACTIVITY_ID appears 
	 * in the LinkedHashSet. The activities that doesn't appear in the set will be added after this 
	 * ones.
	 * @param activities A list of <code>Activity</code> to be ordered.
	 * @param activityIds a LinkedHashSet containing ACTIVITY_ID Integers in the desired order.
	 * @return The ordered list.
	 */
	public ArrayList<Activity> orderActivitiesByTime(ArrayList<Activity> activities, LinkedHashSet<Integer> activityIds) {
		ArrayList<Activity> reorderedActivities = new ArrayList<>();
//		if (activityIds != null) {
			for (int i : activityIds) {
				for (Activity a : activities) {
					if (i == a.getIdActivity()) {
						reorderedActivities.add(a);
						break;
					}
				}
			}
			for (Activity a : activities) {
				if (!reorderedActivities.contains(a)) reorderedActivities.add(a);
			}
//		}
		return reorderedActivities;
	}
}

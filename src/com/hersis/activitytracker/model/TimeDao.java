package com.hersis.activitytracker.model;

import ch.qos.logback.classic.Logger;
import com.hersis.activitytracker.Activity;
import com.hersis.activitytracker.Time;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Observable;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Igor Rodriguez <igorrodriguezelvira@gmail.com>
 */
public class TimeDao extends Observable {
	private static TimeDao timeDao;
	private final Logger log = (Logger) LoggerFactory.getLogger("model.timeDao");
	
	private TimeDao() {
	}
	
	public static TimeDao getInstance() {
		if (timeDao == null) {
			timeDao = new TimeDao();
		}
		return timeDao;
	}
	
	public ArrayList<Time> getTimesByActivity(Connection conn, Activity activity) throws SQLException {
		final String sql = "SELECT * FROM APP.TIMES "
							+ "WHERE ID_ACTIVITY = " + activity.getIdActivity();
		
		ArrayList<Time> times = new ArrayList<>();
		
		try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
			
			while (rs.next()) {
				int idTime = rs.getInt(("ID_TIME"));
				int idActivity = rs.getInt("ID_ACTIVITY");
				Timestamp startTime = rs.getTimestamp("START_TIME");
				Timestamp endTime = rs.getTimestamp("END_TIME");
				long duration = rs.getLong("DURATION");
				String description = rs.getString("DESCRIPTION");
				times.add(new Time(idTime, idActivity, startTime, endTime, duration, description));
			}
		} catch (SQLException ex) {
            log.error("Unable to read the times from the database.\nMessage: {}\nError code: {}", 
					ex.getMessage(), ex.getErrorCode());
            throw ex;
        } 
		return times;			
	}
	
	public ArrayList<Time> getTimes(Connection conn) throws SQLException {
		final String sql = "SELECT * FROM APP.TIMES ORDER BY END_TIME DESC";
		
		ArrayList<Time> times = new ArrayList<>();
		
		try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
			while (rs.next()) {
				int idTime = rs.getInt(("ID_TIME"));
				int idActivity = rs.getInt("ID_ACTIVITY");
				Timestamp startTime = rs.getTimestamp("START_TIME");
				Timestamp endTime = rs.getTimestamp("END_TIME");
				long duration = rs.getLong("DURATION");
				String description = rs.getString("DESCRIPTION");
				times.add(new Time(idTime, idActivity, startTime, endTime, duration, description));
			}
		} catch (SQLException ex) {
            log.error("Unable to read the times from the database.\nMessage: {}\nError code: {}", 
					ex.getMessage(), ex.getErrorCode());
            throw ex;
        } 
		return times;			
	}
	
	public LinkedHashSet<Integer> getDistinctActivityIdsByTime(Connection conn) throws SQLException {
		final String sql = "SELECT DISTINCT ID_ACTIVITY, END_TIME FROM APP.TIMES ORDER BY END_TIME DESC";
		LinkedHashSet<Integer> activityIds = new LinkedHashSet<>();
		
		try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
			while (rs.next()) {
				activityIds.add(rs.getInt("ID_ACTIVITY"));
			}
		} catch (SQLException ex) {
            log.error("Unable to read the times from the database.\nMessage: {}\nError code: {}", 
					ex.getMessage(), ex.getErrorCode());
            throw ex;
        } 
		return activityIds;		
	}
	
	public int insertTime(Connection conn, Time time) throws SQLException {
		final String sql = "INSERT INTO APP.TIMES (" +
								"ID_ACTIVITY, " +
								"START_TIME, " +
								"END_TIME, " +
								"DURATION, " + 
								"DESCRIPTION) " +
							"VALUES (?, ?, ?, ?, ?)";
		int affectedRows = -1;
				
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, time.getIdActivity());
			stmt.setTimestamp(2, time.getStartTime());
			stmt.setTimestamp(3, time.getEndTime());
			stmt.setLong(4, time.getDuration());
			stmt.setString(5, time.getDescription());
			
			affectedRows = stmt.executeUpdate();
			
			if (affectedRows > 0) {
				setChanged();
				notifyObservers();
			}
		} catch (SQLException ex) {
            log.error("Unable to insert time to the database.\nMessage: {}\nError code: {}", 
					ex.getMessage(), ex.getErrorCode());
            throw ex;
        } 
		return affectedRows;
	}
	
	public int updateTime(Connection conn, Time oldTime, Time newTime) throws SQLException {
		final String sql = "UPDATE APP.TIMES SET " +
								"ID_ACTIVITY = ?, " +
								"START_TIME = ?, " +
								"END_TIME = ?, " +
								"DURATION = ?, " +
								"DESCRIPTION = ? " +
							"WHERE ID_TIME = ?";
		int affectedRows = -1;
				
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, newTime.getIdActivity());
			stmt.setTimestamp(2, newTime.getStartTime());
			stmt.setTimestamp(3, newTime.getEndTime());
			stmt.setLong(4, newTime.getDuration());
			stmt.setString(5, newTime.getDescription());
			stmt.setInt(6, oldTime.getIdTime());
			
			affectedRows = stmt.executeUpdate();
			// If there are changes, notify observers.
			if (affectedRows > 0) {
				setChanged();
				notifyObservers();
			}
		} catch (SQLException ex) {
            log.error("Unable to update time in the database.\nMessage: {}\nError code: {}", 
					ex.getMessage(), ex.getErrorCode());
            throw ex;
        } 
		return affectedRows;
	}
	
	public int deleteTime(Connection conn, Time time) throws SQLException {
		final String sql = "DELETE FROM APP.TIMES " +
							"WHERE ID_TIME = ?";
		int affectedRows = -1;
				
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, time.getIdTime());
			
			affectedRows = stmt.executeUpdate();
			// If there are changes, notify observers.
			if (affectedRows > 0) {
				setChanged();
				notifyObservers();
			}
		} catch (SQLException ex) {
            log.error("Unable to delete time from the database.\nMessage: {}\nError code: {}", 
					ex.getMessage(), ex.getErrorCode());
            throw ex;
        } 
		return affectedRows;
	}
}

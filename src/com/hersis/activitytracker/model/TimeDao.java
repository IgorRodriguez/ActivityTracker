package com.hersis.activitytracker.model;

import ch.qos.logback.classic.Logger;
import com.hersis.activitytracker.Time;
import java.sql.*;
import java.util.ArrayList;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Igor Rodriguez <igorrodriguezelvira@gmail.com>
 */
public class TimeDao {
	private final Logger log = (Logger) LoggerFactory.getLogger("model.ActivityDao");
	
	public TimeDao() {
	}
	
	public ArrayList<Time> getTimes(Connection conn) throws SQLException {
		final String sql = "SELECT * FROM APP.TIMES";
		ArrayList<Time> times = new ArrayList<>();
		
		try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
			while (rs.next()) {
				int idTime = rs.getInt(("ID_TIME"));
				int idActivity = rs.getInt("ID_ACTIVITY");
				Timestamp startTime = rs.getTimestamp("START_TIME");
				Timestamp endTime = rs.getTimestamp("END_TIME");
				Timestamp duration = rs.getTimestamp("DURATION");
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
	
	public int insertTime(Connection conn, Time time) throws SQLException {
		final String sql = "INSERT INTO APP.TIMES (" +
								"ID_ACTIVITY, " +
								"START_TIME, " +
								"END_TIME, " +
								"DURATION, " + 
								"DESCRIPTION) " +
							"VALUES (?, ?, ?, ?, ?)";
				
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, time.getIdActivity());
			stmt.setTimestamp(2, time.getStartTime());
			stmt.setTimestamp(3, time.getEndTime());
			stmt.setTimestamp(4, time.getDuration());
			stmt.setString(5, time.getDescription());
			
			return stmt.executeUpdate();
		} catch (SQLException ex) {
            log.error("Unable to insert time to the database.\nMessage: {}\nError code: {}", 
					ex.getMessage(), ex.getErrorCode());
            throw ex;
        } 
	}
}

package com.hersis.activitytracker.controler;

import com.hersis.activitytracker.Activity;
import com.hersis.activitytracker.Time;
import com.hersis.activitytracker.model.ActivityDao;
import com.hersis.activitytracker.model.Dao;
import com.hersis.activitytracker.model.TimeDao;
import com.hersis.activitytracker.view.TimeDialog;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Igor Rodriguez <igorrodriguezelvira@gmail.com>
 */
public class TimeBO {
	private static final int MINIMUM_TIME_DURATION = 60_000;
	private final Dao dao;
	private final ActivityDao activityDao = new ActivityDao();
	private final TimeDao timeDao = new TimeDao();
	
	private final TimeDialog timeDialog;
	private AlertMessages alertMessages = new AlertMessages();
	private ErrorMessages errorMessages = new ErrorMessages();

	TimeBO(Dao dao, TimeDialog timeDialog) {
		this.dao = dao;
		this.timeDialog = timeDialog;
	}

	void showNewTime() {
		timeDialog.showNewTime();
		timeDialog.setVisible(true);
	}
	
	ArrayList<Activity> getActivities() {
		ArrayList<Activity> activities = null;
		try {
			// Set the JComboBox values from the database in descendant order by date.
			Connection conn = dao.connect();
			activities = activityDao.orderActivitiesByTime(activityDao.getActivities(conn), 
					timeDao.getDistinctActivityIdsByTime(conn));
		} catch (SQLException ex) {
			errorMessages.sqlExceptionError("setActivities()", ex);
		} catch (ClassNotFoundException ex) {
			errorMessages.classNotFoundError("setActivities()", ex);
		} finally {
			dao.disconnect();
		}
		return activities;
	}

	void cancelTimeEdition() {
		timeDialog.setVisible(false);
	}

	void saveTime(Time oldTime, Time newTime) {
		boolean saved;
		
		if (oldTime == null) {
			saved = insertTime(newTime);
		} else {
			saved = updateTime(oldTime, newTime);
		} 
		
		if (saved) {
			timeDialog.setTime(null);
			timeDialog.setVisible(false);
			//updateTimeTable();
			//timeListDialog.selectLastInsertedRow(newTime);
		}
	}

	private boolean insertTime(Time newTime) {
		boolean saved = false;
		
		try {
			Connection conn = dao.connect();
			// Check that the fields are not null and that the duration is greater than 1 minute.
			if (newTime.isFullFilled() && newTime.getDuration().getTime() >= MINIMUM_TIME_DURATION) {
				int insertTime = timeDao.insertTime(conn, newTime);
				if (insertTime > 0) saved = true;				
			} else {
				checkTimeFields(newTime);
			}
		} catch (SQLException ex) {
			errorMessages.sqlExceptionError("insertTime()", ex);
		} catch (ClassNotFoundException ex) {
			errorMessages.classNotFoundError("insertTime()", ex);
		} finally {
			dao.disconnect();
		}
		return saved;
	}

	private boolean updateTime(Time oldTime, Time newTime) {
		throw new UnsupportedOperationException("Not yet implemented");
	}
	
	private void checkTimeFields(Time time) {
		if (time.getIdActivity() < 0) {
			alertMessages.emptyTimeField(timeDialog, AlertMessages.ACTIVITY_ID);
		} else if (time.getStartTime() == null) {
			alertMessages.emptyTimeField(timeDialog, AlertMessages.START_TIME);
		} else if (time.getEndTime() == null) {
			alertMessages.emptyTimeField(timeDialog, AlertMessages.END_TIME);
		} else if (time.getDuration() == null || time.getDuration().getTime() < MINIMUM_TIME_DURATION) {
			alertMessages.emptyTimeField(timeDialog, AlertMessages.DURATION);
		} else {
			alertMessages.emptyTimeField(timeDialog, AlertMessages.OTHER_PROBLEM);
		}
	}
}

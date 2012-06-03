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
		throw new UnsupportedOperationException("Not yet implemented");
	}
}

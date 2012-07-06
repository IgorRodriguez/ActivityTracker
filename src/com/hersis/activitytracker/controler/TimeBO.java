package com.hersis.activitytracker.controler;

import com.hersis.activitytracker.view.AlertMessages;
import com.hersis.activitytracker.Activity;
import com.hersis.activitytracker.Time;
import com.hersis.activitytracker.model.ActivityDao;
import com.hersis.activitytracker.model.Dao;
import com.hersis.activitytracker.model.TimeDao;
import com.hersis.activitytracker.view.TimeDialog;
import com.hersis.activitytracker.view.TimeListDialog;
import java.awt.Component;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 *
 * @author Igor Rodriguez <igorrodriguezelvira@gmail.com>
 */
public class TimeBO implements Observer {
	public static final int MINIMUM_TIME_DURATION = 60_000;
	private final Dao dao;
	private final ActivityDao activityDao = ActivityDao.getInstance();
	private final TimeDao timeDao = TimeDao.getInstance();
	
	private final TimeDialog timeDialog;
	private final TimeListDialog timeListDialog;
	private ErrorMessages errorMessages = new ErrorMessages();

	TimeBO(TimeDialog timeDialog, TimeListDialog timeListDialog) throws ClassNotFoundException, SQLException {
		this.dao = Dao.getInstance();
		this.timeDialog = timeDialog;
		this.timeListDialog = timeListDialog;
		
		activityDao.addObserver(this);
		timeDao.addObserver(this);
	}

	void showNewTime() {
		timeDialog.showNewTime();
		timeDialog.setVisible(true);
	}
	
	ArrayList<Activity> getActivities() {
		ArrayList<Activity> activities = null;
		try {
			// Returns the JComboBox values from the database in descendant order by date.
			Connection conn = dao.connect();
			activities = activityDao.orderActivitiesByTime(activityDao.getActivities(conn), 
					timeDao.getDistinctActivityIdsByTime(conn));
		} catch (SQLException ex) {
			errorMessages.sqlExceptionError("getActivities()", ex);
		} catch (ClassNotFoundException ex) {
			errorMessages.classNotFoundError("getActivities()", ex);
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
			timeListDialog.selectLastInsertedRow(newTime);
		}
	}

	private boolean insertTime(Time newTime) {
		boolean saved = false;
		
		try {
			Connection conn = dao.connect();
			// Check that the fields are not null and that the duration is greater than 1 minute.
			if (newTime.isFullFilled() && newTime.getDuration() >= MINIMUM_TIME_DURATION) {
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
		boolean saved = false;
		
		try {
			Connection conn = dao.connect();
			// Check that the fields are not null and that the duration is greater than 1 minute.
			if (newTime.isFullFilled() && newTime.getDuration() >= MINIMUM_TIME_DURATION) {
				int updateTime = timeDao.updateTime(conn, oldTime, newTime);
				if (updateTime > 0) saved = true;				
			} else {
				checkTimeFields(newTime);
			}
		} catch (SQLException ex) {
			errorMessages.sqlExceptionError("updateTime()", ex);
		} catch (ClassNotFoundException ex) {
			errorMessages.classNotFoundError("updateTime()", ex);
		} finally {
			dao.disconnect();
		}
		return saved;
	}
	
	private void checkTimeFields(Time time) {
		if (time.getIdActivity() < 0) {
			AlertMessages.emptyTimeField(timeDialog, AlertMessages.ACTIVITY_ID);
		} else if (time.getStartTime() == null) {
			AlertMessages.emptyTimeField(timeDialog, AlertMessages.START_TIME);
		} else if (time.getEndTime() == null) {
			AlertMessages.emptyTimeField(timeDialog, AlertMessages.END_TIME);
		} else if (time.getDuration() < MINIMUM_TIME_DURATION) {
			AlertMessages.emptyTimeField(timeDialog, AlertMessages.DURATION);
		} else {
			AlertMessages.emptyTimeField(timeDialog, AlertMessages.OTHER_PROBLEM);
		}
	}

	void showEditTime(Time time) {
		if (time != null) {
			timeDialog.showEditTime(time);
			timeDialog.setVisible(true);
		} else {
			AlertMessages.noTimeSelectedInTableForEditing(timeDialog);
		}
	}

	void deleteTime(Component dialogParent, Time time) {
		if (time != null) {
			if (AlertMessages.deleteTimeConfirmation(dialogParent, time)) {
				try {
					dao.connect();
					timeDao.deleteTime(dao.getConnection(), time);
					timeDialog.setTime(null);
					timeListDialog.selectPreviousRow();
					timeDialog.setVisible(false);
				} catch (SQLException ex) {
					errorMessages.sqlExceptionError("deleteTime()", ex);
				} catch (ClassNotFoundException ex) {
					errorMessages.classNotFoundError("deleteTime()", ex);
				} finally {
					dao.disconnect();
				}
			}
		} else {
			AlertMessages.noTimeSelectedInTableForDeleting(timeListDialog);
		}
	}

	void viewTimes() {
		timeListDialog.setVisible(true);
	}

	public void updateTimeTable() {
		timeListDialog.updateTimeTable(getTimes());
	}

	private ArrayList<Time> getTimes() {
		ArrayList<Time> times = null;
		
		try {
			dao.connect();
			times = timeDao.getTimes(dao.getConnection());
		} catch (SQLException ex) {
			errorMessages.sqlExceptionError("getTimes()", ex);
		} catch (ClassNotFoundException ex) {
			errorMessages.classNotFoundError("getTimes()", ex);
		} finally {
			dao.disconnect();
		}
		
		return times;
	}

	void closeTimeList() {
		timeListDialog.setVisible(false);
	}

	@Override
	public void update(Observable o, Object arg) {
		timeDialog.loadCmbActivities();
		
		if (o instanceof ActivityDao) {
			
		} else if (o instanceof TimeDao) {
			updateTimeTable();
		}
	}

	void loadCmbActivities() {
		timeDialog.loadCmbActivities();
	}
}

package com.hersis.activitytracker.controler;

import com.hersis.activitytracker.Activity;
import com.hersis.activitytracker.Time;
import com.hersis.activitytracker.model.ActivityDao;
import com.hersis.activitytracker.model.Dao;
import com.hersis.activitytracker.model.TimeDao;
import com.hersis.activitytracker.view.ActivityDialog;
import com.hersis.activitytracker.view.ActivityListDialog;
import com.hersis.activitytracker.view.AlertMessages;
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
public class ActivityBO implements Observer {
	private final Dao dao;
	private final ActivityDialog activityDialog;
	private final ActivityListDialog activityListDialog;
	private final ActivityDao activityDao = ActivityDao.getInstance();
	private final TimeDao timeDao = TimeDao.getInstance();
	private final ErrorMessages errorMessages = new ErrorMessages();
	private final Controller controller;

	public ActivityBO(Controller controller, ActivityDialog activityDialog, ActivityListDialog activityListDialog) 
			throws ClassNotFoundException, SQLException {
		this.controller = controller;
		this.dao = Dao.getInstance();
		this.activityDialog = activityDialog;	
		this.activityListDialog = activityListDialog;
		
		activityDao.addObserver(this);
		timeDao.addObserver(this);
		dao.addObserver(this);
	}

	void saveActivity(Activity oldActivity, Activity newActivity) {
		boolean saved;
		
		if (oldActivity == null) {
			saved = insertActivity(newActivity);
		} else {
			saved = updateActivity(oldActivity, newActivity);
		} 
		
		if (saved) {
			activityDialog.setActivity(null);
			activityDialog.setVisible(false);
			activityListDialog.selectLastInsertedRow(newActivity);
		}
	}
   
	private boolean insertActivity(Activity activity) {
		String activityName = activity.getName();
		boolean saved = false;
		
		try {
			Connection conn = Dao.getConnection();
			if (!"".equals(activityName)) {
				if (!activityDao.nameExists(conn, activityName)) {
					int insertActivity = activityDao.insertActivity(conn, activity);
					if (insertActivity > 0) saved = true;
				} else {
					AlertMessages.activityNameExists(activityDialog, activityName);
				}
			} else {
				AlertMessages.emptyActivityFields(activityDialog);
			}
		} catch (SQLException ex) {
			ErrorMessages.sqlExceptionError("insertActivity()", ex);
		} catch (ClassNotFoundException ex) {
			ErrorMessages.classNotFoundError("insertActivity()", ex);
		}
		return saved;
	}

	private boolean updateActivity(Activity oldActivity, Activity newActivity) {
		boolean saved = false;
		
		try {
			Connection conn = Dao.getConnection();
			if (!"".equals(newActivity.getName())) {
				if (oldActivity.getName().equals(newActivity.getName()) ||
						!activityDao.nameExists(conn, newActivity.getName())) {
					int updateActivity = activityDao.updateActivity(conn, oldActivity, newActivity);
					if (updateActivity > 0) saved = true;
				} else {
					AlertMessages.activityNameExists(activityDialog, newActivity.getName());
				}
			} else {
				AlertMessages.emptyActivityFields(activityDialog);
			}
		} catch (SQLException ex) {
			ErrorMessages.sqlExceptionError("updateActivity()", ex);
		} catch (ClassNotFoundException ex) {
			ErrorMessages.classNotFoundError("updateActivity()", ex);
		}
		return saved;
	}

	void cancelActivityEdition() {
		activityDialog.setVisible(false);
	}

	void deleteActivity(Component dialogParent, Activity activity) {
		if (activity != null) {
			if (AlertMessages.deleteActivityConfirmation(dialogParent, activity)) {
				try {
					Connection conn = Dao.getConnection();
					for (Time t : timeDao.getTimesByActivity(conn, activity)) {
						timeDao.deleteTime(conn, t);
					}
					activityDao.deleteActivity(conn, activity);
					activityDialog.setActivity(null);
					activityListDialog.selectPreviousRow();
					activityDialog.setVisible(false);
				} catch (SQLException ex) {
					ErrorMessages.sqlExceptionError("deleteActivity()", ex);
				} catch (ClassNotFoundException ex) {
					ErrorMessages.classNotFoundError("deleteActivity()", ex);
				}
			}
		} else {
			AlertMessages.noActivitySelectedInTableForDeleting(activityListDialog);
		}
	}

	void closeActivityList() {
		activityListDialog.setVisible(false);
	}

	void viewActivities() {
//		updateActivityTable();
		activityListDialog.setVisible(true);
	}

	void updateActivityTable() {
		activityListDialog.updateActivityTable(getActivities());
	}

	void showNewActivity() {
		activityDialog.newActivity();
		activityDialog.setVisible(true);
	}

	void showEditActivity(Activity activity) {
		if (activity != null) {
			activityDialog.editActivity(activity);
			activityDialog.setVisible(true);
		} else {
			AlertMessages.noActivitySelectedInTableForEditing(activityListDialog);
		}
	}

	ArrayList<Activity> getActivities() {
		ArrayList<Activity> activities = null;
		
		try {
			activities = activityDao.getActivities(Dao.getConnection());
		} catch (SQLException ex) {
			ErrorMessages.sqlExceptionError("getActivities()", ex);
		} catch (ClassNotFoundException ex) {
			ErrorMessages.classNotFoundError("getActivities()", ex);
		}
		
		return activities;
	}

	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof ActivityDao || o instanceof TimeDao || o instanceof Dao) {
			updateActivityTable();
		}
	}
	
}

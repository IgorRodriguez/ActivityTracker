package com.hersis.activitytracker.controler;

import com.hersis.activitytracker.Activity;
import com.hersis.activitytracker.Time;
import com.hersis.activitytracker.model.ActivityDao;
import com.hersis.activitytracker.model.Dao;
import com.hersis.activitytracker.model.TimeDao;
import com.hersis.activitytracker.view.ActivityDialog;
import com.hersis.activitytracker.view.ActivityListDialog;
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
	private final AlertMessages alertMessages = new AlertMessages();
	private final ErrorMessages errorMessages = new ErrorMessages();
	private final Controller controller;

	public ActivityBO(Controller controller, ActivityDialog activityDialog, ActivityListDialog activityListDialog) 
			throws ClassNotFoundException, SQLException {
		this.controller = controller;
		this.dao = Dao.getInstance();
		this.activityDialog = activityDialog;	
		this.activityListDialog = activityListDialog;
		
		activityDao.addObserver(this);
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
			Connection conn = dao.connect();
			if (!"".equals(activityName)) {
				if (!activityDao.nameExists(conn, activityName)) {
					int insertActivity = activityDao.insertActivity(conn, activity);
					if (insertActivity > 0) saved = true;
				} else {
					alertMessages.activityNameExists(activityDialog, activityName);
				}
			} else {
				alertMessages.emptyActivityFields(activityDialog);
			}
		} catch (SQLException ex) {
			errorMessages.sqlExceptionError("insertActivity()", ex);
		} catch (ClassNotFoundException ex) {
			errorMessages.classNotFoundError("insertActivity()", ex);
		} finally {
			dao.disconnect();
		}
		return saved;
	}

	private boolean updateActivity(Activity oldActivity, Activity newActivity) {
		boolean saved = false;
		
		try {
			Connection conn = dao.connect();
			if (!"".equals(newActivity.getName())) {
				if (oldActivity.getName().equals(newActivity.getName()) ||
						!activityDao.nameExists(conn, newActivity.getName())) {
					int updateActivity = activityDao.updateActivity(conn, oldActivity, newActivity);
					if (updateActivity > 0) saved = true;
				} else {
					alertMessages.activityNameExists(activityDialog, newActivity.getName());
				}
			} else {
				alertMessages.emptyActivityFields(activityDialog);
			}
		} catch (SQLException ex) {
			errorMessages.sqlExceptionError("updateActivity()", ex);
		} catch (ClassNotFoundException ex) {
			errorMessages.classNotFoundError("updateActivity()", ex);
		} finally {
			dao.disconnect();
		}
		return saved;
	}

	void cancelActivityEdition() {
		activityDialog.setVisible(false);
	}

	void deleteActivity(Component dialogParent, Activity activity) {
		//TODO delete all the dependant times in Times table.
		if (activity != null) {
			if (alertMessages.deleteActivityConfirmation(dialogParent, activity)) {
				try {
					Connection conn = dao.connect();
					for (Time t : timeDao.getTimesByActivity(conn, activity)) {
						timeDao.deleteTime(conn, t);
					}
					activityDao.deleteActivity(conn, activity);
					activityDialog.setActivity(null);
					activityListDialog.selectPreviousRow();
					activityDialog.setVisible(false);
				} catch (SQLException ex) {
					errorMessages.sqlExceptionError("deleteActivity()", ex);
				} catch (ClassNotFoundException ex) {
					errorMessages.classNotFoundError("deleteActivity()", ex);
				} finally {
					dao.disconnect();
				}
			}
		} else {
			alertMessages.noActivitySelectedInTableForDeleting(activityListDialog);
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
			alertMessages.noActivitySelectedInTableForEditing(activityListDialog);
		}
	}

	ArrayList<Activity> getActivities() {
		ArrayList<Activity> activities = null;
		
		try {
			dao.connect();
			activities = activityDao.getActivities(dao.getConnection());
		} catch (SQLException ex) {
			errorMessages.sqlExceptionError("getActivities()", ex);
		} catch (ClassNotFoundException ex) {
			errorMessages.classNotFoundError("getActivities()", ex);
		} finally {
			dao.disconnect();
		}
		
		return activities;
	}

	@Override
	public void update(Observable o, Object arg) {
		updateActivityTable();
	}
	
}

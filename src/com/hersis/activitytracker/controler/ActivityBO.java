package com.hersis.activitytracker.controler;

import com.hersis.activitytracker.Activity;
import com.hersis.activitytracker.model.ActivityDao;
import com.hersis.activitytracker.model.Dao;
import com.hersis.activitytracker.view.ActivityDialog;
import com.hersis.activitytracker.view.ActivityListDialog;
import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author Igor Rodriguez <igorrodriguezelvira@gmail.com>
 */
public class ActivityBO {
	private final Dao dao;
	private final ActivityDialog activityDialog;
	private final ActivityListDialog activityListDialog;
	private final ActivityDao activityDao = new ActivityDao();
	private final AlertMessages alertMessages = new AlertMessages();
	private final ErrorMessages errorMessages = new ErrorMessages();
	private final Controller controller;

	public ActivityBO(Controller controller, Dao dao, ActivityDialog activityDialog, ActivityListDialog activityListDialog) {
		this.controller = controller;
		this.dao = dao;
		this.activityDialog = activityDialog;	
		this.activityListDialog = activityListDialog;
	}

	void newActivity() {
		activityDialog.setActivity(null);
		activityDialog.clearAllFields();
		activityDialog.setVisibleBtnDelete(false);
		activityDialog.setVisible(true);
	}

	void saveActivity() {
		boolean saved;
		
		if (activityDialog.getActivity() == null) {
			saved = insertActivity();
		} else {
			saved = updateActivity();
		} 
		
		if (saved) {
			activityDialog.setVisible(false);
			controller.loadCmbActivities();
		}
	}
	
	private Activity getActivityFromFields() {
		String name = activityDialog.getTextName();
		String description = activityDialog.getTextDescription();
		
		return new Activity(name, description);
	}

	private boolean insertActivity() {
		Activity activity = getActivityFromFields();
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

	private boolean updateActivity() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	void cancelActivityEdition() {
		activityDialog.setVisible(false);
	}

	void deleteActivity() {
		Activity activity = activityDialog.getActivity();
		
		if (activity != null) {
			try {
				dao.connect();
				activityDao.deleteActivity(dao.getConnection(), activity);
				activityDialog.setVisible(false);
			} catch (SQLException ex) {
				errorMessages.sqlExceptionError("deleteActivity()", ex);
			} catch (ClassNotFoundException ex) {
				errorMessages.classNotFoundError("deleteActivity()", ex);
			} finally {
				dao.disconnect();
			}
		}
	}

	void closeActivityList() {
		activityListDialog.setVisible(false);
	}

	void viewActivities() {
		activityListDialog.setVisible(true);
	}
	
}

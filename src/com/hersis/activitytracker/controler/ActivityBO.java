package com.hersis.activitytracker.controler;

import com.hersis.activitytracker.Activity;
import com.hersis.activitytracker.model.ActivityDao;
import com.hersis.activitytracker.model.Dao;
import com.hersis.activitytracker.view.ActivityDialog;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Igor Rodriguez <igorrodriguezelvira@gmail.com>
 */
public class ActivityBO {
	private final Dao dao;
	private final ActivityDialog activityDialog;
	private final ActivityDao activityDao = new ActivityDao();
	private final AlertMessages alertMessages = new AlertMessages();
	private final ErrorMessages errorMessages = new ErrorMessages();
	private final Controller controller;

	public ActivityBO(Controller controller, Dao dao, ActivityDialog activityDialog) {
		this.controller = controller;
		this.dao = dao;
		this.activityDialog = activityDialog;	
	}

	void newActivity() {
		activityDialog.setActivity(null);
		activityDialog.clearAllFields();
		activityDialog.setVisibleBtnDelete(false);
		activityDialog.setVisible(true);
	}

	void saveActivity() {
		boolean saved = false;
		
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
	
}

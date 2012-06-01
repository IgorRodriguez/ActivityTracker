package com.hersis.activitytracker.controler;

import com.hersis.activitytracker.Activity;
import com.hersis.activitytracker.model.ActivityDao;
import com.hersis.activitytracker.model.Dao;
import com.hersis.activitytracker.view.ActivityDialog;
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

	public ActivityBO(Dao dao, ActivityDialog activityDialog) {
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
		if (activityDialog.getActivity() == null) {
			insertActivity();
		} else {
			updateActivity();
		} 		
	}
	
	private Activity getActivityFromFields() {
		String name = activityDialog.getTextName();
		String description = activityDialog.getTextDescription();
		
		return new Activity(name, description);
	}

	private void insertActivity() {
		try {
			dao.connect();
			activityDao.insertActivity(dao.getConnection(), getActivityFromFields());
			lkjk
					//TODO Check the SQL error and call the appropriate alert message.
		} catch (SQLException ex) {
			Logger.getLogger(ActivityBO.class.getName()).log(Level.SEVERE, null, ex);
		} catch (ClassNotFoundException ex) {
			Logger.getLogger(ActivityBO.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	private void updateActivity() {
		throw new UnsupportedOperationException("Not yet implemented");
	}
	
}

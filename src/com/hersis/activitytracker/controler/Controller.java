package com.hersis.activitytracker.controler;

import ch.qos.logback.classic.Logger;
import com.hersis.activitytracker.Activity;
import com.hersis.activitytracker.Time;
import com.hersis.activitytracker.model.Dao;
import com.hersis.activitytracker.view.*;
import java.awt.BorderLayout;
import java.awt.Component;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import javax.swing.WindowConstants;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Igor Rodriguez <igorrodriguezelvira@gmail.com>
 */
public class Controller {
	private static final Logger log = (Logger) LoggerFactory.getLogger("controller.Controller");
	private static Controller controller;
	private Dao dao;
	
	private MainForm mainForm;
	private MainToolbar mainToolbar;
	private TimerPanel timerPanel;
	private ActivityDialog activityDialog;
	private ActivityListDialog activityListDialog;
	private TimeDialog timeDialog;
	private TimeListDialog timeListDialog;
	private BackupDialog backupDialog;
	private BackupConfigDialog backupConfigDialog;
	
	private ControllerBO controllerBo = new ControllerBO();
	private TimerBO timerBo;	
	private ActivityBO activityBo;
	private TimeBO timeBo;
	private BackupBO backupBo;
	private final ErrorMessages errorMessages = new ErrorMessages();
	private final AlertMessages alertMessages = new AlertMessages();
	
	public Controller() {
		try {
			init();
			dao = Dao.getInstance();
			
			timerBo = new TimerBO(timerPanel);
			activityBo = new ActivityBO(this, activityDialog, activityListDialog);
			timeBo = new TimeBO(timeDialog, timeListDialog);
			backupBo = new BackupBO(backupDialog, backupConfigDialog);
			
			// CmbActivities need to be loaded here, cannot be done in their's respectives BO constructors.
			loadCmbActivities();
			activityBo.updateActivityTable();
			timeBo.updateTimeTable();
			
			mainForm.setVisible(true);
		} catch (NullPointerException ex) {
			errorMessages.nullPointerError("Controller()", ex);
		} catch (ClassNotFoundException ex) {
			errorMessages.classNotFoundError("Controller()", ex);
		} catch (SQLException ex) {
			errorMessages.sqlExceptionError("Controller()", ex);
		}
	}
	
	private static Controller getInstance() {
		if (controller == null) {
			controller = new Controller();
		}
		return controller;
	}
	
	private void init() {
		controllerBo.modifyLookAndFeel();
		
		controllerBo.loadProperties();
		
		// Main form creation and settings
		mainForm = new MainForm(this);
		mainToolbar = new MainToolbar(this);
		timerPanel = new TimerPanel(this);
		
		mainForm.add(mainToolbar, BorderLayout.NORTH);
		mainForm.add(timerPanel, BorderLayout.CENTER);
		mainForm.pack();
		mainForm.setLocationRelativeTo(null);
		mainForm.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		mainForm.getRootPane().setDefaultButton(timerPanel.getNewButton());
		
//		// Dialog creation and settings
		activityDialog = new ActivityDialog(mainForm, true, this);
		activityDialog.setLocationRelativeTo(mainForm);
		activityListDialog = new ActivityListDialog(mainForm, true, this);
		activityListDialog.setLocationRelativeTo(mainForm);
		timeDialog = new TimeDialog(mainForm, true, this);
		timeDialog.setLocationRelativeTo(mainForm);
		timeListDialog = new TimeListDialog(mainForm, true, this);
		timeListDialog.setLocationRelativeTo(mainForm);
		backupDialog = new BackupDialog(mainForm, true, this);
		backupDialog.setLocationRelativeTo(mainForm);
		backupConfigDialog = new BackupConfigDialog(mainForm, true, this);
		backupConfigDialog.setLocationRelativeTo(mainForm);
	}
	
	final void loadCmbActivities() {
		timerBo.loadCmbActivities();
		timeBo.loadCmbActivities();
	}
	
	public String getPropertie(String key) {
		return controllerBo.getPropertie(key);
	}
	
	public void setPropertie(String key, String value) {
		controllerBo.setPropertie(key, value);
	}
	
	public void removePropertie(String key) {
		controllerBo.removePropertie(key);
	}
	
	/**
	 * Finalizes correctly the application and exits.
	 */
	public void exit() {
		controllerBo.exit(dao, mainForm);
	}

	/**
	 * Action when "play" button is pressed in the GUI.
	 */
	public void play() {
		timerBo.play();
	}

	/**
	 * Action when "pause" button is pressed in the GUI.
	 */
	public void pause() {
		timerBo.pause();
	}

	/**
	 * Action when "stop" button is pressed in the GUI.
	 */
	public void stop() {
		try {
			timerBo.stop();
		} catch (SQLException ex) {
			errorMessages.sqlExceptionError("stop()", ex);
		} catch (ClassNotFoundException ex) {
			errorMessages.classNotFoundError("stop()", ex);
		}
	}

	/**
	 * Action when "new" button is pressed in the GUI.
	 */
	public void startTracking() {
		try {
			timerBo.startTracking();
		} catch (IndexOutOfBoundsException ex) {
			alertMessages.startTrackingIndexOutOfBounds(timerPanel, ex);
		}
	}

	/**
	 * Action when "new activity" is selected in the GUI.
	 */
	public void showNewActivityWindow() {
		activityBo.showNewActivity();
	}

	/**
	 * Action when "accept" is pressed in the ActivityDialog.
	 */
	public void saveActivity(Activity oldActivity, Activity newActivity) {
		activityBo.saveActivity(oldActivity, newActivity);
	}

	public void cancelActivityEdition() {
		activityBo.cancelActivityEdition();
	}

	public void closeActivityList() {
		activityBo.closeActivityList();
	}

	public void viewActivitiesWindow() {
		activityBo.viewActivities();
	}

	public void showEditActivityWindow(Activity activity) {
		activityBo.showEditActivity(activity);
	}

	public void deleteActivity(Component dialogParent, Activity activity) {
		activityBo.deleteActivity(dialogParent, activity);
	}

	public void showNewTimeWindow() {
		timeBo.showNewTime();
	}

	public long calculateDuration(Calendar startTime, Calendar endTime) {
		return controllerBo.calculateDuration(startTime, endTime);
	}

	public String getDurationString(long time) {
		return controllerBo.getDurationString(time);
	}

	public ArrayList<Activity> getActivitiesOrderedByTime() {
		return timeBo.getActivities();
	}

	public void cancelTimeEdition() {
		timeBo.cancelTimeEdition();
	}

	public void saveTime(Time oldTime, Time newTime) {
		timeBo.saveTime(oldTime, newTime);
	}
	
	public ArrayList<Activity> getActivities() {
		return activityBo.getActivities();
	}

	public void showEditTimeWindow(Time time) {
		timeBo.showEditTime(time);
	}

	public void deleteTime(Component dialogParent, Time time) {
		timeBo.deleteTime(dialogParent, time);
	}

	public void viewTimesWindow() {
		timeBo.viewTimes();
	}

	public void closeTimeList() {
		timeBo.closeTimeList();
	}

	public void closeBackup() {
		backupBo.closeBackupWindow();
	}

	public void showBackupWindow() {
		backupBo.showBackupWindow();
	}

	public void showBackupConfigWindow() {
		backupBo.showBackupConfigWindow();
	}
}

package com.hersis.activitytracker.controler;

import ch.qos.logback.classic.Logger;
import com.hersis.activitytracker.Activity;
import com.hersis.activitytracker.ApplicationProperties;
import com.hersis.activitytracker.Time;
import com.hersis.activitytracker.model.ActivityDao;
import com.hersis.activitytracker.model.Dao;
import com.hersis.activitytracker.model.TimeDao;
import com.hersis.activitytracker.view.*;
import java.awt.BorderLayout;
import java.awt.Component;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Observer;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Igor Rodriguez <igorrodriguezelvira@gmail.com>
 */
public class Controller {
	private static final Logger log = (Logger) LoggerFactory.getLogger("controller.Controller");
	private static Controller controller;

	private static MainForm mainForm;
	private MainToolbar mainToolbar;
	private static TimerPanel timerPanel;
	private ActivityDialog activityDialog;
	private ActivityListDialog activityListDialog;
	private TimeDialog timeDialog;
	private TimeListDialog timeListDialog;
	private BackupDialog backupDialog;
	private BackupConfigDialog backupConfigDialog;
		
	private static ControllerBO controllerBo = new ControllerBO();
	private static TimerBO timerBo;	
	private static ActivityBO activityBo;
	private static TimeBO timeBo;
	private static BackupBO backupBo;
	
	//TODO Remove references to the public variable below.
	public static final String APPLICATION_NAME = "ActivityTrackerV2";
		
	public Controller() {
		log.debug("Starting Controller()");
		try {
			init();
			
			// TimerBO
			timerBo = new TimerBO(timerPanel);
			Dao.getInstance().addObserver(timerBo);
			ActivityDao.getInstance().addObserver(timerBo);
			TimeDao.getInstance().addObserver(timerBo);
			
			// ActivityBO
			activityBo = new ActivityBO(activityDialog, activityListDialog);
			Dao.getInstance().addObserver(activityBo);
			ActivityDao.getInstance().addObserver(activityBo);
			TimeDao.getInstance().addObserver(activityBo);
			
			// TimeBO
			timeBo = new TimeBO(timeDialog, timeListDialog);
			Dao.getInstance().addObserver(timeBo);
			ActivityDao.getInstance().addObserver(timeBo);
			TimeDao.getInstance().addObserver(timeBo);
			log.debug("TimeDao created");
			// BackupBO
			backupBo = new BackupBO(backupDialog, backupConfigDialog);
			addPropertiesObserver(backupBo);
			
			log.debug("Forms created");
			
			// CmbActivities need to be loaded here, cannot be done in their's respectives BO constructors.
			loadCmbActivities();
			activityBo.updateActivityTable();
			timeBo.updateTimeTable();
			backupDialog.setBackupDateLabel();
			
			mainForm.setVisible(true);
		} catch (NullPointerException ex) {
			ErrorMessages.nullPointerError("Controller()", ex);
		} catch (ClassNotFoundException ex) {
			ErrorMessages.classNotFoundError("Controller()", ex);
		} catch (SQLException ex) {
			ErrorMessages.sqlExceptionError("Controller()", ex);
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
		mainForm = new MainForm();
		log.debug("MainForm created");
	
		mainToolbar = new MainToolbar(this);
		log.debug("MainToolbar created");
		timerPanel = new TimerPanel(this);
		log.debug("TimerPanel created");
		
		mainForm.add(mainToolbar, BorderLayout.NORTH);
		mainForm.add(timerPanel, BorderLayout.CENTER);
		mainForm.pack();
		mainForm.setLocationRelativeTo(null);
		mainForm.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		mainForm.getRootPane().setDefaultButton(timerPanel.getNewButton());
		
		log.debug("MainForm configured");
//		// Dialog creation and settings
		activityDialog = new ActivityDialog(mainForm, true);
		activityDialog.setLocationRelativeTo(mainForm);
		
		activityListDialog = new ActivityListDialog(mainForm, true);
		activityListDialog.setLocationRelativeTo(mainForm);
		
		timeDialog = new TimeDialog(mainForm, true);
		timeDialog.setLocationRelativeTo(mainForm);
		log.debug("TimeDialog created");
		
		timeListDialog = new TimeListDialog(mainForm, true);
		timeListDialog.setLocationRelativeTo(mainForm);
		log.debug("TimeListDialog created");
		
		backupDialog = new BackupDialog(mainForm, true);
		backupDialog.setLocationRelativeTo(mainForm);
		addPropertiesObserver(backupDialog);
		log.debug("BackupDialog created");
		
		backupConfigDialog = new BackupConfigDialog(mainForm, true);
		backupConfigDialog.setLocationRelativeTo(mainForm);
		log.debug("End of window creation");
	}
	
	final void loadCmbActivities() {
		timerBo.loadCmbActivities();
		timeBo.loadCmbActivities();
	}
	
	public static String getPropertie(ApplicationProperties key) {
		return controllerBo.getPropertie(key);
	}
	
	public static void setPropertie(ApplicationProperties key, String value) {
		controllerBo.setPropertie(key, value);
	}
	
	public static void removePropertie(ApplicationProperties key) {
		controllerBo.removePropertie(key);
	}
	
	public static void addPropertiesObserver(Observer o) {
		controllerBo.addObserver(o);
	}
	
	public static void deletePropertiesObserver(Observer o) {
		controllerBo.deleteObserver(o);
	}
	
	/**
	 * Finalizes correctly the application and exits.
	 */
	public static void exit() {
		ControllerBO.exit(mainForm);
	}
	
	public static JFrame getMainFrame() {
		return mainForm;
	}

	/**
	 * Action when "play" button is pressed in the GUI.
	 */
	public static void play() {
		timerBo.play();
	}

	/**
	 * Action when "pause" button is pressed in the GUI.
	 */
	public static void pause() {
		timerBo.pause();
	}

	/**
	 * Action when "stop" button is pressed in the GUI.
	 */
	public static void stop() {
		try {
			timerBo.stop();
		} catch (SQLException ex) {
			ErrorMessages.sqlExceptionError("stop()", ex);
		} catch (ClassNotFoundException ex) {
			ErrorMessages.classNotFoundError("stop()", ex);
		}
	}

	/**
	 * Action when "new" button is pressed in the GUI.
	 */
	public static void startTracking() {
		try {
			timerBo.startTracking();
		} catch (IndexOutOfBoundsException ex) {
			AlertMessages.startTrackingIndexOutOfBounds(timerPanel, ex);
		}
	}

	/**
	 * Action when "new activity" is selected in the GUI.
	 */
	public static void showNewActivityWindow() {
		activityBo.showNewActivity();
	}

	/**
	 * Action when "accept" is pressed in the ActivityDialog.
	 */
	public static void saveActivity(Activity oldActivity, Activity newActivity) {
		activityBo.saveActivity(oldActivity, newActivity);
	}

	public static void cancelActivityEdition() {
		activityBo.cancelActivityEdition();
	}

	public static void closeActivityList() {
		activityBo.closeActivityList();
	}

	public static void viewActivitiesWindow() {
		activityBo.viewActivities();
	}

	public static void showEditActivityWindow(Activity activity) {
		activityBo.showEditActivity(activity);
	}

	public static void deleteActivity(Component dialogParent, Activity activity) {
		activityBo.deleteActivity(dialogParent, activity);
	}

	public static void showNewTimeWindow() {
		timeBo.showNewTime();
	}

	public static long calculateDuration(Calendar startTime, Calendar endTime) {
		return controllerBo.calculateDuration(startTime, endTime);
	}

	public static String getDurationString(long time) {
		return controllerBo.getDurationString(time);
	}

	public static ArrayList<Activity> getActivitiesOrderedByTime() {
		return timeBo.getActivities();
	}

	public static void cancelTimeEdition() {
		timeBo.cancelTimeEdition();
	}

	public static void saveTime(Time oldTime, Time newTime) {
		timeBo.saveTime(oldTime, newTime);
	}
	
	public static ArrayList<Activity> getActivities() {
		return activityBo.getActivities();
	}

	public static void showEditTimeWindow(Time time) {
		timeBo.showEditTime(time);
	}

	public static void deleteTime(Component dialogParent, Time time) {
		timeBo.deleteTime(dialogParent, time);
	}

	public static void viewTimesWindow() {
		timeBo.viewTimes();
	}

	public static void closeTimeList() {
		timeBo.closeTimeList();
	}

	/**********************************************************************************************
	 * Backup
	 **********************************************************************************************/
	
	public static void closeBackupWindow() {
		backupBo.closeBackupWindow();
	}

	public static void showBackupWindow() {
		backupBo.showBackupWindow();
	}

	public static void showBackupConfigWindow() {
		backupBo.showBackupConfigWindow();
	}
	
	public static void startBackup(Component parentWindow) {
		backupBo.startBackup(parentWindow);
	}
	
	public static File[] getAvailableBackups(File filePath) {
		return BackupBO.getAvailableBackups(filePath);
	}

	public static void restoreBackup(Component parentWindow, String path) {
		BackupBO.restoreBackup(parentWindow,path);
	}

	public static Calendar getLastBackupDate() {
		return BackupBO.getLastBackupDate();
	}
}

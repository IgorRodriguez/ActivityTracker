package com.hersis.activitytracker.controler;

import ch.qos.logback.classic.Level;
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
	private static AboutDialog aboutDialog;
		
	private static ControllerBO controllerBo = ControllerBO.getInstance();
	private static TimerBO timerBo;	
	private static ActivityBO activityBo;
	private static TimeBO timeBo;
	private static BackupBO backupBo;
			
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
			
			// BackupBO
			backupBo = new BackupBO(backupDialog, backupConfigDialog);
			addPropertiesObserver(backupBo);
			
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
	
	public static Controller getInstance() {
		if (controller == null) {
			controller = new Controller();
		}
		return controller;
	}
	
	private void init() {
//		controllerBo.modifyLookAndFeel();
		
		configureLogger();
		
		// Main form creation and settings
		mainForm = new MainForm();	
		mainToolbar = new MainToolbar();
		timerPanel = new TimerPanel();
		
		mainForm.add(mainToolbar, BorderLayout.NORTH);
		mainForm.add(timerPanel, BorderLayout.CENTER);
		mainForm.pack();
		mainForm.setLocationRelativeTo(null);
		mainForm.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		mainForm.getRootPane().setDefaultButton(timerPanel.getNewButton());
		
//		// Dialog creation and settings
		activityDialog = new ActivityDialog(mainForm, true);
		activityDialog.setLocationRelativeTo(mainForm);
		
		activityListDialog = new ActivityListDialog(mainForm, true);
		activityListDialog.setLocationRelativeTo(mainForm);
		
		timeDialog = new TimeDialog(mainForm, true);
		timeDialog.setLocationRelativeTo(mainForm);
		
		timeListDialog = new TimeListDialog(mainForm, true);
		timeListDialog.setLocationRelativeTo(mainForm);
		
		backupDialog = new BackupDialog(mainForm, true);
		backupDialog.setLocationRelativeTo(mainForm);
		addPropertiesObserver(backupDialog);
		
		backupConfigDialog = new BackupConfigDialog(mainForm, true);
		backupConfigDialog.setLocationRelativeTo(mainForm);
		
		aboutDialog = new AboutDialog(mainForm, true);
		aboutDialog.setLocationRelativeTo(mainForm);
		log.debug("End of window creation");
	}
	
	/**
	 * Configures the logging of the application.
	 */
	public void configureLogger() {
        log.setLevel(Level.DEBUG);
		ControllerBO.configureLog();
		
		final String separator= "*****************************************************************";
		final String message =	"Starting application";
		log.info(separator);
		log.info(message);
		log.info(separator);
	}
	
	final void loadCmbActivities() {
		timerBo.loadCmbActivities();
		timeBo.loadCmbActivities();
	}
	
	public static String getPropertie(ApplicationProperties key) {
		return controllerBo.getPropertie(key);
	}
	
	/**
	 * Sets the specified property to the given value.
	 * @param key The property to change.
	 * @param value The new value to the given property.
	 */
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
		controllerBo.exit(mainForm);
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
	
	public static void showAboutWindow() {
		aboutDialog.setVisible(true);
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
	
	public static void startBackup(Component parentWindow, String destinationPath) {
		backupBo.startBackup(parentWindow, destinationPath);
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

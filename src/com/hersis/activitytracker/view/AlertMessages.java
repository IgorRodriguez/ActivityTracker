package com.hersis.activitytracker.view;

import ch.qos.logback.classic.Logger;
import com.hersis.activitytracker.Activity;
import com.hersis.activitytracker.Time;
import java.awt.Component;
import java.io.IOException;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Igor Rodriguez <igorrodriguezelvira@gmail.com>
 */
public abstract class AlertMessages {
	private final static Logger log = (Logger) LoggerFactory.getLogger("controller.AlertMessages");
	public static final int OTHER_PROBLEM = -1;
	public static final int ACTIVITY_ID = 0;
	public static final int START_TIME = 1;
	public static final int END_TIME = 2;
	public static final int DURATION = 3;

	/***********************************************************************************************
	 * Backup alerts
	 **********************************************************************************************/
	
	static void noBackupSelected(Component dialogParent) {
		String message = "There isn't any backup selected";
		String title = "Alert";
		JOptionPane.showMessageDialog(dialogParent, message, title, JOptionPane.WARNING_MESSAGE);
	}

	public static void backupPathNull(Component dialogParent) {
		String message = "Select a valid backup directory";
		String title = "Alert";
		JOptionPane.showMessageDialog(dialogParent, message, title, JOptionPane.WARNING_MESSAGE);
	}

	public static void backupPeriodNull(Component dialogParent) {
		String message = "Select a backup period";
		String title = "Alert";
		JOptionPane.showMessageDialog(dialogParent, message, title, JOptionPane.WARNING_MESSAGE);
	}
	
	/***********************************************************************************************
	 * Activity alerts
	 **********************************************************************************************/

	public static void emptyActivityFields(Component dialogParent) {
		String message = "The name of the activity cannot be empty";
		String title = "Alert";
		JOptionPane.showMessageDialog(dialogParent, message, title, JOptionPane.WARNING_MESSAGE);
	}

	public static void activityNameExists(Component dialogParent, String name) {
		String message = "The name '" + name + "' already exists, choose another one";
		String title = "Alert";
		JOptionPane.showMessageDialog(dialogParent, message, title, JOptionPane.WARNING_MESSAGE);
	}

	public static void noActivitySelectedInTableForEditing(Component dialogParent) {
		String message = "You need to select an activity in the table in order to edit it";
		String title = "Alert";
		JOptionPane.showMessageDialog(dialogParent, message, title, JOptionPane.WARNING_MESSAGE);
	}

	public static void noActivitySelectedInTableForDeleting(Component dialogParent) {
		String message = "You need to select an activity in the table in order to delete it";
		String title = "Alert";
		JOptionPane.showMessageDialog(dialogParent, message, title, JOptionPane.WARNING_MESSAGE);
	}

	public static boolean deleteActivityConfirmation(Component dialogParent, Activity activity) {
		boolean delete = false;
		String message = "Are you sure that do you like to delete the activity '" + activity.getName() + "'?";
		message += "\nNote that all the times stored with this activity will be erased as well.";
		String title = "Alert";
		int response = JOptionPane.showConfirmDialog(dialogParent, message, title, 
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (response == JOptionPane.OK_OPTION) delete = true;
		
		return delete;
	}

	/***********************************************************************************************
	 * Time alerts
	 **********************************************************************************************/
	
	public static void emptyTimeField(Component dialogParent, int problem) {
		String message;
		
		switch (problem) {
			case ACTIVITY_ID:
				message = "You must select an activity in the list";
				break;
			case START_TIME:
				message = "You must select an activity start time";
				break;
			case END_TIME: 
				message = "You must select an activity end time";
				break;
			case DURATION:
				message = "The activity must last at least 1 minute";
				break;
			case OTHER_PROBLEM: default:
				message = "Couldn't save this activity time. Check that you filled all the fields correctly";
		}
		String title = "Alert";
		JOptionPane.showMessageDialog(dialogParent, message, title, JOptionPane.WARNING_MESSAGE);
	}

	public static boolean deleteTimeConfirmation(Component dialogParent, Time time) {
		boolean delete = false;
		String message = "Are you sure that do you like to delete this time?";
		String title = "Alert";
		int response = JOptionPane.showConfirmDialog(dialogParent, message, title, 
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (response == JOptionPane.OK_OPTION) delete = true;
		
		return delete;
	}

	public static void noTimeSelectedInTableForDeleting(Component dialogParent) {
		String message = "You need to select a time in the table in order to delete it";
		String title = "Alert";
		JOptionPane.showMessageDialog(dialogParent, message, title, JOptionPane.WARNING_MESSAGE);
	}

	public static void noTimeSelectedInTableForEditing(Component dialogParent) {
		String message = "You need to select a time in the table in order to edit it";
		String title = "Alert";
		JOptionPane.showMessageDialog(dialogParent, message, title, JOptionPane.WARNING_MESSAGE);
	}

	/***********************************************************************************************
	 * Properties alerts
	 **********************************************************************************************/
	
	public static void propertiesLoadIOException(IOException e) {
		String message = "Unable to load the previous configuration of the application.\n" +
				"Default values will be loaded.";
            JOptionPane.showMessageDialog(null, message, "Unable to load configuration",
                            JOptionPane.WARNING_MESSAGE);
            log.info(message + "\nMessage: {}", e.getLocalizedMessage());
	}

	public static void propertiesSaveIOException(IOException e) {
		String message = "Unable to save the configuration of the application.";
            JOptionPane.showMessageDialog(null, message, "Unable to save configuration",
                            JOptionPane.WARNING_MESSAGE);
            log.info(message + "\nMessage: {}", e.getLocalizedMessage());
	}
	
	/***********************************************************************************************
	 * Other alerts
	 **********************************************************************************************/

	public static boolean exitSQLException(Component mainParent, SQLException e) {
		boolean exit = true;
		String errorMessage = "The database isn't correctly closed.\n" +
				"Do you like to force the closure of the application?";
		
		int answer = JOptionPane.showConfirmDialog(mainParent, errorMessage, 
				"¿Force and exit?", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
		if (answer == JOptionPane.CANCEL_OPTION || answer == JOptionPane.CLOSED_OPTION) exit = false; 
		
		return exit;
	}
	
	public static void startTrackingIndexOutOfBounds(Component parent, IndexOutOfBoundsException ex) {
		String message = ex.getLocalizedMessage();
		log.error(message);
		JOptionPane.showMessageDialog(parent, message, "Error", JOptionPane.ERROR_MESSAGE);
	}
}

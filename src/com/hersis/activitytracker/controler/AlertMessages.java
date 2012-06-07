package com.hersis.activitytracker.controler;

import com.hersis.activitytracker.Activity;
import com.hersis.activitytracker.Time;
import com.hersis.activitytracker.view.TimeDialog;
import com.hersis.activitytracker.view.TimeListDialog;
import java.awt.Component;
import javax.swing.JOptionPane;

/**
 *
 * @author Igor Rodriguez <igorrodriguezelvira@gmail.com>
 */
public class AlertMessages {
	public static final int OTHER_PROBLEM = -1;
	public static final int ACTIVITY_ID = 0;
	public static final int START_TIME = 1;
	public static final int END_TIME = 2;
	public static final int DURATION = 3;

	void emptyActivityFields(Component dialogParent) {
		String message = "The name of the activity cannot be empty";
		String title = "Alert";
		JOptionPane.showMessageDialog(dialogParent, message, title, JOptionPane.WARNING_MESSAGE);
	}

	void activityNameExists(Component dialogParent, String name) {
		String message = "The name '" + name + "' already exists, choose another one";
		String title = "Alert";
		JOptionPane.showMessageDialog(dialogParent, message, title, JOptionPane.WARNING_MESSAGE);
	}

	void noActivitySelectedInTableForEditing(Component dialogParent) {
		String message = "You need to select an activity in the table in order to edit it";
		String title = "Alert";
		JOptionPane.showMessageDialog(dialogParent, message, title, JOptionPane.WARNING_MESSAGE);
	}

	void noActivitySelectedInTableForDeleting(Component dialogParent) {
		String message = "You need to select an activity in the table in order to delete it";
		String title = "Alert";
		JOptionPane.showMessageDialog(dialogParent, message, title, JOptionPane.WARNING_MESSAGE);
	}

	boolean deleteActivityConfirmation(Component dialogParent, Activity activity) {
		boolean delete = false;
		String message = "Are you sure that do you like to delete the activity '" + activity.getName() + "'?";
		message += "\nNote that all the times stored with this activity will be erased as well.";
		String title = "Alert";
		int response = JOptionPane.showConfirmDialog(dialogParent, message, title, 
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (response == JOptionPane.OK_OPTION) delete = true;
		
		return delete;
	}

	void emptyTimeField(Component dialogParent, int problem) {
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

	boolean deleteTimeConfirmation(Component dialogParent, Time time) {
		boolean delete = false;
		String message = "Are you sure that do you like to delete this time?";
		String title = "Alert";
		int response = JOptionPane.showConfirmDialog(dialogParent, message, title, 
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (response == JOptionPane.OK_OPTION) delete = true;
		
		return delete;
	}

	void noTimeSelectedInTableForDeleting(Component dialogParent) {
		String message = "You need to select a time in the table in order to delete it";
		String title = "Alert";
		JOptionPane.showMessageDialog(dialogParent, message, title, JOptionPane.WARNING_MESSAGE);
	}

	void noTimeSelectedInTableForEditing(Component dialogParent) {
		String message = "You need to select a time in the table in order to edit it";
		String title = "Alert";
		JOptionPane.showMessageDialog(dialogParent, message, title, JOptionPane.WARNING_MESSAGE);
	}
	
}

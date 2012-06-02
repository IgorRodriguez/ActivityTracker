package com.hersis.activitytracker.controler;

import com.hersis.activitytracker.Activity;
import java.awt.Component;
import javax.swing.JOptionPane;

/**
 *
 * @author Igor Rodriguez <igorrodriguezelvira@gmail.com>
 */
public class AlertMessages {
	private Component sdfa;

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
		String title = "Alert";
		int response = JOptionPane.showConfirmDialog(dialogParent, message, title, 
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (response == JOptionPane.OK_OPTION) delete = true;
		
		return delete;
	}
	
}

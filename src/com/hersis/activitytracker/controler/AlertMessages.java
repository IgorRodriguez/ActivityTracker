package com.hersis.activitytracker.controler;

import com.hersis.activitytracker.view.ActivityDialog;
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
	
}

package com.hersis.activitytracker.controler;

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
	
}

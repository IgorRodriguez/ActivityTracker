package com.hersis.activitytracker.controler;

import ch.qos.logback.classic.Logger;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Igor Rodriguez <igorrodriguezelvira@gmail.com>
 */
public class ErrorMessages {
	private final Logger log = (Logger) LoggerFactory.getLogger("controller.ControllerBO");
	
	/**
	 * Shows a message in case of a ClassNotFoundException.
	 * @param methodName The name of the method that calls this one.
	 * @param e 
	 */
	void classNotFoundAlert(String methodName, ClassNotFoundException e) {
		String message = "Unable to load the class in method '" + methodName + "': \n" + e.getLocalizedMessage();
		log.error(message);
		JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
	}
	
	/**
	 * Shows a message in case of a SQL exception.
	 * @param methodName The name of the method that calls this one.
	 * @param e The SQL exception.
	 */
	void sqlExceptionAlert(String methodName, SQLException e) {
		if (!"Derby system shutdown.".equals(e.getLocalizedMessage())) {
			String message = "Error while executing SQL instruction in method '" + methodName + "': \n" + 
					e.getLocalizedMessage() + " - " + e.getErrorCode();
			log.error(message);
			JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	void nullPointerAlert(String methodName, NullPointerException ex) {
		log.error(ex.getLocalizedMessage());
	}
}

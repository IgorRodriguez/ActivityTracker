package com.hersis.activitytracker.controler;

import ch.qos.logback.classic.Logger;
import com.hersis.activitytracker.view.TimerPanel;
import java.io.IOException;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.UnsupportedLookAndFeelException;
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
	void classNotFoundError(String methodName, ClassNotFoundException e) {
		String message = "Unable to load the class in method '" + methodName + "': \n" + e.getLocalizedMessage();
		log.error(message);
		JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
	}
	
	/**
	 * Shows a message in case of a SQL exception.
	 * @param methodName The name of the method that calls this one.
	 * @param e The SQL exception.
	 */
	void sqlExceptionError(String methodName, SQLException e) {
		if (!"Derby system shutdown.".equals(e.getLocalizedMessage())) {
			String message = "Error while executing SQL instruction in method '" + methodName + "': \n" + 
					e.getLocalizedMessage() + " - " + e.getErrorCode();
			log.error(message);
			JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	void nullPointerError(String methodName, NullPointerException ex) {
		log.error("NullPointerException on {}: {}", methodName, ex.getLocalizedMessage());
	}

	void unsupportedLookAndFeelError(String methodName, UnsupportedLookAndFeelException e) {
		String message = "Unable to load the class in method '" + methodName + "': \n" + e.getLocalizedMessage();
		log.error(message);
		JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
	}

	void createPropertiesFileIOException(String methodName, IOException e) {
		String message = "Unable to create the configuration file in method '" + methodName + "': \n" + 
				e.getLocalizedMessage();
		log.error(message);
	}
}

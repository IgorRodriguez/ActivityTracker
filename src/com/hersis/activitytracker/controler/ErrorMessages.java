package com.hersis.activitytracker.controler;

import ch.qos.logback.classic.Logger;
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

	private static final Logger log = (Logger) LoggerFactory.getLogger("controller.ControllerBO");
	
	/**
	 * Shows a message in case of a ClassNotFoundException.
	 * @param methodName The name of the method that calls this one.
	 * @param e 
	 */
	public static void classNotFoundError(String methodName, ClassNotFoundException e) {
		String message = "Unable to load the class in method '" + methodName + "': \n" + e.getLocalizedMessage();
		log.error(message);
		JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
	}
	
	/**
	 * Shows a message in case of a SQL exception.
	 * @param methodName The name of the method that calls this one.
	 * @param e The SQL exception.
	 */
	public static void sqlExceptionError(String methodName, SQLException e) {
		if (!"Derby system shutdown.".equals(e.getLocalizedMessage())) {
			String message = "Error while executing SQL instruction in method '" + methodName + "': \n" + 
					e.getLocalizedMessage() + " - " + e.getErrorCode();
			log.error(message);
			JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	public static void nullPointerError(String methodName, NullPointerException ex) {
		log.error("NullPointerException on {}: {}", methodName, ex.getLocalizedMessage());
	}

	public static void unsupportedLookAndFeelError(String methodName, UnsupportedLookAndFeelException e) {
		String message = "Unable to load the class in method '" + methodName + "': \n" + e.getLocalizedMessage();
		log.error(message);
		JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
	}

	public static void createPropertiesFileIOException(String methodName, IOException e) {
		String message = "Unable to create the configuration file in method '" + methodName + "': \n" + 
				e.getLocalizedMessage();
		log.error(message);
	}
	
	/**********************************************************************************************
	 * Backup
	 **********************************************************************************************/
		
	public static void databaseBackupError(String methodName, SQLException ex, String backupPath) {
		String logMessage = "Unable to backup database with method '" + methodName + "'\n" + 
				"Path '" + backupPath + "'\n Error: " + ex.getLocalizedMessage();
		log.error(logMessage);
		
		String message = "Unable to backup database in path '" + backupPath;
		JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
	}
}

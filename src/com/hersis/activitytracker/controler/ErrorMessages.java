package com.hersis.activitytracker.controler;

import ch.qos.logback.classic.Logger;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.UnsupportedLookAndFeelException;
import net.lingala.zip4j.exception.ZipException;
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
		JOptionPane.showMessageDialog(Controller.getMainFrame(), message, "Error", 
				JOptionPane.ERROR_MESSAGE);
	}
	
	/**
	 * Shows a message in case of a SQL exception.
	 * @param methodName The name of the method that calls this one.
	 * @param e The SQL exception.
	 */
	public static void sqlExceptionError(String methodName, SQLException e) {
		int errorCode = e.getErrorCode();
		if (!(errorCode != 50000 && "XJ015".equals(e.getSQLState()))) {
			String message = "Error while executing SQL instruction in method '" + methodName + "': \n" + 
					e.getLocalizedMessage() + "\nError code: " + e.getErrorCode() + "\nSQLState: " +
					e.getSQLState();
			log.error(message);
			JOptionPane.showMessageDialog(Controller.getMainFrame(), message, "Error", 
					JOptionPane.ERROR_MESSAGE);
		}
	}

	public static void nullPointerError(String methodName, NullPointerException ex) {
		log.error("NullPointerException on {}: {}", methodName, ex.getLocalizedMessage());
		ex.printStackTrace();
	}

	public static void unsupportedLookAndFeelError(String methodName, UnsupportedLookAndFeelException e) {
		String message = "Unable to load the look and feel in method '" + methodName + "': \n" + 
				e.getLocalizedMessage();
		log.error(message);
		JOptionPane.showMessageDialog(Controller.getMainFrame(), message, "Error", 
				JOptionPane.ERROR_MESSAGE);
	}

	public static void createPropertiesFileIOException(String methodName, IOException e) {
		String message = "Unable to create the configuration file in method '" + methodName + "': \n" + 
				e.getLocalizedMessage();
		log.error(message);
	}

	static void applicationPathDecodingException(String methodName, String applicationPath, 
			UnsupportedEncodingException ex) {
		String message = "Unable to set the application path in method '" + methodName + "'\n" +
				"Path: '" + applicationPath + "'\nError message: " + ex.getLocalizedMessage() + 
				"\nUsing user's home dir.";
		log.error(message);
		JOptionPane.showMessageDialog(Controller.getMainFrame(), message, "Error", 
					JOptionPane.ERROR_MESSAGE);
	}
	
	/**********************************************************************************************
	 * Backup
	 **********************************************************************************************/
		
	public static void databaseBackupError(String methodName, SQLException ex, String backupPath) {
		String logMessage = "Unable to backup database with method '" + methodName + "'\n" + 
				"Path '" + backupPath + "'\nError: " + ex.getLocalizedMessage();
		log.error(logMessage);
		
		String message = "Unable to backup database in path '" + backupPath;
		JOptionPane.showMessageDialog(Controller.getMainFrame(), message, "Error", 
				JOptionPane.ERROR_MESSAGE);
	}

	public static void databaseBackupZipError(String methodName, ZipException ex, String backupPath) {
		String logMessage = "Unable to zip the backed-up database with method '" + methodName + "'\n" + 
				"Path '" + backupPath + "'\nError: " + ex.getLocalizedMessage();
		log.error(logMessage);
		
		String message = "Unable to zip the backed-up database in path '" + backupPath;
		JOptionPane.showMessageDialog(Controller.getMainFrame(), message, "Error", 
				JOptionPane.ERROR_MESSAGE);
	}

	static void restoreIOExceptionError(String methodName, IOException ex, String backupPath) {
		String logMessage = "Unable to restore the database with method '" + methodName + "'\n" + 
				"Path '" + backupPath + "'\nError: " + ex.getLocalizedMessage();
		log.error(logMessage);
		
		String message = "Unable to restore the database from path '" + backupPath;
		JOptionPane.showMessageDialog(Controller.getMainFrame(), message, "Error", 
				JOptionPane.ERROR_MESSAGE);
	}

	public static void databaseRestoreError(String methodName, SQLException ex, String backupPath) {
		String logMessage = "Unable to restore database with method '" + methodName + "'\n" + 
				"Database backup path '" + backupPath + "'\nError: " + ex.getLocalizedMessage();
		log.error(logMessage);
		
		String message = "Unable to restore database from path '" + backupPath;
		JOptionPane.showMessageDialog(Controller.getMainFrame(), message, "Error", 
				JOptionPane.ERROR_MESSAGE);
	}

	public static void databaseRestoreZipError(String methodName, ZipException ex, String backupPath) {
		String logMessage = "Unable to unzip the backed-up database with method '" + methodName + "'\n" + 
				"Path '" + backupPath + "'\nError: " + ex.getLocalizedMessage();
		log.error(logMessage);
		
		String message = "Unable to unzip the backed-up database in path '" + backupPath;
		JOptionPane.showMessageDialog(Controller.getMainFrame(), message, "Error", 
				JOptionPane.ERROR_MESSAGE);
	}
}

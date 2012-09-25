package com.hersis.activitytracker;

import com.hersis.activitytracker.controler.BackupBO;
import com.hersis.activitytracker.controler.ControllerBO;

/**
 *
 * @author Igor Rodriguez <igorrodriguezelvira@gmail.com>
 */
public enum ApplicationProperties {
	/**
	 * The name of the application, as will be shown. 
	 * By default, "ActivityTrackerV2".
	 */
	APPLICATION_NAME("ActivityTrackerV2"),
	
	/**
	 * The path to the application.
	 * Default value defined in ControllerBO.getDefaultApplicationPath().
	 */
	APPLICATION_PATH(ControllerBO.getDefaultApplicationPath()),
	
	/**
	 * A backup period. 
	 * By default, DISABLED.
	 */
	BACKUP_PERIOD(BackupPeriod.DISABLED.toString()), 
	
	/**
	 * The path where to backup the application. 
	 * Default value defined in BackupBo.getDefaultBackupPath().
	 */
	BACKUP_PATH(BackupBO.getDefaultBackupPath()), 
	
	/**
	 * The prefix of the database backup file.
	 * Default value: APPLICATION_NAME + "_"
	 */
	BACKUP_FORMAT_STRING(APPLICATION_NAME + "_"),
	
	/**
	 * The last date when the application was backed-up.
	 * Default value defined in BackupBO.getDefaultBackupDate().
	 */
	LAST_BACKUP_DATE(BackupBO.getDefaultBackupDate()),
	
	/**
	 * The path to the applications properties file. 
	 * Default value defined in ControllerBO.getDefaultPropertiesFilePath().
	 */
	PROPERTIES_FILE(ControllerBO.getDefaultPropertiesFilePath()),
	
	/**
	 * The path to the log properties file.
	 * Default value defined in ControllerBO.getDefaultLogPropertiesFilePath();
	 */
	LOG_PROPERTIES_FILE(ControllerBO.getDefaultLogPropertiesFile()),
	
	/**
	 * The path where the log and log-backup files will be saved.
	 * Default value defined in ControllerBO.getDefaultLogFilePath();
	 */
	LOG_FILE_PATH(ControllerBO.getDefaultLogFilePath()),
	
	/**
	 * The name of the log file that will be created without extension.
	 */
	LOG_NAME("ActivityTracker"),
	
	/**
	 * The maximum size of the log file, in KB.
	 */
	LOG_MAXIMUM_SIZE("250");
	
	private String defaultValue;
	
	private ApplicationProperties(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	
	public String getDefaultValue() {
		return defaultValue;
	}
}

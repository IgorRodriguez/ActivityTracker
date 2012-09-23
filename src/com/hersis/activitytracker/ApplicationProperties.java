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
	 * The last date when the application was backed-up.
	 * Default value defined in BackupBO.getDefaultBackupDate().
	 */
	LAST_BACKUP_DATE(BackupBO.getDefaultBackupDate()),
	
	/**
	 * The path to the applications properties file. 
	 * Default value defined in ControllerBO.getDefaultPropertiesFilePath().
	 */
	PROPERTIES_FILE_PATH(ControllerBO.getDefaultPropertiesFilePath()),
	
	/**
	 * The path to the log properties file.
	 * Default value defined in ControllerBO.getDefaultLogPropertiesFilePath();
	 */
	LOG_PROPERTIES_FILE_PATH(ControllerBO.getDefaultLogPropertiesFilePath()),
	
	/**
	 * The path to the file where the log of the application will be saved.
	 * Default value defined in ControllerBO.getDefaultLogFilePath();
	 */
	LOG_FILE_PATH(ControllerBO.getDefaultLogFilePath());
	
	private String defaultValue;
	
	private ApplicationProperties(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	
	public String getDefaultValue() {
		return defaultValue;
	}
}

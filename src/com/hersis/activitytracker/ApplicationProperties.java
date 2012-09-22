package com.hersis.activitytracker;

import com.hersis.activitytracker.controler.BackupBO;
import com.hersis.activitytracker.controler.ControllerBO;

/**
 *
 * @author Igor Rodriguez <igorrodriguezelvira@gmail.com>
 */
public enum ApplicationProperties {
	APPLICATION_NAME("ActivityTrackerV2"),
	APPLICATION_PATH(ControllerBO.getDefaultApplicationPath()),
	BACKUP_PERIOD(BackupPeriod.DISABLED.toString()), 
	BACKUP_PATH(BackupBO.getDefaultBackupPath()), 
	LAST_BACKUP_DATE(BackupBO.getDefaultBackupDate()),
	PROPERTIES_FILE_PATH(ControllerBO.getDefaultPropertiesFilePath()),
	LOG_PROPERTIES_FILE_PATH("log4j.properties"),
	LOG_FILE_PATH(ControllerBO.getDefaultLogFilePath());
	
	private String defaultValue;
	
	private ApplicationProperties(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	
	public String getDefaultValue() {
		return defaultValue;
	}
}

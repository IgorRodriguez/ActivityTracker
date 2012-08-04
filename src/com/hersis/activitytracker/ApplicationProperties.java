package com.hersis.activitytracker;

import com.hersis.activitytracker.controler.BackupBO;
import java.io.File;

/**
 *
 * @author Igor Rodriguez <igorrodriguezelvira@gmail.com>
 */
public enum ApplicationProperties {
	APPLICATION_NAME("ActivityTrackerV2"),
	APPLICATION_PATH(new File(
			ActivityTrackerMain.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParentFile().getPath()),
	BACKUP_PERIOD(BackupPeriod.DISABLED.toString()), 
	BACKUP_PATH(BackupBO.getDefaultBackupPath()), 
	LAST_BACKUP_DATE(BackupBO.getDefaultBackupDate());
	
	private String defaultValue;
	
	private ApplicationProperties(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	
	public String getDefaultValue() {
		return defaultValue;
	}
}

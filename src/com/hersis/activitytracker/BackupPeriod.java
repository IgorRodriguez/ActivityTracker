package com.hersis.activitytracker;

/**
 *
 * @author Igor Rodriguez <igorrodriguezelvira@gmail.com>
 */
public enum BackupPeriod {
	DISABLED("Disabled"),
	DIARY("Diary"),
	WEEKLY("Weekly"),
	FORTNIGHTLY("Fortnightly"),
	MONTHLY("Monthly"),
	BIMONTHLY("Bimonthly"),
	ALWAYS("Always");

	private final String name;
	
	private BackupPeriod(String name) {
		this.name = name;
	} 

	@Override
	public String toString() {
		return name;
	}
}

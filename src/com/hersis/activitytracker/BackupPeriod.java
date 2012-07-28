package com.hersis.activitytracker;

/**
 *
 * @author Igor Rodriguez <igorrodriguezelvira@gmail.com>
 */
public enum BackupPeriod {
	DISABLED("Disabled", 0, 0),
	DIARY("Diary", 1, 0),
	WEEKLY("Weekly", 7, 0),
	FORTNIGHTLY("Fortnightly", 15, 0),
	MONTHLY("Monthly", 0, 1),
	BIMONTHLY("Bimonthly", 0, 2),
	ALWAYS("Always", 0, 0);

	private final String name;
	private int days;
	private int months;
	
	private BackupPeriod(String name, int days, int months) {
		this.name = name;
		this.days = days;
		this.months = months;
	} 

	@Override
	public String toString() {
		return name;
	}
	
	public int getDays() {
		return days;
	}
	
	public int getMonths() {
		return months;
	}
}

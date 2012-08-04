package com.hersis.activitytracker.controler;

import com.hersis.activitytracker.ApplicationProperties;
import com.hersis.activitytracker.BackupPeriod;
import com.hersis.activitytracker.view.AlertMessages;
import com.hersis.activitytracker.view.BackupConfigDialog;
import com.hersis.activitytracker.view.BackupDialog;
import com.hersis.activitytracker.view.ProgressBarDialog;
import com.hersis.activitytracker.view.aux.BackupFileFilter;
import java.awt.Component;
import java.io.File;
import java.io.FileFilter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Observable;
import java.util.Observer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Igor Rodriguez <igorrodriguezelvira@gmail.com>
 */
public class BackupBO implements Observer {
	private final BackupDialog backupDialog;
	private final BackupConfigDialog backupConfigDialog;
	private static final DateFormat backupDateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
	private static final Pattern BACKUP_DATE_PATTERN = 
			Pattern.compile("(\\d{4})-(\\d{2})-(\\d{2})_(\\d{2}):(\\d{2}):(\\d{2})");
	public static final String BACKUP_FORMAT_STRING = Controller.APPLICATION_NAME + "_";

	BackupBO(BackupDialog backupDialog, BackupConfigDialog backupConfigDialog) {
		this.backupDialog = backupDialog;
		this.backupConfigDialog = backupConfigDialog;
		if (mustPerformBackup()) startBackup(Controller.getMainFrame());
	}
	
	public static String getDefaultBackupDate() {
		Calendar defaultDate = Calendar.getInstance();
		defaultDate.setTimeInMillis(0);
		return backupDateFormat.format(defaultDate.getTime());
	}
	
	public static String getDefaultBackupPath() {
		return "";
	}
	
	/**
	 * Indicates if the time since the last performed backup is greater than the actual backup 
	 * period indicated and, so, the backup must be executed.
	 * @return True if the last backup was performed before the time expressed in the current backup 
	 * period.
	 */
	private boolean mustPerformBackup() {
		long lastBackupDate = 0;
		Calendar backupDate = getLastBackupDate();
		if (backupDate != null)  {
			 lastBackupDate = backupDate.getTimeInMillis();
		}
		long now = Calendar.getInstance().getTimeInMillis();
		BackupPeriod backupPeriod = getBackupPeriod();
		
		if (BackupPeriod.ALWAYS.equals(backupPeriod)) {
			return true;
		} else if (!BackupPeriod.DISABLED.equals((backupPeriod))) {
			double millisecondsSinceBackup = now - lastBackupDate;
			if (millisecondsSinceBackup > 0) {
				double months = millisecondsSinceBackup / (30 * 24 * 60 * 60 * 1000D);
				double days;
				if ((int) months != 0) {
					days = (millisecondsSinceBackup % (int) months) / (24 * 60 * 60 * 1000);
				} else {
					days = millisecondsSinceBackup / (24 * 60 * 60 * 1000);
				}
				if (months >= backupPeriod.getMonths() && days >= backupPeriod.getDays()) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Returns the date in which the backup was performed last time.
	 * @return The date of the last performed backup.
	 */
	public static Calendar getLastBackupDate() {
		Calendar backupDate = Calendar.getInstance();
		backupDate.setTimeInMillis(0); // Default value
		String dateString = Controller.getPropertie(ApplicationProperties.LAST_BACKUP_DATE);
		
		if (dateString != null && !"".equals(dateString)) {	
			Matcher matcher = BACKUP_DATE_PATTERN.matcher(dateString);
			
			if (matcher.find()) {
				backupDate = Calendar.getInstance();
				backupDate.clear();
				
				backupDate.set(Calendar.YEAR, Integer.parseInt(matcher.group(1)));
				backupDate.set(Calendar.MONTH, Integer.parseInt(matcher.group(2)) - 1);
				backupDate.set(Calendar.DAY_OF_MONTH, Integer.parseInt(matcher.group(3)));
				backupDate.set(Calendar.HOUR, Integer.parseInt(matcher.group(4)));
				backupDate.set(Calendar.MINUTE, Integer.parseInt(matcher.group(5)));
				backupDate.set(Calendar.SECOND, Integer.parseInt(matcher.group(6)));
			}
		}
		return backupDate;
	}
	
	/**
	 * Returns the current backup period of the application.
	 * @return The actual backup period.
	 */
	private BackupPeriod getBackupPeriod() {
		BackupPeriod backupPeriod = BackupPeriod.DISABLED; // Set backupPeriod to default: DISABLED.
		String backupPeriodString = Controller.getPropertie(ApplicationProperties.BACKUP_PERIOD);
		boolean backupPeriodNull = false;
		
		if (backupPeriodString != null && !"".equals(backupPeriodString)) {
			backupPeriodString = backupPeriodString.toUpperCase();
			try {
				backupPeriod = BackupPeriod.valueOf(backupPeriodString);
			} catch (IllegalArgumentException ex) {
				backupPeriodNull = true;
			}
		} 
		if (backupPeriodNull) {	
			Controller.setPropertie(ApplicationProperties.BACKUP_PERIOD, backupPeriod.toString());
			AlertMessages.backupPeriodIllegalArgumentException("BackupBO.getBackupPeriod()", 
					backupPeriodString);
		}
		return backupPeriod;
	}

	void closeBackupWindow() {
		backupDialog.setVisible(false);
	}
	
	void showBackupWindow() {
		backupDialog.setVisible(true);
	}

	void showBackupConfigWindow() {
		backupConfigDialog.loadBackupValues();
		backupConfigDialog.setVisible(true);
	}

	/**
	 * Starts the backup of the application to the path specified in the application's properties.
	 * @param parentWindow The parent window for the progress dialog that will be displayed.
	 */
	final void startBackup(Component parentWindow) {
		final String destinationRoot = Controller.getPropertie(ApplicationProperties.BACKUP_PATH);
		
		if (destinationRoot != null && !"".equals(destinationRoot.trim())) {
			ProgressBarDialog backupProgressDialog = ProgressBarDialog.getInstance(parentWindow);
			RunDatabaseBackup runBackup = new RunDatabaseBackup(backupProgressDialog, 
					destinationRoot, backupDateFormat, BACKUP_FORMAT_STRING);
			runBackup.execute();
			
			backupProgressDialog.setTaskTitle("Backing up application...");
			backupProgressDialog.setVisible(true);	// No need to set invisible, RunDatabaseBackup will do it.
		} else {
			AlertMessages.backupPathNull();
		}
	}

	/**
	 * Returns all the available backups in the specified path that match with the application's filter.
	 * @param filePath The path in which to find the backups.
	 * @return An array of File containing all the available backups in the path.
	 */
	static File[] getAvailableBackups(File filePath) {
		FileFilter fileFilter = new BackupFileFilter();
		File [] fileList = null;
		
		if (filePath.exists()) {
			fileList = filePath.listFiles(fileFilter);
		}
		return fileList;
	}

	/**
	 * Starts the restoration of the backup specified in <code>path</code>.
	 * @param parentWindow The parent window for the progress dialog that will be displayed.
	 * @param path The path from which to get the database backup.
	 */
	static void restoreBackup(Component parentWindow, String path) {
		ProgressBarDialog backupProgressDialog = ProgressBarDialog.getInstance(parentWindow);
		RunDatabaseRestore runRestore = new RunDatabaseRestore(backupProgressDialog, path);
		runRestore.execute();

		backupProgressDialog.setTaskTitle("Restoring application...");
		backupProgressDialog.setVisible(true);	// No need to set invisible, RunDatabaseRestore will do it.
	}

	@Override
	public void update(Observable o, Object arg) {
		if (mustPerformBackup()) startBackup(backupDialog);
	}
}

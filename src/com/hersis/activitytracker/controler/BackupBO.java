package com.hersis.activitytracker.controler;

import com.hersis.activitytracker.ApplicationProperties;
import com.hersis.activitytracker.BackupPeriod;
import com.hersis.activitytracker.model.Dao;
import com.hersis.activitytracker.view.AlertMessages;
import com.hersis.activitytracker.view.BackupConfigDialog;
import com.hersis.activitytracker.view.BackupDialog;
import com.hersis.activitytracker.view.ProgressBarDialog;
import com.hersis.activitytracker.view.aux.BackupFileFilter;
import com.hersis.activitytracker.view.aux.ProgressBarPanel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.*;

/**
 *
 * @author Igor Rodriguez <igorrodriguezelvira@gmail.com>
 */
public class BackupBO extends SwingWorker<Object, Object> {
	private final BackupDialog backupDialog;
	private final BackupConfigDialog backupConfigDialog;
	private final DateFormat backupDateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
	private final Pattern backupDatePattern = 
			Pattern.compile("(\\d{4})-(\\d{2})-(\\d{2})_(\\d{2}):(\\d{2}):(\\d{2})");
	public static final String BACKUP_FORMAT_STRING = Controller.APPLICATION_NAME + "_";

	BackupBO(BackupDialog backupDialog, BackupConfigDialog backupConfigDialog) {
		this.backupDialog = backupDialog;
		this.backupConfigDialog = backupConfigDialog;
		if (mustPerformBackup()) startBackup();
	}
	
	private boolean mustPerformBackup() {
		long lastBackupDate = getLastBackupDate().getTimeInMillis();
		long now = Calendar.getInstance().getTimeInMillis();
		BackupPeriod backupPeriod = getBackupPeriod();
		
		if (BackupPeriod.ALWAYS.equals(backupPeriod)) {
			return true;
		} else if (!BackupPeriod.DISABLED.equals((backupPeriod))) {
			long millisecondsSinceBackup = now - lastBackupDate;

			if (millisecondsSinceBackup > 0) {
				int months = (int) millisecondsSinceBackup / (30 * 24 * 60 * 60 * 1000);
				int days = (int) (millisecondsSinceBackup % months) / (24 * 60 * 60 * 1000);
				if (months >= backupPeriod.getMonths() && days >= backupPeriod.getDays()) {
					return true;
				}
			}
		}
		return false;
	}
	
	private Calendar getLastBackupDate() {
		Calendar backupDate = null;
		
		String dateString = Controller.getPropertie(ApplicationProperties.LAST_BACKUP_DATE);
		
		if (dateString != null && !"".equals(dateString)) {	
			Matcher matcher = backupDatePattern.matcher(dateString);
			
			if (matcher.find()) {
				backupDate = Calendar.getInstance();
				backupDate.clear();
				
				backupDate.set(Calendar.YEAR, Integer.parseInt(matcher.group(1)));
				backupDate.set(Calendar.MONTH, Integer.parseInt(matcher.group(2)));
				backupDate.set(Calendar.DAY_OF_MONTH, Integer.parseInt(matcher.group(3)));
				backupDate.set(Calendar.HOUR, Integer.parseInt(matcher.group(4)));
				backupDate.set(Calendar.MINUTE, Integer.parseInt(matcher.group(5)));
				backupDate.set(Calendar.SECOND, Integer.parseInt(matcher.group(6)));
			}
		}
		return backupDate;
	}
	
	private BackupPeriod getBackupPeriod() {
		BackupPeriod backupPeriod = null;
		String backupPeriodString = Controller.getPropertie(ApplicationProperties.BACKUP_PERIOD);
		boolean backupPeriodNull = false;
		
		if (backupPeriodString != null) {
			backupPeriodString = backupPeriodString.toUpperCase();
			try {
				backupPeriod = BackupPeriod.valueOf(backupPeriodString);
			} catch (IllegalArgumentException ex) {
				backupPeriodNull = true;
			}
		} 
		if (backupPeriodNull) {	// Set backupPeriod to default: DISABLED.
			backupPeriod = BackupPeriod.DISABLED;
			Controller.setPropertie(ApplicationProperties.BACKUP_PERIOD, backupPeriod.toString());
			AlertMessages.backupPeriodIllegalArgumentException("BackupBO.getLastBackupDate()", 
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

	final int startBackup() {
		//TODO remove return value
		int backupResult = -1;
		final String destinationRoot = Controller.getPropertie(ApplicationProperties.BACKUP_PATH);
		
		if (destinationRoot != null && !"".equals(destinationRoot.trim())) {
			JDialog backupProgressDialog = new JDialog(Controller.getMainFrame(), true);
			RunDatabaseBackup runBackup = new RunDatabaseBackup(backupProgressDialog, 
					destinationRoot, backupDateFormat, BACKUP_FORMAT_STRING);
			runBackup.execute();
			
			
			ProgressBarPanel progressBarPanel = new ProgressBarPanel();
			progressBarPanel.setTaskTitle("Backing up application...");
			backupProgressDialog.add(BorderLayout.CENTER, progressBarPanel);
			backupProgressDialog.setUndecorated(true);
			backupProgressDialog.pack();
			backupProgressDialog.setLocationRelativeTo(null);
			backupProgressDialog.setVisible(true);
		} else {
			AlertMessages.backupPathNull();
		}
		
		
		return backupResult;
	}
	
	private int runBackup(String destinationRoot) {
		int backupResult = -1;
		String backupDate = backupDateFormat.format(new Date());
		String path = destinationRoot + File.separatorChar + BACKUP_FORMAT_STRING +
				backupDate;

		// Show a progress bar of the task.
		
//		Thread progressThread = new Thread() {
//			@Override
//			public void run() {
//				backupProgressDialog.setTaskTitle("Backing up application...");
//				backupProgressDialog.setVisible(true);
//				backupProgressDialog.repaint();
//			}
//		};
//		progressThread.start();

		try {
			backupResult = Dao.executeBackup(path);
			Controller.setPropertie(ApplicationProperties.LAST_BACKUP_DATE, backupDate);
		} catch (SQLException ex) {
			ErrorMessages.sqlExceptionError("startBackup()", ex);
		} catch (ClassNotFoundException ex) {
			ErrorMessages.classNotFoundError("startBackup()", ex);
		}

//		if (!progressThread.isAlive()) progressThread.interrupt();
		
		//AlertMessages.backupSuccessful(path);
		return backupResult;
	}

	static File[] getAvailableBackups(File filePath) {
		FileFilter fileFilter = new BackupFileFilter();
		File [] fileList = null;
		
		if (filePath.exists()) {
			fileList = filePath.listFiles(fileFilter);
		}
		return fileList;
	}

	static void restoreBackup(String path) {
		try (Dao dao = Dao.getInstance()) {
			dao.restoreBackup(path);
		} catch (IOException ex) {
			ErrorMessages.restoreIOExceptionError("BackupBo.restoreBackup()", ex, path);
		} catch (SQLException ex) {
			ErrorMessages.sqlExceptionError("BackupBo.restoreBackup()", ex);
		} catch (ClassNotFoundException ex) {
			ErrorMessages.classNotFoundError("BackupBo.restoreBackup()", ex);
		}
	}

	@Override
	protected Object doInBackground() throws Exception {
		throw new UnsupportedOperationException("Not supported yet.");
	}
	
}

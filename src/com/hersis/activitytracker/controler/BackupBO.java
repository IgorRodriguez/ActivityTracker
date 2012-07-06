package com.hersis.activitytracker.controler;

import com.hersis.activitytracker.model.Dao;
import com.hersis.activitytracker.view.BackupConfigDialog;
import com.hersis.activitytracker.view.BackupDialog;
import com.hersis.activitytracker.view.aux.BackupFileFilter;
import java.io.File;
import java.io.FileFilter;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

/**
 *
 * @author Igor Rodriguez <igorrodriguezelvira@gmail.com>
 */
public class BackupBO {
	private final BackupDialog backupDialog;
	private final BackupConfigDialog backupConfigDialog;
	private final DateFormat backupDateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
	public static final String BACKUP_FORMAT_STRING = Controller.APPLICATION_NAME + "_";

	BackupBO(BackupDialog backupDialog, BackupConfigDialog backupConfigDialog) {
		this.backupDialog = backupDialog;
		this.backupConfigDialog = backupConfigDialog;
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

	int startBackup() {
		int backupResult = -1;
		String destinationRoot = Controller.getPropertie(Controller.BACKUP_PATH_PROPERTIE);
		
		if (destinationRoot != null && !"".equals(destinationRoot.trim())) {
			String backupDate = backupDateFormat.format(new Date());
			String path = destinationRoot + File.separatorChar + BACKUP_FORMAT_STRING +
					backupDate;
			try {
				backupResult = Dao.executeBackup(path);
				Controller.setPropertie(Controller.LAST_BACKUP_DATE, backupDate);
			} catch (SQLException ex) {
				ErrorMessages.sqlExceptionError("startBackup()", ex);
			} catch (ClassNotFoundException ex) {
				ErrorMessages.classNotFoundError("startBackup()", ex);
			}
		}
		
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
	
}

package com.hersis.activitytracker.controler;

import com.hersis.activitytracker.view.BackupConfigDialog;
import com.hersis.activitytracker.view.BackupDialog;

/**
 *
 * @author Igor Rodriguez <igorrodriguezelvira@gmail.com>
 */
public class BackupBO {
	private final BackupDialog backupDialog;
	private final BackupConfigDialog backupConfigDialog;

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
		backupConfigDialog.setVisible(true);
	}
	
}

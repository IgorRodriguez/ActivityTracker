/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hersis.activitytracker.controler;

import com.hersis.activitytracker.ApplicationProperties;
import com.hersis.activitytracker.model.Dao;
import java.io.File;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.Date;
import javax.swing.JDialog;
import javax.swing.SwingWorker;

/**
 *
 * @author Igor Rodriguez <igorrodriguezelvira@gmail.com>
 */
public class RunDatabaseBackup extends SwingWorker<Void, Void> {
	private final String destinationRoot;
	private final DateFormat backupDateFormat;
	private final String backupFormatString;
	private final JDialog progressBarDialog;

	public RunDatabaseBackup(JDialog progressBarDialog, String destinationRoot, final DateFormat backupDateFormat, 
			final String backupFormatString) {
		this.progressBarDialog = progressBarDialog;
		this.destinationRoot = destinationRoot;
		this.backupDateFormat = backupDateFormat;
		this.backupFormatString = backupFormatString;
	}
	@Override
	protected Void doInBackground() throws Exception {
		String backupDate = backupDateFormat.format(new Date());
		String path = destinationRoot + File.separatorChar + backupFormatString +
				backupDate;
		try {
			Dao.executeBackup(path);
			Controller.setPropertie(ApplicationProperties.LAST_BACKUP_DATE, backupDate);
		} catch (SQLException ex) {
			ErrorMessages.sqlExceptionError("startBackup()", ex);
		} catch (ClassNotFoundException ex) {
			ErrorMessages.classNotFoundError("startBackup()", ex);
		}
		
		return null;
	}
	
	@Override
	protected void done () {
		progressBarDialog.setVisible(false);
	}
	
}

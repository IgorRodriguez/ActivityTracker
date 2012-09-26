package com.hersis.activitytracker.controller;

import com.hersis.activitytracker.model.Dao;
import java.io.IOException;
import java.sql.SQLException;
import javax.swing.JDialog;
import javax.swing.SwingWorker;

/**
 *
 * @author Igor Rodriguez <igorrodriguezelvira@gmail.com>
 */
public class RunDatabaseRestore extends SwingWorker<Void, Void> {
	private final JDialog progressBarDialog;
	private final String sourcePath;

	public RunDatabaseRestore(JDialog progressBarDialog, String sourcePath) {
		this.progressBarDialog = progressBarDialog;
		this.sourcePath = sourcePath;
	}
	
	@Override
	protected Void doInBackground() throws Exception {
		try (Dao dao = Dao.getInstance()) {
			dao.restoreBackup(sourcePath);
		} catch (IOException ex) {
			ErrorMessages.restoreIOExceptionError("RunDatabaseRestore.doInBackground()", ex, sourcePath);
		} catch (SQLException ex) {
			ErrorMessages.sqlExceptionError("RunDatabaseRestore.doInBackground()", ex);
		} catch (ClassNotFoundException ex) {
			ErrorMessages.classNotFoundError("RunDatabaseRestore.doInBackground()", ex);
		}
		
		return null;
	}
	
	@Override
	protected void done () {
		progressBarDialog.setVisible(false);
	}	
}

package com.hersis.activitytracker.view;

import com.hersis.activitytracker.controller.Controller;
import com.hersis.activitytracker.view.util.BackupSwingFileFilter;
import com.hersis.activitytracker.view.util.Locatable;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author Igor Rodriguez <igorrodriguezelvira@gmail.com>
 */
class SharedFileChooser implements Locatable {
	private final JFileChooser fileChooser;
	/**
	 * Indicates that the accept button of the file chooser has been pressed.
	 */
	public static final int APPROVE_OPTION = JFileChooser.APPROVE_OPTION;
	
	private SharedFileChooser() {
		fileChooser = new JFileChooser();
	}
	
	public static SharedFileChooser getInstance(final String name) {
		final SharedFileChooser sharedFileChooser = new SharedFileChooser();
		Controller.registerLocatableWindow(sharedFileChooser, name);		
		
		return sharedFileChooser;
	}
	
	public int showDirectoryChooser(Component parentComponent, String acceptButtonString) {
		// JFileChooser must show only Directories
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setFileFilter(new FileFilter() {
			@Override
            public boolean accept (File file) {
                if(file.isDirectory()) {
                    return true;
                }
                return false;
            }
			@Override
            public String getDescription() {
                return "Directories";
            }
        });
		
		return fileChooser.showDialog(parentComponent, acceptButtonString);
	}
	
	/**
	 * Shows a dialog that will only show directories and application's database backup files.
	 * @param parentComponent The parent component for the dialog.
	 * @param acceptButtonString The caption on the Accept button of the dialog.
	 * @return The action performed by the user, such as accept, cancel or close the dialog.
	 */
	public int showBackupsChooser(Component parentComponent, String acceptButtonString) {
		// JFileChooser must show only backups
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        fileChooser.setFileFilter(new BackupSwingFileFilter());
		
		return fileChooser.showDialog(parentComponent, acceptButtonString);
	}	
	
	public File getSelectedFile() {
		return fileChooser.getSelectedFile();
	}
	
	public void setSelectedFile(String filePath) {
		File file = new File(filePath);
		if (file.exists()) {
			fileChooser.setSelectedFile(file);
		}
	}

	@Override
	public void setBounds(final Rectangle position) {
		fileChooser.setBounds(position);
	}

	@Override
	public Rectangle getBounds() {
		return fileChooser.getBounds();
	}

	@Override
	public String getName() {
		return fileChooser.getName();
	}

	@Override
	public void setName(final String name) {
		fileChooser.setName(name);
	}

	@Override
	public Dimension getPreferredSize() {
		return fileChooser.getPreferredSize();
	}
}

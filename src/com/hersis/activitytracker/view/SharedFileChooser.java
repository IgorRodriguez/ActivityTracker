package com.hersis.activitytracker.view;

import com.hersis.activitytracker.view.util.BackupFileFilter;
import com.hersis.activitytracker.view.util.BackupSwingFileFilter;
import java.awt.Component;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author Igor Rodriguez <igorrodriguezelvira@gmail.com>
 */
class SharedFileChooser {
	private static final JFileChooser fileChooser = new JFileChooser();
	/**
	 * Indicates that the accept button of the file chooser has been pressed.
	 */
	public static final int APPROVE_OPTION = JFileChooser.APPROVE_OPTION;
	
	public static int showDirectoryChooser(Component parentComponent, String acceptButtonString) {
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
	
	public static int showBackupsChooser(Component parentComponent, String acceptButtonString) {
		// JFileChooser must show only backups
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        fileChooser.setFileFilter(new BackupSwingFileFilter());
		
		return fileChooser.showDialog(parentComponent, acceptButtonString);
	}	
	
	public static File getSelectedFile() {
		return fileChooser.getSelectedFile();
	}
	
	public static void setSelectedFile(String filePath) {
		File file = new File(filePath);
		if (file.exists()) 
			fileChooser.setSelectedFile(file);
	}
}

package com.hersis.activitytracker.view.aux;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author Igor Rodriguez <igorrodriguezelvira@gmail.com>
 */
public class BackupSwingFileFilter extends FileFilter {
	private static final String BACKUP_REGEX = ".*\\.zip";
	private static final Pattern BACKUP_PATTERN = Pattern.compile(BACKUP_REGEX);
	
	@Override
	public boolean accept(File f) {
		Matcher matcher = BACKUP_PATTERN.matcher(f.getName());
		if (f.isDirectory() || matcher.find()) {
			return true;
		}
		return false;
	}

	@Override
	public String getDescription() {
		return "Zip files";
	}
}

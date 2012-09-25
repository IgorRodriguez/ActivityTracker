package com.hersis.activitytracker.view.aux;

import com.hersis.activitytracker.ApplicationProperties;
import java.io.File;
import java.io.FileFilter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Igor Rodriguez <igorrodriguezelvira@gmail.com>
 */
public class BackupFileFilter implements FileFilter {
	private static final String BACKUP_REGEX = 
			ApplicationProperties.BACKUP_FORMAT_STRING.getDefaultValue() + "*";
	private static final Pattern BACKUP_PATTERN = Pattern.compile(BACKUP_REGEX);
	
	@Override
	public boolean accept(File f) {
		Matcher matcher = BACKUP_PATTERN.matcher(f.getName());
		if (matcher.find()) {
			return true;
		}
		return false;
	}	
}

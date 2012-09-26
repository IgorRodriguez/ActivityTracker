package com.hersis.activitytracker.controller.util;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.StatusPrinter;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Allows logging to both console and file.
 * @author Igor Rodriguez <igorrodriguezelvira@gmail.com>
 */
public class LogToFile {
	private final static Logger log = LoggerFactory.getLogger("controller.util.LogToFile");
	private static LogToFile logToFile;
	private final String logPropertiesFile;
	private final String logFileName;
	private final String logBackupFileName;
	private final String logFilePath;
	private final String maximumSize;
	
	private LogToFile(final String logPropertiesFile,
						final String logFilePath,
						final String logName,
						final String maximumSize) {
		this.logPropertiesFile = logPropertiesFile;
		this.logFilePath = logFilePath;
		this.logFileName = logName + ".log";
		this.logBackupFileName = logName + ".%i.log.zip";
		this.maximumSize = maximumSize;
		
		configureLog();
	}
	
	public static void configure(final String logPropertiesFile,
									final String logFilePath,
									final String logName,
									final String maximumSize) {
		if (logToFile == null) {
			logToFile = new LogToFile(logPropertiesFile, logFilePath, logName, maximumSize);
		}
	}
		
	/**
	 * Configures the log with the contents of the log's properties file.
	 */
	private void configureLog() {
		// Get the file of log properties.
		final File propertiesFile = getLogPropertiesFile();
		
		// Configure the logger.
		final LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
		final JoranConfigurator configurator = new JoranConfigurator();
		configurator.setContext(context);
		// Call context.reset() to clear any previous configuration, e.g. default 
		// configuration. For multi-step configuration, omit calling context.reset().
		context.reset(); 
		try {
			configurator.doConfigure(propertiesFile);
		} catch (JoranException ex) {
			log.warn("Problem while configuring the logger. \nError: {}", ex.getLocalizedMessage());
		}
		
		StatusPrinter.printInCaseOfErrorsOrWarnings(context);
	}
	
	/**
	 * Returns the file containing the log properties. If it doesn't exist, it's created with the 
	 * default values.
	 * For multi-system reasons, the log file path will be always overwritten to the value in 
	 * <code>ApplicationProperties.LOG_FILE_PATH.getDefaultValue()</code>
	 * @return The file containing the log properties.
	 */
	private File getLogPropertiesFile() {
		final File propertiesFile = new File(logPropertiesFile);
		
		final String fileContent;
		if (!propertiesFile.exists()) {
			fileContent = getDefaultLogPropertiesFileContent();
		} else {
			fileContent = getUpdatedLogPropertiesFileContent();
		}
		
		createLogPropertiesFile(propertiesFile, fileContent);
		
		return propertiesFile;
	}
	
	/**
	 * Returns the default content of the log properties file.
	 * @return The content of the file.
	 */
	private String getDefaultLogPropertiesFileContent() {
		final String logFile = (new File(logFilePath, logFileName)).getPath();
		final String backupLogFile = (new File(logFilePath, logBackupFileName)).getPath();
		
		final String fileContent = 
			"<configuration>\n" + 
			"	<appender name=\"FILE\" class=\"ch.qos.logback.core.rolling.RollingFileAppender\">\n" + 
			"		<file>" + logFile + "</file>\n" + 
			"		<rollingPolicy class=\"ch.qos.logback.core.rolling.FixedWindowRollingPolicy\">\n" +
			"			<fileNamePattern>" + backupLogFile + "</fileNamePattern>\n" +
			"			<minIndex>1</minIndex>\n" +
			"			<maxIndex>2</maxIndex>\n" +
			"		</rollingPolicy>\n" + 
			"		<triggeringPolicy class=\"ch.qos.logback.core.rolling.SizeBasedTriggeringPolicy\">\n" +
			"			<maxFileSize>" + maximumSize + "KB</maxFileSize>\n" +
			"		</triggeringPolicy>\n" +
			"		<encoder>\n" + 
			"			<pattern>%date %level [%thread] %logger{10} [%file:%line] %msg%n</pattern>\n" + 
			"		</encoder>\n" + 
			"	</appender>\n" + 
			"\n" + 
			"	<appender name=\"STDOUT\" class=\"ch.qos.logback.core.ConsoleAppender\">\n" + 
			"		<encoder>\n" + 
			"			<pattern>%level - %msg%n</pattern>\n" + 
			"		</encoder>\n" + 
			"	</appender>\n" + 
			"\n" + 
			"	<root level=\"debug\">\n" + 
			"		<appender-ref ref=\"FILE\" />\n" + 
			"		<appender-ref ref=\"STDOUT\" />\n" + 
			"	 </root>\n" + 
			"</configuration>";
		
		return fileContent;
	}

	/**
	 * Finds the paths of both the log and log backup files in the log properties file stored in the 
	 * instance variable <code>logPropertiesFile</code> and replaces them with the path given by the 
	 * instance variable <code>logFilePath</code>.
	 * @return The content of the file with updated values for both file paths, null if nothing is 
	 * replaced.
	 */
	private String getUpdatedLogPropertiesFileContent() {
		final File propertiesFile = new File(logPropertiesFile);
		final String logRegex = ".*<file>(.*?)</file>.*";
		final String logBackupRegex = ".*<fileNamePattern>(.*?)</fileNamePattern>.*";
		final String logPath = (new File(logFilePath, logFileName)).getPath();
		final String logBackupPath = (new File(logFilePath, logBackupFileName)).getPath();
		
		String fileContent = "";
		try {
			fileContent = new Scanner(propertiesFile, "UTF-8").useDelimiter("\\A").next();
		} catch (FileNotFoundException ex) {
			log.error("Unable to open the logging configuration of the application.\nError: {}", 
					ex.getLocalizedMessage());
		}
		
		String changedContent = fileContent;
		changedContent = replaceInString(logRegex, changedContent, logPath);
		changedContent = replaceInString(logBackupRegex, changedContent, logBackupPath);
		
		// If changedContent equals fileContent, the file doesn't need to be overwritten, so we
		// return null.
		if (changedContent != null && !changedContent.equals(fileContent)) {
			return changedContent;
		} else {
			return null;
		}
	}
	
	/**
	 * Replaces the matches of <code>regex</code> parameter expression in the source string with the 
	 * value of the <code>replacement</code> parameter.
	 * @param regex A valid regular expression that could match the text to be replaced.
	 * @param srcString The source string in witch to find for matches.
	 * @param replacement The value to replace each match of <code>regex</code> parameter.
	 * @return The string with all the matches replaced. Returns null if <code>srcString</code> 
	 * is null.
	 */
	private String replaceInString(final String regex, 
			final String srcString, 
			final String replacement) {
		// Exit
		if (srcString == null || "".equals(srcString)) { return null; }
		// Replace all found files with the value of 
		// <code>ApplicationProperties.LOG_FILE_PATH.getDefaultValue()</code>
		final Pattern pattern = Pattern.compile(regex);
		final Matcher matcher = pattern.matcher(srcString);
		String fileContent = srcString;
		
		while (matcher.find()) {
			fileContent = fileContent.replace(matcher.group(1), replacement);
		}
		
		return fileContent;
	}

	/**
	 * Created the file of log properties.
	 * @param logPropertiesFile The file where to create the physical file.
	 * @param fileContent The content of the file to be created.
	 */
	private static void createLogPropertiesFile(final File propertiesFile, final String fileContent) {
		// If fileContent equals null, there's nothing to overwrite.
		if (fileContent == null) { return; }
		
		try {
			propertiesFile.createNewFile();
			try (final BufferedWriter out = new BufferedWriter(new FileWriter(propertiesFile))) {
				out.write(fileContent);
			}
		} catch (IOException ex) {
			log.error("Unable to edit the log configuration file {}\nError: {}", 
					propertiesFile.getPath(), ex.getLocalizedMessage());
		}
	}
}

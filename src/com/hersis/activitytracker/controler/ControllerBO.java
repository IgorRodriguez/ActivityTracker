package com.hersis.activitytracker.controler;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.StatusPrinter;
import com.hersis.activitytracker.ActivityTrackerMain;
import com.hersis.activitytracker.ApplicationProperties;
import com.hersis.activitytracker.images.Icons;
import com.hersis.activitytracker.model.Dao;
import com.hersis.activitytracker.view.AlertMessages;
import java.awt.Component;
import java.io.*;
import java.net.URLDecoder;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Calendar;
import java.util.Locale;
import java.util.Observable;
import java.util.Properties;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Igor Rodriguez <igorrodriguezelvira@gmail.com>
 */
public class ControllerBO extends Observable {
	private static final Logger log = (Logger) LoggerFactory.getLogger("controller.ControllerBO");			
	private static final Icons ICONS = new Icons();
	private static final Properties appProperties = new Properties();

	public static String getDefaultApplicationPath() {
		String path = "";
		
		try {
			path = (new File(ActivityTrackerMain.class.getProtectionDomain().getCodeSource().getLocation().getPath())).getParent();
			return URLDecoder.decode(path, "UTF-8");
		} catch (UnsupportedEncodingException ex) {
			ErrorMessages.applicationPathDecodingException("ControllerBO.getDefaultApplicationPath()", 
					path, ex);
			return System.getProperty("user.home");
		}
	}
	
	public static String getDefaultPropertiesFilePath() {
		return getDefaultApplicationPath() + File.separatorChar + "ActivityTracker.properties";
	}
	
	public static String getDefaultLogFilePath() {
		return getDefaultApplicationPath() + File.separatorChar + "ActivityTracker.log";
	}

	public static String getDefaultLogPropertiesFilePath() {
		return getDefaultApplicationPath() + File.separatorChar + "logback.xml";
	}
		
	/**
	 * Changes some things on the default LookAndFeel, such as alert message's icons.
	 */
	void modifyLookAndFeel() {
		final LookAndFeel laf = UIManager.getLookAndFeel();
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | 
				UnsupportedLookAndFeelException e) {
			log.error("Error trying to set Nimbus as LookAndFeel: {}", e.getLocalizedMessage());
			try {
				UIManager.setLookAndFeel(laf);
			} catch (UnsupportedLookAndFeelException ex) {
				ErrorMessages.unsupportedLookAndFeelError("modifyLookAndFeel()", ex);
			}
		}
		// Change of the default icons for JOptionPanes
		UIManager.put("OptionPane.informationIcon", ICONS.getIcon(Icons.IconValues.INFO));
		UIManager.put("OptionPane.errorIcon", ICONS.getIcon(Icons.IconValues.ERROR));
		UIManager.put("OptionPane.questionIcon", ICONS.getIcon(Icons.IconValues.QUESTION));
		UIManager.put("OptionPane.warningIcon", ICONS.getIcon(Icons.IconValues.WARNING));
	}
	
	void loadProperties() {
		String propertiesFilePath = ApplicationProperties.PROPERTIES_FILE_PATH.getDefaultValue();
		
		if (!new File(propertiesFilePath).exists()) {
			createPropertiesFile();
		}
        try (FileInputStream propFis = new FileInputStream(propertiesFilePath)) {
            appProperties.load(propFis);
			setPropertie(ApplicationProperties.APPLICATION_PATH, 
					ApplicationProperties.APPLICATION_PATH.getDefaultValue());
            log.debug("Properties loaded successfully");
        } catch (IOException ioe) {
			AlertMessages.propertiesLoadIOException(ioe);
        } 
    }
	
	static void loadLogProperties() {
		// Get the file of log properties.
		final File logPropertiesFile = getLogPropertiesFile();
		
		// Configure the logger.
		final LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
		final JoranConfigurator configurator = new JoranConfigurator();
		configurator.setContext(context);
		// Call context.reset() to clear any previous configuration, e.g. default 
		// configuration. For multi-step configuration, omit calling context.reset().
		context.reset(); 
		try {
			configurator.doConfigure(logPropertiesFile);
		} catch (JoranException ex) {
			AlertMessages.logConfigurationAlert(ex);
		}
		
		StatusPrinter.printInCaseOfErrorsOrWarnings(context);
	}
	
	private static void createPropertiesFile() {
		File propertiesFile = new File(ApplicationProperties.PROPERTIES_FILE_PATH.getDefaultValue());
		try {
			propertiesFile.createNewFile();
		} catch (IOException ex) {
			ErrorMessages.createPropertiesFileIOException("createPropertiesFile()", ex);
		}
	}
    
    private static void saveProperties() {
		String propertiesFilePath = ApplicationProperties.PROPERTIES_FILE_PATH.getDefaultValue();
		
		if (!new File(propertiesFilePath).exists()) {
			createPropertiesFile();
		}
        try (FileOutputStream propFos = new FileOutputStream(propertiesFilePath)) {
            appProperties.store(propFos, "Saved with date: ");
            log.debug("Properties saved successfully");
        } catch (IOException ioe) {
			AlertMessages.propertiesSaveIOException(ioe);
        } 
    }
	
	String getPropertie(ApplicationProperties key) {
			final String propertieValue = appProperties.getProperty(key.toString());
			if (propertieValue != null && !"".equals(propertieValue)) {
				return propertieValue;
			}
			// Return default value
			return key.getDefaultValue();
	}
	
	void setPropertie(ApplicationProperties key, String value) {
			appProperties.setProperty(key.toString(), value);
			setChanged();
			notifyObservers();
	}
	
	void removePropertie(ApplicationProperties key) {
		appProperties.remove(key.toString());
		setChanged();
		notifyObservers();
	}
	
	
	/**
	 * Calculates the milliseconds between two dates.
	 * @param startTime The start time of the calculation.
	 * @param endTime The end time for the calculation.
	 * @return The milliseconds between the two dates as endTime - startTime.
	 */
	long calculateDuration(Calendar startTime, Calendar endTime) {
		long duration = 0;
		
		if (startTime != null && endTime != null) {	
			duration = endTime.getTimeInMillis() - startTime.getTimeInMillis();
		}
		
		return duration;
	}
	
	/**
	 * Formats milliseconds to a String as "0:00".
	 * @param time Time in milliseconds to format.
	 * @return A String in format "0:00" or "-0:00";
	 */
    String getDurationString(long time) {
		final long MILLIS_PER_HOUR = 3_600_000;
		final long MILLIS_PER_MINUTE = 60_000;
		final DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols(Locale.getDefault());
		formatSymbols.setDecimalSeparator(':');
		final DecimalFormat doubleFormatter = new DecimalFormat("#######0.00", formatSymbols);
		long hours = time / MILLIS_PER_HOUR;
		long minutes = (time % MILLIS_PER_HOUR) / MILLIS_PER_MINUTE;
		double doubleTime = hours + minutes / 100.0;
		
		return doubleFormatter.format(doubleTime);
	}
	
	/**
	 * Returns the file containing the log properties. If it doesn't exist, it's created with the 
	 * default values.
	 * For multi-system reasons, the log file path will be always overwritten to the value in 
	 * <code>ApplicationProperties.LOG_FILE_PATH.getDefaultValue()</code>
	 * @return The file containing the log properties.
	 */
	private static File getLogPropertiesFile() {
		final String propertiesFilePath = 
				ApplicationProperties.LOG_PROPERTIES_FILE_PATH.getDefaultValue();
		final File logPropertiesFile = new File(propertiesFilePath);
		
		final String fileContent;
		if (!logPropertiesFile.exists()) {
			fileContent = getDefaultLogPropertiesFileContent();
		} else {
			fileContent = getUpdatedLogPropertiesFileContent(propertiesFilePath);
		}
		
		createLogPropertiesFile(logPropertiesFile, fileContent);
		
		return logPropertiesFile;
	}
	
	/**
	 * Returns the default content of the log properties file.
	 * @param logFilePath The path to the file where the application log will be saved.
	 * @return The content of the file.
	 */
	private static String getDefaultLogPropertiesFileContent() {
		final String logFilePath = ApplicationProperties.LOG_FILE_PATH.getDefaultValue();
		
		final String fileContent = 
			"<configuration>\n" + 
			"	<appender name=\"FILE\" class=\"ch.qos.logback.core.rolling.RollingFileAppender\">\n" + 
			"		<file>" + logFilePath + "</file>\n" + 
			"		<rollingPolicy class=\"ch.qos.logback.core.rolling.FixedWindowRollingPolicy\">\n" +
			"			<fileNamePattern>test.%i.log.zip</fileNamePattern>\n" +
			"			<minIndex>1</minIndex>\n" +
			"			<maxIndex>3</maxIndex>\n" +
//			"		<rollingPolicy class=\"ch.qos.logback.core.rolling.TimeBasedRollingPolicy\">\n" + 
//			"			<SizeBasedTriggeringPolicy " +
//			"				class=\"ch.qos.logback.core.rolling.SizeBasedTriggeringPolicy\">\n" + 
//			"				<maxFileSize>1KB</maxFileSize>\n" + 
//			"			</SizeBasedTriggeringPolicy>\n" + 
			"		</rollingPolicy>\n" + 
			"		<triggeringPolicy class=\"ch.qos.logback.core.rolling.SizeBasedTriggeringPolicy\">\n" +
			"			<maxFileSize>1KB</maxFileSize>\n" +
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

	private static String getUpdatedLogPropertiesFileContent(String propertiesFilePath) {
		final File logPropertiesFile = new File(propertiesFilePath);
		final String filePathRegex = ".*<file>(.*?)</file>.*";
		
		String fileContent = "";
		try {
			fileContent = new Scanner(logPropertiesFile, "UTF-8").useDelimiter("\\A").next();
		} catch (FileNotFoundException ex) {
			AlertMessages.logPropertiesFileNotFound(ex);
		}
		
		// Exit
		if (fileContent == null || "".equals(fileContent)) { return null; }
		
		// Replace all found files with the value of 
		// <code>ApplicationProperties.LOG_FILE_PATH.getDefaultValue()</code>
		final Pattern pattern = Pattern.compile(filePathRegex);
		final Matcher matcher = pattern.matcher(fileContent);
		final String logFilePath = ApplicationProperties.LOG_FILE_PATH.getDefaultValue();
		boolean changed = false;
		
		while (matcher.find()) {
			fileContent = fileContent.replace(matcher.group(1), logFilePath);
			changed = true;
		}
		
		// Exit
		if (!changed) { return null; }
		
		return fileContent;
	}

	/**
	 * Created the file of log properties.
	 * @param logPropertiesFile The file where to create the physical file.
	 * @param fileContent The content of the file to be created.
	 */
	private static void createLogPropertiesFile(final File logPropertiesFile, final String fileContent) {
		try {
			logPropertiesFile.createNewFile();
			try (final BufferedWriter out = new BufferedWriter(new FileWriter(logPropertiesFile))) {
				out.write(fileContent);
			}
		} catch (IOException ex) {
			ErrorMessages.createPropertiesFileIOException("createLogPropertiesFile()", ex);
		}
	}
	
	/**
	 * Finalizes correctly the application and exits.
	 */
	public static void exit(Component mainParent) {	
		boolean exit = true;
		
        saveProperties();
		try {
			Dao.closeDatabaseEngine();
		} catch (SQLException ex) {
			exit = AlertMessages.exitSQLException(mainParent, ex);
		} finally {
			if (exit) {
				final String separator= "*****************************************************************";
				final String message =	"Exiting from the application";
				log.info(separator);
				log.info(message);
				log.info(separator);
				System.exit(0);
			}
		}
	}
}

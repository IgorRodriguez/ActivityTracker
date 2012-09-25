package com.hersis.activitytracker.controler;

import ch.qos.logback.classic.Logger;
import com.hersis.activitytracker.ActivityTrackerMain;
import com.hersis.activitytracker.ApplicationProperties;
import com.hersis.activitytracker.controler.util.LogToFile;
import com.hersis.activitytracker.controler.util.PropertiesFile;
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
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;
import org.slf4j.LoggerFactory;

/**
 * Sets up some application-wide properties and methods as: 
 * the <code>Properties</code> management, the application's look and feel, the logging 
 * policy and the application's <code>exit()</code> method.
 * @author Igor Rodriguez <igorrodriguezelvira@gmail.com>
 */
public class ControllerBO extends Observable {
	private static final Logger log = (Logger) LoggerFactory.getLogger("controller.ControllerBO");			
	private static final Icons ICONS = new Icons();
	private static ControllerBO controllerBo;
	private final PropertiesFile appProperties;

	private ControllerBO() {
		appProperties = new PropertiesFile(ApplicationProperties.PROPERTIES_FILE.getDefaultValue());
		modifyLookAndFeel();
	}
	
	public static ControllerBO getInstance() {
		if (controllerBo == null) {
			controllerBo = new ControllerBO();
		}
		
		return controllerBo;
	}
	/**
	 * Calculates the absolute path to the application's parent directory.
	 * @return The absolute path to the application's parent directory or the user's home directory 
	 * in case of error.
	 */
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
	
	/**
	 * Returns the absolute path to the application properties file.
	 * @return The absolute path to the application properties file.
	 */
	public static String getDefaultPropertiesFilePath() {
		return getDefaultApplicationPath() + File.separatorChar + "ActivityTracker.properties";
	}
	
	/**
	 * Returns the absolute path to the directory where the log files will be saved.
	 * @return The absolute path to the directory where the log files will be saved.
	 */
	public static String getDefaultLogFilePath() {
		return getDefaultApplicationPath() + File.separatorChar + "log";
	}

	/**
	 * Returns the absolute path, including name, of the log's properties file.
	 * @return The absolute path of the log's properties file, including its name.
	 */
	public static String getDefaultLogPropertiesFile() {
		return getDefaultApplicationPath() + File.separatorChar + "logback.xml";
	}
		
	/**
	 * Changes some things on the default LookAndFeel, such as alert message's icons.
	 */
	private void modifyLookAndFeel() {
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
	 * Configures the log of the application.
	 */
	static void configureLog() {
		LogToFile.configure(ApplicationProperties.LOG_PROPERTIES_FILE.getDefaultValue(), 
							ApplicationProperties.LOG_FILE_PATH.getDefaultValue(),
							ApplicationProperties.LOG_NAME.getDefaultValue(),
							ApplicationProperties.LOG_MAXIMUM_SIZE.getDefaultValue());
	}
	
	/**
	 * Finalizes correctly the application and exits.
	 */
	public void exit(Component mainParent) {	
		boolean exit = true;
		
        appProperties.saveProperties();
		try {
			Dao.closeDatabaseEngine();
		} catch (SQLException ex) {
			exit = AlertMessages.exitSQLException(mainParent, ex);
		} finally {
			if (exit) {
				final String separator= "*****************************************************************";
				final String message =	"Quitting application";
				log.info(separator);
				log.info(message);
				log.info(separator);
				System.exit(0);
			}
		}
	}

	public String getPropertie(ApplicationProperties key) {
		final String propertyValue = appProperties.getPropertie(key.toString());
		if (propertyValue != null && !"".equals(propertyValue)) {
			return propertyValue;
		}
		// Return default value
		return key.getDefaultValue();
	}

	public void removePropertie(ApplicationProperties key) {
		appProperties.removePropertie(key.toString());
	}

	public void setPropertie(ApplicationProperties key, String value) {
		appProperties.setPropertie(key.toString(), value);
	}
}

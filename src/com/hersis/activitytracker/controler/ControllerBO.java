package com.hersis.activitytracker.controler;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import com.hersis.activitytracker.ActivityTrackerMain;
import com.hersis.activitytracker.ApplicationProperties;
import com.hersis.activitytracker.controler.util.LogToFile;
import com.hersis.activitytracker.controler.util.PropertiesFile;
import com.hersis.activitytracker.images.Icons;
import com.hersis.activitytracker.model.Dao;
import com.hersis.activitytracker.view.AlertMessages;
import com.hersis.activitytracker.view.util.Locatable;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.io.*;
import java.net.URLDecoder;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Observable;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
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
	private final ArrayList<Locatable> locatableWindows = new ArrayList<>();

	private ControllerBO() {
		configureLog();
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
	private void configureLog() {
		log.setLevel(Level.DEBUG);
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
		
		savePositionOfWindows();
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

	/***********************************************************************************************
	 * Window positioning
	 **********************************************************************************************/
	
	void registerLocatableWindow(Locatable window) {
		locatableWindows.add(window);
	}

	public void locateWindows() {
		for (final Locatable window : locatableWindows) {
			Rectangle position = deserializeWindowPosition(window);
			
			if (position == null) {
				position = getDefaultPositionValues(window);
			} else if (position.x < 0 || position.y < 0) {
				final Rectangle defaultPosition = getDefaultPositionValues(window);
				
				position.x = (position.x < 0) ? defaultPosition.x : position.x;
				position.y = (position.y < 0) ? defaultPosition.y : position.y;
			}
			log.debug("Window's bounds = {}", window.getBounds());
			log.debug("New bounds = {}", position);
			window.setBounds(position);
			((JFrame) window).pack();
			
		
		}
	}

	public void savePositionOfWindows() {
		for (final Locatable window : locatableWindows) {
			appProperties.setPropertie(window.getName(), serializeWindowPosition(window));
		}
	}
	
	private String serializeWindowPosition(Locatable window) {
		final Rectangle bounds = window.getBounds();
		String positionString = "";
		
		positionString += bounds.x + " ";
		positionString += bounds.y + " ";
		positionString += bounds.width + " ";
		positionString += bounds.height;
		
		return positionString;
	}

	private Rectangle deserializeWindowPosition(Locatable window) {
		final String positionString = appProperties.getPropertie(window.getName());
		
		// Exit
		if (positionString == null) {
			log.debug("The position of the window {} is not saved in the properties file.", 
					window.getName());
			return null;
		}
		
		final String [] values = positionString.split(" ");
		
		// Exit
		if (values.length < 4) { return null; }
		
		final Rectangle rectangle = new Rectangle();
		
		try {
			rectangle.x = Integer.parseInt(values[0]);
			rectangle.y = Integer.parseInt(values[1]);
			rectangle.width = Integer.parseInt(values[2]);
			rectangle.height = Integer.parseInt(values[3]);
		} catch (NumberFormatException ex) {
			log.debug("Unable to deserialize the window position of {}. Serialized position: {}", 
					window.getName(), serializeWindowPosition(window));
			return null;
		}
		
		return rectangle;		
	}
	
	private Rectangle getDefaultPositionValues(Locatable window) {
		Rectangle position = new Rectangle();
		// Get the size of the screen
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

		// Determine the new location of the window
		position.width = window.getPreferredSize().width;
		position.height = window.getPreferredSize().height;
		position.x = (dim.width - position.width) / 2;
		position.y = (dim.height - position.height) / 2;
		
		return position;
	}
}

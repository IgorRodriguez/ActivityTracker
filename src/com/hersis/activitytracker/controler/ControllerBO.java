package com.hersis.activitytracker.controler;

import ch.qos.logback.classic.Logger;
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
import java.util.logging.Level;
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
		
	/**
	 * Changes some things on the default LookAndFeel, such as alert message's icons.
	 */
	void modifyLookAndFeel() {
		LookAndFeel laf = UIManager.getLookAndFeel();
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
		
		if (!new File(propertiesFilePath).exists()) createPropertiesFile();
        try (FileInputStream propFis = new FileInputStream(propertiesFilePath)) {
            appProperties.load(propFis);
			setPropertie(ApplicationProperties.APPLICATION_PATH, 
					ApplicationProperties.APPLICATION_PATH.getDefaultValue());
            log.debug("Properties loaded successfully");
        } catch (IOException ioe) {
			AlertMessages.propertiesLoadIOException(ioe);
        } 
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
		
		if (!new File(propertiesFilePath).exists()) createPropertiesFile();
        try (FileOutputStream propFos = new FileOutputStream(propertiesFilePath)) {
            appProperties.store(propFos, "Saved with date: ");
            log.debug("Properties saved successfully");
        } catch (IOException ioe) {
			AlertMessages.propertiesSaveIOException(ioe);
        } 
    }
	
	String getPropertie(ApplicationProperties key) {
		String propertieValue = appProperties.getProperty(key.toString());
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
				System.exit(0);
			}
		}
	}
}

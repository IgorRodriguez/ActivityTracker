package com.hersis.activitytracker.controler;

import ch.qos.logback.classic.Logger;
import com.hersis.activitytracker.images.Icons;
import com.hersis.activitytracker.model.Dao;
import com.hersis.activitytracker.view.AlertMessages;
import java.awt.Component;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Calendar;
import java.util.Locale;
import java.util.Properties;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Igor Rodriguez <igorrodriguezelvira@gmail.com>
 */
class ControllerBO {
	private static final Logger log = (Logger) LoggerFactory.getLogger("controller.ControllerBO");
	private static final String PROPERTIES_FILE_PATH = System.getProperty("user.dir") + File.separatorChar +
			"ActivityTracker.properties";
	private static final Icons ICONS = new Icons();
	private static final Properties appProperties = new Properties();
		
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
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
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
		if (!new File(PROPERTIES_FILE_PATH).exists()) createPropertiesFile();
        try (FileInputStream propFis = new FileInputStream(PROPERTIES_FILE_PATH)) {
            appProperties.load(propFis);
            log.debug("Properties loaded successfully");
        } catch (IOException ioe) {
			AlertMessages.propertiesLoadIOException(ioe);
        } 
    }
	
	void createPropertiesFile() {
		File propertiesFile = new File(PROPERTIES_FILE_PATH);
		try {
			propertiesFile.createNewFile();
		} catch (IOException ex) {
			ErrorMessages.createPropertiesFileIOException("createPropertiesFile()", ex);
		}
	}
    
    void saveProperties() {
		if (!new File(PROPERTIES_FILE_PATH).exists()) createPropertiesFile();
        try (FileOutputStream propFos = new FileOutputStream(PROPERTIES_FILE_PATH)) {
            appProperties.store(propFos, "Saved with date: ");
            log.debug("Properties saved successfully");
        } catch (IOException ioe) {
			AlertMessages.propertiesSaveIOException(ioe);
        } 
    }
	
	static String getPropertie(String key) {
		return appProperties.getProperty(key);
	}
	
	static void setPropertie(String key, String value) {
		appProperties.setProperty(key, value);
	}
	
	static void removePropertie(String key) {
		appProperties.remove(key);
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
	public void exit(Dao dao, Component mainParent) {	
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

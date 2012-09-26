package com.hersis.activitytracker.controller.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Igor Rodriguez <igorrodriguezelvira@gmail.com>
 */
public class PropertiesFile extends Properties {
	private final static Logger log = LoggerFactory.getLogger("controller.util.PropertiesFile");
	private final String propertiesFilePath;
	
	public PropertiesFile(final String propertiesFilePath) {
		this.propertiesFilePath = propertiesFilePath;
		
		loadProperties();
	}
	
	private void loadProperties() {
		if (!new File(propertiesFilePath).exists()) {
			createPropertiesFile();
		}
        try (FileInputStream propFis = new FileInputStream(propertiesFilePath)) {
            this.load(propFis);
            log.debug("Properties loaded successfully");
        } catch (IOException ex) {
			log.error("Couldn't load the application's properties file.\nError: {}", ex);
        } 
    }
	
	private void createPropertiesFile() {
		File propertiesFile = new File(propertiesFilePath);
		try {
			propertiesFile.createNewFile();
		} catch (IOException ex) {
			log.error("Couldn't create the application's properties file.\nError: {}", ex);
		}
	}
    
    public void saveProperties() {
		if (!new File(propertiesFilePath).exists()) {
			createPropertiesFile();
		}
        try (FileOutputStream propFos = new FileOutputStream(propertiesFilePath)) {
            this.store(propFos, "Saved with date: ");
            log.debug("Properties saved successfully");
        } catch (IOException ex) {
			log.error("Couldn't save the application's properties file.\nError: {}", ex);
        } 
    }
}

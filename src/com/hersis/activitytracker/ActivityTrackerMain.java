/*
 * ActivityTracker, also SeguimientoEstudios v2
 * 
 * Developed by Igor Rodríguez.
 * 
 */
package com.hersis.activitytracker;

import ch.qos.logback.classic.Logger;
import com.hersis.activitytracker.controller.Controller;
import java.io.File;
import java.io.IOException;
import org.slf4j.LoggerFactory;


/**
 * The main class of the application.
 */
public class ActivityTrackerMain {
    private static final Logger log = (Logger) LoggerFactory.getLogger("controler.ActivityTrackerMain");
    
    public static void main(String[] args) {
		log.info("Log saved on {}", ApplicationProperties.LOG_FILE_PATH.getDefaultValue());
        File lockFile = new File(Controller.getProperty(ApplicationProperties.APPLICATION_PATH) + 
				File.separatorChar + "lock.lck");
		log.debug("Lock file path: {}", lockFile.getPath());
        if (!lockFile.exists()) {
            try {
                lockFile.createNewFile();
				log.debug("Application file lock created");
				Controller.getInstance();
				log.debug("Controller created");
            } catch (IOException ex) {
                log.info("Couldn't create the locking file: \n{}", ex.getLocalizedMessage());
            } finally {
                if (lockFile.exists()) {
					lockFile.delete();
				}
            }
        } else {
            log.error("There is running another instance of this application. Close it and try again.");
        }

    }
}

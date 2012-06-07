/*
 * ActivityTracker, also SeguimientoEstudios v2
 * 
 * Developed by Igor Rodríguez.
 * 
 */
package com.hersis.activitytracker;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import com.hersis.activitytracker.controler.Controller;
import java.io.File;
import java.io.IOException;
import org.slf4j.LoggerFactory;


/**
 * The main class of the application.
 */
public class ActivityTrackerMain {
    private static final Logger log = (Logger) LoggerFactory.getLogger("controler.ActivityTrackerMain");

    public ActivityTrackerMain() {
        log.setLevel(Level.DEBUG);
    }
    
    public static void main(String[] args) {
        File lockFile = new File(System.getProperty("user.dir") + File.separatorChar + "lock.lck");
        if (!lockFile.exists()) {
            try {
                lockFile.createNewFile();
                Controller controller = Controller.getInstance();
            } catch (IOException ex) {
                log.info("No se pudo crear el fichero de bloqueo.");
            } finally {
                if (lockFile.exists()) lockFile.delete();
            }
        } else {
            System.out.println("Ya se está ejecutando una instancia de este programa. Cierrela e intentelo de nuevo.");
        }

    }
}

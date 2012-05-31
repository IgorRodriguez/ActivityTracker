package com.hersis.activitytracker.controler;

import ch.qos.logback.classic.Logger;
import com.hersis.activitytracker.images.Icons;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Igor Rodriguez <igorrodriguezelvira@gmail.com>
 */
class ControllerBO {
	private final Logger log = (Logger) LoggerFactory.getLogger("controller.ControllerBO");
	private static final Icons ICONS = new Icons();
	
	void modifyLookAndFeel() {
		// Change of the default icons for JOptionPanes
		UIManager.put("OptionPane.informationIcon", ICONS.getIcon(Icons.IconValues.INFO));
		UIManager.put("OptionPane.errorIcon", ICONS.getIcon(Icons.IconValues.ERROR));
		UIManager.put("OptionPane.questionIcon", ICONS.getIcon(Icons.IconValues.QUESTION));
		UIManager.put("OptionPane.warningIcon", ICONS.getIcon(Icons.IconValues.WARNING));
	}
	
	
	void classNotFoundAlert(String methodName, Exception e) {
		String message = "Unable to load the class in method '" + methodName + "': \n" + e.getLocalizedMessage();
		log.error(message);
		JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
	}
	
	void sqlExceptionAlert(String methodName, SQLException e) {
		if (!"Derby system shutdown.".equals(e.getLocalizedMessage())) {
			String message = "Error while executing SQL instruction in method '" + methodName + "': \n" + 
					e.getLocalizedMessage() + " - " + e.getErrorCode();
			log.error(message);
			JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	void nullPointerAlert(String methodName, NullPointerException ex) {
		log.error(ex.getLocalizedMessage());
	}
}

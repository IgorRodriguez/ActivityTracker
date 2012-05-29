package com.hersis.activitytracker.controler;

import ch.qos.logback.classic.Logger;
import com.hersis.activitytracker.images.Icons;
import com.hersis.activitytracker.model.Dao;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Igor Rodriguez <igorrodriguezelvira@gmail.com>
 */
public class Controller {
	private static final Logger log = (Logger) LoggerFactory.getLogger("controller.Controller");
	private Dao dao;
	private static final Icons icons = new Icons();
	private static final ControllerBO controllerBO = new ControllerBO();

	public Controller() {
		init();
		
		try {
			dao = new Dao();
			dao.exitDatabase();
		} catch (ClassNotFoundException ex) {
			classNotFoundAlert("Controller()", ex);
		} catch (SQLException ex) {
			sqlExceptionAlert("Controller()", ex);
		}
		
	}
	
	private void init() {
		controllerBO.modifyLookAndFeel();
	}
	
	private void classNotFoundAlert(String methodName, Exception e) {
		String message = "No se ha podido cargar la clase en el método '" + methodName + "': \n" + e.getLocalizedMessage();
		log.error(message);
		JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
	}
	
	private void sqlExceptionAlert(String methodName, SQLException e) {
		if (!"Derby system shutdown.".equals(e.getLocalizedMessage())) {
			String message = "Ha ocurrido un error al ejecutar la instrucción SQL en el método '" + methodName + "': \n" + 
					e.getLocalizedMessage() + " - " + e.getErrorCode();
			log.error(message);
			JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
}

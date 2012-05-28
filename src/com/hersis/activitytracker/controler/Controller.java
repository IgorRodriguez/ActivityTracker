package com.hersis.activitytracker.controler;

import ch.qos.logback.classic.Logger;
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

	public Controller() {
		try {
			dao = new Dao();
		} catch (ClassNotFoundException ex) {
			classNotFoundAlert("Controller()", ex);
		} catch (SQLException ex) {
			sqlExceptionAlert("Controller()", ex);
		}
	}

	public void start() {
		throw new UnsupportedOperationException("Not yet implemented");
	}
	
	private void classNotFoundAlert(String methodName, Exception e) {
		String message = "No se ha podido cargar la clase en el método '" + methodName + "': \n" + e.getLocalizedMessage();
		log.error(message);
		JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
	}
	
	private void sqlExceptionAlert(String methodName, Exception e) {
		String message = "Ha ocurrido un error al ejecutar la instrucción SQL en el método '" + methodName + "': \n" + 
				e.getLocalizedMessage();
		log.error(message);
		JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
	}
}

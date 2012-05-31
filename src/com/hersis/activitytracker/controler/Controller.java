package com.hersis.activitytracker.controler;

import ch.qos.logback.classic.Logger;
import com.hersis.activitytracker.images.Icons;
import com.hersis.activitytracker.model.ActivityDao;
import com.hersis.activitytracker.model.Dao;
import com.hersis.activitytracker.view.MainForm;
import com.hersis.activitytracker.view.TimerPanel;
import java.sql.SQLException;
import java.util.logging.Level;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Igor Rodriguez <igorrodriguezelvira@gmail.com>
 */
public class Controller {
	private static final Logger log = (Logger) LoggerFactory.getLogger("controller.Controller");
	private Dao dao;
	private ActivityDao activityDao;
	private static final Icons icons = new Icons();
	private ControllerBO controllerBO = new ControllerBO();
	private MainForm mainForm;
	private TimerPanel timerPanel = new TimerPanel();

	public Controller() {
		init();
		
		try {
			dao = new Dao();
			dao.connect();
			activityDao = new ActivityDao();
//			dao.exitDatabase();
		
			timerPanel.setCmbActivities(activityDao.getActivities(dao.getConnection()));
			mainForm.setVisible(true);
		} catch (ClassNotFoundException ex) {
			classNotFoundAlert("Controller()", ex);
		} catch (SQLException ex) {
			sqlExceptionAlert("Controller()", ex);
		}
		
	}
	
	private void init() {
		controllerBO.modifyLookAndFeel();
		mainForm =  new MainForm(this);
		mainForm.add(timerPanel);
		mainForm.pack();
		mainForm.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
	}
	
	private void classNotFoundAlert(String methodName, Exception e) {
		String message = "Unable to load the class in method '" + methodName + "': \n" + e.getLocalizedMessage();
		log.error(message);
		JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
	}
	
	private void sqlExceptionAlert(String methodName, SQLException e) {
		if (!"Derby system shutdown.".equals(e.getLocalizedMessage())) {
			String message = "Error while executing SQL instruction in method '" + methodName + "': \n" + 
					e.getLocalizedMessage() + " - " + e.getErrorCode();
			log.error(message);
			JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void exit() {
		boolean exit = true;
		int errorCode = -1;
		
        try {
            dao.exitDatabase();
        } catch (SQLException ex) {
            errorCode = ex.getErrorCode();
            // At shutdown, Derby throws error 50.000 and SQLState XJ015 to show 
            // that the operation was successful.
            if (errorCode != 50000 && "XJ015".equals(ex.getSQLState())) {
                String errorMessage = "La base de datos no se ha cerrado " +
                        "correctamente.\n" +
                        "¿Desea forzar el cierre de la aplicacion?";
                int answer = JOptionPane.showConfirmDialog(mainForm, errorMessage, 
                        "¿Salir?", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
                if (answer == JOptionPane.CANCEL_OPTION) exit = false; 
            }
        } finally {
            // Exits if not cancelled by the user.
            if (exit) {
				log.info("Successfully disconnected from database: {}", errorCode);
				System.exit(0);
			}
        }
	}
}

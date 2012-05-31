package com.hersis.activitytracker.controler;

import ch.qos.logback.classic.Logger;
import com.hersis.activitytracker.images.Icons;
import com.hersis.activitytracker.model.ActivityDao;
import com.hersis.activitytracker.model.Dao;
import com.hersis.activitytracker.model.TimeDao;
import com.hersis.activitytracker.view.MainForm;
import com.hersis.activitytracker.view.TimerPanel;
import java.sql.SQLException;
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
	private ActivityDao activityDao = new ActivityDao();
	private TimeDao timeDao = new TimeDao();
	
	private static final Icons icons = new Icons();
	private ControllerBO controllerBo = new ControllerBO();
	private MainForm mainForm = new MainForm(this);
	private TimerPanel timerPanel = new TimerPanel(this);
	
	private TimerBO timerBo;	
	
	public Controller() {
		init();
		
		try {
			dao = new Dao();
			
			timerBo = new TimerBO(dao, timerPanel);
			dao.connect();
		
			timerPanel.setCmbActivities(activityDao.getActivities(dao.getConnection()));
			mainForm.setVisible(true);
		} catch (NullPointerException ex) {
			controllerBo.nullPointerAlert("Controller()", ex);
		} catch (ClassNotFoundException ex) {
			controllerBo.classNotFoundAlert("Controller()", ex);
		} catch (SQLException ex) {
			controllerBo.sqlExceptionAlert("Controller()", ex);
		}
	}
	
	private void init() {
		controllerBo.modifyLookAndFeel();
		mainForm.add(timerPanel);
		mainForm.pack();
		mainForm.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		
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

	public void play() {
		timerBo.play();
	}

	public void pause() {
		timerBo.pause();
	}

	public void stop() {
		try {
			timerBo.stop();
		} catch (SQLException ex) {
			controllerBo.sqlExceptionAlert("stop()", ex);
		} catch (ClassNotFoundException ex) {
			controllerBo.classNotFoundAlert("stop()", ex);
		}
	}

	public void startTracking() {
		try {
			timerBo.startTracking();
		} catch (IndexOutOfBoundsException ex) {
			String message = ex.getLocalizedMessage();
			log.error(message);
			JOptionPane.showMessageDialog(timerPanel, message, "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
}

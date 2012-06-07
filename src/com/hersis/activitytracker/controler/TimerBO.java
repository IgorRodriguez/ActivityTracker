package com.hersis.activitytracker.controler;

import ch.qos.logback.classic.Logger;
import com.hersis.activitytracker.Activity;
import com.hersis.activitytracker.Time;
import com.hersis.activitytracker.model.ActivityDao;
import com.hersis.activitytracker.model.Dao;
import com.hersis.activitytracker.model.TimeDao;
import com.hersis.activitytracker.view.TimerPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Observable;
import java.util.Observer;
import java.util.TimeZone;
import javax.swing.Timer;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Igor Rodriguez <igorrodriguezelvira@gmail.com>
 */
public class TimerBO implements Observer {
	private final Logger log = (Logger) LoggerFactory.getLogger("controller.TimerBO");
	private TimerPanel timerPanel;
	private Dao dao;
	private TimeDao timeDao = TimeDao.getInstance();
	private ActivityDao activityDao = ActivityDao.getInstance();
	private final ErrorMessages errorMessages = new ErrorMessages();
	
	private long totalTime = 0;     // Total of time in "play" status
    private long diffTime = 0;      // Time since the last time "play" was pressed.
    private long startTime = -1;    // Time of the first "play".
    private long restartTime = -1;  // Time of the last "play".
    private long pauseTime = -1;    // Time of the last "pause".
	
	/* Controls the execution of the "pause" instructions when "stop" if called. By default it's 
	 * true, for the case "stop" is pressed without having pressed "play" first. */
    private boolean isPaused = true;     
	
	private final static String TIME_FORMAT = "HH:mm:ss";
	private SimpleDateFormat timeFormatter = new SimpleDateFormat(TIME_FORMAT);
	// This one is used in GMT format for showing the lapsed time.
	private SimpleDateFormat totalTimeFormatter = new SimpleDateFormat(TIME_FORMAT);
	private Timer timer;
	
	TimerBO(TimerPanel timerPanel) throws ClassNotFoundException, SQLException {
		this.dao = Dao.getInstance();
		this.timerPanel = timerPanel;
		
		activityDao.addObserver(this);
		timeDao.addObserver(this);
		
		timerPanel.resetControls();
		
		/* Configuration for SimpleDateFormat that allows to show the time in GMT mode. Used in this 
		 * case to prevent the lapsed time to have an incorrect value. For example, without this 
		 * option in a GMT+1 zone the lapsed time should start as 1:00:00 instead of 00:00:00.
		 */
		totalTimeFormatter.setTimeZone(TimeZone.getTimeZone("GMT"));
	}

	void play() {
		String startTimeString = null; // If null, txtStartTime will not be reseted. 
		
		isPaused = false;
				
		if (startTime == -1) { // First "play"
			startTime = System.currentTimeMillis();
			startTimeString = timeFormatter.format(startTime);
		}
		// Refreshing the GUI.
		timerPanel.setPlayControls(startTimeString);
		
		restartTime = System.currentTimeMillis();
		startTimer();
		refreshTotalTime();
	}
	
	
	/**
	 * Refresh the timerPanel's <code>txtTotalTime</code> text value to the total time in "play" 
	 * status.
     */
    private void refreshTotalTime() {
		// The time passed since the last time "play" wass pressed added to the totalTime accumulated 
		// en each "pause".
        diffTime = System.currentTimeMillis() - restartTime + totalTime;
		// Show the text formatted for GMT.
        timerPanel.setTotalTimeText(totalTimeFormatter.format(diffTime));
    }

	/**
	 * Starts a timer that refreshes the totalTime textField in timerPanel every second.
	 */
	private void startTimer() {
		if (timer == null) {
            timer = new javax.swing.Timer(1000, new ActionListener() {
				@Override
                public void actionPerformed(ActionEvent e) {
                    refreshTotalTime();
                }
            });
        }
        timer.start();
	}

	void pause() {
		if(!isPaused) {
            pauseTime = System.currentTimeMillis();
            // Refresh the GUI
			timerPanel.setPauseControls(timeFormatter.format(pauseTime));
			
            timer.stop();
			// Refresh for showing the last time in the JTextField.
            refreshTotalTime();     
			// Increase totalTime with the lapse time since "play" was pressed last time.
            totalTime += pauseTime - restartTime;
			
			/* To prevent that no instructions will be executed if this method is called without 
			 * having pressed first "play" (i.e. at pressing "stop").
			 */		
            isPaused = true;
        }
	}

	void stop() throws SQLException, ClassNotFoundException {
		Activity activity;
		
		// Pause() will we executed for the case "stop" is pressed being in "play" status.
		pause();
		if (startTime > 0 && totalTime >= TimeBO.MINIMUM_TIME_DURATION) {
			try {
				activity = timerPanel.getSelectedActivity();
				Connection conn = dao.connect();
				timeDao.insertTime(conn, new Time(activity.getIdActivity(), new Timestamp(startTime), 
						new Timestamp(pauseTime), totalTime, ""));
			} catch (SQLException | ClassNotFoundException ex) {
				throw ex;
			} finally {			
				dao.disconnect();
			}
		}
		startTime = -1;
		totalTime = 0;
		// Refreshing the GUI
		timerPanel.resetControls();
	}

	/**
	 * Alters the GUI to allow starting a new activity tracking with "play", "stop" buttons.
	 * @throws IndexOutOfBoundsException If there isn't any activity selected in the JComboBox.
	 */
	void startTracking() throws IndexOutOfBoundsException {
		if (timerPanel.getSelectedIndex() != -1) {
			timerPanel.startTracking();
		} else {
			throw new IndexOutOfBoundsException("There isn't any activity selected in the list!");
		}
	}
	
	void loadCmbActivities() {
		try {
			// Set the JComboBox values from the database in descendant order by date.
			Connection conn = dao.connect();
			timerPanel.loadCmbActivities(activityDao.orderActivitiesByTime(activityDao.getActivities(conn), 
					timeDao.getDistinctActivityIdsByTime(conn)));
		} catch (SQLException ex) {
			errorMessages.sqlExceptionError("loadCmbActivities()", ex);
		} catch (ClassNotFoundException ex) {
			errorMessages.classNotFoundError("loadCmbActivities()", ex);
		} finally {
			dao.disconnect();
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		loadCmbActivities();
	}
}

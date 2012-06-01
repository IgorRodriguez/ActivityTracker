package com.hersis.activitytracker.controler;

import ch.qos.logback.classic.Logger;
import com.hersis.activitytracker.Activity;
import com.hersis.activitytracker.Time;
import com.hersis.activitytracker.model.Dao;
import com.hersis.activitytracker.model.TimeDao;
import com.hersis.activitytracker.view.TimerPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import javax.swing.Timer;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Igor Rodriguez <igorrodriguezelvira@gmail.com>
 */
public class TimerBO {
	private final Logger log = (Logger) LoggerFactory.getLogger("controller.TimerBO");
	private TimerPanel timerPanel;
	private Dao dao;
	private TimeDao timeDao = new TimeDao();
	
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
	
	TimerBO(Dao dao, TimerPanel timerPanel) throws NullPointerException {
		if (dao == null) throw new NullPointerException("dao is null");
		if (timerPanel == null) throw new NullPointerException("timerPanel is null");
		
		this.dao = dao;
		this.timerPanel = timerPanel;
		
		timerPanel.setEnabledBtnPlay(false);
		timerPanel.setEnabledBtnPause(false);
		timerPanel.setEnabledBtnStop(false);
		
		/* Configuration for SimpleDateFormat that allows to show the time in GMT mode. Used in this 
		 * case to prevent the lapsed time to have an incorrect value. For example, without this 
		 * option in a GMT+1 zone the lapsed time should start as 1:00:00 instead of 00:00:00.
		 */
		totalTimeFormatter.setTimeZone(TimeZone.getTimeZone("GMT"));
	}

	void play() {
		// Refreshing the GUI.
		timerPanel.setEnabledBtnPlay(false);
		timerPanel.setEnabledBtnPause(true);
		timerPanel.setEnabledBtnStop(true);
		timerPanel.setPausedAtText("");
		isPaused = false;
		
		if (startTime == -1) { // First "play"
			startTime = System.currentTimeMillis();
			timerPanel.setStartTimeText(timeFormatter.format(startTime));
		}
		
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
            // Refresh the GUI
			timerPanel.setEnabledBtnPlay(true);
			timerPanel.setEnabledBtnPause(false);
			
            pauseTime = System.currentTimeMillis();
            
            timer.stop();
			// Refresh for showing the last time in the JTextField.
            refreshTotalTime();     
			// Increase totalTime with the lapse time since "play" was pressed last time.
            totalTime += pauseTime - restartTime;
			timerPanel.setPausedAtText(timeFormatter.format(pauseTime));
			
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
		if (startTime > 0) {
			try {
				activity = timerPanel.getSelectedActivity();
				Connection conn = dao.connect();
				timeDao.insertTime(conn, new Time(activity.getIdActivity(), new Timestamp(startTime), 
						new Timestamp(pauseTime), new Timestamp(totalTime), ""));
				startTime = -1;
				totalTime = 0;

				// Refreshing the GUI
				timerPanel.setEnabledBtnPlay(false);
				timerPanel.setEnabledBtnPause(false);
				timerPanel.setEnabledBtnStop(false);
				timerPanel.setEnabledBtnNew(true);
				timerPanel.setEnabledCmbActivities(true);
			} catch (SQLException | ClassNotFoundException ex) {
				throw ex;
			} finally {			
				dao.disconnect();
			}
		}
	}

	/**
	 * Alters the GUI to allow starting a new activity tracking with "play", "stop" buttons.
	 * @throws IndexOutOfBoundsException If there isn't any activity selected in the JComboBox.
	 */
	void startTracking() throws IndexOutOfBoundsException {
		if (timerPanel.getSelectedIndex() != -1) {
			timerPanel.setEnabledBtnPlay(true);
			timerPanel.setEnabledBtnNew(false);
			timerPanel.setEnabledCmbActivities(false);
			timerPanel.setEnabledBtnStop(true);
			timerPanel.setStartTimeText("");
			timerPanel.setPausedAtText("");
			timerPanel.setTotalTimeText("");
		} else {
			throw new IndexOutOfBoundsException("There isn't any activity selected in the list!");
		}
	}
}

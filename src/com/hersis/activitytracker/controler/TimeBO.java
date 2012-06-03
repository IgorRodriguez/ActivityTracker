package com.hersis.activitytracker.controler;

import com.hersis.activitytracker.model.Dao;
import com.hersis.activitytracker.view.TimeDialog;

/**
 *
 * @author Igor Rodriguez <igorrodriguezelvira@gmail.com>
 */
public class TimeBO {
	private final Dao dao;
	private final TimeDialog timeDialog;

	TimeBO(Dao dao, TimeDialog timeDialog) {
		this.dao = dao;
		this.timeDialog = timeDialog;
	}

	void showNewTime() {
		timeDialog.setControlTime();
		timeDialog.calculateDuration();
		timeDialog.setVisible(true);
	}
	
}

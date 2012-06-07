package com.hersis.activitytracker.view;

import com.hersis.activitytracker.Activity;
import com.hersis.activitytracker.Time;
import com.hersis.activitytracker.controler.Controller;
import java.awt.Color;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Shows a window with controls to create, edit and delete Time instances.
 * 
 * @author Igor Rodriguez <igorrodriguezelvira@gmail.com>
 */
public class TimeDialog extends javax.swing.JDialog {
	private final Controller controller;
	private Time oldTime = null;

	/**
	 * Creates new form TimeDialog
	 */
	public TimeDialog(java.awt.Frame parent, boolean modal) {
		super(parent, modal);
		this.controller = Controller.getInstance();
		initComponents();
		
		// Set dateChoosers date format.
		Locale locale = Locale.getDefault();
		String localeDatePattern = 
				((SimpleDateFormat) DateFormat.getDateInstance(SimpleDateFormat.LONG, locale)).toPattern();		
		startTimeDateChooser.setDateFormatString(localeDatePattern);
		endTimeDateChooser.setDateFormatString(localeDatePattern);
		
		this.getRootPane().setDefaultButton(btnAccept);
	}
	
	/**
	 * Sets the current time in the start and end fields.
	 */
	void setTimeOnFields() {
		Date now = Calendar.getInstance().getTime();
		startTimeDateChooser.setDate(now);
		spnHourStartTime.setValue(now);
		spnMinuteStartTime.setValue(now);
		endTimeDateChooser.setDate(now);
		spnHourEndTime.setValue(now);
		spnMinuteEndTime.setValue(now);
	}	
	
	/**
	 * Calculates the start time with the content of the start time fields.
	 * @return Calendar containing the date from the fields.
	 */
	Calendar getStartTimeFromFields() {
		Calendar startTime = startTimeDateChooser.getCalendar();
		if (startTime != null) {
			startTime.set(Calendar.HOUR, 0);
			startTime.set(Calendar.MINUTE, 0);
			startTime.set(Calendar.SECOND, 0);
			startTime.set(Calendar.MILLISECOND, 0);
			
			Calendar hour = Calendar.getInstance();
			Calendar minute = Calendar.getInstance();
			// Calculate Start time
			hour.clear();
			minute.clear();
			hour.setTime((Date) spnHourStartTime.getValue());
			minute.setTime((Date) spnMinuteStartTime.getValue());
			startTime.set(Calendar.HOUR, hour.get(Calendar.HOUR));
			startTime.set(Calendar.MINUTE, minute.get(Calendar.MINUTE));
		}
		return startTime;
	}
	
	/**
	 * Calculates the end time with the content of the end time fields.
	 * @return Calendar containing the date from the fields.
	 */
	Calendar getEndTimeFromFields() {
		Calendar endTime = endTimeDateChooser.getCalendar();
		if (endTime != null) {
			endTime.set(Calendar.HOUR, 0);
			endTime.set(Calendar.MINUTE, 0);
			endTime.set(Calendar.SECOND, 0);
			endTime.set(Calendar.MILLISECOND, 0);
			
			Calendar hour = Calendar.getInstance();
			Calendar minute = Calendar.getInstance();
			// Calculate End time
			hour.clear();
			minute.clear();
			hour.setTime((Date) spnHourEndTime.getValue());
			minute.setTime((Date) spnMinuteEndTime.getValue());
			endTime.set(Calendar.HOUR, hour.get(Calendar.HOUR));
			endTime.set(Calendar.MINUTE, minute.get(Calendar.MINUTE));
		}
		return endTime;
	}
	
	/**
	 * Prepares the window to be ready to insert a new Time.
	 */
	public void showNewTime() {
		oldTime = null;
		clearTextFields();
		setTimeOnFields();
		refreshDurationField();
		btnDelete.setVisible(false);
		this.pack();
	}
	
	/**
	 * Prepares the window to edit the given time.
	 * @param time A non null Time instance.
	 */
	public void showEditTime(Time time) {
		clearTextFields();
		fillAllFields(time);
		setTime(time);
		btnDelete.setVisible(true);
		this.pack();
	}
	
	/**
	 * Resets all the JTextFields to blank.
	 */
	private void clearTextFields() {
		txaDescription.setText("");
	}
	
	/**
	 * Fills all the fields with the values in the given Time.
	 * @param time A non null Time instance.
	 */
	private void fillAllFields(Time time) {
		ArrayList<Activity> activities = controller.getActivities();
		for (Activity a : activities) {
			if (a.getIdActivity() == time.getIdActivity()) cmbActivities.getModel().setSelectedItem(a);
		}
		setStartTimeFields(time.getStartTime());
		setEndTimeFields(time.getEndTime());
		refreshDurationField();
		txaDescription.setText(time.getDescription());
	}
	
	/**
	 * Fills the start time fields with the given date.
	 * @param time The date with which fill the fields.
	 */
	private void setStartTimeFields(Timestamp time) {
		Date date = new Date(time.getTime());
		
		startTimeDateChooser.setDate(date);
		spnHourStartTime.setValue(date);
		spnMinuteStartTime.setValue(date);
	}
	
	/**
	 * Fills the end time fields with the given date.
	 * @param time The date with which fill the fields.
	 */
	private void setEndTimeFields(Timestamp time) {
		Date date = new Date(time.getTime());
		
		endTimeDateChooser.setDate(date);
		spnHourEndTime.setValue(date);
		spnMinuteEndTime.setValue(date);
	}
	
	/**
	 * Fills cmbActivities.
	 * @return The list of activities with which it has been filed.
	 */
	public ArrayList<Activity> loadCmbActivities() {
		ArrayList<Activity> activities = controller.getActivitiesOrderedByTime();
		cmbActivities.removeAllItems();
		for (Activity a : activities) {
			cmbActivities.addItem(a);
		}
		return activities;
	}
	
	/**
	 * Refresh the value of txtDuration with the values of the start and end time fields.
	 */
	private void refreshDurationField() {
		final Color defaultColor = Color.BLACK;
		final Color errorColor = Color.RED;
				
		if (startTimeDateChooser.getCalendar() == null) {
			startTimeDateChooser.setCalendar(Calendar.getInstance());
		}
		if (endTimeDateChooser.getCalendar() == null) {
			endTimeDateChooser.setCalendar(Calendar.getInstance());
		}
		long duration = controller.calculateDuration(getStartTimeFromFields(), getEndTimeFromFields());
		txtDuration.setForeground((duration >= 0) ? defaultColor : errorColor);
		String durationString = controller.getDurationString(duration);
		txtDuration.setText(durationString);
	}
	
	/**
	 * Returns the activity selected in cmbActivities.
	 * @return The Activity selected in cmbActivities. Doesn't check if there is an activity selected.
	 */
	public Activity getSelectedActivity() {
		return (Activity) cmbActivities.getSelectedItem();
	}
	
	/**
	 * Returns the index of the Activity selected in cmbActivities.
	 * @return The index of the activity selected in cmbActivities. -1 if there is no activity selected.
	 */
	public int getSelectedActivityIndex() {
		return cmbActivities.getSelectedIndex();
	}
	
	/**
	 * Fills a Time with the values of the fields.
	 * @return Time containing the values of the fields. It won't be null be can be not completely 
	 * filled.
	 */
	private Time getTimeFromFields() {
		int activityId = getSelectedActivity().getIdActivity();
		Calendar startTimeCal = getStartTimeFromFields();
		Calendar endTimeCal = getEndTimeFromFields();
		Timestamp startTime = null;
		Timestamp endTime = null;
		long duration = -1;
		if (startTimeCal != null) startTime = new Timestamp(startTimeCal.getTimeInMillis());
		if (endTimeCal != null) endTime = new Timestamp(endTimeCal.getTimeInMillis());
		if (startTime != null && endTime != null) {
			duration = controller.calculateDuration(startTimeCal, endTimeCal);
		}
		String description = txaDescription.getText();
		
		return new Time(activityId, startTime, endTime, duration, description);
	}
	
	public void setTime(Time time) {
		this.oldTime = time;		
	}

	public Time getTime() {
		return oldTime;
	}
	/**
	 * This method is called from within the constructor to initialize the form. WARNING: Do NOT
	 * modify this code. The content of this method is always regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        timePanel = new javax.swing.JPanel();
        activityPanel = new javax.swing.JPanel();
        lblActivity = new javax.swing.JLabel();
        cmbActivities = new javax.swing.JComboBox();
        startTimePanel = new javax.swing.JPanel();
        startTimeDatePanel = new javax.swing.JPanel();
        lblDateStartTime = new javax.swing.JLabel();
        startTimeDateChooser = new com.toedter.calendar.JDateChooser();
        startTimeHourPanel = new javax.swing.JPanel();
        lblHourStartTime = new javax.swing.JLabel();
        spnHourStartTime = new javax.swing.JSpinner();
        startTimeMinutePanel = new javax.swing.JPanel();
        lblMinuteStartTime = new javax.swing.JLabel();
        spnMinuteStartTime = new javax.swing.JSpinner();
        endTimePanel = new javax.swing.JPanel();
        endTimeDatePanel = new javax.swing.JPanel();
        lblDateEndTime = new javax.swing.JLabel();
        endTimeDateChooser = new com.toedter.calendar.JDateChooser();
        endTimeHourPanel = new javax.swing.JPanel();
        lblHourEndTime = new javax.swing.JLabel();
        spnHourEndTime = new javax.swing.JSpinner();
        endTimeMinutePanel = new javax.swing.JPanel();
        lblMinuteEndTime = new javax.swing.JLabel();
        spnMinuteEndTime = new javax.swing.JSpinner();
        durationPanel = new javax.swing.JPanel();
        lblDuration = new javax.swing.JLabel();
        txtDuration = new javax.swing.JTextField();
        descriptionPanel = new javax.swing.JPanel();
        lblDescription = new javax.swing.JLabel();
        txaDescriptionScrollPane = new javax.swing.JScrollPane();
        txaDescription = new javax.swing.JTextArea();
        buttonPanel = new javax.swing.JPanel();
        btnDelete = new javax.swing.JButton();
        separatorPanel = new javax.swing.JPanel();
        btnCancel = new javax.swing.JButton();
        btnAccept = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        timePanel.setLayout(new java.awt.GridBagLayout());

        activityPanel.setLayout(new java.awt.GridBagLayout());

        lblActivity.setText("Activity:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        activityPanel.add(lblActivity, gridBagConstraints);

        cmbActivities.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 20.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        activityPanel.add(cmbActivities, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 2.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 7, 5, 7);
        timePanel.add(activityPanel, gridBagConstraints);

        startTimePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Start time"));
        startTimePanel.setLayout(new java.awt.GridBagLayout());

        startTimeDatePanel.setLayout(new java.awt.GridBagLayout());

        lblDateStartTime.setText("Date:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        startTimeDatePanel.add(lblDateStartTime, gridBagConstraints);

        startTimeDateChooser.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                startTimeDateChooserPropertyChange(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 10.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        startTimeDatePanel.add(startTimeDateChooser, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 2.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(7, 7, 7, 7);
        startTimePanel.add(startTimeDatePanel, gridBagConstraints);

        startTimeHourPanel.setLayout(new java.awt.GridBagLayout());

        lblHourStartTime.setText("Hour:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        startTimeHourPanel.add(lblHourStartTime, gridBagConstraints);

        spnHourStartTime.setModel(new javax.swing.SpinnerDateModel(new java.util.Date(), null, null, java.util.Calendar.HOUR));
        spnHourStartTime.setEditor(new javax.swing.JSpinner.DateEditor(spnHourStartTime, "HH"));
        spnHourStartTime.setVerifyInputWhenFocusTarget(false);
        spnHourStartTime.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spnHourStartTimeStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 10.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        startTimeHourPanel.add(spnHourStartTime, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LAST_LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(7, 7, 7, 7);
        startTimePanel.add(startTimeHourPanel, gridBagConstraints);

        startTimeMinutePanel.setLayout(new java.awt.GridBagLayout());

        lblMinuteStartTime.setText("Minute:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        startTimeMinutePanel.add(lblMinuteStartTime, gridBagConstraints);

        spnMinuteStartTime.setModel(new javax.swing.SpinnerDateModel());
        spnMinuteStartTime.setEditor(new javax.swing.JSpinner.DateEditor(spnMinuteStartTime, "mm"));
        spnMinuteStartTime.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spnMinuteStartTimeStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 10.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        startTimeMinutePanel.add(spnMinuteStartTime, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LAST_LINE_END;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(7, 7, 7, 7);
        startTimePanel.add(startTimeMinutePanel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 4.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 7, 5, 7);
        timePanel.add(startTimePanel, gridBagConstraints);

        endTimePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("End time"));
        endTimePanel.setLayout(new java.awt.GridBagLayout());

        endTimeDatePanel.setLayout(new java.awt.GridBagLayout());

        lblDateEndTime.setText("Date:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        endTimeDatePanel.add(lblDateEndTime, gridBagConstraints);

        endTimeDateChooser.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                endTimeDateChooserPropertyChange(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 10.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        endTimeDatePanel.add(endTimeDateChooser, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 2.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(7, 7, 7, 7);
        endTimePanel.add(endTimeDatePanel, gridBagConstraints);

        endTimeHourPanel.setLayout(new java.awt.GridBagLayout());

        lblHourEndTime.setText("Hour:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        endTimeHourPanel.add(lblHourEndTime, gridBagConstraints);

        spnHourEndTime.setModel(new javax.swing.SpinnerDateModel(new java.util.Date(), null, null, java.util.Calendar.HOUR));
        spnHourEndTime.setEditor(new javax.swing.JSpinner.DateEditor(spnHourEndTime, "HH"));
        spnHourEndTime.setVerifyInputWhenFocusTarget(false);
        spnHourEndTime.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spnHourEndTimeStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 10.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        endTimeHourPanel.add(spnHourEndTime, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LAST_LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(7, 7, 7, 7);
        endTimePanel.add(endTimeHourPanel, gridBagConstraints);

        endTimeMinutePanel.setLayout(new java.awt.GridBagLayout());

        lblMinuteEndTime.setText("Minute:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        endTimeMinutePanel.add(lblMinuteEndTime, gridBagConstraints);

        spnMinuteEndTime.setModel(new javax.swing.SpinnerDateModel());
        spnMinuteEndTime.setEditor(new javax.swing.JSpinner.DateEditor(spnMinuteEndTime, "mm"));
        spnMinuteEndTime.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spnMinuteEndTimeStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 10.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        endTimeMinutePanel.add(spnMinuteEndTime, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LAST_LINE_END;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(7, 7, 7, 7);
        endTimePanel.add(endTimeMinutePanel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 4.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 7, 5, 7);
        timePanel.add(endTimePanel, gridBagConstraints);

        durationPanel.setLayout(new java.awt.GridBagLayout());

        lblDuration.setText("Duration:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        durationPanel.add(lblDuration, gridBagConstraints);

        txtDuration.setBackground(new java.awt.Color(229, 229, 229));
        txtDuration.setEditable(false);
        txtDuration.setFont(new java.awt.Font("Dialog", 0, 20)); // NOI18N
        txtDuration.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtDuration.setFocusable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 20.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        durationPanel.add(txtDuration, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 2.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 7, 5, 7);
        timePanel.add(durationPanel, gridBagConstraints);

        descriptionPanel.setLayout(new java.awt.GridBagLayout());

        lblDescription.setText("Description:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        descriptionPanel.add(lblDescription, gridBagConstraints);

        txaDescriptionScrollPane.setPreferredSize(new java.awt.Dimension(301, 100));

        txaDescription.setColumns(20);
        txaDescription.setLineWrap(true);
        txaDescription.setRows(5);
        txaDescription.setPreferredSize(new java.awt.Dimension(300, 75));
        txaDescriptionScrollPane.setViewportView(txaDescription);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 20.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        descriptionPanel.add(txaDescriptionScrollPane, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 6.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 7, 5, 7);
        timePanel.add(descriptionPanel, gridBagConstraints);

        buttonPanel.setLayout(new java.awt.GridBagLayout());

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/hersis/activitytracker/images/trashcan_full.png"))); // NOI18N
        btnDelete.setText("Delete");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 7, 7, 7);
        buttonPanel.add(btnDelete, gridBagConstraints);

        separatorPanel.setLayout(new java.awt.BorderLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 2.0;
        gridBagConstraints.weighty = 1.0;
        buttonPanel.add(separatorPanel, gridBagConstraints);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/hersis/activitytracker/images/button_cancel.png"))); // NOI18N
        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHEAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 7, 7, 7);
        buttonPanel.add(btnCancel, gridBagConstraints);

        btnAccept.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/hersis/activitytracker/images/button_ok.png"))); // NOI18N
        btnAccept.setText("Accept");
        btnAccept.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAcceptActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHEAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 7, 7, 7);
        buttonPanel.add(btnAccept, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_END;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        timePanel.add(buttonPanel, gridBagConstraints);

        getContentPane().add(timePanel, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

	private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
		controller.deleteTime(this, oldTime);
	}//GEN-LAST:event_btnDeleteActionPerformed

	private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
		controller.cancelTimeEdition();
	}//GEN-LAST:event_btnCancelActionPerformed

	private void btnAcceptActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAcceptActionPerformed
		controller.saveTime(oldTime, getTimeFromFields());
	}//GEN-LAST:event_btnAcceptActionPerformed

	private void startTimeDateChooserPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_startTimeDateChooserPropertyChange
		refreshDurationField();
	}//GEN-LAST:event_startTimeDateChooserPropertyChange

	private void endTimeDateChooserPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_endTimeDateChooserPropertyChange
		refreshDurationField();
	}//GEN-LAST:event_endTimeDateChooserPropertyChange

	private void spnHourStartTimeStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spnHourStartTimeStateChanged
		refreshDurationField();
	}//GEN-LAST:event_spnHourStartTimeStateChanged

	private void spnMinuteStartTimeStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spnMinuteStartTimeStateChanged
		refreshDurationField();
	}//GEN-LAST:event_spnMinuteStartTimeStateChanged

	private void spnHourEndTimeStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spnHourEndTimeStateChanged
		refreshDurationField();
	}//GEN-LAST:event_spnHourEndTimeStateChanged

	private void spnMinuteEndTimeStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spnMinuteEndTimeStateChanged
		refreshDurationField();
	}//GEN-LAST:event_spnMinuteEndTimeStateChanged

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel activityPanel;
    private javax.swing.JButton btnAccept;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnDelete;
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JComboBox cmbActivities;
    private javax.swing.JPanel descriptionPanel;
    private javax.swing.JPanel durationPanel;
    private com.toedter.calendar.JDateChooser endTimeDateChooser;
    private javax.swing.JPanel endTimeDatePanel;
    private javax.swing.JPanel endTimeHourPanel;
    private javax.swing.JPanel endTimeMinutePanel;
    private javax.swing.JPanel endTimePanel;
    private javax.swing.JLabel lblActivity;
    private javax.swing.JLabel lblDateEndTime;
    private javax.swing.JLabel lblDateStartTime;
    private javax.swing.JLabel lblDescription;
    private javax.swing.JLabel lblDuration;
    private javax.swing.JLabel lblHourEndTime;
    private javax.swing.JLabel lblHourStartTime;
    private javax.swing.JLabel lblMinuteEndTime;
    private javax.swing.JLabel lblMinuteStartTime;
    private javax.swing.JPanel separatorPanel;
    private javax.swing.JSpinner spnHourEndTime;
    private javax.swing.JSpinner spnHourStartTime;
    private javax.swing.JSpinner spnMinuteEndTime;
    private javax.swing.JSpinner spnMinuteStartTime;
    private com.toedter.calendar.JDateChooser startTimeDateChooser;
    private javax.swing.JPanel startTimeDatePanel;
    private javax.swing.JPanel startTimeHourPanel;
    private javax.swing.JPanel startTimeMinutePanel;
    private javax.swing.JPanel startTimePanel;
    private javax.swing.JPanel timePanel;
    private javax.swing.JTextArea txaDescription;
    private javax.swing.JScrollPane txaDescriptionScrollPane;
    private javax.swing.JTextField txtDuration;
    // End of variables declaration//GEN-END:variables

}

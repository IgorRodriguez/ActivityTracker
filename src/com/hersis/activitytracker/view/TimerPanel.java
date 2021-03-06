package com.hersis.activitytracker.view;

import com.hersis.activitytracker.Activity;
import com.hersis.activitytracker.controller.Controller;
import java.util.ArrayList;
import javax.swing.JButton;

/**
 *
 * @author Igor Rodriguez <igorrodriguezelvira@gmail.com>
 */
public class TimerPanel extends javax.swing.JPanel {
	/**
	 * Creates new form TimerPanel
	 */
	public TimerPanel() {
		initComponents();
	}

	public void loadCmbActivities(ArrayList<Activity> activities) {
		cmbActivities.removeAllItems();
		for (Activity a : activities) {
			cmbActivities.addItem(a);
		}
	}
	
	public void resetControls() {
		cmbActivities.setEnabled(true);
		btnNew.setEnabled(true);
		btnPlay.setEnabled(false);
		btnPause.setEnabled(false);
		btnStop.setEnabled(false);
	}
	
	/**
	 * Sets the controls in "play" status.
	 * @param startTimeString String to be set in the control txtStartTime. If null, the value of 
	 * the control will be maintained.
	 */
	public void setPlayControls(String startTimeString) {
		if (startTimeString != null) txtStartTime.setText(startTimeString);
		btnPlay.setEnabled(false);
		btnPause.setEnabled(true);
		btnStop.setEnabled(true);
		txtPausedAt.setText("");
	}
	
	public void setPauseControls(String pausedAtString) {
		btnPlay.setEnabled(true);
		btnPause.setEnabled(false);
		txtPausedAt.setText(pausedAtString);
	}
	
	public Activity getSelectedActivity() {
		return (Activity) cmbActivities.getSelectedItem();
	}
	
	public int getSelectedIndex() {
		return cmbActivities.getSelectedIndex();
	}
	
	public void setTotalTimeText(String text) {
		txtTotalTime.setText(text);
	}
	
	public JButton getNewButton() {
		return btnNew;
	}
	
	public void startTracking() {
		cmbActivities.setEnabled(false);
		btnNew.setEnabled(false);
		btnPlay.setEnabled(true);
		btnPlay.requestFocusInWindow();
		btnStop.setEnabled(true);
		txtStartTime.setText("");
		txtPausedAt.setText("");
		txtTotalTime.setText("");
	}

	/**
	 * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
	 * content of this method is always regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        cmbActivities = new javax.swing.JComboBox();
        btnPlay = new javax.swing.JButton();
        btnPause = new javax.swing.JButton();
        btnStop = new javax.swing.JButton();
        txtStartTime = new javax.swing.JTextField();
        lblStartTime = new javax.swing.JLabel();
        lblPausedAt = new javax.swing.JLabel();
        lblTotalTime = new javax.swing.JLabel();
        txtPausedAt = new javax.swing.JTextField();
        txtTotalTime = new javax.swing.JTextField();
        btnNew = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 3.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(7, 7, 10, 7);
        add(cmbActivities, gridBagConstraints);

        btnPlay.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/hersis/activitytracker/images/player_play.png"))); // NOI18N
        btnPlay.setText("Play");
        btnPlay.setPreferredSize(new java.awt.Dimension(140, 58));
        btnPlay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPlayActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(12, 7, 7, 5);
        add(btnPlay, gridBagConstraints);

        btnPause.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/hersis/activitytracker/images/player_pause.png"))); // NOI18N
        btnPause.setText("Pause");
        btnPause.setPreferredSize(new java.awt.Dimension(140, 58));
        btnPause.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPauseActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(12, 5, 7, 5);
        add(btnPause, gridBagConstraints);

        btnStop.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/hersis/activitytracker/images/player_stop.png"))); // NOI18N
        btnStop.setText("Stop");
        btnStop.setPreferredSize(new java.awt.Dimension(140, 58));
        btnStop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStopActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHEAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(12, 5, 7, 7);
        add(btnStop, gridBagConstraints);

        txtStartTime.setBackground(new java.awt.Color(229, 229, 229));
        txtStartTime.setEditable(false);
        txtStartTime.setFont(new java.awt.Font("Dialog", 0, 20)); // NOI18N
        txtStartTime.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtStartTime.setFocusable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(7, 5, 0, 7);
        add(txtStartTime, gridBagConstraints);

        lblStartTime.setText("Hora de inicio:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 7, 0, 5);
        add(lblStartTime, gridBagConstraints);

        lblPausedAt.setText("Pausado en:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 7, 0, 5);
        add(lblPausedAt, gridBagConstraints);

        lblTotalTime.setText("Tiempo total:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 7, 0, 5);
        add(lblTotalTime, gridBagConstraints);

        txtPausedAt.setBackground(new java.awt.Color(229, 229, 229));
        txtPausedAt.setEditable(false);
        txtPausedAt.setFont(new java.awt.Font("Dialog", 0, 20)); // NOI18N
        txtPausedAt.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtPausedAt.setFocusable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(7, 5, 0, 7);
        add(txtPausedAt, gridBagConstraints);

        txtTotalTime.setBackground(new java.awt.Color(229, 229, 229));
        txtTotalTime.setEditable(false);
        txtTotalTime.setFont(new java.awt.Font("Dialog", 0, 20)); // NOI18N
        txtTotalTime.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtTotalTime.setFocusable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(7, 5, 0, 7);
        add(txtTotalTime, gridBagConstraints);

        btnNew.setText("New");
        btnNew.setMinimumSize(new java.awt.Dimension(25, 25));
        btnNew.setPreferredSize(new java.awt.Dimension(25, 25));
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(7, 7, 10, 7);
        add(btnNew, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

	private void btnPlayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPlayActionPerformed
		Controller.play();
	}//GEN-LAST:event_btnPlayActionPerformed

	private void btnPauseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPauseActionPerformed
		Controller.pause();
	}//GEN-LAST:event_btnPauseActionPerformed

	private void btnStopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStopActionPerformed
		Controller.stop();
	}//GEN-LAST:event_btnStopActionPerformed

	private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
		Controller.startTracking();
	}//GEN-LAST:event_btnNewActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnNew;
    private javax.swing.JButton btnPause;
    private javax.swing.JButton btnPlay;
    private javax.swing.JButton btnStop;
    private javax.swing.JComboBox cmbActivities;
    private javax.swing.JLabel lblPausedAt;
    private javax.swing.JLabel lblStartTime;
    private javax.swing.JLabel lblTotalTime;
    private javax.swing.JTextField txtPausedAt;
    private javax.swing.JTextField txtStartTime;
    private javax.swing.JTextField txtTotalTime;
    // End of variables declaration//GEN-END:variables
}

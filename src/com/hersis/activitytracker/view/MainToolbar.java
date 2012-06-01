package com.hersis.activitytracker.view;

import com.hersis.activitytracker.controler.Controller;

/**
 *
 * @author Igor Rodriguez <igorrodriguezelvira@gmail.com>
 */
public class MainToolbar extends javax.swing.JPanel {
	private final Controller controller;

	/**
	 * Creates new form MainToolbar
	 */
	public MainToolbar(Controller controller) {
		this.controller = controller;
		initComponents();
	}

	/**
	 * This method is called from within the constructor to initialize the form. WARNING: Do NOT
	 * modify this code. The content of this method is always regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainToolbar = new javax.swing.JToolBar();
        btnNewActivity = new javax.swing.JButton();
        btnNewTime = new javax.swing.JButton();
        btnViewActivities = new javax.swing.JButton();
        btnViewTimes = new javax.swing.JButton();
        btnBackup = new javax.swing.JButton();
        btnOptions = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout());

        mainToolbar.setFloatable(false);
        mainToolbar.setRollover(true);

        btnNewActivity.setText("Nueva act");
        btnNewActivity.setFocusable(false);
        btnNewActivity.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNewActivity.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnNewActivity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActivityActionPerformed(evt);
            }
        });
        mainToolbar.add(btnNewActivity);

        btnNewTime.setText("Nuevo tiempo");
        btnNewTime.setFocusable(false);
        btnNewTime.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNewTime.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        mainToolbar.add(btnNewTime);

        btnViewActivities.setText("Ver act");
        btnViewActivities.setFocusable(false);
        btnViewActivities.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnViewActivities.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnViewActivities.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewActivitiesActionPerformed(evt);
            }
        });
        mainToolbar.add(btnViewActivities);

        btnViewTimes.setText("Ver tiempo");
        btnViewTimes.setFocusable(false);
        btnViewTimes.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnViewTimes.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        mainToolbar.add(btnViewTimes);

        btnBackup.setText("Backup");
        btnBackup.setFocusable(false);
        btnBackup.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnBackup.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        mainToolbar.add(btnBackup);

        btnOptions.setText("Opciones");
        btnOptions.setFocusable(false);
        btnOptions.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOptions.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        mainToolbar.add(btnOptions);

        add(mainToolbar, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

	private void btnNewActivityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActivityActionPerformed
		controller.newActivity();
	}//GEN-LAST:event_btnNewActivityActionPerformed

	private void btnViewActivitiesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActivitiesActionPerformed
		controller.viewActivities();
	}//GEN-LAST:event_btnViewActivitiesActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBackup;
    private javax.swing.JButton btnNewActivity;
    private javax.swing.JButton btnNewTime;
    private javax.swing.JButton btnOptions;
    private javax.swing.JButton btnViewActivities;
    private javax.swing.JButton btnViewTimes;
    private javax.swing.JToolBar mainToolbar;
    // End of variables declaration//GEN-END:variables
}

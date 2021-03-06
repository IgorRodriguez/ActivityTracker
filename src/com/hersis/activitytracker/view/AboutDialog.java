package com.hersis.activitytracker.view;

import com.hersis.activitytracker.controller.Controller;
import com.hersis.activitytracker.view.util.Locatable;
import java.awt.Frame;

/**
 *
 * @author Igor Rodriguez
 */
public class AboutDialog extends javax.swing.JDialog implements Locatable {

	/**
	 * Creates new form AboutDialog
	 */
	private AboutDialog(java.awt.Frame parent, boolean modal) {
		super(parent, modal);
		initComponents();
	}
	
	public static AboutDialog getInstance(
			final Frame parent, final boolean modal, final String name) {
		AboutDialog aboutDialog = new AboutDialog(parent, modal);
		aboutDialog.setLocationRelativeTo(parent);
		aboutDialog.getRootPane().setDefaultButton(btnClose);
		Controller.registerLocatableWindow(aboutDialog, name);
		
		return aboutDialog;
	}

	/**
	 * This method is called from within the constructor to initialize the form. WARNING: Do NOT
	 * modify this code. The content of this method is always regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        lblApplicationIcon = new javax.swing.JLabel();
        lblApplicationName = new javax.swing.JLabel();
        lblDevelopedBy = new javax.swing.JLabel();
        lblDevelopedByData = new javax.swing.JLabel();
        lblReleaseDate = new javax.swing.JLabel();
        lblReleaseDateData = new javax.swing.JLabel();
        lblLicense = new javax.swing.JLabel();
        lblLicenseData = new javax.swing.JLabel();
        buttonPanel = new javax.swing.JPanel();
        separatorPanel = new javax.swing.JPanel();
        btnClose = new javax.swing.JButton();

        setTitle("About ActivityTracker v2");
        setResizable(false);
        getContentPane().setLayout(new java.awt.GridBagLayout());

        lblApplicationIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/hersis/activitytracker/images/activity_tracker_200.png"))); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 7, 0, 7);
        getContentPane().add(lblApplicationIcon, gridBagConstraints);

        lblApplicationName.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        lblApplicationName.setText("Activity Tracker v2");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(7, 7, 20, 7);
        getContentPane().add(lblApplicationName, gridBagConstraints);

        lblDevelopedBy.setText("Developed by: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 7, 19, 7);
        getContentPane().add(lblDevelopedBy, gridBagConstraints);

        lblDevelopedByData.setFont(new java.awt.Font("Dialog", 2, 12)); // NOI18N
        lblDevelopedByData.setText("Igor Rodríguez");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(7, 0, 19, 20);
        getContentPane().add(lblDevelopedByData, gridBagConstraints);

        lblReleaseDate.setText("Release date:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 7, 19, 7);
        getContentPane().add(lblReleaseDate, gridBagConstraints);

        lblReleaseDateData.setFont(new java.awt.Font("Dialog", 2, 12)); // NOI18N
        lblReleaseDateData.setText("August 2012");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(7, 0, 19, 20);
        getContentPane().add(lblReleaseDateData, gridBagConstraints);

        lblLicense.setText("License: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 7, 19, 7);
        getContentPane().add(lblLicense, gridBagConstraints);

        lblLicenseData.setFont(new java.awt.Font("Dialog", 2, 12)); // NOI18N
        lblLicenseData.setText("Whichever you like. GPLv2?");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(7, 0, 19, 20);
        getContentPane().add(lblLicenseData, gridBagConstraints);

        buttonPanel.setLayout(new java.awt.GridBagLayout());

        separatorPanel.setLayout(new java.awt.BorderLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 3.0;
        gridBagConstraints.weighty = 1.0;
        buttonPanel.add(separatorPanel, gridBagConstraints);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/hersis/activitytracker/images/button_cancel.png"))); // NOI18N
        btnClose.setText("Close");
        btnClose.setToolTipText(null);
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        buttonPanel.add(btnClose, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(7, 7, 7, 7);
        getContentPane().add(buttonPanel, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

	private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
		setVisible(false);
	}//GEN-LAST:event_btnCloseActionPerformed

	
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private static javax.swing.JButton btnClose;
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JLabel lblApplicationIcon;
    private javax.swing.JLabel lblApplicationName;
    private javax.swing.JLabel lblDevelopedBy;
    private javax.swing.JLabel lblDevelopedByData;
    private javax.swing.JLabel lblLicense;
    private javax.swing.JLabel lblLicenseData;
    private javax.swing.JLabel lblReleaseDate;
    private javax.swing.JLabel lblReleaseDateData;
    private javax.swing.JPanel separatorPanel;
    // End of variables declaration//GEN-END:variables
}

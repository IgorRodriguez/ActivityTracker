package com.hersis.activitytracker.view;

import com.hersis.activitytracker.ApplicationProperties;
import com.hersis.activitytracker.BackupPeriod;
import com.hersis.activitytracker.controler.Controller;
import java.io.File;

/**
 *
 * @author Igor Rodriguez <igorrodriguezelvira@gmail.com>
 */
public class BackupConfigDialog extends javax.swing.JDialog {

	/**
	 * Creates new form BackupConfigDialog
	 */
	public BackupConfigDialog(java.awt.Frame parent, boolean modal) {
		super(parent, modal);
		initComponents();
		loadCmbPeriods();
	}

	private void loadCmbPeriods() {
		cmbPeriods.removeAllItems();
		for (BackupPeriod p : BackupPeriod.values()) {
			cmbPeriods.addItem(p);
		}
	}
	
	private void selectBackupPeriodOnComboBox() {
		String propertie = Controller.getPropertie(ApplicationProperties.BACKUP_PERIOD);
		BackupPeriod backupPeriod;
		if (propertie != null) {
			propertie = propertie.toUpperCase();
			backupPeriod = BackupPeriod.valueOf(propertie);
			cmbPeriods.getModel().setSelectedItem(backupPeriod);
		}
	}
	
	private void setBackupPath() {
		String propertie = Controller.getPropertie(ApplicationProperties.BACKUP_PATH);
		if (propertie != null) {
			txtPath.setText(propertie);
		} else {
			txtPath.setText("");
		}
	}
	
	public void loadBackupValues() {
		setBackupPath();
		selectBackupPeriodOnComboBox();
	}
	
	/**
	 * This method is called from within the constructor to initialize the form. WARNING: Do NOT
	 * modify this code. The content of this method is always regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        backupConfigPanel = new javax.swing.JPanel();
        backupPathPanel = new javax.swing.JPanel();
        txtPath = new javax.swing.JTextField();
        btnFindPath = new javax.swing.JButton();
        periodsPanel = new javax.swing.JPanel();
        cmbPeriods = new javax.swing.JComboBox();
        buttonPanel = new javax.swing.JPanel();
        btnCancel = new javax.swing.JButton();
        separatorPanel = new javax.swing.JPanel();
        btnAccept = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        backupConfigPanel.setLayout(new java.awt.GridBagLayout());

        backupPathPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Backup path"));
        backupPathPanel.setLayout(new java.awt.GridBagLayout());

        txtPath.setPreferredSize(new java.awt.Dimension(200, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 10.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(7, 7, 7, 7);
        backupPathPanel.add(txtPath, gridBagConstraints);

        btnFindPath.setText("...");
        btnFindPath.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFindPathActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(7, 7, 7, 7);
        backupPathPanel.add(btnFindPath, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(7, 7, 7, 7);
        backupConfigPanel.add(backupPathPanel, gridBagConstraints);

        periodsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Periodicity"));
        periodsPanel.setLayout(new java.awt.GridBagLayout());

        cmbPeriods.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(7, 7, 7, 7);
        periodsPanel.add(cmbPeriods, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(7, 7, 7, 7);
        backupConfigPanel.add(periodsPanel, gridBagConstraints);

        buttonPanel.setLayout(new java.awt.GridBagLayout());

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/hersis/activitytracker/images/button_cancel.png"))); // NOI18N
        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        buttonPanel.add(btnCancel, gridBagConstraints);

        separatorPanel.setLayout(new java.awt.BorderLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 3.0;
        gridBagConstraints.weighty = 1.0;
        buttonPanel.add(separatorPanel, gridBagConstraints);

        btnAccept.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/hersis/activitytracker/images/button_ok.png"))); // NOI18N
        btnAccept.setText("Accept");
        btnAccept.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAcceptActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        buttonPanel.add(btnAccept, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHEAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 7, 7, 7);
        backupConfigPanel.add(buttonPanel, gridBagConstraints);

        getContentPane().add(backupConfigPanel, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

	private void btnAcceptActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAcceptActionPerformed
		String backupPath = txtPath.getText();
		File filePath = new File(txtPath.getText().trim());
		if (backupPath != null && !"".equals(backupPath.trim()) && filePath.exists()) {
			Controller.setPropertie(ApplicationProperties.BACKUP_PATH, backupPath.trim());
		} else {
			AlertMessages.backupPathNullWhileConfiguring(this);
			return;
		}
		Object selectedPeriod = cmbPeriods.getModel().getSelectedItem();
		if (selectedPeriod != null) {
			Controller.setPropertie(ApplicationProperties.BACKUP_PERIOD, selectedPeriod.toString());
		} else {
			AlertMessages.backupPeriodNull(this);
			return;
		}
		// This will execute only if there is no alert thrown.
		this.setVisible(false);
	}//GEN-LAST:event_btnAcceptActionPerformed

	private void btnFindPathActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFindPathActionPerformed
		SharedFileChooser.setSelectedFile(txtPath.getText().trim());
		int selected = SharedFileChooser.showDirectoryChooser(this, "Select");
		if (selected == SharedFileChooser.APPROVE_OPTION) {
			txtPath.setText(SharedFileChooser.getSelectedFile().getPath());
		}
	}//GEN-LAST:event_btnFindPathActionPerformed

	private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
		this.setVisible(false);
	}//GEN-LAST:event_btnCancelActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel backupConfigPanel;
    private javax.swing.JPanel backupPathPanel;
    private javax.swing.JButton btnAccept;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnFindPath;
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JComboBox cmbPeriods;
    private javax.swing.JPanel periodsPanel;
    private javax.swing.JPanel separatorPanel;
    private javax.swing.JTextField txtPath;
    // End of variables declaration//GEN-END:variables
}

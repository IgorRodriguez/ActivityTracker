package com.hersis.activitytracker.view;

import com.hersis.activitytracker.Activity;
import com.hersis.activitytracker.controler.Controller;

/**
 *
 * @author Igor Rodriguez <igorrodriguezelvira@gmail.com>
 */
public class ActivityDialog extends javax.swing.JDialog {
	private final Controller controller;
	private Activity activity;

	/**
	 * Creates new form ActivityDialog
	 */
	public ActivityDialog(java.awt.Frame parent, boolean modal, Controller controller) {
		super(parent, modal);
		this.controller = controller;
		initComponents();
		this.getRootPane().setDefaultButton(btnAccept);
	}
	
	public void newActivity() {
		setActivity(null);
		clearAllFields();
		btnDelete.setVisible(false);
	}
	
	public Activity getActivity() {
		return activity;
	}

	/**
	 * Sets the <code>Activity</code> value for this panel. 
	 * @param activity The <code>Activity</code> value for this panel. 
	 */
	public void setActivity(Activity activity) {
		this.activity = activity;
		
	}
	
	public Activity getActivityFromFields() {
		String name = txtName.getText();
		String description = txaDescription.getText();
		
		return new Activity(name, description);
	}
	
	public void clearAllFields() {
		txtName.setText("");
		txaDescription.setText("");
	}
	
	public void setVisibleBtnDelete(boolean value) {
		btnDelete.setVisible(value);
	}

	public String getTextName() {
		return txtName.getText();
	}
	
	public void setTextName(String str) {
		txtName.setText(str);
	}

	public String getTextDescription() {
		return txaDescription.getText();
	}
	
	public void setTextDescription(String str) {
		txaDescription.setText(str);
	}
	/**
	 * This method is called from within the constructor to initialize the form. WARNING: Do NOT
	 * modify this code. The content of this method is always regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        activityPanel = new javax.swing.JPanel();
        txtName = new javax.swing.JTextField();
        lblName = new javax.swing.JLabel();
        lblDescription = new javax.swing.JLabel();
        txaDescriptionScrollPane = new javax.swing.JScrollPane();
        txaDescription = new javax.swing.JTextArea();
        buttonPanel = new javax.swing.JPanel();
        btnDelete = new javax.swing.JButton();
        separatorPanel = new javax.swing.JPanel();
        btnCancel = new javax.swing.JButton();
        btnAccept = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Activity editor");

        activityPanel.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.weightx = 5.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 7, 7, 7);
        activityPanel.add(txtName, gridBagConstraints);

        lblName.setText("Name:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 7, 7, 7);
        activityPanel.add(lblName, gridBagConstraints);

        lblDescription.setText("Description:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(7, 7, 7, 7);
        activityPanel.add(lblDescription, gridBagConstraints);

        txaDescriptionScrollPane.setPreferredSize(new java.awt.Dimension(301, 100));

        txaDescription.setColumns(20);
        txaDescription.setRows(5);
        txaDescription.setPreferredSize(new java.awt.Dimension(300, 75));
        txaDescriptionScrollPane.setViewportView(txaDescription);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.weightx = 5.0;
        gridBagConstraints.weighty = 5.0;
        gridBagConstraints.insets = new java.awt.Insets(7, 7, 7, 7);
        activityPanel.add(txaDescriptionScrollPane, gridBagConstraints);

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
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_END;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        activityPanel.add(buttonPanel, gridBagConstraints);

        getContentPane().add(activityPanel, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

	private void btnAcceptActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAcceptActionPerformed
		controller.saveActivity();
	}//GEN-LAST:event_btnAcceptActionPerformed

	private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
		controller.cancelActivityEdition();
	}//GEN-LAST:event_btnCancelActionPerformed

	private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
		controller.deleteActivity();
	}//GEN-LAST:event_btnDeleteActionPerformed

	
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel activityPanel;
    private javax.swing.JButton btnAccept;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnDelete;
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JLabel lblDescription;
    private javax.swing.JLabel lblName;
    private javax.swing.JPanel separatorPanel;
    private javax.swing.JTextArea txaDescription;
    private javax.swing.JScrollPane txaDescriptionScrollPane;
    private javax.swing.JTextField txtName;
    // End of variables declaration//GEN-END:variables
}

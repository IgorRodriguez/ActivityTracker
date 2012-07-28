package com.hersis.activitytracker.view.aux;

/**
 *
 * @author Igor Rodriguez <igorrodriguezelvira@gmail.com>
 */
public class ProgressBarPanel extends javax.swing.JPanel {

	/**
	 * Creates new ProgressBarPanel
	 */
	public ProgressBarPanel() {
		initComponents();
	}
	
	public void setTaskTitle(String title) {
		lblPanelTitle.setText(title);
	}
	
	public String getTaskTitle() {
		return lblPanelTitle.getText();
	}
	
	public void setWaitMessage(String message) {
		lblWaitMessage.setText(message);
	}
	
	public String getWaitMessage() {
		return lblWaitMessage.getText();
	}

	/**
	 * This method is called from within the constructor to initialize the form. WARNING: Do NOT
	 * modify this code. The content of this method is always regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        progressBar = new javax.swing.JProgressBar();
        lblPanelTitle = new javax.swing.JLabel();
        lblWaitMessage = new javax.swing.JLabel();

        setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        setInheritsPopupMenu(true);
        setPreferredSize(new java.awt.Dimension(300, 85));
        setLayout(new java.awt.GridBagLayout());

        progressBar.setIndeterminate(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 7, 7, 7);
        add(progressBar, gridBagConstraints);

        lblPanelTitle.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        lblPanelTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPanelTitle.setText("Performing operation...");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 7, 0, 7);
        add(lblPanelTitle, gridBagConstraints);

        lblWaitMessage.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblWaitMessage.setText("Please, wait until the task finishes.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 7, 10, 7);
        add(lblWaitMessage, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lblPanelTitle;
    private javax.swing.JLabel lblWaitMessage;
    private javax.swing.JProgressBar progressBar;
    // End of variables declaration//GEN-END:variables
}

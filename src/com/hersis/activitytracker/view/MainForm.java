package com.hersis.activitytracker.view;

import com.hersis.activitytracker.controler.Controller;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 *
 * @author Igor Rodriguez <igorrodriguezelvira@gmail.com>
 */
public class MainForm extends javax.swing.JFrame {
	Controller controller;
	/**
	 * Creates new form MainForm
	 */
	public MainForm(Controller controller) {
		this.controller = controller;
		initComponents();
		init();
	}

	private void init() {
		this.addWindowListener(
				new WindowAdapter() {
					@Override
					public void windowClosing(WindowEvent e) {
						controller.exit();
					}
				});
	}
	/**
	 * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
	 * content of this method is always regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainMenuBar = new javax.swing.JMenuBar();
        mnuFile = new javax.swing.JMenu();
        mniBackup = new javax.swing.JMenuItem();
        mniOptions = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        mniExit = new javax.swing.JMenuItem();
        mnuEdit = new javax.swing.JMenu();
        mniNewActivity = new javax.swing.JMenuItem();
        mniNewTime = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        mniViewActivities = new javax.swing.JMenuItem();
        mniViewTimes = new javax.swing.JMenuItem();
        mnuHelp = new javax.swing.JMenu();
        mniAbout = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Activity tracker v2");

        mnuFile.setText("File");

        mniBackup.setText("Backup");
        mniBackup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mniBackupActionPerformed(evt);
            }
        });
        mnuFile.add(mniBackup);

        mniOptions.setText("Options...");
        mnuFile.add(mniOptions);
        mnuFile.add(jSeparator1);

        mniExit.setText("Exit");
        mniExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mniExitActionPerformed(evt);
            }
        });
        mnuFile.add(mniExit);

        mainMenuBar.add(mnuFile);

        mnuEdit.setText("Edition");

        mniNewActivity.setText("New activity");
        mniNewActivity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mniNewActivityActionPerformed(evt);
            }
        });
        mnuEdit.add(mniNewActivity);

        mniNewTime.setText("New time");
        mniNewTime.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mniNewTimeActionPerformed(evt);
            }
        });
        mnuEdit.add(mniNewTime);
        mnuEdit.add(jSeparator2);

        mniViewActivities.setText("View activities");
        mniViewActivities.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mniViewActivitiesActionPerformed(evt);
            }
        });
        mnuEdit.add(mniViewActivities);

        mniViewTimes.setText("View times");
        mniViewTimes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mniViewTimesActionPerformed(evt);
            }
        });
        mnuEdit.add(mniViewTimes);

        mainMenuBar.add(mnuEdit);

        mnuHelp.setText("Help");

        mniAbout.setText("About...");
        mnuHelp.add(mniAbout);

        mainMenuBar.add(mnuHelp);

        setJMenuBar(mainMenuBar);

        pack();
    }// </editor-fold>//GEN-END:initComponents

	private void mniExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mniExitActionPerformed
		controller.exit();
	}//GEN-LAST:event_mniExitActionPerformed

	private void mniNewActivityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mniNewActivityActionPerformed
		controller.showNewActivityWindow();
	}//GEN-LAST:event_mniNewActivityActionPerformed

	private void mniViewActivitiesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mniViewActivitiesActionPerformed
		controller.viewActivitiesWindow();
	}//GEN-LAST:event_mniViewActivitiesActionPerformed

	private void mniNewTimeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mniNewTimeActionPerformed
		controller.showNewTimeWindow();
	}//GEN-LAST:event_mniNewTimeActionPerformed

	private void mniViewTimesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mniViewTimesActionPerformed
		controller.viewTimesWindow();
	}//GEN-LAST:event_mniViewTimesActionPerformed

	private void mniBackupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mniBackupActionPerformed
		controller.showBackupWindow();
	}//GEN-LAST:event_mniBackupActionPerformed

	
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JMenuBar mainMenuBar;
    private javax.swing.JMenuItem mniAbout;
    private javax.swing.JMenuItem mniBackup;
    private javax.swing.JMenuItem mniExit;
    private javax.swing.JMenuItem mniNewActivity;
    private javax.swing.JMenuItem mniNewTime;
    private javax.swing.JMenuItem mniOptions;
    private javax.swing.JMenuItem mniViewActivities;
    private javax.swing.JMenuItem mniViewTimes;
    private javax.swing.JMenu mnuEdit;
    private javax.swing.JMenu mnuFile;
    private javax.swing.JMenu mnuHelp;
    // End of variables declaration//GEN-END:variables
}

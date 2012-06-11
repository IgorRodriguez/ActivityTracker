package com.hersis.activitytracker.view;

import com.hersis.activitytracker.Activity;
import com.hersis.activitytracker.controler.Controller;
import com.hersis.activitytracker.view.aux.ActivityListTableModel;
import com.hersis.activitytracker.view.aux.CustomDefaultTableCellRenderer;
import java.awt.Point;
import java.util.ArrayList;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author Igor Rodriguez <igorrodriguezelvira@gmail.com>
 */
public class ActivityListDialog extends javax.swing.JDialog {
	private final Controller controller;
	private ActivityListTableModel activityListModel = new ActivityListTableModel();
	int previousRow = -1;

	/**
	 * Creates new form ActivityListDialog
	 */
	public ActivityListDialog(java.awt.Frame parent, boolean modal, Controller controller) {
		super(parent, modal);
		this.controller = controller;
		initComponents();
		this.getRootPane().setDefaultButton(btnClose);
	}

	public void updateActivityTable(ArrayList<Activity> activities) {
		activityListModel.removeAllActivities();
		for (Activity a : activities) {
			activityListModel.addActivity(a);
		}
   
        TableColumnModel cm = tblActivities.getColumnModel();
        cm.getColumn(0).setPreferredWidth(60);
        cm.getColumn(1).setPreferredWidth(100);
        
        tblActivities.setDefaultRenderer(String.class, new CustomDefaultTableCellRenderer());
    }
	
	public void selectPreviousRow() {
		int rowCount = activityListModel.getRowCount();
		
		if (previousRow >= 0 && rowCount > 0) {
			if (rowCount > previousRow) {
				tblActivities.getSelectionModel().setSelectionInterval(previousRow, previousRow);
			} else if (rowCount == previousRow) {
				tblActivities.getSelectionModel().setSelectionInterval(previousRow - 1, previousRow - 1);	
			} else {
				int row = rowCount - 1;
				tblActivities.getSelectionModel().setSelectionInterval(row, row);
			} 
				
		}
	}
	
	public void selectLastInsertedRow(Activity activity) {
		int index = activityListModel.getActivityIndex(activity);
		tblActivities.getSelectionModel().setSelectionInterval(index, index);
	}
	
	@Override
	public void setVisible(boolean value) {
		super.setVisible(value);
	}
	
	/**
	 * This method is called from within the constructor to initialize the form. WARNING: Do NOT
	 * modify this code. The content of this method is always regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        activityListPanel = new javax.swing.JPanel();
        tblActivitiesScrollPane = new javax.swing.JScrollPane();
        tblActivities = new javax.swing.JTable();
        bottomButtonPanel = new javax.swing.JPanel();
        btnDelete = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();
        separatorPanel = new javax.swing.JPanel();
        rightButtonPanel = new javax.swing.JPanel();
        btnEdit = new javax.swing.JButton();
        btnNew = new javax.swing.JButton();
        separatorRightPanel1 = new javax.swing.JPanel();
        separatorRightPanel2 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Activity list");
        setName("ActivityListDialog");

        activityListPanel.setLayout(new java.awt.GridBagLayout());

        tblActivitiesScrollPane.setPreferredSize(new java.awt.Dimension(400, 300));

        tblActivities.setModel(activityListModel);
        tblActivities.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblActivities.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblActivitiesMouseClicked(evt);
            }
        });
        tblActivitiesScrollPane.setViewportView(tblActivities);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 10.0;
        gridBagConstraints.weighty = 7.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 7, 7, 7);
        activityListPanel.add(tblActivitiesScrollPane, gridBagConstraints);

        bottomButtonPanel.setLayout(new java.awt.GridBagLayout());

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/hersis/activitytracker/images/trashcan_full.png"))); // NOI18N
        btnDelete.setText("Delete");
        btnDelete.setMinimumSize(new java.awt.Dimension(100, 42));
        btnDelete.setPreferredSize(new java.awt.Dimension(100, 42));
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        bottomButtonPanel.add(btnDelete, gridBagConstraints);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/hersis/activitytracker/images/button_cancel.png"))); // NOI18N
        btnClose.setText("Close");
        btnClose.setMinimumSize(new java.awt.Dimension(100, 42));
        btnClose.setPreferredSize(new java.awt.Dimension(100, 42));
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        bottomButtonPanel.add(btnClose, gridBagConstraints);

        separatorPanel.setPreferredSize(new java.awt.Dimension(104, 30));

        javax.swing.GroupLayout separatorPanelLayout = new javax.swing.GroupLayout(separatorPanel);
        separatorPanel.setLayout(separatorPanelLayout);
        separatorPanelLayout.setHorizontalGroup(
            separatorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 207, Short.MAX_VALUE)
        );
        separatorPanelLayout.setVerticalGroup(
            separatorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 42, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 2.0;
        gridBagConstraints.weighty = 1.0;
        bottomButtonPanel.add(separatorPanel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_END;
        gridBagConstraints.weightx = 4.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(7, 7, 7, 7);
        activityListPanel.add(bottomButtonPanel, gridBagConstraints);

        rightButtonPanel.setLayout(new java.awt.GridBagLayout());

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/hersis/activitytracker/images/txt.png"))); // NOI18N
        btnEdit.setText("Edit");
        btnEdit.setPreferredSize(new java.awt.Dimension(100, 42));
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        rightButtonPanel.add(btnEdit, gridBagConstraints);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/hersis/activitytracker/images/empty.png"))); // NOI18N
        btnNew.setText("New");
        btnNew.setPreferredSize(new java.awt.Dimension(100, 42));
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        rightButtonPanel.add(btnNew, gridBagConstraints);

        javax.swing.GroupLayout separatorRightPanel1Layout = new javax.swing.GroupLayout(separatorRightPanel1);
        separatorRightPanel1.setLayout(separatorRightPanel1Layout);
        separatorRightPanel1Layout.setHorizontalGroup(
            separatorRightPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 135, Short.MAX_VALUE)
        );
        separatorRightPanel1Layout.setVerticalGroup(
            separatorRightPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 14, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        rightButtonPanel.add(separatorRightPanel1, gridBagConstraints);

        javax.swing.GroupLayout separatorRightPanel2Layout = new javax.swing.GroupLayout(separatorRightPanel2);
        separatorRightPanel2.setLayout(separatorRightPanel2Layout);
        separatorRightPanel2Layout.setHorizontalGroup(
            separatorRightPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 135, Short.MAX_VALUE)
        );
        separatorRightPanel2Layout.setVerticalGroup(
            separatorRightPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 150, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 10.0;
        rightButtonPanel.add(separatorRightPanel2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridheight = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_END;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 7.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 7, 7, 7);
        activityListPanel.add(rightButtonPanel, gridBagConstraints);

        getContentPane().add(activityListPanel, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

	private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
		controller.closeActivityList();
	}//GEN-LAST:event_btnCloseActionPerformed

	private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
		controller.showNewActivityWindow();
	}//GEN-LAST:event_btnNewActionPerformed

	private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
		Activity selectedActivity;
		int selectedRow = tblActivities.getSelectedRow();
		previousRow = selectedRow;
		if (selectedRow != -1) {
			selectedActivity = activityListModel.getActivityAt(selectedRow);
		} else {
			selectedActivity = null;
		}
		controller.showEditActivityWindow(selectedActivity);
	}//GEN-LAST:event_btnEditActionPerformed

	private void tblActivitiesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblActivitiesMouseClicked
		if (evt.getClickCount() > 1) {
			int row = tblActivities.rowAtPoint(new Point(evt.getX(), evt.getY()));
			controller.showEditActivityWindow(activityListModel.getActivityAt(row));
		}
	}//GEN-LAST:event_tblActivitiesMouseClicked

	private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
		Activity selectedActivity;
		int selectedRow = tblActivities.getSelectedRow();
		previousRow = selectedRow;
		if (selectedRow != -1) {
			selectedActivity = activityListModel.getActivityAt(selectedRow);
		} else {
			selectedActivity = null;
		}
		controller.deleteActivity(this, selectedActivity);
	}//GEN-LAST:event_btnDeleteActionPerformed

	
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel activityListPanel;
    private javax.swing.JPanel bottomButtonPanel;
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnNew;
    private javax.swing.JPanel rightButtonPanel;
    private javax.swing.JPanel separatorPanel;
    private javax.swing.JPanel separatorRightPanel1;
    private javax.swing.JPanel separatorRightPanel2;
    private javax.swing.JTable tblActivities;
    private javax.swing.JScrollPane tblActivitiesScrollPane;
    // End of variables declaration//GEN-END:variables
}

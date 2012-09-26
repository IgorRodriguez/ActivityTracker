package com.hersis.activitytracker.view.util;

import com.hersis.activitytracker.Activity;
import com.hersis.activitytracker.controller.Controller;
import java.awt.Component;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author Igor Rodriguez <igorrodriguezelvira@gmail.com>
 * @since 2012-06-04
 */
public class ActivityIdTableCellRenderer extends JLabel implements TableCellRenderer{
	private final ArrayList<Activity> activities;

	public ActivityIdTableCellRenderer() {
		super();
		this.activities = Controller.getActivities();
	}
	
    @Override
    public Component getTableCellRendererComponent(JTable table, Object number,
            boolean isSelected, boolean hasFocus, int row, int column) {
        
        DefaultTableCellRenderer defaultRenderer = new DefaultTableCellRenderer();
        Component renderer = defaultRenderer.getTableCellRendererComponent(table, 
                    number, isSelected, hasFocus, row, column);
        
        int value = (Integer) number;
		String activityName = "";
		for (Activity a : activities) {
			if (a.getIdActivity() == value) {
				activityName = a.getName();
				break;
			}
		}

        this.setText(activityName);
        this.setBackground(renderer.getBackground());
        this.setFont(renderer.getFont());
        this.setForeground(renderer.getForeground());
        this.setOpaque(true);
        
        return this;
    }
}

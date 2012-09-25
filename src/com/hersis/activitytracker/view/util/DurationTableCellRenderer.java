package com.hersis.activitytracker.view.util;

import com.hersis.activitytracker.controler.Controller;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author Igor Rodriguez <igorrodriguezelvira@gmail.com>
 * @since 2012-06-04
 */
public class DurationTableCellRenderer extends JLabel implements TableCellRenderer{
	
    @Override
    public Component getTableCellRendererComponent(JTable table, Object number,
            boolean isSelected, boolean hasFocus, int row, int column) {
        
        DefaultTableCellRenderer defaultRenderer = new DefaultTableCellRenderer();
        Component renderer = defaultRenderer.getTableCellRendererComponent(table, 
                    number, isSelected, hasFocus, row, column);
        
        long value = (long) number;

        this.setText(Controller.getDurationString(value));
		this.setHorizontalAlignment(CENTER);
        this.setBackground(renderer.getBackground());
        this.setFont(renderer.getFont());
        this.setForeground(renderer.getForeground());
        this.setOpaque(true);
        
        return this;
    }
}

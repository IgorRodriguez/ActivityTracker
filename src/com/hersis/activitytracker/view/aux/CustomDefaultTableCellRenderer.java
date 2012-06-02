package com.hersis.activitytracker.view.aux;

import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author Igor Rodriguez <igorrodriguezelvira@gmail.com>
 * @since 2012-04-15
 */
public class CustomDefaultTableCellRenderer extends JLabel implements TableCellRenderer{
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        
        DefaultTableCellRenderer defaultRenderer = new DefaultTableCellRenderer();
        Component renderer = defaultRenderer.getTableCellRendererComponent(table, 
                    value, isSelected, hasFocus, row, column);
        
        this.setText(value.toString());
        this.setBackground(renderer.getBackground());
        this.setFont(renderer.getFont());
        this.setForeground(renderer.getForeground());
        this.setOpaque(true);
        
        return this;
    }
}

package com.hersis.activitytracker.view.aux;

import java.awt.Component;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author Igor Rodriguez <igorrodriguezelvira@gmail.com>
 * @since 2012-06-04
 */
public class TimestampTableCellRenderer extends JLabel implements TableCellRenderer{
    @Override
    public Component getTableCellRendererComponent(JTable table, Object date,
            boolean isSelected, boolean hasFocus, int row, int column) {
        
        DefaultTableCellRenderer defaultRenderer = new DefaultTableCellRenderer();
        Component renderer = defaultRenderer.getTableCellRendererComponent(table, 
                    date, isSelected, hasFocus, row, column);
        
        Timestamp value = (Timestamp) date;
        DateFormat formatter = new SimpleDateFormat();

        this.setText(formatter.format(value.getTime()));
        this.setBackground(renderer.getBackground());
        this.setFont(renderer.getFont());
        this.setForeground(renderer.getForeground());
        this.setOpaque(true);
        
        return this;
    }
}

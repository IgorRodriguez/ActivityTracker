package com.hersis.activitytracker.view.aux;

import com.hersis.activitytracker.Activity;
import java.util.ArrayList;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

/**
 * Model for <code>ActivityList</code>. Each row is an <code>Activity</code> and the
 * columns are the data of that Activity.
 * Implements TableModel and two methods for adding and deleting activities from 
 * the model.
 * @author Igor Rodriguez <igorrodriguezelvira@gmail.com>
 * @since 2012-06-02
 */
public class ActivityListTableModel implements TableModel{
    private ArrayList<Activity> activities = new ArrayList<>();
    private ArrayList<String> columnNames = new ArrayList<>();
    private ArrayList<TableModelListener> listeners = new ArrayList<>();
    
    public ActivityListTableModel() {
        columnNames.add("Name");
        columnNames.add("Description");
    }
    
    /** Returns the number of rows in the model. A
     * <code>JTable</code> uses this method to determine how many rows it
     * should display.  This method should be quick, as it
     * is called frequently during rendering.
     *
     * @return the number of rows in the model
     * @see #getColumnCount
     *
     */
    @Override
    public int getRowCount() {
        return activities.size();
    }

    /** Returns the number of columns in the model. A
     * <code>JTable</code> uses this method to determine how many columns it
     * should create and display by default.
     *
     * @return the number of columns in the model
     * @see #getRowCount
     *
     */
    @Override
    public int getColumnCount() {
        return columnNames.size();
    }

    /** Returns the name of the column at <code>columnIndex</code>.  This is used
     * to initialize the table column header name.  Note: this name does
     * not need to be unique; two columns in a table can have the same name.
     *
     * @param	columnIndex	the index of the column
     * @return  the name of the column
     *
     */
    @Override
    public String getColumnName(int columnIndex) {
        if(columnIndex < columnNames.size() && columnIndex >= 0) {
            return columnNames.get(columnIndex);
        } else {
            return null;
        }
    }

    /** Returns the most specific superclass for all the cell values
     * in the column.  This is used by the <code>JTable</code> to set up a
     * default renderer and editor for the column.
     *
     * @param columnIndex  the index of the column
     * @return the common ancestor class of the object values in the model.
     *
     */
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        // Returns the class of each column
        if (columnIndex == columnNames.indexOf("Name")) {
            return String.class;
        } else if (columnIndex == columnNames.indexOf("Description")) {
            return String.class;
        } else {
            return Object.class;
        }
    }

    /** Returns true if the cell at <code>rowIndex</code> and
     * <code>columnIndex</code>
     * is editable.  Otherwise, <code>setValueAt</code> on the cell will not
     * change the value of that cell.
     *
     * @param	rowIndex	the row whose value to be queried
     * @param	columnIndex	the column whose value to be queried
     * @return	true if the cell is editable
     * @see #setValueAt
     *
     */
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    /** Returns the value for the cell at <code>columnIndex</code> and
     * <code>rowIndex</code>.
     *
     * @param	rowIndex	the row whose value is to be queried
     * @param	columnIndex 	the column whose value is to be queried
     * @return	the value Object at the specified cell
     *
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Activity activity;
        
        // Get the Activity from the indicated row.
        activity = activities.get(rowIndex);
        
        // Get the appropriate field from the columnIndex
        if (columnIndex == columnNames.indexOf("Name")) {
            return activity.getName();
        } else if (columnIndex == columnNames.indexOf("Description")) {
            return activity.getDescription();
        } else {
            return null;
        }        
    }
    
    /**
     * Returns the Activity in the given row.
     * @param rowIndex
     * @return The Activity that is shown in the given row.
     */
    public Activity getActivityAt(int rowIndex) {
        return activities.get(rowIndex);
    }

    /** Sets the value in the cell at <code>columnIndex</code> and
     * <code>rowIndex</code> to <code>aValue</code>.
     *
     * @param	aValue		 the new value
     * @param	rowIndex	 the row whose value is to be changed
     * @param	columnIndex 	 the column whose value is to be changed
     * @see #getValueAt
     * @see #isCellEditable
     */
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        // Get the Activity from the given row
        Activity activity = activities.get(rowIndex);
        
        // Change the field of Activity given by rowIndex, setting aValue.
        if (columnIndex == columnNames.indexOf("Name")) {
            activity.setName((String) aValue);
        } else if (columnIndex == columnNames.indexOf("Description")) {
            activity.setDescription((String) aValue);
        } 
        
        // Notify observers
        TableModelEvent event = new TableModelEvent (this, rowIndex, rowIndex, 
            columnIndex);
        
        // And send to the observers
        notifyObservers(event);
    }

    /** Adds a listener to the list that is notified each time a change
     * to the data model occurs.
     *
     * @param	l   the TableModelListener
     *
     */
    @Override
    public void addTableModelListener(TableModelListener l) {
        // Add the observer to the list.
        listeners.add(l);
    }

    /** Removes a listener from the list that is notified each time a
     * change to the data model occurs.
     *
     * @param	l	the TableModelListener
     *
     */
    @Override
    public void removeTableModelListener(TableModelListener l) {
        listeners.remove(l);
    }
    
    /**
     * Removes a Activity from the given row.
     * @param row The row to be deleted.
     */
    public void deleteActivity(int row) {
        // Remove the row
        activities.remove(row);
        
        // Notify observers
        TableModelEvent event = new TableModelEvent(this, row, row, 
                TableModelEvent.ALL_COLUMNS, TableModelEvent.DELETE);
        
        // And send to the observers
        notifyObservers(event);
    }
    
    /**
     * Adds a Activity to the model.
     * @param msg The Activity to add to the model.
     */
    public void addActivity(Activity activity) {
        // Add the Activity to the model
        activities.add(activity);
        
        // Notify observers
        TableModelEvent event = new TableModelEvent (this, this.getRowCount()-1,
            this.getRowCount()-1, TableModelEvent.ALL_COLUMNS, TableModelEvent.INSERT);
        
        // And send to the observers
        notifyObservers(event);
    }
    
    private void notifyObservers(TableModelEvent event) {
        for (TableModelListener l : listeners) {
            l.tableChanged(event);
        }
    }

	public void removeAllActivities() {
		activities.clear();
	}

	public int getActivityIndex(Activity activity) {
		int index = -1;
		
		if (activity.getIdActivity() > -1) {
			index = activities.indexOf(activity);
		} else {
			for (Activity a : activities) {
				if (a.getName().equals(activity.getName())) {
					index = activities.indexOf(a);
					break;
				}
			}
		}
		
		return index;
	}
}

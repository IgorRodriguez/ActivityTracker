package com.hersis.activitytracker.view.util;

import com.hersis.activitytracker.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

/**
 * Model for <code>TimeList</code>. Each row is a <code>Time</code> and the
 * columns are the data of that Time.
 * Implements TableModel and some methods for adding and deleting times from 
 * the model.
 * @author Igor Rodriguez <igorrodriguezelvira@gmail.com>
 * @since 2012-06-02
 */
public class TimeListTableModel implements TableModel{
	private static final String ACTIVITY_COLUMN = "Activity";
	private static final String START_TIME_COLUMN = "Start time";
	private static final String END_TIME_COLUMN = "End time";
	private static final String DURATION_COLUMN = "Duration";
	private static final String DESCRIPTION_COLUMN = "Description";
	
    private ArrayList<Time> times = new ArrayList<>();
    private ArrayList<String> columnNames = new ArrayList<>();
    private ArrayList<TableModelListener> listeners = new ArrayList<>();
    
    public TimeListTableModel() {
        columnNames.add(ACTIVITY_COLUMN);
        columnNames.add(START_TIME_COLUMN);
        columnNames.add(END_TIME_COLUMN);
        columnNames.add(DURATION_COLUMN);
        columnNames.add(DESCRIPTION_COLUMN);
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
        return times.size();
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
        if (columnIndex == columnNames.indexOf(ACTIVITY_COLUMN)) {
            return Integer.class;
        } else if (columnIndex == columnNames.indexOf(START_TIME_COLUMN)) {
            return Timestamp.class;
        } else if (columnIndex == columnNames.indexOf(END_TIME_COLUMN)) {
            return Timestamp.class;
        } else if (columnIndex == columnNames.indexOf(DURATION_COLUMN)) {
            return Long.class;
        } else if (columnIndex == columnNames.indexOf(DESCRIPTION_COLUMN)) {
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
        Time time;
        
		if (rowIndex < times.size()) {
			// Get the Activity from the indicated row.
			time = times.get(rowIndex);

			// Get the appropriate field from the columnIndex
			 if (columnIndex == columnNames.indexOf(ACTIVITY_COLUMN)) {
				return time.getIdActivity();
			} else if (columnIndex == columnNames.indexOf(START_TIME_COLUMN)) {
				return time.getStartTime();
			} else if (columnIndex == columnNames.indexOf(END_TIME_COLUMN)) {
				return time.getEndTime();
			} else if (columnIndex == columnNames.indexOf(DURATION_COLUMN)) {
				return time.getDuration();
			} else if (columnIndex == columnNames.indexOf(DESCRIPTION_COLUMN)) {
				return time.getDescription();
			} else {
				return null;
			}   
		}
		return null;
    }
    
    /**
     * Returns the Time in the given row.
     * @param rowIndex
     * @return The Activity that is shown in the given row.
     */
    public Time getTimeAt(int rowIndex) {
		Time time = null;
		
		if (rowIndex < times.size()) {
			time = times.get(rowIndex);
		}
		return time;
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
        Time time = times.get(rowIndex);
        
        // Change the field of Activity given by rowIndex, setting aValue.
        if (columnIndex == columnNames.indexOf(ACTIVITY_COLUMN)) {
            time.setIdActivity((Integer) aValue);
        } else if (columnIndex == columnNames.indexOf(START_TIME_COLUMN)) {
            time.setStartTime((Timestamp) aValue);
        } else if (columnIndex == columnNames.indexOf(END_TIME_COLUMN)) {
            time.setEndTime((Timestamp) aValue);
        } else if (columnIndex == columnNames.indexOf(DURATION_COLUMN)) {
            time.setDuration((Long) aValue);
        } else if (columnIndex == columnNames.indexOf(DESCRIPTION_COLUMN)) {
            time.setDescription((String) aValue);
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
     * Removes a Time from the given row.
     * @param row The row to be deleted.
     */
    public void deleteTime(int row) {
		if (row < times.size()) {
			// Remove the row
			times.remove(row);

			// Notify observers
			TableModelEvent event = new TableModelEvent(this, row, row, 
					TableModelEvent.ALL_COLUMNS, TableModelEvent.DELETE);

			// And send to the observers
			notifyObservers(event);
		}
    }
    
    /**
     * Adds a Time to the model.
     * @param msg The Activity to add to the model.
     */
    public void addTime(Time time) {
        // Add the Activity to the model
        times.add(time);
        
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

	public void removeAllTimes() {
		times.clear();
	}

	public int getTimeIndex(Time time) {
		int index = -1;
		
		if (time.getIdTime() > -1) {
			index = times.indexOf(time);
		} else {
			for (Time t : times) {
				boolean actId = t.getIdActivity() == time.getIdActivity();
				boolean sTime = t.getStartTime().equals(time.getStartTime());
				boolean eTime = t.getEndTime().equals(time.getEndTime());
				boolean dur = t.getDuration() == time.getDuration();
				boolean desc = t.getDescription().equals(time.getDescription());
				
				if (actId == true && sTime == true && eTime == true && dur == true && desc == true) {
					index = times.indexOf(t);
					break;
				}
			}
		}
		
		return index;
	}
}

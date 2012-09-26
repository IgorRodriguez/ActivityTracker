package com.hersis.activitytracker.view.util;

import java.awt.Dimension;
import java.awt.Rectangle;

/**
 * 
 * @author Igor Rodriguez <igorrodriguezelvira@gmail.com>
 */
public interface Locatable {	
	/**
	 * Sets the position of the window to the value of <code>position</code> parameter.
	 * @param position A rectangle that specifies the window's position, width and height.
	 */
	void setBounds(final Rectangle position);
	
	/**
	 * Position of the window.
	 * @return A rectangle that specifies the window's position, width and height.
	 */
	Rectangle getBounds();	
	
	/**
	 * Returns the name of the Locatable object.
	 * @return The name of the Locatable object.
	 */
	String getName();
	
	/**
	 * Sets the name of the component.
	 */
	void setName(final String name);
	
	/**
	 * Returns the preferred size of the component.
	 * @return The preferred size of the component.
	 */
	Dimension getPreferredSize();
}
package com.hersis.activitytracker.controller.util;

import com.hersis.activitytracker.view.util.Locatable;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Igor Rodriguez <igorrodriguezelvira@gmail.com>
 */
public class WindowPositionRecorder {
	private static final Logger log = 
			(Logger) LoggerFactory.getLogger("controller.util.WindowPositionRecorder");
	private final ArrayList<Locatable> locatableWindows = new ArrayList<>();
	private final Properties appProperties;
	private boolean locatingWindows = true;
	
	public WindowPositionRecorder(final Properties properties) {
		this.appProperties = properties;
	}

	public void locateWindows() {
		// Exit
		if (!locatingWindows) { return; }
		
		for (final Locatable window : locatableWindows) {
			Rectangle position = deserializeWindowPosition(window);
			final Rectangle defaultPosition = getDefaultPositionValues(window);
			
			if (position == null) {
				position = defaultPosition;
			}
			
			adjustWindowPositionToScreen(position);
			
			log.debug("Window's bounds = {}", window.getBounds());
			log.debug("New bounds = {}", position);
			window.setBounds(position);		
		}
	}

	public void savePositionOfWindows() {
		for (final Locatable window : locatableWindows) {
			appProperties.setProperty(window.getName(), serializeWindowPosition(window));
		}
	}
	
	private String serializeWindowPosition(final Locatable window) {
		final Rectangle bounds = window.getBounds();
		String positionString = "";
		
		positionString += bounds.x + " ";
		positionString += bounds.y + " ";
		positionString += bounds.width + " ";
		positionString += bounds.height;
		
		return positionString;
	}

	private Rectangle deserializeWindowPosition(final Locatable window) {
		final String positionString = appProperties.getProperty(window.getName());
		
		// Exit
		if (positionString == null) {
			log.debug("The position of the window {} is not saved in the properties file.", 
					window.getName());
			return null;
		}
		
		final String [] values = positionString.split(" ");
		
		// Exit
		if (values.length < 4) { return null; }
		
		final Rectangle rectangle = new Rectangle();
		
		try {
			rectangle.x = Integer.parseInt(values[0]);
			rectangle.y = Integer.parseInt(values[1]);
			rectangle.width = Integer.parseInt(values[2]);
			rectangle.height = Integer.parseInt(values[3]);
		} catch (NumberFormatException ex) {
			log.debug("Unable to deserialize the window position of {}. Serialized position: {}", 
					window.getName(), serializeWindowPosition(window));
			return null;
		}
		
		return rectangle;		
	}
	
	private Rectangle adjustWindowPositionToScreen(final Rectangle windowPosition) {
		// Get the size of the screen
		final Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		
		if (windowPosition.width > dim.width) {
			windowPosition.width = dim.width;
		}
		if (windowPosition.height > dim.height) {
			windowPosition.height = dim.height;
		}
		if (windowPosition.x < 0) {
			windowPosition.x = 0;
		} else if (windowPosition.x > dim.width - windowPosition.width) {
			windowPosition.x = dim.width - windowPosition.width;
		}
		if (windowPosition.y < 0) {
			windowPosition.y = 0;
		} else if (windowPosition.y > dim.height - windowPosition.height) {
			windowPosition.y = dim.height - windowPosition.height;
		}
		
		return windowPosition;
	}
	
	private Rectangle getDefaultPositionValues(final Locatable window) {
		final Rectangle position = new Rectangle();
		// Get the size of the screen
		final Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

		// Determine the new location of the window
		position.width = window.getPreferredSize().width;
		position.height = window.getPreferredSize().height;
		position.x = (dim.width - position.width) / 2;
		position.y = (dim.height - position.height) / 2;
		
		return position;
	}
	
	public void registerLocatableWindow(final Locatable window, final String name) {
		window.setName(name);
		locatableWindows.add(window);
	}
	
	public void unregisterLocatableWindow(Locatable window) {
		locatableWindows.remove(window);
	}
	
	public void resetWindowPositions() {
		for (Locatable window : locatableWindows) {
			window.setBounds(getDefaultPositionValues(window));
		}
	}
	
	public void isLocatingWindows(final boolean value) {
		this.locatingWindows = value;
	}
}

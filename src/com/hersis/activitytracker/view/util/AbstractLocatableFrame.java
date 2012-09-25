package com.hersis.activitytracker.view.util;

import javax.swing.JFrame;

/**
 *
 * @author Igor Rodriguez <igorrodriguezelvira@gmail.com>
 */
public class AbstractLocatableFrame extends JFrame implements Locatable {
	
	protected AbstractLocatableFrame(final String name) {
		this.setName(name);
	}
}

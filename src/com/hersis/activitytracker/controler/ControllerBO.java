package com.hersis.activitytracker.controler;

import com.hersis.activitytracker.images.Icons;
import javax.swing.UIManager;

/**
 *
 * @author Igor Rodriguez <igorrodriguezelvira@gmail.com>
 */
class ControllerBO {
	private static final Icons ICONS = new Icons();
	
	void modifyLookAndFeel() {
		// Change of the default icons for JOptionPanes
		UIManager.put("OptionPane.informationIcon", ICONS.getIcon(Icons.IconValues.INFO));
		UIManager.put("OptionPane.errorIcon", ICONS.getIcon(Icons.IconValues.ERROR));
		UIManager.put("OptionPane.questionIcon", ICONS.getIcon(Icons.IconValues.QUESTION));
		UIManager.put("OptionPane.warningIcon", ICONS.getIcon(Icons.IconValues.WARNING));
	}
	
}

package com.hersis.activitytracker.controler;

import com.hersis.activitytracker.Activity;
import com.hersis.activitytracker.images.Icons;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import javax.swing.UIManager;

/**
 *
 * @author Igor Rodriguez <igorrodriguezelvira@gmail.com>
 */
class ControllerBO {
	private static final Icons ICONS = new Icons();
	
	/**
	 * Changes some things on the default LookAndFeel, such as alert message's icons.
	 */
	void modifyLookAndFeel() {
		// Change of the default icons for JOptionPanes
		UIManager.put("OptionPane.informationIcon", ICONS.getIcon(Icons.IconValues.INFO));
		UIManager.put("OptionPane.errorIcon", ICONS.getIcon(Icons.IconValues.ERROR));
		UIManager.put("OptionPane.questionIcon", ICONS.getIcon(Icons.IconValues.QUESTION));
		UIManager.put("OptionPane.warningIcon", ICONS.getIcon(Icons.IconValues.WARNING));
	}
	
	/**
	 * Orders the given list of <code>Activity</code> in the order in which his ACTIVITY_ID appears 
	 * in the LinkedHashSet.
	 * @param activities A list of <code>Activity</code> to be ordered.
	 * @param activityIds a LinkedHashSet containing ACTIVITY_ID Integers in the desired order.
	 * @return The ordered list.
	 */
	ArrayList<Activity> orderActivitiesByTime(ArrayList<Activity> activities, LinkedHashSet<Integer> activityIds) {
		ArrayList<Activity> reorderedActivities = new ArrayList<>();
		for (int i : new LinkedHashSet<>(activityIds)) {
			for (Activity a : activities) {
				if (i == a.getIdActivity()) {
					reorderedActivities.add(a);
					break;
				}
			}
		}
		return reorderedActivities;
	}
}

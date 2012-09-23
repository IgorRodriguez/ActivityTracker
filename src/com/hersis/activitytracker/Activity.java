package com.hersis.activitytracker;

import java.util.Objects;

/**
 *
 * @author Igor Rodriguez <igorrodriguezelvira@gmail.com>
 */
public class Activity implements Comparable{
	private int idActivity = -1;
	private String name;
	private String description = "";
	// Maximum length of the name field. Defines the length of the database field at table creation.
	private static final int nameFieldLength = 35;
	// Maximum length of the description field. Defines the length of the database field at table creation.
	private static final int descriptionFieldLength = 200;

	public Activity(String name) {
		setName(name);
	}

	public Activity(String name, String description) {
		setName(name);
		setDescription(description);
	}

	public Activity(int idActivity, String name, String description) {
		this.idActivity = idActivity;
		setName(name);
		setDescription(description);
	}

	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description of the activity, limited by the number of characters given by 
	 * getDescriptionFieldLength(). If the string passed is longer, it will be truncated.
	 * @param description The description for the activity.
	 */
	public final void setDescription(String description) {
		description = description.trim();
		if (description.length() > getDescriptionFieldLength()) {
			description = description.substring(0, getDescriptionFieldLength());
		}
		
		this.description = description;
	}

	public int getIdActivity() {
		return idActivity;
	}

	private void setIdActivity(int idActivity) {
		this.idActivity = idActivity;
	}

	public String getName() {
		return name;
	}
	/**
	 * Sets the name of the activity, limited by the number of characters given by 
	 * getNameFieldLength(). If the string passed is longer, it will be truncated.
	 * @param description The name of the activity.
	 */
	public final void setName(String name) {
		name = name.trim();
		if (name.length() > getNameFieldLength()) {
			name = name.substring(0, getNameFieldLength());
		}
		
		this.name = name;
	}

	/**
	 * Returns the maximum character length allowed for the description property of Activity.
	 * @return The maximum character length of the description property.
	 */
	public static int getDescriptionFieldLength() {
		return descriptionFieldLength;
	}
	
	/**
	 * Returns the maximum character length allowed for the name property of Activity.
	 * @return The maximum character length of the name property.
	 */
	public static int getNameFieldLength() {
		return nameFieldLength;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Activity other = (Activity) obj;
		if (!Objects.equals(this.name, other.name)) {
			return false;
		}
		if (!Objects.equals(this.description, other.description)) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 47 * hash + Objects.hashCode(this.name);
		hash = 47 * hash + Objects.hashCode(this.description);
		return hash;
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public int compareTo(Object o) {
		if (o instanceof Activity) {
			return compareTo((Activity) o);
		} else {
			throw new ClassCastException();
		}
	}
	
	public int compareTo(Activity a) {
		int nam = this.getName().compareTo(a.getName());
		int desc = this.getDescription().compareTo((a.getDescription()));
		
		if (nam != 0) {
			return nam;
		}
		else {
			return desc;
		}
	}
	
}

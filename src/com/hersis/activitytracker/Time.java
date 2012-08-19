package com.hersis.activitytracker;

import com.hersis.activitytracker.model.Dao;
import java.sql.Timestamp;
import java.util.Objects;

/**
 *
 * @author Igor Rodriguez <igorrodriguezelvira@gmail.com>
 */
public class Time implements Comparable{
	private int idTime = -1;
	private int idActivity = -1;
	private Timestamp startTime;
	private Timestamp endTime;
	private long duration = -1;
	private String description;
	private static final int descriptionFieldLength = 200;

	public Time(int idActivity, Timestamp startTime, Timestamp endTime, long duration, String description) {
		this.idActivity = idActivity;
		this.startTime = startTime;
		this.endTime = endTime;
		this.duration = duration;
		setDescription(description);
	}

	public Time(int idTime, int idActivity, Timestamp startTime, Timestamp endTime, long duration, String description) {
		this.idTime = idTime;
		this.idActivity = idActivity;
		this.startTime = startTime;
		this.endTime = endTime;
		this.duration = duration;
		setDescription(description);
	}

	public String getDescription() {
		return description;
	}

	public final void setDescription(String description) {
		description = description.trim();
		if (description.length() > getDescriptionFieldLength()) 
			description = description.substring(0, getDescriptionFieldLength());
		
		this.description = description;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public Timestamp getEndTime() {
		return endTime;
	}

	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}

	public int getIdActivity() {
		return idActivity;
	}

	public void setIdActivity(int idActivity) {
		this.idActivity = idActivity;
	}

	public int getIdTime() {
		return idTime;
	}

	private void setIdTime(int idTime) {
		this.idTime = idTime;
	}

	public Timestamp getStartTime() {
		return startTime;
	}

	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}

	public static int getDescriptionFieldLength() {
		return descriptionFieldLength;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Time other = (Time) obj;
		if (this.idActivity != other.idActivity) {
			return false;
		}
		if (!Objects.equals(this.startTime, other.startTime)) {
			return false;
		}
		if (!Objects.equals(this.endTime, other.endTime)) {
			return false;
		}
		if (!Objects.equals(this.duration, other.duration)) {
			return false;
		}
		if (!Objects.equals(this.description, other.description)) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 67 * hash + this.idActivity;
		hash = 67 * hash + Objects.hashCode(this.startTime);
		hash = 67 * hash + Objects.hashCode(this.endTime);
		hash = 67 * hash + Objects.hashCode(this.duration);
		hash = 67 * hash + Objects.hashCode(this.description);
		return hash;
	}

	@Override
	public String toString() {
		return "Time{" + "startTime=" + startTime + ", endTime=" + endTime + ", duration=" + duration + ", description=" + description + '}';
	}

	@Override
	public int compareTo(Object o) {
		if (o instanceof Time) {
			return compareTo((Time) o);
		} else {
			throw new ClassCastException();
		}
	}
	
	public int compareTo(Time t) {
		int act = (this.getIdActivity() == t.getIdActivity()) ? 0 : (this.getIdActivity() < t.getIdActivity()) ? -1 : 1;
		int start = this.getStartTime().compareTo(t.getStartTime());
		int end = this.getEndTime().compareTo(t.getEndTime());
		int dur = (this.getDuration() == t.getDuration()) ? 0 : (this.getDuration() < t.getDuration()) ? -1 : 1;
		int desc = this.getDescription().compareTo(t.getDescription());
		
		if (act != 0) return act;
		if (start != 0) return start;
		if (end != 0) return end;
		if (dur != 0) return dur;
		else return desc;
	}

	/**
	 * Check if the object properties needed for inserting it in the database are been initialized.
	 * @return True if idActivity, startTime, endTime and duration are been initialized.
	 */
	public boolean isFullFilled() {
		boolean isFilled = true;
		
		boolean aId = idActivity >= 0;
		boolean sTime = startTime != null;
		boolean eTime = endTime != null;
		boolean dur = duration >= 0;
		
		if (!aId) isFilled = false;
		if (!sTime) isFilled = false;
		if (!eTime) isFilled = false;
		if (!dur) isFilled = false;
		
		return isFilled;
	}
}

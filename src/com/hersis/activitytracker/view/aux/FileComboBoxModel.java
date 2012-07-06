package com.hersis.activitytracker.view.aux;

import java.io.File;
import java.util.ArrayList;
import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

/**
 *
 * @author Igor Rodriguez <igorrodriguezelvira@gmail.com>
 */
public class FileComboBoxModel extends AbstractListModel implements ComboBoxModel{

	ArrayList<File> fileList = new ArrayList<>();
	File selectedFile = null;

	@Override
	public void setSelectedItem(Object anItem) {
		selectedFile = (File) anItem;
	}

	@Override
	public Object getSelectedItem() {
		return selectedFile;
	}

	@Override
	public int getSize() {
		return fileList.size();
	}

	@Override
	public Object getElementAt(int index) {
		return fileList.get(index);
	}
	
}

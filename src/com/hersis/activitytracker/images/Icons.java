package com.hersis.activitytracker.images;

import ch.qos.logback.classic.Logger;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.net.URL;
import java.util.Map;
import java.util.TreeMap;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Igor Rodriguez <igorrodriguezelvira@gmail.com>
 */
public class Icons {
	private static final Logger log = (Logger) LoggerFactory.getLogger("images.Icons"); 
	Map<IconValues, Icon> icons = new TreeMap<>();
		
	public Icons() {
		for (IconValues iv : IconValues.values()) {
			icons.put(iv, createIcon(iv));
		}
	}
	
	private Icon createIcon(IconValues icon) {
		String path = "";
		switch (icon) {
			case OK:
				path = "ok.png";
				break;
			case CANCEL: case ERROR:
				path = "cancel.png";
				break;
			case INFO:
				path = "info.png";
				break;
			case WARNING:
				path = "warning.png";
				break;
			case QUESTION:
				path = "helpround.png";
		}
		URL imgUrl = getClass().getResource(path);
		if (imgUrl != null) {
			return new ImageIcon(imgUrl);
		} else {
			log.warn("Couldn't find the icon image file: {}", path);
			return null;
		}
	}
	
	public Icon getIcon(IconValues icon) {
		return icons.get(icon);
	}
	
	public enum IconValues {
		OK,
		CANCEL,
		INFO,
		WARNING,
		ERROR,
		QUESTION
	}
	public static void main(String [] args) {
		Icons icons = new Icons();
		JFrame frame = new JFrame();
		JLabel label = new JLabel();
		JLabel label2 = new JLabel();
		JLabel label3 = new JLabel();
		JLabel label4 = new JLabel();
		JLabel label5 = new JLabel();
		JLabel label6 = new JLabel();
		label.setIcon(icons.getIcon(IconValues.OK));
		label2.setIcon(icons.getIcon(IconValues.CANCEL));
		label3.setIcon(icons.getIcon(IconValues.INFO));
		label4.setIcon(icons.getIcon(IconValues.WARNING));
		label5.setIcon(icons.getIcon(IconValues.ERROR));
		label6.setIcon(icons.getIcon(IconValues.QUESTION));
		frame.setLayout(new FlowLayout());
		frame.add(label);
		frame.add(label2);
		frame.add(label3);
		frame.add(label4);
		frame.add(label5);
		frame.add(label6);
		frame.setPreferredSize(new Dimension(800, 600));
		frame.pack();
		frame.setVisible(true);
	}
}

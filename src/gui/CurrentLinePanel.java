package gui;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

public class CurrentLinePanel extends JPanel {

	private JLabel currentLine;
	Dimension dimension = new Dimension(200, 50);

	public CurrentLinePanel(String line) {
		currentLine = new JLabel(line);
		this.add(currentLine);
		this.setMaximumSize(dimension);
		this.setPreferredSize(dimension);

		TitledBorder titleb = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black) ,"Current Line:");
		titleb.setTitleJustification(TitledBorder.CENTER);
		this.setBorder(titleb);
	}

	public void refresh(String line) {
		currentLine.setText(line);
	}
}
package gui;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class ErrorsPanel extends JPanel {
	private JLabel errors;

	public ErrorsPanel() {
		//Errors
		JLabel errorsTitle = new JLabel("Errors: ");	
		Dimension dimension = new Dimension(1020, 60);

		errors = new JLabel("-");
		JScrollPane errorScrollPane = new JScrollPane(errors); 
		errorScrollPane.setBorder(BorderFactory.createEmptyBorder());
//		this.setBorder(BorderFactory.createLineBorder(Color.black));
		
		this.add(errorsTitle);
		this.add(errorScrollPane);
		
		this.setMaximumSize(dimension);
		this.setPreferredSize(dimension);
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
	}

	public JLabel getErrorsLabel() {
		return this.errors;
	}
}
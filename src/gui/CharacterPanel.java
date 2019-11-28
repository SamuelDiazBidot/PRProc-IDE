package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import io.CharDisplay;


public class CharacterPanel extends JFrame {
	private CharDisplay chardisp;
	private JPanel panel;
		
	public CharacterPanel(String input[], int startPoint) {
		panel = new JPanel();
		panel.setLayout(null);
		chardisp = new CharDisplay(input, startPoint);
		chardisp.setBounds(0,0, 321, 41);
		
		this.setTitle("Character Output Display");
		this.panel.add(chardisp);
		this.add(panel);
		this.setSize(337, 80);
		this.setResizable(false);
		this.setVisible(true);
	}
	
	public void update(String input[]) {
		chardisp.refresh(input);
	}
	
}
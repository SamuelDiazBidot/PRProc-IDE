package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import io.HexKeyboard;
import simulator.Simulator;


public class HexKeyBoardPanel extends JFrame {
	private HexKeyboard keyboard;
	private JPanel panel;
		
	public HexKeyBoardPanel(Table table, Simulator simul, int registerIndex) {
		panel = new JPanel();
		panel.setLayout(null);
		keyboard = new HexKeyboard(table, panel, simul, registerIndex);
		
		this.setTitle("HexKeyboard");
		this.add(panel);
		this.setSize(250, 200);
		this.setResizable(false);
		this.setVisible(true);
	}
	
	public String[] getBufferedValues() {
		String[] buffer = keyboard.getBuffer();
//		int[] values = new int[4];
//		int val;
//		for(int i=0; i<4; i++) {
//			val = Integer.parseInt(buffer[i]);
//			if(val%2==1) {
//				values[i]= (val & 0xF0) >> 4;
//			}
//			else break;
//		}
		return buffer;
	}
	
	public void update(String input[]) {
		keyboard.refresh(input);
	}
}
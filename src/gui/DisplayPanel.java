package gui;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import io.Display;
import simulator.Simulator;

public class DisplayPanel extends JFrame{
	private String content;
	private Display display;
	private int index;

	public DisplayPanel(Simulator sim, int index) {
		this.index = index;
		String content = String.format("%8s",Integer.toBinaryString(Integer.parseInt(sim.getMemory()[index], 16))).replace(' ', '0');
		display = new Display(content);

		this.add(display);
		this.setTitle("7 Segment");
		this.setSize(160, 170);
		this.setResizable(false);
		this.setVisible(true);
		this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
	}

//	public void refresh(String input) {
	public void refresh(Simulator sim) {
		String content = String.format("%8s",Integer.toBinaryString(Integer.parseInt(sim.getMemory()[index], 16))).replace(' ', '0');
		this.display.refresh(content);
	}
}
package gui;

import java.awt.FlowLayout;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import io.TrafficL;
import simulator.Simulator;

public class TrafficLightPanel extends JFrame {
	private TrafficL trafficL, trafficL2;
	private JPanel panel;
	
	public TrafficLightPanel(Simulator sim, int index) {
		panel = new JPanel();
		panel.setLayout(new FlowLayout());
		String input = String.format("%8s",Integer.toBinaryString(Integer.parseInt(sim.getMemory()[index], 16))).replace(' ', '0');
		String input2 = input;
		trafficL = new TrafficL(input.substring(0,3).concat(input.substring(6,7)));
		String input3 = input2.substring(2,6).concat(input2).substring(7);
		trafficL2 = new TrafficL(input3);
		
		this.setTitle("Traffic Light");
		this.add(panel);
		this.add(trafficL);
		this.add(trafficL2);
		trafficL.setBounds(0, 0, 75, 150);
        trafficL2.setBounds(75, 0, 75, 150);
		this.setBounds(0, 0, 200, 200);
		this.setLayout(new FlowLayout());
		this.setResizable(false);
		this.setVisible(true);
	}
	
	public void refresh(Simulator sim, int index) {
		String input = String.format("%8s",Integer.toBinaryString(Integer.parseInt(sim.getMemory()[index], 16))).replace(' ', '0');
		String input2 = input;
		trafficL.refresh(input.substring(0,3).concat(input.substring(6,7)));
		String input3 = input2.substring(2,6).concat(input2).substring(7);
		trafficL2.refresh(input3);
	}

}
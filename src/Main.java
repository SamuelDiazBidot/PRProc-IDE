import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;

import gui.ColorsKeywords;
import gui.CurrentLinePanel;
import gui.ErrorsPanel;
import gui.MenuBar;
import gui.Table;
import simulator.Simulator;

public class Main {
	public static void main(String[] args) {
		JFrame jf = new JFrame("Phase 4");
		JPanel right = new JPanel();
		JPanel left = new JPanel();
		JPanel top = new JPanel();
		JPanel bot = new JPanel();

		//Text Pane
		ColorsKeywords words = new ColorsKeywords();
		JTextPane textPane = new JTextPane(words.getDocument());
		
		JScrollPane textScrollPane = new JScrollPane(textPane); 
		TextLineNumber tln = new TextLineNumber(textPane);
		textScrollPane.setRowHeaderView(tln);
		left.add(textScrollPane);

		//Simulator
		Simulator sim = new Simulator();
		
		//Current Line
		CurrentLinePanel currLine = new CurrentLinePanel(sim.getCurrInstruction());
		right.add(currLine);
		
		//Register and Memory Tables
		Table tab = new Table(sim.getMemory(), sim.getRegister());
		right.add(tab);

		//Errors
		ErrorsPanel p = new ErrorsPanel();
//		right.add(p);
		bot.add(p);

		right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
//		right.setBorder(BorderFactory.createLineBorder(Color.black));
		//right.setMaximumSize(new Dimension(500, 650));
		
		left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
		top.setLayout(new BoxLayout(top, BoxLayout.X_AXIS));
		top.add(left);
		top.add(right);
		
		jf.add(top);
		jf.add(bot);

		//Menu Bar
		//MenuBar menu = new MenuBar(jf, textArea, errors);
		MenuBar menu = new MenuBar(jf, textPane, p.getErrorsLabel(), sim, tab, currLine);
		jf.setJMenuBar(menu);

		jf.setSize(1024,650);
		jf.setResizable(false);
		jf.setVisible(true);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
		jf.setLayout(new BoxLayout(jf.getContentPane(), BoxLayout.Y_AXIS));
		//jf.setLayout(new FlowLayout());
	}
}
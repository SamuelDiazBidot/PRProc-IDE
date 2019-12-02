package io;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

public class Display extends Canvas {
	private char[] segments = new char[8];

	public Display(String input) {
		this.segments = input.toCharArray();
		this.setBackground(Color.BLACK);
	}

	public void refresh(String input) {
		this.segments = input.toCharArray();
		this.repaint();
	}

	@Override
	public void paint(Graphics g) {
		draw(g, segments, 10, '0');
		draw(g, segments, 70, '1');
	}

	private void draw (Graphics g, char[] segments, int x, char pos) {
		boolean isPos = segments[7] == pos;
		//- A	
		if(segments[0] == '1' && isPos) {
			g.setColor(Color.GREEN);
			g.fillRect(x + 10, 10, 30, 10);
		}else {
			g.setColor(Color.DARK_GRAY);
			g.fillRect(x + 10, 10, 30, 10);
		}
		//|
		if(segments[5] == '1' && isPos) {
			g.setColor(Color.GREEN);
			g.fillRect(x + 0, 20, 10, 40);
		}else {
			g.setColor(Color.DARK_GRAY);
			g.fillRect(x + 0, 20, 10, 40);
		}
		// |
		if(segments[1] == '1' && isPos) {
			g.setColor(Color.GREEN);
			g.fillRect(x + 40, 20, 10, 40);
		}else {
			g.setColor(Color.DARK_GRAY);
			g.fillRect(x + 40, 20, 10, 40);
		}
		// =
		if(segments[6] == '1' && isPos) {
			g.setColor(Color.GREEN);
			g.fillRect(x + 10, 60, 30, 10);
		}else {
			g.setColor(Color.DARK_GRAY);
			g.fillRect(x + 10, 60, 30, 10);
		}
		//|
		if(segments[4] == '1' && isPos) {
			g.setColor(Color.GREEN);
			g.fillRect(x + 0, 70, 10, 40);
		}else {
			g.setColor(Color.DARK_GRAY);
			g.fillRect(x + 0, 70, 10, 40);
		}
		// |
		if(segments[2] == '1' && isPos) {
			g.setColor(Color.GREEN);
			g.fillRect(x + 40, 70, 10, 40);
		}else {
			g.setColor(Color.DARK_GRAY);
			g.fillRect(x + 40, 70, 10, 40);
		}
		//_
		if(segments[3] == '1' && isPos) {
			g.setColor(Color.GREEN);
			g.fillRect(x + 10, 110, 30, 10);
		}else {
			g.setColor(Color.DARK_GRAY);
			g.fillRect(x + 10, 110, 30, 10);
		}
	}
}
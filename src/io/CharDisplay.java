package io;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

public class CharDisplay extends Canvas {
	char[] Chars = new char[8];
	int width=40;
	int startHere = 0;
	
	public CharDisplay(String input[], int startPoint) {
		this.startHere = startPoint;
		for(int i=startPoint;i<startPoint+8;i++) {
			this.Chars[i-startPoint] = (char) Integer.parseInt(input[i], 16);
		}
		this.setBackground(Color.WHITE);
	}

	public void refresh(String input[]) {
		for(int i=startHere;i<startHere+8;i++) {
			this.Chars[i-startHere] = (char) Integer.parseInt(input[i], 16);
		}
		this.repaint();
	}
	@Override
	public void paint(Graphics g)
	{
		for(int i=0;i<8;i++) {
			g.drawRect(width*i, 0, width, 40);
		}
		g.setFont(new Font("Consola", Font.PLAIN, 40));
		for(int i=0;i<8;i++) {
			g.drawString(""+this.Chars[i], 40*i+9, 35);
		}
	}

}
package io;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;

import gui.Table;

public class TrafficL extends Canvas {
	String input = new String();
	Signal green = new Signal(Color.GREEN);
	Signal yellow = new Signal(Color.YELLOW);
	Signal red = new Signal(Color.RED);
	Table tab = null;
	boolean redOn = false;
	boolean yellowOn = false;
	boolean greenOn = false;
	Timer timer;
	boolean blinking = false;

	public TrafficL(String input) {
		this.input = input;
	}

	class Signal extends JPanel{
		private static final long serialVersionUID = 1L;
		Color on;
		int radius = 40;
		int border = 10;
		boolean change;

		Signal(Color color){
			on = color;
			change = true;
		}

		public void turnOn(boolean a){
			change = a;
			repaint();      
		}
	}

	public void refresh(String input) {
		this.input = input;
		this.repaint();
	}

	@Override
	public void paint(Graphics g) {
		draw(g, this.input);
	}

	//
	public void flashingLights(Graphics g,Boolean Red, Boolean Yellow, Boolean Green) {
		timer = new Timer();
		blinking = true;
		timer.schedule(new TimerTask() {
			int i = 0;
			@Override
			public void run() {
				StringBuilder newInput = new StringBuilder();
				if(Red) {
					newInput.append('0');
				}else {
					newInput.append('1');
				}
				if(Yellow) {
					newInput.append('0');
				}else {
					newInput.append('1');
				}
				if(Green){
					newInput.append('0');
				}else {
					newInput.append('1');
				}
				newInput.append('1');
				if(newInput.length()>4) {
					newInput.delete(0, 4);
				}
				i++;
				refresh(newInput.toString());
				//draw(g,newInput.toString());

			}
		}, 2000/*, 2000*/);
	}

	private void draw(Graphics g, String input){
		int radius = 20;
		int border = 10;

		//Draw Rectangle
		g.setColor( Color.black );
		g.fillRect(0,0,getWidth(),getHeight());

		//Red Light
		if(input.charAt(0) == '1') {
			g.setColor(this.red.on);
			redOn = true;
			g.fillOval( 20,border,2*radius,2*radius );
		}else {
			g.setColor(this.red.on.darker().darker().darker());
			redOn=false;
			g.fillOval( 20,border,2*radius,2*radius );
		}

		//Yellow Light
		if(input.charAt(1) == '1') {
			g.setColor(this.yellow.on);
			yellowOn = true;
			g.fillOval( 20,border*5+5,2*radius,2*radius );
		}else {
			g.setColor(this.yellow.on.darker().darker().darker());
			yellowOn=false;
			g.fillOval( 20,border*5+5,2*radius,2*radius );
		}

		//Green Light
		if(input.charAt(2) == '1') {
			g.setColor(this.green.on);
			greenOn = true;
			g.fillOval( 20,border*10,2*radius,2*radius );
		}else {
			g.setColor(this.green.on.darker().darker().darker());
			greenOn=false;
			g.fillOval( 20,border*10,2*radius,2*radius );
		}
		if(blinking) {
			timer.cancel();
		}
		if(input.charAt(input.length()-1)=='1') {
			blink(g,input.charAt(3));
		}
	}

	private void blink(Graphics g,char blink) {
		flashingLights(g,redOn, yellowOn, greenOn);
	}

}
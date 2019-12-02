package io;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;

import gui.Table;
import sun.security.util.Length;

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
	boolean rWasTrue = false;
	boolean yWasTrue = false;
	boolean gWasTrue = false;

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
			@Override
			public void run() {
				StringBuilder newInput = new StringBuilder();
				if(!Red) {
					if(rWasTrue) {
						newInput.append('1');
					}else {
						newInput.append('0');
					}
				}else {
					rWasTrue = true;
					newInput.append('0');
				}

				if(!Yellow) {
					if(yWasTrue) {
						newInput.append('1');
					}else {
						newInput.append('0');
					}
				}else {
					yWasTrue = true;
					newInput.append('0');
				}

				if(!Green) {
					if(gWasTrue) {
						newInput.append('1');
					}else {
						newInput.append('0');
					}
				}else {
					gWasTrue = true;
					newInput.append('0');
				}

				newInput.append('1');

				if(newInput.length()>4) {
					newInput.delete(0, 4);
				}
				refresh(newInput.toString());

			}
		}, 2000);
	}

	private void draw(Graphics g, String input){
		System.out.println(input);
		int radius = 20;
		int border = 10;

		//Draw Rectangle
		g.setColor( Color.black );
		g.fillRect(0,0,getWidth(),getHeight());

		//Red Light
		if(input.charAt(0) == '1') {
			g.setColor(this.red.on);
			redOn = true;
			System.out.println("redOn true");
			g.fillOval( 20,border,2*radius,2*radius );
		}else {
			g.setColor(this.red.on.darker().darker().darker());
			redOn=false;
			System.out.println("redOn false");
			g.fillOval( 20,border,2*radius,2*radius );
		}

		//Yellow Light
		if(input.charAt(1) == '1') {
			g.setColor(this.yellow.on);
			yellowOn = true;
			System.out.println("yellowOn true");
			g.fillOval( 20,border*5+5,2*radius,2*radius );
		}else {
			g.setColor(this.yellow.on.darker().darker().darker());
			yellowOn=false;
			System.out.println("yellowOn false");
			g.fillOval( 20,border*5+5,2*radius,2*radius );
		}

		//Green Light
		if(input.charAt(2) == '1') {
			g.setColor(this.green.on);
			greenOn = true;
			System.out.println("greenOn true");
			g.fillOval( 20,border*10,2*radius,2*radius );
		}else {
			g.setColor(this.green.on.darker().darker().darker());
			greenOn=false;
			System.out.println("greenOn false");
			g.fillOval( 20,border*10,2*radius,2*radius );
		}
		if(blinking) {
			timer.cancel();
		}
		if(input.charAt(input.length()-1)=='1') {
			blink(g,input.charAt(input.length()-1));
		}
	}

	private void blink(Graphics g,char blink) {
		flashingLights(g,redOn, yellowOn, greenOn);
	}

}
package io;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import gui.Table;
import simulator.Simulator;

public class HexKeyboard {
	  static int registerIndex;
	  String[] memory;
	  public HexKeyboard(Table panel, JPanel display, Simulator simul, int registerI) {
		  registerIndex = registerI;
		  int buffer = registerIndex + 4;
		  memory = simul.getMemory();
		  String[] register = simul.getRegister();
		  
		  String hexStrR1 = "0123";
	      String hexStrR2 = "4567";
	      String hexStrR3 = "89AB";
	      String hexStrR4 = "CDEF";
	      String[] hexStrs = {hexStrR1, hexStrR2, hexStrR3, hexStrR4};
	      
		  int bx = 10;
	      int by = 0;
	        for(int i = 0; i < hexStrs.length; i++) {
	        	char[]  hexChars = hexStrs[i].toCharArray();
	        	by += 26;
	        	bx = 10;
	        	for(int y = 0; y < hexChars.length;  y++) {
	        		int z = y;
	        	    
	        		JButton hexButton = new JButton(Character.toString(hexChars[y]));
	        		hexButton.setBounds(bx,by, 50, 26);
	        	    display.add(hexButton); 
	        	    bx += 52;
	        	    
	        	    
	        	    hexButton.addActionListener(
	        	    		new ActionListener() {
	        	    			public void actionPerformed(ActionEvent e) {
	        	    				StringBuilder wholeByte = new StringBuilder();
	        	    				String fourBit = Integer.toHexString(Integer.parseInt(Character.toString(hexChars[z]), 16)).toUpperCase();
	        	    				StringBuilder tempBuilder;
	        	    				//String fourBit = String.format("%4s", Integer.toBinaryString(Integer.parseInt(Character.toString(hexChars[z]), 16))).replace(' ', '0');
	        	    				//int registerIndex = 0;
	        	    				wholeByte.append(fourBit);
	        	    				wholeByte.append("1");
	        	    				for(int i=registerIndex; i<buffer && i != 4096; i++) {
//	        	    				while(registerIndex < buffer && registerIndex != 4096) {
	        	    					if(Integer.parseInt(memory[i],16)%2==1) {
	        	    						i++;
	        	    					}
//	        	    					if(i-1 >= registerIndex && Integer.parseInt(memory[i-1],16)%2==1 && registerIndex != 0) {	        	    					
//		        	    					tempBuilder = new StringBuilder();
//		        	    					tempBuilder.append(memory[i-1].substring(0, 1));
//		        	    					tempBuilder.append("0");
//		        	    					memory[i-1] = tempBuilder.toString();
//		        	    				}
	        	    					if(Integer.parseInt(memory[i],16)%2 == 0) {
		        	    						memory[i] = wholeByte.toString();
		        	    						break;
		        	    				}
	        	    				}
	        	    				panel.update(memory, register);
	        	    			}
	        	    		});
	        	    
	        	}
	        	
	        	      }

	  }
	  
	  public String[] getBuffer() {
		  String[] buffer = {memory[registerIndex], memory[registerIndex+1], memory[registerIndex+2], memory[registerIndex+3]};
		  return buffer;
	  }
	  
	  public void refresh(String input[]) {	
		  this.memory = input;
	  }
}
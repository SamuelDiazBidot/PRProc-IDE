package gui;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import assembler.Assembler;
import io.Display;
import simulator.Simulator;

public class MenuBar extends JMenuBar{
	private String currFile;
	private boolean compiled = false;
	private int hexindex, lightindex;

	public MenuBar(JFrame jf, JTextPane textArea, JLabel errors, Simulator simulator, Table tables, CurrentLinePanel currLine) {
		ArrayList<DisplayPanel> segmentDisplays = new ArrayList<>();
		ArrayList<CharacterPanel> characterDisplays = new ArrayList<>();
		ArrayList<TrafficLightPanel> trafficDisplays = new ArrayList<>();
		ArrayList<HexKeyBoardPanel> keyboardDisplays = new ArrayList<>();

		//Menus
		//file
		JMenu file = new JMenu("File");
		file.getAccessibleContext().setAccessibleDescription("The only menu in this program that has menu items");
		this.add(file);

		JMenu io = new JMenu("IO");
		this.add(io);

		//Build Button
		//<div>Icons made by <a href="https://www.flaticon.com/authors/srip" title="srip">srip</a> from <a href="https://www.flaticon.com/" title="Flaticon">www.flaticon.com</a></div>
		ImageIcon buildIcon = new ImageIcon("assets/brick-wall.png");
		JButton build = new JButton(buildIcon);
		build.setMaximumSize(new Dimension(20, 16));
		build.setToolTipText("Build");
		build.setOpaque(false);
		build.setContentAreaFilled(false);
		build.setBorderPainted(false);

		build.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						String asmInput = getCurrentFile();
						if(asmInput == null) {
							JOptionPane.showMessageDialog(null, "No file to compile.\nPlease save or load a file.");
						}else {
							try {
								//Make obj file
								String objFile = Assembler.assemble(asmInput);

								//Show errors from errors file
								StringBuilder errorStr = new StringBuilder();
								File file = new File("src/errors.txt"); 
								Scanner sc = new Scanner(file); 
								while (sc.hasNextLine()) {
									errorStr.append(convertToMultiline("\n" + "*" + sc.nextLine()));
								}
								errors.setText(errorStr.toString());

								simulator.reset();
								currLine.refresh(simulator.getCurrInstruction());
								//Update memory with contents from obj file
								//String outputFile = getCurrentFile().replaceAll(".asm", ".obj");
								BufferedReader br = new BufferedReader(new FileReader(objFile));
								String line;
								int p1 = 0;
								int p2 = 1;
								while((line = br.readLine()) != null) {
									simulator.getMemory()[p1] = line.substring(0,2);
									simulator.getMemory()[p2] = line.substring(2,4);
									p1 += 2;
									p2 += 2;
								}
								tables.update(simulator.getMemory(), simulator.getRegister());
								characterDisplays.forEach(display -> display.update(simulator.getMemory()));
								segmentDisplays.forEach(display -> display.refresh(simulator));
								trafficDisplays.forEach(display -> display.refresh(simulator, lightindex));
								compiled = true;
								br.close();
							} catch (IOException e1) {}
						}
					}
				}
				);

		this.add(build);

		//Run Button
		//<div>Icons made by <a href="https://www.flaticon.com/authors/google" title="Google">Google</a> from <a href="https://www.flaticon.com/" title="Flaticon">www.flaticon.com</a></div>
		ImageIcon runIcon = new ImageIcon("assets/play-arrow.png");
		JButton runAll = new JButton(runIcon);
		runAll.setMaximumSize(new Dimension(20, 16));
		runAll.setToolTipText("Run All");
		runAll.setOpaque(false);
		runAll.setContentAreaFilled(false);
		runAll.setBorderPainted(false);

		runAll.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						String asmInput = getCurrentFile();
						if(asmInput == null) {
							JOptionPane.showMessageDialog(null, "No file to run.\nPlease save or load a file.");
						} else if(!compiled) {
							JOptionPane.showMessageDialog(null, "No program to run.\nPlease compile a program.");
						} else {
							for(int i = 0; i < 4095; i++) {
								simulator.execute_instruction();
								currLine.refresh(simulator.getCurrInstruction());
								characterDisplays.forEach(display -> display.update(simulator.getMemory()));
								segmentDisplays.forEach(display -> display.refresh(simulator));
								trafficDisplays.forEach(display -> display.refresh(simulator,lightindex));
							}
							tables.update(simulator.getMemory(), simulator.getRegister());
						}
					}
				}
				);

		this.add(runAll);

		//Run Step
		//<div>Icons made by <a href="https://www.flaticon.com/authors/dave-gandy" title="Dave Gandy">Dave Gandy</a> from <a href="https://www.flaticon.com/" title="Flaticon">www.flaticon.com</a></div>
		ImageIcon runStepIcon = new ImageIcon("assets/step-forward.png");
		JButton runStep = new JButton(runStepIcon);
		runStep.setMaximumSize(new Dimension(20, 16));
		runStep.setToolTipText("Run Step");
		runStep.setOpaque(false);
		runStep.setContentAreaFilled(false);
		runStep.setBorderPainted(false);

		runStep.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						String asmInput = getCurrentFile();
						if(asmInput == null) {
							JOptionPane.showMessageDialog(null, "No file to run.\nPlease save or load a file.");
						} else if(!compiled) {
							JOptionPane.showMessageDialog(null, "No program to run.\nPlease compile a program.");
						} else {
//							if(!keyboardDisplays.isEmpty()) {
//								simulator.keyboardEdit(hexindex, keyboardDisplays.get(0).getBufferedValues());
//							}
							keyboardDisplays.forEach(display -> {simulator.keyboardEdit(hexindex, display.getBufferedValues());});
							simulator.execute_instruction();
							currLine.refresh(simulator.getCurrInstruction());
							tables.update(simulator.getMemory(), simulator.getRegister());
							tables.changeColor(simulator.getRegChanged(), simulator);
							characterDisplays.forEach(display -> display.update(simulator.getMemory()));
							segmentDisplays.forEach(display -> display.refresh(simulator));
							trafficDisplays.forEach(display -> display.refresh(simulator,lightindex));
							//	simulator.resetRegChange();
						}
					}
				}
				);

		this.add(runStep);

		//Menu Items
		//Save As...
		JMenuItem saveAs = new JMenuItem("Save As...");
		saveAs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.ALT_MASK));
		saveAs.getAccessibleContext().setAccessibleDescription("Save file");

		saveAs.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						try {
							JFileChooser fileChooser = new JFileChooser();
							int option = fileChooser.showSaveDialog(jf);
							if(option == JFileChooser.APPROVE_OPTION){
								File file = fileChooser.getSelectedFile();
								//Set the current file being worked on
								currFile = file.toString();
								//Set correct file extension
								if (!currFile.endsWith(".asm")) {
									currFile += ".asm";
								}
								PrintStream fileOut = new PrintStream(currFile);								
								System.setOut(fileOut);
								System.out.println(textArea.getText());
								fileOut.close();
							}
						}catch(IOException r) {
						}
					}
				}
				);

		//save
		JMenuItem save = new JMenuItem("Save");
		save.getAccessibleContext().setAccessibleDescription("Save file");

		save.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if(getCurrentFile() != null) {
							try {
								PrintStream fileOut = new PrintStream(currFile);
								System.setOut(fileOut);
								System.out.println(textArea.getText());
								fileOut.close();
							} catch (FileNotFoundException e1) {
								e1.printStackTrace();
							}
						} else {
							try {
								JFileChooser fileChooser = new JFileChooser();
								int option = fileChooser.showSaveDialog(jf);
								if(option == JFileChooser.APPROVE_OPTION){
									File file = fileChooser.getSelectedFile();
									//Set the current file being worked on
									currFile = file.toString();
									//Set correct file extension
									if (!currFile.endsWith(".asm")) {
										currFile += ".asm";
									}
									PrintStream fileOut = new PrintStream(currFile);								
									System.setOut(fileOut);
									System.out.println(textArea.getText());
									fileOut.close();
								}
							}catch(IOException r) {
							}
						}
					}
				}
				);

		//Load
		JMenuItem load = new JMenuItem("Load");
		load.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.ALT_MASK));
		load.getAccessibleContext().setAccessibleDescription("Load file");

		final JFileChooser fc = new JFileChooser();
		FileFilter filter = new FileNameExtensionFilter("Assembly File", "asm");
		fc.setMultiSelectionEnabled(true);
		fc.addChoosableFileFilter(filter);
		fc.setFileFilter(filter);
		//  Cambiar direcion a "C:\\tmp"
		//fc.setCurrentDirectory(new File("C:\\Users\\user\\Documents"));

		load.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						int retVal = fc.showOpenDialog(jf);
						if (retVal == JFileChooser.APPROVE_OPTION) {
							try {
								File selectedFile = fc.getSelectedFile();
								FileReader reader = new FileReader(selectedFile);
								BufferedReader br = new BufferedReader(reader);
								String inputFile = "";
								String textFieldReadable = br.readLine();

								while (textFieldReadable != null){
									inputFile += textFieldReadable+ "\n";  
									textFieldReadable = br.readLine();   
								}
								textArea.setText(inputFile);
								//textArea.read(br, null);
								currFile = selectedFile.toString();
							}catch(IOException r) {
							}
						}
					}
				}
				);

		//7 Segment Display
		JMenuItem segmentDisplay = new JMenuItem("7 Segment Display");
		segmentDisplay.getAccessibleContext().setAccessibleDescription("Save file");

		segmentDisplay.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if(getCurrentFile() == null) {
							JOptionPane.showMessageDialog(null, "No file to run.\nPlease save or load a file.");
						} else if(!compiled) {
							JOptionPane.showMessageDialog(null, "No program to run.\nPlease compile a program.");
						}
						else {
							int index = getIOIndex();
							if(index != -1) {
								segmentDisplays.add(new DisplayPanel(simulator, index));
							}
						}
					}
				}
				);
		//**********************************************************************************
		//Hexkeyboard
		JMenuItem hexkey = new JMenuItem("Keyboard");
		hexkey.getAccessibleContext().setAccessibleDescription("Save file");
		hexkey.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if(getCurrentFile() == null) {
							JOptionPane.showMessageDialog(null, "No file to run.\nPlease save or load a file.");
						} else if(!compiled) {
							JOptionPane.showMessageDialog(null, "No program to run.\nPlease compile a program.");
						}else {
							hexindex = getIOIndex();
							if(hexindex != -1) {
//								HexKeyBoardPanel keyboard = new HexKeyBoardPanel(tables, simulator, hexindex);
								keyboardDisplays.add(new HexKeyBoardPanel(tables, simulator, hexindex));
							}
						}
					}
				}
				);
		//*************Chararacter Display****************************
		JMenuItem charDisp = new JMenuItem("Character Display");
		charDisp.getAccessibleContext().setAccessibleDescription("Character Output Display");
		charDisp.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if(getCurrentFile() == null) {
							JOptionPane.showMessageDialog(null, "No file to run.\nPlease save or load a file.");
						} else if(!compiled) {
							JOptionPane.showMessageDialog(null, "No program to run.\nPlease compile a program.");
						}
						else {
							int index = getIOIndex();
							if(index != -1) {
								characterDisplays.add(new CharacterPanel(simulator.getMemory(), index));
							}
						}
					}
				}
				);

		//Traffic Light		
		JMenuItem trafficL = new JMenuItem("Traffic Light");		
		trafficL.getAccessibleContext().setAccessibleDescription("Traffic");	

		trafficL.addActionListener(	
				new ActionListener() {	
					public void actionPerformed(ActionEvent e) {
						if(getCurrentFile() == null) {
							JOptionPane.showMessageDialog(null, "No file to run.\nPlease save or load a file.");
						} else if(!compiled) {
							JOptionPane.showMessageDialog(null, "No program to run.\nPlease compile a program.");
						} else {
							lightindex = getIOIndex();
							if(lightindex != -1) {
								trafficDisplays.add(new TrafficLightPanel(simulator, lightindex));
							}
						}
					}
				}
				);

		file.add(save);
		file.add(saveAs);
		file.add(load);

		io.add(segmentDisplay);
		io.add(hexkey);
		io.add(charDisp);
		io.add(trafficL);
	}

	public String getCurrentFile() {
		return currFile;
	}

	private static String convertToMultiline(String orig) {
		return "<html>" + orig.replaceAll("\n", "<br>");
	}

	public int getIOIndex() {
		String input = JOptionPane.showInputDialog("Read/Write from: ", 0);
		try {
			int index = Integer.parseInt(input);
			if(index < 0 || index > 4095) {
				JOptionPane.showMessageDialog(null, "Please enter a valid address number between 0 and 4095");
				return -1;
			}
			return index;
		}catch(NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "Please enter a valid number");
			return -1;
		}
	}
}
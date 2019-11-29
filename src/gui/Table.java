package gui;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import simulator.Simulator;

public class Table extends JPanel{
	private JTable memory;
	private JTable registry;
//	private Integer[] index = new Integer[2048];
	private String[] index = new String[2048];
	private String[] registerIndex = {"R0", "R1", "R2", "R3", "R4", "R5", "R6", "R7" };
	private String[] memoryEven = new String[2048];
	private String[] memoryOdd = new String[2048];
	private Dimension dimensionMem = new Dimension(200,360);

	JTable jt;
	public Table(String[] memorydata, String[] register){
		int y =0;
		for(int i =0; i<4096; i += 2) {
			index[y] = Integer.toHexString(i).toUpperCase();
			y++;
		}

		DefaultTableModel model = new DefaultTableModel();
		DefaultTableModel model2 = new DefaultTableModel();
		
		splitMemory(memorydata);
		
		model.addColumn("Address", index);
		model.addColumn("Memory", memoryEven);
		model.addColumn("", memoryOdd);

		model2.addColumn("Register", registerIndex);
		model2.addColumn("Content", register);

		memory = new JTable(model);
		registry = new JTable(model2);

//		memory.setPreferredScrollableViewportSize(new Dimension(450,200));
		memory.setPreferredScrollableViewportSize(dimensionMem);
		memory.setFillsViewportHeight(true);

//		registry.setPreferredScrollableViewportSize(new Dimension(467,128));
		registry.setPreferredScrollableViewportSize(new Dimension(200, 137));
		registry.setFillsViewportHeight(true);

		JScrollPane mem = new JScrollPane(memory);
		mem.setVisible(true);
		mem.setMaximumSize(dimensionMem);

		JScrollPane reg = new JScrollPane(registry);
		reg.setVisible(true);
		reg.setMaximumSize(new Dimension(200,150));

		add(reg);
		add(mem);

		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		//this.setMaximumSize(new Dimension(500, 500));
	}

	public void update(String[] memorydata, String[] register) {
		DefaultTableModel newmodel = new DefaultTableModel();
		DefaultTableModel newmodel2 = new DefaultTableModel();
		splitMemory(memorydata);
		
		newmodel.addColumn("Address", index);
		newmodel.addColumn("Memory", memoryEven);
		newmodel.addColumn("", memoryOdd);
		newmodel2.addColumn("Register", registerIndex);
		newmodel2.addColumn("Content", register);
		memory.setModel(newmodel);
		registry.setModel(newmodel2);

	}
	public void splitMemory(String[] memorydata) {
		int iEven = 0;
		int iOdd = 0;
		for(int i = 0; i<4096; i++) {
			if(i % 2 == 0) {
				memoryEven[iEven] = memorydata[i];
				iEven++;
			}else {
				memoryOdd[iOdd] = memorydata[i];
				iOdd++;
			}
		}
	}
	

	public void changeColor(boolean regChanged[], Simulator s) {

		registry.setDefaultRenderer(registry.getColumnClass(1), new Renderer(s));  
	}

	//for character display io element data
	public String[] getValues(int startPoint) {
		String[] output = {"00000000","00000000","00000000","00000000","00000000","00000000","00000000","00000000"};
		for(int i=0;i<8;i++) {
			output[i] = String.format("%8s",Integer.toBinaryString(Integer.parseInt(memory.getModel().getValueAt(startPoint+i, 1).toString(),16))).replace(' ','0');
		}
		return output;
	}
	
	public String getValue(int startPoint) {
		return String.format("%8s",Integer.toBinaryString(Integer.parseInt(memory.getModel().getValueAt(startPoint, 1).toString(),16))).replace(' ','0');
	}
}

class Renderer extends DefaultTableCellRenderer {
	Simulator sim;
	Renderer(Simulator s){
		this.sim = s;
	}
	public Component getTableCellRendererComponent( JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
	{
		boolean[] b = sim.getRegChanged();

		if(column == 1 && b[row] == true) {
			setBackground(Color.RED);
			sim.resetRegChange();
		}else {
			setBackground(Color.WHITE);
		}
		return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	}
}
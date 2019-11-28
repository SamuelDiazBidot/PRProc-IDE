package simulator;

public class Simulator {
	private String instructionRegister;
	private String currInstruction;
	private int pc;
	private int sp;
	private boolean cond;
	private String[] memory;
	private String[] register;
	private boolean regChanged[];

	//	public Simulator(String[] memory, String[] register) {
	public Simulator() {
		this.memory = new String[4096];
		this.register = new String[8];
		this.regChanged = new boolean[8];
		this.reset();
		//		this.memory = memory;
		//		this.register = register;
	}

	//Loads instruction register with memory locations pointed to by PC and executes the instruction
	public void execute_instruction(){
		instructionRegister = ""+memory[pc] + memory[pc+1];
		String binaryInstruction = String.format("%16s", Integer.toBinaryString(Integer.parseInt(instructionRegister, 16))).replace(' ','0');

		//breaking binary into necessary components for instruction execution
		String opcode = binaryInstruction.substring(0, 5);
		int ra = Integer.parseInt(binaryInstruction.substring(5, 8), 2); 
		int rb = Integer.parseInt(binaryInstruction.substring(8, 11), 2);
		int rc = Integer.parseInt(binaryInstruction.substring(11,14), 2);
		int f2address = Integer.parseInt(binaryInstruction.substring(8, 16), 2);
		int f3address = Integer.parseInt(binaryInstruction.substring(5,16), 2);

		//find out which instruction was loaded and executes accordingly
		switch(opcode) {
		//LOAD	R[Ra] <- [address] save address into register a? 
		case "00000":
			currInstruction = instructionRegister + ": LOAD" + " R" + Integer.toString(ra) + ", "+ Integer.toHexString(f2address);
			//Assign value of address in register a.
			if(ra>0)
				register[ra] = memory[f2address];
			regChanged[ra] = true;
			pc+=2;
			break;
			//LOADIM	R[Ra] <- cons save a literal number into register a?
		case "00001": 
			currInstruction = instructionRegister + ": LOADIM" + " R" + Integer.toString(ra) + ", " + Integer.toHexString(f2address);
			//Get literal number and register number.
			// int dec1 = Integer.parseInt(bin.substring(8, 12), 2);
			// int dec2 = Integer.parseInt(bin.substring(12, 16), 2);
			// String cons = Integer.toHexString(dec1) +     Integer.toHexString(dec2);
			//Assign value of address in register a.
			if(ra>0)
				register[ra] = String.format("%2s", Integer.toHexString(f2address)).replace(' ','0');
//				register[ra] = Integer.toHexString(f2address);
			regChanged[ra] = true;
			pc+=2;

			break;
			//POP R[Ra] <- [mem[SP]] SP <- SP + 1
		case "00010":
			currInstruction = instructionRegister + ": POP" + " R" + Integer.toString(ra);
			sp = sp +1;
			if(ra>0)
				register[ra] = memory[sp];
			regChanged[ra] = true;
			pc+=2;

			break;
			//STORE [mem] <- R[Ra] address and memory the same thing?
		case "00011":
			currInstruction = instructionRegister + ": STORE" + " R" + Integer.toString(ra);
			memory[f2address] = register[ra];
			pc+=2;
			break;
			//PUSH	[mem[SP]] <- R[Ra] SP <- SP – 1
		case "00100":
			currInstruction = instructionRegister + ": PUSH" + " R" + Integer.toString(ra);
			memory[sp] = register[ra];
			sp = sp-1;
			pc+=2;
			break;
			//LOADRIND	R[Ra] <- mem[R[Rb]]
		case "00101":
			currInstruction = instructionRegister + ": LOADRIND" + " R" + Integer.toString(ra) + " R" + Integer.toString(rb);
			int b = Integer.parseInt(register[rb], 16);
			int x = Integer.parseInt(memory[b], 16);
			if(ra>0)
				register[ra] = Integer.toHexString(x);
			regChanged[ra] = true;
			pc+=2;

			break;
			//STORERIND mem[R[Ra]] <- R[Rb]
		case "00110":
			currInstruction = instructionRegister + ": STORERIND" + " R" + Integer.toString(ra) + " R" + Integer.toString(rb);
			int a = Integer.parseInt(register[ra], 16);
			x = Integer.parseInt(memory[a], 16);
			memory[a] = register[rb];
			pc+=2;
			break;
			//ADD	R[Ra]<- R[Rb]+R[Rc] TODO: If overflow it return a 3 bit hex number.
		case "00111":
			currInstruction = instructionRegister + ": ADD" + " R" + Integer.toString(ra) + " R" + Integer.toString(rb) + " R" + Integer.toString(rc);
			//Get register number and add rb with rc. 
			int sum = Integer.parseInt(register[rb], 16) + Integer.parseInt(register[rc], 16);
			//Assign value of sum in register a.
			if(ra>0)
				register[ra] = String.format("%2s", Integer.toHexString(sum)).replace(' ','0');
//				register[ra] = Integer.toHexString(sum);
			regChanged[ra] = true;
			pc+=2;
			break;
			//SUB	R[Ra]<- R[Rb]-R[Rc] TODO: If underflow it return a 3 bit hex number.
		case "01000":
			currInstruction = instructionRegister + ": SUB" + " R" + Integer.toString(ra) + " R" + Integer.toString(rb) + " R" + Integer.toString(rc);
			//Get register number and subtract rb with rc. 
			int dif = Integer.parseInt(register[rb], 16) - Integer.parseInt(register[rc], 16);
			//Assign value of sum in register a.
			if(ra>0)
				register[ra] = Integer.toHexString(dif);
			regChanged[ra] = true;
			pc+=2;
			break;
			//ADDIM	R[Ra]<- R[Ra]+cons TODO: If overflow it return a 3 bit hex number.
		case "01001":
			//Get literal number, register number and add the literal to ra.
			int dec1 = Integer.parseInt(binaryInstruction.substring(8, 12), 2);
			int dec2 = Integer.parseInt(binaryInstruction.substring(12, 16), 2);
			String cons = Integer.toHexString(dec1) + Integer.toHexString(dec2);
			currInstruction = instructionRegister + ": ADDIM" + " R" + Integer.toString(ra) + " " + cons;
			sum = Integer.parseInt(cons, 16) + Integer.parseInt(register[ra], 16);
			//Assign value of sum to register a
			if(ra>0)
				register[ra] = Integer.toHexString(sum);
			regChanged[ra] = true;
			pc+=2;
			break;
			//SUBIM R[Ra]<- R[Ra]-cons TODO: If underflow it return a 3 bit hex   number.
		case "01010":
			//Get literal number, register number and subtract the literal to ra.
			dec1 = Integer.parseInt(binaryInstruction.substring(8, 12), 2);
			dec2 = Integer.parseInt(binaryInstruction.substring(12, 16), 2);
			cons = Integer.toHexString(dec1) + Integer.toHexString(dec2);
			currInstruction = instructionRegister + ": SUBIM" + " R" + Integer.toString(ra) + " " + cons;
			dif = Integer.parseInt(cons, 16) - Integer.parseInt(register[ra], 16);
			//Assign value of dif to register a 
			if(ra>0)
				register[ra] = Integer.toHexString(dif);
			regChanged[ra] = true;
			pc+=2;
			break;
			//AND	R[Ra]<- R[Rb]&&R[Rc]
		case "01011":
			currInstruction = instructionRegister + ": AND" + " R" + Integer.toString(ra) + " R" + Integer.toString(rb) + " R" + Integer.toString(rc);
			b = Integer.parseInt(register[rb], 16);
			int c = Integer.parseInt(register[rc], 16);
			if(ra>0)
				register[ra] = Integer.toHexString(b & c);
			regChanged[ra] = true;
			pc+=2;

			break;
			//OR	R[Ra]<- R[Rb]|R[Rc]
		case "01100":
			currInstruction = instructionRegister + ": OR" + " R" + Integer.toString(ra) + " R" + Integer.toString(rb) + " R" + Integer.toString(rc);
			b = Integer.parseInt(register[rb], 16);
			c = Integer.parseInt(register[rc], 16);
			if(ra>0)
				register[ra] = Integer.toHexString(b | c);
			regChanged[ra] = true;
			pc+=2;
			break;
			//XOR	R[Ra]<- R[Rb]^R[Rc]
		case "01101": 
			currInstruction = instructionRegister + ": XOR" + " R" + Integer.toString(ra) + " R" + Integer.toString(rb) + " R" + Integer.toString(rc);
			b = Integer.parseInt(register[rb], 16);
			c = Integer.parseInt(register[rc], 16);
			if(ra>0)
				register[ra] = Integer.toHexString(b ^ c);
			regChanged[ra] = true;
			pc+=2;

			break;
			//NOT	R[Ra]<- !R[Rb]
		case "01110":
			currInstruction = instructionRegister + ": NOT" + " R" + Integer.toString(ra) + " R" + Integer.toString(rb);
			b = Integer.parseInt(register[rb], 16);
			if(ra>0)
				register[ra] = Integer.toHexString(b ^ 0xff);
			regChanged[ra] = true;
			pc+=2;

			break;
			//NEG	R[Ra]<- -R[Rb] 
		case "01111":
			currInstruction = instructionRegister + ": NEG" + " R" + Integer.toString(ra) + " R" + Integer.toString(rb);
			b = Integer.parseInt(register[rb], 16);
			register[ra] = Integer.toHexString((b ^ 0xff) + 1);
			pc+=2;
			regChanged[ra] = true;
			break;
			//SHIFTR	R[Ra]<- R[Rb]>>>R[Rc]
		case "10000":
			currInstruction = instructionRegister + ": SHIFTR" + " R" + Integer.toString(ra) + " R" + Integer.toString(rb) + " R" + Integer.toString(rc);
			b = Integer.parseInt(register[rb], 16);
			c = Integer.parseInt(register[rc], 16);
			if(ra>0)
				register[ra] = Integer.toHexString(b>>c);
			regChanged[ra] = true;
			pc+=2;

			break;
			//SHIFTL	R[Ra]<- R[Rb]<<<R[Rc]
		case "10001":
			currInstruction = instructionRegister + ": SHIFTL" + " R" + Integer.toString(ra) + " R" + Integer.toString(rb) + " R" + Integer.toString(rc);
			b = Integer.parseInt(register[rb], 16);
			c = Integer.parseInt(register[rc], 16);
			if(ra>0)
				register[ra] = Integer.toHexString(b<<c);
			regChanged[ra] = true;
			pc+=2;

			break;
			//ROTAR	R[Ra]<- R[Rb] shr R[Rc]
		case "10010":
			currInstruction = instructionRegister + ": ROTAR" + " R" + Integer.toString(ra) + " R" + Integer.toString(rb) + " R" + Integer.toString(rc);
			b = Integer.parseInt(register[rb], 16);
			c = Integer.parseInt(register[rc], 16);
			if(ra>0) {
				if(b%2==1) register[ra] = Integer.toHexString((b>>c)|0x80);
				else register[ra] = Integer.toHexString(b>>c);
				pc+=2;
			}
			regChanged[ra] = true;
			break;
			//ROTAL	R[Ra]<- R[Rb] rtl R[Rc]
		case "10011":
			currInstruction = instructionRegister + ": ROTAL" + " R" + Integer.toString(ra) + " R" + Integer.toString(rb) + " R" + Integer.toString(rc);
			b = Integer.parseInt(register[rb], 16);
			c = Integer.parseInt(register[rc], 16);
			if(ra>0) {
				if((b & 0x8000) > 0) register[ra] = Integer.toHexString((b<<c)|0x01);
				else register[ra] = Integer.toHexString(b<<c);
			}
			pc+=2;
			regChanged[ra] = true;
			break;
			//JMPRIND [pc] <- [R[ra]]
		case "10100":
			currInstruction = instructionRegister + ": JMPRIND" + " R" + Integer.toString(ra);
			int addr = Integer.parseInt(register[ra], 16);
			pc = addr;
			break;
			//JMPADDR	[pc] <- address
		case "10101":
			currInstruction = instructionRegister + ": JMPADDR " + Integer.toHexString(f3address);
			addr = Integer.parseInt(binaryInstruction.substring(5, 16), 2);
			pc = addr;
			break;
			//JCONDRIN	If cond then [pc] <- [R[ra]]
		case "10110":
			currInstruction = instructionRegister + ": JCONDRIN" + " R" + Integer.toString(ra);
			if(cond) {
				ra = Integer.parseInt(binaryInstruction.substring(8, 11), 2);
				addr = Integer.parseInt(register[ra], 16);
				pc = addr;
			}
			break;
			//JCONDADDR	If cond then [pc] <-address
		case "10111":
			currInstruction = instructionRegister + ": JCONDADDR " + Integer.toHexString(f3address) ;
			if(cond) {
				addr = Integer.parseInt(binaryInstruction.substring(5, 16), 2);
				pc = addr;
			}
			else pc += 2;
			break;
			//LOOP [R[ra]] <-[R[ra]] – 1 If R[Ra] != 0 [pc] <- address
		case "11000": 
			currInstruction = instructionRegister + ": LOOP" + " R" + Integer.toString(ra) + ", " + Integer.toHexString(f2address);
			int reg = Integer.parseInt(register[ra], 16);
			if(reg == 0) {
				pc+=2;
			}
			else{
				reg--;
				pc = f2address;
			}
			if(ra>0)
				register[ra] = String.format("%2s", Integer.toHexString(reg)).replace(' ','0');
//				register[ra] = Integer.toHexString(reg); 
			regChanged[ra] = true;
			break;
			//GRT Cond <- R[Ra] > R[Rb]
		case "11001":
			currInstruction = instructionRegister + ": GRT" + " R" + Integer.toString(ra)+ " R" + Integer.toString(rb);
			cond = Integer.parseInt(register[ra],16) > Integer.parseInt(register[rb],16);
			pc+=2;
			break;
			//GRTEQ Cond <- R[Ra] >= R[Rb]
		case "11010": 
			currInstruction = instructionRegister + ": GRTEQ" + " R" + Integer.toString(ra)+ " R" + Integer.toString(rb);
			cond = Integer.parseInt(register[ra],16) >= Integer.parseInt(register[rb],16);
			pc+=2;
			break;
			//EQ	Cond <- R[Ra] == R[Rb] 
		case "11011":
			currInstruction = instructionRegister + ": EQ" + " R" + Integer.toString(ra)+ " R" + Integer.toString(rb);
			cond = Integer.parseInt(register[ra],16) == Integer.parseInt(register[rb],16);
			pc+=2;
			break;
			//NEQ	Cond <- R[Ra] != R[Rb]
		case "11100":
			currInstruction = instructionRegister + ": NEQ" + " R" + Integer.toString(ra)+ " R" + Integer.toString(rb);
			cond = Integer.parseInt(register[ra],16) != Integer.parseInt(register[rb],16);
			pc+=2;
			break;
			//NOP
		case "11101":
			currInstruction = instructionRegister + ": NOP";
			pc+=2;
			break;
			//CALL SP <- SP - 2 mem[SP] <- PC PC <- address
		case "11110":
			currInstruction = instructionRegister + ": CALL " + Integer.toHexString(f3address);
			sp = sp -2;
			memory[sp]  = Integer.toString(pc);
			pc = f3address;
			break;
			//RETURN PC <- mem[SP] SP <- SP + 2
		case "11111":
			currInstruction = instructionRegister + ": RETURN";
			pc = Integer.parseInt(memory[sp]);
			sp = sp + 2;
			break;
		default: 
			currInstruction = "-";
			break;
		}
	}

	public String getCurrInstruction() {
		return this.currInstruction;
	}

	public void reset() {
		this.instructionRegister = "";
		this.currInstruction = "-";
		this.pc = 0;
		this.sp = 4095;
		this.cond = false;
		for (int i=0;i<4096;i++) {
			this.memory[i] = "00";
			if(i<8) this.register[i] = "00";
		}
	}

	public String[] getMemory() {
		return this.memory;
	}

	public String[] getRegister() {
		return this.register;
	}

	public boolean[] getRegChanged() {
		return this.regChanged;
	}

	public void resetRegChange() {
		this.regChanged = new boolean[8];
	}
	
	public void keyboardEdit(int Index, String[] values) {
		for(int i=Index;i<Index+values.length;i++){
			this.memory[i] = values[i-Index];
		}
	}
}
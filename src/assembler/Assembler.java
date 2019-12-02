package assembler;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.io.BufferedWriter;
import java.io.FileWriter;

public class Assembler {

	/*
	 * All possible states that a line can be interpreted as
	 * 
	 */
	enum Format {
		FORMAT1, 
		FORMAT2, 
		FORMAT3,
		LABEL,
		ORIGIN,
		DEFINEBYTE,
		CONSTANT,
		COMMENT,
		BLANKLINE,
		SPECIAL,
		NONE
	}

	/*
	 * Matches a string with a format type
	 */	
	static Format getFormat(String line) {
		if(line.matches("(\\s{4}|\\t)(LOADRIND|STORERIND|ADD|SUB|AND|OR|XOR|NOT|NEG|SHIFTR|SHIFTL|ROTAR|ROTAL|GRT|GRTEQ|EQ|NEQ)\\s(\\s*R[0-7]\\s*,|\\s*R[0-7]){1,3}(\\s*\\/\\/.*)?\\s*$")) { 
			return Format.FORMAT1;
		}
		else if(line.matches("(\\s{4}|\\t)(((LOAD|LOADIM|JMPRIND|JCONDRIN|POP|PUSH|ADDIM|SUBIM|LOOP)(\\s+R[0-7]\\s*)(,\\s*(#[0-9a-fA-F]*|\\w+)?))|(STORE(\\s+\\w+\\s*)(,\\s*R[0-7]\\s*)))(\\s*\\/\\/.*)?\\s*$")) {
			return Format.FORMAT2;
		}
		else if(line.matches("(\\s{4}|\\t)org\\s(\\d|[a-fA-F]){1,3}(\\s*\\/\\/.*)?\\s*$")) {
			return Format.ORIGIN;
		}
		else if(line.matches("\\w*?\\sdb\\s(\\d[a-fA-F]|[a-fA-F]\\d|\\d{1,2}|[a-fA-F]{1,2})(,\\s*(\\d[a-fA-F]|[a-fA-F]\\d|\\d{1,2}|[a-fA-F]{1,2}))*(\\s*\\/\\/.*)?\\s*$")) {
			return Format.DEFINEBYTE;
		}
		else if(line.matches("const\\s\\w+\\s(\\d[a-fA-F]|[a-fA-F]\\d|\\d{1,2}|[a-fA-F]{1,2})(\\s*\\/\\/.*)?\\s*$")) {
			return Format.CONSTANT;
		}
		else if(line.matches("(\\s{4}|\\t)(JMPADDR|JCONDADDR|CALL)\\s\\w+?(\\s*\\/\\/.*)?\\s*$")) {
			return Format.FORMAT3;
		}
		else if(line.matches("(\\w+):(\\s*\\/\\/.*)?(\\s*$)")) {
			return Format.LABEL;
		}
		else if(line.matches("\\s*(\\/\\/).*")) {
			return Format.COMMENT;
		}
		else if(line.matches("\\s*")) {
			return Format.BLANKLINE;
		}
		else if(line.matches("(\\s{4}|\\t)(RETURN|NOP)(\\s*\\/\\/.*)?\\s*$")) {
			return Format.SPECIAL;
		}
		else {
			return Format.NONE;
		}
	}

	/*
	 * Matches the a opcode with its corresponding binary representation
	 */
	private static String opCode(String mnemonic) {
		switch(mnemonic) {
		case "LOAD": return "00000";
		case "LOADIM": return "00001";
		case "POP": return "00010";
		case "STORE": return "00011";
		case "PUSH": return "00100";
		case "LOADRIND": return "00101";
		case "STORERIND": return "00110";
		case "ADD": return "00111";
		case "SUB": return "01000";
		case "ADDIM": return "01001";
		case "SUBIM": return "01010";
		case "AND": return "01011";
		case "OR": return "01100";
		case "XOR": return "01101";
		case "NOT": return "01110";
		case "NEG": return "01111";
		case "SHIFTR": return "10000";
		case "SHIFTL": return "10001";
		case "ROTAR": return "10010";
		case "ROTAL": return "10011";
		case "JMPRIND": return "10100";
		case "JMPADDR": return "10101";
		case "JCONDRIN": return "10110";
		case "JCONDADDR": return "10111";
		case "LOOP": return "11000";
		case "GRT": return "11001";
		case "GRTEQ": return "11010";
		case "EQ": return "11011";
		case "NEQ": return "11100";
		case "NOP": return "11101";
		case "CALL":return "11110";
		case "RETURN": return "11111";
		default: return null;
		}
	}

	/*
	 * Reads a file and translates each line of code into hexadecimal values that are written into another file 
	 */
	public static String assemble(String fileName) throws IOException {
		PrintWriter error = new PrintWriter(new BufferedWriter(new FileWriter("src/errors.txt")));
		String outputFile = fileName.replaceAll(".asm", ".obj");
		PrintStream fileOut = new PrintStream(outputFile);
		System.setOut(fileOut);
		HashMap<String, String> vars = new HashMap<String, String>();
		HashMap<String, String> cons = new HashMap<String, String>();
		HashMap<String, String> labels = new HashMap<String, String>();
		HashMap<Integer, String> ordered = new HashMap<Integer, String>();
		int maxLine = 0;
		String HexHold;

		//First pass. Find all vars, const, and labels and add them to their maps
		try(BufferedReader br = new BufferedReader(new FileReader(fileName))) {
			String line;
			int linePos = 0;
			while((line = br.readLine()) != null) {
				String[] tokens = line.split("\\s*,\\s*|\\s+|\\/\\/.*");
				switch(getFormat(line)) {
				case DEFINEBYTE:
					vars.put(tokens[0], Integer.toString(linePos));
					for(int i = 2; i < tokens.length; i++) {
						HexHold = String.format("%2s", Integer.toHexString(Integer.parseInt(tokens[i], 16))).replace(' ','0');
						if(ordered.put(linePos++, HexHold) != null)
							overwriteWarning(error, linePos-1);
					}
					break;
				case CONSTANT:
					cons.put(tokens[1], tokens[2]);
					break;
				case ORIGIN:
					linePos = Integer.parseInt(tokens[2], 16) * 2;
					break;
				case LABEL:
					if(linePos%2 == 1){
						linePos++;
					}
					labels.put(tokens[0].substring(0, tokens[0].length()-1), Integer.toString(linePos));
					break;
				case FORMAT1:
					linePos += 2;
					break;
				case FORMAT2:
					linePos += 2;
					break;
				case FORMAT3:
					linePos += 2;
					break;
				case COMMENT:
					break;
				case BLANKLINE:
					break;
				case SPECIAL:
					linePos += 2;
					break;
				default:
					break;
				}
				if(linePos>maxLine) maxLine = linePos;
			}
		}
		catch(IOException e) {
		}

		//Second pass. 
		try(BufferedReader br = new BufferedReader(new FileReader(fileName))) {
			String line;
			int linePos = 0;
			int asmLinePos = 1;
			while((line = br.readLine()) != null) {
				StringBuilder result = new StringBuilder();
				String resultHexa = "";
				String[] tokens = line.split("\\s*,\\s*|\\s+|\\/\\/.*"); 

				switch(getFormat(line)) {
				case FORMAT1:
					//First 5 bits are the opCode
					result.append(opCode(tokens[1]));
					//Append the 3 bits of each register
					for(int i = 2; i < tokens.length; i++) {
						int register = Character.getNumericValue(tokens[i].charAt(1));
						String bRegister = String.format("%3s", Integer.toBinaryString(register)).replace(' ','0');
						result.append(bRegister);
					}
					//If line contains only two registers apend the last 3 bits for the unused register
					if(tokens.length == 4) {
						result.append("000");
					}
					//If line contains only one register append the last 6 bits for the unused registers
					else if(tokens.length == 3){
						result.append("000000");
					}
					//Append the last 2 bits to achive 16 bits
					result.append("00");
					resultHexa = Integer.toHexString(Integer.parseInt(result.substring(0),2)).toUpperCase();
					if(linePos%2 == 1) linePos++;
					if(resultHexa.length() < 4) {
						if(ordered.put(linePos++, String.format("0" + resultHexa.toString().substring(0, 2)))!=null)
							overwriteWarning(error, linePos-1);
					}
					else {
						if(ordered.put(linePos++, resultHexa.toString().substring(0, 2))!=null)
							overwriteWarning(error, linePos-1);
					}
					if(resultHexa.length() <4) {
						if(ordered.put(linePos++, resultHexa.toString().substring(2, 3))!=null)
							overwriteWarning(error, linePos-1);
					}
					else {
						if(ordered.put(linePos++, resultHexa.toString().substring(2, 4))!=null)
							overwriteWarning(error, linePos-1);
					}
					break;
				case FORMAT2:
					if(tokens[1].equals("STORE")) {
						result.append("00011");
						int register = Character.getNumericValue(tokens[3].charAt(1));
						String bRegister = String.format("%3s", Integer.toBinaryString(register)).replace(' ','0');
						result.append(bRegister);
						if(vars.containsKey(tokens[2])) {
							register = Integer.parseInt(vars.get(tokens[2]));
							bRegister = String.format("%8s", Integer.toBinaryString(register)).replace(' ','0');
							result.append(bRegister);
						}
						//check if its a const
						else if(cons.containsKey(tokens[2])) {
							register = Integer.parseInt(cons.get(tokens[2]), 16);
							bRegister = String.format("%8s", Integer.toBinaryString(register)).replace(' ','0');
							result.append(bRegister);
						}
						//check if its a label
						else if(labels.containsKey(tokens[2])){
							register = Integer.parseInt(labels.get(tokens[2]));
							bRegister = String.format("%8s", Integer.toBinaryString(register)).replace(' ','0');
							result.append(bRegister);
						}
						//check if its a literal hexadecimal number
						else if(tokens[2].matches("#(\\d[a-fA-F]|[a-fA-F]\\d|[a-fA-F]+|\\d+)")) {
							register = Integer.parseInt(tokens[2].substring(1, tokens[2].length()), 16);
							bRegister = String.format("%8s", Integer.toBinaryString(register)).replace(' ','0');
							result.append(bRegister);
						}
						else {
							errorDetected(error ,asmLinePos, "Unable to resolve \"" + tokens[2] + "\"", line); 
							break;
						}
					} else {
						//First 5 bits are the opCode
						result.append(opCode(tokens[1]));
						//Format 2 always has one register. Append the 3 bits of the register.
						int register = Character.getNumericValue(tokens[2].charAt(1));
						String bRegister = String.format("%3s", Integer.toBinaryString(register)).replace(' ','0');
						result.append(bRegister);
						//If the line has another parameter other than the register...
						if(tokens.length > 3) {
							//check if its a variable
							if(vars.containsKey(tokens[3])) {
								register = Integer.parseInt(vars.get(tokens[3]));
								bRegister = String.format("%8s", Integer.toBinaryString(register)).replace(' ','0');
								result.append(bRegister);
							}
							//check if its a const
							else if(cons.containsKey(tokens[3])) {
								register = Integer.parseInt(cons.get(tokens[3]), 16);
								bRegister = String.format("%8s", Integer.toBinaryString(register)).replace(' ','0');
								result.append(bRegister);
							}
							//check if its a label
							else if(labels.containsKey(tokens[3])){
								register = Integer.parseInt(labels.get(tokens[3]));
								bRegister = String.format("%8s", Integer.toBinaryString(register)).replace(' ','0');
								result.append(bRegister);
							}
							//check if its a literal hexadecimal number
							else if(tokens[3].matches("#(\\d[a-fA-F]|[a-fA-F]\\d|[a-fA-F]+|\\d+)")) {
								register = Integer.parseInt(tokens[3].substring(1, tokens[3].length()), 16);
								bRegister = String.format("%8s", Integer.toBinaryString(register)).replace(' ','0');
								result.append(bRegister);
							}
							else {
								errorDetected(error ,asmLinePos, "Unable to resolve \"" + tokens[3] + "\"", line); 
								break;
							}
						}
						//If it only has a register append the rest of the bits to achive 16 bits.
						else {
							result.append("00000000");
						}
					}
					resultHexa = Integer.toHexString(Integer.parseInt(result.substring(0,16),2)).toUpperCase();
					if(linePos%2 == 1) linePos++;
					if(resultHexa.length() < 4) {
						if(ordered.put(linePos++, String.format("0" + resultHexa.toString().substring(0, 2)))!=null)
							overwriteWarning(error, linePos-1);
					}
					else {
						if(ordered.put(linePos++, resultHexa.toString().substring(0, 2))!=null)
							overwriteWarning(error, linePos-1);
					}
					if(resultHexa.length() <4) {
						if(ordered.put(linePos++, resultHexa.toString().substring(2, 3))!=null)
							overwriteWarning(error, linePos-1);
					}
					else
						if(ordered.put(linePos++, resultHexa.toString().substring(2, 4))!=null)
							overwriteWarning(error, linePos-1);
					break;
				case FORMAT3:
					result.append(opCode(tokens[1]));
					int address;
					String bAddress;
					//check if its a variable
					if(vars.containsKey(tokens[2])) {
						address = Integer.parseInt(vars.get(tokens[2]), 16);
						bAddress = String.format("%11s", Integer.toBinaryString(address)).replace(' ','0');
						result.append(bAddress);
					}
					//check if its a conts
					else if(cons.containsKey(tokens[2])) {
						address = Integer.parseInt(cons.get(tokens[2]),16);
						bAddress = String.format("%11s", Integer.toBinaryString(address)).replace(' ','0');
						result.append(bAddress);
					}
					//check if its a label
					else if(labels.containsKey(tokens[2])){
						address = Integer.parseInt(labels.get(tokens[2]));
						bAddress = String.format("%11s", Integer.toBinaryString(address)).replace(' ','0');
						result.append(bAddress);
					}
					//its a literal number
					else if(tokens[2].matches("([a-fA-F]\\d|\\d[a-fA-F]|\\d{1,2}|[a-fA-F]{1,2})")){
						address = Integer.parseInt(tokens[2], 16);
						bAddress = String.format("%11s", Integer.toBinaryString(address)).replace(' ','0');
						result.append(bAddress);
					}
					else {
						errorDetected(error, asmLinePos, "Unable to resolve \"" + tokens[2] + "\"", line);
						break;
					}
					resultHexa = Integer.toHexString(Integer.parseInt(result.substring(0),2)).toUpperCase();
					if(linePos%2 == 1) linePos++;
					if(resultHexa.length() < 4) {
						if(ordered.put(linePos++, String.format("0" + resultHexa.toString().substring(0, 2)))!=null)
							overwriteWarning(error, linePos-1);
					}
					else {
						if(ordered.put(linePos++, resultHexa.toString().substring(0, 2))!=null)
							overwriteWarning(error, linePos-1);
					}
					if(resultHexa.length() <4) {
						if(ordered.put(linePos++, resultHexa.toString().substring(2, 3))!=null)
							overwriteWarning(error, linePos-1);
					}
					else {
						if(ordered.put(linePos++, resultHexa.toString().substring(2, 4))!=null)
							overwriteWarning(error, linePos-1);
					}
					break;
				case DEFINEBYTE:
					linePos += tokens.length - 2;
					break;
				case CONSTANT:
					break;
				case LABEL:
					break;
				case ORIGIN:
					linePos = Integer.parseInt(tokens[2], 16) * 2;
					break;
				case COMMENT:
					break;
				case BLANKLINE:
					break;
				case SPECIAL:
					result.append(opCode(tokens[1]));
					result.append("00000000000");
					resultHexa = Integer.toHexString(Integer.parseInt(result.substring(0),2)).toUpperCase();
					if(linePos%2 == 1) linePos++;
					if(ordered.put(linePos++, String.format(resultHexa.toString().substring(0, 2)))!=null)
						overwriteWarning(error, linePos-1);
					break;
				default:
					errorDetected(error ,asmLinePos, "Syntax error", line); 
					break;
				}
				asmLinePos++;
			}
			error.close();
		}
		catch(IOException e) {
		}
		for(int i =0; i<maxLine;i++) {
			if(ordered.get(i) != null) {
				System.out.print(ordered.get(i));
			}
			else
				System.out.print("00");
			i++;
			if(ordered.get(i) != null) {
				System.out.println(ordered.get(i));
			}
			else
				System.out.println("00");
		}
		return outputFile;
	}

	/*
	 * Writes a given error message with a line position to a specific file
	 */
	private static void errorDetected(PrintWriter writer,int linePos, String errorType, String line) {
		writer.append(errorType + " at line: " + linePos + "  \"" + line + "\" " + findError(line));
		writer.append('\n');
	}
	private static void overwriteWarning(PrintWriter writer, int linePos) {
		writer.append("Warning: Data at Address " + linePos + " has been overwritten.  ");
		writer.append('\n');
	}
	private static String findError(String line) {
		String errorMsg = "";
		//Errors for format 1
		if(line.matches(".*(LOADRIND|STORERIND|ADD|SUB|AND|OR|XOR|NOT|NEG|SHIFTR|SHIFTL|ROTAR|ROTAL|GRT|GRTEQ|EQ|NEQ).*")) {
			if(!line.matches("(\\s{4}|\\t).*"))
				errorMsg = "Line missing a tab or 4 spaces before instruction";
			else if(!line.matches(".*(\\s*R[0-7]\\s*,|\\s*R[0-7]){1,3}.*"))
				errorMsg = "Instruction has an invalid amount of registers.";
		}
		//Errors for format 2
		else if(line.matches(".*(LOAD|LOADIM|JMPRIND|JCONDRIN|POP|STORE|PUSH|ADDIM|SUBIM|LOOP).*")) {
			if(!line.matches("(\\s{4}|\\t).*"))
				errorMsg = "Line missing a tab or 4 spaces before instruction";
			else if(!line.matches(".*R[0-7].*"))
				errorMsg = "Instruction must have 1 register.";
			else if(line.matches("(.*R[0-7].*){2,}"))
				errorMsg = "Instruction has more than 1 register.";
		}
		//Errors for Return and Nop
		else if(line.matches(".*(RETURN|NOP).*")) {
			if(!line.matches("(\\s{4}|\\t).*"))
				errorMsg = "Line missing a tab or 4 spaces before instruction.";
			else if(line.matches(".*"))
				errorMsg = "Instruction does not contains any parameters.";
		}
		//Errors for format 3
		else if(line.matches(".*(JMPADDR|JCONDADDR|CALL).*")) {
			if(!line.matches("(\\s{4}|\\t).*"))
				errorMsg = "Line missing a tab or 4 spaces before instruction.";
			else if(line.matches("\\s.*"))
				errorMsg = "Instruction only needs and address.";
		}

		return errorMsg;
	}
}
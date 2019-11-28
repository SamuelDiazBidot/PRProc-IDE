package assembler;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class AssemblerTest {

	@Test
	public void getFormartest() {
		assertEquals(Assembler.getFormat("	LOAD R1, valor1"), Assembler.Format.FORMAT2);
		assertEquals(Assembler.getFormat("	LOADIM R1, #12"), Assembler.Format.FORMAT2);
		assertEquals(Assembler.getFormat("	LOADIM R1, #FF"), Assembler.Format.FORMAT2);
		assertEquals(Assembler.getFormat("	LOADIM R1, #1F"), Assembler.Format.FORMAT2);
		assertEquals(Assembler.getFormat("	LOADIM R1, #F1"), Assembler.Format.FORMAT2);
		assertEquals(Assembler.getFormat("	POP R1"), Assembler.Format.FORMAT2);
		assertEquals(Assembler.getFormat("	STORE R1, val"), Assembler.Format.FORMAT2);
		assertEquals(Assembler.getFormat("	PUSH R1"), Assembler.Format.FORMAT2);
		assertEquals(Assembler.getFormat("	LOADRIND R1, R2"), Assembler.Format.FORMAT1);
		assertEquals(Assembler.getFormat("	STORERIND R1, R2"), Assembler.Format.FORMAT1);
		assertEquals(Assembler.getFormat("	ADD R1, R2, R3"), Assembler.Format.FORMAT1);
		assertEquals(Assembler.getFormat("	SUB R1, R2, R3"), Assembler.Format.FORMAT1);
		assertEquals(Assembler.getFormat("	ADDIM R1, cons"), Assembler.Format.FORMAT2);
		assertEquals(Assembler.getFormat("	SUBIM R1, cons"), Assembler.Format.FORMAT2);
		assertEquals(Assembler.getFormat("	AND R1, R2, R3"), Assembler.Format.FORMAT1);
		assertEquals(Assembler.getFormat("	OR R1, R2, R3"), Assembler.Format.FORMAT1);
		assertEquals(Assembler.getFormat("	XOR R1, R2, R3"), Assembler.Format.FORMAT1);
		assertEquals(Assembler.getFormat("	NOT R1, R2"), Assembler.Format.FORMAT1);
		assertEquals(Assembler.getFormat("	NEG R1, R2"), Assembler.Format.FORMAT1);
		assertEquals(Assembler.getFormat("	SHIFTR R1, R2, R3"), Assembler.Format.FORMAT1);
		assertEquals(Assembler.getFormat("	SHIFTL R1, R2, R3"), Assembler.Format.FORMAT1);
		assertEquals(Assembler.getFormat("	ROTAR R1, R2, R3"), Assembler.Format.FORMAT1);
		assertEquals(Assembler.getFormat("	ROTAL R1, R2, R3"), Assembler.Format.FORMAT1);
		assertEquals(Assembler.getFormat("	JMPRIND R1"), Assembler.Format.FORMAT2);
		assertEquals(Assembler.getFormat("	JMPADDR val"), Assembler.Format.FORMAT3);
		assertEquals(Assembler.getFormat("	JCONDRIN R1"), Assembler.Format.FORMAT2);
		assertEquals(Assembler.getFormat("	JCONDADDR val"), Assembler.Format.FORMAT3);
		assertEquals(Assembler.getFormat("	LOOP R1, val"), Assembler.Format.FORMAT2);
		assertEquals(Assembler.getFormat("	GRT R1, R2"), Assembler.Format.FORMAT1);
		assertEquals(Assembler.getFormat("	GRTEQ R1, R2"), Assembler.Format.FORMAT1);
		assertEquals(Assembler.getFormat("	EQ R1, R2"), Assembler.Format.FORMAT1);
		assertEquals(Assembler.getFormat("	NEQ R1, R2"), Assembler.Format.FORMAT1);
		assertEquals(Assembler.getFormat("	CALL val"), Assembler.Format.FORMAT3);
		assertEquals(Assembler.getFormat("val db 01"), Assembler.Format.DEFINEBYTE);
		assertEquals(Assembler.getFormat("val db FF"), Assembler.Format.DEFINEBYTE);
		assertEquals(Assembler.getFormat("val db 0F"), Assembler.Format.DEFINEBYTE);
		assertEquals(Assembler.getFormat("val db F0"), Assembler.Format.DEFINEBYTE);
		assertEquals(Assembler.getFormat("val db 0, FF, F1"), Assembler.Format.DEFINEBYTE);
		assertEquals(Assembler.getFormat("	org 1"), Assembler.Format.ORIGIN);
		assertEquals(Assembler.getFormat("const val 15"), Assembler.Format.CONSTANT);
		assertEquals(Assembler.getFormat("const val 0A"), Assembler.Format.CONSTANT);
		//assertEquals(Assembler.getFormat("const val hh"), Assembler.Format.CONSTANT);
		assertEquals(Assembler.getFormat("start:"), Assembler.Format.LABEL);
		assertEquals(Assembler.getFormat("//comment"), Assembler.Format.COMMENT);
	}
}

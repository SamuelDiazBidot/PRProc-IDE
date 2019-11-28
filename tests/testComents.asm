	org 0	//origin 0
start:	//Start label
	LOADIM R1,#1	//load 1 to register 1
	LOADIM R2, #2	//load 2 to register 2
	ADD R3,R2,R1	//register 3 is now 3
fin://s
	JMPADDR fin //JUMP to fin





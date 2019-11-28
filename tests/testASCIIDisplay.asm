	org 0
	JMPADDR keyboard
	org 1
jose db 4A, 6F, 73, 65, 20, 20, 20, 20
sam db 53, 61, 6D, 75, 65, 6C, 20, 20
ken db 4B, 65, 6E, 6E, 65, 74, 68, 20
mig db 4D, 69, 67, 75, 65, 6C, 20, 20
	org 20
one db 60
two db DD
three db F4
	org 60
names:
	LOADIM R1, #2
	LOADIM R2, #32
	LOADIM R3, #4
	LOADIM R6, #1
looping:
	LOADIM R7, #7
	LOOP R3, change
	EQ R3, R0
	JCONDADDR done
change:
	LOADRIND R4, R1
	STORERIND R2, R4
	ADD R1, R1, R6
	ADD R2, R2, R6
	LOADRIND R4, R1
	STORERIND R2, R4
	LOOP R7, change
	LOADIM R2, #32
	JMPADDR looping
done:
	JMPADDR done
numbers:
	LOADIM R1, #40
	LOADIM R2, #32
	LOADIM R3, #03
	LOADIM R6, #1
numloop:
	LOADRIND R4, R1
	STORERIND R2, R4
	ADD R1, R1, R6
	LOOP R3, numloop
	JMPADDR done
keyboard:
	LOAD R1, #28
	LOAD R2, #29
	LOAD R3, #2A
	ADD R5, R1, R2
	ADD R5, R5, R3


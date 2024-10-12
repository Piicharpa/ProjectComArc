main    lw  0   1   n                       ;$1 = n
        lw  0   2   r                       ;$2 = r
        lw  0   6   comAdr                  ;$6 = combsub (address)
        jalr    6   7                       ;$7 = return address 
        halt
comsub  lw  0   6   pos1                    ;$6 = 1
        sw  5   7   stack                   ;save $7 (return addr) on stack
        add 5   6   5                       ;increment stack pointer
        sw  5   1   stack                   ;save $1 (input n) on stack
        add 5   6   5                       ;increment stack pointer
        sw  5   2   stack                   ;save $2 (input r) on stack
        add 5   6   5                       ;increment stack pointer
        sw  5   4   stack                   ;save $4 (temp) on stack
        add 5   6   5                       ;increment stack pointer
        beq 2   0   bcAdr                   ;r == 0
        beq 1   2   bcAdr                   ;n == r
        lw  0   6   neg1                    ;$6 = -1
        add 1   6   1                       ;n = n - 1
        lw  0   6   comAdr                  
        jalr    6   7                       ;combination(n-1,r)
        add 0   3   4                       ;value of combination(n-1,r)
        lw  0   6   neg1                    ;$6 = -1
        add 2   6   2                       ;r = r - 1
        lw  0   6   comAdr  
        jalr    6   7                       ;combination(n-1,r-1)
        add 3   4   3                       ;combination(n-1,r) + combination(n-1,r-1)
        lw  0   6   popAdr
        jalr    6   7                       ;jump to pop stack
pop     lw  0   6   neg1                    ;$6 = -1
        add 5   6   5                       ;increment stack pointer
        lw  5   4   stack                   ;recover original $4
        add 5   6   5                       ;increment stack pointer
        lw  5   2   stack                   ;recover original $2 
        add 5   6   5                       ;increment stack pointer
        lw  5   1   stack                   ;recover original $1
        add 5   6   5                       ;increment stack pointer
        lw  5   7   stack                   ;recover original return address
        jalr    7   6                       ;return. 
base    lw  0   3   pos1                    ;$3 = 1                    
        lw  0   6   popAdr
        jalr    6   7                       ;jump to pop stack
pos1    .fill    1
neg1    .fill   -1
n   .fill   7                               ;combination(7,3)
r   .fill   4
comAdr  .fill   comsub
bcAdr   .fill   base 
popAdr  .fill   pop
stack   .fill   0                           ;Start of stack
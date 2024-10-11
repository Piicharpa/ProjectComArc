main    lw  0   1   n                       ;$1 = n
        lw  0   6   fibAdr                  ;$6 = fibsub (address)
        jalr    6   7                       ;$7 = return address 
        halt
fibsub  lw  0   6   pos1                    ;$6 = 1
        sw  5   7   stack                   ;save return addr on stack
        add 5   6   5                       ;increment stack pointer
        sw  5   1   stack                   ;save $1 (input n) on stack
        add 5   6   5
        beq 1   0   rt0Adr                  ;n == 0
        beq 1   6   rt1Adr                  ;n == 1
        lw  0   6   neg1                    ;$6 = -1
        add 1   6   1                       ;n = n - 1
        lw  0   6   fibAdr  
        jalr  6   7                         ;fibonacci(n-1)
        add 0   3   4
        lw  0   6   neg1                    ;$6 = -1
        add 1   6   1                       ;n = n - 1  
        add 1   6   1                       ;n = n - 1
        lw  0   6   fibAdr  
        jalr    6   7                       ;fibonacci(n-2)
        add 3   4   3
        lw  0   6   popAdr
        jalr    6   7
pop     lw  0   6   neg1                    ;$6 = -1
        add 5   6   5  
        lw  5   1   stack                   ;recover original $1
        add 5   6   5  
        lw  5   7   stack                   ;recover original return address
        jalr    7   6                       ;return. 
rt0     lw  0   3   zero                    ;$3 = 0                    
        lw  0   6   popAdr
        jalr    6   7
rt1     lw  0   3   pos1                    ;$3 = 1                    
        lw  0   6   popAdr
        jalr    6   7
zero    .fill   0
pos1    .fill   1
neg1    .fill   -1
n       .fill   3                           ;fibonacci(3)
fibAdr  .fill   fibsub
popAdr  .fill   pop
rt0Adr  .fill   rt0
rt1Adr  .fill   rt1
stack   .fill   0                           ;Start of stack
        .fill   0    
        .fill   0    
        .fill   0
        .fill   0  

main    lw  0   1   n                       ;$1 = n
        lw  0   2   r                       ;$2 = r
        lw  0   6   combiAdr                ;$6 = combi_sub (address)
        jalr    6   7                       ;$7 = return address 
        halt
combi_sub   lw  0   6   pos1                ;$6 = 1
            sw  5   7   stack               ;save return addr on stack
            add 5   6   5                   ;increment stack pointer
            sw  5   1   stack               ;save $1 (input n) on stack
            add 5   6   5
            sw  5   2   stack               ;save $2 (input r) on stack
            add 5   6   5
            beq 2   0   base_case_Adr       ;r == 0
            beq 1   2   base_case_Adr       ;n == r
            lw  0   6   neg1                ;$6 = -1
            add 1   6   1                   ;n = n - 1
            lw  0   6   combiAdr  
            jalr  6   7                     ;combination(n-1,r)
            sw  5   3   stack
            add 5   6   5
            lw  5   1   stack               ;load original n back
            add 1   6   1                   ;n = n - 1  
            add 2   6   2                   ;r = r - 1
            lw  0   6   combiAdr  
            jalr  6   7                     ;combination(n-1,r-1)
            ;lw  0   6   neg1 ไม่แน่ใจว่าต้องโหลดใหม่ไหม ตอนเทสเอาออกก่อนก็ได้
            add 5   6   5                   ;decrement stack pointer
            lw  5   6   stack               ;load result of combination(n-1,r) from stack
            add 3   6   3                   ;combination(n-1,r) + combination(n-1,r-1)
            add 5   6   5  
            lw  5   2   stack               ;recover original $2 
            add 5   6   5  
            lw  5   1   stack               ;recover original $1
            add 5   6   5  
            lw  5   7   stack               ;recover original return address
            jalr    7   6                   ;return. 
base_case   add 0   6   3                   ;value = 1
            jalr    7   6                   ;return.
pos1    .fill    1
neg1    .fill   -1
n   .fill   7                               ;combination(7,3)
r   .fill   3
combiAdr    .fill   combi_sub
base_case_Adr   .fill   base_case
stack   .fill   0                            ;Start of stack
        .fill   0    
        .fill   0    
        .fill   0
        .fill   0  
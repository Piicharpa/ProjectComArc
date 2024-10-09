main    lw  0   1   n                       ;$1 = n
        lw  0   6   fiboAdr                 ;$6 = fibo_sub (address)
        jalr    6   7                       ;$7 = return address 
        halt
fibo_sub    lw  0   6   pos1                ;$6 = 1
            sw  5   7   stack               ;save return addr on stack
            add 5   6   5                   ;increment stack pointer
            sw  5   1   stack               ;save $1 (input n) on stack
            add 5   6   5
            beq 1   0   return0Adr          ;n == 0
            beq 1   6   return1Adr          ;n == 1
            lw  0   6   neg1                ;$6 = -1
            add 1   6   1                   ;n = n - 1
            lw  0   6   fiboAdr  
            jalr  6   7                     ;fibonacci(n-1)
            sw  5   3   stack
            add 5   6   5
            lw  5   1   stack               ;load original n back
            add 1   6   1                   ;n = n - 1  
            add 1   6   1                   ;n = n - 1
            lw  0   6   fiboAdr  
            jalr    6   7                   ;fibonacci(n-2)
            ;lw  0   6   neg1 ไม่แน่ใจว่าต้องโหลดใหม่ไหม ตอนเทสเอาออกก่อนก็ได้
            add 5   6   5                   ;decrement stack pointer
            lw  5   6   stack               ;load result of fibonacci(n-1) from stack
            add 3   6   3                   ;fibonacci(n-1) + fibonacci(n-2)
            add 5   6   5  
            lw  5   1   stack               ;recover original $1
            add 5   6   5  
            lw  5   7   stack               ;recover original return address
            jalr    7   6                   ;return. 
return0 add 0   0   3                       ;value = 0
        jalr    7   6
return1 add 0   6   3                       ;value = 1
        jalr    7   6
pos1    .fill    1
neg1    .fill   -1
n       .fill   3                           ;fibonacci(3)
fiboAdr     .fill   fibo_sub
return0Adr  .fill   return0
return1Adr  .fill   return1
stack   .fill   0                           ;Start of stack
        .fill   0    
        .fill   0    
        .fill   0
        .fill   0  
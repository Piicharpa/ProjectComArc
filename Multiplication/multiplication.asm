        lw 0 1 c            ; Load the multiplicand(c) into register 1
        lw 0 2 p            ; Load the multiplier(p) into register 2
        lw 0 5 0            ; Initialize register 5(result) to 0
        
loop:   beq 2 0 done        ; If p is 0, jump to done
        add 5 5 5           ; Double the result -> result = result + result
        add 1 1 1           ; Double mcand -> mcand = mcand * 2
        addi 2 -1 2         ; Decrement mplier by 1 -> mplier = mplier - 1
        beq 2 0 loop        ; Repeat loop if mplier is not 0

done:   add 5 0 1           ; Move the final result from register 5 to register 1
        halt                ; Stop execution
        
------------------------------------------------------------------------------------------------------        
        
        lw      0   1   c               ; $1 = c
        lw      0   2   p               ; $2 = p
        lw      0   3   pos0            ; $3 = 0
        lw      0   4   pos1            ; $4 = 1
        lw      0   5   pos0            ; $5 = 0

loop:   beq     2   0   done            ; if p == 0, jump to done
        and     6   2   4               ; $6 = p & 1 
        beq     6   0   skip_add        ; if LSB of p == 0, skip add
        add     3   1   3               ; result = result + c

skip_add:
        add     1   1   1               ; c = c << 1 
        add     5   4   5               ; increase loop count, stack pointer
        add     2   2   2               ; p = p >> 1 

        beq     0   0   loop            ; jump back loop, con cal

done:   halt                            ; end

pos0    .fill   0                       ; start, compare
pos1    .fill   1                       ; check LSB
mcand   .fill   32766                   ; multiplicand 
mplier  .fill   10383                   ; multiplier 
stack   .fill   0                       ; Stack
        .fill   0
        .fill   0
        .fill   0
        .fill   0

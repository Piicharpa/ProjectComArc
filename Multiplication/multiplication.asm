        lw      0   1   mcand   Load multiplicand (mcand) into $1
        lw      0   2   mplier  Load multiplier (mplier) into $2
        lw      0   3   pos0    Initialize result to 0 in $3
        lw      0   4   pos1    Load constant 1 into $4 for LSB check
        lw      0   5   pos0    Initialize loop counter or stack pointer

loop    beq     2   0   done    If mplier == 0, jump to done (end)
        and     6   2   4       $6 = mplier & 1 (check LSB of mplier)
        beq     6   0   sadd    If LSB of mplier is 0, skip addition

        add     3   1   3       result = result + mcand 

sadd    add     1   1   1       mcand = mcand << 1 (shift left by 1)
        add     2   2   2       mplier = mplier - 1 (decrement multiplier)
        
        beq     0   0   loop    Jump back to loop to continue the calculation

done    add     3   0   1       Move the final result from $3 to $1
        halt                    Stop execution

pos0    .fill   0       Constant 0 for initialization
pos1    .fill   1       Constant 1 for checking LSB
mcand   .fill   32766   Multiplicand (32766)
mplier  .fill   10383   Multiplier (10383)

stack   .fill   0       Stack for status checking

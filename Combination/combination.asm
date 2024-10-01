;combination(7,3)
main
lw  0   1   n ;$1 = n
lw  0   2   r ;$2 = r
lw  0   6   combiAdr ; $6 = combi_sub (address)
jalr    6   7;
halt

combi_sub
lw  0   6   pos1 ;$6 = 1
sw  5   7   stack
add 5   6   5
sw  5   1   stack
add 5   6   5
sw  5   2   stack
add 5   6   5

beq 2   0   base_case_Adr
beq 1   2   base_case_Adr


base_case 
lw  0   6   pos1 ;$6 = 1
add 0   6   3


zero    .fill   0
pos1    .fill    1
neg1    .fill   -1
n   .fill   7
r   .fill   3
combiAdr    .fill   combi_sub
base_case_Adr   .fill   base_case
stack   .fill   0 ; Start of stack
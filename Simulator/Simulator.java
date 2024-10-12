import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Simulator {

    public static void main(String[] args) {
        String fileName = "Assembler/File/output.txt"; // Path ของไฟล์ machine code

        MachineState state = new MachineState();
        if (!loadMemoryFromFile(state, fileName)) return;

        simulateMachine(state);
    }

    public static boolean loadMemoryFromFile(MachineState state, String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            int address = 0;

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                try {
                    int instruction = Integer.parseInt(line.trim());
                    state.mem[address++] = instruction;
                } catch (NumberFormatException e) {
                    System.out.println("Error reading instruction at address " + address + ": " + line);
                    return false;
                }
            }
            state.numMemory = address;
            state.highestNumMemory = address;
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
            return false;
        }

        return true;
    }

    public static void simulateMachine(MachineState state) {
        int instructionCount = 0;

        while (instructionCount < 5000) {
            printState(state);
            // pauseExecution();

            int instruction = fetch(state);
            instructionCount++;  // Count the current instruction

            if (instruction == 0) { // Assuming '0' represents HALT instruction
                halt();
                System.out.println("Total of " + instructionCount + " instructions executed.");
                break;
            }

            int opcode = (instruction >> 22) & 0x7;  // Opcode is in bits 22-24
            int regA = (instruction >> 19) & 0x7;    // RegA is in bits 19-21
            int regB = (instruction >> 16) & 0x7;    // RegB is in bits 16-18
            int destReg = instruction & 0x7;         // Dest register in bits 0-2
            int offset = signExtend(instruction & 0xFFFF); // 16-bit offset/sign-extended
            
            

            switch (opcode) {
                case 0: // ADD
                    state.reg[destReg] = state.reg[regA] + state.reg[regB];
                    if (destReg == 3) {
                        System.out.println("Updated $r3 in ADD: " + state.reg[destReg]);
                    }
                    break;

                case 1: // NAND
                    state.reg[destReg] = ~(state.reg[regA] & state.reg[regB]);
                    if (destReg == 3) {
                        System.out.println("Updated $r3 in NAND: " + state.reg[destReg]);
                    }
                    break;

                case 2: // LW (Load Word)
                    int memAddressLW = state.reg[regA] + offset;
                    if (memAddressLW >= 0 && memAddressLW < state.mem.length) {
                        state.reg[regB] = state.mem[memAddressLW];
                        if (regB == 3) {
                            System.out.println("Loading value into $r3: " + state.reg[regB]);
                        }
                    } else {
                        System.out.println("Error: Memory access out of bounds at address " + memAddressLW);
                        return;
                    }
                    break;

                case 3: // SW (Store Word)
                    int memAddressSW = state.reg[regA] + offset;
                    if(memAddressSW > state.highestNumMemory-1) state.highestNumMemory++;
                    if (memAddressSW >= 0 && memAddressSW < state.mem.length) {
                        state.mem[memAddressSW] = state.reg[regB];
                        if (regB == 3) {
                            System.out.println("Storing value from $r3: " + state.reg[regB]);
                        }
                    } else {
                        System.out.println("Error: Memory access out of bounds at address " + memAddressSW);
                        return;
                    }
                    break;

                case 4: // BEQ (Branch if Equal)
                    if (state.reg[regA] == state.reg[regB]) {
                      
                      
                        int absOffset = Math.abs(offset);
                        if(state.mem[absOffset] > 1000 ) state.pc = state.pc + 1 + offset;
                        else state.pc = state.mem[offset];

                        
                    
                    } else {
                        state.pc += 1;  // Only increment PC if branch is not taken
                    }
                    continue; // Skip the PC update for BEQ

                case 5: // JALR (Jump and Link Register)
                    if (regA != regB) {
                        state.reg[regB] = state.pc + 1;
                    }
                    state.pc = state.reg[regA];
                    continue;  // Skip PC update for JALR

                case 6: // HALT
                    halt();
                    System.out.println("Machine halted.");
                    System.out.println("Total of " + instructionCount + " instructions executed.");
                    System.out.println("Final state of the machine:");
                    printFinalState(state); // Printing state before exit
                    return;  // Exit simulation

                case 7: // NOOP (No Operation)
                    break;

                default:
                    System.out.println("Error: Illegal opcode " + opcode);
                    return;
            }

            updatePC(state);
        }
    }

    

    public static void printState(MachineState state) {
        System.out.println("\n@@@\nState:");
        System.out.println("\tPC: " + state.pc);
        System.out.println("\tMemory:");
        for (int i = 0; i < state.highestNumMemory; i++) {
            System.out.println("\t\tmem[" + i + "] = " + state.mem[i]);
        }
        System.out.println("\tRegisters:");
        for (int i = 0; i < state.reg.length; i++) {
            System.out.println("\t\treg[" + i + "] = " + state.reg[i]);
        }
        System.out.println("End state\n");
    }

    public static void printFinalState(MachineState state) {
        System.out.println("\n@@@\nState:");
        System.out.println("\tPC: " + state.pc);
        System.out.println("\tMemory:");
        for (int i = 0; i < state.numMemory; i++) {
            System.out.println("\t\tmem[" + i + "] = " + state.mem[i]);
        }
        System.out.println("\tRegisters:");
        for (int i = 0; i < state.reg.length; i++) {
            System.out.println("\t\treg[" + i + "] = " + state.reg[i]);
        }
        System.out.println("End state\n");
    }

    public static int fetch(MachineState state) {
        return state.mem[state.pc]; 
    }

    public static void halt() {
        System.out.println("Halt instruction encountered. Stopping simulation.");
    }

    public static void updatePC(MachineState state) {
        state.pc += 1; 
    }

    public static int signExtend(int value) {
        return (value & (1 << 15)) != 0 ? value | 0xFFFF0000 : value; // Sign extend correct
    }

    static class MachineState {
        int pc; 
        int[] reg = new int[8]; 
        int[] mem = new int[65536]; 
        int numMemory; 
        int highestNumMemory; 
    }
}
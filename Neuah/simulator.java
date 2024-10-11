import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class simulator {

    private static final int NUM_MEMORY = 65536;  // Size of the memory (65536 words)
    private static final int NUM_REGISTERS = 8;   // Number of registers (8 registers)

    // Opcodes
    private static final int ADD = 0;
    private static final int NAND = 1;
    private static final int LW = 2;
    private static final int SW = 3;
    private static final int BEQ = 4;
    private static final int JALR = 5;
    private static final int HALT = 6;
    private static final int NOOP = 7;

    public static class State {
        int pc;                          // Program Counter
        int[] memory;                    // Memory (65536 words)
        int[] registers;                 // 8 Registers
        int memorySize;                  // Size of the loaded memory

        public State() {
            pc = 0;
            memory = new int[NUM_MEMORY];
            registers = new int[NUM_REGISTERS];
        }
    }

    public static void main(String[] args) {
        State state = new State();
        loadProgram(state, "machinecode.txt"); // Load the machine code from file

        // Initialize registers to 0
        for (int i = 0; i < NUM_REGISTERS; i++) {
            state.registers[i] = 0;
        }

        // Simulate the program until halted
        while (true) {
            printState(state); // Print state before executing instruction

            int instruction = state.memory[state.pc];
            int opcode = (instruction >> 22) & 0b111;

            switch (opcode) {
                case ADD:
                    executeAdd(state, instruction);
                    break;
                case NAND:
                    executeNand(state, instruction);
                    break;
                case LW:
                    executeLw(state, instruction);
                    break;
                case SW:
                    executeSw(state, instruction);
                    break;
                case BEQ:
                    executeBeq(state, instruction);
                    break;
                case JALR:
                    executeJalr(state, instruction);
                    break;
                case HALT:
                    System.out.println("Halted.");
                    printState(state);
                    return; // End simulation
                case NOOP:
                    break; // Do nothing
                default:
                    System.err.println("Error: Unknown opcode.");
                    return;
            }

            if (opcode != JALR && opcode != BEQ) {
                state.pc++; // Move to the next instruction unless JALR/BEQ modifies PC
            }
        }
    }

    // Load the machine code program into memory
    private static void loadProgram(State state, String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                state.memory[state.memorySize] = Integer.parseInt(line);
                state.memorySize++;
            }
        } catch (IOException e) {
            System.err.println("Error: Unable to load program file.");
        }
    }

    // Add instruction (ADD)
    private static void executeAdd(State state, int instruction) {
        int regA = (instruction >> 19) & 0b111;
        int regB = (instruction >> 16) & 0b111;
        int destReg = instruction & 0b111;
        if (destReg != 0) { // Register 0 is always 0
            state.registers[destReg] = state.registers[regA] + state.registers[regB];
        }
    }

    // Nand instruction (NAND)
    private static void executeNand(State state, int instruction) {
        int regA = (instruction >> 19) & 0b111;
        int regB = (instruction >> 16) & 0b111;
        int destReg = instruction & 0b111;
        if (destReg != 0) {
            state.registers[destReg] = ~(state.registers[regA] & state.registers[regB]);
        }
    }

    // Load Word instruction (LW)
    private static void executeLw(State state, int instruction) {
        int regA = (instruction >> 19) & 0b111;
        int regB = (instruction >> 16) & 0b111;
        int offsetField = signExtend(instruction & 0xFFFF); // Sign extension
        int address = state.registers[regA] + offsetField;

        if (address >= 0 && address < NUM_MEMORY) {
            state.registers[regB] = state.memory[address];
        } else {
            System.err.println("Error: Memory address out of bounds.");
        }
    }

    // Store Word instruction (SW)
    private static void executeSw(State state, int instruction) {
        int regA = (instruction >> 19) & 0b111;
        int regB = (instruction >> 16) & 0b111;
        int offsetField = signExtend(instruction & 0xFFFF); // Sign extension
        int address = state.registers[regA] + offsetField;

        if (address >= 0 && address < NUM_MEMORY) {
            state.memory[address] = state.registers[regB];
        } else {
            System.err.println("Error: Memory address out of bounds.");
        }
    }

    // Branch if Equal instruction (BEQ)
    private static void executeBeq(State state, int instruction) {
        int regA = (instruction >> 19) & 0b111;
        int regB = (instruction >> 16) & 0b111;
        int offsetField = signExtend(instruction & 0xFFFF); // Sign extension

        if (state.registers[regA] == state.registers[regB]) {
            state.pc += offsetField; // Branch to target
        }
    }

    // Jump and Link instruction (JALR)
    private static void executeJalr(State state, int instruction) {
        int regA = (instruction >> 19) & 0b111;
        int regB = (instruction >> 16) & 0b111;

        int temp = state.pc + 1;
        state.pc = state.registers[regA] - 1; // Jump to regA address
        if (regB != 0) {
            state.registers[regB] = temp; // Store PC+1 into regB
        }
    }

    // Sign extension for 16-bit offset field
    private static int signExtend(int value) {
        if ((value & (1 << 15)) != 0) {
            return value | 0xFFFF0000; // If the sign bit is 1, extend with 1's
        }
        return value; // If the sign bit is 0, no need for extension
    }

    // Print current state (registers, memory, and PC)
    private static void printState(State state) {
        System.out.println("@@@");
        System.out.println("PC: " + state.pc);

        System.out.println("Memory:");
        for (int i = 0; i < state.memorySize; i++) {
            System.out.println("mem[" + i + "] = " + state.memory[i]);
        }

        System.out.println("Registers:");
        for (int i = 0; i < NUM_REGISTERS; i++) {
            System.out.println("reg[" + i + "] = " + state.registers[i]);
        }

        System.out.println("End of state");
    }
}

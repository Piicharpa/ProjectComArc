import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Simulator {

    public static void main(String[] args) {
        // Select a file from the folder
        String fileName = selectFile("Assembler/File/");
        if (fileName == null) return;

        // Load file and initialize machine state
        MachineState state = new MachineState();
        if (!loadMemoryFromFile(state, fileName)) return;

        // Start machine simulation
        simulateMachine(state);
    }

    // File selection from a folder
    public static String selectFile(String folderPath) {
        JFileChooser fileChooser = new JFileChooser(folderPath);
        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            return selectedFile.getAbsolutePath();
        } else {
            System.out.println("No file selected.");
            return null;
        }
    }

    // Load machine code into memory from file
    public static boolean loadMemoryFromFile(MachineState state, String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            int address = 0;

            // Read lines from the file and load into memory
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue; // Skip empty lines
                try {
                    int instruction = Integer.parseInt(line.trim());
                    state.mem[address++] = instruction;
                } catch (NumberFormatException e) {
                    System.out.println("Error reading instruction at address " + address + ": " + line);
                    return false;
                }
            }
            state.numMemory = address; // Update the count of memory instructions
            state.highestNumMemory = address; // Set highest memory address used
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
            return false;
        }

        return true; // Return true if successfully loaded
    }

    //hi
    // Simulate the machine execution
    public static void simulateMachine(MachineState state) {
        int instructionCount = 0;

        // Main simulation loop
        while (true) {
            if (instructionCount == 5000) break; // Limit to 5000 instructions

            printState(state); // Print state before executing instruction

            int instruction = fetch(state); // Fetch instruction

            if (instruction == 0) { // Assuming '0' represents the halt instruction
                halt();
                System.out.println("Total of " + (instructionCount + 1) + " instructions executed.");
                break;
            }

            int opcode = (instruction >> 22) & 0x7; // 22-24
            int regA = (instruction >> 19) & 0x7;   // 19-21
            int regB = (instruction >> 16) & 0x7;   // 16-18
            int destReg = instruction & 0x7;        // Last 3 bits for destination register
            int offset = instruction & 0xFFFF;      // Last 16 bits for offset

            switch (opcode) {
                case 0: // ADD
                    state.reg[destReg] = state.reg[regA] + state.reg[regB];
                    break;

                case 1: // NAND
                    state.reg[destReg] = ~(state.reg[regA] & state.reg[regB]);
                    break;

                case 2: // LW (Load Word)
                    state.reg[regB] = state.mem[state.reg[regA] + signExtend(offset)];
                    break;

                case 3: // SW (Store Word)
                    state.mem[state.reg[regA] + signExtend(offset)] = state.reg[regB];
                    break;

                case 4: // BEQ (Branch if Equal)
                    if (state.reg[regA] == state.reg[regB]) {
                        state.pc += signExtend(offset);
                    }
                    break;

                case 5: // JALR (Jump and Link Register)
                    state.reg[regB] = state.pc + 1;
                    state.pc = state.reg[regA];
                    break;

                case 6: // HALT
                    halt();
                    System.out.println("Machine halted.");
                    System.out.println("Total of " + instructionCount + " instructions executed.");
                    System.out.println("Final state of the machine:");
                    state.pc++;
                    printState(state);

                case 7: // NOOP (No Operation)
                    break;

                default:
                    System.out.println("Error: Illegal opcode " + opcode);
                    return;
            }

            instructionCount++;
            updatePC(state); // Update PC after executing instruction
        }

        printState(state); // Print state before exiting
    }

   

    // Print the current state of the machine
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

    // Fetch the instruction at the current PC
    public static int fetch(MachineState state) {
        return state.mem[state.pc]; // Return the instruction at PC
    }

    // Halt the simulator
    public static void halt() {
        System.out.println("Halt instruction encountered. Stopping simulation.");
    }

    // Increment the PC to point to the next instruction
    public static void updatePC(MachineState state) {
        state.pc += 1; // Increment PC
    }

    // Sign extend the offset from 16 bits to 32 bits
    public static int signExtend(int value) {
        return (value & (1 << 15)) != 0 ? value - (1 << 16) : value; // Sign extension
    }

    // Class representing the state of the machine
    static class MachineState {
        int pc; // Program counter
        int[] reg = new int[8]; // Assuming 8 registers
        int[] mem = new int[65536]; // Assuming 64K memory size
        int numMemory; // Number of instructions loaded into memory
        int highestNumMemory; // Highest memory address used
    }
}


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class simulator {
    private static final int NUMMEMORY = 65536; // Maximum memory units
    private static final int NUMREGS = 8; // Number of registers in the machine
    private static final int MAXLINELENGTH = 5000; // Maximum number of instructions for testing

    // Structure to hold the state of the machine
    public static class State {
        int pc = 0; // Program Counter (points to the next instruction)
        int[] mem = new int[NUMMEMORY]; // Memory
        int[] reg = new int[NUMREGS]; // Registers
        int numMemory = 0; // Number of memory units used
    }

    // Function to print the state of the machine
    public static void printState(State state) {
        System.out.println("\n@@@\nState:");
        System.out.println("\tPC: " + state.pc);
        System.out.println("\tMemory:");
        for (int i = 0; i < state.numMemory; i++) {
            System.out.println("\t\tmem[" + i + "] = " + state.mem[i]);
        }
        System.out.println("\tRegisters:");
        for (int i = 0; i < NUMREGS; i++) {
            System.out.println("\t\treg[" + i + "] = " + state.reg[i]);
        }
        System.out.println("End state\n");
    }

    // Main function controlling the operation of the simulator
    public static void main(String[] args) {

        // Select file from folder
        String fileName = selectFile("Output/");
        if (fileName == null) return;

        // Load file and set memory
        State state = new State();
        if (!loadMemoryFromFile(state, fileName)) return;

        // Start simulating the machine's operation
        simulateMachine(state);

    }

    // Function to select a file
    private static String selectFile(String folderPath) {
        File folder = new File(folderPath);
        File[] listOfFiles = folder.listFiles();

        if (listOfFiles == null || listOfFiles.length == 0) {
            System.err.println("Error: No files found in Output folder");
            return null;
        }

        System.out.println("Select a file to read:");
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                System.out.println(i + ": " + listOfFiles[i].getName());
            }
        }

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter file number: ");
        int fileIndex = scanner.nextInt();

        if (fileIndex < 0 || fileIndex >= listOfFiles.length || !listOfFiles[fileIndex].isFile()) {
            System.err.println("Error: Invalid file selection");
            return null;
        }

        return folderPath + listOfFiles[fileIndex].getName();
    }

    // Function to load memory from a file
    private static boolean loadMemoryFromFile(State state, String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (state.numMemory >= NUMMEMORY) {
                    System.err.println("Error: Memory overflow while loading file.");
                    return false;
                }
                state.mem[state.numMemory] = Integer.parseInt(line);
                state.numMemory++;
            }
            System.out.println("Loaded " + state.numMemory + " words into memory.");
            return true;
        } catch (IOException e) {
            System.err.println("Error: Can't open file " + fileName);
            e.printStackTrace();
            return false;
        } catch (NumberFormatException e) {
            System.err.println("Error: Invalid number format in file " + fileName);
            e.printStackTrace();
            return false;
        }
    }

    // Function to simulate the operation of the machine
    private static void simulateMachine(State state) {
        int totalInstructions = 0;

        while (true) {
            printState(state);
            if (state.pc < 0 || state.pc >= state.numMemory) {
                System.err.println("Error: Program Counter out of bounds.");
                break;
            }

            int opcode = state.mem[state.pc] >> 22;

            switch (opcode) {
                case 0: // ADD
                    executeRFormat(state, (a, b) -> a + b);
                    break;
                case 1: // NAND
                    executeRFormat(state, (a, b) -> ~(a & b));
                    break;
                case 2: // LW
                    executeLoadStore(state, true);
                    break;
                case 3: // SW
                    executeLoadStore(state, false);
                    break;
                case 4: // BEQ
                    executeBranch(state);
                    break;
                case 5: // JALR
                    executeJALR(state);
                    break;
                case 6: // HALT
                    System.out.println("Machine halted.");
                    System.out.println("Total of " + totalInstructions + " instructions executed.");
                    return;
                case 7: // NOOP
                    break;
                default:
                    System.err.println("Unknown opcode: " + opcode);
                    return;
            }

            state.pc++;
            totalInstructions++;

            if (totalInstructions > MAXLINELENGTH) {
                System.out.println("Max instruction length reached.");
                break;
            }
        }

        System.out.println("Final state of the machine:");
        printState(state);
    }

    // Function for R-Format instructions (ADD, NAND)
    private static void executeRFormat(State state, ArithmeticOperation operation) {
        int[] args = decodeRFormat(state.mem[state.pc]);
        state.reg[args[2]] = operation.apply(state.reg[args[0]], state.reg[args[1]]);
    }

    // Function for Load/Store instructions (LW, SW)
    private static void executeLoadStore(State state, boolean isLoad) {
        int[] args = decodeIFormat(state.mem[state.pc]);
        int offset = args[2] + state.reg[args[0]];

        // Memory access check
        if (offset < 0 || offset >= NUMMEMORY) {
            System.err.println("Memory access error: Invalid memory access at address " + offset);
            System.exit(1);
        }

        if (isLoad) {
            state.reg[args[1]] = state.mem[offset]; //load
        } else {
            state.mem[offset] = state.reg[args[1]]; //store
        }
    }

    // Function for BEQ instruction
    private static void executeBranch(State state) {
        int[] args = decodeIFormat(state.mem[state.pc]);
        if (state.reg[args[0]] == state.reg[args[1]]) {
            state.pc += args[2];
        }
    }

    // Function for JALR instruction
    private static void executeJALR(State state) {
        int[] args = decodeRFormat(state.mem[state.pc]);
        state.reg[args[1]] = state.pc + 1;
        state.pc = state.reg[args[0]] - 1;
    }

    // Function to decode R-Format instructions
    private static int[] decodeRFormat(int instruction) {
        return new int[]{
                (instruction >> 19) & 7, // regA
                (instruction >> 16) & 7, // regB
                instruction & 7          // destReg
        };
    }

    // Function to decode I-Format instructions
    private static int[] decodeIFormat(int instruction) {
        return new int[]{
                (instruction >> 19) & 7, // regA
                (instruction >> 16) & 7, // regB
                convertNum(instruction & 0xFFFF) // offset
        };
    }

    // Function to convert 16-bit to signed integer
    private static int convertNum(int num) {
        if ((num & (1 << 15)) != 0) {
            return num - (1 << 16);
        }
        return num;
    }

    // Interface for custom arithmetic operations
    @FunctionalInterface
    interface ArithmeticOperation {
        int apply(int a, int b);
    }
}

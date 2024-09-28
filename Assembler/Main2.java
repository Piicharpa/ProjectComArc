package Assembler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import Assembler.Tokenizer.Tokenizer;

public class Main2 {

    private static final int MAX_LINE_LENGTH = 1000;

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("error: usage: java Assembler <assembly-code-file> <machine-code-file>");
            System.exit(1);
        }

        String inFileString = args[0];
        String outFileString = args[1];

        try (BufferedReader inFile = new BufferedReader(new FileReader(inFileString));
             FileWriter outFile = new FileWriter(outFileString)) {

            String label = "", opcode = "", arg0 = "", arg1 = "", arg2 = "";
            while (readAndParse(inFile, label, opcode, arg0, arg1, arg2)) {
                // Handle the parsed line (e.g., convert opcode to machine code and write to the file)
                if (opcode.equals("add")) {
                    // Handle 'add' opcode
                }
                // Add handling for other opcodes
            }

        } catch (IOException e) {
            System.err.println("Error opening file: " + e.getMessage());
            System.exit(1);
        }
    }

    private static boolean readAndParse(BufferedReader inFile, String label, String opcode, String arg0, String arg1, String arg2) throws IOException {
        String line = inFile.readLine();
        if (line == null) {
            return false; // Reached end of file
        }

        // Reset variables
        label = opcode = arg0 = arg1 = arg2 = "";

        // Check if the line is too long
        if (line.length() > MAX_LINE_LENGTH) {
            System.out.println("error: line too long");
            System.exit(1);
        }

        // Use split to tokenize the line (assuming space/tab-separated tokens)
        String[] tokens = line.trim().split("\\s+");
        int tokenCount = tokens.length;

        // First token might be a label
        if (tokenCount > 0 && !isOpcode(tokens[0])) {
            label = tokens[0];
        }

        // Remaining tokens are opcode and arguments
        if (tokenCount > 1) opcode = tokens[1];
        if (tokenCount > 2) arg0 = tokens[2];
        if (tokenCount > 3) arg1 = tokens[3];
        if (tokenCount > 4) arg2 = tokens[4];

        return true;
    }

    private static boolean isOpcode(String token) {
        return token.equals("add") || token.equals("nand") || token.equals("lw") || token.equals("sw") || token.equals("beq") || token.equals("jalr") || token.equals("halt") || token.equals("noop");
    }

    private static boolean isNumber(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
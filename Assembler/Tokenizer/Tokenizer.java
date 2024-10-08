package Assembler.Tokenizer;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Tokenizer {
    
    private List<String> tokens;
    private int currentTokenIndex;

    public Tokenizer() {
        tokens = new ArrayList<>();
        currentTokenIndex = 0;
    }

    // Tokenize a line of assembly code
    public void tokenizeLine(String line) {
        tokens.clear();
        currentTokenIndex = 0;

        // Remove comments based on instruction type
        line = removeComment(line);

        // Split line into tokens using whitespace as the delimiter
        StringTokenizer st = new StringTokenizer(line);
        while (st.hasMoreTokens()) {
            tokens.add(st.nextToken());
        }
    }

    // Get the next token in the line
    public String getNextToken() {
        if (hasMoreTokens()) {
            return tokens.get(currentTokenIndex++);
        }
        return null; 
    }

    public String peekNextToken() {
        if (hasMoreTokens()) {
            int next = currentTokenIndex + 1;
            return tokens.get(next);
        }
        return null; 
    }

    // Check if there are more tokens
    public boolean hasMoreTokens() {
        return currentTokenIndex < tokens.size();
    }

    // Reset the tokenizer to the beginning
    public void resetTokenizer() {
        currentTokenIndex = 0;
    }

    // Helper function to remove comments from the line based on instruction type
    private String removeComment(String line) {
        // Split line into parts to determine instruction type
        String[] parts = line.split("\\s+");

        // Prepare cleaned line that contains instruction and its fields
        StringBuilder cleanedLine = new StringBuilder();

        // Check if there's a label
        int index = 0;
        // if (!isOpcode(parts[0]) && parts[0].length() <= 6) {
        if (!isOpcode(parts[0])) {
            // Add label
            cleanedLine.append(parts[0]);
            // Skip to inst
            index++; 
        }

        // Check if there is an instruction
        if (index < parts.length) {
            String instruction = parts[index];
            int argumentCount = getArgumentCount(instruction);
            cleanedLine.append(" ").append(instruction); // Append instruction

            // Append fields based on argument count
            for (int i = 0; i < argumentCount; i++) {
                if (index < parts.length) {
                    cleanedLine.append(" ").append(parts[++index]);
                }
            }

            return cleanedLine.toString().trim(); // Return the cleaned line
        }

        return line; // No comment found
    }

    private static boolean isOpcode(String token) {
        return token.equals("add") || token.equals("nand") || token.equals("lw") || token.equals("sw") || token.equals("beq") || token.equals("jalr") || token.equals("halt") || token.equals("noop");
    }

    // Get the number of arguments based on instruction type
    private int getArgumentCount(String instruction) {
        switch (instruction) {
            case "add":
            case "nand":
            case "lw":
            case "sw":
            case "beq":
                return 3; // R, I-type
            case "jalr":
                return 2; // J-type
            case "noop":
            case "halt":
                return 0; // O-type
            case ".fill":
                return 1; // Fill has 1 field
            default:
                return -1; // Unknown instruction
        }
    }

    public void printTokens() {
        for (String token : tokens) {
            System.out.println(token);
        }
    }
}

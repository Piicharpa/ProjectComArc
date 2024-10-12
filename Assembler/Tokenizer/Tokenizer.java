package Assembler.Tokenizer;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Tokenizer {
    
    private List<String> tokens;
    private int currentTokenIndex;

    /**
     * Constructor for the Tokenizer class.
     * Initializes the tokens list and sets the current token index to zero.
     */
    public Tokenizer() {
        tokens = new ArrayList<>();
        currentTokenIndex = 0;
    }

    /**
     * Tokenizes a line of assembly code.
     * Clears any existing tokens and processes the new line.
     *
     * @param line the line of assembly code to tokenize
     */
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

    /**
     * Retrieves the next token from the list of tokens.
     *
     * @return the next token if available, otherwise null
     */
    public String getNextToken() {
        if (hasMoreTokens()) {
            return tokens.get(currentTokenIndex++);
        }
        return null; 
    }

    /**
     * Retrieves the next token in the list without advancing the current token index.
     *
     * @return the next token if available, otherwise null
     */
    public String peekNextToken() {
        if (hasMoreTokens()) {
            int next = currentTokenIndex + 1;
            return tokens.get(next);
        }
        return null; 
    }

    /**
     * Checks if there are more tokens to process.
     *
     * @return true if there are tokens remaining, otherwise false
     */
    public boolean hasMoreTokens() {
        return currentTokenIndex < tokens.size();
    }

    /**
     * Resets the tokenizer to the beginning of the token list.
     * This sets the current token index to 0, so tokens can be reprocessed from the start.
     */
    public void resetTokenizer() {
        currentTokenIndex = 0;
    }


    /**            
     * This function takes an assembly code line and removes any comment after the instruction
     * and its arguments, based on the argument count of the instruction.
     *
     * @param line the line of assembly code
     * @return the line of code without any comments
     */
    private String removeComment(String line) {
        // Split line into parts to determine instruction type
        String[] parts = line.split("\\s+");

        // Prepare cleaned line that contains instruction and its fields
        StringBuilder cleanedLine = new StringBuilder();

        // Check if there's a label
        int index = 0;
        if (!isOpcode(parts[0])) {
            // Add label
            cleanedLine.append(parts[0]);
            // Skip to inst
            index++; 
        }

        // Check if there"s  an instruction
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


    /**
     * Checks if the given token is a valid opcode in the assembly language.
     *
     * @param token the string to check if it is a valid opcode
     * @return true if the token matches one of the valid opcodes ("add", "nand", "lw", "sw", 
     *         "beq", "jalr", "halt", or "noop"), otherwise false
     */
    private static boolean isOpcode(String token) {
        return token.equals("add") || token.equals("nand") || token.equals("lw") || token.equals("sw") || token.equals("beq") || token.equals("jalr") || token.equals("halt") || token.equals("noop");
    }

    /**
     * Gets the number of arguments required based on the instruction type.
     *
     * @param instruction the instruction whose argument count is to be determined
     * @return the number of arguments required for the given instruction
     *         - 3 for R and I-type instructions (e.g., "add", "nand", "lw", "sw", "beq")
     *         - 2 for J-type instructions (e.g., "jalr")
     *         - 0 for O-type instructions (e.g., "noop", "halt")
     *         - 1 for ".fill" instruction
     *         - -1 for unknown instructions
     */
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


    /**
     * Prints all tokens that have been tokenized from the input line.
     */
    public void printTokens() {
        for (String token : tokens) {
            System.out.println(token);
        }
    }
}
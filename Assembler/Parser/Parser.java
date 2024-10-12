package Assembler.Parser;

import Assembler.Evaluator.EvalException;
import Assembler.Tokenizer.Tokenizer;

public class Parser {
    private Tokenizer tokenizer;

    /**
     * Constructor for the Parser class.
     * Initializes a new Tokenizer instance to process lines of assembly code.
     */
    public Parser() {
        this.tokenizer = new Tokenizer();
    }

    /**
     * Parses a single line of assembly code and returns a ParsedLine object.
     *
     * @param line the line of assembly code to be parsed
     * @param address the memory address corresponding to the line
     * @return a ParsedLine object containing the parsed components (label, instruction, arguments)
     * @throws Exception if an unknown instruction is encountered
     */
    public ParsedLine parseLine(String line, int address) throws Exception {
        tokenizer.tokenizeLine(line); // Tokenize the line first
        ParsedLine parsedLine = new ParsedLine(address); 
        
        if (tokenizer.hasMoreTokens()) {
            String current = tokenizer.getNextToken();
            
            // Check if the token is an instruction opcode
            if (isOpcode(current)) {
                parsedLine.setInstruction(current);
            } else { 
                // Handle labels and symbolic addresses
                if(tokenizer.hasMoreTokens()) {
                    String nextToken = tokenizer.getNextToken();
                    if (isOpcode(nextToken)) { 
                        // If the next token is an opcode, set the current token as a label
                        parsedLine.setLabel(current);
                        parsedLine.setInstruction(nextToken);
                    } else if (current.equals(".fill")) { 
                        parsedLine.setInstruction(current);
                    } else if (nextToken.equals(".fill")) { 
                        // Handle symbolic addresses with the .fill directive
                        parsedLine.setSymbolic(current);
                        parsedLine.setInstruction(nextToken);
                    } else { 
                        // Throw an exception if the instruction is invalid
                        throw new EvalException.UnknownInstruction(current);
                    }
                } else { 
                    // If there are mo more tokens, assume the instruction is invalid
                    throw new EvalException.UnknownInstruction(current);
                }
            } 
        }

        // Add arguments to the instruction (if any)
        while (tokenizer.hasMoreTokens()) {
            parsedLine.addArgument(tokenizer.getNextToken());
        }

        return parsedLine;
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

}
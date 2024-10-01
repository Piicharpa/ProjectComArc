package Assembler.Parser;

import Assembler.Tokenizer.Tokenizer;

public class Parser {
    private Tokenizer tokenizer;

    public Parser() {
        this.tokenizer = new Tokenizer();
    }

    // Parse each line of assembly code
    public ParsedLine parseLine(String line, int address) throws Exception {
        tokenizer.tokenizeLine(line); // Tokenize the line first
        ParsedLine parsedLine = new ParsedLine(address); // Create a new ParsedLine object
        
        if (tokenizer.hasMoreTokens()) {
            String token = tokenizer.getNextToken();
            String current = null;
            // Instruction
            if (isOpcode(token)) {
                parsedLine.setInstruction(token);
            }else{
                current = token;
                token = tokenizer.getNextToken();
                
                // Symbolic
                if(token.equals(".fill")) {
                    parsedLine.setSymbolic(current);
                }else{
                // Label
                    parsedLine.setLabel(current);
                }
                parsedLine.setInstruction(token);
            }
        }

        // Add arguments to the instruction (if any)
        while (tokenizer.hasMoreTokens()) {
            parsedLine.addArgument(tokenizer.getNextToken());
        }

        return parsedLine;
    }

    private static boolean isOpcode(String token) {
        return token.equals("add") || token.equals("nand") || token.equals("lw") || token.equals("sw") || token.equals("beq") || token.equals("jalr") || token.equals("halt") || token.equals("noop");
    }

}

package Assembler.Parser;

import Assembler.Evaluator.EvalException;
import Assembler.Tokenizer.Tokenizer;

public class Parser {
    private Tokenizer tokenizer;

    public Parser() {
        this.tokenizer = new Tokenizer();
    }

    // Parse each line of assembly code
    public ParsedLine parseLine(String line, int address) throws Exception {
        tokenizer.tokenizeLine(line); // Tokenize the line first
        ParsedLine parsedLine = new ParsedLine(address); 
        
        if (tokenizer.hasMoreTokens()) {
            String current = tokenizer.getNextToken();
            String next = null;
            // Instruction
            if (isOpcode(current)) {
                parsedLine.setInstruction(current);
            }else{
                next = tokenizer.getNextToken();
                if(next.equals(".fill")) {
                    // Symbolic
                    parsedLine.setSymbolic(current);
                // } else if (Character.isDigit(token.charAt(0))) {
                //     // Unknown Ins
                //     throw new EvalException.unknownIns(current);
                } else {
                    // Label
                    parsedLine.setLabel(current);    
                }
                parsedLine.setInstruction(current);
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

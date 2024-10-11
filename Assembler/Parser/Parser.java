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
            // String next = null;
            
            // Instruction
            if (isOpcode(current)) {
                parsedLine.setInstruction(current);
            } else { // Symbolic, label, wrong instruction
                if(tokenizer.hasMoreTokens()) {
                    String nextToken = tokenizer.getNextToken();
                    if (isOpcode(nextToken)) { // Label + inst
                        parsedLine.setLabel(current);
                        parsedLine.setInstruction(nextToken);
                    } else if (nextToken.equals(".fill")) { // Symbolic + .fill
                        parsedLine.setSymbolic(current);
                        parsedLine.setInstruction(nextToken);
                    } else { // wrong inst
                        throw new EvalException.UnknownInstruction(current);
                    }
                } else { // Get null (wrong inst by remove comment)
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

    private static boolean isOpcode(String token) {
        return token.equals("add") || token.equals("nand") || token.equals("lw") || token.equals("sw") || token.equals("beq") || token.equals("jalr") || token.equals("halt") || token.equals("noop");
    }

}
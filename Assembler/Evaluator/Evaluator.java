package Assembler.Evaluator;

import Assembler.Parser.ParsedLine;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Evaluator {
    // A symbol table to store labels and their values
    private Map<String, Integer> symbolValueMap; 
    // A table to store labels and their corresponding addresses
    private Map<String, Integer> labelAddrMap; 
    // A table to store symbols and their corresponding addresses
    private Map<String, Integer> symbolAddrMap;  

    /**
     * Constructor for the Evaluator class.
     * Initializes the maps used for storing symbol values, label addresses, and symbol addresses.
     */
    public Evaluator() {
        this.symbolValueMap = new HashMap<>();
        this.labelAddrMap = new HashMap<>();
        this.symbolAddrMap = new HashMap<>();
    }

    /**
     * Evaluates a list of parsed lines from assembly code.
     * This method builds the symbolValueMap with labels and .fill values, 
     * then generates machine code for each instruction.
     * 
     * @param parsedLines A list of parsed lines representing assembly code.
     */
    public void evaluate(List<ParsedLine> parsedLines) {
        for (ParsedLine line : parsedLines) {
            // Store labels and their addresses
            if (line.getLabel() != null) {
                if (line.getLabel().length() > 6) throw new EvalException.SixCharacters(line.getLabel()); 
                else if (!Character.isLetter(line.getLabel().charAt(0))) throw new EvalException.FirstCharacter(line.getLabel()); 
                else if (labelAddrMap.containsKey(line.getLabel())) throw new EvalException.SameLabel(line.getLabel());
                else {
                    labelAddrMap.put(line.getLabel(), line.getAddress());
                }
            }
        }

        // Process '.fill' instructions to resolve symbolic values
        for (ParsedLine line : parsedLines) {
             // Handle .fill (symbolic) 
            if (line.getInstruction() != null && line.getInstruction().equals(".fill")) {
                if (!line.getArguments().isEmpty()) {
                    String value = line.getArguments().get(0);
                    if (labelAddrMap.containsKey(value)) {
                        // If the value refers to a label
                        symbolValueMap.put(line.getSymbolic(), labelAddrMap.get(value));
                    } else if (symbolValueMap.containsKey(value)) { 
                        // If the value refers to another symbolic value
                        symbolValueMap.put(line.getSymbolic(), symbolValueMap.get(value));
                    } else if (isNumber(value)) {
                        // If the value is a number
                        symbolValueMap.put(line.getSymbolic(), Integer.parseInt(value));
                    } else {
                        throw new EvalException.Undefined(value);
                    }
                    symbolAddrMap.put(line.getSymbolic(), line.getAddress());
                }
            }
        }

        // Start evaluate instructions
        for (ParsedLine line : parsedLines) {
            evaluateLine(line);
        }
    }


    /**
     * Evaluates a single parsed line and generates machine code.
     * 
     * @param line The parsed line representing an assembly instruction.
     */
    private void evaluateLine(ParsedLine line) {
        String instruction = line.getInstruction();

        if (instruction == null) {
            return; // Skip empty or label-only lines
        }

        switch (instruction) {
            case "add":
                evaluateRType(line, "000");
                break;
            case "nand":
                evaluateRType(line, "001");
                break;
            case "lw":
                evaluateIType(line, "010");
                break;
            case "sw":
                evaluateIType(line, "011");
                break;
            case "beq":
                evaluateIType(line, "100");
                break;
            case "jalr":
                evaluateJType(line, "101");
                break;
            case "halt": 
                String binaryCode = "1100000000000000000000000";
                int decimalCode = Integer.parseInt(binaryCode, 2);
                // System.out.println("Machine code for " + line.getInstruction() + ": " + binaryCode + " > " + decimalCode);
                System.out.println(decimalCode);
                break;
            case "noop": 
                binaryCode = "1110000000000000000000000";
                decimalCode = Integer.parseInt(binaryCode, 2);
                // System.out.println("Machine code for " + line.getInstruction() + ": " + binaryCode + " > " + decimalCode);
                System.out.println(decimalCode);
                break;
            case ".fill":
                Integer value = symbolValueMap.get(line.getSymbolic());
                if (value != null) {
                    System.out.println(value);
                } else {
                    System.out.println(0); // Output 0 if the value is null
                }
                break;
            default:
                throw new EvalException.UnknownInstruction(line.getInstruction());
        }
    }

    /**
     * Evaluates R-Type instructions (add, nand) and generates corresponding machine code.
     * 
     * @param line   The parsed line representing the R-Type instruction.
     * @param opcode The opcode associated with the R-Type instruction.
     */
    private void evaluateRType(ParsedLine line, String opcode) {
        List<String> args = line.getArguments();
        if (args.size() != 3) {
            System.err.println("Error: add instruction requires 3 arguments");
            return;
        }
        int regA = Integer.parseInt(args.get(0));
        int regB = Integer.parseInt(args.get(1));
        int destReg = Integer.parseInt(args.get(2));

        String binaryCode = generateRTypeCode(opcode, regA, regB, destReg);
        int decimalCode = Integer.parseInt(binaryCode, 2);
        // System.out.println("Machine code for " + line.getInstruction() + ": " + binaryCode + " > " + decimalCode);
        System.out.println(decimalCode);
    }

    /**
     * Evaluates I-Type instructions (lw, sw, beq) and generates corresponding machine code.
     * 
     * @param line   The parsed line representing the I-Type instruction.
     * @param opcode The opcode associated with the I-Type instruction.
     */
    private void evaluateIType(ParsedLine line, String opcode) {
        List<String> args = line.getArguments();
        if (args.size() != 3) {
            System.err.println("Error: " + line.getInstruction() + " instruction requires 3 arguments");
            return;
        }

        int regA = Integer.parseInt(args.get(0));
        int regB = Integer.parseInt(args.get(1));
        int offset;

        if (labelAddrMap.containsKey(args.get(2))) { // If it is label
            if (line.getInstruction().equals("beq")) { // For beq
                int targetAddress = labelAddrMap.get(args.get(2));
                offset = targetAddress - (line.getAddress() + 1); // PC + 1 + offset
            } else {
                offset = labelAddrMap.get(args.get(2)); // For lw & sw
            }
        } else if (symbolValueMap.containsKey(args.get(2))) { // If it is symbolic
            offset = symbolAddrMap.get(args.get(2));
        } else if (isNumber(args.get(2))) {
            offset = Integer.parseInt(args.get(2)); // If not, treat as numeric
        } else {
            throw new EvalException.offsetNotFound(args.get(2));
        }

        if (offset < -32768 || offset > 32767) throw new EvalException.Offset(offset);


        String binaryCode = generateITypeCode(opcode, regA, regB, offset);
        int decimalCode = Integer.parseInt(binaryCode, 2);
        // System.out.println("Machine code for " + line.getInstruction() + ": " + binaryCode + " > " + decimalCode);
        System.out.println(decimalCode);
    }


    /**
     * Evaluates J-Type instructions (jalr) and generates corresponding machine code.
     * 
     * @param line   The parsed line representing the J-Type instruction.
     * @param opcode The opcode associated with the J-Type instruction.
     */
    private void evaluateJType(ParsedLine line, String opcode) {
        List<String> args = line.getArguments();
        if (args.size() != 2) {
            System.err.println("Error: jalr instruction requires 2 arguments");
            return;
        }
        int regA = Integer.parseInt(args.get(0));
        int regB = Integer.parseInt(args.get(1));

        String binaryCode = generateJTypeCode(opcode, regA, regB);
        int decimalCode = Integer.parseInt(binaryCode, 2);
        // System.out.println("Machine code for " + line.getInstruction() + ": " + binaryCode + " > " + decimalCode);
        System.out.println(decimalCode);
    }

     /**
     * Generates binary code for R-Type instructions.
     * 
     * @param opcode  The opcode for the R-Type instruction.
     * @param regA    The first register operand.
     * @param regB    The second register operand.
     * @param destReg The destination register.
     * @return The generated binary code as a String.
     */
    public String generateRTypeCode(String opcode, int regA, int regB, int destReg) {
        // R-type: Opcode (3 bits), regA (3 bits), regB (3 bits), unused bits (12 bits), destReg (3 bits)
        String regABinary = String.format("%3s", Integer.toBinaryString(regA)).replace(' ', '0');
        String regBBinary = String.format("%3s", Integer.toBinaryString(regB)).replace(' ', '0');
        String destRegBinary = String.format("%3s", Integer.toBinaryString(destReg)).replace(' ', '0');
        return String.format("%s%s%s%013d%s", opcode, regABinary, regBBinary, 0, destRegBinary);
    }

    /**
     * Generates binary code for I-Type instructions (lw, sw, beq).
     * 
     * @param opcode  The opcode for the I-Type instruction.
     * @param regA    The first register operand.
     * @param regB    The second register operand.
     * @param offset  The offset value for the instruction.
     * @return The generated binary code as a String.
     */
    public String generateITypeCode(String opcode, int regA, int regB, int offset) {
        // // Ensure that the offset is within the 16-bit two's complement range
        // if (offset < -32768 || offset > 32767) {
        //     throw new IllegalArgumentException("Offset out of 16-bit range: " + offset);
        // }

        // I-type: Opcode (3 bits), regA (3 bits), regB (3 bits), offset (16 bits)
        String regABinary = String.format("%3s", Integer.toBinaryString(regA)).replace(' ', '0');
        String regBBinary = String.format("%3s", Integer.toBinaryString(regB)).replace(' ', '0');
        String offsetBinary = String.format("%16s", Integer.toBinaryString(offset & 0xFFFF)).replace(' ', '0');
        return String.format("%s%s%s%s", opcode, regABinary, regBBinary, offsetBinary);
    }

    /**
     * Generates binary code for J-Type instructions (jalr).
     * 
     * @param opcode  The opcode for the J-Type instruction.
     * @param regA    The first register operand.
     * @param regB    The second register operand.
     * @return The generated binary code as a String.
     */
    public String generateJTypeCode(String opcode, int regA, int regB) {
        // J-type: Opcode (3 bits), regA (3 bits), regB (3 bits), unused bits (16 bits)
        String regABinary = String.format("%3s", Integer.toBinaryString(regA)).replace(' ', '0');
        String regBBinary = String.format("%3s", Integer.toBinaryString(regB)).replace(' ', '0');
        return String.format("%s%s%s%016d", opcode, regABinary, regBBinary, 0);
    }

    /**
     * Checks if a given string is a valid number.
     * 
     * @param str The string to check.
     * @return True if the string is a valid number, otherwise false.
     */
    public static boolean isNumber(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }

        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
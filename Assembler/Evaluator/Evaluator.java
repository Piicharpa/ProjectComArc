package Assembler.Evaluator;

import Assembler.Parser.ParsedLine;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Evaluator {
    private Map<String, Integer> symbolTable; // A symbol table to store labels and their value
    private Map<String, Integer> labelTable; // A label and address
    private Map<String, Integer> addressTable; // A symbol andd address

    public Evaluator() {
        this.symbolTable = new HashMap<>();
        this.labelTable = new HashMap<>();
        this.addressTable = new HashMap<>();
    }

    // First pass: Build the symbol table with labels and .fill values
    public void evaluate(List<ParsedLine> parsedLines) {
        for (ParsedLine line : parsedLines) {
            // Store labels and their addresses
            if (line.getLabel() != null) {
                if (line.getLabel().length() > 6) throw new EvalException.SixCharacters(line.getLabel()); 
                else if (!Character.isLetter(line.getLabel().charAt(0))) throw new EvalException.InvalidLabelFormat(line.getLabel()); 
                else if (labelTable.containsKey(line.getLabel())) throw new EvalException.SameLabel(line.getLabel());
                else {
                    labelTable.put(line.getLabel(), line.getAddress());
                    // System.out.println("label: " + line.getLabel() + " address: " + labelTable.get(line.getLabel()));
                }
            }
        }

        for (ParsedLine line : parsedLines) {
             // Handle .fill (sysmbolic) 
            if (line.getInstruction() != null && line.getInstruction().equals(".fill")) {
                // System.out.println(line.toString());
                if (!line.getArguments().isEmpty()) {
                    String value = line.getArguments().get(0);
                    // System.out.println("val " + value);

                    if (labelTable.containsKey(value)) {
                        // Handle symbolic address (e.g., start)
                        symbolTable.put(line.getSymbolic(), labelTable.get(value));
                    } else if (symbolTable.containsKey(value)) { 
                        // Handle symbolic .fill symbolic
                        symbolTable.put(line.getSymbolic(), symbolTable.get(value));
                    } else if (isNumber(value)) {
                        // Handle numeric value
                        symbolTable.put(line.getSymbolic(), Integer.parseInt(value));
                    } else {
                        throw new EvalException.Undefined(value);
                    }

                    addressTable.put(line.getSymbolic(), line.getAddress());
                    // System.out.println("sym: " + line.getSymbolic() + " address: " + addressTable.get(line.getSymbolic()));
                }
            }
        }

        // Start evaluate instructions
        for (ParsedLine line : parsedLines) {
            evaluateLine(line);
        }
    }


    // Evaluate a single parsed line and generate machine code
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
            case "halt": // Handle halt instruction
                String binaryCode = "1100000000000000000000000";
                int decimalCode = Integer.parseInt(binaryCode, 2);
                // System.out.println("Machine code for " + line.getInstruction() + ": " + binaryCode + " > " + decimalCode);
                System.out.println(decimalCode);
                break;
            case "noop": // Handle noop (no operation)
                binaryCode = "1110000000000000000000000";
                decimalCode = Integer.parseInt(binaryCode, 2);
                // System.out.println("Machine code for " + line.getInstruction() + ": " + binaryCode + " > " + decimalCode);
                System.out.println(decimalCode);
                break;
            case ".fill":
                System.out.println(symbolTable.get(line.getSymbolic()));
                break;
            default:
                throw new EvalException.UnknownInstruction(line.getInstruction());
        }
    }

    // Evaluate R-Type instruction (add, nand)
    private void evaluateRType(ParsedLine line, String opcode) {
        List<String> args = line.getArguments();
        if (args.size() != 3) {
            System.out.println("Error: add instruction requires 3 arguments");
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

    // Evaluate I-Type instruction (lw, sw, beq)
    private void evaluateIType(ParsedLine line, String opcode) {
        List<String> args = line.getArguments();
        if (args.size() != 3) {
            System.out.println("Error: " + line.getInstruction() + " instruction requires 3 arguments");
            return;
        }

        int regA = Integer.parseInt(args.get(0));
        int regB = Integer.parseInt(args.get(1));
        int offset;

        if (labelTable.containsKey(args.get(2))) { // If it is label
            if (line.getInstruction().equals("beq")) { // For beq
                int targetAddress = labelTable.get(args.get(2));
                offset = targetAddress - (line.getAddress() + 1); // PC + 1 + offset
            } else {
                offset = labelTable.get(args.get(2)); // For lw & sw
            }
        } else if (symbolTable.containsKey(args.get(2))) { // If it is symbolic
            offset = addressTable.get(args.get(2));
        } else if (isNumber(args.get(2))) {
            offset = Integer.parseInt(args.get(2)); // If not, treat as numeric
        } else {
            throw new EvalException.Undefined(args.get(2));
        }

        if (!(offset >=  -32768 && offset <=  32768))  throw new EvalException.Offset(offset);


        String binaryCode = generateITypeCode(opcode, regA, regB, offset);
        int decimalCode = Integer.parseInt(binaryCode, 2);
        // System.out.println("Machine code for " + line.getInstruction() + ": " + binaryCode + " > " + decimalCode);
        System.out.println(decimalCode);
    }


    // Evaluate J-Type instruction (jalr)
    private void evaluateJType(ParsedLine line, String opcode) {
        List<String> args = line.getArguments();
        if (args.size() != 2) {
            System.out.println("Error: jalr instruction requires 2 arguments");
            return;
        }
        int regA = Integer.parseInt(args.get(0));
        int regB = Integer.parseInt(args.get(1));

        String binaryCode = generateJTypeCode(opcode, regA, regB);
        int decimalCode = Integer.parseInt(binaryCode, 2);
        // System.out.println("Machine code for " + line.getInstruction() + ": " + binaryCode + " > " + decimalCode);
        System.out.println(decimalCode);
    }

    public String generateRTypeCode(String opcode, int regA, int regB, int destReg) {
        // R-type: Opcode (3 bits), regA (3 bits), regB (3 bits), unused bits (12 bits), destReg (3 bits)
        String regABinary = String.format("%3s", Integer.toBinaryString(regA)).replace(' ', '0');
        String regBBinary = String.format("%3s", Integer.toBinaryString(regB)).replace(' ', '0');
        String destRegBinary = String.format("%3s", Integer.toBinaryString(destReg)).replace(' ', '0');
        return String.format("%s%s%s%013d%s", opcode, regABinary, regBBinary, 0, destRegBinary);
    }

    // Generate binary code for I-type instruction (lw, sw, beq)
    public String generateITypeCode(String opcode, int regA, int regB, int offset) {
        // Ensure that the offset is within the 16-bit two's complement range
        if (offset < -32768 || offset > 32767) {
            throw new IllegalArgumentException("Offset out of 16-bit range: " + offset);
        }

        // I-type: Opcode (3 bits), regA (3 bits), regB (3 bits), offset (16 bits)
        String regABinary = String.format("%3s", Integer.toBinaryString(regA)).replace(' ', '0');
        String regBBinary = String.format("%3s", Integer.toBinaryString(regB)).replace(' ', '0');
        String offsetBinary = String.format("%16s", Integer.toBinaryString(offset & 0xFFFF)).replace(' ', '0');
        return String.format("%s%s%s%s", opcode, regABinary, regBBinary, offsetBinary);
    }

    // Generate binary code for J-type instruction (jalr)
    public String generateJTypeCode(String opcode, int regA, int regB) {
        // J-type: Opcode (3 bits), regA (3 bits), regB (3 bits), unused bits (16 bits)
        String regABinary = String.format("%3s", Integer.toBinaryString(regA)).replace(' ', '0');
        String regBBinary = String.format("%3s", Integer.toBinaryString(regB)).replace(' ', '0');
        return String.format("%s%s%s%016d", opcode, regABinary, regBBinary, 0);
    }

    // Generate binary code for O-type instruction (halt, noop)
    public String generateOTypeCode(String opcode) {
        // O-type: Opcode (3 bits), unused bits (22 bits)
        return String.format("%s%022d", opcode, 0);
    }

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
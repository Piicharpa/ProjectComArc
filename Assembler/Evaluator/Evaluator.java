package Assembler.Evaluator;

import Assembler.Parser.ParsedLine;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Evaluator {
    private Map<String, Integer> symbolTable; // A symbol table to store labels and their addresses
    private Map<String, Integer> labelTable;
    private Map<String, Integer> addressTable;

    public Evaluator() {
        // this.temporaryFillValues = new HashMap<>();
        this.symbolTable = new HashMap<>();
        this.labelTable = new HashMap<>();
        this.addressTable = new HashMap<>();
    }

    // First pass: Build the symbol table with labels and .fill values
    // @SuppressWarnings("unlikely-arg-type")
    public void firstPass(List<ParsedLine> parsedLines) {
        for (ParsedLine line : parsedLines) {
            // Store labels and their addresses
            if (line.getLabel() != null) {
                labelTable.put(line.getLabel(), line.getAddress());
                System.out.println("label: " + line.getLabel() + " address: " + labelTable.get(line.getLabel()));
            }

            // Handle .fill directives
            if (line.getInstruction() != null && line.getInstruction().equals(".fill")) {
                if (!line.getArguments().isEmpty()) {
                    String value = line.getArguments().get(0);
                    // Try to parse the value as a number
                    if (labelTable.containsKey(value)) {
                        // Map (symbolic, address from labelTable) Ex. start
                        symbolTable.put(line.getSymbolic(), labelTable.get(value));
                    } else {
                        // Ex. "5", "-1"
                        symbolTable.put(line.getSymbolic(), Integer.parseInt(value));
                    }

                    addressTable.put(line.getSymbolic(), line.getAddress());
                    System.out.println("sym: " + line.getSymbolic() + " address: " + addressTable.get(line.getSymbolic()));
                    // try {
                    // // Numeric
                    // symbolTable.put(line.getLabel(), Integer.parseInt(value));
                    // } catch (NumberFormatException e) {

                    // symbolTable.put(line.getLabel(), value);
                    // }
                }
            }

            // currentAddress++; // Increment the address for the next instruction
        }
    }

    // Second pass: Evaluate each line using the symbol table to generate machine
    // code
    public void secondPass(List<ParsedLine> parsedLines) {
        for (ParsedLine line : parsedLines) {
            // Evaluate instructions
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
                System.out.println("Machine code for "+ "110" + "0000000000000000000000");
                break;
            case "noop": // Handle noop (no operation)
                System.out.println("Machine code for " + "111" + "0000000000000000000000");
                break;
            default:
                System.out.println("Unknown instruction: " + instruction);
                break;
        }
        // System.out.println(line + " " + line.getAddress());
    }

    // Evaluate R-Type instruction
    private void evaluateRType(ParsedLine line, String opcode) {
        List<String> args = line.getArguments();
        if (args.size() != 3) {
            System.out.println("Error: add instruction requires 3 arguments");
            return;
        }
        int regA = Integer.parseInt(args.get(0));
        int regB = Integer.parseInt(args.get(1));
        int destReg = Integer.parseInt(args.get(2));

        System.out.println("Machine code for " + line.getInstruction() + ": " + generateRTypeCode(opcode, regA, regB, destReg));
    }

    // Evaluate I-Type instruction
    private void evaluateIType(ParsedLine line, String opcode) {
        List<String> args = line.getArguments();
        if (args.size() != 3) {
            System.out.println("Error: lw instruction requires 3 arguments");
            return;
        }
        int regA = Integer.parseInt(args.get(0));
        int regB = Integer.parseInt(args.get(1));
        // int offset = addressTable.get(args.get(2));

        int offset;
        if (symbolTable.containsKey(args.get(2))) {
            offset = addressTable.get(args.get(2));
        } else {
            // System.out.println(offset);
            offset = Integer.parseInt(args.get(2)); // If not a symbolic address, use numeric
        }

        System.out.println("Machine code for " + line.getInstruction() + ": " + generateITypeCode(opcode, regA, regB, offset));
    }

    // Evaluate J-Type instruction
    private void evaluateJType(ParsedLine line, String opcode) {
        List<String> args = line.getArguments();
        if (args.size() != 2) {
            System.out.println("Error: jalr instruction requires 2 arguments");
            return;
        }
        int regA = Integer.parseInt(args.get(0));
        int regB = Integer.parseInt(args.get(1));

        System.out.println("Machine code for jalr: " + generateJTypeCode(opcode, regA, regB));
    }

    // // Handle .fill directive in the second pass
    // private void evaluateFill(ParsedLine line) {
    //     String value = line.getArguments().get(0);

    //     int numericValue;
    //     if (symbolTable.containsKey(value)) {
    //         // If it's a label, use the address from the symbolTable
    //         numericValue = symbolTable.get(value);
    //     } else {
    //         // Try to parse the value as a number
    //         try {
    //             numericValue = Integer.parseInt(value);
    //         } catch (NumberFormatException e) {
    //             System.out.println("Error: Invalid .fill argument " + value);
    //             return;
    //         }
    //     }

    //     System.out.println("Machine code for .fill: " + numericValue);
    // }

    // Generate binary code for R-type instruction (add, nand)
    // public String generateRTypeCode(String opcode, int regA, int regB, int destReg) {
    //     // R-type: Opcode (3 bits), regA (3 bits), regB (3 bits), unused bits (12 bits), destReg (3 bits)
    //     return String.format("%s %03d %03d %012d %03d", opcode, regA, regB, 0, destReg);
    // }    

    // // Generate binary code for I-type instruction (lw, sw, beq)
    // public String generateITypeCode(String opcode, int regA, int regB, int offset) {
    //     // Ensure that the offset is within the 16-bit two's complement range
    //     if (offset < -32768 || offset > 32767) {
    //         throw new IllegalArgumentException("Offset out of 16-bit range: " + offset);
    //     }

    //     // I-type: Opcode (3 bits), regA (3 bits), regB (3 bits), offset (16 bits)
    //     String offsetBinary = String.format("%16s", Integer.toBinaryString(offset & 0xFFFF)).replace(' ', '0');
    //     return String.format("%03s %03d %03d %s", opcode, regA, regB, offsetBinary);
    // }

    // // Generate binary code for J-type instruction (jalr)
    // public String generateJTypeCode(String opcode, int regA, int regB) {
    //     // J-type: Opcode (3 bits), regA (3 bits), regB (3 bits), unused bits (16 bits)
    //     return String.format("%s %03d %03d %016d", opcode, regA, regB, 0);
    // }    

    // // Generate binary code for O-type instruction (halt, noop)
    // public String generateOTypeCode(String opcode) {
    //     // O-type: Opcode (3 bits), unused bits (22 bits)
    //     return String.format("%03s %022d", opcode, 0);
    // }

public String generateRTypeCode(String opcode, int regA, int regB, int destReg) {
    // R-type: Opcode (3 bits), regA (3 bits), regB (3 bits), unused bits (12 bits), destReg (3 bits)
    String regABinary = String.format("%3s", Integer.toBinaryString(regA)).replace(' ', '0');
    String regBBinary = String.format("%3s", Integer.toBinaryString(regB)).replace(' ', '0');
    String destRegBinary = String.format("%3s", Integer.toBinaryString(destReg)).replace(' ', '0');
    return String.format("%s %s %s %012d %s", opcode, regABinary, regBBinary, 0, destRegBinary);
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
    return String.format("%s %s %s %s", opcode, regABinary, regBBinary, offsetBinary);
}

// Generate binary code for J-type instruction (jalr)
public String generateJTypeCode(String opcode, int regA, int regB) {
    // J-type: Opcode (3 bits), regA (3 bits), regB (3 bits), unused bits (16 bits)
    String regABinary = String.format("%3s", Integer.toBinaryString(regA)).replace(' ', '0');
    String regBBinary = String.format("%3s", Integer.toBinaryString(regB)).replace(' ', '0');
    return String.format("%s %s %s %016d", opcode, regABinary, regBBinary, 0);
}

// Generate binary code for O-type instruction (halt, noop)
public String generateOTypeCode(String opcode) {
    // O-type: Opcode (3 bits), unused bits (22 bits)
    return String.format("%s %022d", opcode, 0);
}


}

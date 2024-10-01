package Assembler.Parser;

import java.util.ArrayList;
import java.util.List;

public class ParsedLine {
    private String label;
    private String instruction;
    private String symbolic;
    private int address;
    private List<String> arguments;

    public ParsedLine(int address) {
        this.arguments = new ArrayList<>();
        this.address = address;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;  
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public String getInstruction() {
        return instruction;  
    }

    public void setSymbolic(String symbolic) {
        this.symbolic = symbolic;
    }

    public String getSymbolic() {
        return symbolic; 
    }
    public void setAddress(int address) {
        this.address = address;
    }

    public int getAddress() {
        return address; 
    }

    public void addArgument(String argument) {
        arguments.add(argument);
    }

    public List<String> getArguments() {
        return arguments; 
    } 

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (address != -1) {
            sb.append("address: ").append(address).append("\n");
        }
        // Check and append label
        if (label != null) {
            sb.append("Label: ").append(label).append("\n");
        }
        // // Check and append symbolic reference
        if (symbolic != null) {
            sb.append("Symbolic: ").append(symbolic).append("\n");
        }
        // Check and append instruction
        if (instruction != null) {
            sb.append("Instruction: ").append(instruction).append("\n");
        }
        // Append arguments if there are any
        if (!arguments.isEmpty()) {
            for (int i = 0; i < arguments.size(); i++) {
                sb.append("Argument ").append(i + 1).append(": ").append(arguments.get(i)).append("\n");
            }
        } else {
            // sb.append("No arguments.\n");
        }
        return sb.toString();
    }
}

package Assembler.Parser;

import java.util.ArrayList;
import java.util.List;

public class ParsedLine {
    private String label;
    private String instruction;
    private String symbolic;
    private List<String> arguments;

    public ParsedLine() {
        this.arguments = new ArrayList<>();
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;  // Getter for label
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public String getInstruction() {
        return instruction;  // Getter for instruction
    }

    public void setSymbolic(String symbolic) {
        this.symbolic = symbolic;
    }

    public void addArgument(String argument) {
        arguments.add(argument);
    }

    public List<String> getArguments() {
        return arguments;  // Getter for arguments
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
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

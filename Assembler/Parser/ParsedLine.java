package Assembler.Parser;

import java.util.ArrayList;
import java.util.List;

public class ParsedLine {
    private String label;
    private String instruction;
    private String symbolic;
    private int address;
    private List<String> arguments;

    /**
     * Constructor for ParsedLine.
     * Initializes the parsed line with the given address and an empty argument list.
     *
     * @param address the memory address associated with this line of code
     */
    public ParsedLine(int address) {
        this.arguments = new ArrayList<>();
        this.address = address;
    }

     /**
     * Sets the label for this parsed line.
     *
     * @param label the label to be set
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * Returns the label of this parsed line.
     *
     * @return the label as a String
     */
    public String getLabel() {
        return label;  
    }

    /**
     * Sets the instruction for this parsed line.
     *
     * @param instruction the instruction to be set
     */
    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    /**
     * Returns the instruction of this parsed line.
     *
     * @return the instruction as a String
     */
    public String getInstruction() {
        return instruction;  
    }

    /**
     * Sets the symbolic reference for this parsed line.
     *
     * @param symbolic the symbolic reference to be set
     */
    public void setSymbolic(String symbolic) {
        this.symbolic = symbolic;
    }

    /**
     * Returns the symbolic reference of this parsed line.
     *
     * @return the symbolic reference as a String
     */
    public String getSymbolic() {
        return symbolic; 
    }

    /**
     * Sets the address for this parsed line.
     *
     * @param address the memory address to be set
     */
    public void setAddress(int address) {
        this.address = address;
    }

    /**
     * Returns the memory address of this parsed line.
     *
     * @return the address as an int
     */
    public int getAddress() {
        return address; 
    }

    /**
     * Adds an argument to the argument list for this parsed line.
     *
     * @param argument the argument to be added
     */
    public void addArgument(String argument) {
        arguments.add(argument);
    }

    /**
     * Returns the list of arguments for this parsed line.
     *
     * @return a List of arguments
     */
    public List<String> getArguments() {
        return arguments; 
    } 

    /**
     * Generates a string representation of this parsed line, displaying the address,
     * label, symbolic reference, instruction, and arguments (if any).
     *
     * @return the string representation of the parsed line
     */
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
        // Check and append symbolic reference
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
package Assembler;

public class Main {

    public static void main(String[] args) {
        AssemblerProcessor processor = new AssemblerProcessor();
        // System.out.println("Hello World");

        String inputFilePath = "Combination/combination.asm";
        // String inputFilePath = "Assembler/File/Aj.txt";
        // String inputFilePath = "Assembler/File/Pcombine.txt";
        // String inputFilePath = "Assembler/File/FiboAu.txt";

        // Output file path
        String outputFilePath = "Assembler/File/output.txt";

        processor.process(inputFilePath, outputFilePath);
    }
}
package Assembler;

public class Main {

    public static void main(String[] args) {
        AssemblerProcessor processor = new AssemblerProcessor();

        // Input file path
        String inputFilePath = "Combination/combination.asm";

        // Output file path
        String outputFilePath = "Assembler/File/output.txt";

        processor.process(inputFilePath, outputFilePath);
    }
}
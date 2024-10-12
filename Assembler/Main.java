package Assembler;

public class Main {

    public static void main(String[] args) {
        AssemblerProcessor processor = new AssemblerProcessor();
        // System.out.println("Hello World");
        // Input file path
        String inputFilePath = "Assembler/File/test1.txt";
        // String inputFilePath = "Assembler/File/test2.txt";

        // Output file path
        String outputFilePath = "Assembler/File/output.txt";

        processor.process(inputFilePath, outputFilePath);
    }
}

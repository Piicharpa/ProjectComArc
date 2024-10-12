package Assembler;

import Assembler.Parser.Parser;
import Assembler.Parser.ParsedLine;
import Assembler.Evaluator.Evaluator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class AssemblerProcessor {

    /**
     * Processes the assembly input file and outputs the result to a specified file.
     * 
     * This method reads assembly code from the provided input file, parses each line,
     * evaluates the parsed lines, and writes the output to the output file. 
     * It temporarily redirects the standard output (System.out) to write to the output file,
     * and upon completion or error, it restores the original System.out.
     *
     * @param inputFilePath  the path to the input assembly file
     * @param outputFilePath the path to the output file where results will be written
     */
    public void process(String inputFilePath, String outputFilePath) {
        try {
            PrintStream fileOut = new PrintStream(outputFilePath);
            PrintStream originalOut = System.out;

            BufferedReader reader = new BufferedReader(new FileReader(inputFilePath));
            String line;
            List<ParsedLine> parsedLines = new ArrayList<>();
            Parser parser = new Parser();
            int address = 0;

            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    ParsedLine parsedLine = parser.parseLine(line, address); // Parse each line
                    parsedLines.add(parsedLine); // Add parsed line to the list
                    address++;
                }
            }
            reader.close();

            Evaluator evaluator = new Evaluator();

            System.setOut(fileOut);
            evaluator.evaluate(parsedLines); 
            
            fileOut.close();
            
    
            System.setOut(originalOut);
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println("Program exiting with status: 0");
            System.exit(0);

        } catch (Exception e) {
            System.err.println();
            System.err.println();
            System.err.println();
            System.err.println("Error: " + e.getMessage());
            System.err.println("Program exiting with status: 1");
            System.exit(1);
        }
    }
}

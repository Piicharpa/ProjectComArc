package Assembler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import Assembler.Tokenizer.Tokenizer;

public class Main {

    public static void main(String[] args) {
        // File path to the assembly code file
        String filePath = "Assembler/File/test1"; // Replace with actual file path

        Tokenizer tokenizer = new Tokenizer();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int lineNumber = -1; // Track line number for error reporting

            while ((line = reader.readLine()) != null) {
                lineNumber++;

                // Skip empty lines 
                if (line.trim().isEmpty()) {
                    continue;
                }

                // Tokenize the line
                tokenizer.tokenizeLine(line);

                // Process tokens
                System.out.println("Line " + lineNumber + ":");
                while (tokenizer.hasMoreTokens()) {
                    String token = tokenizer.getNextToken();
                    System.out.println("  Token: " + token);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }

}
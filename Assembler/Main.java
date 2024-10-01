package Assembler;

import Assembler.Parser.Parser;
import Assembler.Parser.ParsedLine;
import Assembler.Evaluator.Evaluator;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        try {
            String filePath = "Assembler/File/test1";
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            List<ParsedLine> parsedLines = new ArrayList<>();
            // Tokenizer tokenizer = new Tokenizer();  
            Parser parser = new Parser(); 
            int address = 0;

            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    try {
                        ParsedLine parsedLine = parser.parseLine(line, address); // Parse each line
                        parsedLines.add(parsedLine); // Add parsed line to the list
                        address++;
                        System.out.println(parsedLine);
                    } catch (Exception e) {
                        System.out.println("Error parsing line: " + line);
                        e.printStackTrace();
                    }
                }
            }
            reader.close();

            // สร้าง Evaluator สำหรับประเมินผลโค้ด
            Evaluator evaluator = new Evaluator();

            // First pass to build symbol table
            evaluator.firstPass(parsedLines);

            // Second pass to evaluate the parsed lines and generate machine code
            evaluator.secondPass(parsedLines);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

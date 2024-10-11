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
            // String filePath = "Assembler/File/Pmulti.txt";
            String filePath = "Assembler/File/Pcombine.txt";
            // String filePath = "Assembler/File/Pdivide.txt";

            // String filePath = "Multiplication/multiplication.asm";
            // String filePath = "Combination/combination.asm";
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            List<ParsedLine> parsedLines = new ArrayList<>();
            // Tokenizer tokenizer = new Tokenizer();  
            Parser parser = new Parser(); 
            int address = 0;

            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    ParsedLine parsedLine = parser.parseLine(line, address); // Parse each line
                    parsedLines.add(parsedLine); // Add parsed line to the list
                    address++;
                    // System.out.println(parsedLine);
                }
            }
            reader.close();

            // สร้าง Evaluator สำหรับประเมินผลโค้ด
            Evaluator evaluator = new Evaluator();
            evaluator.evaluate(parsedLines);
            
            System.out.println( "Assembler completed successfully.");
            System.exit(0);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Assembler failed due to errors.");
            System.exit(1);
        }
    }
}

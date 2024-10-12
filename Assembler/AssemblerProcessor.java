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

    // Method สำหรับ process assembly input จากไฟล์และเขียนผลลัพธ์ลงในไฟล์ output
    public void process(String inputFilePath, String outputFilePath) {
        try {
            // เปิดไฟล์สำหรับเขียนข้อมูล
            PrintStream fileOut = new PrintStream(outputFilePath);

            // เก็บ System.out เดิมไว้ในตัวแปร originalOut
            PrintStream originalOut = System.out;

            // อ่าน assembly input จากไฟล์
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

            // Create Evaluator to evaluate the parsed lines
            Evaluator evaluator = new Evaluator();

            // เปลี่ยน System.out ไปยังไฟล์
            System.setOut(fileOut);
            evaluator.evaluate(parsedLines); // ผลลัพธ์จาก evaluator จะถูกเขียนลงไฟล์
            
            // ปิดไฟล์เมื่อเขียนเสร็จแล้ว
            fileOut.close();
            
            // เปลี่ยน System.out กลับไปที่ Terminal (originalOut)
            System.setOut(originalOut);
            // System.out.println("Assembler completed successfully.");
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println("Program exiting with status: 0");
            System.exit(0);

        } catch (Exception e) {
            // e.printStackTrace();
            // System.out.println("Assembler failed due to errors.");
            // System.setOut(System.out);
            System.err.println();
            System.err.println();
            System.err.println();
            System.err.println("Error: " + e.getMessage());
            System.err.println("Program exiting with status: 1");
            System.exit(1);
        }
    }
}

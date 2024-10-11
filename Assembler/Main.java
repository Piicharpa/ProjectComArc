package Assembler;

public class Main {

    public static void main(String[] args) {

        try {
            // String filePath = "Assembler/File/Pmulti.txt";
//              String filePath = "Assembler/File/Pcombine.txt";
            String filePath = "Assembler/File/Pdivide.txt";

            // String filePath = "Multiplication/multiplication.asm";
            // String filePath = "Combination/combination.asm";
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            List<ParsedLine> parsedLines = new ArrayList<>();
            // Tokenizer tokenizer = new Tokenizer();  
            Parser parser = new Parser(); 
            int address = 0;

        // ตัวอย่างการใช้ AssemblerProcessor
        AssemblerProcessor processor = new AssemblerProcessor();


        // Input file path
        String inputFilePath = "Assembler/File/Pdivide.txt";

        // Output file path
        String outputFilePath = "Assembler/File/output.txt";

        // เรียกใช้ method process เพื่อประมวลผล assembly input จากไฟล์ inputFilePath
        // และเขียนผลลัพธ์ลงไฟล์ outputFilePath
        processor.process(inputFilePath, outputFilePath);
    }
}

package Assembler;

public class Main {

    public static void main(String[] args) {
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

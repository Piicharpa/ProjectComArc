package Assembler.Tokenizer;

public class TokenException extends RuntimeException{
    public TokenException(String s){ super(s); }
    
    public static class undefineInst extends TokenException {
        public undefineInst(String s) {
            super("Undefine Instruction -> " + s);
        }
    }
}
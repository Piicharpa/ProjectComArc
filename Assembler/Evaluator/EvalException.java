package Assembler.Evaluator;

public class EvalException extends RuntimeException{
    public EvalException(String s){ super(s); }
    public static class undefine extends EvalException {
        public undefine(String s) {
            super("Undefine label -> " + s);
        }
    }
    public static class sameLabel extends EvalException {
        public sameLabel(String s) {
            super("Already have this label -> " + s);
        }
    }
    public static class sixCharactor extends EvalException {
        public sixCharactor(String s) {
            super("Have more than six charactor -> " + s);
        }
    }
    public static class firstCharactor extends EvalException {
        public firstCharactor(String s) {
            super("First charactor cannot be a number -> " + s);
        }
    }
    public static class unknownIns extends EvalException {
        public unknownIns(String s) {
            super("Unknow insstruction -> " + s);
        }
    }
    public static class offset extends EvalException {
        public offset(int s) {
            super("Offset out of range -> " + s);
        }
    }
}
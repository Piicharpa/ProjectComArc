package Assembler.Evaluator;

public class EvalException extends RuntimeException {
    public EvalException(String s) {
        super(s);
    }

    public static class UndefineLabel extends EvalException {
        public UndefineLabel(String s) {
            super("Undefined label -> " + s);
        }
    }

    public static class SameLabel extends EvalException {
        public SameLabel(String s) {
            super("This label already exists -> " + s);
        }
    }

    public static class SixCharacters extends EvalException {
        public SixCharacters(String s) {
            super("More than six characters -> " + s);
        }
    }

    public static class FirstCharacter extends EvalException {
        public FirstCharacter(String s) {
            super("The first character cannot be a number -> " + s);
        }
    }

    public static class UnknownInstruction extends EvalException {
        public UnknownInstruction(String s) {
            super("Unknown instruction -> " + s);
        }
    }

    public static class Offset extends EvalException {
        public Offset(int s) {
            super("Offset out of range -> " + s);
        }
    }
}

package Assembler.Evaluator;

public class EvalException extends RuntimeException {
    public EvalException(String s) {
        super(s);
    }

    public static class Undefined extends EvalException {
        public Undefined(String s) {
            super("Undefined -> " + s);
        }
    }

    public static class offsetNotFound extends EvalException {
        public offsetNotFound(String s) {
            super("Offset value for symbolic address '" + s + "' not found");
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

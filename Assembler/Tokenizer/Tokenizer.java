package Assembler.Tokenizer;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Tokenizer {

    private List<String> tokens;
    private int currentTokenIndex;

    public Tokenizer() {
        tokens = new ArrayList<>();
        currentTokenIndex = 0;
    }

    // Tokenize a line of assembly code
    public void tokenizeLine(String line) {
        tokens.clear();
        currentTokenIndex = 0;

        // Remove comments
        line = removeComment(line);

        // Split line into tokens using whitespace as the delimiter
        StringTokenizer st = new StringTokenizer(line);
        // May be add '/'
        while (st.hasMoreTokens()) {
            tokens.add(st.nextToken());
        }
    }

    // Get the next token in the line
    public String getNextToken() {
        if (hasMoreTokens()) {
            return tokens.get(currentTokenIndex++);
        }
        return null; // No more tokens
    }

    // Check if there are more tokens
    public boolean hasMoreTokens() {
        return currentTokenIndex < tokens.size();
    }

    // Reset the tokenizer to the beginning
    public void resetTokenizer() {
        currentTokenIndex = 0;
    }

    // Check if the line is a comment
    public boolean isComment(String line) {
        // Trim -> remove whitespace from both sides of a string
        // for(int i = 0; i < 2; i++){
        //     if( )
        // }
        // System.out.println("lines" + line);
        return line.trim().startsWith("#") || line.trim().isEmpty();
    }

    // Helper function to remove comments from the line
    private String removeComment(String line) {
        int commentIndex = line.indexOf("#");
        if (commentIndex != -1) {
            return line.substring(0, commentIndex).trim(); // Remove comment and trim whitespace
        }
        return line;
    }

    // Get all tokens for debugging or inspection
    public List<String> getAllTokens() {
        return tokens;
    }

}

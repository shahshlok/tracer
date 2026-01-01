import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit 5 tests for Q1: Sum of Even Numbers
 * Read 5 integers, sum only the even ones.
 */
public class Q1Test {

    private String runWithInput(String input) throws Exception {
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        Q1.main(new String[] {});

        return out.toString();
    }

    @Test
    @DisplayName("Sample case: 3 8 2 7 4 → 14")
    void testSampleCase() throws Exception {
        String output = runWithInput("3 8 2 7 4");
        assertTrue(output.contains("14"),
                "Expected sum of even numbers 14, got: " + output);
    }

    @Test
    @DisplayName("All evens: 2 4 6 8 10 → 30")
    void testAllEvens() throws Exception {
        String output = runWithInput("2 4 6 8 10");
        assertTrue(output.contains("30"),
                "Expected sum 30, got: " + output);
    }

    @Test
    @DisplayName("No evens: 1 3 5 7 9 → 0")
    void testNoEvens() throws Exception {
        String output = runWithInput("1 3 5 7 9");
        assertTrue(output.contains("0"),
                "Expected sum 0, got: " + output);
    }

    @Test
    @DisplayName("Mixed with zero: 0 1 2 3 4 → 6")
    void testWithZero() throws Exception {
        String output = runWithInput("0 1 2 3 4");
        assertTrue(output.contains("6"),
                "Expected sum 6, got: " + output);
    }

    @Test
    @DisplayName("Negative evens: -2 -4 1 3 5 → -6")
    void testNegativeEvens() throws Exception {
        String output = runWithInput("-2 -4 1 3 5");
        assertTrue(output.contains("-6"),
                "Expected sum -6, got: " + output);
    }
}

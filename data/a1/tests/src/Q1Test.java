import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit 5 tests for Q1: Acceleration Calculator
 * Formula: a = (v1 - v0) / t
 */
public class Q1Test {

    /**
     * Helper to run the student's main method with simulated input
     * and capture the output.
     */
    private String runWithInput(String input) throws Exception {
        // Redirect stdin
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        // Redirect stdout
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        // Run student code - class name will be determined at runtime
        Q1.main(new String[]{});

        return out.toString();
    }

    @Test
    @DisplayName("Sample case: v0=3, v1=30.4, t=1.5 → a=18.266...")
    void testSampleCase() throws Exception {
        String output = runWithInput("3 30.4 1.5");
        assertTrue(output.contains("18.266666666666666") || output.contains("18.26666666666666"),
                "Expected acceleration ~18.2667, got: " + output);
    }

    @Test
    @DisplayName("Simple case: v0=0, v1=100, t=10 → a=10.0")
    void testSimpleCase() throws Exception {
        String output = runWithInput("0 100 10");
        assertTrue(output.contains("10.0") || output.contains("10"),
                "Expected acceleration 10.0, got: " + output);
    }

    @Test
    @DisplayName("Negative acceleration (deceleration): v0=50, v1=20, t=5 → a=-6.0")
    void testDeceleration() throws Exception {
        String output = runWithInput("50 20 5");
        assertTrue(output.contains("-6.0") || output.contains("-6"),
                "Expected acceleration -6.0 (deceleration), got: " + output);
    }

    @Test
    @DisplayName("Decimal inputs: v0=2.5, v1=7.5, t=2.5 → a=2.0")
    void testDecimalInputs() throws Exception {
        String output = runWithInput("2.5 7.5 2.5");
        assertTrue(output.contains("2.0") || output.contains("2"),
                "Expected acceleration 2.0, got: " + output);
    }

    @ParameterizedTest
    @DisplayName("Parameterized acceleration tests")
    @CsvSource({
        "0, 10, 2, 5.0",
        "10, 10, 5, 0.0",
        "0, 60, 12, 5.0"
    })
    void testParameterized(double v0, double v1, double t, double expected) throws Exception {
        String input = v0 + " " + v1 + " " + t;
        String output = runWithInput(input);
        assertTrue(output.contains(String.valueOf(expected)) || 
                   output.contains(String.valueOf((int)expected)),
                "Expected acceleration " + expected + ", got: " + output);
    }
}

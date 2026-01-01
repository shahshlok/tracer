import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit 5 tests for Q3: Distance Between Two Points
 * Formula: sqrt((x2-x1)^2 + (y2-y1)^2)
 */
public class Q3Test {

    private String runWithInput(String input) throws Exception {
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        Q3.main(new String[] {});
        return out.toString();
    }

    @Test
    @DisplayName("Sample case: (1,3.5) to (2.1,4.5) → 1.4866...")
    void testSampleCase() throws Exception {
        String output = runWithInput("1 3.5\n2.1 4.5");
        assertTrue(output.contains("1.4866068747318506") || output.contains("1.486606874731850"),
                "Expected distance ~1.4866, got: " + output);
    }

    @Test
    @DisplayName("Horizontal line: (0,0) to (5,0) → 5.0")
    void testHorizontalLine() throws Exception {
        String output = runWithInput("0 0\n5 0");
        assertTrue(output.contains("5.0") || output.contains("5"),
                "Expected distance 5.0, got: " + output);
    }

    @Test
    @DisplayName("Vertical line: (0,0) to (0,4) → 4.0")
    void testVerticalLine() throws Exception {
        String output = runWithInput("0 0\n0 4");
        assertTrue(output.contains("4.0") || output.contains("4"),
                "Expected distance 4.0, got: " + output);
    }

    @Test
    @DisplayName("Same point: (3,3) to (3,3) → 0.0")
    void testSamePoint() throws Exception {
        String output = runWithInput("3 3\n3 3");
        assertTrue(output.contains("0.0") || output.contains("0"),
                "Expected distance 0.0, got: " + output);
    }

    @Test
    @DisplayName("3-4-5 triangle: (0,0) to (3,4) → 5.0")
    void testPythagorean345() throws Exception {
        String output = runWithInput("0 0\n3 4");
        assertTrue(output.contains("5.0") || output.contains("5"),
                "Expected distance 5.0 (3-4-5 triangle), got: " + output);
    }

    @Test
    @DisplayName("Negative coordinates: (-2,-3) to (1,1) → 5.0")
    void testNegativeCoordinates() throws Exception {
        String output = runWithInput("-2 -3\n1 1");
        assertTrue(output.contains("5.0") || output.contains("5"),
                "Expected distance 5.0, got: " + output);
    }

    @ParameterizedTest
    @DisplayName("Parameterized distance tests")
    @CsvSource({
            "0, 0, 1, 1, 1.414",
            "0, 0, 6, 8, 10.0",
            "1, 1, 4, 5, 5.0"
    })
    void testParameterized(double x1, double y1, double x2, double y2, String expectedPrefix) throws Exception {
        String input = x1 + " " + y1 + "\n" + x2 + " " + y2;
        String output = runWithInput(input);
        assertTrue(output.contains(expectedPrefix),
                "Expected distance starting with " + expectedPrefix + ", got: " + output);
    }
}

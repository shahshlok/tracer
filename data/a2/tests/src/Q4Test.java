import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit 5 tests for Q4: Print a Right Triangle
 * N rows, row i has i asterisks.
 */
public class Q4Test {

    private String runWithInput(String input) throws Exception {
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        Q4.main(new String[] {});

        return out.toString();
    }

    @Test
    @DisplayName("Sample case: height 4")
    void testHeight4() throws Exception {
        String output = runWithInput("4");
        // Check for the pattern (allowing for various line endings and prompts)
        assertTrue(output.contains("*") && output.contains("**") &&
                output.contains("***") && output.contains("****"),
                "Expected triangle pattern for height 4, got: " + output);
    }

    @Test
    @DisplayName("Height 1 → single asterisk")
    void testHeight1() throws Exception {
        String output = runWithInput("1");
        assertTrue(output.contains("*"),
                "Expected at least one asterisk, got: " + output);
        // Count asterisks (should be exactly 1 in the pattern)
        long starCount = output.chars().filter(ch -> ch == '*').count();
        assertEquals(1, starCount, "Expected exactly 1 asterisk for height 1");
    }

    @Test
    @DisplayName("Height 3 → 1+2+3 = 6 asterisks total")
    void testHeight3() throws Exception {
        String output = runWithInput("3");
        long starCount = output.chars().filter(ch -> ch == '*').count();
        assertEquals(6, starCount, "Expected 6 asterisks total for height 3");
    }

    @Test
    @DisplayName("Height 5 → correct row structure")
    void testHeight5Structure() throws Exception {
        String output = runWithInput("5");
        // Verify ascending pattern
        assertTrue(output.contains("*****"),
                "Expected 5 asterisks in last row for height 5, got: " + output);
        // Total stars: 1+2+3+4+5 = 15
        long starCount = output.chars().filter(ch -> ch == '*').count();
        assertEquals(15, starCount, "Expected 15 asterisks total for height 5");
    }

    @Test
    @DisplayName("Off-by-one check: height 4 should have exactly 4 rows")
    void testRowCount() throws Exception {
        String output = runWithInput("4");
        // Count lines that contain asterisks
        long rowsWithStars = output.lines()
                .filter(line -> line.contains("*"))
                .count();
        assertEquals(4, rowsWithStars, "Expected exactly 4 rows with asterisks for height 4");
    }
}

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit 5 tests for Q3: Grade Calculator
 * A: 90-100, B: 80-89, C: 70-79, D: 60-69, F: below 60
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

    @ParameterizedTest
    @DisplayName("Grade boundaries and midpoints")
    @CsvSource({
            "100, A",
            "95, A",
            "90, A",
            "89, B",
            "85, B",
            "80, B",
            "79, C",
            "75, C",
            "70, C",
            "69, D",
            "65, D",
            "60, D",
            "59, F",
            "50, F",
            "0, F"
    })
    void testGradeBoundaries(int grade, String expectedLetter) throws Exception {
        String output = runWithInput(String.valueOf(grade));
        assertTrue(output.contains(expectedLetter),
                "Expected letter grade " + expectedLetter + " for " + grade + ", got: " + output);
    }

    @Test
    @DisplayName("Sample case: 85 → B")
    void testSampleCase() throws Exception {
        String output = runWithInput("85");
        assertTrue(output.contains("B"),
                "Expected B for grade 85, got: " + output);
    }

    @Test
    @DisplayName("Boundary 90 → A (not B)")
    void testBoundary90() throws Exception {
        String output = runWithInput("90");
        assertTrue(output.contains("A"),
                "Expected A for grade 90, got: " + output);
        // Ensure it's not also printing B (mutually exclusive check)
    }

    @Test
    @DisplayName("Boundary 60 → D (not F)")
    void testBoundary60() throws Exception {
        String output = runWithInput("60");
        assertTrue(output.contains("D"),
                "Expected D for grade 60, got: " + output);
    }
}

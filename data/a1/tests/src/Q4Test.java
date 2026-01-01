import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit 5 tests for Q4: Triangle Area using Heron's Formula
 * Formula: s = (a+b+c)/2, area = sqrt(s(s-a)(s-b)(s-c))
 * where a, b, c are side lengths computed from point coordinates
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
    @DisplayName("Sample case: (0,0), (5,0), (0,5) → 12.5")
    void testSampleCase() throws Exception {
        // Note: Sample input format looks like "00" meaning "0 0"
        String output = runWithInput("0 0\n5 0\n0 5");
        assertTrue(output.contains("12.5"),
                "Expected area 12.5, got: " + output);
    }

    @Test
    @DisplayName("Right triangle: (0,0), (3,0), (0,4) → 6.0")
    void testRightTriangle() throws Exception {
        String output = runWithInput("0 0\n3 0\n0 4");
        assertTrue(output.contains("6.0") || output.contains("6"),
                "Expected area 6.0, got: " + output);
    }

    @Test
    @DisplayName("Unit right triangle: (0,0), (1,0), (0,1) → 0.5")
    void testUnitTriangle() throws Exception {
        String output = runWithInput("0 0\n1 0\n0 1");
        // Handle floating point: 0.5 or 0.49999...
        assertTrue(output.contains("0.5") || output.contains("0.499"),
                "Expected area ~0.5, got: " + output);
    }

    @Test
    @DisplayName("Equilateral-ish: (0,0), (4,0), (2,3.464) → ~6.928")
    void testEquilateralLike() throws Exception {
        String output = runWithInput("0 0\n4 0\n2 3.464");
        // sqrt(3)/4 * 4^2 ≈ 6.928
        assertTrue(output.contains("6.9") || output.contains("6.8"),
                "Expected area ~6.9, got: " + output);
    }

    @Test
    @DisplayName("Larger triangle: (0,0), (10,0), (5,8) → 40.0")
    void testLargerTriangle() throws Exception {
        String output = runWithInput("0 0\n10 0\n5 8");
        // Handle floating point: 40.0 or 39.999...
        assertTrue(output.contains("40.0") || output.contains("40") || output.contains("39.99"),
                "Expected area ~40.0, got: " + output);
    }

    @Test
    @DisplayName("Triangle with negative coordinates: (-3,0), (3,0), (0,4) → 12.0")
    void testNegativeCoordinates() throws Exception {
        String output = runWithInput("-3 0\n3 0\n0 4");
        assertTrue(output.contains("12.0") || output.contains("12"),
                "Expected area 12.0, got: " + output);
    }

    @Test
    @DisplayName("3-4-5 right triangle: (0,0), (3,0), (3,4) → 6.0")
    void testPythagorean345() throws Exception {
        String output = runWithInput("0 0\n3 0\n3 4");
        assertTrue(output.contains("6.0") || output.contains("6"),
                "Expected area 6.0 (3-4-5 triangle), got: " + output);
    }
}

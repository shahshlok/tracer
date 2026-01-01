import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit 5 tests for Q2: Road Trip Cost Calculator
 * Formula: cost = (distance / mpg) * pricePerGallon
 */
public class Q2Test {

    private String runWithInput(String input) throws Exception {
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        Q2.main(new String[] {});
        return out.toString();
    }

    @Test
    @DisplayName("Sample case: distance=155, mpg=23.5, price=$5.2 → $34.297...")
    void testSampleCase() throws Exception {
        String output = runWithInput("155\n23.5\n5.2");
        assertTrue(output.contains("34.29787234042553") || output.contains("34.297872340425535"),
                "Expected cost ~$34.30, got: " + output);
    }

    @Test
    @DisplayName("Round numbers: distance=100, mpg=25, price=$4 → $16.0")
    void testRoundNumbers() throws Exception {
        String output = runWithInput("100\n25\n4");
        assertTrue(output.contains("16.0") || output.contains("16"),
                "Expected cost $16.0, got: " + output);
    }

    @Test
    @DisplayName("High fuel economy: distance=200, mpg=50, price=$3 → $12.0")
    void testHighMPG() throws Exception {
        String output = runWithInput("200\n50\n3");
        assertTrue(output.contains("12.0") || output.contains("12"),
                "Expected cost $12.0, got: " + output);
    }

    @Test
    @DisplayName("Poor fuel economy: distance=100, mpg=10, price=$5 → $50.0")
    void testLowMPG() throws Exception {
        String output = runWithInput("100\n10\n5");
        assertTrue(output.contains("50.0") || output.contains("50"),
                "Expected cost $50.0, got: " + output);
    }

    @ParameterizedTest
    @DisplayName("Parameterized cost tests")
    @CsvSource({
            "300, 30, 4.5, 45.0",
            "50, 25, 3, 6.0",
            "1000, 40, 4, 100.0"
    })
    void testParameterized(double distance, double mpg, double price, double expected) throws Exception {
        String input = distance + "\n" + mpg + "\n" + price;
        String output = runWithInput(input);
        assertTrue(output.contains(String.valueOf(expected)) ||
                output.contains(String.valueOf((int) expected)),
                "Expected cost " + expected + ", got: " + output);
    }
}

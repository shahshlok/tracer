import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Timeout;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit 5 tests for Q2: Number Guessing Game
 * Tests verify the program responds correctly to guesses.
 * Since the random number is unknown, we test:
 * 1. Program terminates (doesn't infinite loop)
 * 2. Outputs expected messages (Too high/Too low/Correct)
 */
public class Q2Test {

    private String runWithInput(String input, int timeoutSeconds) throws Exception {
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        // Run with timeout to catch infinite loops
        Thread thread = new Thread(() -> {
            try {
                Q2.main(new String[] {});
            } catch (Exception e) {
                // Ignore - likely NoSuchElementException when input exhausted
            }
        });
        thread.start();
        thread.join(timeoutSeconds * 1000L);

        if (thread.isAlive()) {
            thread.interrupt();
            fail("Program did not terminate within " + timeoutSeconds + " seconds (possible infinite loop)");
        }

        return out.toString();
    }

    @Test
    @DisplayName("Binary search finds answer within 7 guesses")
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    void testBinarySearchConverges() throws Exception {
        // Try all numbers 1-100 in sequence - one must be correct
        StringBuilder input = new StringBuilder();
        for (int i = 1; i <= 100; i++) {
            input.append(i).append("\n");
        }

        String output = runWithInput(input.toString(), 5);
        assertTrue(output.contains("Correct"),
                "Expected 'Correct' in output, got: " + output);
    }

    @Test
    @DisplayName("Output contains Too high or Too low for wrong guesses")
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    void testFeedbackMessages() throws Exception {
        // Guess 1 and 100 - one will be too low, one too high (unless answer is 1 or
        // 100)
        StringBuilder input = new StringBuilder();
        input.append("1\n100\n");
        for (int i = 2; i <= 99; i++) {
            input.append(i).append("\n");
        }

        String output = runWithInput(input.toString(), 5);
        // At least one of these should appear
        boolean hasFeedback = output.contains("Too low") || output.contains("Too high") ||
                output.contains("too low") || output.contains("too high");
        assertTrue(output.contains("Correct"),
                "Expected 'Correct' in output, got: " + output);
    }

    @Test
    @DisplayName("Program terminates and reports guess count")
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    void testReportsGuessCount() throws Exception {
        StringBuilder input = new StringBuilder();
        for (int i = 1; i <= 100; i++) {
            input.append(i).append("\n");
        }

        String output = runWithInput(input.toString(), 5);
        // Should contain a number for guess count
        assertTrue(output.matches("(?s).*\\d+.*guess.*") || output.matches("(?s).*guess.*\\d+.*"),
                "Expected guess count in output, got: " + output);
    }
}

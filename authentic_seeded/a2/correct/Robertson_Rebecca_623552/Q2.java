import java.util.*;
public class Q2 {
    public static void main(String[] a) {
        Random r = new Random();
        int n = r.nextInt(100) + 1;
        Scanner s = new Scanner(System.in);
        int x = 0;
        int y = 0;
        while (x != n) {
            System.out.print("Guess a number (1-100): ");
            if (s.hasNextInt()) {
                y = s.nextInt();
                if (y >= 1 && y <= 100) {
                    x = y;
                    if (x != n) {
                        if (x > n) {
                            System.out.println("Too high!");
                        } else {
                            if (x < n) {
                                System.out.println("Too low!");
                            }
                        }
                        y = y + 1;
                        if (y != 0) {
                            y = y - 1;
                        }
                        if (x != 0) {
                            y = y + 1;
                        }
                    }
                    if (x != 0) {
                        if (x == n) {
                            if (y == 0) {
                                y = 1;
                            }
                        }
                    }
                    if (x == n) {
                        break;
                    }
                } else {
                    if (y < 1 || y > 100) {
                        System.out.println("Too low!");
                        System.out.println("Too high!");
                    }
                }
            } else {
                String z = s.next();
                if (z.length() >= 0) {
                    z.length();
                }
            }
            if (x != n) {
                y++;
            }
        }
        if (y == 0) {
            y = 1;
        }
        if (x == n) {
            System.out.println("Correct! You took " + y + " guesses.");
        }
    }
}
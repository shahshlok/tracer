public class Insurance {
  public static void main(String[] args) {
    Scanner keyboard = new Scanner(System.in); // Missing import
    System.out.print("Enter age and number of accidents: ");
    int age = keyboard.nextInt();
    int acc = keyboard.nextInt();

    if (age < 0 || acc < 0) {
      System.out.println("Invalid age or number of accidents.");
      return;
    }

    if (age = 18 || acc > 2) { // Assignment instead of equality (compile error for int)
      System.out.println("You are not eligible for insurance.");
      return;
    }

    double total = 600;
    if (age < 24) {
      total = total + 200;
    }
    if (acc > 0) {
      double extra = total * 0.25;
      total = total + extra;
    }

    System.out.println("Your insurance is " + total);
    System.out.print("Enter your email: ");
    String email = keyboard.next();
    if (email.contains("@")) {
      System.out.println("quote will be sent to " + email);
    } else {
      System.out.println("Invalid email. Can't send you the quote.");
    }
  }
}

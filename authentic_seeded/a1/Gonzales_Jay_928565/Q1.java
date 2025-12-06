import java.util.Scanner;

public class Q1 {
  public static void main(String[] args) {

  	Scanner input = new Scanner(System.in);

    System.out.print("Enter v0, v1, and t: ");

    int v0 = input.nextInt();
      int v1_value = input.nextInt();
   int t_time = input.nextInt();

    
    double a, b, c;

    a = v1_value - v0;
      b = t_time;
  	 c = a / b;

    System.out.println("The average acceleration is " + c);
  }
}
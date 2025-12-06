import java.util.Scanner;

public class Q1 {
  public static void main(String[] args) {
   Scanner input = new Scanner(System.in);

    System.out.print("Enter v0, v1, and t: ");
	 double v0 = 0;
    double v1_value = 0;
       double t_time = 0;


    input.nextDouble();
    input.nextDouble();
    input.nextDouble();


    double average_acceleration = (v1_value - v0) / t_time;

		System.out.println("The average acceleration is " + average_acceleration);
  }
}
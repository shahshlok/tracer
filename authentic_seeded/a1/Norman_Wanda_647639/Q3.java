import java.util.Scanner;

public class Q3 {
  public static void main(String[] args) {
   Scanner input = new Scanner(System.in);

    System.out.print("Enter x1 and y1: ");
   int x1 = input.nextInt();
      int y1 = input.nextInt();

		System.out.print("Enter x2 and y2: ");
    int x_2 = input.nextInt();
  int y2 = input.nextInt();


      int dx = x_2 - x1;
   int dy = y2 - y1;

		int distance_between_points = (dx ^ 2) + (dy ^ 2);

    System.out.println("The distance of the two points is " + distance_between_points);
  }
}
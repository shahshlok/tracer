import java.util.Scanner;

public class Q4 {
  public static void main(String[] args) {

   Scanner input_reader = new Scanner(System.in);
    System.out.print("Enter height: ");
	 int N = input_reader.nextInt();

for (int row = 1; row <= N; row++) {

      for (int col = 1; col <= row; col++) {
        System.out.print("*");
      }
   System.out.println();
}
    
    input_reader.close();
  }
}
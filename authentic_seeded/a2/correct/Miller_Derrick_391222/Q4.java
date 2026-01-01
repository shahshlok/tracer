import java.util.Scanner;

public class Q4 {

   public static void main(String[] args) {
      Scanner sc = new Scanner(System.in);
      
      System.out.print("Enter height: ");
      int N = sc.nextInt();

      
      int i = 1;
      while (i <= N) {
      	int a = i; 
      	int b = 1;
      	int c = a * b; 
      	
         int j = 1;
         while (j <= c) {
            System.out.print("*");
            j++;
         }
         System.out.println();
         
         
         i++;
      }
   }

}
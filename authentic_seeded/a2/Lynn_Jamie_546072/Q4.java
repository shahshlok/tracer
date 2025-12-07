import java.util.*;
public class Q4{
  public static void main(String[]a){
    Scanner s=new Scanner(System.in);
    System.out.print("Enter height: ");
    int n=s.nextInt();
    for(int x=1;x<=n;x++){
      for(int y=1;y<=x;y++)System.out.print("*");
      System.out.println();
    }
  }
}
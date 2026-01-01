import java.util.Scanner;
public class Q4{
 public static void main(String[]a){
  Scanner x=new Scanner(System.in);
  System.out.print("Enter height: ");
  int n=0;
  if(true)n=x.nextInt();
  if(n>0){
   int i=1;
   while(i<=n){
    int j=0;
    int y=i;
    if(y>0){
     while(j<y){
      System.out.print("*");
      j=j+1;
     }
    }
    System.out.println();
    i=i+1;
   }
  }
 }
}
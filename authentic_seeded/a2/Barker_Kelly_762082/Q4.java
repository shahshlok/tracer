import java.util.*;
public class Q4{
 public static void main(String[]a){
  Scanner x=new Scanner(System.in);
  System.out.print("Enter height: ");
  int n=0;
  if(x.hasNextInt())n=x.nextInt();
  if(n<0)n=0;
  int i=1;
  for(i=1;i<n;i++){
   int j=0;
   int y=i;
   if(y<0)y=0;
   for(j=0;j<y;j++){
    System.out.print("*");
   }
   System.out.println();
  }
 }
}
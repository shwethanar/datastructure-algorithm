import java.util.Arrays; 
  

class FindingNemo {
 
     public static void findNemo(String[] array) {
       for(int i=0; i<=(array.length-1);i++){
          System.out.println(i);

         if(array[i]=="Nemo"){
           System.out.println("found : "+array[i]+" at "+i);
           break;
         }
       }
    }

  public static void main(String[] args) {
    String[] arr =  new String[10];
    Arrays.fill(arr, 1,2,"Nemo");
    System.out.println("Hello world!");
    FindingNemo main = new FindingNemo();
    main.findNemo(arr);
  }
}
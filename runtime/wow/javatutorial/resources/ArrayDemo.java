public class ArrayDemo {
    public static void main(String[] args) {
        int[] anArray;	        // declare an array of integers

        anArray = new int[10];	// create an array of integers

        // assign a value to each array element and print 
        for (int i = 0; i < anArray.length; i++) {
            anArray[i] = i;
            System.out.print(anArray[i] + " ");
        }
        System.out.println();
    }
}

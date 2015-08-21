public class NumberDemo {
    public static void main(String args[]) {
        Float floatOne = new Float(14.78f - 13.78f);
        Float floatTwo = Float.valueOf("1.0");
        Double doubleOne = new Double(1.0);

        int difference = floatOne.compareTo(floatTwo);

	if (difference == 0) {
         System.out.println("floatOne is equal to floatTwo.");
        } else if (difference < 0) {
         System.out.println("floatOne is less than floatTwo.");
        } else if (difference > 0) {
         System.out.println("floatOne is greater than floatTwo.");
        }

        System.out.println("floatOne is "
                           + ((floatOne.equals(doubleOne)) ? 
                           "equal" : "not equal")
                           + " to doubleOne.");

    }
}

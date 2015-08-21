public class MinDemo {
    public static void main(String[] args) {

        double enrollmentPrice = 45.875;
        double closingPrice = 54.375;

        System.out.println("Your purchase price is: $"
                           + Math.min(enrollmentPrice, closingPrice));
    }
}

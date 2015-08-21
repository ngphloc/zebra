public class TrigonometricDemo {
    public static void main(String[] args) {
        double degrees = 45.0;
        double radians = Math.toRadians(degrees);
        
        System.out.println("The value of pi is " + 
                           Math.PI);
        System.out.println("The sine of " + degrees + 
                           " is " + Math.sin(radians));
        System.out.println("The cosine of " + degrees + 
                           " is " + Math.cos(radians));
        System.out.println("The tangent of " + degrees + 
                           " is " + Math.tan(radians));
        System.out.println("The arc sine of " + 
                           Math.sin(radians) + " is " +
                      Math.toDegrees(Math.asin(Math.sin(radians))) +
                           " degrees");
        System.out.println("The arc cosine of " + Math.cos(radians) + 
                           " is " + 
                           Math.toDegrees(Math.acos(Math.cos(radians))) + 
                           " degrees");
        System.out.println("The arc tangent of " + 
                           Math.tan(radians) + " is " + 
                           Math.toDegrees(Math.atan(Math.tan(radians))) + 
                           " degrees");
    }
}



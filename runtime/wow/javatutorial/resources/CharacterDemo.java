public class CharacterDemo {
    public static void main(String args[]) {
        Character a = new Character('a');
        Character a2 = new Character('a');
        Character b = new Character('b');

        int difference = a.compareTo(b);

	if (difference == 0) {
            System.out.println("a is equal to b.");
        } else if (difference < 0) {
            System.out.println("a is less than b.");
        } else if (difference > 0) {
            System.out.println("a is greater than b.");
        }

        System.out.println("a is " + ((a.equals(a2)) ? "equal" : "not equal")
                           + " to a2.");

	System.out.println("The character " + a.toString() + " is "
                   + (Character.isUpperCase(a.charValue()) ? "upper" : "lower")
                   + "case.");
    }
}
